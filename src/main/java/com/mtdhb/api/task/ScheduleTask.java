package com.mtdhb.api.task;

import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mtdhb.api.constant.e.CookieUseStatus;
import com.mtdhb.api.constant.e.ThirdPartyApplication;
import com.mtdhb.api.entity.Cookie;
import com.mtdhb.api.service.CookieService;
import com.mtdhb.api.service.CookieUseCountService;
import com.mtdhb.api.service.TimesService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author i@huangdenghe.com
 * @date 2018/07/14
 */
@Component
@Slf4j
public class ScheduleTask {

    @Autowired
    private CookieService cookieService;
    @Autowired
    private CookieUseCountService cookieUseCountService;
    @Autowired
    private TimesService timesService;
    @Resource(name = "usage")
    private Map<Long, Long> usage;
    @Resource(name = "queues")
    private List<LinkedBlockingQueue<Cookie>> queues;
    @Resource(name = "endpoints")
    private AtomicLong[] endpoints;

    @Scheduled(cron = "1 0 0 * * ?")
    public void reload() {
        log.info("Reload starting...");
        usage.clear();
        Stream.of(ThirdPartyApplication.values()).forEach(application -> {
            queues.get(application.ordinal()).clear();
            endpoints[application.ordinal()].set(Long.MAX_VALUE);
            cookieService.load(application);
        });
    }

    @Scheduled(cron = "1 0 0 * * ?")
    public void clear() {
        log.info("Clear starting...");
        cookieUseCountService.deleteByStatus(CookieUseStatus.SUCCESS);
    }

    @Scheduled(cron = "1 0 0 * * ?")
    public void gift() {
        log.info("Gift starting...");
        timesService.gift();
    }

}
