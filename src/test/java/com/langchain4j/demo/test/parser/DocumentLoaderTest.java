package com.langchain4j.demo.test.parser;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.ClassPathDocumentLoader;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.loader.UrlDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

/**
 * 测试加载文档
 *
 * @author dawei
 */
public class DocumentLoaderTest {

    /**
     *  加载解析 txt 文档
     */
    @Test
    public void classPathDocumentLoaderTest() {
        String classPath = "temp/docs/最伟大的人.txt";
        Document document = ClassPathDocumentLoader.loadDocument(classPath);
        System.out.println("\n" + classPath + ": \n " + document.text());

        String classPath2 = "temp/docs/百炼系列手机产品介绍.pdf";
        Document document2 = ClassPathDocumentLoader.loadDocument(classPath2);
        System.out.println("\n" + classPath2 + ": \n " + document2.text());

        String classPath3 = "temp/docs/手机产品介绍.docx";
        Document document3 = ClassPathDocumentLoader.loadDocument(classPath3);
        System.out.println("\n" + classPath3 + ": \n " + document3.text());

        String classPath4 = "temp/docs/产品文档.md";
        Document document4 = ClassPathDocumentLoader.loadDocument(classPath4);
        System.out.println("\n" + classPath4 + ": \n " + document4.text());
    }


    /**
     *  加载解析 txt 文档
     */
    @Test
    public void fileSystemDocumentLoaderTest() {
        String textPath = System.getProperty("user.dir") + "src/main/resources/temp/docs/最伟大的人.txt";
        Path path = Path.of(textPath);
        Document fileDocument = FileSystemDocumentLoader.loadDocument(path, new TextDocumentParser());
        System.out.println("【File System document】 \n " + fileDocument.text());
    }

    /**
     *  加载解析 txt 文档
     */
    @Test
    public void textDocumentLoaderTest() {
        String textUrl =  "https://raw.githubusercontent.com/langchain4j/langchain4j/main/langchain4j/src/test/resources/test-file-utf8.txt";
        Document urlDocument = UrlDocumentLoader.load(textUrl, new TextDocumentParser());
        System.out.println("【Url document】 \n " + urlDocument.text());
    }

    /**
     *  加载解析 PDF 文档
     */
    @Test
    public void pdfDocumentLoaderTest() {
        String pdfUrl = "https://sca-oss-bucket.oss-cn-shenzhen.aliyuncs.com/sfc/%E7%99%BE%E7%82%BC%E7%B3%BB%E5%88%97%E6%89%8B%E6%9C%BA%E4%BA%A7%E5%93%81%E4%BB%8B%E7%BB%8D%20.pdf?Expires=1757185678&OSSAccessKeyId=TMP.3KqPGCLCf2nRhJ8E7FEdRqUP8W7PJeq9dy4iovWcUzo3ZxHohpQsE9LPTYhMTXdngm64SRsPJq9YQKzTQDE9Su2TGn5u2M&Signature=DP9brfp8SQm5JxXa9XoOAHOOOKQ%3D";
        Document urlDocument = UrlDocumentLoader.load(pdfUrl, new ApacheTikaDocumentParser());
        System.out.println("【Url PDF document】 \n " + urlDocument.text());
    }
}
