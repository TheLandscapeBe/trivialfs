package org.fofcn.trivialfs.bucket.volume.lb;

import java.util.List;

/**
 * load balancer interface
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/21 14:44
 */
public interface LoadBalance<T> {

    /**
     * choose a object by load balance algorithm
     * @param list object list
     * @return obj
     */
    T selectOne(List<T> list);
}
