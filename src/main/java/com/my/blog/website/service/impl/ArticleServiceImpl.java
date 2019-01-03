package com.my.blog.website.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.my.blog.website.dto.Types;
import com.my.blog.website.exception.TipException;
import com.my.blog.website.mapper.ArticleMapper;
import com.my.blog.website.modal.Vo.ContentVo;
import com.my.blog.website.modal.Vo.ContentVoExample;
import com.my.blog.website.service.ArticleService;
import com.my.blog.website.utils.Tools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public PageInfo<ContentVo> getArticles(Integer page, Integer limit) {
        log.debug("getArticles method");
        PageHelper.startPage(page, limit);
//        List<ContentVo> data = articleMapper.selectByTypeAndStatusWithBLOBs(Types.ARTICLE.getType(), Types.PUBLISH.getType());
        ContentVoExample example = new ContentVoExample();
        example.setOrderByClause("created desc");
        example.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andStatusEqualTo(Types.PUBLISH.getType());
        List<ContentVo> data = articleMapper.selectByTypeAndStatusWithBLOB(example);
        log.debug("data : {}", data);
        return new PageInfo<>(data);
    }

    @Override
    public ContentVo getArticle(String cid) {
        log.debug("getArticle method");
        ContentVo contentVo = null;
        if (StringUtils.isNotBlank(cid)) {
            if (Tools.isNumber(cid)) {
                contentVo = articleMapper.selectByCid(Integer.valueOf(cid));
            } else {
                List<ContentVo> contentVos = articleMapper.selectBySlug(cid);
                if (contentVos.size() != 1) {
                    throw new TipException("query content by id and return is not one");
                }
                contentVo = contentVos.get(0);
            }
        }
        if (contentVo != null) {
            articleMapper.updateHits(contentVo.getHits() + 1, contentVo.getCid());
        }
        return contentVo;
    }
}
