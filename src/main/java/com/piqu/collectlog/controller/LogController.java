package com.piqu.collectlog.controller;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mongodb.MongoException;
import com.piqu.collectlog.db.FishService;
import com.piqu.collectlog.db.RedisService;
import com.piqu.collectlog.db.ScoreService;
import com.piqu.collectlog.db.UserService;
import com.piqu.collectlog.entity.Fish;
import com.piqu.collectlog.entity.ScoreSource;
import com.piqu.collectlog.entity.User;
import com.piqu.collectlog.exception.CollectLogException;
import com.piqu.collectlog.util.Contants;
import com.piqu.collectlog.util.ServiceManager;

public class LogController {

	private static Logger logger = LoggerFactory.getLogger(LogController.class);

	private static UserService userService = ServiceManager.newInstance().getBean(UserService.class);

	private static FishService fishService = ServiceManager.newInstance().getBean(FishService.class);
	
	private static RedisService redisService = ServiceManager.newInstance().getBean(RedisService.class);
	
	private static ScoreService scoreService = ServiceManager.newInstance().getBean(ScoreService.class);

	private static Gson gson = new Gson();
	/**
	 * 描述：判断每条日志类型，进行转发<br>
	 * 逻辑：判断每条日志类型，进行转发
	 * 
	 * @param logs
	 * @throws CollectLogException
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	public static void transLog(List<String> logs) throws ParseException {
		if (null != logs && logs.size() > 0) {
			for (String log : logs) {
				try {
					Map<String, Object> map = gson.fromJson(log, Map.class);
					String logtype = (String) map.get("logtype");
					if (null == logtype) {
						logger.error("采集的日志信息中没有日志类型，无法进行解析:{}",gson.toJson(log));
					} else {
						switch (logtype) {
						case Contants.LOGTYPE_PLAYER:
							User user = gson.fromJson(log, User.class);
							processUser(user);
							break;
						case Contants.LOGTYPE_FISH:
							Fish fish = gson.fromJson(log, Fish.class);
							processFish(fish);
							break;
						case Contants.LOGTYPE_SCORE:
							ScoreSource scoreSource = gson.fromJson(log, ScoreSource.class);
							processScore(scoreSource);
							break;
						default:
							logger.error("未知的日志类型，无法进行解析：{}", log);
						}
					}
				} catch (JsonSyntaxException e) {
					logger.error("日志信息格式异常，无法解析：{}", log);
				}
			}
		} else {
			logger.info("没有采集到任何日志信息");
		}

	}

	private static void processScore(ScoreSource scoreSource) throws ParseException {
		scoreService.source(scoreSource);
		
	}

	private static void processFish(Fish fish) throws ParseException {
		String oprtype = fish.getOprtype();
		
		// 业务校验
		if (null == oprtype) {
			logger.error("采集的日志信息中没有操作类型，无法进行解析：{}",gson.toJson(fish));
		}

		switch (oprtype) {
//		case Contants.OPT_APPEAR:
//			fishService.appear(fish);
//			break;
		case Contants.OPT_FIRE:
			fishService.fire(fish);
			break;
		case Contants.OPT_DIED:
			fishService.died(fish);
			break;
		default:
			logger.error("未知的操作方式，无法进行解析：{}", gson.toJson(fish));
		}

	}

	private static void processUser(User user) throws ParseException {
		String oprtype = user.getOprtype();
		// 业务校验
		if (null == oprtype) {
			logger.error("采集的日志信息中没有操作类型，无法进行解析：{}",gson.toJson(user));
		}
		switch (oprtype) {
		case Contants.OPT_REGISTER:
			userService.register(user);
			break;
		case Contants.OPT_LOGIN:
			userService.login(user);
			break;
//		case Contants.OPT_LOGOUT:
//			userService.logout(map);
//			break;
		case Contants.OPT_RECHARGE:
			userService.charge(user);
			break;
		default:
			logger.error("未知的操作方式，无法进行解析：{}", gson.toJson(user));
		}
	}
	
	public static void putString(String key,String value){
		redisService.putString(key, value);
	}
	
	public static String getString(String key){
		return redisService.getString(key);
	}

}
