package com.piqu.collectlog.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.piqu.collectlog.entity.Fish;

@Component("fishService")
public class FishService {

	// 以年月日时为主键
	private SimpleDateFormat sdf_id = new SimpleDateFormat("yyMMdd.HH");

	private SimpleDateFormat sdf_ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private MongoTemplate mongoTemplate;

	/**
	 * 描述：无效方法
	 * 逻辑：
	 * @param map
	 */
	@Deprecated
	public void appear(Map<String, Object> map) {

		DBObject dbObject = new BasicDBObject(map);
		dbObject.removeField("logtype");
		dbObject.removeField("oprtype");
		mongoTemplate.getCollection("appearfish").insert(dbObject);
	}

	/**
	 * 描述：开火打鱼<br> 逻辑：
	 * 
	 * @param map
	 * @throws ParseException
	 */
	public void fire(Fish fish) throws ParseException {
		String date = fish.getDate();
		String d = sdf_id.format(sdf_ymdhms.parse(date));
		Query query = new Query();
		Criteria c = new Criteria();
		query.addCriteria(c.where("_id").is(d));
		
		Map map = mongoTemplate.findOne(query, Map.class, "score");
		List<Map> list = new ArrayList<>();
		if(MapUtils.isNotEmpty(map)){
			list = (ArrayList<Map>)map.get("data");
			boolean exists = JSON.serialize(list).contains(String.valueOf(fish.getUserid()));
			
			// 不存在则新增
			if (!exists) {
				DBObject dbObject = new BasicDBObject();
				Map<String,Object> data = new HashMap<>();
				dbObject.put("data", list);
				data.put("userid", fish.getUserid());
				data.put("score", 0);//掉落幸运豆
				data.put("winscore", fish.getCanno());//回收幸运豆
				data.put("cannocount", 1);
				data.put("bull_score", 0);
				data.put("killfish", 0);
				data.put("killbull", 0);
				list.add(data);
				Update update = new Update();
				update.set("data", list);
				mongoTemplate.updateFirst(query, update , "score");
			}else{
				Update update = new Update();
				update.inc("data.$.winscore", fish.getCanno());
				update.inc("data.$.cannocount", 1);
				query.addCriteria(Criteria.where("data.userid").is(fish.getUserid()));
				
				mongoTemplate.updateFirst(query, update, "score");
			}
		}else{
			DBObject dbObject = new BasicDBObject();
			Map<String,Object> data = new HashMap<>();
			dbObject.put("_id", d);
			dbObject.put("data", list);
			data.put("userid", fish.getUserid());
			data.put("score", 0);
			data.put("winscore", fish.getCanno());//回收幸运豆
			data.put("bull_score", 0);
			data.put("killfish", 0);
			data.put("killbull", 0);
			data.put("cannocount", 1);
			list.add(data);
			
			mongoTemplate.insert(dbObject, "score");
		}
	}

	public void died(Fish fish) throws ParseException {
		String date = fish.getDate();
		String d = sdf_id.format(sdf_ymdhms.parse(date));

		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(d));
		
		Map map = mongoTemplate.findOne(query, Map.class, "score");
		List<Map> list = new ArrayList<>();
		if(MapUtils.isNotEmpty(map)){
			list = (ArrayList<Map>)map.get("data");
			boolean exists = JSON.serialize(list).contains(String.valueOf(fish.getUserid()));
			
			// 不存在则新增
			if (!exists) {
				DBObject dbObject = new BasicDBObject();
				Map<String,Object> data = new HashMap<>();
				dbObject.put("data", list);
				data.put("userid", fish.getUserid());
				data.put("score", fish.getScore());
				data.put("winscore", 0);
				data.put("bull_score", fish.getBull_score());
				if(fish.getFishtype()==21){//fishtype = 21,海牛,海牛掉落牛券
					data.put("killbull", 1);
					data.put("killfish", 0);
				}else{
					data.put("killbull", 0);
					data.put("killfish", 1);
				}
				list.add(data);
				Update update = new Update();
				update.set("data", list);
				mongoTemplate.updateFirst(query, update , "score");
			}else{
				Update update = new Update();
				update.inc("data.$.score", fish.getScore());
				update.inc("data.$.bull_score", fish.getBull_score());
				if(fish.getFishtype()==21){//fishtype = 21,海牛,海牛掉落牛券
					update.inc("data.$.killbull", 1);
				}else{
					update.inc("data.$.killfish", 1);
				}
				query.addCriteria(Criteria.where("data.userid").is(fish.getUserid()));
				mongoTemplate.updateFirst(query, update, "score");
			}
		}else{
			DBObject dbObject = new BasicDBObject();
			Map<String,Object> data = new HashMap<>();
			dbObject.put("_id", d);
			dbObject.put("data", list);
			data.put("userid", fish.getUserid());
			data.put("score", fish.getScore());
			data.put("winscore", 0);
			data.put("bull_score", fish.getBull_score());
			if(fish.getFishtype()==21){//fishtype = 21,海牛,海牛掉落牛券
				data.put("killbull", 1);
				data.put("killfish", 0);
			}else{
				data.put("killbull", 0);
				data.put("killfish", 1);
			}
			data.put("cannocount", 0);
			list.add(data);
			
			mongoTemplate.insert(dbObject, "score");
		}

	}

}
