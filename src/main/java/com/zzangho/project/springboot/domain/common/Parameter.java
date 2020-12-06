package com.zzangho.project.springboot.domain.common;

import lombok.Data;

@Data
public class Parameter {
    private String kwd;
    private String category;
    private Integer pageNum;
    private Integer limit;
}
