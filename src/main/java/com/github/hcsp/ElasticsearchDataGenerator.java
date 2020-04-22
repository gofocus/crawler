package com.github.hcsp;

import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchGenerationException;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: gofocus
 * @Date: 21:45 2020/4/20
 */
public class ElasticsearchDataGenerator {

    public void generateDate() throws IOException {
        List<News> seedNews = new MockDataDao().getNews(2000);
        writeSingleThread(seedNews);
    }

    private void writeSingleThread(List<News> seedNews) throws IOException {
        try (RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")))) {
            for (int i = 0; i < 10; i++) {
                BulkRequest bulkRequest = new BulkRequest();
                for (News news : seedNews) {
                    Map<String, Object> jsonMap = new HashMap<>();
                    jsonMap.put("title", news.getTitle());
                    jsonMap.put("content", news.getContent());
                    jsonMap.put("url", news.getUrl());
                    jsonMap.put("created_at", news.getCreatedAt());
                    IndexRequest indexRequest = new IndexRequest("news").source(jsonMap);
                    bulkRequest.add(indexRequest);
                }
                BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            }
        } catch (ElasticsearchGenerationException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            new ElasticsearchDataGenerator().generateDate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
