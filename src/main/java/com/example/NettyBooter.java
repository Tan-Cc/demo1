package com.example;

import com.example.netty.WCServer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class NettyBooter implements ApplicationListener<ContextRefreshedEvent> {


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //如果启动完毕，则开始启动netty
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            try {
                WCServer.getInstance().start();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
