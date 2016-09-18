package com.piqu.collectlog.db;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.piqu.collectlog.util.ServiceManager;

@Component
public class ScheduledService {
	
	private Logger logger = LoggerFactory.getLogger(ScheduledService.class);
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	@Autowired(required = true)
	private RedisService redisService;
	
	/**
	 * 描述：定时器将已处理完的日志进行备份
	 * 逻辑：
	 */
	@Scheduled(cron="0 0 0/2 * * ?")
	public void backup(){
		try {
			logger.info("定时器开始备份。。。");
			String dir = ServiceManager.newInstance().getVlue("param.properties", "MONITOR_DIR");
			String backStr = ServiceManager.newInstance().getVlue("param.properties", "BACK_DIR");
			File directory = new File(dir);
			File[] files = directory.listFiles();
			if(ArrayUtils.isNotEmpty(files)){
				Date date = new Date();
				File back = new File(backStr);
				if(!back.exists()) back.mkdir();
				
				for(int i=files.length-1;i>-1;i--){
					long modified = files[i].lastModified();
					Date tmpDate = new Date(modified+2*60*60*1000);
					//如果距离文件最后操作时间过了两个小时，则备份文件
					if(date.after(tmpDate)){
						logger.info("开始备份文件{}",files[i]);
						//每天产生一个按日志命名的文件夹
						String dirName = backStr+"\\"+sdf.format(new Date(modified));
						File backDir = new File(dirName);
						
						FileUtils.moveFileToDirectory(files[i], backDir, true);
						logger.info("开始清除缓存");
						redisService.cleanCache(files[i].getName());
					}
				}
			}
		} catch (IOException e) {
			logger.error("备份日志采集信息失败", e);
		}
	}

}
