package com.zm.service;

import com.zm.websocket.WebSocketServer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WsService {

    @Resource
    private WebSocketServer webSocketServer;

    @Async        //实现异步化
    public void senInfo(String message){
        webSocketServer.sendInfo(message);
    }
}
