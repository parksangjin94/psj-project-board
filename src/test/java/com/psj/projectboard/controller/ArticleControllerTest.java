package com.psj.projectboard.controller;

import com.psj.projectboard.config.SecurityConfig;
import com.psj.projectboard.dto.ArticleWithCommentsDto;
import com.psj.projectboard.dto.UserAccountDto;
import com.psj.projectboard.service.ArticleService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Set;

@DisplayName("View 컨트롤러 - 게시글")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class) // () 안의 컨트롤러만 읽어들인다.
class ArticleControllerTest {

    private final MockMvc mvc;

    @MockBean private ArticleService articleService;
    /* @MockBean :
       @WebMvcTest를 이용한 테스트에서 사용할 수 있습니다.
       @WebMvcTest는 Controller를 테스트할 때 주로 이용되며, 단일 클래스의 테스트를 진행하므로 @MockBean을 통해 가짜 객체를 만들어 준다. => Controller객체까지만 생성되고 Serivce 객체는 생성하지 않는다.
       @MockBean은 위와 같이 Bean 컨테이너에 객체(Service)가 있어야 다른 객체(Controller)와 협력할 수 있는데, 객체를 만들 수 없는 경우(@WebMvcTest)에 사용할 수 있다. */

    public ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

//    @Disabled("구현 중") // 테스트를 통과하지 못할 시 빌드가 실패하기 때문에 제외 처리
    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
        // Given
        BDDMockito.given(articleService.searchArticles(ArgumentMatchers.eq(null),ArgumentMatchers.eq(null), ArgumentMatchers.any(Pageable.class))).willReturn(Page.empty());
        // When & Then
        mvc.perform(MockMvcRequestBuilders.get("/articles"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.view().name("articles/index"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("articles")); // map에 ""키가 있는지 확인

        BDDMockito.then(articleService).should().searchArticles(ArgumentMatchers.eq(null),ArgumentMatchers.eq(null), ArgumentMatchers.any(Pageable.class));
    }

//    @Disabled("구현 중") // 테스트를 통과하지 못할 시 빌드가 실패하기 때문에 처리
    @DisplayName("[view][GET] 게시글 상세 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleView_thenReturnsArticleView() throws Exception {
        // Given
        Long articleId = 1l;
        BDDMockito.given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentsDto());

        // When & Then
        mvc.perform(MockMvcRequestBuilders.get("/articles/"+articleId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.view().name("articles/detail"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("article")) // map에 ""키가 있는지 확인
                .andExpect(MockMvcResultMatchers.model().attributeExists("articleComments"));
        BDDMockito.then(articleService).should().getArticle(articleId);
    }

    @Disabled("구현 중") // 테스트를 통과하지 못할 시 빌드가 실패하기 때문에 처리
    @DisplayName("[view][GET] 게시글 검색 전용 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleSearchView_thenReturnsArticleSearchView() throws Exception {

        // Given

        // When & Then
        mvc.perform(MockMvcRequestBuilders.get("/articles/search"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.view().name("articles/search"));
    }

    @Disabled("구현 중") // 테스트를 통과하지 못할 시 빌드가 실패하기 때문에 처리
    @DisplayName("[view][GET] 게시글 해시태그 검색 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleHashtagSearchView_thenReturnsArticleHashtagSearchView() throws Exception {

        // Given

        // When & Then
        mvc.perform(MockMvcRequestBuilders.get("/articles/search-hashtag"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.view().name("articles/search-hashtag"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("hashtag"));
    }

    private ArticleWithCommentsDto createArticleWithCommentsDto(){
        return ArticleWithCommentsDto.of(
                1l,
                createUserAccountDto(),
                Set.of(),
                "title",
                "content",
                "#SPRING",
                LocalDateTime.now(),
                "sj",
                LocalDateTime.now(),
                "sj"
        );
    }

    private UserAccountDto createUserAccountDto(){
        return UserAccountDto.of(
                1l,
                "sj",
                "1234",
                "sj@gamil.com",
                "SJ",
                "memo",
                LocalDateTime.now(),
                "sj",
                LocalDateTime.now(),
                "sj"
        );
    }

}