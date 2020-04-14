package com.github.hcsp;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * @Author: GoFocus
 * @Date: 2020-04-13 15:26
 * @Description:
 */
public class Main {

    public static void main(String[] args) throws IOException {
        ArrayList<String> linkPool = new ArrayList<>();
        HashSet<String> processedLink = new HashSet<>();
        String homePage = "https://sina.cn";
        linkPool.add(homePage);

        while (true) {
            if (linkPool.isEmpty()) {
                break;
            }

            int index = linkPool.size() - 1;
            String link = linkPool.remove(index);

            if (processedLink.contains(link)) {
                continue;
            }

            if (link.contains("sina.cn") && !link.contains("passport") && (link.contains("news.sina.cn") || link.equals(homePage)) ) {
                System.out.println(link);
                CloseableHttpClient httpclient = HttpClients.createDefault();
                HttpGet httpGet = new HttpGet(link);
                try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
//                    System.out.println(response1.getStatusLine());
                    HttpEntity entity1 = response1.getEntity();
                    String html = EntityUtils.toString(entity1);
                    Document doc = Jsoup.parse(html);

                    Elements links = doc.select("a");

                    for (Element aTag : links) {
                        linkPool.add(aTag.attr("href"));
                    }

                    Elements articles = doc.select("article");
                    if (!articles.isEmpty()) {
                        for (Element article : articles) {
                                System.out.println(article.selectFirst("h1").text());
                        }
                    }
                    processedLink.add(link);
                }
            }

        }

    }


}


