package webcrawler; 

import java.net.*;
import java.util.*;
import java.io.*;

/**
  *  Class for facilitating Http requests. 
  *  Takes a host String, a port int, a HashMap<String, String> of request paramters, 
  *  and a method String indicating if it is a GET, POST, etc. 
  *  
  *  Written by Charles Capps for CS399J at Portland State University
  *  
  *  @author Charles Capps
  *
  **/
  
  
public class UrlHttpRequest{

	HttpURLConnection myConnection;
	private final String enc = "UTF-8";
	private static final int timeout = 200; //200 ms default timeouts

	public UrlHttpRequest(String host, String port, String uriString, HashMap<String, String> parameters, String method) 
				throws IOException, ProtocolException
	{
		String urlString = "http://" + host +  ":" + port + uriString;  
		
		if (method=="GET")
		{
			urlString = buildQueryString(urlString, parameters); 
		}
		
		URL url = new URL(urlString); 
		
		//System.out.println(urlString);
		
		myConnection = (HttpURLConnection) url.openConnection();
		myConnection.setConnectTimeout(200);
		myConnection.setReadTimeout(200);
		myConnection.setRequestMethod(method); 
		
		if (method=="POST")
		{
			myConnection.setDoOutput(true); 
		}
		
		myConnection.connect(); 
		
		if (method=="POST")
		{
			writePostData(parameters); 
		}
	}
	
	public UrlHttpRequest(URL url, String method, HashMap<String, String> parameters) 
		throws IOException, ProtocolException {	
			
			//System.out.println(urlString);
			
			myConnection = (HttpURLConnection) url.openConnection();
			myConnection.setRequestMethod(method); 
			
			if (method=="POST")
			{
				myConnection.setDoOutput(true); 
			}
			
			myConnection.connect(); 
			
			if (method=="POST")
			{
				writePostData(parameters); 
			}
		}
	
	private void writePostData(HashMap<String, String> parameters) throws IOException
	{
		if (parameters==null)
			return;
		
		BufferedWriter bw = new BufferedWriter(new PrintWriter(myConnection.getOutputStream()));
		boolean start = true;
		
		for (String key : parameters.keySet())
		{
			if (!start)
				bw.write("&"); 
				
			bw.write(URLEncoder.encode(key, enc) + "=" + URLEncoder.encode(parameters.get(key), enc));
			
			start = false; 
		}
		
		bw.flush();
	}
	
	
	public void printResponseMessage() throws IOException
	{
		System.out.println(); 
		BufferedReader br = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
		do{
			System.out.println(br.readLine()); 
		}
		while (br.ready());
	}
	
	public BufferedReader getInputReader() throws IOException{
		return new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
	
	}
	
	public int getResponseCode() throws IOException{
		return myConnection.getResponseCode();
	}
	
	private String buildQueryString(String urlString, HashMap<String, String> parameters) throws IOException
	{
		StringBuilder urlQueryString = new StringBuilder(urlString); 
			
		if (!parameters.isEmpty())
		{
			urlQueryString.append("?"); 
		}
		
		for (String key : parameters.keySet())
		{
			if (urlQueryString.charAt(urlQueryString.length()-1)!='?')
			{
				urlQueryString.append("&"); 
			}
			urlQueryString.append(URLEncoder.encode(key, enc) + "=" + URLEncoder.encode(parameters.get(key), enc));
			
		}
		
		return urlQueryString.toString(); 
		
	}
	
	public void disconnect() {
		myConnection.disconnect();
	}


}