package com.renjian.blog.service;

import com.renjian.blog.module.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {

    Type saveType(Type type);

    Type getType(Long id);

    Page<Type> listType(Pageable pageable);

    Type updateType(Long id,Type type);

    void deleteType(Long id);

    Type getByName(String name);

    List<Type> listType();

    List<Type> listType(Long userId);

    Page<Type> listMyType(Long userId,Pageable pageable);

    List<Type> listTypeTop(Integer size);

}
