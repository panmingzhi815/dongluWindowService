package com.donglu.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServiceUtils {
	public static void main(String[] args) {
//		byte[] setIpMsg = ServiceUtils.getCloseMsg("shutdown -s -t 660");
		byte[] setIpMsg = ServiceUtils.getCloseMsg("shutdown -a");
		System.out.println(ServiceUtils.getHexString(setIpMsg));
		System.out.println(ServiceUtils.getHexString(ServiceUtils.getResultByte(setIpMsg)));
		System.out.println(ServiceUtils.getHexString(ServiceUtils.getReturnResult(setIpMsg, 'y')));
		System.out.println(new String(ServiceUtils.getResultByte(ServiceUtils.getReturnResult(setIpMsg, 'y'))));
		sendMsg("127.0.0.1",setIpMsg); 
	}
	/**
	 * @param setIpMsg
	 */
	public static boolean sendMsg(String ip,byte[] setIpMsg) {
		try {
			Socket c=new Socket(ip, 12345);
			c.setSoTimeout(2000);
			OutputStream os = c.getOutputStream();
			os.write(setIpMsg);
			os.flush();
			InputStream is = c.getInputStream();
			byte[] b = new byte[11];
			is.read(b);
			System.out.println(getHexString(b));
			byte[] resultByte = getResultByte(b);
			c.close();
			return new String(resultByte).trim().equals("y");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 获取发送ip的数据
	 * @param ip
	 * @return
	 */
	public static byte[] getSetIpMsg(String ip){
		String[] split = ip.split("\\.");
		if (split.length!=4) {
			return null;
		}
		byte[] b=new byte[4];
		int j = 0;
		for (int i = 3; i >= 0; i--) {
			String s2 = split[i];
			b[j++] = (byte) Integer.parseInt(s2);
		}
		return initSendByte(b, CodeEnum.setServiceIp.getCode(),CodeEnum.setServiceIp.getLength());
	}
	
	public static byte[] getCloseMsg(String close){
		byte[] b = close.getBytes();
		return initSendByte(b, (byte) 0x11,30);
	}
	
	/**
	 * 组建字节
	 * @param data 数据
	 * @param code 功能码
	 * @param dataLength 
	 * @return
	 */
	public static byte[] initSendByte(byte[] data,byte code, int dataLength) {
		byte[] b=new byte[10+dataLength];
		for (int i = 0; i < b.length; i++) {
			b[i]=(byte) 0xff;
		}
		int j=0;
		b[j++]=(byte) Integer.parseInt("01", 16);
		b[j++]=(byte) Integer.parseInt("57", 16);
		b[j++]=(byte) Integer.parseInt("00", 16);
		b[j++]=(byte) Integer.parseInt("01", 16);
		b[j++]=(byte) Integer.parseInt("00", 16);
		b[j++]=(byte) Integer.parseInt("01", 16);
		b[j++]=code;
		b[j++]=(byte) Integer.parseInt("02", 16);
		for (int i = 0; i < data.length; i++) {
			b[j++] = data[i];
		}
		b[b.length-2]=(byte) Integer.parseInt("03", 16);
		b[b.length-1]=BCC(b, 0, b.length-1);
		return b;
	}
	
	public  static byte[] getResultByte(byte[] bytes){
		byte[] b=new byte[bytes.length-10];
		for (int i = 0; i < b.length; i++) {
			b[i] = bytes[i+8];
		}
		return b;
	}
	
	public static byte[] getReturnResult(byte[] data,char result){
		byte[] b=new byte[1];
		b[0]=(byte) result;
		byte code=getCode(data);
		return initSendByte(b, code,1);
	}
	
	private static byte getCode(byte[] data) {
		return data[6];
	}
	public static String getHexString(byte[] bytes){
		StringBuilder sb=new StringBuilder();
		
		for (int j = 0; j < bytes.length; j++) {
			byte b = bytes[j];
			int i=b&0xff;
			String hexString = Integer.toHexString(i);
			if (hexString.length()==1) {
				hexString="0"+hexString;
			}
			sb.append(hexString.toUpperCase());
			if (j!=bytes.length-1) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}
	/**
     * 计算校验位
     *
     * @param bb
     * @param start
     * @param length
     * @return
     */
    public static byte BCC(byte[] bb, int start, int length) {
        byte checksum = bb[start];
        for (int i = start + 1; i < (start + length); i++) {
            checksum ^= bb[i];
        }
        checksum |= 0x20;
        return checksum;
    }
	public static String getLocalIp(){
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			return localHost.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
}
