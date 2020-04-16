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
    public List<String> getAllLinks() {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            return session.selectList("com.github.hcsp.MyMapper.getAllLinks");
        }
    }

    @Override
    public int setLinkProcessed(String linkProcessed){
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            return session.update("com.github.hcsp.MyMapper.setLinkProcessed", linkProcessed);
        }
    }

    @Override
    public int storeNewLinks(HashSet<String> newLinkPool) throws SQLException {
        for (String newLink : newLinkPool) {
            try (SqlSession session = sqlSessionFactory.openSession(true)) {
                session.insert("com.github.hcsp.MyMapper.storeNewLinks", newLink);
            }
        }
        return 1;
    }


    @Override
    public synchronized String getLinkUnprocessed() {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            String linkUnprocessed = session.selectOne("com.github.hcsp.MyMapper.getLinkUnprocessed");
            setLinkProcessed(linkUnprocessed);
            return linkUnprocessed;
        }
    }

    @Override
    public int storeIntoDataBase(News news) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            return session.insert("com.github.hcsp.MyMapper.storeIntoDataBase", news);
        }
    }
}
