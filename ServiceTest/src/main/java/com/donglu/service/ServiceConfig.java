package com.donglu.service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ServiceConfig implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8710168087805946672L;
	public static final String configFileName = "ServiceConfig.properties";
	public static ServiceConfig instance;
	
	private String deviceIp=null;
	private int devicePort=10001;
	private int serverPort=10002;
	
	private ServiceConfig(){}
	
	public static ServiceConfig getInstance(){
		if(instance == null){
			instance = new ServiceConfig();
		}
		instance.loadConfig();
		return instance;
	}
	
	public void saveConfig(){
		try (FileOutputStream fos = new FileOutputStream(configFileName, false);PrintWriter out = new PrintWriter(fos);){
			out.println(String.format("%s=%s", "deviceIp", deviceIp));
			out.println(String.format("%s=%s", "devicePort", devicePort));
			out.println(String.format("%s=%s", "serverPort", serverPort));
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void loadConfig() {
		Path path = Paths.get(configFileName);
		if(!Files.exists(path)){
			try {
				Files.createFile(path);
				saveConfig();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        try (FileInputStream fileInputStream = new FileInputStream(configFileName);){
        	Properties preferenceStore = new Properties();
            preferenceStore.load(fileInputStream);
            this.deviceIp = preferenceStore.getProperty("deviceIp");
            devicePort=Integer.valueOf(preferenceStore.getProperty("devicePort", "10001"));
            serverPort=Integer.valueOf(preferenceStore.getProperty("serverPort", "10002"));
        } catch (IOException e) {
            e.printStackTrace();
        } 
	}


	public static void main(String[] args) {
		ServiceConfig.getInstance().saveConfig();
	}

	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
		saveConfig();
	}

	public int getDevicePort() {
		return devicePort;
	}

	public void setDevicePort(int devicePort) {
		this.devicePort = devicePort;
		saveConfig();
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
		saveConfig();
	}
}
