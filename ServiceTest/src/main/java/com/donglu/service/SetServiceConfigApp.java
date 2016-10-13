package com.donglu.service;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JPanel;

public class SetServiceConfigApp {

	private JFrame frame;
	private JTextField textField;
	private JTextArea textArea;

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
		frame.setBounds(100, 100, 331, 381);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 10, 287, 41);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblip = new JLabel("设备ip");
		lblip.setBounds(0, 10, 36, 15);
		panel.add(lblip);
		
		textField = new JTextField();
		textField.setBounds(41, 7, 171, 21);
		panel.add(textField);
		textField.setColumns(10);
		textField.setText(ServiceConfig.getInstance().getDeviceIp());
		
		JButton button = new JButton("确定");
		button.setBounds(222, 6, 65, 23);
		panel.add(button);
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
				List<String> addService = CmdUtil.addService();
				if (addService!=null) {
					for (String string : addService) {
						textArea.append(string+"\t\n");
					}
					JOptionPane.showConfirmDialog(frame, "服务器启动完成");
				}else{
					JOptionPane.showMessageDialog(frame, "服务启动失败，请用管理员身份运行");
				}
				System.exit(0);
			}
		});
		
		textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setBounds(10, 65, 295, 268);
		frame.getContentPane().add(textArea);
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
