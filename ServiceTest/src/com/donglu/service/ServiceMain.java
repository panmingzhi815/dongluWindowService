package com.donglu.service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.boris.winrun4j.AbstractService;
import org.boris.winrun4j.EventLog;
import org.boris.winrun4j.ServiceException;

public class ServiceMain extends AbstractService {

	@Override
	public int serviceMain(String[] arg0) throws ServiceException {
		int count = 0;
		try {
			File f = new File("service_log.txt");
			if (!f.exists()) {
				f.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(f, true);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
			fos.write((df.format(new Date()) + "winrun4j service start").getBytes());
			while (!shutdown) {
				try {
					Thread.sleep(6000);
				} catch (InterruptedException e) {
				}
				fos.write("serviet is starting".getBytes());
				if (++count % 10 == 0)
					EventLog.report("WinRun4J Test Service", EventLog.INFORMATION, "Ping");
			}
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
}
