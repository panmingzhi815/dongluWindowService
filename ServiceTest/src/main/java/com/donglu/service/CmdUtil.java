package com.donglu.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class CmdUtil {
	public static void main(String[] args) {
		addService();
	}

	/**
	 * 
	 */
	public static List<String> addService() {
		Runtime runtime = Runtime.getRuntime();
		try {
			File file = new File("temp.bat");
			if (!file.exists()) {
				boolean b = file.createNewFile();
				if (!b) {
					return null;
				}
			}
			List<String> list = new ArrayList<>();
			String classpath = System.getProperty("user.dir");
			list.add("cd "+classpath);
			list.add(classpath.substring(0, 2));
			List<String> lines = Files.readAllLines(Paths.get("run.bat"));
			PrintStream ps=new PrintStream(new FileOutputStream(file));
			for (String string : list) {
				ps.println(string);
			}
			for (String string : lines) {
				ps.println(string);
			}
			ps.flush();
			ps.close();
			return runCmd(runtime,"temp.bat");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param runtime
	 * @param cmd 
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public static List<String> runCmd(Runtime runtime, String cmd) throws IOException, UnsupportedEncodingException {
		List<String> list=new ArrayList<>();
		Process exec = runtime.exec(cmd);
		InputStream inputStream = exec.getInputStream();
		BufferedReader br=new BufferedReader(new InputStreamReader(inputStream, "GBK"));
		String s=null;
		while ((s=br.readLine())!=null) {
			System.out.println(s);
			list.add(s);
		}
		return list;
	}
}
