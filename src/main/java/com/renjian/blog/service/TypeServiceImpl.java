package com.renjian.blog.service;

import com.renjian.blog.NotFoundException;
import com.renjian.blog.dao.TypeRepository;
import com.renjian.blog.module.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeRepository typeRepository;

    @Transactional(rollbackFor=Exception.class)
    @Override
    public Type saveType(Type type) {

        return typeRepository.save(type);
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public Type getType(Long id) {
        return typeRepository.getById(id);
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public Type updateType(Long id, Type type) {

        Type t=typeRepository.getById(id);
        if(t==null){
            throw new NotFoundException("不存在该类型");
        }

        BeanUtils.copyProperties(type,t);
        return typeRepository.save(t);
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public void deleteType(Long id) {
        typeRepository.deleteById(id);
    }

    @Override
    public Type getByName(String name) {
        return typeRepository.getByName(name);
    }

    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }

    @Override
    public List<Type> listType(Long userId) {
        return typeRepository.getMyType(userId);
    }

    @Override
    public Page<Type> listMyType(Long userId,Pageable pageable) {
        return typeRepository.getMyType(userId,pageable);
    }

    @Override
    public List<Type> listTypeTop(Integer size) {
        Sort sort=new Sort(Sort.Direction.DESC,"blogs.size");
        Pageable pageable=new PageRequest(0,size,sort);
        return typeRepository.findTop(pageable);
    }
}
