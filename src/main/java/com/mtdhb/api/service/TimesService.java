package com.mtdhb.api.service;

import com.mtdhb.api.constant.e.ThirdPartyApplication;
import com.mtdhb.api.dto.TimesDTO;

/**
 * @author i@huangdenghe.com
 * @date 2018/10/29
 */
public interface TimesService {

    long getAvailable(ThirdPartyApplication application, long userId);

    TimesDTO getTimes(ThirdPartyApplication application, long userId);
    
    void gift();

}
