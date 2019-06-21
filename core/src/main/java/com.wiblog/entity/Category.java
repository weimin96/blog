package com.wiblog.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 *
 * @author pwm
 * @date 2019-06-15
 */
@Data
public class Category implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;


    private String categoryName;

}
