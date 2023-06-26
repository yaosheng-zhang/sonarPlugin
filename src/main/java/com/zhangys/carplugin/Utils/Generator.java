package com.zhangys.carplugin.Utils;

import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.util.Proxys;

import java.net.Proxy;
import java.util.Arrays;
import java.util.List;

public class Generator {

    private static final List<String> API_KEY_LIST=Arrays.asList(
            "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",//wyj
            "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",
            "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",
            "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",
            "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",
            "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",//zys
            "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",
            "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",
            "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",
            "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",
            "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",//vivo
            "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",
            "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",
            "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",//rq7
            "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",
            "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",
            "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",//5p8t
            "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",
            "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",
            "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",//j7bq
            "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",
            "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"

                                                            );
    private static final String API_HOST="https://api.openai.com/";








    public String  getCodeFromModle(String code){
        //国内需要代理 国外不需要
        final String SYSTEM="请你作为开发人员对代码中指定行数进行重构，返回的结果中只允许出现重构后的代码，非代码部分必须以JAVA注释 // 的方式出现";
        Proxy proxy = Proxys.http("127.0.0.1", 1080);

        ChatGPT chatGPT = ChatGPT.builder()
                .apiKeyList(API_KEY_LIST)
                .proxy(proxy)
                .timeout(900)
                .apiHost(API_HOST) //反向代理地址
                .build()
                .init();

        Message system = getSystem(SYSTEM);
        Message message = getMessage(code);

        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .messages(Arrays.asList(system, message))
                .temperature(0)
                .presencePenalty(0)
                .frequencyPenalty(0)
                .build();

        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
        Message res = response.getChoices().get(0).getMessage();
        return res.getContent();
    }



    public String  getConfidenceFromModle(String code){
        final String SYSTEM="你将获得actionable置信度查询。\n" +
                "每个查询都将用json格式包含code和msg两个键。\n" +
                "你将收到一份代码片段和一个static analysis tool产生的警告，你需要判断这个警告是否为actionable并给出confidence。\n" +
                "以JSON格式提供你的输出，包含以下键：confidence和reason。";

        //国内需要代理 国外不需要

        Proxy proxy = Proxys.http("127.0.0.1", 1080);

        ChatGPT chatGPT = ChatGPT.builder()
                .apiKeyList(API_KEY_LIST)
                .proxy(proxy)
                .timeout(900)
                .apiHost(API_HOST) //反向代理地址
                .build()
                .init();

        Message system = getSystem(SYSTEM);
        Message message = getMessage(code);

        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .messages(Arrays.asList(system, message))
                .temperature(0.5)
                .presencePenalty(0)
                .frequencyPenalty(0)
                .build();

        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
        Message res = response.getChoices().get(0).getMessage();
        return res.getContent();
    }



    private Message getMessage(String code) {
        return Message.of(code);
    }
    private Message getSystem(String system) {
        return Message.ofSystem(system);
    }





}
