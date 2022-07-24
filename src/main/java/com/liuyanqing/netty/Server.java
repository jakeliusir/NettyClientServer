package com.liuyanqing.netty;
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



   public static void main(String[] args) throws Exception {
//	   BIO的连接方式
//	 ServerSocket ss  = new ServerSocket();
//	 ss.bind(new InetSocketAddress(8899));
//	 Socket s  = ss.accept();
//	 System.out.println("一个客户端已经连接");
	   //netty的server端写法
	   
	   
//	启动线程池 大管家和工作人员  
   EventLoopGroup bossGroup = new NioEventLoopGroup(1);
   EventLoopGroup workGroup = new NioEventLoopGroup(1);
   
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
    System.out.println("server Started");
    f.channel().closeFuture().sync();
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
		try {
			
		buf =(ByteBuf)msg;
		byte [] bytes = new byte[buf.readableBytes()];
		buf.getBytes(buf.readerIndex(), bytes);
		System.out.println(new String(bytes));
		
		Server.clients.writeAndFlush(msg);
//			System.out.println(buf);
//			System.out.println(buf.refCnt());
		}finally {
			if (buf!=null) {
//			ReferenceCountUtil.release(buf);
			System.out.println(buf.refCnt());
			}else {
				System.out.println(buf.refCnt());
			}
		}
		
		
		
		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      cause.printStackTrace();
      ctx.close();
	}
	
}
