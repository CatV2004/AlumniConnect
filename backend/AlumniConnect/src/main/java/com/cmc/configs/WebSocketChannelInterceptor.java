/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.configs;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

/**
 *
 * @author FPTSHOP
 */
@Component
public class WebSocketChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            System.out.println("[WebSocket] New connection. Session ID: " + accessor.getSessionId());
            System.out.println("[WebSocket] Auth header: " + accessor.getFirstNativeHeader("Authorization"));
        }

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            System.out.println("[WebSocket] New subscription to: " + accessor.getDestination());
        }

        return message;
    }
}
