package org.fofcn.trivialfs.common.exception;

/**
 * TrickyFsException
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/02 16:31
 */
public class TrivialFsException extends Exception {
    public TrivialFsException(Exception e) {
        super(e);
    }

    public TrivialFsException(String msg) {
        super(msg);
    }
}
