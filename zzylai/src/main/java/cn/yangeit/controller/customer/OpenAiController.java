package cn.yangeit.controller.customer;

import cn.yangeit.config.BaseContext;
import cn.yangeit.config.LoggingAdvisor;
import cn.yangeit.mapper.FamilyMemberMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@RestController
@CrossOrigin//  解决跨域问题
@Tag(name = "用户端-AI助理", description = "AI专属助理")
public class OpenAiController {

    private final ChatClient chatClient;

    public OpenAiController(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory, VectorStore vectorStore) {
        this.chatClient = chatClientBuilder
                .defaultSystem("""
                        您是中州养老系统的专属客服助手。请以友好、乐于助人且愉快的方式来回复。
                                您正在通过在线聊天系统与客户互动。
                                提供探访预约、取消预约、获取家人列表、取消预约次数等功能的查询。
                                在询问用户之前，请检查消息历史记录以获取此信息。
                                如果探访预约，需要预约人姓名、预约人手机号码，家人姓名，预约时段(必须收集这四个数据)才能进行增加探访预约.
                                在取消预约之前，展示预约列表给用户，且用户明确回复“确认”或“是”后，才可以取消预约。
                                请讲中文,如果是列表的信息的话，需要组织下语言。
                                展示预约信息(记得name是预约人，visitor是老人)时候用空格分隔开，预约时段的末尾加上**。
                                今天的日期是 {current_date}
                                微信用户Id：{userId}
                                我的名字：{name}
                        """)
                .defaultAdvisors(
                        new PromptChatMemoryAdvisor(chatMemory),
                        new LoggingAdvisor(),
                        new QuestionAnswerAdvisor(vectorStore)
                )
                .defaultFunctions("cancelledCount","getElderList","addReservation","getReservationList","cancelReservation")
                .build();
    }

    @Autowired
    FamilyMemberMapper familyMemberMapper;

    @CrossOrigin//  解决跨域问题
    @GetMapping(value = "/customer/ai/generateStreamAsString", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> generateStreamAsString(
            @RequestParam(value = "message", defaultValue = "讲个笑话") String message) {

        Flux<String> result = this.chatClient
                .prompt()
                .user(message)
                .system(promptSystemSpec -> {
                    promptSystemSpec.param("current_date", LocalDate.now().toString());
                    promptSystemSpec.param("userId", BaseContext.getCurrentId());
                    promptSystemSpec.param("name", familyMemberMapper.selectById(BaseContext.getCurrentId()).getName());
                })
                .advisors(advisorSpec -> advisorSpec.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY,10 ))
                .stream()
                .content();

        //在result的后面加上[complete]标记，表示生成完成
        result = result.concatWith(Flux.just("[complete]"));
        return result;
    }
}

