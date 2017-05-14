package com.nokduro.lazinism;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

@SuppressWarnings("restriction")
public class WebServer {
	
	LaunchData main;
	
	public WebServer(int port, LaunchData data) {
		this.main = data;
		HttpServer server;
		try {
			server = HttpServer.create(new InetSocketAddress(port), 0);
			server.createContext("/", new MapHandler(main));
			server.createContext("/data", new DataHandler(main));
	        server.setExecutor(null);
	        server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		
	}
	static class MapHandler implements HttpHandler {
		LaunchData maindata;
        public MapHandler(LaunchData main) {
        	this.maindata = main;
        }

		@Override
        public void handle(HttpExchange t) throws IOException {
			
            String response = "<html><head><title>녹두로 트게더 맵 추천 탐색기 Ver. "+Execute.version+"</title>"
            		+ "<style>"
            		+ "table, td,th{border: 1px solid black;border-collapse: collapse;}table{margin: auto;text-align: center;}a.col000000:link{color: black;text-decoration: none;}a.col000000:visited{color: black;text-decoration: none;}a.col000000:hover{color: black;text-decoration: none;}a.col000000:active{color: black;text-decoration: none;}a.colffffff:link{color: white;text-decoration: none;}a.colffffff:visited{color: white;text-decoration: none;}a.colffffff:hover{color: white;text-decoration: none;}a.colffffff:active{color: white;text-decoration: none;}"
            		+ "</style>"
            		+ "<script>"
            		+ "var reversed = false;"
            		+ "function reverseTableRows(tableId) {"
            		+ "    var table = document.getElementById(tableId);"
            		+ "        newTbody = document.createElement('tbody');"
            		+ "        oldTbody = table.tBodies[0];"
            		+ "        newTbody.appendChild(oldTbody.rows[0]);"
            		+ "        oldrows = oldTbody.rows;"
            		+ "        i = oldrows.length - 1;"
            		+ "    while (i >= 0) {"
            		+ "        newTbody.appendChild(oldrows[i]);"
            		+ "        i -= 1;"
            		+ "    }\n"
            		+ "    oldTbody.parentNode.replaceChild(newTbody, oldTbody);"
            		+ "reversed = !reversed;"
            		+ "}"
            		+ "function sendMessage(msg) {"
            		+ "waitForSocketConnection(ws, function() {"
            		+ "ws.send(msg);"
            		+ "});};"
            		+ "function waitForSocketConnection(socket, callback){"
            		+ "setTimeout("
            		+ "function(){"
            		+ "if (socket.readyState === 1) {"
            		+ "if(callback !== undefined){"
            		+ "callback();"
            		+ "}"
            		+ "return;"
            		+ "} else {"
            		+ "waitForSocketConnection(socket,callback);"
            		+ "}"
            		+ "}, 5);"
            		+ "};"
            		+ "ws = new WebSocket(\"ws:localhost:5502/\");"
            		+ "ws.onmessage = function(e){"
            		+ "if(e.data.includes(\"refresh\")){refreshdata();} "
            		+ "};"
            		+ "ws.onopen = function(e){sendMessage(\"connectSocket\");};"
            		+ "function refreshdata(){"
            		+ "sendMessage(\"refreshlist\");"
            		+ "window.location.reload(true);"
            		+ "};"
            		+ "function httpGet(theUrl)"
					+ "{"
					+ "var xmlHttp = new XMLHttpRequest();"
					+ "xmlHttp.open( \"GET\", theUrl, false );"
					+ "xmlHttp.send( null );"
					+ "return xmlHttp.responseText;"
					+ "}; "
					+ "function deleterow(i){"
					+ "var tempreverse = false;"
					+ "if(reversed){"
					+ "reverseTableRows(\'tgdtable\');"
					+ "tempreverse = true;"
					+ "}"
					+ "row = document.getElementById(\"buttonid-\".concat(i)).rowIndex;"
					+ "document.getElementById(\"tgdtable\").deleteRow(row);"
					+ "sendMessage(\"deleterow-\".concat(row));"
					+ "if(tempreverse){"
					+ "reverseTableRows(\'tgdtable\');"
					+ "tempreverse = false;"
					+ "}"
					+ "};"
					+ "function clearclear(){"
					+ "sendMessage(\"clearclear\");"
					+ "alert(\"리스트 새로 고침을 해야 적용됩니다.\");"
					+ "};"
            		+ "</script></head><body>"
            		+ "<div style=\"margin-bottom: 8px\" id=\"button\"><button width=200 type=\"button\" onclick=\"refreshdata()\">리스트 새로 고침</button>  <button width=200 type=\"button\" onclick=\"clearclear()\">클리어 데이터 지우기</button>  <button width=200 type=\"button\" onclick=\"reverseTableRows(\'tgdtable\')\">역순 정렬</button></div>"
            		+ "<div id=\"tablediv\" width=1850><table id=\"tgdtable\" style=\"width:100%\" align=\"center\">"
            		+ "<tr><th width=800>트게더 제목</th><th width=150>작성자</th><th width=300>맵 코드</th><th width=400>원 제목</th><th width=100>클리어 퍼센트</th><th width=100>클리어 체크</th></tr>";
            int i=1;
            Map<String, String> colorcode = new HashMap<String, String>();
    		colorcode.put("Expert", "#ea348b");
    		colorcode.put("Super Expert", "#ff4545");
    		colorcode.put("Normal", "#2691bc");
    		colorcode.put("Easy", "#28ad8a");
    		colorcode.put("None", "#f9cf00");
            for(Vector<String> v :this.maindata.mapdata){
            	String backcolor = "#ffffff";
            	String fontcolor = "000000";
            	switch(v.get(6)){
            		case "Super Expert":
            			backcolor = colorcode.get(v.get(6));
            			fontcolor = "ffffff";
            			break;
            		case "Expert":
            			backcolor = colorcode.get(v.get(6));
            			fontcolor = "ffffff";
            			break;
            		case "Normal":
            			backcolor = colorcode.get(v.get(6));
            			fontcolor = "ffffff";
            			break;
            		case "Easy":
            			backcolor = colorcode.get(v.get(6));
            			fontcolor = "ffffff";
            			break;
            		case "None":
            			backcolor = colorcode.get(v.get(6));
            			fontcolor = "000000";
            			break;
            		default:
            			backcolor = "#ffffff";
            			fontcolor = "000000";	
            	}
            	response +="<tr style=\"background-color: "+backcolor+";color:#"+fontcolor+";\" id=\"buttonid-"+i+"\">";
            	response +="<td><a target=\"_blank\" class=\"col"+fontcolor+"\" href=\""+v.get(3)+"\">";
            	response +=v.get(0);
            	response +="</a></td>";
            	response +="<td>";
            	response +=v.get(1);
            	response +="</td>";
            	response +="<td>";
            	response +=v.get(2);
            	response +="</td>";
            	response +="<td><a target=\"_blank\" class=\"col"+fontcolor+"\" href=\""+v.get(4)+"\">";
            	response +=v.get(5);
            	response +="</a></td>";
            	response +="<td style=\"background-color: "+backcolor+";color:#"+fontcolor+"\">";
            	response +=v.get(7);
            	response +="</td>";
            	response +="<td>";
            	response +="<button type=\"button\" onclick=\"deleterow("+i+")\" width=100>클리어 표시!</button>";
            	response +="</td>";
            	response +="</tr>";
            	i+=1;
            }
            response +="</table></div>"
            		+ "</body></html>";
            byte[] out = response.getBytes("UTF-8");
			t.getResponseHeaders().set("Content-Type", "text/html; charset=" + "UTF-8");
            t.sendResponseHeaders(200, out.length);
            OutputStream os = t.getResponseBody();
            os.write(out);
            os.close();
        }
	}
		
	static class DataHandler implements HttpHandler {
		LaunchData maindata;
		public DataHandler(LaunchData data) {
			this.maindata = data;
		}

		@Override
		public void handle(HttpExchange t) throws IOException {
			Map<String, String> result = new HashMap<String, String>();
			String q = t.getRequestURI().getQuery();
			String response = "";
			if(q!=null){
				for (String param : q.split("&")) {
			        String pair[] = param.split("=");
			        if (pair.length>1) {
			            result.put(pair[0], pair[1]);
			        }else{
			            result.put(pair[0], "");
			        }
			    }
				String s = result.get("order");
				if(s!=null|s!=""){
					int order = 0;
					try{
						order = Integer.parseInt(s);
						if(order < this.maindata.mapdata.size()){
							response += this.maindata.mapdata.get(order);
						}
						else response = String.valueOf(this.maindata.mapdata.size());
					}catch(NumberFormatException e){
						response = String.valueOf(this.maindata.mapdata.size());
					}
				}
				else response = String.valueOf(this.maindata.mapdata.size());
			}
			else response = String.valueOf(this.maindata.mapdata.size());
			byte[] out = response.getBytes("UTF-8");
			t.getResponseHeaders().set("Content-Type", "text/html; charset=" + "UTF-8");
            t.sendResponseHeaders(200, out.length);
            OutputStream os = t.getResponseBody();
            os.write(out);
            os.close();
		}
	}
}
