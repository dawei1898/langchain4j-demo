package com.langchain4j.demo.test.parser;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.InputStream;

/**
 * 测试文档解析
 *
 * @author dawei
 */
public class TextDocumentParserTest {


    /**
     *  解析 txt 文档
     */
    @Test
    public void textDocumentParserTest() {
        String textPath = "temp/docs/最伟大的人.txt";
        String pdfPath = "temp/docs/百炼系列手机产品介绍.pdf";

        TextDocumentParser parser = new TextDocumentParser();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(textPath);
        Document document = parser.parse(inputStream);
        System.out.println("document = " + document.text());
    }
}
