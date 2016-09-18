package com.test;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class TestMongo {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws UnknownHostException {
		MongoTemplate template = new MongoTemplate(new Mongo("127.0.0.1", 27017),
				"collectlog");
//		DBObject dbObject = new BasicDBObject();
//		dbObject.put("name", 1);
//		DBObject dbObject1 = new BasicDBObject();
//		dbObject1.put("expireAfterSeconds", 5);
		
//		template.getCollection("testcollection").
//		createIndex(dbObject, dbObject1);
		
//		DBObject dbObject2 = new BasicDBObject();
//		dbObject2.put("name", new Date());
//		template.insert(dbObject2, "testcollection");
	}

}
