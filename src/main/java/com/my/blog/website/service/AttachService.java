package com.my.blog.website.service;

import com.github.pagehelper.PageInfo;
import com.my.blog.website.entity.Attach;

/**
 * 附件接口
 * @author xinzone
 */
public interface AttachService {
    /**
     * 分页查询附件
     * @param page 页码
     * @param limit 页大小
     * @return
     */
    PageInfo<Attach> getAttachs(Integer page, Integer limit);

    /**
     * 保存附件
     * @param attach 附件信息
     */
    void save(Attach attach);

    /**
     * 根据附件id查询附件
     * @param id
     * @return
     */
    Attach selectById(Integer id);

    /**
     * 删除附件
     * @param id
     */
    void deleteById(Integer id);
}
