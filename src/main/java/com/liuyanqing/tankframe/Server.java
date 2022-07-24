package com.liuyanqing.tankframe;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

public class Server {
	public static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	public void serverStart(){
	
//		�����̳߳� ��ܼҺ͹�����Ա  
	   EventLoopGroup bossGroup = new NioEventLoopGroup(1);
	   EventLoopGroup workGroup = new NioEventLoopGroup(3);
	   
	   try {
	    ServerBootstrap bootstrap = new ServerBootstrap();
	    ChannelFuture f = bootstrap.group(bossGroup,workGroup)
	    .channel(NioServerSocketChannel.class)
	    .childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pl = ch.pipeline();
				pl.addLast(new ServerChildHandler());
			}
		}) 
	    .bind(8899)
	    .sync();
	    
	    ServerFrame.INSTANCE.updateServerMsg("Server Started!");
	    f.channel().closeFuture().sync();
	   } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   finally{
		   bossGroup.shutdownGracefully();
		   workGroup.shutdownGracefully();	
		   }
	   }
}
class  ServerChildHandler extends ChannelInboundHandlerAdapter{
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Server.clients.add(ctx.channel());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
	ByteBuf buf = null;
	
			
		buf =(ByteBuf)msg;
		byte [] bytes = new byte[buf.readableBytes()];
		buf.getBytes(buf.readerIndex(), bytes);

	
		String s = new String(bytes);		
		ServerFrame.INSTANCE.updateClientMsg(ctx.channel().id()+":"+s);
		if (s.equals("_bye_")) {
			ServerFrame.INSTANCE.updateServerMsg("�ͻ���Ҫ���˳�");
			Server.clients.remove(ctx.channel());
			ctx.close();
			
		}else {
			Server.clients.writeAndFlush(msg);
		}
				
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      cause.printStackTrace();
      //ɾ�������쳣�Ŀͻ��� �����쳣����
//      Server.clients.remove(ctx.channel());
      ctx.close();
	}
	
}
