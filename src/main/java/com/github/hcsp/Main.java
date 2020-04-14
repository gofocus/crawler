package com.github.hcsp;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * @Author: GoFocus
 * @Date: 2020-04-13 15:26
 * @Description:
 */

public class Main {
    private static Connection connection;

    public static void main(String[] args) throws IOException, SQLException {
        connection = DriverManager.getConnection("jdbc:h2:file:D:\\Git\\crawler\\crawler;MV_STORE=false", "root", "root");
        String link;
        while ((link = getLinkUnprocessed()) != null) {
            ArrayList<String> allLinks = getAllLinks();
            Document document = getHtmlAndParse(link);

            HashSet<String> newLinkPool = new HashSet<>();
            document.select("a").stream().map(tag -> tag.attr("href")).filter(Main::isInterestingLink).filter(newLink -> !allLinks.contains(newLink)).forEach(newLinkPool::add);

            storeNewLinks(newLinkPool);
            storeIntoDataBaseIfItIsNewsPage(document);
            setLinkProcessed(link);
        }

    }

    private static ArrayList<String> getAllLinks() throws SQLException {
        ArrayList<String> links = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT distinct LINK FROM LINK_POOL");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            links.add(resultSet.getString(1));
        }
        return links;
    }

    private static void setLinkProcessed(String linkProcessed) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE LINK_POOL SET PROCESSED = true WHERE LINK = ?");
        statement.setString(1, linkProcessed);
        statement.execute();
    }

    private static void storeNewLinks(HashSet<String> newLinkPool) throws SQLException {
        String sql = "INSERT INTO LINK_POOL (LINK) values ?";
        for (String newLink : newLinkPool) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newLink);
            statement.execute();
        }
    }

    private static String getLinkUnprocessed() throws SQLException {
        String sql = "SELECT LINK FROM LINK_POOL WHERE PROCESSED = false LIMIT 1";
        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return "";
    }

    private static void storeIntoDataBaseIfItIsNewsPage(Document document) throws SQLException {
        Elements articles = document.select("article");
        if (!articles.isEmpty()) {
            String title = articles.get(0).selectFirst("h1").text();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO NEWS (TITLE, CONTENT, CREATED_AT, MODIFIED_AT) VALUES (?,?,?,?)");
            statement.setString(1, title);
            statement.setString(2, "");
            statement.setDate(3, new Date(System.currentTimeMillis()));
            statement.setDate(4, new Date(System.currentTimeMillis()));
            statement.execute();
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


