package no.hvl.dat110.aciotdevice.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import com.google.gson.Gson;

public class RestClient {
	
	public RestClient() {
		// TODO Auto-generated constructor stub
	}

	private static String logpath = "/accessdevice/log";

	public void doPostAccessEntry(String message) {

		// TODO: implement a HTTP POST on the service to post the message
		
		AccessMessage newMessage = new AccessMessage(message);
		
		try (Socket s = new Socket(Configuration.host, Configuration.port)) {
			
			Gson gson = new Gson();
			
			String jsonbody = gson.toJson(newMessage);

			// construct the POST request
			String httppostrequest = 
					"POST " + logpath + " HTTP/1.1\r\n" + 
			        "Host: " + Configuration.host + "\r\n" +
					"Content-type: application/json\r\n" + 
			        "Content-length: " + jsonbody.length() + "\r\n" +
					"Connection: close\r\n" + 
			        "\r\n" + 
					jsonbody + 
					"\r\n";

			// sent the HTTP request
			OutputStream output = s.getOutputStream();

			PrintWriter pw = new PrintWriter(output, false);

			pw.print(httppostrequest);
			pw.flush();
			
			// read the HTTP response
			InputStream in = s.getInputStream();

			Scanner scan = new Scanner(in);
			StringBuilder jsonresponse = new StringBuilder();
			boolean header = true;

			while (scan.hasNext()) {

				String nextline = scan.nextLine();

				if (header) {
					System.out.println(nextline);
				} else {
					jsonresponse.append(nextline);
				}

				// simplified approach to identifying start of body: the empty line
				if (nextline.isEmpty()) {
					header = false;
				}

			}
			System.out.println(jsonresponse);

		} catch (IOException ex) {
			System.err.println(ex);
		}
		
	}
	
	private static String codepath = "/accessdevice/code";
	
	public AccessCode doGetAccessCode() {

		AccessCode code = new AccessCode();
		int[] newCode = new int[2];
		
		// TODO: implement a HTTP GET on the service to get current access code
		
		try (Socket s = new Socket(Configuration.host, Configuration.port)) {

			// construct the GET request
			String httpgetrequest = "GET " + codepath + " HTTP/1.1\r\n" + "Accept: application/json\r\n"
					+ "Host: " + Configuration.host + "\r\n" + "Connection: close\r\n" + "\r\n";

			// sent the HTTP request
			OutputStream output = s.getOutputStream();

			PrintWriter pw = new PrintWriter(output, false);

			pw.print(httpgetrequest);
			pw.flush();

			// read the HTTP response
			InputStream in = s.getInputStream();

			Scanner scan = new Scanner(in);
			StringBuilder jsonresponse = new StringBuilder();
			boolean header = true;

			while (scan.hasNext()) {

				String nextline = scan.nextLine();

				if (header) {
					System.out.println(nextline);
				} else {
					jsonresponse.append(nextline);
				}

				// simplified approach to identifying start of body: the empty line
				if (nextline.isEmpty()) {
					header = false;
				}

			}
			System.out.println(jsonresponse);
		    newCode[0] = Character.getNumericValue(jsonresponse.charAt(15));
		    newCode[1] = Character.getNumericValue(jsonresponse.charAt(17));
			scan.close();

		} catch (IOException ex) {
			System.err.println(ex);
		}
		
		code.setAccesscode(newCode);
		return code;
	}
	
	
}
