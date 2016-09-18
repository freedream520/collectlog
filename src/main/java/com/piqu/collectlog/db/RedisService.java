package com.piqu.collectlog.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class RedisService {
	
	@Autowired(required = true)
	private JedisPool jedisPool;
	
	/**
	 * 描述：在redis中保存每次读取文件的偏移量
	 * 逻辑：
	 * @param key
	 * @param value
	 */
	public void putString(String key,String value){
		try(Jedis jedis = jedisPool.getResource()){
			jedis.set("collect."+key, value);
		}
	}
	
	/**
	 * 描述：获取redis中保存的文件读取偏移量
	 * 逻辑：
	 * @param key
	 */
	public String getString(String key){
		try(Jedis jedis = jedisPool.getResource()){
			return jedis.get("collect."+key);
		}
	}
	
	public void cleanCache(String key){
		try(Jedis jedis = jedisPool.getResource()){
			jedis.del("collect."+key);
		}
	}

}
