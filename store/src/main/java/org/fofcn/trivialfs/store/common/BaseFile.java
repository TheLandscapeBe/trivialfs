package org.fofcn.trivialfs.store.common;

import lombok.extern.slf4j.Slf4j;
import org.fofcn.trivialfs.common.FilePaddingUtil;
import org.fofcn.trivialfs.common.R;
import org.fofcn.trivialfs.common.RWrapper;
import org.fofcn.trivialfs.store.common.constant.StoreConstant;
import org.fofcn.trivialfs.store.common.enums.ReadWriteState;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 存储文件
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/03/24 15:10
 */
@Slf4j
public class BaseFile {
    /**
     * 文件路径
     */
    private final File file;

    /**
     * 文件句柄
     */
    private volatile RandomAccessFile rasFile;

    /**
     * 文件管道
     */
    private FileChannel fileChannel;

    /**
     * 当前写入位置,初始化为4096
     */
    private final AtomicLong writePos = new AtomicLong(4096L);

    /**
     * 预分配位置
     */
    private final AtomicLong paddingPos = new AtomicLong(0L);

    /**
     * 读写锁
     */
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /**
     * 自动扩展大小
     */
    private final long autoExpandSize;

    /**
     * 块最大大小
     */
    private final long maxBlockFileSize;

    /**
     * 读写状态，初始化为可写
     */
    private final AtomicReference<ReadWriteState> readWriteStateRef = new AtomicReference<>(ReadWriteState.WRITEABLE);

    public BaseFile(final File file, final long autoExpandSize, final long maxBlockFileSize) {
        this.file = file;
        this.autoExpandSize = autoExpandSize;
        this.maxBlockFileSize = maxBlockFileSize;
    }

    /**
     * 验证文件是否为存储文件
     * @return true:是，false:否
     */
    public boolean valid() {
        try {
            long magic = rasFile.readLong();
            if (StoreConstant.INDEX_SUPER_MAGIC_NUMBER == magic) {
                return true;
            }
        } catch (IOException e) {
            log.error("read file error");
            return false;
        }

        return false;
    }

    /**
     * 文件初始化
     */
    public boolean init() {
        doBeforeInit();
        boolean newFile = false;
        try {
            if (!file.getParentFile().exists()) {
                Files.createDirectories(Paths.get(file.getParentFile().getAbsolutePath()));
                newFile = true;
            }

            if (!file.exists()) {
                Files.createFile(Paths.get(file.getAbsolutePath()));
                newFile = true;
            }

            this.rasFile = new RandomAccessFile(file, "rw");
            fileChannel = rasFile.getChannel();
            // 如果是新文件，那么执行新文件初始化
            if (newFile) {
                doInitNewFile();
            } else {
                doInitOldFile();
                doRecover();
            }
            // 刷盘
            fileChannel.force(false);
            doAfterInit();
        } catch (IOException e) {
            throw new IllegalArgumentException("file not found, file path: " + file.getAbsolutePath());
        }

        return true;
    }

    /**
     * 关闭
     */
    public void close() {
        try {
            fileChannel.close();
            rasFile.close();
        } catch (IOException e) {
            log.error("random access file close error.", e);
        }
    }

    /**
     * 追加文件
     * @param buffer
     * @return 追加结果
     */
    public R<AppendResult> append(ByteBuffer buffer) {
        return doAppend(-1L, buffer);
    }

    /**
     * 指定偏移追加文件
     * @param buffer 内容
     * @param offset 偏移
     * @return 追加结果
     */
    public R<AppendResult> append(long offset, ByteBuffer buffer) {
        return doAppend(offset, buffer);
    }

    /**
     * 读取文件
     * @param pos 读取偏移
     * @param length 读取长度
     * @return 读取结果
     */
    public ByteBuffer read(long pos, int length) {
        ByteBuffer buffer = ByteBuffer.allocate(length);
        readWriteLock.readLock().lock();
        try {
            fileChannel.position(pos);
            fileChannel.read(buffer);
            buffer.flip();
            return buffer;
        } catch (IOException e) {
            log.error("io error", e);
        } finally {
            readWriteLock.readLock().unlock();
        }

        return null;
    }

    /**
     * 添加文件
     * @param offset 偏移，如果为-1L，则使用writePos
     * @param buffer 内容
     * @return 结果
     */
    private R<AppendResult> doAppend(long offset, ByteBuffer buffer) {
        AppendResult appendResult = new AppendResult();
        if (readWriteLock.writeLock().tryLock()) {
            if (readWriteStateRef.get().equals(ReadWriteState.READABLE)) {
                // todo 定义bizCode为BLOCK_EXECEED_MAX_SIZE
                return RWrapper.fail(1);
            }

            padding();

            int length = buffer.limit();
            // 判断写入偏移与最大块大小之间的差别如果小于length，那么直接返回失败
            if (maxBlockFileSize - writePos.get() < length) {
                // todo 定义bizCode为BLOCK_EXECEED_MAX_SIZE
                readWriteStateRef.getAndSet(ReadWriteState.READABLE);
                return RWrapper.fail(1);
            }

            long pos;
            try {
                pos = offset == -1L ? writePos.get() : offset;
                // 写入数据内容
                fileChannel.write(buffer, pos);
                pos = writePos.addAndGet(offset);
                doAfterAppend(pos, length);
            } catch (IOException e) {
                log.error("write file error.", e);
                return RWrapper.fail();
            } finally {
                readWriteLock.writeLock().unlock();
            }

            appendResult.setOffset(pos);
            return RWrapper.success(appendResult);
        }

        return RWrapper.fail();
    }



    /**
     * 预填充文件
     * @param startPos 起始偏移
     * @param length 填充重读
     */
    private void padding(long startPos, long length) {
        try {
            FilePaddingUtil.padFile(fileChannel, startPos + length);
            paddingPos.addAndGet(length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 填充文件大小
     */
    private void padding() {
        long paddingSize = autoExpandSize;
        // 计算padding位置与写入位置的差距，如果差距为自动扩展的大小的一半就进行扩展
        long diff = paddingPos.get() - writePos.get();
        while(diff < autoExpandSize / 2 && paddingPos.get() < maxBlockFileSize) {
            if (paddingPos.get() + autoExpandSize < maxBlockFileSize) {
                paddingSize = maxBlockFileSize - paddingPos.get();
            }
            padding(paddingPos.get(), paddingSize);
            diff = paddingPos.get() - writePos.get();
        }
    }

    protected void incrPaddingPos(long incr) {
        paddingPos.addAndGet(incr);
    }

    protected void padFile(long toPos) throws IOException {
        FilePaddingUtil.padFile(fileChannel, toPos);
        incrPaddingPos(toPos);
    }

    protected void writeLong(long offset, long data) throws IOException {
        rasFile.seek(offset);
        rasFile.writeLong(data);
    }

    protected void writeLong(long data) throws IOException {
        rasFile.writeLong(data);
    }

    protected long readLong(long pos) throws IOException {
        rasFile.seek(pos);
        return rasFile.readLong();
    }

    protected long readLong() throws IOException {
        return rasFile.readLong();
    }

    protected void resetWritePos(long newPos) throws IOException {
        writePos.set(newPos);
        padding(newPos, 0L);
    }

    protected MappedByteBuffer map(int startPos, int size) throws IOException {
        return fileChannel.map(FileChannel.MapMode.READ_WRITE, startPos, size);
    }

    protected void flush() throws IOException {
        fileChannel.force(false);
    }

    protected FileChannel getFileChannel() {
        return fileChannel;
    }

    public long getWritePos() {
        return writePos.get();
    }

    protected void doInitNewFile() throws IOException {

    }

    protected void doRecover() throws IOException {

    }

    protected void doInitOldFile() throws IOException {

    }

    protected void doBeforeInit() {

    }

    protected void doAfterInit() throws IOException {

    }

    /**
     * 主文件添加完成后的动作
     * @param offset 偏移
     * @param length 写入长度
     */
    protected void doAfterAppend(long offset, int length) {
    }


}
