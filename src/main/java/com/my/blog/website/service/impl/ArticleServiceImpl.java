package com.my.blog.website.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.my.blog.website.dto.ArticleDto;
import com.my.blog.website.dto.Types;
import com.my.blog.website.exception.TipException;
import com.my.blog.website.mapper.ArticleMapper;
import com.my.blog.website.entity.Content;
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
    public PageInfo<Content> getArticles(Integer page, Integer limit) {
        log.debug("getArticles method");
        PageHelper.startPage(page, limit);
//        List<Content> data = articleMapper.selectByTypeAndStatusWithBLOBs(Types.ARTICLE.getType(), Types.PUBLISH.getType());
        ContentVoExample example = new ContentVoExample();
        example.setOrderByClause("created desc");
        example.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andStatusEqualTo(Types.PUBLISH.getType());
        List<Content> data = articleMapper.selectByTypeAndStatusWithBLOB(example);
        log.debug("data : {}", data);
        return new PageInfo<>(data);
    }

    @Override
    public ArticleDto getArticle(String cid) {
        log.debug("getArticle method");
        ArticleDto articleDto = null;
        if (StringUtils.isNotBlank(cid)) {
            if (Tools.isNumber(cid)) {
                articleDto = articleMapper.selectByCid(Integer.valueOf(cid));
            } else {
                List<ArticleDto> contents = articleMapper.selectBySlug(cid);
                if (contents.size() != 1) {
                    throw new TipException("query content by id and return is not one");
                }
                articleDto = contents.get(0);
            }
        }
        if (articleDto != null) {
            articleMapper.updateHits(articleDto.getHits() + 1, articleDto.getCid());
            List<ArticleDto> previousArticles = articleMapper.selectPreviousArticles(articleDto.getCreated());
            if (previousArticles != null && !previousArticles.isEmpty()) {
                articleDto.setPrevious(previousArticles.get(0));
            }
            List<ArticleDto> olderArticles = articleMapper.selectOlderArticles(articleDto.getCreated());
            if (olderArticles != null && !olderArticles.isEmpty()) {
                articleDto.setOlder(olderArticles.get(0));
            }
        }
        return articleDto;
    }
}
