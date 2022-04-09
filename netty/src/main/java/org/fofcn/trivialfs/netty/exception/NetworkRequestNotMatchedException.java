package org.fofcn.trivialfs.netty.exception;

public class NetworkRequestNotMatchedException extends NetworkException {
    public NetworkRequestNotMatchedException(String message) {
        this(message, null);
    }

    public NetworkRequestNotMatchedException(String message, Throwable cause) {
        super(message, cause);
    }
}
