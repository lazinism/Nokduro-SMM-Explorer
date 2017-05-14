package com.nokduro.lazinism;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

public class LzURLUtils {
    public static String getText(String url) throws IOException {
        URL website;
		try {
			website = new URL(url);
		} catch (MalformedURLException e) {
			System.out.println("올바른 URL이 아닙니다.");
			return "";
		}
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                    connection.getInputStream(), "UTF-8"));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        	response.append("\n");
        	}

        in.close();

        return response.toString();
    }
    public static String getAdvText(String url, String start, String end) throws IOException {
    	URL website;
    	try {
			website = new URL(url);
		} catch (MalformedURLException e) {
			System.out.println("올바른 URL이 아닙니다.");
			return "";
		}
    	URLConnection conn = website.openConnection();
    	BufferedReader in = new BufferedReader(
    							new InputStreamReader(
    								conn.getInputStream(), "UTF-8"));
    	
    	StringBuilder res = new StringBuilder();
    	String inputline;
    	Boolean started = false;
    	while(!started && ((inputline = in.readLine()) != null)){
    		if(inputline.contains(start)){
    			res.append(inputline);
            	res.append("\n");
    			started = true;
    			break;
    		}
    	}
    	while(started && ((inputline = in.readLine()) != null)){
    		res.append(inputline);
        	res.append("\n");
    		if(inputline.contains(end)){
    			break;
    		}
    	}
    	
    	in.close();
    	
    	return res.toString();
    }
    public static boolean checkValid(String url){
    	try {
        	URL website = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) website.openConnection();
			if(conn.getResponseCode() == 200){
				return true;
			}
			else return false;
    	} catch (UnknownHostException e){
    		System.out.println("네트워크에 정상적으로 연결되지 않았습니다.");
    		return false;
    	} catch(FileNotFoundException e){
    		System.out.println(url + ": 404 Error");
    		return false;
		} catch (IOException e) {
			return false;
		}
    }
}