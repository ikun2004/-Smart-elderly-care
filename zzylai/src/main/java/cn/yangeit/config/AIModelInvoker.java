package cn.yangeit.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//大模型配置
@Component
public class AIModelInvoker {

    @Autowired
    ChatClient.Builder chatClientBuilder;

    public String tyInvoker(String prompt) {

        ChatClient chatClient = chatClientBuilder.build();

        String content = chatClient.prompt().user(prompt).call().content();
        // System.out.println("内容："+content.replace("`", "").replace("json", ""));
        return content.replace("`", "").replace("json", "");
    }
}
