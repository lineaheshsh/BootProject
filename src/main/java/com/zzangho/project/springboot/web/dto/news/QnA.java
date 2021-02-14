package com.zzangho.project.springboot.web.dto.news;

import com.zzangho.project.springboot.domain.posts.Posts;
import com.zzangho.project.springboot.domain.qna.TbBoard;
import com.zzangho.project.springboot.domain.qna.TbBoardCategory;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * QnA DTO 클래스
 *  -. TbBoardCategoryRequestDto Class : QnA 카테고리 정보를 등록
 *  -. TbBoardCategoryResponseDto Class : QnA 카테고리 정보를 전달해 주는 클래스
 */
public class QnA {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class TbBoardCategoryRequestDto {
        private String category_id;
        private String category_nm;
        private String user_id;
        private String reg_dt;
        private String udt_dt;
        private String del_yn;

        @Builder
        public TbBoardCategoryRequestDto(String category_id, String category_nm, String user_id) {
            this.category_id = category_id;
            this.category_nm = category_nm;
            this.user_id = user_id;
        }

        public TbBoardCategory toEntity() {
            return TbBoardCategory.builder()
                    .categoryId(category_id)
                    .categoryNm(category_nm)
                    .user_id(user_id)
                    .del_yn(del_yn)
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class TbBoardCategoryResponseDto {
        private Long seq;
        private String category_id;
        private String category_nm;
        private String user_id;
        private Date reg_dt;
        private Date udt_dt;
        private String del_yn;

        @Builder
        public TbBoardCategoryResponseDto(TbBoardCategory entity) {
            this.seq = entity.getSeq();
            this.category_id = entity.getCategoryId();
            this.category_nm = entity.getCategoryNm();
            this.user_id = entity.getUser_id();
            this.reg_dt = entity.getReg_dt();
            this.udt_dt = entity.getUdt_dt();
            this.del_yn = entity.getDel_yn();
        }
    }

    ///////////////////////////////////////////////////////////////// Board
    @Getter
    @Setter
    @NoArgsConstructor
    public static class TbBoardRequestDto {
        private Long seq;
        private String category_id;
        private String contents;
        private String kwd;
        private String ttl;
        private String writer;
        private String doc_id;

        @Builder
        public TbBoardRequestDto(Long seq, String category_id, String contents, String kwd, String ttl, String writer, String doc_id) {
            this.seq = seq;
            this.category_id = category_id;
            this.contents = contents;
            this.kwd = kwd;
            this.ttl = ttl;
            this.writer = writer;
            this.doc_id = doc_id;
        }

        public TbBoard toEntity() {
            LocalDateTime date = LocalDateTime.now();

            return TbBoard.builder()
                    .category_id(category_id)
                    .contents(contents)
                    .ttl(ttl)
                    .writer(writer)
                    .reg_dt(date)
                    .udt_dt(date)
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class TbBoardResponseDto {
        private int returnCode;
        private String returnMessage;
        private List<TbBoardInfoDto> result;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class TbBoardInfoDto {
        private Long seq;
        private String category_id;
        private String contents;
        private String kwd;
        private String ttl;
        private String writer;
        private String reg_dt;
        private String udt_dt;
        private String doc_id;
    }
}
