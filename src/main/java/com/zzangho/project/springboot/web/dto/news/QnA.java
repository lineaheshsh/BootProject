package com.zzangho.project.springboot.web.dto.news;

import com.zzangho.project.springboot.domain.posts.Posts;
import com.zzangho.project.springboot.domain.qna.TbBoardCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

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
        public TbBoardCategoryRequestDto(String category_id, String category_nm, String user_id, String reg_dt, String udt_dt, String del_yn) {
            this.category_id = category_id;
            this.category_nm = category_nm;
            this.user_id = user_id;
        }

        public TbBoardCategory toEntity() {
            return TbBoardCategory.builder()
                    .category_id(category_id)
                    .category_nm(category_nm)
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
            this.category_id = entity.getCategory_id();
            this.category_nm = entity.getCategory_nm();
            this.user_id = entity.getUser_id();
            this.reg_dt = entity.getReg_dt();
            this.udt_dt = entity.getUdt_dt();
            this.del_yn = entity.getDel_yn();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class TbBoardRequestDto {
        private int seq;
        private String board_id;
        private String category_nm;
        private String contents;
        private String kwd;
        private String ttl;
        private String writer;

        @Builder
        public TbBoardRequestDto(String board_id, String category_nm, String contents, String kwd, String ttl, String writer) {
            this.board_id = board_id;
            this.category_nm = category_nm;
            this.contents = contents;
            this.kwd = kwd;
            this.ttl = ttl;
            this.writer = writer;
        }

        /*public TbBoardCategory toEntity() {
            return TbBoardCategory.builder()
                    .board_id(board_id)
                    .category_nm(category_nm)
                    .user_id(user_id)
                    .del_yn(del_yn)
                    .build();
        }*/
    }
}
