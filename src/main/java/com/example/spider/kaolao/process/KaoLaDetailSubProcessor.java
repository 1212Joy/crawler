package com.example.spider.kaolao.process;

import com.example.vo.KaoLaDetail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.handler.RequestMatcher;
import us.codecraft.webmagic.handler.SubPageProcessor;
import us.codecraft.webmagic.selector.PlainText;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 * Created by zhuzhihang on 2016/12/13.
 */
@Slf4j
public class KaoLaDetailSubProcessor extends KaoLaProcessor implements SubPageProcessor {

    private String detailScript;

    public KaoLaDetailSubProcessor(String detailScript) {
        this.detailScript = detailScript;
    }


    @Override
    public RequestMatcher.MatchOther processPage(Page page) {
        log.info("[考拉详情页] - [page] = {}", page.getUrl());
        String rawText = page.getRawText();

        int start = rawText.indexOf("__kaolaProductData = ");
        int next = rawText.indexOf("};", start);

        javax.script.ScriptEngineManager m = new javax.script.ScriptEngineManager();
        ScriptEngine jsEngine = m.getEngineByExtension("js");
        try {
            String jsCode = rawText.substring(start, next + 2);

            jsEngine.eval(jsCode.concat(detailScript));
            Object detail = jsEngine.get("e");
            KaoLaDetail kaoLaDetail = objectMapper.convertValue(detail, KaoLaDetail.class);
            kaoLaDetail.setUrl(page.getUrl().get());
            page.putField("result", kaoLaDetail);

        } catch (ScriptException e) {
            log.error("[详情解析失败] - [e] = {}", ExceptionUtils.getStackTrace(e));
        }

        return null;

    }

    @Override
    public boolean match(Request page) {
        return new PlainText(page.getUrl()).regex(detailUrl).match();
    }
}
