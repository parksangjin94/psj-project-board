package com.psj.projectboard.repository;

import com.psj.projectboard.domain.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment,Long> {
}
