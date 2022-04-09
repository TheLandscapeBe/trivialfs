package org.fofcn.trivialfs.common.exception;

/**
 * 网络异常
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/06 16:26
 */
public class TrivialFsNetworkException extends TrivialFsException {

    public TrivialFsNetworkException(Exception e) {
        super(e);
    }

    public TrivialFsNetworkException(String msg) {
        super(msg);
    }
}
