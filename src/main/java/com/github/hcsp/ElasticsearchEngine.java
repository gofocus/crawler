package com.github.hcsp;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
<<<<<<< HEAD
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
=======
>>>>>>> b1d71937a3b08e09aca23ce586a198e47d2f920d

/**
 * @Author: GoFocus
 * @Date: 2020-04-23 16:27
 * @Description:
 */
public class ElasticsearchEngine {

    public static void main(String[] args) throws UnsupportedEncodingException {
        while (true) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

            try {
                String keyword = reader.readLine();
                search(keyword);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private static void search(String keyword) throws IOException {

        try (RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")))) {
            SearchRequest request = new SearchRequest("news");
            request.source(new SearchSourceBuilder().query(new MultiMatchQueryBuilder(keyword, "title", "content")));

            SearchResponse result = client.search(request, RequestOptions.DEFAULT);

            System.out.println(result);

        }


    }
}
