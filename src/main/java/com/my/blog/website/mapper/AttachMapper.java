package com.my.blog.website.mapper;

import com.my.blog.website.entity.Attach;
import com.my.blog.website.modal.Vo.AttachVoExample;
import com.my.blog.website.sqlprovider.AttachSqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AttachMapper {

    String SELECT_FIELDS = "id, fname, ftype, fkey, author_id, created";

    @Select("select " + SELECT_FIELDS + " from t_attach order by id desc")
    List<Attach> selectAll();

    @Insert("insert into t_attach (fname, ftype, fkey, author_id, created) values(#{fname}, #{ftype}, #{fkey}, #{authorId}, #{created})")
    @Options(useGeneratedKeys = true)
    void insert(Attach attach);

    @Insert("<script>" +
                "insert into t_attach" +
                "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">" +
                    "<if test=\"id != null\">" +
                        "id," +
                    "</if>" +
                    "<if test=\"fname != null\">" +
                        "fname," +
                    "</if>" +
                    "<if test=\"ftype != null\">" +
                        "ftype," +
                    "</if>" +
                    "<if test=\"fkey != null\">" +
                        "fkey," +
                    "</if>" +
                    "<if test=\"authorId != null\">" +
                        "author_id," +
                    "</if>" +
                    "<if test=\"created != null\">" +
                        "created," +
                    "</if>" +
                "</trim>" +
                "<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">" +
                    "<if test=\"id != null\">" +
                        "#{id,jdbcType=INTEGER}," +
                    "</if>" +
                    "<if test=\"fname != null\">" +
                        "#{fname,jdbcType=VARCHAR}," +
                    "</if>" +
                    "<if test=\"ftype != null\">" +
                        "#{ftype,jdbcType=VARCHAR}," +
                    "</if>" +
                    "<if test=\"fkey != null\">" +
                        "#{fkey,jdbcType=VARCHAR}," +
                    "</if>" +
                    "<if test=\"authorId != null\">" +
                        "#{authorId,jdbcType=INTEGER}," +
                    "</if>" +
                    "<if test=\"created != null\">" +
                        "#{created,jdbcType=INTEGER}," +
                    "</if>" +
                "</trim>" +
            "</script>")
    void insertSelective(Attach attach);

    @Select("select " + SELECT_FIELDS + " from t_attach where id = #{id}")
    Attach selectOne(Integer id);

    @Delete("delete from t_attach where id = #{id}")
    void delete(Integer id);

    @Select("select count(1) from t_attach")
    long count();

    @SelectProvider(type = AttachSqlProvider.class, method = "countByExample")
    long countByExample(AttachVoExample example);
}
