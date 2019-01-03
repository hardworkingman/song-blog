package com.my.blog.website.sqlprovider;

import com.my.blog.website.modal.Vo.AttachVoExample;

import java.util.List;

public class AttachSqlProvider {

    public String countByExample(final AttachVoExample example) {
        return "select count(1) from t_attach " + exampleWhereClause(example.getOredCriteria());
    }

    private String exampleWhereClause(List<AttachVoExample.Criteria> oredCriteria) {
        if (oredCriteria != null && !oredCriteria.isEmpty()) {
            StringBuilder sql = new StringBuilder(" where ");
            int i = 0;
            for (AttachVoExample.Criteria criteria: oredCriteria) {
                if (criteria.isValid()) {
                    i++;
                    if (i != 1) {
                        sql.append(" or ");
                    }
                    sql.append(" (");
                    if (criteria.getCriteria() != null && !criteria.getCriteria().isEmpty()) {
                        int j = 0;
                        for (AttachVoExample.Criterion criterion: criteria.getCriteria()) {
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
}
