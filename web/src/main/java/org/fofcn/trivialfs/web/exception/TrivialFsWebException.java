package org.fofcn.trivialfs.web.exception;

/**
 * trivial file system runtime exception
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/05/07 15:49
 */
public class TrivialFsWebException extends RuntimeException {

    private int code;

    private String message;

    public TrivialFsWebException(final int code, final String message) {
        this.code = code;
        this.message = message;
    }


}
