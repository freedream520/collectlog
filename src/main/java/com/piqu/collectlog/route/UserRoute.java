package com.piqu.collectlog.route;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.piqu.collectlog.db.Statistics;

import static spark.Spark.get;

@Component()
public class UserRoute {

	@Autowired(required = true)
	private Statistics statistics;

	@PostConstruct
	public void init() {
		// 某天登录人数
		get("/login/:date", (req, res) -> {
			String date = req.params(":date");
			String count = statistics.loginTotalByDate(date);
			return count;
		});
		// 某天某时段登录人数
		get("/login/:date/:hour", (req, res) -> {
			String date = req.params(":date");
			String hour = req.params(":hour");
			String count = statistics.loginTotalByTime(date+"."+hour);
			return count;
		});
		// 总注册人数
		get("/register", (req, res) -> {
			return statistics.registerTotal();
		});
		// 某天的注册人数
		get("/register/:date", (req, res) -> {
			String date = req.params(":date");
			return statistics.registerTotalBDate(date);
		});
	}

}
