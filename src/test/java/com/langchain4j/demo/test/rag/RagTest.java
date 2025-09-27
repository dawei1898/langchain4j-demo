package com.langchain4j.demo.test.rag;

import com.langchain4j.demo.ai.AssistantTest;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import jakarta.annotation.Resource;
import org.apache.commons.math3.geometry.partitioning.Embedding;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.InputStream;

/**
 * RAG (Retrieval-Augmented Generation)
 * 检索增强生成
 *
 * @author dawei
 */

@SpringBootTest
@ActiveProfiles("dev")
public class RagTest {

    @Resource
    private ChatModel chatModel;

    @Resource
    private EmbeddingModel embeddingModel;


    @Test
    public void ragTest() {
        // 读取文档
        String textPath = "temp/docs/最伟大的人.txt";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(textPath);
        DocumentParser parser = new ApacheTikaDocumentParser();
        Document document = parser.parse(inputStream);
        System.out.println("【Text document】\n" + document.text());

        // 将文档存入嵌入数据库
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        //EmbeddingStoreIngestor.ingest(document, embeddingStore);
        EmbeddingStoreIngestor.builder()
                .embeddingModel(embeddingModel) // 采用远程嵌入模型，不走本地
                .embeddingStore(embeddingStore)
                .documentSplitter(new DocumentByParagraphSplitter(1000, 0))
                .build()
                .ingest(document);

        EmbeddingStoreContentRetriever retriever =
                EmbeddingStoreContentRetriever.builder()
                        .embeddingStore(embeddingStore)
                        .embeddingModel(embeddingModel)  // ✅ 显式指定，不走默认
                        .build();


        String message = "在古代，谁是最伟大的人？";
        System.out.println("【提问】 = " + message);

        AssistantTest assistant = AiServices.builder(AssistantTest.class)
                .chatModel(chatModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(5))
                .contentRetriever(retriever)
                .build();

        String resp = assistant.chat(message);
        System.out.println("【回答】 = " + resp);
    }

}
