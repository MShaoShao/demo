package com.example.demo.javatrain.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 小苗学java4-jdbc数据库连接并实现数据的增删改查
 *
 * @author MiaoShaoDong
 * @date 15:12 2020/6/18
 */
public class DataConnection {
    /***  配置驱动数据库的jar包名称*/
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    /***  配置数据库的连接地址*/
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/default";
    /***  配置连接数据库的用户名*/
    private static final String DB_USER = "root";
    /***  配置连接数据库的密码*/
    private static final String DB_PASS = "123456";

    public static void main(String[] args) throws Exception {
        //创建数据库的连接对象
        Connection conn;
        //创建sql的执行对象
        Statement stmt;
        //创建接收数据库查询接口的对象
        ResultSet result;
        //1.利用Class类加载驱动程序
        Class.forName(DB_DRIVER);
        //2.连接数据库
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        //3.Statement接口需要通过Connection接口进行实例化操作
        stmt = conn.createStatement();
        //3.1.执行Sql插入语句，向数据库插入数据
        String sql = "INSERT INTO `default`.`user`(name, password, sex, createtime) VALUES('赵六', '789', 0, '2020.6.18 14:00:01')";
        stmt.executeUpdate(sql);
        //3.2.执行sql删除语句，并按条件删除对应的数据
        stmt.executeUpdate("delete from user where id=5");
        //3.2.执行sql删除语句，并按条件更新对应数据库属性的值
        stmt.executeUpdate("update user set name='赵6' where id=4");
        //3.4.执行sql查询语句，查询对应条件的数据库结果并输出
        result = stmt.executeQuery("SELECT * FROM user WHERE  name ='赵六'");
        //输出对应的用户名
        while (result.next()){
            System.out.println(result.getString(3));
        }
        //4.关闭数据库
        conn.close();
    }
}
