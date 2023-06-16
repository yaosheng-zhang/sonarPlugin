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
                "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",
            "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"


                                                            );
    private static final String API_HOST="https://api.openai.com/";








    public String  getCodeFromModle(String code){
        //国内需要代理 国外不需要
        final String SYSTEM="请你作为开发人员对代码进行重构，返回的结果中只允许出现重构后的代码，非代码部分必须以JAVA注释 // 的方式出现";
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
                .temperature(0)
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
