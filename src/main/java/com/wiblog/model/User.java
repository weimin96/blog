package com.wiblog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pwm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable{
    private static final long serialVersionUID = -8825627271281726314L;

    private Integer uid;

    private String phone;

    private String username;

    @JsonIgnore
    private String password;

    private String sex;

    private String email;

    private String avatarImg;

    private Date createTime;

    private Date logged;
}