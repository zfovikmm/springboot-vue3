package com.zm.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 分类
 * </p>
 *
 * @author glimpse
 * @since 2023-09-17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryQueryResp implements Serializable {


    /**
     * id
     */
    private Long id;

    /**
     * 父id
     */
    private Long parent;

    /**
     * 名称
     */
    private String name;

    /**
     * 顺序
     */
    private Integer sort;


}
