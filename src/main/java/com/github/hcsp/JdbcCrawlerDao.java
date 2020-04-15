package com.github.hcsp;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * @Author: GoFocus
 * @Date: 2020-04-15 16:17
 * @Description:
 */
public class JdbcCrawlerDao implements CrawlerDao {
    private Connection connection;

    public JdbcCrawlerDao() {
        try {
            this.connection = DriverManager.getConnection("jdbc:h2:file:D:\\Git\\crawler\\crawler;MV_STORE=false", "root", "root");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public ArrayList<String> getAllLinks() throws SQLException {
        ArrayList<String> links = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT distinct LINK FROM LINK_POOL"); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                links.add(resultSet.getString(1));
            }
            return links;
        }
    }

    @Override
    public void setLinkProcessed(String linkProcessed) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE LINK_POOL SET PROCESSED = true WHERE LINK = ?")) {
            statement.setString(1, linkProcessed);
            statement.execute();
        }
    }

    @Override
    public void storeNewLinks(HashSet<String> newLinkPool) throws SQLException {
        String sql = "INSERT INTO LINK_POOL (LINK) values ?";
        for (String newLink : newLinkPool) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, newLink);
                statement.execute();
            }
        }
    }

    @Override
    public String getLinkUnprocessed() throws SQLException {
        String sql = "SELECT LINK FROM LINK_POOL WHERE PROCESSED = false LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
            return "";
        }
    }

    @Override
    public void storeIntoDataBase(String title, String content, String link) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO NEWS (TITLE, CONTENT, CREATED_AT, MODIFIED_AT, URL) VALUES (?,?,now(),now(),?)")) {
            statement.setString(1, title);
            statement.setString(2, content);
            statement.setString(3, link);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
