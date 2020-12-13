package com.zzangho.project.springboot.web.dto.news;

import lombok.Data;

@Data
public class NewsDto {

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

}
