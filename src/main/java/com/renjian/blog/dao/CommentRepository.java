package com.renjian.blog.dao;

import com.renjian.blog.module.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

     List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);

     Comment getById(Long id);
}
