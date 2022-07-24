package com.liuyanqing.tankframe;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;



public class ServerFrame extends Frame {
  
	Server s =new Server();
	TextArea taLeft = new TextArea();
	TextArea taRight = new TextArea();
	Button btnStart = new Button("start");
	public static final ServerFrame INSTANCE = new ServerFrame();
	public ServerFrame()  {
		this.setSize(1600, 600);
		this.setLocation(300, 30);
		this.add(btnStart,BorderLayout.NORTH);
		
        Panel panel = new Panel(new GridLayout(1,2));
        panel.add(taLeft);
        panel.add(taRight);
        this.add(panel);
        addWindowListener(new WindowAdapter() {
     			@Override
     			public void windowClosing(WindowEvent e) {
     				System.exit(0);
     			} 
     		});
    
		
	}
	
//	private void connnectToServer() throws Exception {
//	    c = new Client();
//	    c.connected();
//	}
	public static void main(String[] args) throws Exception {
//        ServerFrame clientFrame = ServerFrame.INSTANCE;
//        clientFrame.setVisible(true);
//        clientFrame.connnectToServer();
		ServerFrame.INSTANCE.setVisible(true);
		ServerFrame.INSTANCE.s.serverStart();
        
	}
	


	public void updateServerMsg(String str) {
		this.taLeft.setText(taLeft.getText()+str+System.getProperty("line.separator"));
		
	}
	public void updateClientMsg(String str) {
		this.taRight.setText(taRight.getText()+str+System.getProperty("line.separator"));
		
	}
	
}
