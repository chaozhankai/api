package com.mtdhb.api.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.mtdhb.api.constant.e.ThirdPartyApplication;

import lombok.Data;

/**
 * @author i@huangdenghe.com
 * @date 2018/10/25
 */
@Data
@Entity
public class CookieRankDaily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer ranking;
    private Long count;
    @Enumerated
    private ThirdPartyApplication application;
    private Long userId;
    private Long timesId;
    private Timestamp gmtCreate;
    private Timestamp gmtModified;

}
