package com.mtdhb.api.dto;

import lombok.Data;

/**
 * @author i@huangdenghe.com
 * @date 2017/12/06
 */
@Data
public class Result {

    public Integer code;
    public String message;
    public Object data;

}
