package com.example.demo.guava.eventbus.demo;

import com.ibm.icu.text.Collator;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * 小苗学java第三天 读取txt文件信息
 *
 * @author MiaoShaoDong
 * @date 21:48 2020/6/4
 */
@Slf4j
public class ReadByte {

    private static final String FILE_PATH = "D:/DSL.txt";

    /**
     * 功能：读取txt文件的内容
     * 步骤：1、先要获得文件句柄
     * 2、获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取
     * 3、读取到输入流后，需要读取生成字节流
     * 4、一行一行的输出。readline()方法
     * 注意：需要考虑异常情况
     */
    private static void readTxtFile() {

        String codeStyle = "GBK";
        //1、先要获得文件句柄
        File file = new File(ReadByte.FILE_PATH);
        //判断文件是否存在
        try {
            if (file.exists() && file.isFile()) {
                //注意编码格式，对文件进行处理当做字节流读取
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file), codeStyle);
                //将读取到的字节流放入内存中，以便高效按行取出
                BufferedReader bufferedReader = new BufferedReader(reader);
                String lineTxt = null;
                //按行输出
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    System.out.println(lineTxt);
                }
                reader.close();
            } else {
                System.out.println("查找不到指定文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            log.info("异常记录为：", e);
        }
    }


    public static void main(String[] args) {
        //readTxtFile();
        //转义字符替换
//        String s = "log -> www.baidu23231.com: Temporary failure in name resolution \\\\nCannot handle \\\"host\\\" cmdline arg `www.baidu23231.com' on position 1 (argc 5)";
//        System.out.println("替换前字符串为：" + s);
//        System.out.println("替换后字符串为：" + StringEscapeUtils.unescapeJava(s));

        List<String> sortList = Arrays.asList("sa-dsa-das", "1111111111111111111111");
        sortList.sort((o1, o2) -> compare(o1, o2));
        System.out.println(sortList);
    }

    private static int compare(String src, String dst) {
        checkNotNull(src, "compare src不能为空：%s ", src);
        checkNotNull(dst, "compare dst不能为空：%s ", dst);
        Comparator<Object> chinaCompare = Collator.getInstance(Locale.CHINESE);
        int len1 = src.length();
        int len2 = dst.length();
        if (len1 > len2) {
            if (isNumberic(src) && isNumberic(dst)){
                return chinaCompare.compare(src,dst);
            }
            return 1;
        }else if(len1 < len2){
            return -1;
        }
        return chinaCompare.compare(src,dst);
    }

    private static boolean isNumberic(String str) {
        String regex = "^[a-z0-9A-Z]+$";
        return str.matches(regex);
    }
}
