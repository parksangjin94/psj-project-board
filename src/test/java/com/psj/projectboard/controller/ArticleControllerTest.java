package com.psj.projectboard.controller;

import com.psj.projectboard.config.SecurityConfig;
import com.psj.projectboard.domain.type.SearchType;
import com.psj.projectboard.dto.ArticleWithCommentsDto;
import com.psj.projectboard.dto.UserAccountDto;
import com.psj.projectboard.service.ArticleService;
import com.psj.projectboard.service.PaginationService;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@DisplayName("View 컨트롤러 - 게시글")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class) // () 안의 컨트롤러만 읽어들인다.
class ArticleControllerTest {

    private final MockMvc mvc;

    @MockBean private ArticleService articleService;
    @MockBean private PaginationService paginationService;
    /* @MockBean :
       @WebMvcTest를 이용한 테스트에서 사용할 수 있다.
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
        BDDMockito.given(paginationService.getPaginationBarNumbers(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).willReturn(List.of(0, 1, 2, 3, 4));

        // When & Then
        mvc.perform(MockMvcRequestBuilders.get("/articles"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.view().name("articles/index"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("articles")) // map에 ""키가 있는지 확인
                .andExpect(MockMvcResultMatchers.model().attributeExists("paginationBarNumbers"));
        BDDMockito.then(articleService).should().searchArticles(ArgumentMatchers.eq(null),ArgumentMatchers.eq(null), ArgumentMatchers.any(Pageable.class));
        BDDMockito.then(paginationService).should().getPaginationBarNumbers(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 검색어와 함께 호출")
    @Test
    public void givenSearchKeyword_whenSearchingArticlesView_thenReturnsArticlesView() throws Exception {
        // Given
        SearchType searchType =SearchType.TITLE;
        String searchValue = "title";
        BDDMockito.given(articleService.searchArticles(ArgumentMatchers.eq(searchType),ArgumentMatchers.eq(searchValue),ArgumentMatchers.any(Pageable.class))).willReturn(Page.empty());
        BDDMockito.given(paginationService.getPaginationBarNumbers(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).willReturn(List.of(0, 1, 2, 3, 4));

        // When & Then
        mvc.perform(MockMvcRequestBuilders.get("/articles")
                        .queryParam("searchType", searchType.name()) // enum의 name()과 toString()은 모두 Enum의 값을 string으로 표현한다. name()과 toString()의 주요 차이점은 메소드의 재정의 가능 여부이다. name()은 final 메소드이므로 재정의할 수 없지만 toString()은 재정의가 가능하다.
                        .queryParam("searchValue", searchValue)   // queryparam : url을 통해 넘어온 데이터를 변수에 저장할수 있음
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.view().name("articles/index"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("articles")) // map에 ""키가 있는지 확인
                .andExpect(MockMvcResultMatchers.model().attributeExists("searchTypes"));
        BDDMockito.then(articleService).should().searchArticles(ArgumentMatchers.eq(searchType),ArgumentMatchers.eq(searchValue), ArgumentMatchers.any(Pageable.class));
        BDDMockito.then(paginationService).should().getPaginationBarNumbers(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 페이징, 정렬 기능")
    @Test
    void givenPagingAndSortingParams_whenSearchingArticlesView_thenReturnsArticlesView() throws Exception {
        // Given
        String sortName = "title";
        String direction = "desc";
        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc(sortName)));
        List<Integer> barNumbers = List.of(1, 2, 3, 4, 5);
        BDDMockito.given(articleService.searchArticles(null, null, pageable)).willReturn(Page.empty());
        BDDMockito.given(paginationService.getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages())).willReturn(barNumbers);

        // When & Then
        mvc.perform(
                        MockMvcRequestBuilders.get("/articles")
                                .queryParam("page", String.valueOf(pageNumber))
                                .queryParam("size", String.valueOf(pageSize))
                                .queryParam("sort", sortName + "," + direction)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.view().name("articles/index"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("articles"))
                .andExpect(MockMvcResultMatchers.model().attribute("paginationBarNumbers", barNumbers));
        BDDMockito.then(articleService).should().searchArticles(null, null, pageable);
        BDDMockito.then(paginationService).should().getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages());
    }


//    @Disabled("구현 중") // 테스트를 통과하지 못할 시 빌드가 실패하기 때문에 처리
    @DisplayName("[view][GET] 게시글 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleView_thenReturnsArticleView() throws Exception {
        // Given
        Long articleId = 1l;
        long totalCount = 1l;
        BDDMockito.given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentsDto());
        BDDMockito.given(articleService.getArticleCount()).willReturn(totalCount);


        // When & Then
        mvc.perform(MockMvcRequestBuilders.get("/articles/"+articleId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.view().name("articles/detail"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("article")) // map에 ""키가 있는지 확인
                .andExpect(MockMvcResultMatchers.model().attributeExists("articleComments"))
                .andExpect(MockMvcResultMatchers.model().attribute("totalCount", totalCount));
        BDDMockito.then(articleService).should().getArticle(articleId);
        BDDMockito.then(articleService).should().getArticleCount();
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