﻿package com.voucher.manage.serviceImpl;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voucher.manage.mapper.UsersMapper;
import com.voucher.manage.model.Users;
import com.voucher.manage.service.UserService;
import com.voucher.weixin.base.SNSUserInfo;


@Service("userService")
public class UserServiceImpl implements UserService {
	private UsersMapper usersMapper;         //操作用户信息

	@Autowired
	public void setUsersMapper(UsersMapper usersMapper) {
		this.usersMapper = usersMapper;
	}

	public List<Users> getAllFullUser(Integer campusId,Integer limit, Integer offset, String sort,
			String order,String search) {
		return usersMapper.getAllFullUser(campusId,limit,offset,sort,order,search);
	}
	
	public Integer getUserCount(String campusAdmin ,Integer campusId,String search) {
		return usersMapper.getUserCount(campusAdmin,campusId,search);
	}

	public Integer getUserFullCount(Integer campusId,String search) {
		return usersMapper.getUserFullCount(campusId,search);
	}



	@Override
	public Integer getOpenId(Integer campusId, String openId) {
		// TODO Auto-generated method stub
		return usersMapper.getOpenId(campusId, openId);
	}

	@Override
	public Integer insertUser(SNSUserInfo snsUserInfo) {
		// TODO Auto-generated method stub
		return usersMapper.insertUserInfo(snsUserInfo);
	}

	@Override
	public Users getUserInfoById(Integer campusId, String openId) {
		// TODO Auto-generated method stub
		return usersMapper.getUserByOpenId(campusId, openId);
	}

	@Override
	public Integer upUserByOpenId(SNSUserInfo snsUserInfo) {
		// TODO Auto-generated method stub
		return usersMapper.upUserByOpenId(snsUserInfo);
	}

}