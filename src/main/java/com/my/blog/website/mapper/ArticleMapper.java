package com.my.blog.website.mapper;

import com.my.blog.website.modal.Vo.ContentVo;
import com.my.blog.website.modal.Vo.ContentVoExample;
import com.my.blog.website.sqlprovider.ArticleSqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {

    String SELECT_FIELDS = "cid, title, slug, created, modified, author_id, type, status, tags, categories, hits, comments_num, allow_comment, allow_ping, allow_feed";

    @Select("")
    long countByExample(ContentVoExample example);

    @Select("select " + SELECT_FIELDS + ", content from t_contents WHERE type = #{type} and status = #{status} order by created desc")
    List<ContentVo> selectByTypeAndStatusWithBLOBs(@Param("type") String type, @Param("status") String status);

    @SelectProvider(type = ArticleSqlProvider.class, method = "selectByExampleWithBLOBs")
    List<ContentVo> selectByTypeAndStatusWithBLOB(ContentVoExample contentVoExample);

    @Select("select " + SELECT_FIELDS + ", content from t_contents where cid = #{cid}")
    ContentVo selectByCid(Integer cid);

    @Select("select " + SELECT_FIELDS + ", content from t_contents where slug = #{slug}")
    List<ContentVo> selectBySlug(String slug);

    @Update("update t_contents set hits = #{hits} where cid = #{cid}")
    void updateHits(@Param("hits") Integer hits, @Param("cid") Integer cid);
}
