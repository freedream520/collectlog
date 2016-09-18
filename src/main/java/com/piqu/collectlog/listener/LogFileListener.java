package com.piqu.collectlog.listener;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.piqu.collectlog.controller.LogController;
import com.piqu.collectlog.file.FileUtils;

public class LogFileListener implements FileAlterationListener {

	private Logger logger = LoggerFactory.getLogger(LogFileListener.class);

	final Cache<String, Map<String, Object>> cache = CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.HOURS)
			.maximumSize(100).build();
	private final String ENCODING = "UTF-8";
	private final int MAXNUM = 5000;

	@Override
	public void onDirectoryChange(File arg0) {
		logger.info("onDirectoryChange :" + arg0.getName());

	}

	@Override
	public void onDirectoryCreate(File arg0) {
		logger.info("onDirectoryCreate:" + arg0.getName());

	}

	@Override
	public void onDirectoryDelete(File arg0) {
		logger.info("onDirectoryDelete :" + arg0.getName());

	}

	@SuppressWarnings("unchecked")
	@Override
	public void onFileChange(File file) {
		logger.info(file.getName() + "文件onchange");

		// try {
		// input = new FileInputStream(file);
		//
		// LineIterator iterator = IOUtils.lineIterator(input, "UTF-8");
		//
		// Long lineTmp = -1L;
		// while (iterator.hasNext()) {
		// String data = iterator.nextLine();
		// lineTmp++;
		// if (lineTmp.compareTo(pos) > 0) {
		// pos++;
		// logs.add(data);
		// }
		// }
		// map.put("pos", pos);
		// cache.put(file.getName(), map);
		//
		// LogController.transLog(logs);
		// } catch (IOException | CollectLogException e) {
		// e.printStackTrace();
		// }
		// finally {
		// IOUtils.closeQuietly(input);
		// }
		synchronized (file) {
			List<String> logs = new ArrayList<String>();

			// Map<String, Object> map = cache.getIfPresent(file.getName());
			String py = LogController.getString(file.getName());

			if (StringUtils.isBlank(py)) {
				LogController.putString(file.getName(), "0");
			} else {
				Map<String, Object> res = new HashMap<String, Object>();
				try {
					do {
						// long pos = (long) map.get("pos");
						long pos = Long.parseLong(py);
						res = FileUtils.bufferedRandomAccessFileReadLine(file, ENCODING, pos, MAXNUM);
						logs = (List<String>) res.get("pins");
						// map.put("pos", res.get("pos"));
						// cache.put(file.getName(), map);
						LogController.putString(file.getName(), String.valueOf(res.get("pos")));
						LogController.transLog(logs);
						logger.info("本次" + file.getName() + "文件执行" + logs.size() + "条数据");

					} while (null != res.get("pins") && ((List<String>) res.get("pins")).size() == MAXNUM);

				} catch (ParseException e) {
					logger.error("数据分析异常:{}", e);
				}
			}

		}

	}

	@Override
	public void onFileCreate(File file) {
		logger.info("onFileCreate :" + file.getName());
		// 每个文件一个缓存
		String py = LogController.getString(file.getName());
		if(StringUtils.isBlank(py)){
			LogController.putString(file.getName(), "0");
		}
		
		if (file.length() > 0) {

			Map<String, Object> res = new HashMap<String, Object>();
			try {
				do {
					long pos = Long.parseLong(LogController.getString(file.getName()));
					res = FileUtils.bufferedRandomAccessFileReadLine(file, ENCODING, pos, MAXNUM);
					List<String> logs = (List<String>) res.get("pins");
					LogController.putString(file.getName(), String.valueOf(res.get("pos")));
					LogController.transLog(logs);
					logger.info("本次" + file.getName() + "文件执行" + logs.size() + "条数据");

				} while (null != res.get("pins") && ((List<String>) res.get("pins")).size() == MAXNUM);

			} catch (ParseException e) {
				logger.error("数据分析异常:{}", e);
			}
		}

		logger.debug(file.getName() + "文件创建");

	}

	public void createCache(File file) {

	}

	@Override
	public void onFileDelete(File file) {
		logger.info("onFileDelete :" + file.getName());

	}

	@Override
	public void onStart(FileAlterationObserver arg0) {

		logger.info("开始对采集的日志信息进行分析");

	}

	@Override
	public void onStop(FileAlterationObserver arg0) {
		logger.info("日志分析完毕");

	}

}
