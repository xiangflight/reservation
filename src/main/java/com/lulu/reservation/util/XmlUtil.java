package com.lulu.reservation.util;

import com.lulu.reservation.domain.request.WxPayRequest;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.thoughtworks.xstream.mapper.MapperWrapper;

import java.io.Writer;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2018/8/18 下午1:28
 * <p>
 * 类说明：
 *     XML工具类
 */

public class XmlUtil {

    private static XStream xStream = new XStream(new XppDriver(new NoNameCoder()) {

        @Override
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out) {
                // 重写这个方法，不再像XppDriver那样调用nameCoder来进行编译，而是直接返回节点名称，避免双下划线出现
                @Override
                public String encodeNode(String name) {
                    return name;
                }
            };
        }
    });

    /**
     * 对象转xml
     *
     * @param obj object
     * @return xml string
     */
    public static String toXMl(Object obj) {
        //使用注解设置别名必须在使用之前加上注解类才有作用
        try {
            xStream.processAnnotations(obj.getClass());
            String xml = xStream.toXML(obj);
            return xml;
        } catch (Exception e) {
            return null;
        }
    }

    public static Object fromXML(String xml) {
        XStreamWrapper xStream = new XStreamWrapper(new DomDriver());
        xStream.processAnnotations(WxPayRequest.class);
        return xStream.fromXML(xml);
    }

    public static class XStreamWrapper extends XStream {

        public XStreamWrapper(HierarchicalStreamDriver hierarchicalStreamDriver) {
            super(hierarchicalStreamDriver);
        }

        @Override
        protected MapperWrapper wrapMapper(MapperWrapper next) {
            return new MapperWrapper(next) {
                @Override
                public boolean shouldSerializeMember(@SuppressWarnings("rawtypes") Class definedIn, String fieldName) {
                    // 不能识别的节点，掠过。
                    if (definedIn == Object.class) {
                        return false;
                    }
                    return super.shouldSerializeMember(definedIn, fieldName);
                }
            };
        }
    }

}
