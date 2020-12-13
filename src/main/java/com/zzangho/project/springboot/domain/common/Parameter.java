package com.zzangho.project.springboot.domain.common;

import lombok.Data;

@Data
public class Parameter {
    private String kwd;
    private String category;
    private String company;
    private String orderby;
    private Integer pageNum;
    private Integer limit;
}
