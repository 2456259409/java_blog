package com.renjian.blog.service;

import com.renjian.blog.module.Blog;
import com.renjian.blog.module.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {

    Blog getBlog(Long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlog(String query,Pageable pageable);

    Blog saveBlog(Blog blog);

    void deleteBlog(Long id);

    Blog updateBlog(Long id,Blog blog);

    List<Blog> listRecommendBlogs(Integer size);

    Blog getAndConvert(Long id);

    Page<Blog> listBlog(Pageable pageable,Long tagId);

    List<Blog> getByTypeId(Long id);

    Map<String,List<Blog>> archiveBlog();

    Long countBlog();

    Page<Blog> getMyBlogs(Long userId,Pageable pageable);



}
