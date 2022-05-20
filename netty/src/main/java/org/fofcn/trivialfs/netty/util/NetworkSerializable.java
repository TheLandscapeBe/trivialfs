package org.fofcn.trivialfs.netty.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author errorfatal89@gmail.com
 */
public class NetworkSerializable {
    private static final Logger log = LoggerFactory.getLogger("Network");

    public static <T> List<T> jsonArrayDecode(byte[] bytes, Class<T> classType) {
        if (bytes == null || classType == null) {
            return null;
        }

        String json = new String(bytes, StandardCharsets.UTF_8);
        return (List<T>)JSON.parseObject(json, classType);

    }


    public static <T> T jsonDecode(byte[] bytes, Class<T> classType) {
        if (bytes == null || classType == null) {
            return null;
        }
        String json = new String(bytes, StandardCharsets.UTF_8);
        return JSON.parseObject(json, classType);
    }

    public static <T> List<T> jsonDecodeArray(byte[] bytes, Class<T> classType) {
        if (bytes == null || classType == null) {
            return null;
        }

        String json = new String(bytes, StandardCharsets.UTF_8);
        return JSON.parseArray(json, classType);
    }


    public static <T> byte[] jsonEncode(T obj) {
        String jsonStr = JSON.toJSONString(obj);
        return jsonStr.getBytes(StandardCharsets.UTF_8);
    }
}
