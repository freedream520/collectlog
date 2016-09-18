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
import com.piqu.collectlog.entity.ScoreSource;

@Component("scoreService")
public class ScoreService {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	// 以年月日时为主键
	private SimpleDateFormat sdf_id = new SimpleDateFormat("yyMMdd.HH");

	private SimpleDateFormat sdf_ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	enum OprateType{
		INIT,UPDATE
	}
	/**
	 * 描述：记录幸运豆和牛券的来源
	 * 逻辑：
	 * @throws ParseException 
	 */
	public void source(ScoreSource scoreSource) throws ParseException{
		String date = scoreSource.getDate();
		String d = sdf_id.format(sdf_ymdhms.parse(date));
		Query query = new Query();
		Criteria c = new Criteria();
		query.addCriteria(c.where("_id").is(d));
		
		Map map = mongoTemplate.findOne(query, Map.class, "source");
		List<Map> list = new ArrayList<>();
		if(MapUtils.isNotEmpty(map)){
			list = (ArrayList<Map>)map.get("data");
			boolean exists = JSON.serialize(list).contains(String.valueOf(scoreSource.getUserid()));
			
			// 不存在则新增
			if (!exists) {
				Update update = new Update();
				transData(update, list, scoreSource, OprateType.INIT);
				update.set("data", list);
				mongoTemplate.updateFirst(query, update , "source");
			}else{
				Update update = new Update();
				transData(update, list, scoreSource, OprateType.UPDATE);
				query.addCriteria(Criteria.where("data.userid").is(scoreSource.getUserid()));
				
				mongoTemplate.updateFirst(query, update, "score");
			}
		}else{
			DBObject dbObject = new BasicDBObject();
			dbObject.put("_id", d);
			transData(null, list, scoreSource, OprateType.INIT);
			dbObject.put("data", list);
			
			mongoTemplate.insert(dbObject, "source");
		}
	}
	
	public void transData(Update update, List<Map> list, ScoreSource scoreSource, OprateType opr){
		String comment = scoreSource.getComment();
		Map<String,Object> data = new HashMap<>();
		switch (comment) {
		case "签到":
			if(opr == OprateType.INIT){
				data.put("qiandao", scoreSource.getScore());
				data.put("userid", scoreSource.getUserid());
				list.add(data);
			}
			if(opr == OprateType.UPDATE){
				update.inc("data.$.qiandao", scoreSource.getScore());
			}
			break;
		case "绑定手机奖励":
			if(opr == OprateType.INIT){
				data.put("bind", scoreSource.getScore());
				data.put("userid", scoreSource.getUserid());
				list.add(data);
			}
			if(opr == OprateType.UPDATE){
				update.inc("data.$.bind", scoreSource.getScore());
			}
			break;
		case "分享":
			if(opr == OprateType.INIT){
				data.put("share", scoreSource.getScore());
				data.put("userid", scoreSource.getUserid());
				list.add(data);
			}
			if(opr == OprateType.UPDATE){
				update.inc("data.$.share", scoreSource.getScore());
			}
			break;
		case "开宝箱":
			if(opr == OprateType.INIT){
				data.put("openbox", scoreSource.getScore());
				data.put("userid", scoreSource.getUserid());
				list.add(data);
			}
			if(opr == OprateType.UPDATE){
				update.inc("data.$.openbox", scoreSource.getScore());
			}
			break;
		case "排行榜奖励":
			if(opr == OprateType.INIT){
				data.put("rank", scoreSource.getScore());
				data.put("userid", scoreSource.getUserid());
				list.add(data);
			}
			if(opr == OprateType.UPDATE){
				update.inc("data.$.rank", scoreSource.getScore());
			}
			break;
		case "救济金":
			if(opr == OprateType.INIT){
				data.put("jiuji", scoreSource.getScore());
				data.put("jiujibull", scoreSource.getBullScore());
				data.put("userid", scoreSource.getUserid());
				list.add(data);
			}
			if(opr == OprateType.UPDATE){
				update.inc("data.$.jiuji", scoreSource.getScore());
				update.inc("data.$.jiujibull", scoreSource.getBullScore());
			}
			break;
		case "充值":
			if(opr == OprateType.INIT){
				data.put("charge", scoreSource.getScore());
				data.put("chargebull", scoreSource.getBullScore());
				data.put("userid", scoreSource.getUserid());
				data.put("chargecount", 1);
				data.put("amount", scoreSource.getAmount());
				list.add(data);
			}
			if(opr == OprateType.UPDATE){
				update.inc("data.$.charge", scoreSource.getScore());
				update.inc("data.$.chargebull", scoreSource.getBullScore());
				update.inc("data.$.chargecount", 1);
				update.inc("data.$.amount", scoreSource.getAmount());
			}
			break;
		default:
			break;
		}
		
	}

}
