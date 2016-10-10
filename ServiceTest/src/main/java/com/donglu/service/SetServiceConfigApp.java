package com.donglu.service;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;

public class SetServiceConfigApp {

	private JFrame frame;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SetServiceConfigApp window = new SetServiceConfigApp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SetServiceConfigApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 338, 86);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblip = new JLabel("设备ip");
		lblip.setBounds(10, 13, 42, 15);
		frame.getContentPane().add(lblip);
		
		textField = new JTextField();
		textField.setBounds(51, 13, 171, 21);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		textField.setText(ServiceConfig.getInstance().getDeviceIp());
		
		JButton button = new JButton("确定");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = textField.getText();
				if (text.split("\\.").length!=4) {
					JOptionPane.showMessageDialog(frame, "ip不正确");
					return;
				}
				boolean setIp = setIp(text);
				if (!setIp) {
					JOptionPane.showMessageDialog(frame, "ip设置失败");
					return;
				}else{
					JOptionPane.showMessageDialog(frame, "ip设置成功");
				}
				ServiceConfig.getInstance().setDeviceIp(text);
				File f=new File("run.bat");
				if (!f.exists()) {
					return;
				}
				try(BufferedReader br=new BufferedReader(new FileReader(f))) {
					List<String> list=new ArrayList<>();
					String s=null;
					while ((s=br.readLine())!=null ) {
						list.add(s);
					}
					for (String string : list) {
						Runtime.getRuntime().exec(string);
					}
					System.exit(0);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		button.setBounds(232, 12, 65, 23);
		frame.getContentPane().add(button);
	}
	
	public boolean setIp(String ip){
		if (ip!=null) {
			String localIp = ServiceUtils.getLocalIp();
			if (localIp!=null) {
				boolean flag = ServiceUtils.sendMsg(ip, ServiceUtils.getSetIpMsg(localIp));
				return flag;
			}else{
				JOptionPane.showMessageDialog(frame, "本地ip获取失败");
				return false;
			}
		}
		return true;
	}
}
