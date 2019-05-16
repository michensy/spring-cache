package com.zd.controller;

import com.sf.idworker.generator.IdWorkerInstance;
import com.zd.biz.SpringCacheBiz;
import com.zd.dao.model.TfSysUser;
import com.zd.service.UserService;
import com.zd.util.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author Zidong
 * @date 2019/5/9 9:22 AM
 */
@RestController
@Slf4j
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SpringCacheBiz springCacheBiz;

    /**
     * 使用 @CachePut标注的方法在执行前不会去检查缓存中是否存在之前执行过的结果，而是每次都会执行该方法，并将执行结果以键值对的形式存入指定的缓存中。
     * @return uid
     */
    @GetMapping("/add")
    public String addUser() {
        Long uid = IdWorkerInstance.getId();
        // 此种方式无法生效，因为 spring-cache注解使用 aop实现，通过创建内部类来代理缓存方法，同类调用或者子类调用父类带有缓存注解的方法时属于内部调用，没有走代理，所以注解不生效。
        // userService.addUser(buildUserCache(TfSysUser.builder().userId(uid).build()));
        // 解决方法，将方法抽离到一个独立类中进行调用
        TfSysUser tfSysUser = new TfSysUser();
        tfSysUser.setUserId(uid);
        TfSysUser userDo = springCacheBiz.buildUserCache(tfSysUser);
        int insert = userService.addUser(userDo);
        if (insert == 0) {
            return "新增失败！";
        }
        return "新增成功，UID为：" + uid;
    }

    /**
     * 使用 @Cacheable注解，先从缓存中查询，如果有，直接用，否则执行查询
     */
    @GetMapping("/get")
    @Cacheable(value = "user", key = "#uid")
    public TfSysUser getUser(String uid) {
        return userService.getUser(uid);
    }

    /**
     *
     * 如果是 JavaBean作为参数，则需要使用 @CacheEvict(value = "user", key = "#user.uid")方式来绑定 uid
     * @param uid
     * @return
     */
    @GetMapping("update")
    @CacheEvict(value = "user", key = "#uid")
    public String updateUser(String uid) {
        TfSysUser tfSysUser = new TfSysUser();
        tfSysUser.setUserId(Long.valueOf(uid));
        tfSysUser.setUserName("test");
        tfSysUser.setStaffId(RandomUtils.generateNumberString(11));
        tfSysUser.setMobileNo(RandomUtils.generateNumberString(11));
        // int update = userService.updateUser(TfSysUser.builder().userName("测试用户").staffId(RandomUtils.generateNumberString(11)).mobileNo(RandomUtils.generateNumberString(11)).build());
        int update = userService.updateUser(tfSysUser);
        if (update == 0) {
            return "更新失败！";
        }
        return "更新成功，UID为：" + uid;
    }

    /**
     * 使用 @CacheEvict注解清空目标缓存
     * @param uid
     * @return
     */
    @GetMapping("del")
    @CacheEvict(value = "user", key = "#uid")
    public String delteUser(String uid) {
        int del = userService.delUser(uid);
        if (del == 0) {
            return "删除失败！";
        }
        return "删除成功，UID为：" + uid;
    }

    /**
     * 新增用户的时候构建缓存对象
     */
    @CachePut(value = "user", key = "#user.userId")
    public TfSysUser buildUserCache(TfSysUser user) {
        user.setUserName("测试用户");
        user.setStaffId(RandomUtils.generateNumberString(11));
        user.setMobileNo(RandomUtils.generateNumberString(11));
        user.setValidTag(true);
        user.setCreateTime(new Date());
        return user;
    }
}
