package com.my.blog.website.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.my.blog.website.constant.WebConst;
import com.my.blog.website.dto.Types;
import com.my.blog.website.exception.TipException;
import com.my.blog.website.mapper.ArticleMapper;
import com.my.blog.website.mapper.ContentVoMapper;
import com.my.blog.website.mapper.MetaVoMapper;
import com.my.blog.website.entity.Content;
import com.my.blog.website.modal.Vo.ContentVoExample;
import com.my.blog.website.service.IContentService;
import com.my.blog.website.service.IMetaService;
import com.my.blog.website.service.IRelationshipService;
import com.my.blog.website.utils.DateKit;
import com.my.blog.website.utils.TaleUtils;
import com.my.blog.website.utils.Tools;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xinzone on 2019/1/2.
 */
@Service
@Slf4j
public class ContentServiceImpl implements IContentService {

    @Resource
    private ContentVoMapper contentDao;

    @Autowired
    private ArticleMapper articleMapper;

    @Resource
    private MetaVoMapper metaDao;

    @Resource
    private IRelationshipService relationshipService;

    @Resource
    private IMetaService metasService;

    @Override
    public void publish(Content contents) {
        if (null == contents) {
            throw new TipException("文章对象为空");
        }
        if (StringUtils.isBlank(contents.getTitle())) {
            throw new TipException("文章标题不能为空");
        }
        if (StringUtils.isBlank(contents.getContent())) {
            throw new TipException("文章内容不能为空");
        }
        int titleLength = contents.getTitle().length();
        if (titleLength > WebConst.MAX_TITLE_COUNT) {
            throw new TipException("文章标题过长");
        }
        int contentLength = contents.getContent().length();
        if (contentLength > WebConst.MAX_TEXT_COUNT) {
            throw new TipException("文章内容过长");
        }
        if (null == contents.getAuthorId()) {
            throw new TipException("请登录后发布文章");
        }
        if (StringUtils.isNotBlank(contents.getSlug())) {
            if (contents.getSlug().length() < 5) {
                throw new TipException("路径太短了");
            }
            if (!TaleUtils.isPath(contents.getSlug())) throw new TipException("您输入的路径不合法");
            ContentVoExample contentVoExample = new ContentVoExample();
            contentVoExample.createCriteria().andTypeEqualTo(contents.getType()).andStatusEqualTo(contents.getSlug());
            long count = contentDao.countByExample(contentVoExample);
            if (count > 0) throw new TipException("该路径已经存在，请重新输入");
        } else {
            contents.setSlug(null);
        }

        contents.setContent(EmojiParser.parseToAliases(contents.getContent()));

        int time = DateKit.getCurrentUnixTime();
        contents.setCreated(time);
        contents.setModified(time);
        contents.setHits(0);
        contents.setCommentsNum(0);

        String tags = contents.getTags();
        String categories = contents.getCategories();
        contentDao.insert(contents);
        Integer cid = contents.getCid();

        metasService.saveMetas(cid, tags, Types.TAG.getType());
        metasService.saveMetas(cid, categories, Types.CATEGORY.getType());
    }

    @Override
    public PageInfo<Content> getContents(Integer p, Integer limit) {
        log.debug("Enter getContents method");
        ContentVoExample example = new ContentVoExample();
        example.setOrderByClause("created desc");
        example.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andStatusEqualTo(Types.PUBLISH.getType());
        PageHelper.startPage(p, limit);
        List<Content> data = contentDao.selectByExampleWithBLOBs(example);
        PageInfo<Content> pageInfo = new PageInfo<>(data);
        log.debug("Exit getContents method");
        return pageInfo;
    }

    @Override
    public Content getContents(String id) {
        if (StringUtils.isNotBlank(id)) {
            if (Tools.isNumber(id)) {
                Content content = contentDao.selectByPrimaryKey(Integer.valueOf(id));
                if (content != null) {
                    content.setHits(content.getHits() + 1);
                    contentDao.updateByPrimaryKey(content);
                }
                return content;
            } else {
                ContentVoExample contentVoExample = new ContentVoExample();
                contentVoExample.createCriteria().andSlugEqualTo(id);
                List<Content> contents = contentDao.selectByExampleWithBLOBs(contentVoExample);
                if (contents.size() != 1) {
                    throw new TipException("query content by id and return is not one");
                }
                return contents.get(0);
            }
        }
        return null;
    }

    @Override
    public void updateContentByCid(Content content) {
        if (null != content && null != content.getCid()) {
            contentDao.updateByPrimaryKeySelective(content);
        }
    }

    @Override
    public PageInfo<Content> getArticles(Integer mid, int page, int limit) {
        int total = metaDao.countWithSql(mid);
        PageHelper.startPage(page, limit);
        List<Content> list = contentDao.findByCatalog(mid);
        PageInfo<Content> paginator = new PageInfo<>(list);
        paginator.setTotal(total);
        return paginator;
    }

    @Override
    public PageInfo<Content> getArticles(String keyword, Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        ContentVoExample contentVoExample = new ContentVoExample();
        ContentVoExample.Criteria criteria = contentVoExample.createCriteria();
        criteria.andTypeEqualTo(Types.ARTICLE.getType());
        criteria.andStatusEqualTo(Types.PUBLISH.getType());
        criteria.andTitleLike("%" + keyword + "%");
        contentVoExample.setOrderByClause("created desc");
        List<Content> contents = contentDao.selectByExampleWithBLOBs(contentVoExample);
        return new PageInfo<>(contents);
    }

    @Override
    public PageInfo<Content> getArticlesWithpage(ContentVoExample commentVoExample, Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<Content> contents = contentDao.selectByExampleWithBLOBs(commentVoExample);
        return new PageInfo<>(contents);
    }

    @Override
    public void deleteByCid(Integer cid) {
        Content contents = this.getContents(cid + "");
        if (null != contents) {
            contentDao.deleteByPrimaryKey(cid);
            relationshipService.deleteById(cid, null);
        }
    }

    @Override
    public void updateCategory(String ordinal, String newCatefory) {
        Content content = new Content();
        content.setCategories(newCatefory);
        ContentVoExample example = new ContentVoExample();
        example.createCriteria().andCategoriesEqualTo(ordinal);
        contentDao.updateByExampleSelective(content, example);
    }

    @Override
    public void updateArticle(Content contents) {
        if (null == contents || null == contents.getCid()) {
            throw new TipException("文章对象不能为空");
        }
        if (StringUtils.isBlank(contents.getTitle())) {
            throw new TipException("文章标题不能为空");
        }
        if (StringUtils.isBlank(contents.getContent())) {
            throw new TipException("文章内容不能为空");
        }
        if (contents.getTitle().length() > 200) {
            throw new TipException("文章标题过长");
        }
        if (contents.getContent().length() > 65000) {
            throw new TipException("文章内容过长");
        }
        if (null == contents.getAuthorId()) {
            throw new TipException("请登录后发布文章");
        }
        if (StringUtils.isBlank(contents.getSlug())) {
            contents.setSlug(null);
        }
        int time = DateKit.getCurrentUnixTime();
        contents.setModified(time);
        Integer cid = contents.getCid();
        contents.setContent(EmojiParser.parseToAliases(contents.getContent()));

        contentDao.updateByPrimaryKeySelective(contents);
        relationshipService.deleteById(cid, null);
        metasService.saveMetas(cid, contents.getTags(), Types.TAG.getType());
        metasService.saveMetas(cid, contents.getCategories(), Types.CATEGORY.getType());
    }
}
