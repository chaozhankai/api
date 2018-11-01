package com.mtdhb.api.service;

import java.sql.Timestamp;
import java.util.List;

import com.mtdhb.api.constant.e.ThirdPartyApplication;
import com.mtdhb.api.dto.CookieRankDailyDTO;

/**
 * @author i@huangdenghe.com
 * @date 2018/10/31
 */
public interface CookieRankDailyService {

    List<CookieRankDailyDTO> list(ThirdPartyApplication application, Timestamp gmtCreate, int size);
    
}
