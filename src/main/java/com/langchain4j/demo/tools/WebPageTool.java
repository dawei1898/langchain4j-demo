package com.langchain4j.demo.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * 网页抓取工具
 *
 * @author dawei
 */
public class WebPageTool {

    @Tool("Returns the content of a web page, given the URL")
    public String getWebPageContent(@P("URL of the page") String url) throws IOException {
        Document jsoupDocument = Jsoup.connect(url).get();
        return jsoupDocument.body().text();
    }
}
