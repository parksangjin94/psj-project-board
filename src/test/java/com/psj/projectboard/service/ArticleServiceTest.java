package com.psj.projectboard.service;

import com.psj.projectboard.domain.Article;
import com.psj.projectboard.domain.type.SearchType;
import com.psj.projectboard.dto.ArticleDto;
import com.psj.projectboard.dto.ArticleUpdateDto;
import com.psj.projectboard.repository.ArticleRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks private ArticleService sut; // 테스트 대상, 목을 주입하는 대상

    @Mock private ArticleRepository articleRepository; // 의존 대상

    @DisplayName("게시글을 검색하면, 게시글 리스트를 반환한다.")
    @Test
    void givenSearchParameters_whenSearchingArticles_thenReturnsArticleList(){
        // Given

        // Pageble을 파라미터로하여 가져온 결과물은 Page<SomeObject> 형태로 반환 되며,
        // Page를 사용한다면 대부분 다수의 row를 가져오기 때문에 Page<List<SomeObject>>의 형태로 반환을 한다.
        // 이 페이지 객체에는 Pagination을 구현할 때 사용하면 좋은 메서드가 있다.

        // When
        Page<ArticleDto> articles = sut.searchArticles(SearchType.TITLE,"search keyword");

        // Then
        assertThat(articles).isNotNull();
    }

    @DisplayName("게시글을 조회하면, 게시글을 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticle_thenReturnsArticle(){
        // Given
        Long articleId = 1l;

        // When
        ArticleDto articles = sut.searchArticle(articleId);

        // Then
        assertThat(articles).isNotNull();
    }

    @DisplayName("게시글 정보를 입력하면, 게시글을 생성한다.")
    @Test
    void givenArticleInfo_whenSavingArticle_thenSavesArticle(){
        // Given
        ArticleDto dto = ArticleDto.of(LocalDateTime.now(),"SJ","title","content","#SPRING");
        BDDMockito.given(articleRepository.save(ArgumentMatchers.any(Article.class))).willReturn(null);

        // When
        sut.saveArticle(dto);

        // Then
        BDDMockito.then(articleRepository).should().save(ArgumentMatchers.any(Article.class));
    }

    @DisplayName("게시글의 ID와 수정 정보를 입력하면, 게시글을 수정한다.")
    @Test
    void givenArticleIdAndModifiedInfo_whenUpdatingArticle_thenUpdatesArticle(){
        // Given
        ArticleUpdateDto dto = ArticleUpdateDto.of("new title","new content","#SPRING");
        BDDMockito.given(articleRepository.save(ArgumentMatchers.any(Article.class))).willReturn(null);

        // When
        sut.updateArticle(1L, dto);

        // Then
        BDDMockito.then(articleRepository).should().save(ArgumentMatchers.any(Article.class));
    }

    @DisplayName("게시글의 ID를 입력하면, 게시글을 삭제한다.")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticle(){
        // Given

        BDDMockito.willDoNothing().given(articleRepository).delete(ArgumentMatchers.any(Article.class));

        // When
        sut.deleteArticle(1l);

        // Then
        BDDMockito.then(articleRepository).should().delete(ArgumentMatchers.any(Article.class));
    }

}