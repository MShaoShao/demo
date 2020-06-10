package com.example.demo.guava.eventbus.demo;

import com.example.demo.guava.eventbus.demo.bean.UrlLog;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * 排序工具
 *
 * @author MiaoShaoDong
 * @date 15:05 2020/6/10
 */
@Slf4j
public class SortUtils {

    private static final int EXAMPLE_SIZE = 5;

    public static void main(String[] args) {

        List<UrlLog> urls = new ArrayList<>(2);

        for (int i = 0; i < EXAMPLE_SIZE; i++) {
            UrlLog urlLog = new UrlLog();
            UrlLog urlLog1 = new UrlLog();
            long temp = 121 + i;
            urlLog.setCreateTime(temp);
            urlLog.setUrl("172.17.162.17:8080");
            urlLog.setStatus(true);
            urlLog.setProType("curl");
            urlLog1.setCreateTime(temp);
            urlLog1.setUrl("172.17.162.17:8080");
            urlLog1.setStatus(true);
            urlLog1.setProType("traceRoute");
            urls.add(urlLog);
            urls.add(urlLog1);
        }

        List<UrlLog> urlListSorts = urls.stream().sorted(Comparator.comparing(UrlLog::getUrl)
                .thenComparing(UrlLog::getCreateTime).reversed()
                .thenComparing(UrlLog::getStatus)).collect(toList());
        log.info("排序后的探测日志为，{}", urlListSorts);
    }
}
