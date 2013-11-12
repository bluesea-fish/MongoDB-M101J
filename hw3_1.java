package com.tengen;


import com.mongodb.*;

import java.net.UnknownHostException;

public class hw3_1 {
    public static void main(String[] args)throws UnknownHostException{
        //connection to my MongoDB database.
        MongoClient client = new MongoClient(new ServerAddress("localhost", 27017));
        DB database = client.getDB("school");
        DBCollection students = database.getCollection("students");

        DBCursor cursor = students.find(new BasicDBObject())
                .sort(new BasicDBObject("_id", 1));

        //This section iterates over students and finds
        // And finds the the "type" homework
        try {
            while (cursor.hasNext()){
                DBObject current = cursor.next();
                BasicDBList scores = (BasicDBList)current.get("scores");
                double comp = 100;
                for(int i = 0; i < scores.size(); i++){
                        DBObject reScore = (DBObject)scores.get(i);
                            if(reScore.get("type").equals("homework") && (Double)reScore.get("score") < comp){
                                     comp =(Double)reScore.get("score");

                                System.out.println(reScore);
                            }


                    }
                  scores.remove(new BasicDBObject("type", "homework").append("score", comp));
                    BasicDBObject queryInsert = new BasicDBObject("_id", current.get("_id"));
                    students.update(queryInsert,new BasicDBObject("$set", new BasicDBObject("scores", scores) ));

                System.out.println(scores);
        }


       }finally {
            cursor.close();
        }


    }

}
