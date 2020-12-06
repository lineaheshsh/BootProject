package com.zzangho.project.springboot.domain.news;

import lombok.Data;

@Data
public class News {

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
