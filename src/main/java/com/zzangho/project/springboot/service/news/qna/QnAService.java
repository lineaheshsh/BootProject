package com.zzangho.project.springboot.service.news.qna;

import com.zzangho.project.springboot.domain.qna.TbBoard;
import com.zzangho.project.springboot.domain.qna.TbBoardCategory;
import com.zzangho.project.springboot.domain.qna.TbBoardCategoryRepository;
import com.zzangho.project.springboot.domain.qna.TbBoardRepository;
import com.zzangho.project.springboot.service.elasticSearch.ESservice;
import com.zzangho.project.springboot.web.dto.news.QnA;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@RequiredArgsConstructor
@Service
public class QnAService {
    private final TbBoardCategoryRepository tbBoardCategoryRepository;

    private final TbBoardRepository tbBoardRepository;

    private final ESservice eSservice;

    // QnA 카테고리 정보 등록
    @Transactional
    public Long save(QnA.TbBoardCategoryRequestDto tbBoardCategoryRequestDto) {

        Long totalCount = findAllCount();

        if ( totalCount == 0 ) {
            tbBoardCategoryRequestDto.setCategory_id("01");
        } else {
            String tCategoryId = "";
            if ( totalCount < 10 ) {
                tCategoryId = "0" + String.valueOf(totalCount + 1);
            } else {
                tCategoryId = String.valueOf(totalCount + 1);
            }

            tbBoardCategoryRequestDto.setCategory_id(tCategoryId);
        }

        return tbBoardCategoryRepository.save(tbBoardCategoryRequestDto.toEntity()).getSeq();
    }

    // 전체 데이터 건수 조회
    public Long findAllCount() {
        return tbBoardCategoryRepository.count();
    }

    // 전체 데이터 조회
    @Transactional(readOnly = true)
    public List<QnA.TbBoardCategoryResponseDto> findAll() {
        return tbBoardCategoryRepository.findAll().stream()
                .map(QnA.TbBoardCategoryResponseDto::new)
                .collect(Collectors.toList());
    }

    // 데이터 한건 조회(id)
    @Transactional
    public QnA.TbBoardCategoryResponseDto findById(String category_id) {
        TbBoardCategory entity = tbBoardCategoryRepository.findByCategoryId(category_id);

        if ( entity == null ) {
            new IllegalArgumentException("해당 게시글이 없습니다. category_id=" + category_id);
            return null;
        }

        return new QnA.TbBoardCategoryResponseDto(entity);
    }

    /**
     * qna 게시판 데이터 추가
     * @param category_nm
     * @param tbBoardRequestDto
     * @return
     */
    @Transactional
    public boolean addDocument(String category_nm, QnA.TbBoardRequestDto tbBoardRequestDto) {

        boolean isAdd = false;

        System.out.println("tbBoardRepository save start");
        Long seq = tbBoardRepository.save(tbBoardRequestDto.toEntity()).getSeq();
        System.out.println("seq :: " + seq);

        if ( seq > 0 ) {

            // 엘라스틱서치에 넣을 데이터 불러오기
            Optional<TbBoard> tbBoard = tbBoardRepository.findById(seq);

            if ( tbBoard.isPresent() ) {

                try {
                    XContentBuilder builder = jsonBuilder()
                                                .startObject()
                                                    .field("seq", tbBoard.get().getSeq())
                                                    .field("category_nm", category_nm)
                                                    .field("contents", tbBoard.get().getContents())
                                                    .field("kwd", tbBoard.get().getKwd())
                                                    .field("ttl", tbBoard.get().getTtl())
                                                    .field("writer", tbBoard.get().getWriter())
                                                    .field("reg_dt", tbBoard.get().getReg_dt())
                                                    .field("udt_dt", tbBoard.get().getUdt_dt())
                                                .endObject();

                    System.out.println("addDocument Start");
                    eSservice.addDocument(category_nm, builder);
                    isAdd = true;
                    System.out.println("addDocument End");

                    return isAdd;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return isAdd;

    }

    @Transactional
    public boolean updateDocument(String category_nm, QnA.TbBoardRequestDto tbBoardRequestDto) {

        boolean isUpdate = false;

        System.out.println("tbBoardRepository update start");
        Optional<TbBoard> selBoard = tbBoardRepository.findById(tbBoardRequestDto.getSeq());

        selBoard.ifPresent(board -> {
            board.update(tbBoardRequestDto.getCategory_id(), tbBoardRequestDto.getTtl(), tbBoardRequestDto.getContents());
        });

        try {
            XContentBuilder builder = jsonBuilder()
                    .startObject()
                    .field("category_nm", category_nm)
                    .field("contents", tbBoardRequestDto.getContents())
                    .field("ttl", tbBoardRequestDto.getTtl())
                    .endObject();

            System.out.println("updateDocument Start");
            eSservice.updateDocument(category_nm, tbBoardRequestDto.getDoc_id(), builder);
            isUpdate = true;
            System.out.println("updateDocument End");

            return isUpdate;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isUpdate;

    }

    public QnA.TbBoardResponseDto qnaList(QnA.TbBoardRequestDto tbBoardRequestDto) {

        QnA.TbBoardResponseDto response = new QnA.TbBoardResponseDto();
        String message = "";
        int code = 0;

        SearchHits searchHits = eSservice.qnaSearch("alias_qna_category", tbBoardRequestDto);

        List<QnA.TbBoardInfoDto> list = new ArrayList<>();
        for (SearchHit searchHit : searchHits) {
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();

            QnA.TbBoardInfoDto tbBoardInfoDto = QnA.TbBoardInfoDto.builder()
                    .category_id(String.valueOf(sourceAsMap.get("category_nm")))
                    .ttl((String) sourceAsMap.get("ttl"))
                    .contents((String) sourceAsMap.get("contents"))
                    .writer((String) sourceAsMap.get("writer"))
                    .kwd("lee")
                    .seq(Long.valueOf((Integer) sourceAsMap.get("seq")))
                    .reg_dt((String) sourceAsMap.get("reg_dt"))
                    .udt_dt((String) sourceAsMap.get("udt_dt"))
                    .build();

            list.add(tbBoardInfoDto);
            tbBoardInfoDto = null;
        }

        if ( !list.isEmpty() ) {
            response.setResult(list);
            code = 200;
            message = "ok";
        } else {
            code = -1;
            message = "[Error] elastic search is alive?";
        }

        response.setReturnCode(code);
        response.setReturnMessage(message);

        return response;
    }

    public QnA.TbBoardResponseDto findByBoardId(QnA.TbBoardRequestDto tbBoardRequestDto) {
        QnA.TbBoardResponseDto response = new QnA.TbBoardResponseDto();
        String message = "";
        int code = 0;

        System.out.println("seq :: " + tbBoardRequestDto.getSeq());

        SearchHits searchHits = eSservice.qnaSearch("alias_qna_category", tbBoardRequestDto);

        List<QnA.TbBoardInfoDto> list = new ArrayList<>();
        for (SearchHit searchHit : searchHits) {
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();

            QnA.TbBoardInfoDto tbBoardInfoDto = QnA.TbBoardInfoDto.builder()
                    .category_id(String.valueOf(sourceAsMap.get("category_nm")))
                    .ttl((String) sourceAsMap.get("ttl"))
                    .contents((String) sourceAsMap.get("contents"))
                    .writer((String) sourceAsMap.get("writer"))
                    .kwd("lee")
                    .seq(Long.valueOf((Integer) sourceAsMap.get("seq")))
                    .reg_dt((String) sourceAsMap.get("reg_dt"))
                    .udt_dt((String) sourceAsMap.get("udt_dt"))
                    .doc_id(searchHit.getId())
                    .build();

            list.add(tbBoardInfoDto);
            tbBoardInfoDto = null;
        }

        if ( !list.isEmpty() ) {
            response.setResult(list);
            code = 200;
            message = "ok";
        } else {
            code = -1;
            message = "[Error] elastic search is alive?";
        }

        response.setReturnCode(code);
        response.setReturnMessage(message);

        return response;
    }

    @Transactional
    public boolean deleteDocument(QnA.TbBoardRequestDto tbBoardRequestDto) {

        boolean isDelete = false;

        System.out.println("tbBoardRepository delete start");
        tbBoardRepository.deleteById(tbBoardRequestDto.getSeq());
        System.out.println("tbBoardRepository delete end");

        isDelete = eSservice.deleteDocument("alias_qna_category", tbBoardRequestDto.getDoc_id());

        return isDelete;
    }
}
