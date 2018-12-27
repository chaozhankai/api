package com.mtdhb.api.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.stereotype.Service;

import com.mtdhb.api.autoconfigure.ThirdPartyApplicationProperties;
import com.mtdhb.api.constant.CacheNames;
import com.mtdhb.api.constant.e.CookieUseStatus;
import com.mtdhb.api.constant.e.ThirdPartyApplication;
import com.mtdhb.api.dao.CookieRankDailyRepository;
import com.mtdhb.api.dao.CookieRepository;
import com.mtdhb.api.dao.CookieUseCountRepository;
import com.mtdhb.api.dao.TimesRepository;
import com.mtdhb.api.dto.CookieRankDTO;
import com.mtdhb.api.dto.TimesDTO;
import com.mtdhb.api.entity.CookieRankDaily;
import com.mtdhb.api.entity.Times;
import com.mtdhb.api.service.CookieService;
import com.mtdhb.api.service.TimesService;

/**
 * @author i@huangdenghe.com
 * @date 2018/10/29
 */
@Service
public class TimesServiceImpl implements TimesService {

    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private CookieService cookieService;
    @Autowired
    private CookieRepository cookieRepository;
    @Autowired
    private CookieRankDailyRepository cookieRankDailyRepository;
    @Autowired
    private CookieUseCountRepository cookieUseCountRepository;
    @Autowired
    private TimesRepository timesRepository;
    @Autowired
    private ThirdPartyApplicationProperties thirdPartyApplicationProperties;

    @Override
    public long getAvailable(ThirdPartyApplication application, long userId) {
        return getTimes(application, userId).getAvailable();
    }

    @Override
    public TimesDTO getTimes(ThirdPartyApplication application, long userId) {
        // TODO 直接数据库 sum 的话得写更多代码，先程序计算
        long number = timesRepository
                .findByApplicationAndUserIdAndGmtCreateGreaterThan(application, userId,
                        Timestamp.valueOf(LocalDate.now().atStartOfDay()))
                .stream().mapToLong(times -> times.getNumber()).sum();
        long total = cookieRepository.countByApplicationAndValidAndUserId(application, true, userId)
                * thirdPartyApplicationProperties.getDailies()[application.ordinal()] + number;
        long used = cookieUseCountRepository.countByStatusAndApplicationAndReceivingUserIdAndGmtCreateGreaterThan(
                CookieUseStatus.SUCCESS, application, userId, Timestamp.valueOf(LocalDate.now().atStartOfDay()));
        TimesDTO timesDTO = new TimesDTO();
        // TODO 还要减去检测到私用的次数
        timesDTO.setAvailable(total - used);
        timesDTO.setTotal(total);
        return timesDTO;
    }

    @Override
    public void gift() {
        int size = 100;
        // TODO 配置
        long[] numbers = new long[] { 100, 80, 60, 40, 40, 40, 40, 40, 40, 40 };
        Timestamp timestamp = Timestamp.from(Instant.now());
        // TODO 配置
        List<CookieRankDTO> list = cookieService.listCookieRank(ThirdPartyApplication.ELE, size);
        List<CookieRankDaily> cookieRankDailys = list.stream().map(cookieRankDTO -> {
            long userId = cookieRankDTO.getUserId();
            Times times = new Times();
            int index = cookieRankDTO.getRanking() - 1;
            if (index < numbers.length) {
                times.setNumber(numbers[index]);
            } else {
                times.setNumber(0L);
            }
            times.setApplication(ThirdPartyApplication.MEITUAN);
            times.setUserId(userId);
            times.setGmtCreate(timestamp);
            timesRepository.save(times);
            CookieRankDaily cookieRankDaily = new CookieRankDaily();
            cookieRankDaily.setRanking(cookieRankDTO.getRanking());
            cookieRankDaily.setCount(cookieRankDTO.getCount());
            cookieRankDaily.setApplication(ThirdPartyApplication.ELE);
            cookieRankDaily.setUserId(userId);
            cookieRankDaily.setTimesId(times.getId());
            cookieRankDaily.setGmtCreate(timestamp);
            return cookieRankDaily;
        }).collect(Collectors.toList());
        cookieRankDailyRepository.saveAll(cookieRankDailys);
        cacheManager.getCache(CacheNames.COOKIE_RANK_DAILY).evict(SimpleKeyGenerator
                .generateKey(ThirdPartyApplication.ELE, Timestamp.valueOf(LocalDate.now().atStartOfDay()), size));
    }

}
