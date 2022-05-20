package org.fofcn.trivialfs.bucket.volume.lb;

/**
 * load balancer interface
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/21 14:44
 */
public interface LoadBalance<T> {

    /**
     * choose a object by load balance algorithm
     * @return obj
     */
    T selectOne();
}
