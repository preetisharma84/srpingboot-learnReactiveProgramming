package com.project.learnreactiveprogramming;

import com.mongodb.lang.NonNullApi;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.lang.NonNull;

@EnableReactiveMongoRepositories
public class DatabaseConfiguration extends AbstractReactiveMongoConfiguration {

    @Bean
    //MongoClient is a class that helps our program to communicate/connect with MongoDb database
    public MongoClient mongoClient() {
        return MongoClients.create();
    }

    @Override
    @NonNull
    protected String getDatabaseName() {
        return "student_database";
    }

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(mongoClient(), getDatabaseName());
    }
}
