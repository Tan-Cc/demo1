package com.example.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

//单例模式
public class WCServer {

    private static volatile WCServer wcServer = null;

    private static EventLoopGroup bossGroup;
    private static EventLoopGroup workerGroup;
    private static ChannelFuture future;
    private static ServerBootstrap serverBootstrap;

    private WCServer() {}

    public static WCServer getInstance(){
        if (wcServer == null) {
            synchronized (WCServer.class) {
                if (wcServer == null) {

                    wcServer = new WCServer();

                    bossGroup = new NioEventLoopGroup();
                    workerGroup = new NioEventLoopGroup();

                    serverBootstrap = new ServerBootstrap();
                    serverBootstrap.group(bossGroup,workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .childHandler(new ChannelInitializer<SocketChannel>() {

                                @Override
                                protected void initChannel(SocketChannel socketChannel) throws Exception {
                                    ChannelPipeline pipeline = socketChannel.pipeline();
                                    // http解编码器
                                    pipeline.addLast(new HttpServerCodec());
                                    // 对httpMessage进行聚合，变成FullHttpRequest和FullHttpResponse
                                    pipeline.addLast(new HttpObjectAggregator(1024*64));
                                    //
                                    pipeline.addLast(new WebSocketServerProtocolHandler("/wc"));
                                }
                            });
                }
            }
        }

        return wcServer;
    }

    public void start() {
        this.future = serverBootstrap.bind(8091);
        System.err.println("netty启动成功...");
    }

}
