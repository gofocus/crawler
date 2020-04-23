package com.github.hcsp;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.Random;

/**
 * @Author: gofocus
 * @Date: 22:49 2020/4/16
 */
public class MockDataGenerator {

    private static void mockData(SqlSessionFactory sqlSessionFactory, int targetNewsCount) {
        try (SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            List<News> seedNews = session.selectList("com.github.hcsp.MockMapper.getNews");
            int count = targetNewsCount - seedNews.size();
            Random random = new Random();
            try {
                while (count-- > 0) {
                    int index = random.nextInt(seedNews.size());
                    News news = new News(seedNews.get(index));
                    Instant newInstant = news.getCreatedAt().minusSeconds(random.nextInt(3600 * 24 * 365));
                    news.setCreatedAt(newInstant);
                    news.setModifiedAt(newInstant);
                    session.insert("com.github.hcsp.MockMapper.insertNews", news);
                    System.out.println("LEFT:" + count);
                    if (count % 2000 == 0) {
                        session.flushStatements();
                    }

                }
                session.commit();
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException(e);
            }
        }

    }

    public static void main(String[] args) {
        SqlSessionFactory sqlSessionFactory;
        try {
            String resource = "db/mybatis/mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        mockData(sqlSessionFactory, 100_0000);
    }


}


