package com.nokduro.lazinism;

import java.net.UnknownHostException;

public class LaunchWebServers {
	
	LaunchData data;
	SocketServer socketserv;
	WebServer webserv;

	public LaunchWebServers(LaunchData mapdata) {
		this.data = mapdata;
		try {
			socketserv = new SocketServer(5502, this.data);
			socketserv.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		webserv = new WebServer(5503, data);
	}
	
}
