package com.zzangho.project.springboot.service.news.qna;

import com.zzangho.project.springboot.domain.qna.TbBoardCategory;
import com.zzangho.project.springboot.domain.qna.TbBoardCategoryRepository;
import com.zzangho.project.springboot.web.dto.PostsListResponseDto;
import com.zzangho.project.springboot.web.dto.news.QnA;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class QnAService {
    private final TbBoardCategoryRepository tbBoardCategoryRepository;

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
}
