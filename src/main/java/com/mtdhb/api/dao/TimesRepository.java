package com.mtdhb.api.dao;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.mtdhb.api.constant.e.ThirdPartyApplication;
import com.mtdhb.api.entity.Times;

/**
 * @author i@huangdenghe.com
 * @date 2018/10/25
 */
public interface TimesRepository extends CrudRepository<Times, Long> {

    List<Times> findByApplicationAndUserIdAndGmtCreateGreaterThan(ThirdPartyApplication application, long userId,
            Timestamp gmtCreate);

}
