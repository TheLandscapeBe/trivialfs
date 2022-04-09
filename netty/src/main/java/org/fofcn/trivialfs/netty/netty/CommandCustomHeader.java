package org.fofcn.trivialfs.netty.netty;

import java.io.Serializable;

/**
 * @author errorfatal89@gmail.com
 */
public interface CommandCustomHeader extends Serializable {
    int getCode();
}
