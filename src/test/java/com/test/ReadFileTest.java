package com.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.piqu.collectlog.file.FileUtils;

public class ReadFileTest {

	private static Logger logger = LoggerFactory.getLogger(ReadFileTest.class);

	private static String ENCODING = "UTF-8";
	private static final int NUM = 50000;
	private static File file = new File(ClassLoader.getSystemResource("").getPath() + File.separator + "test.txt");
	private static File randomFile = new File(
			ClassLoader.getSystemResource("").getPath() + File.separator + "randomFile.txt");

	@Before
	public void makeData() throws IOException {
		String prefix = "_$#";
		if (!file.exists()) {
			file.createNewFile();
		}
		if (!randomFile.exists()) {
			randomFile.createNewFile();
		} else {
			return;
		}
		OutputStreamWriter out = null;
		try {
			out = new OutputStreamWriter(new FileOutputStream(file, true), ENCODING);

			for (int j = 0; j < 10000000; j++) {
				out.write(prefix + (int) (13000000 * Math.random()) + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	@Test
	public void testRandomAccessFile() {
		long start = System.currentTimeMillis();

		logger.info(String.valueOf(file.exists()));
		long pos = 0L;

		while (true) {
			Map<String, Object> res = FileUtils.randomAccessFileReadLine(file, ENCODING, pos, NUM);
			if (MapUtils.isEmpty(res)) {
				break;
			}
			// Object po = res.get("pos");
			List<String> pins = (List<String>) res.get("pins");
			if (CollectionUtils.isNotEmpty(pins)) {
				logger.info(Arrays.toString(pins.toArray()));
				if (pins.size() < NUM) {
					break;
				}
			} else {
				break;
			}
			pos = (long) res.get("pos");
		}
		logger.info(((System.currentTimeMillis() - start) / 1000) + "");
	}

	@Test
	public void testBufferedRandomAccessFile() {
		long start = System.currentTimeMillis();

		logger.info(String.valueOf(file.exists()));
		long pos = 0L;
		
		while(true){
			Map<String, Object> res = FileUtils.bufferedRandomAccessFileReadLine(file, ENCODING, pos, NUM);
			if(MapUtils.isEmpty(res)){
				break;
			}
			List<String> pins = (List<String>)res.get("pins");
			if (CollectionUtils.isNotEmpty(pins)) {
				logger.info(Arrays.toString(pins.toArray()));
				if (pins.size() < NUM) {
					break;
				}
			} else {
				break;
			}
			pos = (long) res.get("pos");
		}
		logger.info(((System.currentTimeMillis() - start) / 1000) + "");
	}
}
