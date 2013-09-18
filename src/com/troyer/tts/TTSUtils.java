package com.troyer.tts;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class TTSUtils {

	public static File saveMp3File(String word, String dir, String tl) {
		String str = "http://translate.google.cn/translate_tts?ie=UTF-8&q=${word}&tl="+tl+"&prev=input";
		File file = null;
		String fileName = word;
		try {
			word = URLEncoder.encode(word, "utf-8");
			String urlstr = str.replace("${word}", word);

			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(urlstr);
			HttpResponse rs = client.execute(get);

			InputStream instream = rs.getEntity().getContent();

			file = new File(dir + "/" + tl + "/" + fileName + ".mp3");
			File parent = file.getParentFile(); 
			if(parent != null && !parent.exists()){
				parent.mkdirs();
			}
			
			FileOutputStream out = new FileOutputStream(file);
			int i = 0;
			while ((i = instream.read()) != -1) {
				out.write(i);
			}
			out.flush();
			out.close();
			instream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}
	
	public static Image getImage(String fileName) {
		URL url = FileUtils.class.getResource(fileName);
		Image image = Toolkit.getDefaultToolkit().getImage(url);
		return image;
	}
	
}

