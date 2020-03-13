package com.renjian.blog.dao;

import com.renjian.blog.module.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypeRepository extends JpaRepository<Type,Long> {

    public Type getById(Long id);

    public Type getByName(String name);

    @Query("select t from Type t")
    public List<Type> findTop(Pageable pageable);

    @Query("select t from Type t where t.user.id=?1")
    Page<Type> getMyType(Long id,Pageable pageable);

    @Query("select t from Type t where t.user.id=?1")
    List<Type> getMyType(Long id);

}
