package com.example.cw_ood;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static final String CONNECTION_STRING = "mongodb+srv://kalanabimsara8:y09LYInOngyG4bgV@ooddb.b6kvb.mongodb.net/"; // MongoDB connection string
    private static final String DATABASE_NAME = "prodatabase";


    public static MongoDatabase getDatabase() {
        try {
            // Initialize MongoClient
            if (mongoClient == null) {
                ConnectionString connectionString = new ConnectionString(CONNECTION_STRING);
                mongoClient = MongoClients.create(connectionString);
            }

            if (database == null) {
                database = mongoClient.getDatabase(DATABASE_NAME);
            }
        } catch (Exception e) {
            System.err.println("An error occurred while connecting to the database: " + e.getMessage());
        }
        return database;
    }

    public static void close() {

    }
}
