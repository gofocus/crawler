package com.github.hcsp;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @Author: GoFocus
 * @Date: 2020-04-15 16:27
 * @Description:
 */
public class Main {

    public static void main(String[] args) throws IOException, SQLException {
        CrawlerDao dao = new MybatisCrawlerDao();

        for (int i = 0; i < 10; i++) {
            new Crawler(dao).start();
        }


    }
}
