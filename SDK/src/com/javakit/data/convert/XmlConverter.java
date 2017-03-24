package com.javakit.data.convert;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

/**
 * Created by cdts on 17/03/2017.
 */
public class XmlConverter {

//    public static Document string2xml(String xmlString) {
//        try {
//            StringReader sr = new StringReader(xmlString);
//            InputSource is = new InputSource(sr);
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder= factory.newDocumentBuilder();
//            Document doc = builder.parse(is);
//            return doc;
//        } catch (Exception e) {
//            System.out.println(e.toString());
//            return null;
//        }
//    }
//    /**
//     * 将xml字符串<STRONG>转换</STRONG>为JSON字符串
//     *
//     * @param xmlString
//     *            xml字符串
//     * @return JSON<STRONG>对象</STRONG>
//     */
//    public static JSON xml2json(String xmlString) {
//        XMLSerializer xmlSerializer = new XMLSerializer();
//        return xmlSerializer.read(xmlString);
////        return json.toString(1);
//    }
//
//    /**
//     * JSON(数组)字符串<STRONG>转换</STRONG>成XML字符串
//     *
//     * @param jsonString
//     * @return
//     */
//    public static String json2xml(String jsonString) {
//        XMLSerializer xmlSerializer = new XMLSerializer();
//        return xmlSerializer.write(JSONSerializer.toJSON(jsonString));
//        // return xmlSerializer.write(JSONArray.fromObject(jsonString));//这种方式只支持JSON数组
//    }
}
