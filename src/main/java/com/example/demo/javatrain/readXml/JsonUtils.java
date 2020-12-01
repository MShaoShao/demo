package com.example.demo.javatrain.readXml;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.*;

import java.io.IOException;
import java.util.List;

/**
 * @author MiaoShaoDong
 * @date 17:59 2020/11/3
 */
public class JsonUtils {

    public static JSONObject documentToJSONObject(String xml) {
        return elementToJSONObject(strToDocument(xml).getRootElement());
    }

    public static Document strToDocument(String xml){
        try {
            //加上xml标签是为了获取最外层的标签，如果不需要可以去掉
            return DocumentHelper.parseText("<xml>"+xml+"</xml>");
        } catch (DocumentException e) {
            return null;
        }
    }

    public static JSONObject elementToJSONObject(Element node) {
        JSONObject result = new JSONObject();
        // 当前节点的名称、文本内容和属性
        List<Attribute> listAttr = node.attributes();// 当前节点的所有属性的list
        for (Attribute attr : listAttr) {// 遍历当前节点的所有属性
            result.put(attr.getName(), attr.getValue());
        }
        // 递归遍历当前节点所有的子节点
        // 所有一级子节点的list
        List<Element> listElement = node.elements();
        if (!listElement.isEmpty()) {
            // 遍历所有一级子节点
            for (Element e : listElement) {
                // 判断一级节点是否有属性和子节点
                if (e.attributes().isEmpty() && e.elements().isEmpty()){
                    // 沒有则将当前节点作为上级节点的属性对待
                    result.put(e.getName(), e.getTextTrim());
                }
                else {
                    // 判断父节点是否存在该一级节点名称的属性
                    if (!result.containsKey(e.getName())){
                        // 没有则创建
                        result.put(e.getName(), new JSONArray());
                    }
                    // 将该一级节点放入该节点名称的属性对应的值中
                    ((JSONArray) result.get(e.getName())).add(elementToJSONObject(e));
                }
            }
        }
        return result;
    }

    public static void main(String[] args) throws JSONException, IOException {

        String result = "<paramConfigs>" +
                "    <!--    SNMPV1连接参数显示-->" +
                "    <SNMP type=\"SNMP\" displayName=\"SNMP\">" +
                "        <params>" +
                "            <param name=\"version\" paramType=\"ENUM\" dataType=\"INTEGER\" defaultValue=\"1\" displayName=\"版本号\"" +
                "                   required=\"true\"" +
                "                   description=\"描述\">" +
                "                <items>" +
                "                    <item name=\"v1\" value=\"0\"/>" +
                "                    <item name=\"v2c\" value=\"1\"/>" +
                "                    <item name=\"v3\" value=\"3\"/>" +
                "                </items>" +
                "            </param>" +
                "            <param name=\"port\" paramType=\"TEXT\" dataType=\"INTEGER\" defaultValue=\"161\" displayName=\"端口号\" min=\"1\"" +
                "                   max=\"65535\" description=\"端口号1-65535\"/>" +
                "            <param name=\"community\" paramType=\"TEXT\" dataType=\"STRING\" defaultValue=\"public\"" +
                "                   displayName=\"只读共同体\"/>" +
                "            <param name=\"writeCommunity\" paramType=\"TEXT\" dataType=\"STRING\" defaultValue=\"public\"" +
                "                   displayName=\"读写共同体\"/>" +
                "            <param name=\"secName\" paramType=\"TEXT\" dataType=\"STRING\" defaultValue=\"public\" displayName=\"用户名\" showCondition=\"version==3\"/>" +
                "            <param name=\"secLevel\" paramType=\"ENUM\" dataType=\"INTEGER\" defaultValue=\"1\" displayName=\"安全级别\" showCondition=\"version==3\">" +
                "                <items>" +
                "                    <item name=\"NOAUTH_NOPRIV\" value=\"1\"/>" +
                "                    <item name=\"AUTH_NOPRIV\" value=\"2\"/>" +
                "                    <item name=\"AUTH_PRIV\" value=\"3\"/>" +
                "                </items>" +
                "            </param>" +
                "            <param name=\"authProtocol\" paramType=\"ENUM\" dataType=\"STRING\" defaultValue=\"MD5\" displayName=\"认证协议\"" +
                "                   showCondition=\"version==3&amp;&amp;secLevel>=2\">" +
                "                <items>" +
                "                    <item name=\"AuthMD5\" value=\"MD5\"/>" +
                "                    <item name=\"AuthSHA\" value=\"SHA\"/>" +
                "                </items>" +
                "            </param>" +
                "            <param name=\"authPassword\" paramType=\"PASSWORD\" dataType=\"STRING\" defaultValue=\"\" displayName=\"认证密码\"" +
                "                   showCondition=\"version==3&amp;&amp;secLevel>=2\"/>" +
                "            <param name=\"privProtocol\" paramType=\"ENUM\" dataType=\"STRING\" defaultValue=\"DES\" displayName=\"加密协议\"" +
                "                   showCondition=\"version==3&amp;&amp;secLevel==3\">" +
                "                <items>" +
                "                    <item name=\"PrivDES\" value=\"DES\"/>" +
                "                    <item name=\"Priv3DES\" value=\"3DES\"/>" +
                "                    <item name=\"PrivAES128\" value=\"AES128\"/>" +
                "                    <item name=\"PrivAES192\" value=\"AES192\"/>" +
                "                    <item name=\"PrivAES256\" value=\"AES256\"/>" +
                "                </items>" +
                "            </param>" +
                "            <param name=\"privPassword\" paramType=\"PASSWORD\" dataType=\"STRING\" defaultValue=\"\" displayName=\"加密密码\"" +
                "                   showCondition=\"version==3&amp;&amp;secLevel==3\"/>" +
                "        </params>" +
                "        <testParams>" +
                "            <param name=\"ip\" paramType=\"TEXT\" dataType=\"STRING\" defaultValue=\"\" displayName=\"ip\"/>" +
                "        </testParams>" +
                "    </SNMP>" +
                "    <!--    MYSQL连接参数显示-->" +
                "    <MYSQL type=\"DB_MYSQL\" displayName=\"JDBC（MySQL）\">" +
                "        <params>" +
                "            <param name=\"user\" paramType=\"PASSWORD\" dataType=\"STRING\" defaultValue=\"\" displayName=\"用户名\" required=\"true\"/>" +
                "            <param name=\"password\" paramType=\"PASSWORD\" dataType=\"STRING\" defaultValue=\"\" displayName=\"密码\"/>" +
                "            <param name=\"port\" paramType=\"TEXT\" dataType=\"INTEGER\" min=\"1\" max=\"65535\" defaultValue=\"3306\" displayName=\"端口\" required=\"true\"/>" +
                "        </params>" +
                "        <testParams>" +
                "            <param name=\"ip\" paramType=\"TEXT\" dataType=\"STRING\" defaultValue=\"\" displayName=\"ip\"/>" +
                "        </testParams>" +
                "    </MYSQL>" +
                "</paramConfigs>";

        System.out.println(documentToJSONObject(result).toJSONString());


    }
}
