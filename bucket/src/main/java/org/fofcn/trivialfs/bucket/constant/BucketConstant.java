package org.fofcn.trivialfs.bucket.constant;

/**
 * bucket constant definition
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/15 16:48
 */
public class BucketConstant {

    /**
     * path for root name
     */
    public static final String ROOT_PATH = "/buckets";

    /**
     * store node cluster formatter
     */
    public static final String STORE_CLUSTER_FMT = "/buckets/%s/StoreCluster";

    /**
     * readable store cluster formatter
     */
    public static final String STORE_CLUSTER_READABLE_FMT = STORE_CLUSTER_FMT + "/readable";

    /**
     * writable store cluster formatter
     */
    public static final String STORE_CLUSTER_WRITABLE_FMT = STORE_CLUSTER_FMT + "/writable";

    /**
     * readable store cluster node formatter
     */
    public static final String STORE_CLUSTER_READABLE_NODE_FMT = STORE_CLUSTER_FMT + "/readable/%s";

    /**
     * writable store cluster node formatter
     */
    public static final String STORE_CLUSTER_WRITABLE_NODE_FMT = STORE_CLUSTER_FMT + "/writable/%s";

    /**
     * zk or etcd path separator character
     */
    public static final char PATH_SEPARATOR_CHAR = '/';
}
