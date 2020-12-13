package com.zzangho.project.springboot.service.elasticSearch;

import com.zzangho.project.springboot.domain.common.Parameter;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.Filters;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.FieldHighlightContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.fetch.subphase.highlight.Highlighter;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

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

        BoolQueryBuilder queryBuilders = QueryBuilders.boolQuery();

        queryBuilders.must(QueryBuilders.matchAllQuery())
                     .filter(QueryBuilders.termQuery("category_nm", category));

        // 신문사 옵션이 전체가 아닌경우
        if ( parameter.getCompany() != null && !"".equals(parameter.getCompany()) )
            queryBuilders.filter(QueryBuilders.termsQuery("company", parameter.getCompany().split(",")));

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilders);
        searchSourceBuilder.from(parameter.getPageNum());
        searchSourceBuilder.size(parameter.getLimit());

        // 정렬
        if ( "r".equals(parameter.getOrderby()) ) {
            searchSourceBuilder.sort(new FieldSortBuilder("_score").order(SortOrder.DESC));
        } else {
            searchSourceBuilder.sort(new FieldSortBuilder("udt_dt").order(SortOrder.DESC));
        }

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
            return null;
        }

        return searchHits;
    }

    public SearchHits match(String indexName, Parameter parameter) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("title", parameter.getKwd()));
        searchSourceBuilder.from(parameter.getPageNum());
        searchSourceBuilder.size(parameter.getLimit());
        searchSourceBuilder.sort(new FieldSortBuilder("udt_dt").order(SortOrder.DESC));

        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("by_company").field("company");
        searchSourceBuilder.aggregation(aggregationBuilder);

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
            return null;
        }

        return searchHits;
    }

    /**
     * Bool Query
     * @param indexName
     * @param parameter
     * @param category
     * @return
     */
    public SearchHits boolQuery(String indexName, Parameter parameter, String category) {

        BoolQueryBuilder queryBuilders = QueryBuilders.boolQuery();

        // 키워드가 있을 경우
        if ( parameter.getKwd() != null && !"".equals(parameter.getKwd()) )
            queryBuilders.must(QueryBuilders.termQuery("title", parameter.getKwd()));

        queryBuilders.filter(QueryBuilders.termQuery("category_nm", category));

        // 신문사 옵션이 전체가 아닌경우
        if ( parameter.getCompany() != null && !"".equals(parameter.getCompany()) )
            queryBuilders.filter(QueryBuilders.termsQuery("company", parameter.getCompany().split(",")));

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilders);
        searchSourceBuilder.from(parameter.getPageNum());
        searchSourceBuilder.size(parameter.getLimit());

        // 정렬
        if ( "r".equals(parameter.getOrderby()) ) {
            searchSourceBuilder.sort(new FieldSortBuilder("_score").order(SortOrder.DESC));
        } else {
            searchSourceBuilder.sort(new FieldSortBuilder("udt_dt").order(SortOrder.DESC));
        }

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
            return null;
        }

        return searchHits;
    }

    /**
     * Group by 함수
     * @param indexName
     * @return
     */
    public Aggregations aggregation(String indexName) {

        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("by_company")
                                                                        .field("company")
                                                                        .size(100);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.aggregation(aggregationBuilder);

        SearchRequest request = new SearchRequest(indexName);
        request.source(searchSourceBuilder);

        SearchResponse response = null;
        Aggregations aggregations = null;

        try(RestHighLevelClient client = createConnection();){

            response = client.search(request, RequestOptions.DEFAULT);
            aggregations = response.getAggregations();

        }catch (Exception e) {
            /*
             * 예외처리
             */
            e.printStackTrace();
            return null;
        }

        return aggregations;
    }
}
