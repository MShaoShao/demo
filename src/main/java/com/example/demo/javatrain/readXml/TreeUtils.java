package com.example.demo.javatrain.readXml;

import com.alibaba.fastjson.JSON;
import com.example.demo.javatrain.readXml.bean.CiAndRelationResp;
import com.example.demo.javatrain.readXml.bean.CiDto;
import com.example.demo.javatrain.readXml.bean.ITree;
import com.example.demo.javatrain.readXml.bean.RelationDto;
import com.sun.istack.internal.NotNull;
import jodd.util.StringUtil;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author MiaoShaoDong
 * @date 18:07 2020/11/26
 */
public class TreeUtils {

    /**
     * 集合转树结构
     *
     * @param collection 目标集合
     * @param clazz      集合元素类型
     * @param <T>
     * @return
     */
    public static <T> Collection<T> toTree(@NotNull Collection<T> collection, @NotNull Class<T> clazz) {
        return toTree(collection, null, null, null, clazz);
    }

    /**
     * 集合转树结构,注意,使用此方法,则集合元素必须继承ITree接口
     *
     * @param collection 目标集合
     * @param <T>
     * @return
     */
    public static <T extends ITree> Collection<T> toTree(@NotNull Collection<T> collection) {
        try {
            if (collection == null || collection.isEmpty()) return null;// 如果目标集合为空,直接返回一个空树
            // 找出所有的根节点
            Collection<T> roots = null;
            if (collection.getClass().isAssignableFrom(Set.class)) roots = new HashSet<>();
            else roots = new ArrayList<>();
            for (T tree : collection) {
                Object o = ITree.class.getMethod("getParent").invoke(tree);
                if (o instanceof String) {
                    if (StringUtil.isEmpty((String) o)) {
                        roots.add(tree);
                    }
                } else if (o == null) {
                    roots.add(tree);
                }
            }
            // 从目标集合移除所有的根节点
            collection.removeAll(roots);
            // 为根节点添加孩子节点
            for (T tree : roots) {
                addChild(tree, collection);
            }
            return roots;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 集合转树结构
     *
     * @param collection 目标集合
     * @param id         被依赖字段名称
     * @param parent     依赖字段名称
     * @param children   子节点集合属性名称
     * @param clazz      集合元素类型
     * @param <T>
     * @return
     */
    public static <T> Collection<T> toTree(@NotNull Collection<T> collection, String id, String parent, String children, @NotNull Class<T> clazz) {
        try {
            if (collection == null || collection.isEmpty()) return null;// 如果目标集合为空,直接返回一个空树
            if (StringUtil.isEmpty(id)) id = "id";// 如果被依赖字段名称为空则默认为id
            if (StringUtil.isEmpty(parent)) parent = "parent";// 如果依赖字段为空则默认为parent
            if (StringUtil.isEmpty(children)) children = "children";// 如果子节点集合属性名称为空则默认为children
            Collection<T> roots = null;// 初始化根节点集合
            if (collection.getClass().isAssignableFrom(Set.class))
                roots = new HashSet<>();// 如果目标节点是一个set集合,则初始化根节点集合为hashset
            else roots = new ArrayList<>();// 否则初始化为Arraylist,
            // 这里集合初始化只分2中,要么是hashset,要么ArrayList,因为这两种最常用,其他不常用的摒弃
            Field idField = null;
            try {
                idField = clazz.getDeclaredField(id);// 获取依赖字段
            } catch (NoSuchFieldException e1) {
                idField = clazz.getSuperclass().getDeclaredField(id);
            }
            Field parentField = null;
            try {
                parentField = clazz.getDeclaredField(parent);// 获取被依赖字段
            } catch (NoSuchFieldException e1) {
                parentField = clazz.getSuperclass().getDeclaredField(parent);
            }
            Field childrenField = null;// 获取孩子字段
            try {
                childrenField = clazz.getDeclaredField(children);
            } catch (NoSuchFieldException e1) {
                childrenField = clazz.getSuperclass().getDeclaredField(children);
            }
            // 设置为可访问
            idField.setAccessible(true);
            parentField.setAccessible(true);
            childrenField.setAccessible(true);
            // 找出所有的根节点
            for (T c : collection) {
                Object o = parentField.get(c);
                if (o instanceof String) {
                    if (StringUtil.isEmpty((String) o)) {// 如果父节点为空则说明是根节点,添加到根节点集合
                        roots.add(c);
                    }
                } else {
                    if (o == null) {
                        roots.add(c);
                    }
                }
            }
            // 从目标集合移除所有根节点
            collection.removeAll(roots);
            for (T c : roots) {// 遍历根节点,依次添加子节点
                addChild(c, collection, idField, parentField, childrenField);
            }
            // 关闭可访问
            idField.setAccessible(false);
            parentField.setAccessible(false);
            childrenField.setAccessible(false);
            return roots;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static <T extends ITree> void addChild(T tree, Collection<T> collection) {
        try {
            Object id = ITree.class.getMethod("getId").invoke(tree);
            Collection<T> children = (Collection<T>) ITree.class.getMethod("getChildren").invoke(tree);
            for (T cc : collection) {
                Object o = ITree.class.getMethod("getParent").invoke(cc);
                if (id.equals(o)) {// 如果当前节点的被依赖值和目标节点的被依赖值相等,则说明,当前节点是目标节点的子节点
                    if (children == null) {// 如果目标节点的孩子集合为null,初始化目标节点的孩子集合
                        if (collection.getClass().isAssignableFrom(Set.class)) {// 如果目标集合是一个set集合,则初始化目标节点的孩子节点集合为set
                            children = new HashSet<>();
                        } else children = new ArrayList<>();// 否则初始化为list
                    }
                    // 将当前节点添加到目标节点的孩子节点
                    children.add(cc);
                    // 重设目标节点的孩子节点集合,这里必须重设,因为如果目标节点的孩子节点是null的话,这样是没有地址的,就会造成数据丢失,所以必须重设,如果目标节点所在类的孩子节点初始化为一个空集合,而不是null,则可以不需要这一步,因为java一切皆指针
                    ITree.class.getMethod("setChildren", Collection.class).invoke(tree, children);
                    // 递归添加孩子节点
                    addChild(cc, collection);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 为目标节点添加孩子节点,此方法为私有,不能为公开,否则类修改信息无法恢复,后面有公开方法,其专门为目标节点添加子节点
     *
     * @param c             目标节点
     * @param collection    目标集合
     * @param idField
     * @param parentField
     * @param childrenField
     * @param <T>
     * @throws IllegalAccessException
     */
    private static <T> void addChild(@NotNull T c, @NotNull Collection<T> collection, @NotNull Field idField, @NotNull Field parentField, @NotNull Field childrenField) throws IllegalAccessException {
        Object id = idField.get(c);// 获取目标节点的被依赖值
        Collection<T> children = (Collection<T>) childrenField.get(c);// 获取目标节点的孩子列表
        for (T cc : collection) {// 遍历目标集合
            Object o = parentField.get(cc);// 获取当前节点的依赖值
            if (id.equals(o)) {// 如果当前节点的被依赖值和目标节点的被依赖值相等,则说明,当前节点是目标节点的子节点
                if (children == null) {// 如果目标节点的孩子集合为null,初始化目标节点的孩子集合
                    if (collection.getClass().isAssignableFrom(Set.class)) {// 如果目标集合是一个set集合,则初始化目标节点的孩子节点集合为set
                        children = new HashSet<>();
                    } else children = new ArrayList<>();// 否则初始化为list
                }
                // 将当前节点添加到目标节点的孩子节点
                children.add(cc);
                // 重设目标节点的孩子节点集合,这里必须重设,因为如果目标节点的孩子节点是null的话,这样是没有地址的,就会造成数据丢失,所以必须重设,如果目标节点所在类的孩子节点初始化为一个空集合,而不是null,则可以不需要这一步,因为java一切皆指针
                childrenField.set(c, children);
                // 递归添加孩子节点
                addChild(cc, collection, idField, parentField, childrenField);
            }
        }
        // 特别说明:大家可以看到此递归没有明显出口,其出口就是是否当前节点的依赖值和目标节点的被依赖值一样,一样就递归,不一样进不了if,自然出递归
        // 此工具类自我感觉是最简单的,最实用的工具类,我看网上许多人写的,都是云的雾的,本来也想借鉴,但是实在没一个能看的感觉思路清晰,没办法,自己动手造轮子
    }

    /**
     * 为目标节点添加孩子
     *
     * @param c          目标节点
     * @param collection 目标集合
     * @param id         被依赖字段名
     * @param parent     依赖字段名
     * @param children   孩子节点字段名
     * @param clazz      集合元素所在类别
     * @param <T>
     */
    public static <T> void addChild(@NotNull T c, @NotNull Collection<T> collection, String id, String parent, String children, @NotNull Class<T> clazz) {
        try {
            if (collection == null || collection.isEmpty()) return;// 如果目标集合为空,直接返回一个空树
            if (StringUtil.isEmpty(id)) id = "id";// 如果被依赖字段名称为空则默认为id
            if (StringUtil.isEmpty(parent)) parent = "parent";// 如果依赖字段为空则默认为parent
            if (StringUtil.isEmpty(children)) children = "children";// 如果子节点集合属性名称为空则默认为children
            Field idField = null;
            try {
                idField = clazz.getDeclaredField(id);// 获取依赖字段
            } catch (NoSuchFieldException e1) {
                idField = clazz.getSuperclass().getDeclaredField(id);
            }
            Field parentField = null;
            try {
                parentField = clazz.getDeclaredField(parent);// 获取被依赖字段
            } catch (NoSuchFieldException e1) {
                parentField = clazz.getSuperclass().getDeclaredField(parent);
            }
            Field childrenField = null;// 获取孩子字段
            try {
                childrenField = clazz.getDeclaredField(children);
            } catch (NoSuchFieldException e1) {
                childrenField = clazz.getSuperclass().getDeclaredField(children);
            }
            // 设置为可访问
            idField.setAccessible(true);
            parentField.setAccessible(true);
            childrenField.setAccessible(true);
            addChild(c, collection, idField, parentField, childrenField);
            // 关闭可访问
            idField.setAccessible(false);
            parentField.setAccessible(false);
            childrenField.setAccessible(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 为目标节点添加孩子
     *
     * @param c          目标节点
     * @param collection 目标集合
     * @param clazz      集合元素所在类型
     * @param <T>
     */
    public static <T> void addChild(@NotNull T c, @NotNull Collection<T> collection, @NotNull Class<T> clazz) {
        addChild(c, collection, null, null, null, clazz);
    }
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
        String s = readJsonFile("C:\\工作代码\\demo\\src\\main\\java\\com\\example\\demo\\javatrain\\readXml\\ciAndRelation.json");
        CiAndRelationResp result = JSON.parseObject(s, CiAndRelationResp.class);
        List<Menu> list = new ArrayList<>();
        Menu menu1= new Menu();
        menu1.setId("00000000002c4b63");
        menu1.setParent(null);
        list.add(menu1);
        List<RelationDto> relation = result.getRelation();
        relation.forEach(relationDto ->{
             Menu menu= new Menu();
            menu.setId("relationDto.getTo()");
            menu.setParent(null);
            list.add(menu);
            menu.setId(relationDto.getTo());
             menu.setParent(relationDto.getFrom());
             list.add(menu);
        });
        Collection<Menu> menus = TreeUtils.toTree(list, null, null, null, Menu.class);
        System.out.println(JSON.toJSONString(menus));
    }
}

class Menu {
    private String id;
    private String parent;
    private List<Menu> children;

    public Menu() {
    }

    public Menu(String id, String parent) {
        this.id = id;
        this.parent = parent;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}
