package com.zzangho.project.springboot.util;

import org.elasticsearch.search.builder.SearchSourceBuilder;

public class QueryUtil {

    public SearchSourceBuilder matchAllQuery() {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        return searchSourceBuilder;
    }
}
