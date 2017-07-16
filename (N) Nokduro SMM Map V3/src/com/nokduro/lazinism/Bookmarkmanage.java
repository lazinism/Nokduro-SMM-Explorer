package com.nokduro.lazinism;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Bookmarkmanage {
	
	LaunchData ld;
	final String authurl = "https://supermariomakerbookmark.nintendo.net/users/auth/nintendo";
	private String cookies;
	
	public Bookmarkmanage(LaunchData ld) {
		this.ld = ld;
	}
	
	public void logintoSMM(String id, String pw) throws IOException{
		String param = "lang=ja-JP&nintendo_authenticate&nintendo_authorize&scope&username="+id+"%password="+pw+"";
		postURL(authurl, param,"LOGIN");
	}
	
	public void addMap(String mapcode) throws IOException{
        String courseURL = "https://supermariomakerbookmark.nintendo.net/courses/"+mapcode;
        String bookmarkURL = courseURL + "/play_at_later";
        postURL(bookmarkURL, "","BOOKMARK");
	}
	public void postURL(String surl, String param, String message) throws IOException{
		URL url = new URL(surl);
		HttpsURLConnection conn =  (HttpsURLConnection)url.openConnection();
		conn.setRequestMethod("POST");
		conn.setUseCaches(false);
        if (cookies != null) {
        	conn.addRequestProperty("Cookie", cookies);
        }
        
        conn.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(param);
		wr.flush();
		wr.close();
		String cookieTemp = conn.getHeaderField("Set-Cookie");
        if(cookieTemp != null){
        	cookies = cookieTemp;
        }
        int responseCode = conn.getResponseCode();
        System.out.println("[Bookmarkmanage-"+message+"] POST 시도 중... URL: "+surl+" 코드:"+responseCode);
	}
}
