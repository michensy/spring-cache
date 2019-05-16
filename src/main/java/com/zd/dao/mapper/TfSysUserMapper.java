package com.zd.dao.mapper;

import com.zd.core.mapper.MyMapper;
import com.zd.dao.model.TfSysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TfSysUserMapper extends MyMapper<TfSysUser> {
    void insertBatch(@Param(value = "users") List<TfSysUser> users);
}