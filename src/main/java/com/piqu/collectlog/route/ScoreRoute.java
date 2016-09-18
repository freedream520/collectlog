package com.piqu.collectlog.route;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.piqu.collectlog.db.Statistics;

import static spark.Spark.*;

@Component
public class ScoreRoute {
	
	@Autowired(required = true)
	private Statistics statistics;
	
	@PostConstruct
	public void init(){
		//查询用户某天的分数情况
		get("/score/:userid/:date", (request,response) -> {
			String date = request.params(":date");
			String userid = request.params(":userid");
			String jsonScore = statistics.statisticsScoreByUserid(userid, date);
			return jsonScore;
		});
		//查询用户某个小时的分数情况
		get("/score/:userid/:date/:hour", (request,response) -> {
			String date = request.params(":date");
			String userid = request.params(":userid");
			String hour = request.params(":hour");
			String jsonScore = statistics.statisticsScoreByUseridHour(userid,date,hour);
			return jsonScore;
		});
	}

}
