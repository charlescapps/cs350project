package webcrawler;

import java.util.Comparator;
import java.net.URL;

public class URLHostComparator implements Comparator<URL> {
	
	public URLHostComparator() {
		
		
	}
	
	public int compare(URL u1, URL u2) {
		return u1.getHost().toLowerCase().compareTo(u2.getHost().toLowerCase());
	}
	
	public boolean equals(URL u1, URL u2) {
		return u1.getHost().toLowerCase().equals(u2.getHost().toLowerCase());
		
	}

}
