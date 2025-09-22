package com.langchain4j.demo.test.parser;


import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.net.URL;


/**
 * 测试 Tika 文档解析
 *
 * @author dawei
 */

public class ApacheTikaDocumentParserTest {

    @Test
    public void textDocumentParserTest() {
        String textPath = "temp/docs/最伟大的人.txt";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(textPath);
        DocumentParser parser = new ApacheTikaDocumentParser();
        Document document = parser.parse(inputStream);
        System.out.println("【Text document】\n" + document.text());
    }

    @Test
    public void pdfDocumentParserTest() throws Exception {
        //  本地文件
        String pdfPath = "temp/docs/百炼系列手机产品介绍.pdf";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(pdfPath);

        // 网络文件
        String pdfUrl = "https://sca-oss-bucket.oss-cn-shenzhen.aliyuncs.com/sfc/%E7%99%BE%E7%82%BC%E7%B3%BB%E5%88%97%E6%89%8B%E6%9C%BA%E4%BA%A7%E5%93%81%E4%BB%8B%E7%BB%8D%20.pdf?Expires=1757185678&OSSAccessKeyId=TMP.3KqPGCLCf2nRhJ8E7FEdRqUP8W7PJeq9dy4iovWcUzo3ZxHohpQsE9LPTYhMTXdngm64SRsPJq9YQKzTQDE9Su2TGn5u2M&Signature=DP9brfp8SQm5JxXa9XoOAHOOOKQ%3D";
        InputStream inputStream2 = new URL(pdfUrl).openStream();

        DocumentParser parser = new ApacheTikaDocumentParser();
        Document document = parser.parse(inputStream2);
        System.out.println("【PDF document】\n" + document.text());
    }

    @Test
    public void mdDocumentParserTest()  {
        String mdPath = "temp/docs/产品文档.md";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(mdPath);
        DocumentParser parser = new ApacheTikaDocumentParser();
        Document document = parser.parse(inputStream);
        System.out.println("【MD document】\n" + document.text());
    }

    @Test
    public void docxDocumentParserTest()  {
        String docxPath = "temp/docs/手机产品介绍.docx";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(docxPath);
        DocumentParser parser = new ApacheTikaDocumentParser();
        Document document = parser.parse(inputStream);
        System.out.println("【Docx document】\n" + document.text());
    }

    @Test
    public void htmlDocumentParserTest() throws Exception {
        String htmlUrl = "https://docs.langchain4j.dev/apidocs/index.html";

        InputStream inputStream = new URL(htmlUrl).openStream();
        DocumentParser parser = new ApacheTikaDocumentParser();
        Document document = parser.parse(inputStream);
        System.out.println("【Html document】\n" + document.text());
    }





}
