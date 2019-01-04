package com.my.blog.website.service;

import com.github.pagehelper.PageInfo;
import com.my.blog.website.dto.ArticleDto;
import com.my.blog.website.entity.Content;

public interface ArticleService {

    PageInfo<Content> getArticles(Integer page, Integer limit);

    ArticleDto getArticle(String cid);

}
