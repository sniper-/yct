package com.qdch.yct.web.mapper;

import com.qdch.yct.web.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(@Param("userName")String userName);

    int insert(User user);

    User selectByPrimaryKey(@Param("userName")String userName);

    int updateByPrimaryKey(User user);

    User getDataById(@Param("id") Long id);
}
