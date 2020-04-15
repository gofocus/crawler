package com.github.hcsp;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

/**
 * @Author: GoFocus
 * @Date: 2020-04-15 15:06
 * @Description:
 */
public interface CrawlerDao {

    List<String> getAllLinks() throws SQLException;

    int setLinkProcessed(String linkProcessed) throws SQLException;

    int storeNewLinks(HashSet<String> newLinkPool) throws SQLException;

    String getLinkUnprocessed() throws SQLException;

    int storeIntoDataBase(News news);
}
