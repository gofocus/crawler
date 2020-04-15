package com.github.hcsp;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

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

    @SuppressFBWarnings("DMI_CONSTANT_DB_PASSWORD")
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
    public int setLinkProcessed(String linkProcessed) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE LINK_POOL SET PROCESSED = true WHERE LINK = ?")) {
            statement.setString(1, linkProcessed);
            boolean execute = statement.execute();
            if (execute) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    @Override
    public int storeNewLinks(HashSet<String> newLinkPool) throws SQLException {
        String sql = "INSERT INTO LINK_POOL (LINK) values ?";
        for (String newLink : newLinkPool) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, newLink);
                boolean execute = statement.execute();
                if (execute) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
        return 0;
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
    public int storeIntoDataBase(News news) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO NEWS (TITLE, CONTENT, CREATED_AT, MODIFIED_AT, URL) VALUES (?,?,now(),now(),?)")) {
            statement.setString(1, news.getTitle());
            statement.setString(2, news.getContent());
            statement.setString(3, news.getUrl());
            statement.execute();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
