package com.zzangho.project.springboot.web;

import com.zzangho.project.springboot.config.auth.LoginUser;
import com.zzangho.project.springboot.config.auth.dto.SessionUser;
import com.zzangho.project.springboot.domain.common.Parameter;
import com.zzangho.project.springboot.domain.qna.TbBoardCategory;
import com.zzangho.project.springboot.service.elasticSearch.ESservice;
import com.zzangho.project.springboot.service.news.qna.QnAService;
import com.zzangho.project.springboot.web.dto.news.NewsDto;
import com.zzangho.project.springboot.web.dto.news.QnA;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * 엘라스틱 서치
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/qna")
public class QnAController {

    private final ESservice eSservice;

    private final QnAService qnAService;

    @GetMapping("/categoryList")
    public String newsQnA(@ModelAttribute Parameter parameter, Model model, @LoginUser SessionUser user) {

        List<QnA.TbBoardCategoryResponseDto> categoryList = qnAService.findAll();

        // session값
        if ( user != null ) {
            model.addAttribute("profile", user.getPicture());   // profile 사진
            model.addAttribute("name", user.getName()); // user name
        }

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("categoryTotal", categoryList.size());

        return "qna/categoryList";
    }

    @GetMapping("/categoryAdd")
    public String newsQnAAdd(@ModelAttribute Parameter parameter, Model model, @LoginUser SessionUser user) {

        // session값
        if ( user != null ) {
            model.addAttribute("profile", user.getPicture());   // profile 사진
            model.addAttribute("name", user.getName()); // user name
        }

        return "qna/categoryAdd";
    }

    /**
     * QnA 게시판 카테고리 추가
     * @param tbBoardCategoryRequestDto
     * @param user
     * @return
     */
    @PutMapping("/categoryAdd")
    @ResponseBody
    public Map<String, String> categoryAdd(@RequestBody QnA.TbBoardCategoryRequestDto tbBoardCategoryRequestDto, @LoginUser SessionUser user) {
        System.out.println("categoryNM :: " + tbBoardCategoryRequestDto.getCategory_nm());

        Map<String, String> resultMap = new HashMap<>();
        String msg = "";

        boolean createIndex = eSservice.createIndex(tbBoardCategoryRequestDto.getCategory_nm());

        if ( createIndex ) {
            tbBoardCategoryRequestDto.setUser_id(user.getName());
            tbBoardCategoryRequestDto.setDel_yn("Y");
            System.out.println("dto :: " + tbBoardCategoryRequestDto.getDel_yn());
            Long insertId = qnAService.save(tbBoardCategoryRequestDto);

            if ( insertId > 0 ) {
                msg = "ok";
            } else {
                msg = "insert Failed";
            }
        } else {
            msg = "index create Failed";
        }

        resultMap.put("msg", msg);

        return resultMap;
    };

    @GetMapping("/board")
    public String boardList(@ModelAttribute QnA.TbBoardRequestDto tbBoardRequestDto, Model model, @LoginUser SessionUser user) {

        // qna 데이터 가져오기
        QnA.TbBoardResponseDto response = qnAService.qnaList(tbBoardRequestDto);

        // session값
        if ( user != null ) {
            model.addAttribute("profile", user.getPicture());   // profile 사진
            model.addAttribute("name", user.getName()); // user name
        }

        model.addAttribute("response", response);

        return "qna/qnaList";
    };

    /**
     * 게시판 등록페이지
     * @param model
     * @param user
     * @return
     */
    @GetMapping("/qnaAdd")
    public String qnaAdd(Model model, @LoginUser SessionUser user) {

        List<QnA.TbBoardCategoryResponseDto> categoryList = qnAService.findAll();
        model.addAttribute("categoryList", categoryList);

        // session값
        if ( user != null ) {
            model.addAttribute("profile", user.getPicture());   // profile 사진
            model.addAttribute("name", user.getName()); // user name
        }

        return "qna/qnaAdd";
    }

    /**
     * 게시판 데이터 추가
     * @param tbBoardRequestDto
     * @param user
     * @return
     */
    @PutMapping("/board")
    @ResponseBody
    public Map<String, String> qnaAddDocument(@RequestBody QnA.TbBoardRequestDto tbBoardRequestDto, @LoginUser SessionUser user) {

        Map<String, String> resultMap = new HashMap<>();
        String msg = "";

        // 카테고리 정보를 가져오기 위해 category_id로 조회
        QnA.TbBoardCategoryResponseDto responseDto = qnAService.findById(tbBoardRequestDto.getCategory_id());
        System.out.println(">>>>>>>>>>>>>>>>> " + responseDto.getCategory_nm());

        tbBoardRequestDto.setWriter(user.getName());
        boolean isAdd = qnAService.addDocument(responseDto.getCategory_nm(), tbBoardRequestDto);

        if ( isAdd ) {
            msg = "ok";
        } else {
            msg = "Add Document Fail";
        }

        resultMap.put("msg", msg);

        return resultMap;
    }

    /**
     * 게시판 수정페이지
     * @param model
     * @param user
     * @return
     */
    @GetMapping("/board/{seq}")
    public String qnaAdd(@PathVariable Long seq, Model model, @LoginUser SessionUser user) {

        List<QnA.TbBoardCategoryResponseDto> categoryList = qnAService.findAll();


        model.addAttribute("categoryList", categoryList);

        // session값
        if ( user != null ) {
            model.addAttribute("profile", user.getPicture());   // profile 사진
            model.addAttribute("name", user.getName()); // user name
        }

        return "qna/boardEdit";
    }
}
