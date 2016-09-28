package com.donglu.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.boris.winrun4j.AbstractService;
import org.boris.winrun4j.ServiceException;

public class ServiceMain extends AbstractService {
	private int timeOut=2000;
	@Override
	public int serviceMain(String[] arg0) throws ServiceException {
		int count = 0;
		count=startSocket();
		return count;
	}

	/**
	 * 
	 */
	public int startSocket() {
		String ip=null;
		File file = new File("controlIp.txt");
		if (file.exists()) {
			try(BufferedReader br=new BufferedReader(new FileReader(file))) {
				String string = br.readLine();
				if (string!=null&&string.trim().split("\\.").length==4) {
					ip=string;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("ip==="+ip);
		if (ip!=null) {
			String localIp = ServiceUtils.getLocalIp();
			System.out.println("localIp==="+localIp);
			if (localIp!=null) {
				boolean flag = false;
				while (!flag) {
					flag = ServiceUtils.sendMsg(ip, ServiceUtils.getSetIpMsg(localIp));
				}
			}
		}
		ServerSocket socket = null;
		try {
			socket=new ServerSocket(12345);
			while (true) {
				Socket accept = socket.accept();
				byte[] b=new byte[40];
				int read=0;
				try {
					accept.setSoTimeout(2000);
					long currentTimeMillis = System.currentTimeMillis();
					InputStream  is= accept.getInputStream();
					while (read!=b.length) {
						if (System.currentTimeMillis()-currentTimeMillis>timeOut) {
							throw new Exception("读取超时");
						}
						read=is.available();
						Thread.sleep(10);
					}
					is.read(b);
					System.out.println(ServiceUtils.getHexString(b));
					String s = new String(ServiceUtils.getResultByte(b));
					s=s.substring(0, s.indexOf(new String(new byte[]{(byte)0xff})));
					System.out.println(s);
					boolean flag=true;
					if (s.contains("shutdown")) {
						flag=cmdRun(s);
					}
					if (flag) {
						OutputStream os = accept.getOutputStream();
						os.write(ServiceUtils.getReturnResult(b, 'y'));
						os.write(b);
						os.flush();
					}
					writerMsg(s);
				} catch (Exception e) {
					e.printStackTrace();
					writerMsg("监听是发生错误："+e.getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (socket!=null) {
					socket.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return 0;
	}

	private boolean cmdRun(String s) {
		try {
			Runtime.getRuntime().exec(s);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param b
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void writerMsg(String s) {
		try {
			File f = new File("service_log.txt");
			if (!f.exists()) {
				f.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(f, true);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
			fos.write((df.format(new Date()) +"--"+ s+"\t\n").getBytes());
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ServiceMain s=new ServiceMain();
		s.startSocket();
	}
}
