package com.zzangho.project.springboot.domain.qna;

import com.zzangho.project.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
public class TbBoard {
    @Id // 해당 테이블의 PK 필드
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK생성 규칙, 해당 설정은 auto_increment
    private Long seq;
    private String category_id;
    private String ttl;
    private String contents;
    private String writer;
    private String kwd;

    @Column(updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime reg_dt;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime udt_dt;

    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'N'")
    private String del_yn;

    @Builder
    public TbBoard(        String category_id,
                           String ttl,
                           String contents,
                           String writer,
                           String kwd,
                           LocalDateTime reg_dt,
                           LocalDateTime udt_dt,
                           String del_yn) {
        this.category_id = category_id;
        this.ttl = ttl;
        this.contents = contents;
        this.writer = writer;
        this.kwd = kwd;
        this.reg_dt = reg_dt;
        this.udt_dt = udt_dt;
        this.del_yn = del_yn;
    }

    public void update(String category_id, String ttl, String contents) {
        this.category_id = category_id;
        this.ttl = ttl;
        this.contents = contents;
    }
}
