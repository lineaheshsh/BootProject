package com.zzangho.project.springboot.web;

import com.zzangho.project.springboot.config.auth.LoginUser;
import com.zzangho.project.springboot.config.auth.dto.SessionUser;
import com.zzangho.project.springboot.domain.common.Parameter;
import com.zzangho.project.springboot.service.elasticSearch.ESservice;
import com.zzangho.project.springboot.service.news.qna.QnAService;
import com.zzangho.project.springboot.web.dto.news.NewsDto;
import com.zzangho.project.springboot.web.dto.news.QnA;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 엘라스틱 서치
 */
@RequiredArgsConstructor
@Controller
public class NewsController {

    private final ESservice eSservice;

    private final QnAService qnAService;

    @GetMapping("/newsList")
    public String newsList(@ModelAttribute NewsDto.Request parameter, Model model, @LoginUser SessionUser user) {

        Map<String, Object> resultMap = new HashMap<>();
        long total = 0;

        SearchHits politicsSearchHit = null;
        SearchHits economySearchHit = null;
        SearchHits societySearchHit = null;
        SearchHits cultureSearchHit = null;
        SearchHits worldSearchHit = null;
        SearchHits itSearchHit = null;

        // 페이지 번호가 비어잇을 경우 0으로 셋팅
        if (StringUtils.isEmpty(parameter.getPageNum()) ) parameter.setPageNum(0);

        // limit이 비어있을 경우 10으로 셋팅
        if (StringUtils.isEmpty(parameter.getLimit()) ) parameter.setLimit(10);

        // orderby가 비어있을 경우 r으로 셋팅
        if (StringUtils.isEmpty(parameter.getOrderby()) ) parameter.setOrderby("r");

        if ( StringUtils.isEmpty(parameter.getCategory()) || "POLITICS".equals(parameter.getCategory()) ) {
            politicsSearchHit = eSservice.search("nori_naver_news", parameter, "정치");

            // 정치
            if ( politicsSearchHit != null ) {

                total += politicsSearchHit.getTotalHits().value;
                model = setData(model, politicsSearchHit, "politicsList");
                model.addAttribute("politicsTotal", politicsSearchHit.getTotalHits().value);
            }
        }

        if ( StringUtils.isEmpty(parameter.getCategory()) || "ECONOMY".equals(parameter.getCategory()) ) {
            economySearchHit = eSservice.search("nori_naver_news", parameter, "경제");

            // 경제
            if ( economySearchHit != null ) {

                total += economySearchHit.getTotalHits().value;
                model = setData(model, economySearchHit, "economyList");
                model.addAttribute("economyTotal", economySearchHit.getTotalHits().value);
            }
        }

        if ( StringUtils.isEmpty(parameter.getCategory()) || "SOCIETY".equals(parameter.getCategory()) ) {
            societySearchHit = eSservice.search("nori_naver_news", parameter, "사회");

            // 사회
            if ( societySearchHit != null ) {

                total += societySearchHit.getTotalHits().value;
                model = setData(model, societySearchHit, "societyList");
                model.addAttribute("societyTotal", societySearchHit.getTotalHits().value);
            }
        }

        if ( StringUtils.isEmpty(parameter.getCategory()) || "CURTURE".equals(parameter.getCategory()) ) {
            cultureSearchHit = eSservice.search("nori_naver_news", parameter, "생활/문화");

            // 생활/문화
            if ( cultureSearchHit != null ) {

                total += cultureSearchHit.getTotalHits().value;
                model = setData(model, cultureSearchHit, "cultureList");
                model.addAttribute("cultureTotal", cultureSearchHit.getTotalHits().value);
            }
        }

        if ( StringUtils.isEmpty(parameter.getCategory()) || "WORLD".equals(parameter.getCategory()) ) {
            worldSearchHit = eSservice.search("nori_naver_news", parameter, "세계");

            // 세계
            if ( worldSearchHit != null ) {

                total += worldSearchHit.getTotalHits().value;
                model = setData(model, worldSearchHit, "worldList");
                model.addAttribute("worldTotal", worldSearchHit.getTotalHits().value);
            }
        }

        if ( StringUtils.isEmpty(parameter.getCategory()) || "IT".equals(parameter.getCategory()) ) {
            itSearchHit = eSservice.search("nori_naver_news", parameter, "IT/과학");

            // IT/과학
            if ( itSearchHit != null ) {

                total += itSearchHit.getTotalHits().value;
                model = setData(model, itSearchHit, "itList");
                model.addAttribute("itTotal", itSearchHit.getTotalHits().value);
            }
        }

        // session값
        if ( user != null ) {
            model.addAttribute("profile", user.getPicture());   // profile 사진
            model.addAttribute("name", user.getName()); // user name
        }

        Aggregations aggregations = eSservice.aggregation("nori_naver_news", 100, "company", "", "", false);
        if ( aggregations != null ) {
            List<Map<String, Object>> companyList = new ArrayList<>();
            Terms byCompanyAggregation = aggregations.get("company");

            // For each entry
            for (Terms.Bucket entry : byCompanyAggregation.getBuckets()) {
                Map<String, Object> companyMap = new HashMap<>();
                String key = entry.getKeyAsString();            // bucket key
                long docCount = entry.getDocCount();            // Doc count

                companyMap.put("company", key);
                companyMap.put("count", docCount);
                companyList.add(companyMap);
                companyMap = null;
            }
            model.addAttribute("companyList", companyList);
        }

        model.addAttribute("params", parameter);
        model.addAttribute("total", total);
        model.addAttribute("pageNum", 0);
        model.addAttribute("limit", 10);

        return "news/newsList";
    }

    /**
     * searchHit -> NewsDto.Info
     * @param model
     * @param searchHits
     * @param resultKey
     * @return
     */
    private Model setData(Model model, SearchHits searchHits, String resultKey) {

        List<NewsDto.Info> list = new ArrayList<>();
        for (SearchHit searchHit : searchHits) {
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();

            NewsDto.Info newsDto = NewsDto.Info.builder().contents_id((Integer) sourceAsMap.get("contents_id"))
                    .domain((String) sourceAsMap.get("domain"))
                    .category_nm((String) sourceAsMap.get("category_nm"))
                    .title((String) sourceAsMap.get("title"))
                    .contents((String) sourceAsMap.get("contents"))
                    .writer((String) sourceAsMap.get("writer"))
                    .date((String) sourceAsMap.get("date"))
                    .ampm((String) sourceAsMap.get("ampm"))
                    .time((String) sourceAsMap.get("time"))
                    .company((String) sourceAsMap.get("company"))
                    .udt_dt((String) sourceAsMap.get("udt_dt"))
                    .url((String) sourceAsMap.get("url"))
                    .build();

            list.add(newsDto);
            newsDto = null;
        }

        model.addAttribute(resultKey, list);

        return model;
    }


    //////////////////////////////////////////////////////////////////////////////////////////// Analysis
    @GetMapping("/newsAnalysis")
    public String newsAnalysis(@ModelAttribute Parameter parameter, Model model, @LoginUser SessionUser user) {

        // session값
        if ( user != null ) {
            model.addAttribute("profile", user.getPicture());   // profile 사진
            model.addAttribute("name", user.getName()); // user name
        }

        Aggregations aggregations = eSservice.aggregation("nori_naver_news", 10, "category_nm", "", "", false);
        if ( aggregations != null ) {
            List<Map<String, Object>> categoryList = new ArrayList<>();
            Terms byCategoryNMAggregation = aggregations.get("category_nm");

            // For each entry
            for (Terms.Bucket entry : byCategoryNMAggregation.getBuckets()) {
                Map<String, Object> categoryMap = new HashMap<>();
                String key = entry.getKeyAsString();            // bucket key

                categoryMap.put("name", key);
                categoryList.add(categoryMap);
                categoryMap = null;
            }
            model.addAttribute("categoryList", categoryList);
        }

        return "news/newsAnalysis";
    }

    @ResponseBody
    @GetMapping("/newsCompanyCount")
    public List<Map<String, Object>> newsCompanyCount(@LoginUser SessionUser user) {

        List<Map<String, Object>> companyList = new ArrayList<>();
        Aggregations aggregations = eSservice.aggregation("nori_naver_news", 10, "company", "", "", false);

        if ( aggregations != null ) {
            Terms byCompanyAggregation = aggregations.get("company");

            // For each entry
            for (Terms.Bucket entry : byCompanyAggregation.getBuckets()) {
                Map<String, Object> companyMap = new HashMap<>();
                String key = entry.getKeyAsString();            // bucket key
                long docCount = entry.getDocCount();            // Doc count

                companyMap.put("company", key);
                companyMap.put("count", docCount);
                companyList.add(companyMap);
                companyMap = null;
            }
        }

        return companyList;
    }

    @ResponseBody
    @GetMapping("/newsCompanyDateCount")
    public List<Map<String, Object>> newsCompanyDateCount(@LoginUser SessionUser user) {

        List<Map<String, Object>> dateCompanyList = new ArrayList<>();
        Aggregations agg_date = eSservice.aggregation("nori_naver_news", 10, "date", "domain", "NAVER", true);

        if ( agg_date != null ) {
            Terms dateAggregation = agg_date.get("date");

            // For each entry
            for (Terms.Bucket entry : dateAggregation.getBuckets()) {
                Map<String, Object> companyMap = new HashMap<>();
                String key = entry.getKeyAsString();            // bucket key
                long docCount = entry.getDocCount();            // Doc count

                companyMap.put("date", key);
                companyMap.put("count", docCount);
                dateCompanyList.add(companyMap);
                companyMap = null;
            }
        }

        // 날짜 정렬
        Collections.sort(dateCompanyList, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String date1 = (String) o1.get("date");
                String date2 = (String) o2.get("date");
                return date1.compareTo(date2);
            }
        });

        return dateCompanyList;
    }

    @ResponseBody
    @GetMapping("/newsHotKeyword")
    public List<Map<String, Object>> newsHotKeyword(@RequestParam String categoryNM, @LoginUser SessionUser user) {

        System.out.println("[" + categoryNM + "]");
        List<Map<String, Object>> hotKeywordList = new ArrayList<>();
        Aggregations agg_date = eSservice.aggregation("nori_naver_news", 100, "keywords", "category_nm", categoryNM, false);

        if ( agg_date != null ) {
            Terms dateAggregation = agg_date.get("keywords");

            // For each entry
            for (Terms.Bucket entry : dateAggregation.getBuckets()) {
                Map<String, Object> keywordMap = new HashMap<>();
                String key = entry.getKeyAsString();            // bucket key
                long docCount = entry.getDocCount();            // Doc count

                keywordMap.put("keyword", key);
                keywordMap.put("count", docCount);
                hotKeywordList.add(keywordMap);
                keywordMap = null;
            }
        }

        return hotKeywordList;
    }
}
