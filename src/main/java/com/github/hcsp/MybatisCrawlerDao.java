package com.github.hcsp;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

/**
 * @Author: GoFocus
 * @Date: 2020-04-15 16:57
 * @Description:
 */
public class MybatisCrawlerDao implements CrawlerDao {
    private SqlSessionFactory sqlSessionFactory;

    public MybatisCrawlerDao() {
        try {
            String resource = "db/mybatis/mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<String> getAllLinks(){
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList("com.github.hcsp.MyMapper.getAllLinks");
        }
    }

    @Override
    public void setLinkProcessed(String linkProcessed) throws SQLException {

    }

    @Override
    public void storeNewLinks(HashSet<String> newLinkPool) throws SQLException {

    }

    @Override
    public String getLinkUnprocessed() throws SQLException {
        return null;
    }

    @Override
    public void storeIntoDataBase(String title, String content, String link) {

    }
}
