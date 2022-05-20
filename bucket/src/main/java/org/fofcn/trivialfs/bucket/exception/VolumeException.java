package org.fofcn.trivialfs.bucket.exception;

/**
 * Volume exception
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/21 11:39
 */
public class VolumeException extends RuntimeException {

    public VolumeException(String message) {
        super(message);
    }

    public VolumeException(Throwable cause) {
        super(cause);
    }
}
