package org.fofcn.trivialfs.bucket.constant;

/**
 * read write enumeration
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/28 14:46
 */
public enum ReadWriteEnum {
    READ(1, "read"),
    WRITE(2, "write");

    int code;

    String desc;

    ReadWriteEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
