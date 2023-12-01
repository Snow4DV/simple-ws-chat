package com.example.proj4.controller;

import com.example.proj4.entity.Message;
import com.example.proj4.entity.ResponseMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;


@Controller
@RequestMapping("/")
public class WebSocketController {
    private final AtomicInteger userId = new AtomicInteger(0);

    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "To subscribe use /webs endpoint";
    }


    @GetMapping("/webs")
    public ModelAndView webs() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("webs");
        return modelAndView;
    }
    @MessageMapping("/webs")
    @SendTo("/topic/webs-topic")
    public ResponseMessage send(Message message)  {
        String time = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
        return new ResponseMessage(String.valueOf(this.userId.incrementAndGet()), message.getText(), time);
    }
}

