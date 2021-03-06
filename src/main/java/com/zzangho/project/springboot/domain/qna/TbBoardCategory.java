package com.zzangho.project.springboot.domain.qna;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
public class TbBoardCategory {

    @Id // 해당 테이블의 PK 필드
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK생성 규칙, 해당 설정은 auto_increment
    private Long seq;
    @Column(name="category_id")
    private String categoryId;
    @Column(name="category_nm")
    private String categoryNm;
    private String user_id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date reg_dt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date udt_dt;

    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'N'")
    private String del_yn;

    @Builder
    public TbBoardCategory(String categoryId,
                           String categoryNm,
                           String user_id,
                           Date reg_dt,
                           Date udt_dt,
                           String del_yn) {
        this.categoryId = categoryId;
        this.categoryNm = categoryNm;
        this.user_id = user_id;
        this.reg_dt = reg_dt;
        this.udt_dt = udt_dt;
        this.del_yn = del_yn;
    }
}
