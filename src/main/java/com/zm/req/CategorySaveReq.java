package com.zm.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
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
public class CategorySaveReq implements Serializable {


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
    @NotNull(message = "【名称】不能为空")
    private String name;

    /**
     * 顺序
     */
    @NotNull(message = "【排序】不能为空")
    private Integer sort;


}
