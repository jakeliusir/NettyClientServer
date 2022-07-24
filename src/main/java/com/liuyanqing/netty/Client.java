package com.liuyanqing.netty;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;
public class Client {
    //netty�ǻ����¼�ģ��  netty���з��������첽��
	public static void main(String[] args)throws Exception {
        //�̳߳� ѭ���¼����ϴ��� ���Թ涨�߳���Ŀ
		EventLoopGroup group = new NioEventLoopGroup(20);
		//����������
		Bootstrap bootstrap  =new Bootstrap();
		try {
			//�����̳߳�
			ChannelFuture f = bootstrap.group(group)
			//�������� Ĭ�ϲ��Ӿ���������
			.channel(NioSocketChannel.class)
			.handler(new ClientChannelInitializer()) 	
			.connect("localhost",8899);
//			.sync();
			f.addListener(new ChannelFutureListener() {
				
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (!future.isSuccess()) {
						System.out.println("not Connected");
					}else {
						System.out.println("Connected");
					}
					
				}
			}).sync();
			
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}}}
class ClientChannelInitializer extends ChannelInitializer<SocketChannel>{
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(new ClientHandler());
		
	}
	
}

class ClientHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		  
	      ByteBuf  buf = Unpooled.copiedBuffer("hello".getBytes());
	      ctx.writeAndFlush(buf);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = null;
		try {	
		buf =(ByteBuf)msg;
		byte [] bytes = new byte[buf.readableBytes()];
		buf.getBytes(buf.readerIndex(), bytes);
		System.out.println(new String(bytes));
//		ctx.writeAndFlush(msg);
//			System.out.println(buf);
//			System.out.println(buf.refCnt());
		}finally {
			if (buf!=null) {
			ReferenceCountUtil.release(buf);
			System.out.println(buf.refCnt());
			}else {
				System.out.println(buf.refCnt());
			}
		}
	}

	
	
}
