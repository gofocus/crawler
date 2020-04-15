package com.github.hcsp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * @Author: GoFocus
 * @Date: 2020-04-15 15:06
 * @Description:
 */
public interface CrawlerDao {

    ArrayList<String> getAllLinks() throws SQLException;

    void setLinkProcessed(String linkProcessed) throws SQLException;

    void storeNewLinks(HashSet<String> newLinkPool) throws SQLException;

    String getLinkUnprocessed() throws SQLException;

    void storeIntoDataBase(String title, String content, String link);
}
