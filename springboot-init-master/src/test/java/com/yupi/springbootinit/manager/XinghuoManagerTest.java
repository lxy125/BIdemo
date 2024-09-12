package com.yupi.springbootinit.manager;

import io.github.briqt.spark4j.SparkClient;
import io.github.briqt.spark4j.constant.SparkApiVersion;
import io.github.briqt.spark4j.model.SparkMessage;
import io.github.briqt.spark4j.model.SparkSyncChatResponse;
import io.github.briqt.spark4j.model.request.SparkRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

class XinghuoManagerTest {
    @ConfigurationProperties(prefix = "xunfei.client")

    @Test
    void sendMesToAIUseXingHuo() {
        // 手动创建 SparkClient 实例并设置 apiKey、apiSecret、appid
        SparkClient sparkClient = new SparkClient();


        // 发送的消息内容
        String content = "你好";
        List<SparkMessage> messages = new ArrayList<>();
        messages.add(SparkMessage.userContent(content));

        // 构造请求
        SparkRequest sparkRequest = SparkRequest.builder()
                .messages(messages)  // 消息列表
                .maxTokens(2048)      // 模型回答的 tokens 的最大长度
                .temperature(0.2)     // 核采样阈值，决定结果的随机性
                .apiVersion(SparkApiVersion.V3_5) // 指定 API 版本
                .build();

        // 同步调用接口
        SparkSyncChatResponse chatResponse = sparkClient.chatSync(sparkRequest);
        String responseContent = chatResponse.getContent();

        // 打印模型的回复内容
        System.out.println("AI 回复: " + responseContent);
    }
}
