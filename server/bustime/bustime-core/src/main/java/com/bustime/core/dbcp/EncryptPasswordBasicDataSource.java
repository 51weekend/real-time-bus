/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-7-5.
 */

package com.bustime.core.dbcp;

import static com.sdo.billing.util.SecurityHelper.ALGORITHM__RSA;

import org.apache.commons.dbcp.BasicDataSource;

import com.sdo.billing.util.SecurityHelper;

/**
 * 数据库密码加密.
 *
 * @author chengdong
 */
public class EncryptPasswordBasicDataSource extends BasicDataSource {

    public void setPassword(String password) {
        super.setPassword(SecurityHelper.decryptBase64(ALGORITHM__RSA, privateKey, password));
    }

    public static String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKhV0Kw+Rlq4a5HYFl7gsqcClKXTBSzggwrarpQPpmXUgXO/MQx6d8fi6fkWL10Qn3qB821M5hPTstvO9BowvQiWNe9KcQGMN/u/ZzHwCKZSBpIF7PNTUghSXqfPnwOprSvkj56zixMMKDGU+mHp4RhGpAHvJV0WchEd55yFY0DdAgMBAAECgYBoUl5zEFj7igUoKlmazPgKpn/G0KMJb83mYSYGHjjKLMtZFyjnidJHrym/M2+A5ndfLb/Vge2oZe8XaAdBX+kVstwA1pnzoL0GwjzvYb1ef3uIpSYsTPvCgr33KslqPNaGAmAvraw0ZoJBNvisPjHjbMsDf++XCZvTgyHcKwmgZQJBANQ8lMcaZnSYYikk1qfprD3t2WBYY+Ipzn/hLqynKeUa9T8llMhjSV6MqzzFzN8R65QmsLd2UzRWdsv3vgkDFO8CQQDLC8rBMR0VNicFM6qdTXKzoKjCOp//zuuqw9qkAfXtsA2h8KJ0SUNXmqGpJOLazdp03iS7CiGLNT42mR/C477zAkAo1H7Kzwlm5On9gFjLdOjuARQQtPCCUgioEWopTP+CU+1aeKBm7LLwvZ8z0dnYEFqB5e+ZW+ol53xzQPYORcmBAkEAtwzjdnXbnULbvMHOf8/Rp4UgfzIxCoK2TZYSJ6eo2ebM84CoRKPD9VdvhLi/V7kbyB55CLJieUi/1KMJUj7V5wJBAIjwxY9sTUBSLeDj6ii95TTdpm8mWjtKIIcBDZseA6mE04zDERZaV8Qs/HKouybhfNqBfc9rB3SwMuhLndBK0Qo=";

}
