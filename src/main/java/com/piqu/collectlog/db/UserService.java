package com.piqu.collectlog.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.piqu.collectlog.entity.ScoreSource;
import com.piqu.collectlog.entity.User;

@Component("userService")
public class UserService {
	
	@Autowired(required = true)
	private MongoTemplate mongoTemplate;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd.HH");
	private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired(required = true)
	private ScoreService scoreService;
	
	/**
	 * 描述：用户注册<br>
	 * 逻辑：用户注册<br>
	 * @param map
	 * @throws MongoException
	 * @throws ParseException 
	 */
	public void register(User user) throws MongoException, ParseException{
//		logger.info("用户注册：{}",map.get("userid"));
		//注册表里只存用户和注册时间
		DBObject dbObject = new BasicDBObject();
		dbObject.put("date", sdf.format(sdf2.parse(user.getDate())));
		dbObject.put("_id", user.getUserid());
		mongoTemplate.getCollection("register").
				insert(dbObject,WriteConcern.SAFE);
		
		
		//用户表存用户首次登录信息
		DBObject dbObject2 = new BasicDBObject();
		dbObject2.put("_id", user.getUserid());
		List<String> logininfo = new ArrayList<String>();
		logininfo.add(sdf.format(sdf2.parse(user.getDate())));
		//去重
		dbObject2.put("logininfo", new ArrayList<>(new HashSet<>(logininfo)));
		
		mongoTemplate.getCollection("user").insert(dbObject2,WriteConcern.SAFE);
	}

	/**
	 * 描述：用户登录
	 * 逻辑：
	 * @param user
	 * @throws MongoException
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public void login(User user) throws MongoException, ParseException {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(user.getUserid()));
		Map<String,Object> userMap = mongoTemplate.findOne(query , Map.class, "user");
		List<String> logininfo = userMap==null?new ArrayList<String>():(List<String>)userMap.get("logininfo");
		
		logininfo.add(sdf.format(sdf2.parse(user.getDate())));
		Update update = new Update();
		update.set("logininfo", new ArrayList<>(new HashSet<>(logininfo)));
		mongoTemplate.upsert(query, update, "user");
	}

	@Deprecated
	public void logout(Map<String, Object> map) throws MongoException {
		double userid = (double)map.get("userid");
//		logger.info("用户退出：{}",userid);
		Query query = new Query();
		query.addCriteria(Criteria.where("userid").is(userid));
		Update update= new Update();
		update.set("logoutdate", map.get("date"));
		mongoTemplate.updateFirst(query, update, "user");
	}

	@SuppressWarnings("unchecked")
	public void charge(User user) throws MongoException, ParseException{
		ScoreSource source = new ScoreSource();
		BeanUtils.copyProperties(user, source);
		scoreService.source(source);
	}
	
}
