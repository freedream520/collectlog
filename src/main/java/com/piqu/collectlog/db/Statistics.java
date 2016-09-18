package com.piqu.collectlog.db;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.mongodb.AggregationOutput;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@Component()
public class Statistics {

	private Gson gson = new Gson();

	private Logger logger = LoggerFactory.getLogger(Statistics.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	/**
	 * 描述：统计某一天登录总人数 逻辑：
	 * 
	 * @param date
	 *            格式为：yyyyMMdd
	 * @return
	 */
	public String loginTotalByDate(String date) {
		Query query = new Query();
		query.addCriteria(Criteria.where("logininfo").elemMatch(Criteria.where("$regex").is(date)));
		long count = mongoTemplate.count(query, "user");

		JSONObject jo = new JSONObject();
		jo.put("count", count);
		return jo.toJSONString();
	}

	/**
	 * 描述：统计某时段登录总人数 逻辑：
	 * 
	 * @param time
	 * @return
	 */
	public String loginTotalByTime(String time) {
		Query query = new Query();
		query.addCriteria(Criteria.where("logininfo").all(time));
		long count = mongoTemplate.count(query, "user");

		JSONObject jo = new JSONObject();
		jo.put("count", count);
		return jo.toJSONString();

	}

	/**
	 * 描述：总注册人数 逻辑：
	 * 
	 * @return
	 */
	public long registerTotal() {
		return mongoTemplate.getCollection("register").count();
	}

	/**
	 * 描述：查询某天的注册人数 逻辑：
	 * 
	 * @param date
	 *            格式：yyyyMMdd
	 * @return
	 */
	public long registerTotalBDate(String date) {
		Query query = new Query();
		query.addCriteria(Criteria.where("date").all(date));
		long count = mongoTemplate.count(query, "user");
		return count;
	}

	/**
	 * 描述：查询用户某一天的得分情况 逻辑：
	 * 
	 * @param userid
	 * @param date
	 *            格式：yyMMdd
	 * @return
	 */
	public String statisticsScoreByUserid(String userid, String date) {
		List<DBObject> pipeline = new ArrayList<>();

		String match = "{$match:{_id:{$regex:\"" + date + "\"}}}";
		String unwind = "{$unwind:\"$data\"}";
		String group = "{$group:{_id:\"$data.userid\",killfish:{$sum:\"$data.killfish\"},score:{$sum:\"$data.score\"},winscore:{$sum:\"$data.winscore\"},cannocount:{$sum:\"$data.cannocount\"},bull_score:{$sum:\"$data.bull_score\"}}}";
		String matchTotal = "{$match:{_id:" + Integer.parseInt(userid) + "}}";
		pipeline.add((DBObject) JSON.parse(match));
		pipeline.add((DBObject) JSON.parse(unwind));
		pipeline.add((DBObject) JSON.parse(group));
		pipeline.add((DBObject) JSON.parse(matchTotal));
		AggregationOutput aggregate = mongoTemplate.getCollection("score").aggregate(pipeline);
		Iterable<DBObject> results = aggregate.results();

		return JSON.serialize(results.iterator().next());
	}

	/**
	 * 描述：查询用户某小时的得分情况 逻辑：
	 * 
	 * @param userid
	 * @param date
	 *            格式：yyMMdd
	 * @param hour
	 *            格式：HH
	 * @return
	 */
	public String statisticsScoreByUseridHour(String userid, String date, String hour) {
		List<DBObject> pipeline = new ArrayList<>();

		String match = "{$match:{_id:\"" + date + "." + hour + "\"}}}";
		String unwind = "{$unwind:\"$data\"}";
		String group = "{$group:{_id:\"$data.userid\",killfish:{$sum:\"$data.killfish\"},score:{$sum:\"$data.score\"},winscore:{$sum:\"$data.winscore\"},cannocount:{$sum:\"$data.cannocount\"},bull_score:{$sum:\"$data.bull_score\"}}}";
		String matchTotal = "{$match:{_id:" + Integer.parseInt(userid) + "}}";
		pipeline.add((DBObject) JSON.parse(match));
		pipeline.add((DBObject) JSON.parse(unwind));
		pipeline.add((DBObject) JSON.parse(group));
		pipeline.add((DBObject) JSON.parse(matchTotal));
		AggregationOutput aggregate = mongoTemplate.getCollection("score").aggregate(pipeline);
		Iterable<DBObject> results = aggregate.results();

		return JSON.serialize(results.iterator().next());
	}

	public String statisticsEconoByUserid(String userid, String date) {
		List<DBObject> pipeline = new ArrayList<>();

		String match = "{$match:{_id:{$regex:\"" + date + "\"}}}";
		String unwind = "{$unwind:\"$data\"}";
		String group = "{$group:{_id:\"$data.userid\",qiandao:{$sum:\"$data.qiandao\"},bind:{$sum:\"$data.bind\"},"
				+ "share:{$sum:\"$data.share\"},openbox:{$sum:\"$data.openbox\"},jiuji:{$sum:\"$data.jiuji\"},"
				+ "jiujibull:{$sum:\"$data.jiujibull\"},jiujibull:{$sum:\"$data.jiujibull\"},charge:{$sum:\"$data.charge\"},"
				+ "chargebull:{$sum:\"$data.chargebull\"},chargecount:{$sum:\"$data.chargecount\"},amount:{$sum:\"$data.amount\"},rank:{$sum:\"$data.rank\"}}}";
		String matchTotal = "{$match:{_id:" + Integer.parseInt(userid) + "}}";
		pipeline.add((DBObject) JSON.parse(match));
		pipeline.add((DBObject) JSON.parse(unwind));
		pipeline.add((DBObject) JSON.parse(group));
		pipeline.add((DBObject) JSON.parse(matchTotal));
		AggregationOutput aggregate = mongoTemplate.getCollection("source").aggregate(pipeline);
		Iterable<DBObject> results = aggregate.results();

		return JSON.serialize(results.iterator().next());
	}
	
	public String statisticsEconoByUseridHour(String userid, String date, String hour){
		List<DBObject> pipeline = new ArrayList<>();

		String match = "{$match:{_id:\"" + date + "." + hour + "\"}}}";
		String unwind = "{$unwind:\"$data\"}";
		String group = "{$group:{_id:\"$data.userid\",qiandao:{$sum:\"$data.qiandao\"},bind:{$sum:\"$data.bind\"},"
				+ "share:{$sum:\"$data.share\"},openbox:{$sum:\"$data.openbox\"},jiuji:{$sum:\"$data.jiuji\"},"
				+ "jiujibull:{$sum:\"$data.jiujibull\"},jiujibull:{$sum:\"$data.jiujibull\"},charge:{$sum:\"$data.charge\"},"
				+ "chargebull:{$sum:\"$data.chargebull\"},chargecount:{$sum:\"$data.chargecount\"},amount:{$sum:\"$data.amount\"},rank:{$sum:\"$data.rank\"}}}";
		String matchTotal = "{$match:{_id:" + Integer.parseInt(userid) + "}}";
		pipeline.add((DBObject) JSON.parse(match));
		pipeline.add((DBObject) JSON.parse(unwind));
		pipeline.add((DBObject) JSON.parse(group));
		pipeline.add((DBObject) JSON.parse(matchTotal));
		AggregationOutput aggregate = mongoTemplate.getCollection("source").aggregate(pipeline);
		Iterable<DBObject> results = aggregate.results();

		return JSON.serialize(results.iterator().next());
	}

}
