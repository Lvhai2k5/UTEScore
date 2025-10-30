package vn.ute.utescore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // nơi client lắng nghe
        config.setApplicationDestinationPrefixes("/app"); // prefix gửi message từ client
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // ✅ Endpoint cho Check-in realtime
        registry.addEndpoint("/ws-checkin").setAllowedOriginPatterns("*").withSockJS();

        // ✅ Endpoint cho Chat realtime
        registry.addEndpoint("/ws-chat").setAllowedOriginPatterns("*").withSockJS();
    }

}

