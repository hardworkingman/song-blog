package com.my.blog.website.mapper;

import com.my.blog.website.modal.Bo.ArchiveBo;
import com.my.blog.website.entity.Content;
import com.my.blog.website.modal.Vo.ContentVoExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface ContentVoMapper {
    long countByExample(ContentVoExample example);

    int deleteByExample(ContentVoExample example);

    int deleteByPrimaryKey(Integer cid);

    int insert(Content record);

    int insertSelective(Content record);

    List<Content> selectByExampleWithBLOBs(ContentVoExample example);

    List<Content> selectByExample(ContentVoExample example);

    Content selectByPrimaryKey(Integer cid);

    int updateByExampleSelective(@Param("record") Content record, @Param("example") ContentVoExample example);

    int updateByExampleWithBLOBs(@Param("record") Content record, @Param("example") ContentVoExample example);

    int updateByExample(@Param("record") Content record, @Param("example") ContentVoExample example);

    int updateByPrimaryKeySelective(Content record);

    int updateByPrimaryKeyWithBLOBs(Content record);

    int updateByPrimaryKey(Content record);

    List<ArchiveBo> findReturnArchiveBo();

    List<Content> findByCatalog(Integer mid);
}