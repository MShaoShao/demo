package com.example.demo.guava.utils;

import com.google.common.base.Strings;

/**
 * 小苗学java6- guava.Strings类中方法学习总结
 *
 * @author MiaoShaoDong
 * @date 17:49 2020/6/29
 */
public class StringsTrain {
    public static void main(String[] args) {
        //1. nullToEmpty方法若字符串为null则返回空值，否则返回该字符串
        String name = null;
        System.out.println(Strings.nullToEmpty(name));
        //2. emptyToNull方法若字符串为空值则返回null值，否则返回该字符串
        String name1 = "";
        System.out.println(Strings.emptyToNull(name1));
        //3. isNullOrEmpty方法判断字符串是否为空或null，若是则返回true，否则返回false
        String name2 = "";
        String name3 = null;
        System.out.println("name2的判断结果为"+Strings.isNullOrEmpty(name2)+"，name3的判断结果为"+Strings.isNullOrEmpty(name3));
        //4. padStart方法,判断特定字符串是否大于设置的最小长度，若小于则在字符串“开始”补充手工设置的“特定字符”，大于则不补充
        String name4 = "8";
        String name44 = "818";
        System.out.println("小于最小长度的前缀补充情况："+Strings.padStart(name4,3, '苗')
                +"\n大于最小长度则不补充情况为："+Strings.padStart(name44,3, '苗'));
        //5. padEnd方法判断特定字符串是否大于设置的最小长度，若小于则在字符串“结束”补充手工设置的“特定字符”，大于则不补充
        String name5 = "8";
        String name55 = "818";
        System.out.println("小于最小长度的后缀补充情况："+Strings.padEnd(name5,3, '苗')
                +"\n大于最小长度则不补充情况为："+Strings.padEnd(name55,3, '苗'));
        //6. repeat方法按照特定的次数重复字符串
        String name6 = "喵大大";
        System.out.println("重复六次喵大大的字符串为："+Strings.repeat(name6,6));
        //7. commonPrefix返回两个字符串共有的最长前缀字符串
        String name7 = "123466754";
        String name77 = "123456789";
        System.out.println("两个字符串共有的最长前缀字符串为："+Strings.commonPrefix(name7,name77));
        //8. commonSuffix返回两个字符串共有的最长后缀字符串
        String name8 = "123466789";
        String name88 = "123456789";
        System.out.println("两个字符串共有的最长后缀字符串为："+Strings.commonSuffix(name8,name88));
        //9. validSurrogatePairAt判断字符串是否超过定义的index值，若超过则返回false，没超过则返回true(只在test测试中存在，当前不做解释)
    }
}
