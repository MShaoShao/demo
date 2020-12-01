package com.example.demo.javatrain.readXml;

import com.alibaba.fastjson.JSON;
import com.example.demo.javatrain.readXml.bean.CiAndRelationResp;
import com.example.demo.javatrain.readXml.bean.CiDto;
import com.example.demo.javatrain.readXml.bean.RelationDto;
import lombok.Data;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author MiaoShaoDong
 * @date 14:37 2020/11/26
 */
public class ReadJson {

    //读取json文件
    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String s = readJsonFile("C:\\工作代码\\demo\\src\\main\\java\\com\\example\\demo\\javatrain\\readXml\\bean\\ciAndRelation.json");
        CiAndRelationResp result = JSON.parseObject(s, CiAndRelationResp.class);
        List<CiDto> ci = result.getCi();
        List<RelationDto> relation = result.getRelation();
        List<RelationMsg> relationMap = DFS(ci, relation, "00000000002c4b63");
        System.out.println(JSON.toJSONString(relationMap));
    }


    private static List<RelationMsg> DFS(List<CiDto> ci, List<RelationDto> relation, String rootId) {

        //第一次深度遍历初版
        /*
        List<RelationDto> childList = relation.stream().filter(s->s.getFrom().equals(id)).collect(Collectors.toList());
        System.out.println(JSON.toJSONString(childList));
        int count = childList.size();
        System.out.println(count);
        for (int i = 0;i<childList.size();i++){
            DFS(relation,childList.get(i).getTo());
        }*/
        //第二次改造加对应深度遍历条件
        /*List<RelationDto> childList = relation.stream().filter(s -> s.getFrom().equals(childId)).collect(Collectors.toList());
        System.out.println(JSON.toJSONString(childList));
        int count = childList.size();
        System.out.println(count);
        //获取childId对应的ci资源
        List<CiDto> list =  ci.stream().filter(c->c.getId().equals(childId)).collect(Collectors.toList());
        //hasResourceId 为真，则为目的主资源id，否则为组件
        boolean hasResourceId = Strings.isNullOrEmpty(ci.get(0).getResourceId());
        if (count != 0){
            for (int i = 0;i<childList.size();i++){
                DFS(ci,relation,rootId,childList.get(i).getTo());
            }
        }else {
            //只有为组件时，count才会为0，目的设备不需要遍历到，只到连接关系是便可得到
            if (!hasResourceId){
                //判断组件的from是否是rootId
                if(relation.stream().anyMatch(s -> s.getTo().equals(childId) && s.getFrom().equals(rootId))){
                    //from与rootId相等，
                    System.out.println("第一层组成关系，不做记录");
                }else{
                    System.out.println("第n层组成关系，进行记录，并反推");
                }
            }
        }*/
        //第三次遍历，直接遍历，不使用递归
        List<RelationMsg> relationMsgList = new ArrayList<>();
        Map<String, Integer> result = new HashMap<>();
        int comCount = 0;
        int conCount = 0;
        for (RelationDto currentRelation : relation) {
            //判断当前类型为组成还是连接
            if ("compose".equals(currentRelation.getRelationTypeCode())) {
                //判断组成关系是否为第一层
                if (currentRelation.getFrom().equals(rootId)) {
                    //若是则不作处理，判断下一个
                    continue;
                } else {
                    CiDto componentCi = ci.stream().filter(c -> c.getId().equals(currentRelation.getFrom())).collect(Collectors.toList()).get(0);
                    CiDto dstComponentCi = ci.stream().filter(c -> c.getId().equals(currentRelation.getTo())).collect(Collectors.toList()).get(0);
                    if (componentCi.getResourceId().equals(rootId)) {
                        comCount += 1;
                        RelationMsg relationMsg = new RelationMsg();
                        relationMsg.setRelationName("接口-组成-板卡");
                        relationMsg.setRelationCount(comCount);
                        relationMsg.setId(currentRelation.getFrom());
                        relationMsg.setDisplayName((String) componentCi.getAttributes().get("displayName"));
                        relationMsg.setName((String) componentCi.getAttributes().get("name"));
                        relationMsg.setIfName((String) componentCi.getAttributes().get("displayName"));
                        relationMsg.setIfDisplayName((String) componentCi.getAttributes().get("displayName"));
                        relationMsg.setCardName((String) dstComponentCi.getAttributes().get("displayName"));
                        relationMsg.setCardDisplayName((String) dstComponentCi.getAttributes().get("displayName"));
                        relationMsgList.add(relationMsg);
                    }
                }
            }
            if ("connect".equals(currentRelation.getRelationTypeCode())) {
//                List<CiDto> srcCi =  ci.stream().filter(c->c.getId().equals(rootId)).collect(Collectors.toList());
//                List<CiDto> srcComponentCi =  ci.stream().filter(c->c.getId().equals(currentRelation.getFrom())).collect(Collectors.toList());
                String dstResourceId = ci.stream().filter(c -> c.getId().equals(currentRelation.getTo()))
                        .map(CiDto::getResourceId).collect(Collectors.toList()).get(0);
                CiDto componentCi = ci.stream().filter(c -> c.getId().equals(rootId)).collect(Collectors.toList()).get(0);
                CiDto srcComponentCi = ci.stream().filter(c -> c.getId().equals(currentRelation.getFrom())).collect(Collectors.toList()).get(0);
                CiDto dstCi = ci.stream().filter(c -> c.getId().equals(dstResourceId)).collect(Collectors.toList()).get(0);
                CiDto dstComponentCi = ci.stream().filter(c -> c.getId().equals(currentRelation.getTo())).collect(Collectors.toList()).get(0);
                String relationName = String.format("连接-%s/%s", dstCi.getAttributes().get("name"), dstComponentCi.getAttributes().get("name"));
                if (result.containsKey(relationName)) {
                    conCount = result.get(relationName) + 1;
                    RelationMsg relationMsg1 = relationMsgList.stream().filter(s->s.getRelationName().equals(relationName)).collect(Collectors.toList()).get(0);
                    relationMsg1.setRelationCount(conCount);
                    relationMsgList.remove(relationMsg1);
                    relationMsgList.add(relationMsg1);
                    result.put(relationName, conCount);
                } else {
                    int conCount1 = 1;
                    ds(relationMsgList, currentRelation, componentCi, srcComponentCi, dstCi, dstComponentCi, relationName, conCount1);
                    result.put(relationName, conCount1);
                }

            }
        }
        return relationMsgList;
    }

    private static void ds(List<RelationMsg> relationMsgList, RelationDto currentRelation, CiDto componentCi, CiDto srcComponentCi, CiDto dstCi, CiDto dstComponentCi, String relationName, int conCount1) {
        RelationMsg relationMsg2 = new RelationMsg();
        relationMsg2.setRelationName(relationName);
        relationMsg2.setRelationCount(conCount1);
        relationMsg2.setId(currentRelation.getFrom());
        relationMsg2.setDisplayName((String) componentCi.getAttributes().get("displayName"));
        relationMsg2.setName((String) componentCi.getAttributes().get("name"));
        relationMsg2.setSourceDevice((String) componentCi.getAttributes().get("displayName"));
        relationMsg2.setSrcPort((String) srcComponentCi.getAttributes().get("displayName"));
        relationMsg2.setDistDevice((String) dstCi.getAttributes().get("displayName"));
        relationMsg2.setDistPort((String) dstComponentCi.getAttributes().get("displayName"));
        relationMsg2.setDistIp((String) dstCi.getAttributes().get("ipAddress"));
        relationMsgList.add(relationMsg2);
    }
}

@Data
class RelationMsg {
    private String relationName;
    private Integer relationCount;
    private String id;
    private String name;
    private String displayName;
    private String srcPort;
    private String sourceDevice;
    private String distDevice;
    private String distIp;
    private String distPort;
    private String ifName;
    private String ifDisplayName;
    private String cardName;
    private String cardDisplayName;
}
