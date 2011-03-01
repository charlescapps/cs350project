package webcrawler;
import java.io.IOException;
import java.net.*;

public class VerifyAlgorithms{
	public static void main(String[] args) throws IOException {
		URL myURL = new URL("http://www.spoofee.com");
		URLConnection u = myURL.openConnection();
		System.out.println("DefaultUseCaches: " + u.getUseCaches());
		System.out.println("DefaultReadTimeout: " + u.getReadTimeout());
		System.out.println("DefaultConnectTimeout: " + u.getConnectTimeout());
	}
}
