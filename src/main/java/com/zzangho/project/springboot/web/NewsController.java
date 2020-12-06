package com.zzangho.project.springboot.web;

import com.zzangho.project.springboot.config.auth.LoginUser;
import com.zzangho.project.springboot.config.auth.dto.SessionUser;
import com.zzangho.project.springboot.domain.common.Parameter;
import com.zzangho.project.springboot.domain.news.News;
import com.zzangho.project.springboot.service.elasticSearch.ESservice;
import com.zzangho.project.springboot.service.posts.PostsService;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 엘라스틱 서치
 */
@RequiredArgsConstructor
@Controller
public class NewsController {

    private final ESservice eSservice;

    @GetMapping("/newsList")
    public String newsList(@ModelAttribute Parameter parameter, Model model, @LoginUser SessionUser user) {

        Map<String, Object> resultMap = new HashMap<>();
        List<News> politicsList = new ArrayList<>();
        List<News> economyList = new ArrayList<>();
        List<News> societyList = new ArrayList<>();
        List<News> cultureList = new ArrayList<>();
        List<News> worldList = new ArrayList<>();
        List<News> itList = new ArrayList<>();

        SearchHits politicsSearchHit = null;
        SearchHits economySearchHit = null;
        SearchHits societySearchHit = null;
        SearchHits cultureSearchHit = null;
        SearchHits worldSearchHit = null;
        SearchHits itSearchHit = null;

        // 페이지 번호가 비어잇을 경우 0으로 셋팅
        if (StringUtils.isEmpty(parameter.getPageNum()) ) {
            parameter.setPageNum(0);
        }

        // limit이 비어있을 경우 10으로 셋팅
        if (StringUtils.isEmpty(parameter.getLimit()) ) {
            parameter.setLimit(10);
        }

        // 키워드가 비어있을 경우 전체 검색
        if ( StringUtils.isEmpty(parameter.getKwd()) ) {
            politicsSearchHit = eSservice.matchAll("naver_news", parameter, "정치");
            economySearchHit = eSservice.matchAll("naver_news", parameter, "경제");
            societySearchHit = eSservice.matchAll("naver_news", parameter, "사회");
            cultureSearchHit = eSservice.matchAll("naver_news", parameter, "생활/문화");
            worldSearchHit = eSservice.matchAll("naver_news", parameter, "세계");
            itSearchHit = eSservice.matchAll("naver_news", parameter, "IT/과학");
        } else {
            politicsSearchHit = eSservice.boolQuery("naver_news", parameter, "정치");
            economySearchHit = eSservice.boolQuery("naver_news", parameter, "경제");
            societySearchHit = eSservice.boolQuery("naver_news", parameter, "사회");
            cultureSearchHit = eSservice.boolQuery("naver_news", parameter, "생활/문화");
            worldSearchHit = eSservice.boolQuery("naver_news", parameter, "세계");
            itSearchHit = eSservice.boolQuery("naver_news", parameter, "IT/과학");
        }

        // 정치
        if ( politicsSearchHit != null ) {
            System.out.println("politicsSearchHit is not null [" + politicsSearchHit.getTotalHits() + "]");
            for (SearchHit searchHit : politicsSearchHit) {
                Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                News news = new News();

                news.setContents_id((Integer) sourceAsMap.get("contents_id"));
                news.setDomain((String) sourceAsMap.get("domain"));
                news.setCategory_nm((String) sourceAsMap.get("category_nm"));
                news.setTitle((String) sourceAsMap.get("title"));
                news.setContents((String) sourceAsMap.get("contents"));
                news.setWriter((String) sourceAsMap.get("writer"));
                news.setDate((String) sourceAsMap.get("date"));
                news.setAmpm((String) sourceAsMap.get("ampm"));
                news.setTime((String) sourceAsMap.get("time"));
                news.setCompany((String) sourceAsMap.get("company"));
                news.setUdt_dt((String) sourceAsMap.get("udt_dt"));

                politicsList.add(news);
                news = null;
            }

            model.addAttribute("politicsTotal", politicsSearchHit.getTotalHits());
            model.addAttribute("politicsList", politicsList);
        }

        // 경제
        if ( economySearchHit != null ) {
            System.out.println("economySearchHit is not null");
            for (SearchHit searchHit : economySearchHit) {
                Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                News news = new News();

                news.setContents_id((Integer) sourceAsMap.get("contents_id"));
                news.setDomain((String) sourceAsMap.get("domain"));
                news.setCategory_nm((String) sourceAsMap.get("category_nm"));
                news.setTitle((String) sourceAsMap.get("title"));
                news.setContents((String) sourceAsMap.get("contents"));
                news.setWriter((String) sourceAsMap.get("writer"));
                news.setDate((String) sourceAsMap.get("date"));
                news.setAmpm((String) sourceAsMap.get("ampm"));
                news.setTime((String) sourceAsMap.get("time"));
                news.setCompany((String) sourceAsMap.get("company"));
                news.setUdt_dt((String) sourceAsMap.get("udt_dt"));

                economyList.add(news);
                news = null;
            }

            model.addAttribute("economyTotal", economySearchHit.getTotalHits());
            model.addAttribute("economyList", economyList);
        }

        // 사회
        if ( societySearchHit != null ) {
            System.out.println("societySearchHit is not null");
            for (SearchHit searchHit : societySearchHit) {
                Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                News news = new News();

                news.setContents_id((Integer) sourceAsMap.get("contents_id"));
                news.setDomain((String) sourceAsMap.get("domain"));
                news.setCategory_nm((String) sourceAsMap.get("category_nm"));
                news.setTitle((String) sourceAsMap.get("title"));
                news.setContents((String) sourceAsMap.get("contents"));
                news.setWriter((String) sourceAsMap.get("writer"));
                news.setDate((String) sourceAsMap.get("date"));
                news.setAmpm((String) sourceAsMap.get("ampm"));
                news.setTime((String) sourceAsMap.get("time"));
                news.setCompany((String) sourceAsMap.get("company"));
                news.setUdt_dt((String) sourceAsMap.get("udt_dt"));

                societyList.add(news);
                news = null;
            }

            model.addAttribute("societyTotal", societySearchHit.getTotalHits());
            model.addAttribute("societyList", societyList);
        }

        // 생활/문화
        if ( cultureSearchHit != null ) {
            System.out.println("cultureSearchHit is not null");
            for (SearchHit searchHit : cultureSearchHit) {
                Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                News news = new News();

                news.setContents_id((Integer) sourceAsMap.get("contents_id"));
                news.setDomain((String) sourceAsMap.get("domain"));
                news.setCategory_nm((String) sourceAsMap.get("category_nm"));
                news.setTitle((String) sourceAsMap.get("title"));
                news.setContents((String) sourceAsMap.get("contents"));
                news.setWriter((String) sourceAsMap.get("writer"));
                news.setDate((String) sourceAsMap.get("date"));
                news.setAmpm((String) sourceAsMap.get("ampm"));
                news.setTime((String) sourceAsMap.get("time"));
                news.setCompany((String) sourceAsMap.get("company"));
                news.setUdt_dt((String) sourceAsMap.get("udt_dt"));

                cultureList.add(news);
                news = null;
            }

            model.addAttribute("cultureTotal", cultureSearchHit.getTotalHits());
            model.addAttribute("cultureList", cultureList);
        }

        // 세계
        if ( worldSearchHit != null ) {
            System.out.println("worldSearchHit is not null");
            for (SearchHit searchHit : worldSearchHit) {
                Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                News news = new News();

                news.setContents_id((Integer) sourceAsMap.get("contents_id"));
                news.setDomain((String) sourceAsMap.get("domain"));
                news.setCategory_nm((String) sourceAsMap.get("category_nm"));
                news.setTitle((String) sourceAsMap.get("title"));
                news.setContents((String) sourceAsMap.get("contents"));
                news.setWriter((String) sourceAsMap.get("writer"));
                news.setDate((String) sourceAsMap.get("date"));
                news.setAmpm((String) sourceAsMap.get("ampm"));
                news.setTime((String) sourceAsMap.get("time"));
                news.setCompany((String) sourceAsMap.get("company"));
                news.setUdt_dt((String) sourceAsMap.get("udt_dt"));

                worldList.add(news);
                news = null;
            }

            model.addAttribute("worldTotal", worldSearchHit.getTotalHits());
            model.addAttribute("worldList", worldList);
        }

        // IT/과학
        if ( itSearchHit != null ) {
            System.out.println("itSearchHit is not null");
            for (SearchHit searchHit : itSearchHit) {
                Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                News news = new News();

                news.setContents_id((Integer) sourceAsMap.get("contents_id"));
                news.setDomain((String) sourceAsMap.get("domain"));
                news.setCategory_nm((String) sourceAsMap.get("category_nm"));
                news.setTitle((String) sourceAsMap.get("title"));
                news.setContents((String) sourceAsMap.get("contents"));
                news.setWriter((String) sourceAsMap.get("writer"));
                news.setDate((String) sourceAsMap.get("date"));
                news.setAmpm((String) sourceAsMap.get("ampm"));
                news.setTime((String) sourceAsMap.get("time"));
                news.setCompany((String) sourceAsMap.get("company"));
                news.setUdt_dt((String) sourceAsMap.get("udt_dt"));

                itList.add(news);
                news = null;
            }

            model.addAttribute("itTotal", itSearchHit.getTotalHits());
            model.addAttribute("itList", itList);
        }

        // session값
        if ( user != null ) {
            model.addAttribute("profile", user.getPicture());   // profile 사진
            model.addAttribute("name", user.getName()); // user name
        }

        model.addAttribute("pageNum", 0);
        model.addAttribute("limit", 10);

        return "news/newsList";
    }
}
