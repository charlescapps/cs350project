package webcrawler;

import java.net.URL;
import java.net.URLEncoder;
import java.net.MalformedURLException;
import java.util.TreeSet;

public class TestConnection {

		public static void main(String[] args) throws MalformedURLException, Exception {
			
			WebPageParser wpp = new WebPageParser();
			
			wpp.setURL(new URL("http://www.spoofee.com"));
			TreeSet<URL> links = wpp.getLinks();
			
			for (URL u: links)
				System.out.println(u);
			
			
		}
}
