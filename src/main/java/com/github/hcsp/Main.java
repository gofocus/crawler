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

/**
 * @Author: GoFocus
 * @Date: 2020-04-13 15:26
 * @Description:
 */
public class Main {
    private static String homePage = "https://sina.cn";

    public static void main(String[] args) throws IOException {
        ArrayList<String> linkPool = new ArrayList<>();
        HashSet<String> processedLink = new HashSet<>();
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

            if (isInterestingLink(link)) {
                Document document = getHtmlAndParse(link);
                document.select("a").stream().map(tag -> tag.attr("href")).forEach(linkPool::add);
                storeIntoDataBaseIfItIsNewsPage(document);
                processedLink.add(link);

            }

        }

    }

    private static void storeIntoDataBaseIfItIsNewsPage(Document document) {
        Elements articles = document.select("article");
        if (!articles.isEmpty()) {
            for (Element article : articles) {
                System.out.println(article.selectFirst("h1").text());
            }
        }
    }

    private static Document getHtmlAndParse(String link) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(link);
        try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
            System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();
            String html = EntityUtils.toString(entity1);
            return Jsoup.parse(html);
        }
    }

    private static boolean isInterestingLink(String link) {
        return isSina(link) && isNotLoginPage(link) && (isNewsPage(link) || isHomePage(link));
    }

    private static boolean isHomePage(String link) {
        return link.equals(homePage);
    }

    private static boolean isNewsPage(String link) {
        return link.contains("news.sina.cn");
    }

    private static boolean isNotLoginPage(String link) {
        return !link.contains("passport");
    }

    private static boolean isSina(String link) {
        return link.contains("sina.cn");
    }


}


