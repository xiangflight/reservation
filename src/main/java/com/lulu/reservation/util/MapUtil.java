package com.lulu.reservation.util;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2018/8/18 下午1:09
 * <p>
 * 类说明：
 *     经参数以key值字典排序
 */

public class MapUtil {

    public static String join(Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> keyList = new ArrayList<>(map.keySet());
        Collections.sort(keyList);
        int size = keyList.size();
        for (int i = 0; i < size; i++) {
            String key = keyList.get(i);
            String value = map.get(key);
            if (StringUtils.isEmpty(value)) {
                continue;
            }
            if (i == size - 1) {
                stringBuilder.append(key).append("=").append(value);
            }
            else {
                stringBuilder.append(key).append("=").append(value).append("&");
            }
        }
        return stringBuilder.toString();
    }

}
