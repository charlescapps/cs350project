package webcrawler;

import java.net.URL;
import java.net.HttpURLConnection; 
import java.util.TreeSet; 
import java.util.ArrayList;
import java.util.regex.*; 
import java.io.BufferedReader;

public class WebPageParser {
	private URL curURL; 
	private UrlHttpRequest conn; 
	private BufferedReader httpReader; 
	private String line; 
	private Pattern linkRegex;
	
	public WebPageParser() {
		curURL = null; 
		conn = null; 
		httpReader = null;	
		
		//Regex to find Strings of form href="someURL" where there is at least one space before href 
		//and any amount of space between href, =, and "someURL"
		linkRegex = Pattern.compile("(?i:\\shref\\s*=\\s*[\"\']([^\'\"]+)[\"\'])");
	}
	
	public void setURL(URL u) throws Exception{
		curURL = u; 
		
		if (curURL == null)
			throw new Exception("Cannot find links for NULL URL!");
		
		conn = new UrlHttpRequest(curURL, "GET", null); 
		
		System.out.println("Response Code from " + curURL.toString() +  ": " + conn.getResponseCode());
		
		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK)
			throw new Exception("Invalid Link or failed to connect: " + curURL.toString());
	}
	/*
	public TreeSet<URL> getLinks() throws Exception, java.io.IOException {
		
		httpReader = conn.getInputReader();
		
		ArrayList<String> links = new ArrayList<String>(); 		
		
		//Parse each line, and add all the links found
		while ( (line = httpReader.readLine()) !=null) {
			links.addAll(getLinks(line)); 
		}
		
		return getGoodUrls(links); 
	}*/
	
public TreeSet<URL> getLinks() throws Exception, java.io.IOException {
		
		httpReader = conn.getInputReader();
		
		ArrayList<String> links = new ArrayList<String>(); 	
		
		StringBuilder webpage = new StringBuilder();
		
		//Parse each line, and add all the links found
		while ( (line = httpReader.readLine()) !=null) {
			webpage.append(line);
			 
		}
		
		links.addAll(getLinks(webpage.toString()));
		
		return getGoodUrls(links); 
	}
	
	private ArrayList<String> getLinks(String aLine) {
		
		//Regular expression to find the URL inside a link tag: <a href="(group)">
		//I ran into issues where the program would hang when I tried to account for attributes before the "href" 
		//So, for now I'm simply finding strings of the form <a href="someURL"
		//Where there can be any amount of whitespace in-between <, a, href, =, and "someURL"
		
		//Pattern regex = Pattern.compile("(?i:<\\s*a\\s+href\\s*=\\s*[\"\']([^\'\">]*)[\"\'])");
		
		//Trying a simpler regex to improve performance...all we really care about is href="someURL"
		
		Matcher m = linkRegex.matcher(aLine);
		
		ArrayList<String> linksFound = new ArrayList<String>(); 
		String urlStr;
		
		while (m.find()) {
			urlStr = m.group(1); 
			
			//If the URL doesn't start with '#'
			if (urlStr.charAt(0)!='#') 
				linksFound.add(urlStr);	
			
		}
			
		//System.out.println("Num matches in line:" + linksFound.size());
		return linksFound;
		
	}
	
	private TreeSet<URL> getGoodUrls(ArrayList<String> links) throws java.net.MalformedURLException {
		
		//Create TreeSet that compares URL's based on equality of Host Name
		TreeSet<URL> goodLinks = new TreeSet<URL>(new URLHostComparator()); 
		URL tmpURL;
		
		//Go through list of links we found, and create a new URL for each
		for (String l:links) {
			try {
				tmpURL = new URL(l);
				//Add the link to the base webpage, i.e. throw out the URI
				if (tmpURL.getProtocol().indexOf("http")>=0)
					goodLinks.add(new URL(tmpURL.getProtocol(), tmpURL.getHost(), ""));
			}
			catch (java.net.MalformedURLException e) {
				//System.out.println("Found invalid URL '"+ e.getMessage() + "' when parsing web page at: " + curURL.toString());
			}
		}
			
		System.out.println("Num links found from " + curURL.toString() + ": " + goodLinks.size());
		
		return goodLinks;
	}
	
	public void disconnect() {
		conn.disconnect();
	}
	
	
	
	
	
}
