package org.fofcn.trivialfs.client;

import org.fofcn.trivialfs.common.exception.TrivialFsNetworkException;

import java.io.File;
import java.io.InputStream;

/**
 * 客户端Api
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/03/29 FutureClientApi
 */
public interface TrivialClient {

    /**
     * 写入文件
     * @param bucket
     * @param content 文件内容
     * @return
     */
    ApiResult write(String bucket, byte[] content) throws TrivialFsNetworkException;

    /**
     * 写入文件
     * @param bucket
     * @param file 文件内容
     * @return
     */
    ApiResult write(String bucket, File file);

    /**
     * 写入文件
     * @param bucket
     * @param filePath 文件内容
     * @return
     */
    ApiResult write(String bucket, String filePath);

    /**
     * 写入文件
     * @param bucket
     * @param inputFile 文件内容
     * @return
     */
    ApiResult write(String bucket, InputStream inputFile);

    /**
     * 读取文件
     * @param bucket
     * @param fileId 文件名
     * @return
     */
    ApiResult read(String bucket, long fileId);
}
