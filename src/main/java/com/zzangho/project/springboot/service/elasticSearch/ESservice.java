package com.zzangho.project.springboot.service.elasticSearch;

import com.zzangho.project.springboot.domain.common.Parameter;
import com.zzangho.project.springboot.web.dto.news.NewsDto;
import com.zzangho.project.springboot.web.dto.news.QnA;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
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
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

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
     * 인덱스 생성
     * @param indexName
     */
    public boolean createIndex(String indexName) {
        String INDEX_NAME = indexName;

        XContentBuilder settingsBuilder = null;

        // settings
        try {
            settingsBuilder = XContentFactory.jsonBuilder()
                                             .startObject()
                                                .field("number_of_shards", 5)
                                                .field("number_of_replicas", 1)
                                                .startObject("analysis")
                                                    .startObject("tokenizer")
                                                        .startObject("nori_user_dict")
                                                            .field("type", "nori_tokenizer")
                                                            .field("decompound_mode", "mixed")
                                                        .endObject()
                                                    .endObject()
                                                    .startObject("analyzer")
                                                        .startObject("my_analyzer")
                                                            .field("type", "custom")
                                                            .field("tokenizer", "nori_user_dict")
                                                        .endObject()
                                                    .endObject()
                                                .endObject()
                                             .endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        XContentBuilder indexBuilder = null;

        // mappings
        try {
            indexBuilder = XContentFactory.jsonBuilder().startObject()
                                                        .startObject("properties")
                                                            .startObject("seq")
                                                                .field("type", "byte")
                                                            .endObject()
                                                            .startObject("category_nm")
                                                                .field("type", "keyword")
                                                            .endObject()
                                                            .startObject("ttl")
                                                                .field("type", "text")
                                                                .field("analyzer", "my_analyzer")
                                                            .endObject()
                                                            .startObject("contents")
                                                                .field("type", "text")
                                                                .field("analyzer", "my_analyzer")
                                                            .endObject()
                                                            .startObject("writer")
                                                                .field("type", "keyword")
                                                            .endObject()
                                                            .startObject("kwd")
                                                                .field("type", "keyword")
                                                            .endObject()
                                                            .startObject("reg_dt")
                                                                .field("type", "keyword")
                                                            .endObject()
                                                            .startObject("udt_dt")
                                                                .field("type", "keyword")
                                                            .endObject()
                                                        .endObject()
                                                    .endObject();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // 인덱스 셋팅, 맵핑 설정
        CreateIndexRequest request = new CreateIndexRequest(INDEX_NAME);
        request.settings(settingsBuilder);
        request.mapping(indexBuilder);

        // alias 설정
        /*String ALIAS_NAME = "board";
        request.alias(new Alias(ALIAS_NAME));*/

        try(RestHighLevelClient client = createConnection();){
            CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
            boolean acknowledged = createIndexResponse.isAcknowledged();
            System.out.println("result :: " + acknowledged);
            return acknowledged;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        /*ActionListener<CreateIndexResponse> listener = new ActionListener<CreateIndexResponse>() {
            @Override
            public void onResponse(CreateIndexResponse createIndexResponse) {
                System.out.println("index Create Success. [" + indexName + "]");
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("index Create Fail!!!! [" + indexName + "]");
            }
        };

        try(RestHighLevelClient client = createConnection();){
            client.indices().createAsync(request, RequestOptions.DEFAULT, listener);

        }catch (Exception e) {
            *//*
             * 예외처리
             *//*
            e.printStackTrace();
        }*/
    };

    /**
     * 인덱스 삭제
     * @return
     */
    public boolean deleteIndex(String indexName) {
        String INDEX_NAME = indexName;

        DeleteIndexRequest request = new DeleteIndexRequest(INDEX_NAME);

        try(RestHighLevelClient client = createConnection();){
            AcknowledgedResponse deleteIndexResponse = client.indices().delete(request, RequestOptions.DEFAULT);
            boolean acknowledged = deleteIndexResponse.isAcknowledged();
            System.out.println("result :: " + acknowledged);
            return acknowledged;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 문서 추가
     */
    public boolean addDocument(String indexName, XContentBuilder builder) {
        String INDEX_NAME = indexName;

        String TYPE_NAME ="_doc";

        IndexRequest request = new IndexRequest(INDEX_NAME, TYPE_NAME);

        try(RestHighLevelClient client = createConnection();) {

            request.source(builder);

            IndexResponse response = client.index(request, RequestOptions.DEFAULT);

            if ( response != null && response.status().getStatus() == 201 ) return true;
            else return false;

        } catch (IOException e) {
            System.out.println("IO Error!");
            return false;
        } catch (ElasticsearchException e) {
            if ( e.status() == RestStatus.CONFLICT) {
                System.out.println("[Elasticsearch Error] 문서 생성에 실패하였습니다.");
            }
            return false;
        }
    }

    /**
     * 뉴스 전체 검색
     * @param indexName 인덱스명
     * @param parameter   파라메터
     * @return  SearchHits
     */
    public SearchHits search(String indexName, NewsDto.Request parameter, String category) {

        BoolQueryBuilder queryBuilders = QueryBuilders.boolQuery();

        // 키워드가 있을 경우
        if ( parameter.getKwd() != null && !"".equals(parameter.getKwd()) )
            queryBuilders.must(QueryBuilders.termQuery("title", parameter.getKwd()));
        else
            queryBuilders.must(QueryBuilders.matchAllQuery());

        if ( category != null && !"".equals(parameter.getCompany()) )
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
     * qna 전체 검색
     * @param indexName 인덱스명
     * @param parameter   파라메터
     * @return  SearchHits
     */
    public SearchHits qnaSearch(String indexName, QnA.TbBoardRequestDto parameter) {

        BoolQueryBuilder queryBuilders = QueryBuilders.boolQuery();

        if ( parameter.getKwd() != null && !"".equals(parameter.getKwd()) )
            queryBuilders.must(QueryBuilders.termQuery("seq", parameter.getSeq()));
        else
            queryBuilders.must(QueryBuilders.matchAllQuery());

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilders);
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);

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
    public SearchHits boolQuery(String indexName, NewsDto.Request parameter, String category) {

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
    public Aggregations aggregation(String indexName, int size, String aggFd, String searchFd, String searchKwd, boolean isOrder) {

        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms(aggFd)
                                                                        .field(aggFd)
                                                                        .size(size);

        if ( isOrder ) aggregationBuilder.order(BucketOrder.key(false));

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if ( !StringUtils.isEmpty(searchKwd) ) {
            searchSourceBuilder.query(QueryBuilders.termQuery(searchFd, searchKwd));
        }
        searchSourceBuilder.aggregation(aggregationBuilder);
        searchSourceBuilder.size(0);
        System.out.println(searchSourceBuilder.toString());

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
