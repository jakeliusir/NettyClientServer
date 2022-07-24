package com.liuyanqing.tankframe;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
public class ClientFrame extends Frame {
	Client c  = null;
	TextArea taArea = new TextArea();
	TextField tField = new TextField();
	public static final ClientFrame INSTANCE = new ClientFrame();
	public ClientFrame()  {
		this.setSize(600, 400);
		this.setLocation(100, 10);
		this.add(taArea, BorderLayout.CENTER);
		this.add(tField, BorderLayout.SOUTH);
		tField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c.send(tField.getText());
//				String s = (taArea.getText() + tField.getText());
//				taArea.setText(s + "\n");
				tField.setText("");
			}
		});
//
//		this.setVisible(true);
		 addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				c.closeConnect();
				System.exit(0);
			} 
		});
	}
	private void connnectToServer() throws Exception {
	    c = new Client();
	    c.connected();
	}
	public static void main(String[] args) throws Exception {
        ClientFrame clientFrame = ClientFrame.INSTANCE;
        clientFrame.setVisible(true);
        clientFrame.connnectToServer();
        
	}
	


	public void updateText(String msgAccected) {
		this.taArea.setText(taArea.getText()+System.getProperty("line.separator")+msgAccected);
		
	}
	
}
