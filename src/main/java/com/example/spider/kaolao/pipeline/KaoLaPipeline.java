package com.example.spider.kaolao.pipeline;


import com.alibaba.fastjson.JSON;
import com.example.configuration.ApplicationConstants;
import com.example.vo.KaoLaDetail;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by zhuzhihang on 2016/12/13.
 */
@Slf4j
public class KaoLaPipeline implements Pipeline {

    private AtomicLong size = new AtomicLong();
    private ApplicationConstants.Origin origin = ApplicationConstants.Origin.NETEASE_KOALA;
    private ApplicationConstants.Category category;
    private List<KaoLaDetail> kaoLaDetailList ;
    public KaoLaPipeline(ApplicationConstants.Category category, List<KaoLaDetail> kaoLaDetailList) {
        this.category = category;
        this. kaoLaDetailList = kaoLaDetailList;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        log.info("[考拉处理数据] - [resultItems] = {}", resultItems);
        KaoLaDetail result = resultItems.get("result");
        if (result == null) {
            return;
        }
        if(null == result.getSales() ){
            result.setSales("0");
        }
        result.setPlatformId(result.getPlatformId().split("-")[0]);
        result.update();
        result.setCategory(category.toString());
        result.setOrigin(origin.toString());
        result.setCreatedAt(new Date());

        String json = JSON.toJSONString(result);
        size.getAndIncrement();
        kaoLaDetailList.add(result);
        log.info("[考拉{}] - [size] = {} - result = {} ", category, size, json);

        // TODO: 2016/12/13 produce result
        // milkProducer.produce(json);
    }
}
