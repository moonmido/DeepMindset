package com.DeepMindset.Ai_Service.Services;


import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.ai.vectorstore.SimpleVectorStore;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

@Service
public class RagService {

    @Value("classpath:/Pdfs/G12.pdf")
    private Resource PdfFile;

    @Value("store.json")
    private String StoreFileName;

    @Bean
    public SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel){
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();
        Path path = Path.of("src","main","resources","store");
        File file = new File(path.toFile(),StoreFileName);

        if(!file.exists()){
            PagePdfDocumentReader reader = new PagePdfDocumentReader(PdfFile);
            List<Document> documents = reader.get();
            TextSplitter textSplitter = new TokenTextSplitter();
            List<Document> chunks = textSplitter.apply(documents);
            vectorStore.add(chunks);
            vectorStore.save(file);
        }else{
            vectorStore.load(file);
        }
return vectorStore;
    }


}
