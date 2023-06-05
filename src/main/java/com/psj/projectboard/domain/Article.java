package com.psj.projectboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),
})

@Entity
public class Article extends AuditingFields  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @ManyToOne(optional = false) @JoinColumn(name = "userId") private UserAccount userAccount; // 유저 정보(ID)
    @Setter @Column(nullable = false) private String title; // 제목
    @Setter @Column(nullable = false, length = 10000) private String content; // 본문

    @Setter private String hashtag; // 해시태그

    @ToString.Exclude
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>(); // 양방향 바인딩

    // 메타 데이터 , 자동 세팅 가능하도록 설정, AuditingFields로 분리
//    @CreatedDate @Column(nullable = false) private LocalDateTime createdAt; // 생성일시
//    @CreatedBy @Column(nullable = false, length = 100) private String createdBy; // 생성자
//    @LastModifiedDate @Column(nullable = false) private LocalDateTime modifiedAt; // 수정일시
//    @LastModifiedBy @Column(nullable = false, length = 100) private String modifiedBy; // 수정자

    // 평소에는 오픈하지 않기 때문에 public을 사용하지 않는다.
    protected Article(){}

    private Article(UserAccount userAccount,String title, String content, String hashtag) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of (UserAccount userAccount,String title, String content, String hashtag) {
        return new Article(userAccount,title, content, hashtag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return id != null && id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
