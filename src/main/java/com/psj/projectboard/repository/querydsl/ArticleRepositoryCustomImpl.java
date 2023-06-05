package com.psj.projectboard.repository.querydsl;

import com.psj.projectboard.domain.Article;
import com.psj.projectboard.domain.QArticle;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ArticleRepositoryCustomImpl extends QuerydslRepositorySupport implements ArticleRepositoryCustom {

    public ArticleRepositoryCustomImpl() {
        super(Article.class);  // ArticleRepository라는 것을 명시 , 생성자
    }

    @Override
    public List<String> findAllDistinctHashtags() {
        QArticle article = QArticle.article;

        JPQLQuery<String> query = from(article)     // < > generictype을 리턴 타입과 일치 시킨다.,  from(테이블)
                .distinct()   // 중복 값 제거
                .select(article.hashtag) // article테이블의 hashtag 컬럼만 내보낸다.
                .where(article.hashtag.isNotNull()); // hashtag가 null일 때를 제외
        return query.fetch();
    }
}
