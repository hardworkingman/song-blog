package com.my.blog.website.sqlprovider;

import com.my.blog.website.modal.Vo.ContentVoExample;

import java.util.List;

public class ArticleSqlProvider {

    private String baseColumnList() {
        return "cid, title, slug, created, modified, author_id, type, status, tags, categories, hits, comments_num, allow_comment, allow_ping, allow_feed";
    }

    private String blobColumnList() {
        return "content";
    }

    private String exampleWhereClause(List<ContentVoExample.Criteria> oredCriteria) {
        if (oredCriteria != null && !oredCriteria.isEmpty()) {
            StringBuilder sql = new StringBuilder(" where ");
            int i = 0;
            for (ContentVoExample.Criteria criteria: oredCriteria) {
                if (criteria.isValid()) {
                    i++;
                    if (i != 1) {
                        sql.append(" or ");
                    }
                    sql.append(" (");
                    if (criteria.getCriteria() != null && !criteria.getCriteria().isEmpty()) {
                        int j = 0;
                        for (ContentVoExample.Criterion criterion: criteria.getCriteria()) {
                            j++;
                            if (j != 1) {
                                sql.append(" and ");
                            }
                            if (criterion.isNoValue()) {
                                sql.append(criterion.getCondition());
                            } else if (criterion.isSingleValue()) {
                                sql.append(criterion.getCondition());
                                sql.append("'");
                                sql.append(criterion.getValue());
                                sql.append("'");
                            } else if (criterion.isBetweenValue()) {
                                sql.append(criterion.getCondition());
                                sql.append("'");
                                sql.append(criterion.getValue());
                                sql.append("'");
                                sql.append(" and ");
                                sql.append("'");
                                sql.append(criterion.getSecondValue());
                                sql.append("'");
                            } else if (criterion.isListValue()) {
                                if (criterion.getValue() != null) {
                                    sql.append(criterion.getCondition());
                                    sql.append(" in (");
                                    List list = (List) criterion.getValue();
                                    int k = 0;
                                    for (Object o :list) {
                                        k++;
                                        if (k != 1) {
                                            sql.append(",");
                                        }
                                        sql.append("'");
                                        sql.append(o.toString());
                                        sql.append("'");
                                    }
                                    sql.append(") ");
                                }
                            }
                        }
                    }
                    sql.append(") ");
                }
            }
            return sql.toString();
        }
        return "";
    }

    public String selectByExampleWithBLOBs(final ContentVoExample contentVoExample) {
        StringBuilder sql = new StringBuilder("select ");
        if (contentVoExample.isDistinct()) {
            sql.append("distinct ");
        }
        sql.append(baseColumnList());
        sql.append(",");
        sql.append(blobColumnList());
        sql.append(" from t_contents ");
        sql.append(exampleWhereClause(contentVoExample.getOredCriteria()));
        if (contentVoExample.getOrderByClause() != null) {
            sql.append(" order by ");
            sql.append(contentVoExample.getOrderByClause());
        }
        return sql.toString();
    }
}
