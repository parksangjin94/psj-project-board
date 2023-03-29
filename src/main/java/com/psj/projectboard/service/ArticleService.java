package com.psj.projectboard.service;

import com.psj.projectboard.domain.type.SearchType;
import com.psj.projectboard.dto.ArticleDto;
import com.psj.projectboard.dto.ArticleUpdateDto;
import com.psj.projectboard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor // 생성자 자동 생성
@Transactional // 트랜잭션으로 감싸지며, 메서드가 종료될 때 자동으로 롤백된다. 일련의 작업들을 묶어서 하나의 단위로 처리하고 싶다면 @Transactional을 활용하자.
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true) // reading만 할 뿐 변경하지 않기 때문에 readonly 사용
    public Page<ArticleDto> searchArticles(SearchType title, String search_keyword) {
        return Page.empty();
    }

    @Transactional(readOnly = true)
    public ArticleDto searchArticle(Long articleId) {
        return ArticleDto.of(LocalDateTime.now(),"sj","title","content","#45456");
    }

    public void saveArticle(ArticleDto dto) {

    }

    public void updateArticle(long articleId, ArticleUpdateDto dto) {
    }

    public void deleteArticle(long articleId) {
    }
}