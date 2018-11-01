package com.mtdhb.api.service.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.mtdhb.api.constant.CacheNames;
import com.mtdhb.api.constant.e.ThirdPartyApplication;
import com.mtdhb.api.dao.CookieRankDailyRepository;
import com.mtdhb.api.dto.CookieRankDailyDTO;
import com.mtdhb.api.service.CookieRankDailyService;

/**
 * @author i@huangdenghe.com
 * @date 2018/10/31
 */
@Service
public class CookieRankDailyServiceImpl implements CookieRankDailyService {

    @Autowired
    private CookieRankDailyRepository cookieRankDailyRepository;

    @Cacheable(cacheNames = CacheNames.COOKIE_RANK_DAILY)
    @Override
    public List<CookieRankDailyDTO> list(ThirdPartyApplication application, Timestamp gmtCreate, int size) {
        return cookieRankDailyRepository.findCookieRankDailyView(application, gmtCreate, PageRequest.of(0, size))
                .stream().map(cookieRankDailyView -> {
                    CookieRankDailyDTO cookieRankDailyDTO = new CookieRankDailyDTO();
                    BeanUtils.copyProperties(cookieRankDailyView, cookieRankDailyDTO);
                    return cookieRankDailyDTO;
                }).collect(Collectors.toList());
    }

}
