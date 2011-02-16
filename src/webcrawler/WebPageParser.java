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
	
	public WebPageParser() {
		curURL = null; 
		conn = null; 
		httpReader = null;	
	}
	
	public void setURL(URL u) {
		curURL = u; 
	}
	
	public TreeSet<URL> getLinks() throws Exception, java.io.IOException {
		
		if (curURL == null)
			throw new Exception("Cannot find links for NULL URL!");
		
		conn = new UrlHttpRequest(curURL, "GET", null); 
		
		System.out.println("Response Code: " + conn.getResponseCode());
		
		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK)
			throw new Exception("Invalid Link or failed to connect: " + curURL.toString());
		
		httpReader = conn.getInputReader();
		
		ArrayList<String> links = new ArrayList<String>(); 		
		
		//Parse each line, and add all the links found
		while ( (line = httpReader.readLine()) !=null) {
			links.addAll(getLinks(line)); 
		}
		
		return getGoodUrls(links); 
	}
	
	private ArrayList<String> getLinks(String aLine) {
		
		//Regular expression for any number of HTML attributes (to deal with cases like <a class="myClass" href="www.google.com">: 
		
		//String attributes = "(?:\\w+\\s*=\\s*[\"\']\\w*[\"\']\\s+)*";
		
		String attributes = "(?:[^>]+\\s+)*";  //Allow any number of characters not equal to '>' followed by some whitespace 
												//This is a super-general way of dealing with attributes before 'href'
		
		//String attributes = "";
		//Regular expression to find the URL inside a link tag: <a href="(group)">
		
		Pattern regex = Pattern.compile("(?i:<\\s*a\\s+" + attributes + "href\\s*=\\s*[\"\']([^\'\"]+)[\"\'][^>]*>)");
		Matcher m = regex.matcher(aLine);
		
		ArrayList<String> linksFound = new ArrayList<String>(); 
		String urlStr;
		
		while (m.find()) {
			urlStr = m.group(1); 
			
			//If the URL doesn't start with '#'
			if (urlStr.charAt(0)!='#') 
				linksFound.add(m.group(1));	
			
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
				System.out.println("Found invalid URL '"+ e.getMessage() + "' when parsing web page at: " + curURL.toString());
			}
		}
			
		System.out.println("Num found: " + goodLinks.size());
		
		
		return goodLinks;
	}
	
	
	
	
	
}
