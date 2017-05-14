package com.nokduro.lazinism;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class SocketServer extends WebSocketServer {
	LaunchData ld;
	Collection<WebSocket> connectedlist;
	public SocketServer(int port, LaunchData main) throws UnknownHostException {
		super(new InetSocketAddress(port));
		this.ld = main;
		this.connectedlist = new ArrayList<WebSocket>();
	}
	public SocketServer(InetSocketAddress address) {
		super(address);
	}

	@Override
	public void onOpen( WebSocket conn, ClientHandshake handshake ) {
		
		System.out.println( "[SocketServer] "+conn.getRemoteSocketAddress().getAddress().getHostAddress() + " ����" );
		
	}

	@Override
	public void onClose( WebSocket conn, int code, String reason, boolean remote ) {

		System.out.println( "[SocketServer] "+conn.getRemoteSocketAddress().getAddress().getHostAddress() + " ���� ����" );
	}

	@Override
	public void onMessage( WebSocket conn, String message ) {
		if(message.equals("connectSocket")){
			this.connectedlist.add(conn);
			System.out.println("[SocketServer] ���� �Ϸ�!");
		}
		else{
			if(this.connectedlist.contains(conn)){
				System.out.println("[SocketServer] �޽��� ����:"+message);
				if(message.contains("deleterow")){
					int rowcount = Integer.parseInt(message.split("-")[1]);
					try {
						System.out.println("[SocketServer] "+ld.mapdata.get(rowcount-1).get(2)+" Ŭ���� ó���մϴ�.");
						ld.writecleardata(ld.mapdata.get(rowcount-1).get(2));
						ld.mapdata.remove(rowcount-1);
					} catch (IOException e) {
						System.out.println("[SocketServer] Ŭ���� ó�� ����.");
					}
				}
				else if(message.contains("clearclear")){
					System.out.println("[SocketServer] Ŭ���� ������ ����.");
					ld.deleteclear();
				}
				else if(message.contains("refreshlist")){
					try {
						System.out.println("[SocketServer] ����Ʈ ���� ��ħ");
						ld.refreshcancel = true;
						while(!ld.refreshdone){
							Thread.sleep(10);
						}
						ld.refreshcancel = false;
						ld.refreshList2();
					} catch (Exception e) {
						System.out.println("[SocketServer] ����Ʈ ���ΰ�ħ ����.");
					}
				}
			}
		}
	}
	
	public void sendToAll( String text ) {
		Collection<WebSocket> con = this.connectedlist;
		synchronized ( con ) {
			for( WebSocket c : con ) {
				c.send( text );
			}
		}
	}
	@Override
	public void onError(WebSocket arg0, Exception arg1) {
		System.out.println("[SocketServer] ���� �߻�: "+arg0.getRemoteSocketAddress().getAddress().getHostAddress()+", "+arg1.toString()+" on "+arg1.getCause());
	}
}
