package com.zzangho.project.springboot.domain.qna;

import org.springframework.data.jpa.repository.JpaRepository;


public interface TbBoardCategoryRepository extends JpaRepository<TbBoardCategory,Long> {
    TbBoardCategory findByCategoryId(String category_id);
}
