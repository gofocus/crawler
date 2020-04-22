package com.github.hcsp;

import java.util.List;

/**
 * @Author: gofocus
 * @Date: 22:51 2020/4/16
 */
public interface MockMapper {

    List<News> getNews(int bound);

    int insertNews(News news);


}
