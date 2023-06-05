package com.psj.projectboard.service;

import com.psj.projectboard.domain.Article;
import com.psj.projectboard.domain.type.SearchType;
import com.psj.projectboard.dto.ArticleDto;
import com.psj.projectboard.dto.ArticleWithCommentsDto;
import com.psj.projectboard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j // SLF4J(Simple Logging Facade for Java)는 이름에서 확인할 수 있듯이. java.util.logging, logback 및 log4j와 같은 다양한 로깅 프레임 워크에 대한 추상화(인터페이스) 역할을 하는 라이브러리.
@RequiredArgsConstructor // 생성자 자동 생성
@Transactional // 트랜잭션으로 감싸지며, 메서드가 종료될 때 자동으로 롤백된다. 일련의 작업들을 묶어서 하나의 단위로 처리하고 싶다면 @Transactional을 활용하자.
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true) // reading만 할 뿐 변경하지 않기 때문에 readonly 사용
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {

        if (searchKeyword == null || searchKeyword.isBlank()){  // isBlank : 스페이스바로 이루어진 검색어
            return articleRepository.findAll(pageable).map(ArticleDto::from);
        }

        return switch (searchType){  // break;를 안해도 된다.
            case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::from);  // entity를 dto로 맵핑 후 내보낸다.
            case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::from);
            case ID -> articleRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(ArticleDto::from);
            case NICKNAME -> articleRepository.findByUserAccount_NicknameContaining(searchKeyword, pageable).map(ArticleDto::from);
            case HASHTAG -> articleRepository.findByHashtag("#"+searchKeyword, pageable).map(ArticleDto::from);
        };
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleWithCommentsDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: "+articleId));
    }

    public void saveArticle(ArticleDto dto) {
      articleRepository.save(dto.toEntity());
    }

    public void updateArticle(ArticleDto dto) {
        try{
        Article article = articleRepository.getReferenceById(dto.id()); // getReferenceById() : () 안의 항목의 레퍼런스만을 가지고 온다 select쿼리 실행 X
        if (dto.title() != null) { article.setTitle(dto.title()); }   // not null 필드이기 때문에 방어 로직 추가
        if (dto.content() != null) { article.setContent(dto.content()); }
        article.setHashtag(dto.hashtag());
//      articleRepository.save(article); 트랜잭션으로 묶여 있기 때문에 트랜잭션 종료 시 영속성 컨텍스트가 변화를 감지하여 해당 부분의 쿼리 문을 실행한다. save할 필요 없음
        } catch (EntityNotFoundException e){
            log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다. - dto: {}",dto);
        }
    }

    public void deleteArticle(long articleId) {
        articleRepository.deleteById(articleId);
    }

    public long getArticleCount() { return articleRepository.count(); }

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticlesViaHashtag(String hashtag, Pageable pageable) {
        if ( hashtag==null || hashtag.isBlank() ){
            return Page.empty(pageable);
        }

        return articleRepository.findByHashtag(hashtag,pageable).map(ArticleDto::from);
    }

    public List<String> getHashtags() {
        return articleRepository.findAllDistinctHashtags();
    }
}
