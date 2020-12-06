package com.zzangho.project.springboot.service.elasticSearch;

import com.zzangho.project.springboot.domain.common.Parameter;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.mapper.MappedFieldType;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.FieldHighlightContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.fetch.subphase.highlight.Highlighter;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class ESservice {
    /*
     * connection create method
     */
    public RestHighLevelClient createConnection() {
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("127.0.0.1",9200,"http")
                )
        );
    }

    /**
     * 전체 검색
     * @param indexName 인덱스명
     * @param parameter   파라메터
     * @return  SearchHits
     */
    public SearchHits matchAll(String indexName, Parameter parameter, String category) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.boolQuery()
                .must(QueryBuilders.matchAllQuery())
                .filter(QueryBuilders.termQuery("category_nm", category)));
        searchSourceBuilder.from(parameter.getPageNum());
        searchSourceBuilder.size(parameter.getLimit());
        searchSourceBuilder.sort(new FieldSortBuilder("udt_dt").order(SortOrder.DESC));

        System.out.println(searchSourceBuilder.toString());

        SearchRequest request = new SearchRequest(indexName);
        request.source(searchSourceBuilder);

        SearchResponse response = null;
        SearchHits searchHits = null;

        try(RestHighLevelClient client = createConnection();){

            response = client.search(request, RequestOptions.DEFAULT);
            searchHits = response.getHits();

        }catch (Exception e) {
            /*
             * 예외처리
             */
            e.printStackTrace();
        }

        return searchHits;
    }

    public SearchHits match(String indexName, Parameter parameter) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("title", parameter.getKwd()));
        searchSourceBuilder.from(parameter.getPageNum());
        searchSourceBuilder.size(parameter.getLimit());
        searchSourceBuilder.sort(new FieldSortBuilder("udt_dt").order(SortOrder.DESC));

        SearchRequest request = new SearchRequest(indexName);
        request.source(searchSourceBuilder);

        SearchResponse response = null;
        SearchHits searchHits = null;

        try(RestHighLevelClient client = createConnection();){

            response = client.search(request, RequestOptions.DEFAULT);
            searchHits = response.getHits();

        }catch (Exception e) {
            /*
             * 예외처리
             */
            e.printStackTrace();
        }

        return searchHits;
    }

    public SearchHits boolQuery(String indexName, Parameter parameter, String category) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.boolQuery()
                                                .must(QueryBuilders.termQuery("title", parameter.getKwd()))
                                                .filter(QueryBuilders.termQuery("category_nm", category)));
        searchSourceBuilder.from(parameter.getPageNum());
        searchSourceBuilder.size(parameter.getLimit());
        searchSourceBuilder.sort(new FieldSortBuilder("udt_dt").order(SortOrder.DESC));

        System.out.println(searchSourceBuilder.toString());

        SearchRequest request = new SearchRequest(indexName);
        request.source(searchSourceBuilder);

        SearchResponse response = null;
        SearchHits searchHits = null;

        try(RestHighLevelClient client = createConnection();){

            response = client.search(request, RequestOptions.DEFAULT);
            searchHits = response.getHits();

        }catch (Exception e) {
            /*
             * 예외처리
             */
            e.printStackTrace();
        }

        return searchHits;
    }
}
