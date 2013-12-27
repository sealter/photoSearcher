package com.google.theoku.photoSearcher;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;


/**
 * Hello world!
 *
 */
public class App 
{
	public static void main( String[] args ) throws UnknownHostException {
		
		MongoClient mongoClient = new MongoClient();
		
		DB db = mongoClient.getDB("hello");
		
		List<String> dbs = mongoClient.getDatabaseNames();
		for(String aDb : dbs){
			System.out.println(aDb);
		}
		
		DBCollection table = db.getCollection("user");
		
		BasicDBObject document = new BasicDBObject();
		document.put("name", "mkyong");
		document.put("age", 30);
		document.put("createdDate", new Date());
		table.insert(document);
		
		
		DBCursor aFind = table.find(document);
		while(aFind.hasNext()) {
			System.out.println(aFind.next());
		}
	}

}
