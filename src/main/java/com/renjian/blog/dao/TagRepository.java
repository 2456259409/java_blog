package com.renjian.blog.dao;

import com.renjian.blog.module.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Long> {

    public Tag getById(Long id);

    public Tag getByName(String name);

    @Query("select t from Tag t")
    public List<Tag> findTop(Pageable pageable);

    @Query("select t from Tag t where t.user.id=?1")
    Page<Tag> getMyTags(Long id,Pageable pageable);

}
