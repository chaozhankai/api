package com.mtdhb.api.constant.e;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author i@huangdenghe.com
 * @date 2018/03/07
 */
public enum ThirdPartyApplication {

    /**
     * 美团
     */
    MEITUAN,
    /**
     * 饿了么
     */
    ELE,
    /**
     * 饿了么星选
     */
    STAR;

    @JsonValue
    public int getJsonValue() {
        return ordinal();
    }

}
