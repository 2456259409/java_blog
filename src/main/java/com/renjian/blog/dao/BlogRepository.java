package com.renjian.blog.dao;

import com.renjian.blog.module.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog,Long> , JpaSpecificationExecutor<Blog> {

    Blog getById(Long id);

    @Query("select b from Blog b where b.recommend=true")
    List<Blog> findTop(Pageable pageable);

    @Query("select b from Blog b where b.title like ?1 or b.content like ?1")
    Page<Blog> findByQuery(String query,Pageable pageable);

    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update Blog b set b.views = b.views+1 where b.id = ?1")
    void updateViews(Long id);


    List<Blog> getByTypeId(Long typeId);

    @Query("select function('date_format',b.updateTime,'%Y') as year from Blog b group by function('date_format',b.updateTime,'%Y') order by year desc ")
    List<String> findGroupByYear();

    @Query("select b from Blog b where function('date_format',b.updateTime,'%Y')=?1")
    List<Blog> findByYear(String year);

    @Query("select b from Blog b where b.user.id=?1")
    Page<Blog> getByUserId(Long id,Pageable pageable);
}
