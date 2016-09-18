package com.piqu.collectlog.monitor;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.piqu.collectlog.listener.LogFileListener;
import com.piqu.collectlog.util.ServiceManager;

import spark.Spark;

public class LogFileMonitor {
	
	FileAlterationMonitor monitor = null;
	
	private Logger logger = LoggerFactory.getLogger(LogFileMonitor.class);
	
	public LogFileMonitor(long interval){
		monitor = new FileAlterationMonitor(interval);
	}
	
	public void monitor(String path, FileAlterationListener listener) {  
        FileAlterationObserver observer = new FileAlterationObserver(new File(path));  
        monitor.addObserver(observer);  
        observer.addListener(listener);  
    }
    public void stop() throws Exception{
        monitor.stop();
        logger.info("stop...");
    }
    public void start() throws Exception {
        monitor.start();
    }
    public static void main(String[] args) {
    	LogFileMonitor m = new LogFileMonitor(10*60*1000);
    	String dir = "";
    	try {
    		m.start();
    		Spark.port(9090);
    		dir = ServiceManager.newInstance().getVlue("param.properties", "MONITOR_DIR");
    		m.monitor(dir,new LogFileListener());  
		} catch (Exception e) {
			m.logger.error("服务启动失败", e);
			return;
		}
    }

}
