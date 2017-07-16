package com.nokduro.lazinism;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LaunchData {
	
	Bookmarkmanage bm;
	ArrayList<String> clearlist;
	ArrayList<Vector<String>> mapdata;
	boolean refreshcancel,refreshdone;
	JFrame jf;
	JLabel jl;
	JTextField id;
	JPasswordField pw;
	JPanel jp;
	public LaunchWebServers lwss;
	
	public LaunchData() {
		try {
			this.clearlist = readcleardata();
		} catch (IOException e2) {
			this.clearlist = new ArrayList<String>();
		}
		refreshcancel = false;
		this.lwss = new LaunchWebServers(this);
		bm = new Bookmarkmanage(this);
		jf = new JFrame("NSMM - 귀챠니즘 v."+Execute.version);
		jf.setSize(200, 150);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setcenter(jf);jf.setLayout(new GridLayout(4, 1));
		jl = new JLabel("Loading...", SwingConstants.CENTER);
		jf.add(jl);
		JLabel jl2 = new JLabel("아래의 주소를 입력해 주세요.", SwingConstants.CENTER);
		jf.add(jl2);
		JTextField jtf = new JTextField("http://localhost:5503/");
		jtf.setHorizontalAlignment(JTextField.CENTER);
		jtf.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getSource().equals(jtf)){
					if(e.getClickCount()==1) jtf.selectAll();
					else if(e.getClickCount()==2)
						try {
							java.awt.Desktop.getDesktop().browse(new URI(jtf.getText()));
						} catch (IOException | URISyntaxException e1) {	}
				}
			}
		});
		jtf.setEditable(false);
		jf.add(jtf);
		jp = new JPanel();
		jp.setLayout(new GridLayout(1, 2));
		id = new JTextField("아이디");
		pw = new JPasswordField("비밀번호");
		jp.add(id);jp.add(pw);
		jf.add(jp);
		jf.setResizable(false);
		jf.setVisible(true);
		try {
			refreshList2();
		} catch (Exception e) {}
	}
	public void setcenter(JFrame frame){
		Dimension dimen = Toolkit.getDefaultToolkit().getScreenSize(); // 화면 사이즈	
	    Dimension dimen1 = frame.getSize(); // 프레임 사이즈
	    int xpos = (int)(dimen.getWidth()/2-dimen1.getWidth()/2); 
	    int ypos = (int)(dimen.getHeight()/2-dimen1.getHeight()/2);
	    frame.setLocation(xpos,ypos);
	}
	public void refreshList2() throws Exception{
		long startTime = System.currentTimeMillis();
		refreshdone = false;
		mapdata = new ArrayList<Vector<String>>();
		refreshList(1, 1);
		refreshList(2, 31);
		jl.setText("로딩 완료!");
		System.out.println("[LaunchData] 새로고침 완료");
		refreshdone = true;
		long endTime = System.currentTimeMillis();
		System.out.println("[LaunchData] TIME : " + (endTime - startTime) + "(ms)");
	}
	public void refreshList(int pageno, int count) throws Exception{
		ArrayList<String> linklist = null;
		if(pageno==2){
			String s1 = LzURLUtils.getAdvText("https://tgd.kr/nokduro/page/2?category=697759", "/nokduro?category=697759\">마리오 메이커 맵추천</a></li></ul>", "<div class=\"has-category hidden-md-down\">");
			linklist = findcontent(s1, "<a href=\"\\/([0-9])\\d+\\?category=697759\" >");
		}
		else{
			String s1 = LzURLUtils.getAdvText("https://tgd.kr/nokduro?category=697759", "/nokduro?category=697759\">마리오 메이커 맵추천</a></li></ul>", "<div class=\"has-category hidden-md-down\">");
			linklist = findcontent(s1, "<a href=\"\\/([0-9])\\d+\\?category=697759\" >");
		}
		for (String s2:linklist){
			if(!refreshcancel){
				jl.setText("Loading... ("+count+"/60)");
				System.out.println("[LaunchData] Loading... ("+count+"/60)");
				String s3 = s2.replaceAll("<a href=\"", "https://tgd.kr").replaceAll("\" >", "");//트게더 링크
				String content = LzURLUtils.getAdvText(s3, "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">", "<meta name=\"robots\" content=\"index, follow\" />");
				String title = findcontent(content, "<title>.*<\\/title>").get(0).replaceAll("<title>녹두로 - ", "").replaceAll(" - 트게더<\\/title>", "");
				String incontent = findcontent(content, "<meta name=\"description\" content=\".*\" \\/>").get(0);
				ArrayList<String> codecontent = findcontent(incontent, "[A-Z0-9]{4} [A-Z0-9]{4} [A-Z0-9]{4} [A-Z0-9]{4}|[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{4}");
				String author = findcontent(content, "<meta name=\"author\" content=\".*\" \\/>").get(0).replaceAll("<meta name=\"author\" content=\"", "").replaceAll("\" \\/>", "");
				for(String code:codecontent){
					String newcode = code.replaceAll(" ", "-");
					if(!clearlist.contains(newcode)){
						if(LzURLUtils.checkValid("https://supermariomakerbookmark.nintendo.net/courses/"+newcode)){
							Vector<String> data = new Vector<String>();
							data.addElement(title);data.addElement(author);data.addElement(newcode);data.addElement(s3);
							Thread t = new Thread(new getPercent(this, newcode, data, this.lwss));
							t.run();
						}
					}
				}
				count++;
			}
		}
	}
	public ArrayList<String> findcontent(String s, String regex){
		ArrayList<String> list = new ArrayList<String>();
		Matcher m = Pattern.compile(regex).matcher(s);
		while(m.find()){
			list.add(m.group());
		}
		return list;
	}
	public ArrayList<String> readcleardata() throws IOException{
		File f = new File(System.getProperty("user.dir")+"\\smm\\cleardata.txt");
		ArrayList<String> s = new ArrayList<String>();
		if(!f.exists()){
			return s;
		}
		else{
			String read = new String(Files.readAllBytes(f.toPath()));
			for (String st:read.split("\\r\\n")) {s.add(st);}
			return s;
		}
	}
	
	public void writecleardata(String newdata) throws IOException{
		(new File(System.getProperty("user.dir")+"\\smm")).mkdir();
		File f = new File(System.getProperty("user.dir")+"\\smm\\cleardata.txt");
		if(!f.exists()){
			f.createNewFile();
		}
		FileWriter fw = new FileWriter(f,true);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter pw = new PrintWriter(bw);
		pw.println(newdata);
		pw.flush();
		pw.close();
		clearlist.add(newdata);
	}
	public void deleteclear(){
		File f = new File(System.getProperty("user.dir")+"\\smm\\cleardata.txt");
		f.delete();
		this.clearlist = new ArrayList<String>();
	}
	
	class getPercent implements Runnable{
		String data;
		Vector<String> vector;
		int rowCount;
		LaunchWebServers lwss;
		public getPercent(LaunchData launchData, String code, Vector<String> vector, LaunchWebServers lwss){
			this.data = code;
			this.vector = vector;
			this.lwss = lwss;
		}
		@Override
		public void run() {
			String smmcontent;
			try {
				smmcontent = LzURLUtils.getAdvText("https://supermariomakerbookmark.nintendo.net/courses/"+data, "<meta property=\"og:title\"", "<div class=\"course-title-wrapper\">");
				String smmtitleraw = findcontent(
						smmcontent,
						"SUPER MARIO MAKER BOOKMARK \\| .* - [A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{4}")
						.get(0).replaceAll("SUPER MARIO MAKER BOOKMARK \\| ", "").replaceAll(" - "+data, "");
				String smmtitle = smmtitleraw.replaceAll("&#39;", "'");
				String diff = "None";
				if(smmcontent.contains("Easy")) diff = "Easy";
				else if(smmcontent.contains("Normal")) diff = "Normal";
				else if(smmcontent.contains("Super Expert")) diff = "Super Expert";
				else if(smmcontent.contains("Expert")) diff = "Expert";
				else smmcontent = "None";
				vector.add("https://supermariomakerbookmark.nintendo.net/courses/"+data);
				vector.add(smmtitle);
				vector.add(diff);
				String percent = findpercent(smmcontent);
				vector.addElement(percent);
				if(!refreshcancel){
					mapdata.add(vector);
					}
			} catch (Exception e) {
			}
		}
		public String findpercent(String smmcontent){
			String percent = "";
			if(smmcontent.split("<div class=\"clear-flag\"><\\/div>").length > 1){
				String percentageraw = smmcontent.split("<div class=\"clear-flag\"><\\/div>")[1].split("<div class=\"typography typography-percent\">")[0];
				
				ArrayList<String> ptraw = findcontent(percentageraw, "<div class=\"typography typography-([0-9]|second)\">");
				for(String pt:ptraw){
					String integer = pt.replaceAll("<div class=\"typography typography-", "").replaceAll("\">", "").replaceAll("second", ".");
					percent += integer;
				}
				percent+="%";
			}
			else percent = "N/A";
			return percent;
		}
	}
}
