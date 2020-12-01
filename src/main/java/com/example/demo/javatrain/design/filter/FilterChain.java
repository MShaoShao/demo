package com.example.demo.javatrain.design.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * 步骤4- 创建过滤器链
 *
 * @author MiaoShaoDong
 * @date 17:00 2020/8/31
 */
public class FilterChain {
    private List<Filter> filters = new ArrayList<>();
    private Target target;
    public void addFilter(Filter filter){
        filters.add(filter);
    }
    public void execute(String request){
        for (Filter filter : filters){
            filter.execute(request);
        }
        target.execute(request);
    }

    public void setTarget(Target target){
        this.target = target;
    }
}
