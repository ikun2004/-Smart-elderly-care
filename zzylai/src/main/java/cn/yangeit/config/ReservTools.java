package cn.yangeit.config;

import cn.yangeit.mapper.FamilyMemberElderMapper;
import cn.yangeit.mapper.ReservationMapper;
import cn.yangeit.pojo.Reservation;
import cn.yangeit.vo.FamilyMemberElderVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.core.io.Resource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

//给ai大模型用的，定义方法和配置
@Configuration
public class ReservTools {
    @Autowired
    ReservationMapper reservationMapper;

    @Autowired
    FamilyMemberElderMapper familyMemberElderMapper;

    public record UserIdParam(Long userId) {}
    public record AddReservationParam(String userId,String visitor,String elderName,String moblie, LocalDateTime time) { }

    @Bean
    @Description("微信用户预约探访")
    public Function<AddReservationParam, String> addReservation() {
        return param -> {

            //1.创建Reservation对象 赋值
            Reservation reservation = new Reservation();
            reservation.setName(param.visitor);
            reservation.setMobile(param.moblie);
            reservation.setTime(param.time);
            reservation.setVisitor(param.elderName);
            reservation.setType(1);
            reservation.setStatus(0);
            reservation.setCreateBy(Long.valueOf(param.userId));
            reservation.setUpdateBy(Long.valueOf(param.userId));
            reservation.setCreateTime(LocalDateTime.now());
            reservation.setUpdateTime(LocalDateTime.now());
            //2.保存预约信息
            int result = reservationMapper.insert(reservation);

            //3.返回信息
            return result > 0 ? "探访预约成功" : "探访预约失败";
        };
    }


    //写一个让ai大模型调用获取用户取消预约次数的Function
    @Bean
    @Description("获取取消预约次数")
    public Function<UserIdParam, String> cancelledCount() {
        return param -> {

            QueryWrapper<Reservation> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("status",2);
            queryWrapper.eq("create_by",param.userId());
            Long count = reservationMapper.selectCount(queryWrapper);
            return "用户id" + param.userId() + "用户,取消了" + count + "次预约";
        };
    }

    @Bean
    @Description("获取微信用户的绑定家人（老人）")
    public Function<UserIdParam, List> getElderList() {
        return crp  -> {
            List<FamilyMemberElderVo> list = familyMemberElderMapper.selectByMemberId(crp.userId());
            return list;
        };
    }
//ai获取预约列表
    @Bean
    @Description("获取探访预约列表")
    public Function<UserIdParam, List> getReservationList(){
        return crp -> {
            QueryWrapper<Reservation> wrapper = new QueryWrapper<>();
            wrapper.eq("create_by", crp.userId);
            wrapper.eq("type", 1); // 0: 参观预约, 1: 探访预约
            wrapper.ne("status", 2); // 过滤已取消的预约
            wrapper.orderByDesc("create_time");

            // 3. 调用reservationMapper查询数据
            List list = reservationMapper.selectList(wrapper);
            return list;
        };
    }
    //取消预约-ai
    public record CancelReservationParam(Long reservationId) {}
    @Bean
    @Description("AI取消预约")
    public Function<CancelReservationParam, String> cancelReservation() {
        return param -> {
            Reservation reservation = reservationMapper.selectById(param.reservationId());
            if (reservation == null) {
                return "未找到该预约。";
            }
            reservation.setStatus(2);
            reservation.setUpdateTime(LocalDateTime.now());
            int result = reservationMapper.updateById(reservation);
            return result > 0 ? "预约已成功取消。" : "取消预约失败。";
        };
    }
    //向量数据库，用于存储文档
   @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel){
        return new SimpleVectorStore(embeddingModel);//简单的向量数据库
    }

    //需要在启动程序的时候，将文档存入到向量数据库中
    @Bean
    public CommandLineRunner commandLineRunner(EmbeddingModel embeddingModel, VectorStore vectorStore, @Value("classpath:rag/terms-of-service.txt") Resource resource) {
        System.out.println("正在将文档存入向量数据库中...");
        return args -> {
            try {
                vectorStore.write(new TokenTextSplitter().transform(new TextReader(resource).read()));
                System.out.println("RAG知识库初始化完成");
            } catch (Exception e) {
                System.err.println("RAG知识库初始化失败(Embedding API不可用)，AI客服将降级运行: " + e.getMessage());
            }
        };

    }

  //基于内存的会话记忆
    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }
}
