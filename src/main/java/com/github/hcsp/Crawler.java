package com.github.hcsp;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * @Author: GoFocus
 * @Date: 2020-04-13 15:26
 * @Description:
 */

public class Crawler {
    private final CrawlerDao dao;

    public Crawler(CrawlerDao dao) {
        this.dao = dao;
    }

    @SuppressFBWarnings("DMI_CONSTANT_DB_PASSWORD")
    public void run() throws IOException, SQLException {
        String link;
        while ((link = dao.getLinkUnprocessed()) != null) {
            ArrayList<String> allLinks = (ArrayList<String>) dao.getAllLinks();
            Document document = getHtmlAndParse(link);

            HashSet<String> newLinkPool = new HashSet<>();
            document.select("a").stream().map(tag -> tag.attr("href")).filter(Crawler::isInterestingLink).filter(newLink -> !allLinks.contains(newLink)).forEach(newLinkPool::add);

            dao.storeNewLinks(newLinkPool);
            storeIntoDataBaseIfItIsNewsPage(document, link);
            dao.setLinkProcessed(link);
        }

    }

    private void storeIntoDataBaseIfItIsNewsPage(Document document, String link) {
        Elements articles = document.select("article");
        if (!articles.isEmpty()) {
            String title = articles.get(0).selectFirst("h1").text();
            String content = articles.select(".art_p").stream().map(Element::text).collect(Collectors.joining("\n"));
            News news = new News(title, content, link);

            dao.storeIntoDataBase(news);
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
        String homePage = "https://sina.cn";
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


