package com.zzangho.project.springboot.web.dto.news;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * News DTO 클래스
 *  -. Info Class : 뉴스의 정보를 return
 */
public class NewsDto {

    /**
     * 뉴스 데이터 정보
     */
    @Getter
    @AllArgsConstructor
    @Builder
    public static class Info {
        private int contents_id;
        private String domain;
        private String category_nm;
        private String title;
        private String contents;
        private String writer;
        private String date;
        private String ampm;
        private String time;
        private String company;
        private String udt_dt;
        private String url;
    }

    /**
     * 뉴스 요청 파라미터
     */
    @Getter
    @Setter
    public static class Request {
        private int contents_id;
        private String domain;
        private String category;
        private String title;
        private String contents;
        private String writer;
        private String date;
        private String ampm;
        private String time;
        private String company;
        private String udt_dt;
        private String url;

        private String kwd;
        private String orderby;
        private Integer pageNum;
        private Integer limit;
    }

    /**
     * 뉴스 전달 파라미터
     */
    @Getter
    @AllArgsConstructor
    public static class Response {
        private Info info;
        private int returnCode;
        private String returnMessage;
    }

}
