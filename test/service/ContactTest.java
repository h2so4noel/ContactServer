package service;

import static org.junit.Assert.*;

import java.net.URI;

import javax.ws.rs.core.Response.Status;

import jetty.JettyMain;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpResponse;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.Uri;
import org.junit.*;

import contact.entity.Contact;


public class ContactTest {
	
	int port = 8080;
	Server server;
	String url;
	HttpClient client = new HttpClient();
	
	@Before
	public void beforeTest() throws Exception{
		//Start Jetty
		JettyMain.startServer(8080);
		server = new Server(8080);
		
		//Setup Clients
		client.start();
		
		//Setup URL
		url = "http://localhost:8080/contacts/";
	}
	
	@After
	public void afterTest() throws Exception {
		//Stop Jetty
		System.out.println("Stop Jetty");
		JettyMain.stopServer();
		server.stop();
		client.stop();
	}

	@Test
	public void testGet1() throws Exception {
		ContentResponse r = client.GET(url);
		System.out.println(r.getStatus());
		assertEquals(Status.OK.getStatusCode(), r.getStatus());
	}
	
	@Test
	public void testGet2() throws Exception {
		ContentResponse r = client.GET(url+10000);
		System.out.println(r.getStatus());
		assertEquals(Status.OK.getStatusCode(), r.getStatus());
	}
	
	@Test
	public void testPost1() throws Exception {
		StringContentProvider contact = new StringContentProvider("<contact id=\"9595\">" +
																"<title>test title</title>" +
																"<name>contact'sadasdasdme</name>" +
																"<email>contact mail address</email>" +
																"<phoneNumber>contact's telephone number</phoneNumber>"+
																"</contact>");
		ContentResponse r = client.newRequest(url).content(contact, "application/xml").method(HttpMethod.POST).send();
		assertEquals(Status.CREATED.getStatusCode(), r.getStatus());
		//Delete for further test
		Request req = client.newRequest(url+9595);
		req = req.method(HttpMethod.DELETE);
		r = req.send();
	}
	
	@Test
	public void testPost2() throws Exception {
		// id = 9999 already exist.
		StringContentProvider contact = new StringContentProvider("<contact id=\"9999\">" +
																"<title>test title</title>" +
																"<name>contact'sadasdasdme</name>" +
																"<email>contact mail address</email>" +
																"<phoneNumber>contact's telephone number</phoneNumber>"+
																"</contact>");
		ContentResponse r = client.newRequest(url).content(contact, "application/xml").method(HttpMethod.POST).send();
		assertEquals(Status.CREATED.getStatusCode(), r.getStatus());
	}
	
	@Test
	public void testPut1() throws Exception {
		Request r = client.newRequest(url+1234);
		r = r.method(HttpMethod.PUT);
		StringContentProvider content = new StringContentProvider("<contact id=\"1234\">" +
																"<title>test title</title>" +
																"<name>contact'sadasdasdme</name>" +
																"<email>contact mail address</email>" +
																"<phoneNumber>contact's telephone number</phoneNumber>"+
																"</contact>");
		r = r.content(content, "application/xml");
		ContentResponse res = r.send();
		assertEquals(Status.OK.getStatusCode(), res.getStatus());
	}
	
	@Test
	public void testPut2() throws Exception {
		Request r = client.newRequest(url+1239);
		r = r.method(HttpMethod.PUT);
		StringContentProvider content = new StringContentProvider("<contact id=\"1239\">" +
																"<title>test title</title>" +
																"<name>contact'sadasdasdme</name>" +
																"<email>contact mail address</email>" +
																"<phoneNumber>contact's telephone number</phoneNumber>"+
																"</contact>");
		r = r.content(content, "application/xml");
		ContentResponse res = r.send();
		assertEquals(Status.OK.getStatusCode(), res.getStatus());
	}
	
	@Test
	public void testDelete1() throws Exception {
		//Add contact to test delete
		StringContentProvider contact = new StringContentProvider("<contact id=\"9595\">" +
																"<title>test title</title>" +
																"<name>contact'sadasdasdme</name>" +
																"<email>contact mail address</email>" +
																"<phoneNumber>contact's telephone number</phoneNumber>"+
																"</contact>");
		ContentResponse r = client.newRequest(url).content(contact, "application/xml").method(HttpMethod.POST).send();
		Request req = client.newRequest(url+9595);
		req = req.method(HttpMethod.DELETE);
		r = req.send();
		assertEquals(Status.OK.getStatusCode(), r.getStatus());
		}
	
	@Test
	public void testDelete2() throws Exception {
		//Test here failed because of no path.
		Request req = client.newRequest(url);
		req = req.method(HttpMethod.DELETE);
		ContentResponse r = req.send();
		assertEquals(Status.OK.getStatusCode(), r.getStatus());
	}
}
