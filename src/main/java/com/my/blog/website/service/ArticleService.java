package com.my.blog.website.service;

import com.github.pagehelper.PageInfo;
import com.my.blog.website.modal.Vo.ContentVo;

public interface ArticleService {

    PageInfo<ContentVo> getArticles(Integer page, Integer limit);

    ContentVo getArticle(String cid);
}
