package com.mtdhb.api.dao;

import java.sql.Timestamp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.mtdhb.api.constant.e.ThirdPartyApplication;
import com.mtdhb.api.entity.CookieRankDaily;
import com.mtdhb.api.entity.view.CookieRankDailyView;

/**
 * @author i@huangdenghe.com
 * @date 2018/10/29
 */
public interface CookieRankDailyRepository extends CrudRepository<CookieRankDaily, Long> {

    @Query("select crd.userId as userId, crd.ranking as ranking, crd.count as count, t.number as number from CookieRankDaily crd, Times t where crd.timesId=t.id and crd.application=?1 and crd.gmtCreate>?2 order by crd.ranking")
    Page<CookieRankDailyView> findCookieRankDailyView(ThirdPartyApplication application, Timestamp gmtCreate,
            Pageable pageable);

}
