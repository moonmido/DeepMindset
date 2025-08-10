package com.DeepMindset.Ai_Service.Services;


import com.DeepMindset.Ai_Service.Outputs.Chats;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.stereotype.Component;

@Component
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder builder , SimpleVectorStore vectorStore) {
        this.chatClient = builder.defaultAdvisors(new QuestionAnswerAdvisor(vectorStore)).build();
    }


    public Chats ChatWithAi(String userQuery){
        if(userQuery==null ||userQuery.isEmpty()) throw new IllegalArgumentException();

        return  chatClient.prompt()
                .user(userQuery)
                .call()
                .entity(Chats.class);
    }

}
