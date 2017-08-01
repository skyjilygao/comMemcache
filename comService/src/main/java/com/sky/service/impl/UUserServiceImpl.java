package com.sky.service.impl;

import com.sky.commons.MemcachedUtils;
import com.sky.dao.UUserMapper;
import com.sky.entity.UUser;
import com.sky.service.UUserService;
import com.whalin.MemCached.MemCachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value="uUserService")
public class UUserServiceImpl implements UUserService {

	@Autowired
	private UUserMapper userMapper;

	@Autowired
	private MemCachedClient memCachedClient;//=new MemCachedClient();

//	@Autowired
//	private MemcachedUtils memcachedUtils;
	public final static String userClassName=UUser.class.getSimpleName();
	public Logger logger= LoggerFactory.getLogger(this.getClass());
	String testid="3";
	public List<UUser> getUsers(){
		List<UUser> list=userMapper.getList();
		return list;
	}
	public UUser getById(Integer id){
		//testid为测试数据，正式使用时需要替换
		UUser user=new UUser();
		testid=1+"";
		this.memCachedClient.set("id",testid);
		this.memCachedClient.set("id",11);
		// 判断缓存中数据是否存在，如果不存在则添加，存在则读取
		if (this.memCachedClient.get("user") != null)
		{
			user = (UUser) this.memCachedClient.get("user");
			System.out.println("本次操作是在缓存中查询数据...");
		}
		else
		{
			user = userMapper.getById(Integer.parseInt(testid));
			this.memCachedClient.set("user", user);
			System.out.println("本次操作是在数据库中查询数据...并已经存放在memCache中");
		}
		return user;
	}

	public void insert(UUser user) {
		Map<String,UUser> map=new HashMap();
		userMapper.insert(user);
		testid= user.getId().toString();
	}

	public boolean update(UUser user) {
		try{
			if(user.getId() != null){
				userMapper.update(user);
//				if(redisService.getMap(userClassName,UUser.class).containsKey(user.getId())){
//					redisService.getMap(userClassName,UUser.class).put(user.getId().toString(),user);
//				}else{
//					Map map=new ConcurrentHashMap();
//					map.put(user.getId(),user);
//					redisService.setMap(userClassName,map,UUser.class);
//				}
			}
		}catch (Exception e){
			logger.error("update user error:",e);
			return false;
		}
		return true;
	}


}
