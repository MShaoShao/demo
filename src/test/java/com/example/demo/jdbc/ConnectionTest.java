package com.example.demo.jdbc;

import org.junit.Test;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author miaoshaodong
 * @date Creater in 16:01 2019/12/6
 */
public class ConnectionTest {
    @Test
    public void testConnection1() throws SQLException {
        Driver driver = null;
        String url=null;
        Properties info=null;
        Connection conn = driver.connect(url,info);
        System.out.println(conn);
    }
}
