package com.example.demo.javatrain;

import lombok.Data;

import java.io.*;
import java.util.Scanner;

/**
 * 学生类定义
 */
@Data
class Student implements Serializable {
    private static final int MAX_SCORE = 100;
    private int id;
    private String name;
    private int math;
    private int os;
    private int java;

    Student(int id, String name, int math, int os, int java) {
        if (id > 0 && name.length() > 0 && math <= MAX_SCORE && os <= MAX_SCORE && java <= MAX_SCORE) {
            this.id = id;
            this.name = name;
            this.math = math;
            this.os = os;
            this.java = java;
        } else {
            System.out.println("输入的学生信息不正确");
        }
    }
}

/**
 * 小苗学java第4天---文件操作练习
 *
 * @author MiaoShaoDong
 * @date 10:18 2020/6/11
 */
public class FileReaderDemo {
    public static void main(String[] args) throws FileNotFoundException {
        //创建student对象
        saveAndReadClass();
    }

    private static void saveAndReadClass() throws FileNotFoundException {
        Student student;
        Scanner scanner = new Scanner(System.in);
        RandomAccessFile readFile = new RandomAccessFile("C:\\Users\\86151\\Desktop\\告警测试\\studentDemo.txt", "rw");
        while (true) {
            System.out.println("输入任意数字，1键结束");
            String input = scanner.next();
            if (input.endsWith("1")) {
                break;
            }
            System.out.println("请输入个人信息");
            System.out.println("姓名：");
            String name = scanner.next();
            System.out.println("学号：");
            int id = scanner.nextInt();
            System.out.println("数学成绩：");
            int math = scanner.nextInt();
            System.out.println("os成绩：");
            int os = scanner.nextInt();
            System.out.println("java成绩：");
            int java = scanner.nextInt();
            student = new Student(id, name, math, os, java);
            if (student.getName() != null) {
                String info = "  id: " + student.getId() + " name：" + student.getName() + "math: " + student.getMath() + " os:" + student.getOs() + " java" + student.getJava();
                byte[] bytes = info.getBytes();
                try {
                    readFile.seek(readFile.length());
                    readFile.write(bytes);
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        }
        byte[] b1 = new byte[1024];
        int i = 0;
        try {
            readFile.seek(0);
            i = readFile.read(b1);
            readFile.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
        System.out.println(new String(b1, 0, i));
    }

    /**
     * 复制文件
     *
     * @param srcPath 源文件地址
     * @param dstPath 文件复制到的地址
     */
    private static void copyFile(String srcPath, String dstPath) {
        //1、获取源文件的名称
        String srcFileName = srcPath.substring(srcPath.lastIndexOf("\\") + 1);
        System.out.println("源文件：" + srcFileName);
        //2、组装目的文件地址
        String dstFilePath = dstPath + File.separator + srcFileName;
        System.out.println("目标文件地址：" + dstFilePath);
        try {
            //3、建立缓存输入输出流
            BufferedInputStream fin = new BufferedInputStream(new FileInputStream(srcPath));
            BufferedOutputStream fou = new BufferedOutputStream(new FileOutputStream(dstFilePath));
            //4、定义保存读取文件流的中间量，可根据读取文件的大小进行设置
            byte[] buffer = new byte[1024 * 8];
            int len = -1;
            //5、 读取文件末尾则返回-1
            while ((len = fin.read(buffer)) != -1) {
                fou.write(buffer, 0, len);
            }
            fou.flush();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    /**
     * 深度遍历文件目录,并过滤出后缀为.txt的文件
     *
     * @param file 读取的文件
     */
    private static void traverseFileDirectory(File file) {
        //.获取根目录的文件树中所有文件
        File[] fieldsData = file.listFiles();
        //4、输出查看
        assert fieldsData != null;
        for (File f : fieldsData) {
            if (!f.isDirectory() && f.isFile() && f.getName().contains(".txt")) {
                System.out.println(f);
            }
        }
    }

}
