package org.fofcn.trivialfs.store.disk.block;

import lombok.Data;
import org.fofcn.trivialfs.store.common.constant.StoreConstant;

/**
 * 文件尾部
 *
 * @author errorfatal89@gmail.com
 * @date 2022/03/23
 */
@Data
public class FileTailor {
    /**
     * 文件块尾部魔数
     */
    private long tailorMagic = StoreConstant.BLOCK_TAILOR_MAGIC_NUMBER;
}
