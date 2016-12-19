package com.example.spider.jd.pipeline;


import com.example.framework.utils.JsonUtils;
import com.example.vo.Crawler;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by zhuzhihang on 2016/12/15.
 */
@Slf4j
public class JDPipeline implements Pipeline {

    private AtomicLong size = new AtomicLong();

    @Override
    public void process(ResultItems resultItems, Task task) {
        log.info("[京东处理数据] - [resultItems] = {}", resultItems);
        Crawler result = resultItems.get("result");
        if (result == null) {
            return;
        }

        size.getAndIncrement();
        String json = JsonUtils.toJson(result);
        log.info("[京东] - [size] = {} - result = {} ", size, json);
        // TODO: 2016/12/13 produce result
        // insert(result);
    }

   /* private void insert(Crawler result) {
        String url = "jdbc:mysql://192.168.0.221:3306/glp?useUnicode=true&characterEncoding=utf-8";
        String user = "simba";
        String password = "BeGYVXnw7&Kj@A!j";
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Class.forName("com.mysql.jdbc.Driver");
            // Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO glp.crawler_props (`platform_Id`, `url`, `title`, `price`, `sku`, `brand`, `category`, `shopName`, `shopType`, `weight`, `sales`, `origin`,`sourceArea`) VALUE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, result.getPlatformId());
            statement.setString(2, result.getUrl());
            statement.setString(3, result.getTitle());
            statement.setString(4, result.getPrice());
            statement.setString(5, result.getSku());
            statement.setString(6, result.getBrand());
            statement.setString(7, result.getCategory());
            statement.setString(8, result.getShopName());
            statement.setString(9, result.getShopType());
            statement.setString(10, result.getWeight());
            statement.setString(11, result.getSales());
            statement.setString(12, result.getOrigin());
            statement.setString(13, result.getSourceArea());
            statement.execute();
        } catch (Exception e) {
            log.info("[创建数据库连接错误] - [e] = {}", ExceptionUtils.getStackTrace(e));
        }
    }*/
}
