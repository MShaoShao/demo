package com.example.demo.java8;

import java.util.*;

import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * map排序
 *
 * @author MiaoShaoDong
 * @date 15:00 2020/7/16
 */
public class MapSortUtils {

    public static void main1(String[] args) {
       /* Map<String,Object> map = new HashMap<>();
        map.put("recoverIndex",11);
        map.put("data", Arrays.asList("11","ss"));
        List<Map<String,Object>> list = new ArrayList<>();
        list.add(map);
        List<Map<String,Object>> list1 = new ArrayList<>();
        list.forEach(s-> list1.add(sortByKey(s)));
        System.out.println(list1);*/

        String s1 = "locations系信息";
        s1 = s1.replace("locations", "ww");
        System.out.println(s1);
    }

    /**
     * 实现不满足恢复现象的位置按条件显示
     *
     * @param phenomenon   告警子现象
     * @param replaceIndex 替换的子现象索引
     * @param replaceValue 替换的内容
     * @param size         位置个数
     */
    private static void noRecoverDescriptionReplace(Map<String, Object> phenomenon, String replaceIndex,
                                                    List<String> replaceValue, long size) {
        if (!isEmpty(replaceIndex)) {
            //StringBuilder p = new StringBuilder(phenomenon.get(replaceIndex).toString());
            String p = phenomenon.get(replaceIndex).toString();
            StringBuilder value = new StringBuilder();
            replaceValue.forEach(s -> value.append(s).append("、"));
            if (size >= 3) {
                //p.replace(0, 9, value.substring(0,value.toString().length()-1) + "等" + size + "个位置");
               int index =  value.toString().indexOf('、', value.toString().indexOf("、") + 1);
                System.out.println(index);
                p = p.replace("locations", value.substring(0, index) + "等" + size + "个位置");
            } else {
                //p.replace(0, 9, value.substring(0,value.toString().length()-1));
                p = p.replace("locations", value.substring(0, value.toString().length() - 1));
            }
            phenomenon.put(replaceIndex, p);
        }
    }

    public static <K extends Comparable<? super K>, V> Map<K, V> sortByKey(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();

        map.entrySet().stream()
                .sorted(Map.Entry.<K, V>comparingByKey().reversed()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("1", "locations系信息");
        map.put("2", "系信息");
        String index = "1";
        List<String> replaceValue = new ArrayList<>();
        replaceValue.add("天津位置");
        replaceValue.add("北京位置");
        replaceValue.add("北京位置1");
        replaceValue.add("北京位置2");
        long s = 4;
        noRecoverDescriptionReplace(map, index, replaceValue, s);
        String s1 = "locations系信息";
        String s22 = s1.replaceAll("locations", "ww");
        System.out.println(s22);
        System.out.println(map);
    }


    public static void main2(String[] args) {
//        String result = "[\"wqe\",\"qwq\",\"wwwwww\"]";
//        StringBuilder builder = new StringBuilder("[");
//        builder.append("wqe");
//        builder.append(",");
//        builder.append("wqe1");
//        builder.append(",");
//        builder.append("wqe2");
//        builder.append("]");
//        System.out.println(builder);
//        String s1 = builder.toString().replace("[","[\"");
//        String s2 = s1.replace(",","\",\"");
//        String s = s2.replace("]","\"]");
//        List<String> deleteIds = JSON.parseArray(s,String.class);
//        System.out.println(deleteIds);
        String s = "\"graphql\": \"mutation {deleteMultiPointView(where:{id:\\\"cd957cfe-9e55-11ea-b710-0242ac120005\\\"}) {id}}\"";
        System.out.println(s.contains("mutation"));
    }

}

