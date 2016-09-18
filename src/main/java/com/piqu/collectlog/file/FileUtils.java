package com.piqu.collectlog.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class FileUtils {
	
	/**
	 * 描述：
	 * 逻辑：
	 * @param file 源文件
	 * @param encoding 文件编码
	 * @param pos 偏移量
	 * @param num 读取量
	 * @return
	 */
	public static Map<String,Object> bufferedRandomAccessFileReadLine(File file,
			String encoding,long pos,int num){
		Map<String, Object> res = Maps.newHashMap();
		List<String> pins = Lists.newArrayList();
		res.put("pins", pins);
		
		BufferedRandomAccessFile reader = null;
		
		try{
			reader = new BufferedRandomAccessFile(file, "r");
			reader.seek(pos);
			
			for(int i=0;i<num;i++){
				String pin = reader.readLine();
				if(StringUtils.isBlank(pin)){
					break;
				}
				
				pins.add(new String(pin.getBytes("8859_1"),encoding));
			}
			res.put("pos", reader.getFilePointer());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(reader);
		}
		return res;
	}
	
	public static Map<String,Object> randomAccessFileReadLine(File file,String encoding,
			long pos,int num){
		Map<String, Object> res = Maps.newHashMap();
		List<String> pins = Lists.newArrayList();
		res.put("pins", pins);
		RandomAccessFile reader = null;
		
		try{
			reader = new RandomAccessFile(file, "r");
			reader.seek(pos);
			for(int i=0;i<num;i++){
				String pin = reader.readLine();
				if(StringUtils.isBlank(pin)){
					break;
				}
				pins.add(pin);
			}
			res.put("pos", reader.getFilePointer());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(reader);
		}
		
		return res;
	}
	
	public static List<String> readLine(File file,String encoding,
			int index,int num){
		List<String> pins = Lists.newArrayList();
		LineNumberReader reader = null;
		try{
			reader = new LineNumberReader(new InputStreamReader(new FileInputStream(file),encoding));
			int lines = 0;
			while(true){
				String pin = reader.readLine();
				if(StringUtils.isBlank(pin)){
					break;
				}
				if(lines >= index){
					if(StringUtils.isNotBlank(pin)){
						pins.add(pin);
					}
				}
				if(num == pins.size()){
					break;
				}
				lines ++;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(reader);
		}
		
		return pins;
	}

}
