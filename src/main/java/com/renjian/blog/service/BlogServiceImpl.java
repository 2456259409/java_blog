package com.renjian.blog.service;

import com.renjian.blog.NotFoundException;
import com.renjian.blog.dao.BlogRepository;
import com.renjian.blog.module.Blog;
import com.renjian.blog.module.BlogQuery;
import com.renjian.blog.module.Type;
import com.renjian.blog.util.MarkdownUtils;
import com.renjian.blog.util.MyExecutor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;


@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;
    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getById(id);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

                List<Predicate> predicates=new ArrayList<>();
                if(!"".equals(blog.getTitle()) && blog.getTitle()!=null){
                    predicates.add(cb.like(root.<String>get("title"),"%"+blog.getTitle()+"%"));
                }
                if(blog.getTypeId()!=null){
                    predicates.add(cb.equal(root.<Type>get("type").get("id"),blog.getTypeId()));
                }

                if(blog.isRecommend()){
                    predicates.add(cb.equal(root.<Boolean>get("recommend"),blog.isRecommend()));
                }

                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }


    @Override
    public Page<Blog> listBlog(Pageable pageable,Long tagId){
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("tags");
                return cb.equal(join.get("id"),tagId);
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query,pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Blog saveBlog(Blog blog) {
        if(blog.getId()==null){
            blog.setCreateTime(new Date());
            blog.setViews(0);
            blog.setUpdateTime(new Date());
        }else {

            Blog bg = blogRepository.getById(blog.getId());
            blog.setViews(bg.getViews());
            blog.setCreateTime(bg.getCreateTime());
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Blog updateBlog(Long id, Blog blog) {

        Blog b=blogRepository.getById(id);
        if(b==null){
            throw new NotFoundException("不存在该博客");
        }
        BeanUtils.copyProperties(b,blog);
        return blogRepository.save(b);
    }

    @Override
    public List<Blog> listRecommendBlogs(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC,"updateTime");
        Pageable pageable=new PageRequest(0,size,sort);
        return blogRepository.findTop(pageable);
    }

    @Override
    public Blog getAndConvert(Long id) {

        Blog blog=blogRepository.getById(id);
        if(blog==null){
            throw new NotFoundException("博客不存在");
        }
        Blog b =new Blog();
        BeanUtils.copyProperties(blog,b);
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(blog.getContent()));
        CompletableFuture.runAsync(()->{
            blogRepository.updateViews(id);
        }, MyExecutor.getExecutor());
        return b;
    }

    @Override
    public List<Blog> getByTypeId(Long id) {
        return blogRepository.getByTypeId(id);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        Map<String, List<Blog>> map=new HashMap<>();
        List<String> years=blogRepository.findGroupByYear();
        for(String year:years){
            map.put(year,blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }

    @Override
    public Page<Blog> getMyBlogs(Long userId,Pageable pageable) {
        return blogRepository.getByUserId(userId,pageable);
    }
}
