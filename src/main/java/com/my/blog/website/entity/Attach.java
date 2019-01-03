package com.my.blog.website.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xinzone
 */
@Data
@Builder
public class Attach implements Serializable {

    private static final long serialVersionUID = 1344598723610275637L;

    private Integer id;

    private String fname;

    private String ftype;

    private String fkey;

    private Integer authorId;

    private Integer created;

}