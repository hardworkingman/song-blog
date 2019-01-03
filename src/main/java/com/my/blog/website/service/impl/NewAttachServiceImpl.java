package com.my.blog.website.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.my.blog.website.mapper.AttachMapper;
import com.my.blog.website.entity.Attach;
import com.my.blog.website.service.AttachService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NewAttachServiceImpl implements AttachService {

    @Autowired
    private AttachMapper attachMapper;

    @Override
    public PageInfo<Attach> getAttachs(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<Attach> list = attachMapper.selectAll();
        return new PageInfo<>(list);
    }

    @Override
    public void save(Attach attach) {
        attachMapper.insertSelective(attach);
    }

    @Override
    public Attach selectById(Integer id) {
        return attachMapper.selectOne(id);
    }

    @Override
    public void deleteById(Integer id) {
        attachMapper.delete(id);
    }
}
