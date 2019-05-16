package com.zd.biz;

import com.zd.dao.model.TfSysUser;
import com.zd.util.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Zidong
 * @date 2019/5/16 11:35 AM
 */
@Service
@Slf4j
public class SpringCacheBiz {

    /**
     * 新增用户的时候构建缓存对象，去除敏感信息，可以把缓存结果用在查询处
     */
    @CachePut(value = "user", key = "#user.userId")
    public TfSysUser buildUserCache(TfSysUser user) {
        user.setUserName("测试用户2");
        user.setStaffId(RandomUtils.generateNumberString(11));
        user.setMobileNo(RandomUtils.generateNumberString(11));
        user.setValidTag(true);
        user.setCreateTime(new Date());
        return user;
    }

}
