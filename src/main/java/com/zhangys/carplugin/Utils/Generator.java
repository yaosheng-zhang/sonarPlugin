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
            "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",//j7bq
            "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",
            "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",
            "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",
            "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",
            "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",//vivo
            "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",
            "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",
            "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",
            "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",

            "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",//rq7
            "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",
            "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",

            "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",//5p8t
            "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",
            "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",

            "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",//zys
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
