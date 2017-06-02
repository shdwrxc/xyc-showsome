package org.xyc.showsome.pecan.mongo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.TextSearchOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Updates.*;

/**
 * created by wks on date: 2017/3/8
 */
public class MongoSample {

    public static void connection() {
        //整个jvm应该只保持一个客户端程序，线程安全Typically you only create one MongoClient instance for a given database cluster and use it across your application. When creating multiple instances:
//        Typically you only create one MongoClient instance for a given database cluster and use it across your application. When creating multiple instances:
//        All resource usage limits (max connections, etc) apply per MongoClient instance
//        To dispose of an instance, make sure you call MongoClient.close() to clean up resources
//        The MongoClient instance actually represents a pool of connections to the database; you will only need one instance of class MongoClient even with multiple threads.
        // To directly connect to a single MongoDB server
        // (this will not auto-discover the primary even if it's a member of a replica set)
        MongoClient mongoClient = new MongoClient();

        // or
        MongoClient mongoClient1 = new MongoClient( "localhost" );

        // or
        MongoClient mongoClient2 = new MongoClient( "localhost" , 27017 );

        // or, to connect to a replica set, with auto-discovery of the primary, supply a seed list of members
        MongoClient mongoClient3 = new MongoClient(
                Arrays.asList(new ServerAddress("localhost", 27017), new ServerAddress("localhost", 27018), new ServerAddress("localhost", 27019)));

        // or use a connection string
        MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017,localhost:27018,localhost:27019");
        MongoClient mongoClient4 = new MongoClient(connectionString);

        //get the database
        MongoDatabase database = mongoClient.getDatabase("mydb");

        //get the collection
        MongoCollection<Document> collection = database.getCollection("test");

        //the record to insert
        Document doc = new Document("name", "MongoDB")
                .append("type", "database")
                .append("count", 1)
                .append("info", new Document("x", 203).append("y", 102));

        //insert one record
        collection.insertOne(doc);

        //more records to insert
        List<Document> documents = new ArrayList<Document>();
        for (int i = 0; i < 100; i++) {
            documents.add(new Document("i", i));
        }

        //insert multi record
        collection.insertMany(documents);

        //the count of the collection(table)
        System.out.println(collection.count());

        //find the first document
        //print { "_id" : { "$oid" : "551582c558c7b4fbacf16735" }, "name" : "MongoDB", "type" : "database", "count" : 1, "info" : { "x" : 203, "y" : 102 } }
        Document myDoc = collection.find().first();
        System.out.println(myDoc.toJson());

        //find all
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }

        //慎用，如果程序提前结束，这里可能会导致mongo的cursor出现问题
        for (Document cur : collection.find()) {
            System.out.println(cur.toJson());
        }

        //Get A Single Document with a Query Filter
        //print { "_id" : { "$oid" : "5515836e58c7b4fbc756320b" }, "i" : 71 }
        //Use the Filters, Sorts, Projections and Updates helpers for simple and concise ways of building up queries.
        //使用filter，sorts，什么的来预处理查询
        myDoc = collection.find(eq("i", 71)).first();
        System.out.println(myDoc.toJson());

        // now use a range query to get a larger subset
        //print all documents where i > 50.
        Block<Document> printBlock = new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document.toJson());
            }
        };
        collection.find(gt("i", 50)).forEach(printBlock);

        //We could also get a range, say 50 < i <= 100:
        collection.find(and(gt("i", 50), lte("i", 100))).forEach(printBlock);

        //sort
        myDoc = collection.find(exists("i")).sort(descending("i")).first();
        System.out.println(myDoc.toJson());

        //exclude the _id field
        myDoc = collection.find().projection(excludeId()).first();
        System.out.println(myDoc.toJson());

//        Sometimes we need to aggregate the data stored in MongoDB. The Aggregates helper provides builders for each of type of aggregation stage.
//        Below we’ll do a simple two step transformation that will calculate the value of i * 10. First we find all Documents where i > 0 by using the Aggregates.match helper. Then we reshape the document by using Aggregates.project in conjunction with the $multiply operator to calculate the “ITimes10” value:
//        collection.aggregate(asList(
//                        match(gt("i", 0)),
//                        project(Document.parse("{ITimes10: {$multiply: ['$i', 10]}}")))
//        ).forEach(printBlock);

//        For $group operations use the Accumulators helper for any accumulator operations. Below we sum up all the values of i by using the Aggregates.group helper in conjunction with the Accumulators.sum helper:
//        myDoc = collection.aggregate(singletonList(group(null, sum("total", "$i")))).first();
//        System.out.println(myDoc.toJson());

        //update one document, the first fit the condition
        collection.updateOne(eq("i", 10), set("i", 110));

        //update multi document
        UpdateResult updateResult = collection.updateMany(lt("i", 100), inc("i", 100));
        System.out.println(updateResult.getModifiedCount());

        //delete one document, the first fit the condition
        collection.deleteOne(eq("i", 110));

        //delete multi document
        DeleteResult deleteResult = collection.deleteMany(gte("i", 100));
        System.out.println(deleteResult.getDeletedCount());

        // 2. Ordered bulk operation - order is guarenteed
//        collection.bulkWrite(
//                Arrays.asList(new InsertOneModel<>(new Document("_id", 4)),
//                        new InsertOneModel<>(new Document("_id", 5)),
//                        new InsertOneModel<>(new Document("_id", 6)),
//                        new UpdateOneModel<>(new Document("_id", 1),
//                                new Document("$set", new Document("x", 2))),
//                        new DeleteOneModel<>(new Document("_id", 2)),
//                        new ReplaceOneModel<>(new Document("_id", 3),
//                                new Document("_id", 3).append("x", 4))));

        // 2. Unordered bulk operation - no guarantee of order of operation
//        collection.bulkWrite(
//                Arrays.asList(new InsertOneModel<>(new Document("_id", 4)),
//                        new InsertOneModel<>(new Document("_id", 5)),
//                        new InsertOneModel<>(new Document("_id", 6)),
//                        new UpdateOneModel<>(new Document("_id", 1),
//                                new Document("$set", new Document("x", 2))),
//                        new DeleteOneModel<>(new Document("_id", 2)),
//                        new ReplaceOneModel<>(new Document("_id", 3),
//                                new Document("_id", 3).append("x", 4))),
//                new BulkWriteOptions().ordered(false));

        //use async
//        collection.insertOne(myDoc, new SingleResultCallback<Void>() {
//            @Override
//            public void onResult(final Void result, final Throwable t) {
//                System.out.println("Inserted!");
//            }
//        });

        //java 8 lambda
//        collection.insertOne(doc, (Void result, final Throwable t) -> System.out.println("Inserted!"));
    }

    public static void admin() {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("mydb");
        MongoCollection<Document> collection = database.getCollection("test");

        //get list of database
        for (String name: mongoClient.listDatabaseNames()) {
            System.out.println(name);
        }

        //drop the database
        mongoClient.getDatabase("databaseToBeDropped").drop();

        //create a collection
        database.createCollection("cappedCollection",
                new CreateCollectionOptions().capped(true).sizeInBytes(0x100000));

        //list the collection
        for (String name : database.listCollectionNames()) {
            System.out.println(name);
        }

        //drop the collection
        collection.drop();

        // create an ascending index on the "i" field
        collection.createIndex(Indexes.ascending("i"));

        //list the index
        //print { "v" : 1, "key" : { "_id" : 1 }, "name" : "_id_", "ns" : "mydb.test" }
        // { "v" : 1, "key" : { "i" : 1 }, "name" : "i_1", "ns" : "mydb.test" }
        for (final Document index : collection.listIndexes()) {
            System.out.println(index.toJson());
        }

        // create a text index on the "content" field
        collection.createIndex(Indexes.text("content"));

        // Insert some documents
        collection.insertOne(new Document("_id", 0).append("content", "textual content"));
        collection.insertOne(new Document("_id", 1).append("content", "additional content"));
        collection.insertOne(new Document("_id", 2).append("content", "irrelevant content"));

        // Find using the text index
        long matchCount = collection.count(Filters.text("textual content -irrelevant"));
        System.out.println("Text search matches: " + matchCount);

        // Find using the $language operator
        Bson textSearch = Filters.text("textual content -irrelevant", new TextSearchOptions().language("english"));
        matchCount = collection.count(textSearch);
        System.out.println("Text search matches (english): " + matchCount);

        // Find the highest scoring match
        Document projection = new Document("score", new Document("$meta", "textScore"));
        Document myDoc = collection.find(textSearch).projection(projection).first();
        System.out.println("Highest scoring document: " + myDoc.toJson());

        //print Text search matches: 2
//        Text search matches (english): 2
//        Highest scoring document: { "_id" : 1, "content" : "additional content", "score" : 0.75 }

        //While not all commands have a specific helper, however you can run any command by using the runCommand() method. Here we call the buildInfo command:
        //只有部分的操作有帮助类，如果有些操作没有帮助类，直接使用runCommand方法来执行命令。
        Document buildInfo = database.runCommand(new Document("buildInfo", 1));
        System.out.println(buildInfo);

    }

    public static void gridfs() {
        //用于存储大文件
        //GridFS 用于存储和恢复那些超过16M（BSON文件限制）的文件(如：图片、音频、视频等)。
    }
}
