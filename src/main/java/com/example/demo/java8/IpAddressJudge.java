package com.example.demo.java8;

/**
 * IP地址判断属于哪个ip分组
 *
 * @author MiaoShaoDong
 * @date 16:12 2020/7/31
 */
public class IpAddressJudge {
    /**
     * 将ip 地址转换成数字
     *
     * @param IpStr 传入的ip地址
     * @return 转换成数字类型的ip地址
     */
    private static long IpToLong(String IpStr){
        String[] ip = IpStr.split("\\.");
        long a = Integer.parseInt(ip[0]);
        long b = Integer.parseInt(ip[1]);
        long c = Integer.parseInt(ip[2]);
        long d = Integer.parseInt(ip[3]);

        long ipNum = a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
        return ipNum;
    }
    private static long IpToLong1(String IpStr){
        String[] ips =  IpStr.split("\\.");
        if(ips == null || ips.length != 4){
            System.out.println("地址不符合Ipv标准");
        }
        int  ip_num = 0;
        ip_num = (Integer.valueOf(ips[0],10) << 24) +
                (Integer.valueOf(ips[1],10)  << 16) +
                (Integer.valueOf(ips[2],10)  << 8) +
                Integer.valueOf(ips[3],10);
        return  ip_num;
    }

    public static void main(String[] args) {
        String s = "172.12.12.1";
        long ipLong = IpToLong(s);
        long ipLong1 = IpToLong1(s);
        System.out.println("第1种转换方法得到的IP值为: "+ipLong+",第2种转换方法得到的IP值为: "+ipLong1);
        boolean s1 = ipLong == ipLong1;
        System.out.println(s1);
    }
}
