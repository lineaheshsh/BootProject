package com.zzangho.project.springboot.web;

import com.zzangho.project.springboot.config.auth.LoginUser;
import com.zzangho.project.springboot.config.auth.dto.SessionUser;
import com.zzangho.project.springboot.domain.common.Parameter;
import com.zzangho.project.springboot.service.elasticSearch.ESservice;
import com.zzangho.project.springboot.service.news.qna.QnAService;
import com.zzangho.project.springboot.web.dto.news.NewsDto;
import com.zzangho.project.springboot.web.dto.news.QnA;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @GetMapping("/qnaList")
    public String qnaList(@ModelAttribute Parameter parameter, Model model, @LoginUser SessionUser user) {

        // session값
        if ( user != null ) {
            model.addAttribute("profile", user.getPicture());   // profile 사진
            model.addAttribute("name", user.getName()); // user name
        }

        return "qna/qnaList";
    };

    @GetMapping("/qnaAdd")
    public String qnaAdd(@ModelAttribute Parameter parameter, Model model, @LoginUser SessionUser user) {

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
    @PutMapping("/qnaAdd")
    @ResponseBody
    public String qnaAddDocument(@RequestBody QnA.TbBoardRequestDto tbBoardRequestDto, @LoginUser SessionUser user) {

        return "qna/qnaAdd";
    }
}
