package com.zd.service;

import com.zd.dao.mapper.TfSysUserMapper;
import com.zd.dao.model.TfSysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author Zidong
 * @date 2019/5/9 10:01 AM
 */
@Service
public class UserService {

    @Autowired
    private TfSysUserMapper userMapper;


    public int addUser(TfSysUser user) {
        return userMapper.insert(user);
    }

    public TfSysUser getUser(String uid) {
        return userMapper.selectByPrimaryKey(uid);
    }

    public int updateUser(TfSysUser user) {
        Example example = new Example(TfSysUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", user.getUserId());
        return userMapper.updateByExampleSelective(user, example);
    }

    public int delUser(String uid) {
        return userMapper.deleteByPrimaryKey(uid);
    }
}
