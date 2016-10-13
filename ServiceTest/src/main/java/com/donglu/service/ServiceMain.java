package com.donglu.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
		ServiceConfig config = ServiceConfig.getInstance();
		String ip=null;
		ip=config.getDeviceIp();
		if (ip!=null) {
			String localIp = ServiceUtils.getLocalIp();
			if (localIp!=null) {
				boolean flag = false;
				for (int i = 0; i < 10&&!flag; i++) {
					config = ServiceConfig.getInstance();
					flag = ServiceUtils.sendMsg(ip, ServiceUtils.getSetIpMsg(localIp));
					writerMsg("设置设备:"+ip+"的服务器ip:"+localIp+"结果为："+flag);
					if (!flag) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}else{
				writerMsg("获取本机ip地址失败");
			}
		}
		ServerSocket socket = null;
		try {
			socket=new ServerSocket(config.getServerPort());
			while (true) {
				Socket accept = socket.accept();
				byte[] b=new byte[40];
				int read=0;
				boolean execute=false;
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
//					System.out.println(ServiceUtils.getHexString(b));
					String s = new String(ServiceUtils.getResultByte(b),"GBK");
					s=s.substring(0, s.indexOf(new String(new byte[]{(byte)0x00})));
					System.out.println(s);
					if (s.contains("shutdown")) {
						execute=cmdRun(s);
					}
					writerMsg(s);
				} catch (Exception e) {
					e.printStackTrace();
					writerMsg("监听是发生错误："+e.getMessage());
				}
				if (execute) {
					OutputStream os = accept.getOutputStream();
					os.write(ServiceUtils.getReturnResult(b, 'y'));
					os.flush();
				}else{
					OutputStream os = accept.getOutputStream();
					os.write(ServiceUtils.getReturnResult(b, 'n'));
					os.flush();
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
			Date date = new Date();
			File f = new File("service_log_"+new SimpleDateFormat("yyyyMM").format(date)+".txt");
			if (!f.exists()) {
				f.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(f, true);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
			fos.write((df.format(date) +"--"+ s+"\t\n").getBytes());
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
