package com.example.demo.java8;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 按照某一字段过滤重复数据
 *
 * @author MiaoShaoDong
 * @date 11:29 2020/7/6
 */
public class StreamDIstinctTrain {
    public static void main(String[] args) {
        ProbeInfo probeInfo = new ProbeInfo();
        probeInfo.setTime(1L);
        probeInfo.setProbeType("TCP");
        probeInfo.setData(ImmutableMap.of("id","1"));
        ProbeInfo probeInfo2 = new ProbeInfo();
        probeInfo2.setTime(2L);
        probeInfo2.setProbeType("TCP");
        probeInfo2.setData(ImmutableMap.of("id","1"));
        ProbeInfo probeInfo3 = new ProbeInfo();
        probeInfo3.setTime(3L);
        probeInfo3.setProbeType("TCP");
        probeInfo3.setData(ImmutableMap.of("id","1"));

        List<ProbeInfo> probe = new ArrayList<>();
        probe.add(probeInfo);
        probe.add(probeInfo2);
        probe.add(probeInfo3);
        Data data = new Data();
        data.setAgentId("100");
        data.setAppId("app1");
        data.setAreaId("area1");
        data.setProbe(probe);

        List<ProbeInfo> urlItems = data.getProbe();
        System.out.println(urlItems.size());
        List<ProbeInfo> urlItemsNew = urlItems.stream().filter(distinctByKey(p->p.getData().get("id"))).collect(Collectors.toList());
        System.out.println(urlItemsNew.size());
        System.out.println(urlItemsNew);
    }
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
