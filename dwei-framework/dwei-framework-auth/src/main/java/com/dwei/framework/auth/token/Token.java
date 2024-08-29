package com.dwei.framework.auth.token;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 令牌
 *
 * @author hww
 */
@Data
@Accessors(chain = true)
public class Token {

    private Long userId;
    private String token;
    private String refreshToken;

}
