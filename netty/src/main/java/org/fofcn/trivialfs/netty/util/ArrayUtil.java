package org.fofcn.trivialfs.netty.util;

/**
 *  array utilities
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/21 14:01
 */
public class ArrayUtil {

    public static boolean isEmpty(String[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(byte[] array) {
        return array == null || array.length == 0;
    }
}
