package service;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.Response.Status;

import jetty.JettyMain;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Server;
import org.junit.*;

import contact.entity.Contact;
import contact.service.DaoFactory;

/**
 * JUnit test to test the Contact server specifically using of ETag.
 * Test by using ETag with GET, POST and PUT
 * @author Pawin Suthipornopas 5510546123
 *
 */

public class EtagTest {
	
	int port = 8080;
	Server server;
	String url;
	HttpClient client = new HttpClient();
	
	/**
	 * This method starts the server and setup Clients and URL to prepare for the test.
	 * URL is local on port 8080.
	 * @throws Exception in case any error happens.
	 */
	@Before
	public void beforeTest() throws Exception{
		//Start Jetty
		JettyMain.startServer(8080);
		server = new Server(8080);
		
		//Setup Clients
		client.start();
		
		//Setup URL
		url = "http://localhost:8080/contacts/";
		
		//Add temp contact
		StringContentProvider contact = new StringContentProvider("<contact id=\"1234\">" +
				"<title>test title</title>" +
				"<name>contact'sadasdasdme</name>" +
				"<email>contact mail address</email>" +
				"<phoneNumber>contact's telephone number</phoneNumber>"+
				"</contact>");
		ContentResponse r = client.newRequest(url).content(contact, "application/xml").method(HttpMethod.POST).send();
	}
	
	/**
	 * Shutdown the server after the test, and close the client.
	 * @throws Exception
	 */
	@After
	public void afterTest() throws Exception {
		//Stop Jetty
		System.out.println("Stop Jetty");
		JettyMain.stopServer();
		server.stop();
		client.stop();
	}
	
	/**
	 * GET Etag Test.
	 * Check the availability of ETag from the method, whether we get the ETag or not.
	 * @throws Exception
	 */
	@Test
	public void testGetETag1() throws Exception{
		Contact contact = DaoFactory.getInstance().getContactDao().find(1234);
		ContentResponse r = client.GET(url+1234);
		assertEquals("\"" + contact.hashCode() + "\"", r.getHeaders().get(HttpHeader.ETAG));
	}
	
	/**
	 * Another GET Etag Test.
	 * This time get a new contact from the server, but the contact didn't update.
	 * @throws Exception
	 */
	@Test
	public void testGetETag2() throws Exception{
		ContentResponse r = client.GET(url+1234);
		String etag = r.getHeaders().get(HttpHeader.ETAG);
		r = client.newRequest(url+1234).method(HttpMethod.GET).header(HttpHeader.IF_NONE_MATCH, etag).send();
		assertEquals(304, r.getStatus());
	}
	
	/**
	 * POST ETag Test.
	 * Just check the availability of ETag using POST
	 * @throws Exception
	 */
	@Test
	public void testPostETag() throws Exception{
		//Delete what's left of the last test
		client.newRequest(url+9595).method(HttpMethod.DELETE).send();
		
		StringContentProvider contact = new StringContentProvider("<contact id=\"9595\">" +
				"<title>test title</title>" +
				"<name>contact'sadasdasdme</name>" +
				"<email>contact mail address</email>" +
				"<phoneNumber>contact's telephone number</phoneNumber>"+
				"</contact>");
		ContentResponse r = client.newRequest(url).content(contact, "application/xml").method(HttpMethod.POST).send();
		assertEquals(Status.CREATED.getStatusCode(), r.getStatus());
		Contact contactNew = DaoFactory.getInstance().getContactDao().find(9595);
		assertEquals("\"" + contactNew.hashCode() + "\"", r.getHeaders().get(HttpHeader.ETAG));
	}
	
	/**
	 * PUT ETag Test.
	 * Updating contact, the result was an old ETag so the content didn't change.
	 * @throws Exception
	 */
	@Test
	public void testPutETag() throws Exception{
		ContentResponse r = client.GET(url+1234);
		String etag = r.getHeaders().get(HttpHeader.ETAG);
		StringContentProvider contact = new StringContentProvider("<contact id=\"1234\">"
				+"<title>some title here</title></contact>");
		r = client.newRequest(url+1234).content(contact, "application/xml").method(HttpMethod.PUT).send();
		r = client.newRequest(url+1234).header(HttpHeader.IF_NONE_MATCH, etag).method(HttpMethod.GET).send();
		assertEquals(304, r.getStatus());
	}
	
	/**
	 * Test if DELETE with ETag was a success.
	 * @throws Exception
	 */
	@Test
	public void testDeleteETag() throws Exception{
		StringContentProvider contact = new StringContentProvider("<contact id=\"1235\">" +
				"<title>test title</title>" +
				"<name>contact'sadasdasdme</name>" +
				"<email>contact mail address</email>" +
				"<phoneNumber>contact's telephone number</phoneNumber>"+
				"</contact>");
		ContentResponse r = client.newRequest(url).content(contact, "application/xml").method(HttpMethod.POST).send();
		
		r = client.GET(url+1235);
		String etag = r.getHeaders().get(HttpHeader.ETAG);
		StringContentProvider content = new StringContentProvider("<contact id=\"1235\">"
				+"<title>some title here</title></contact>");
		client.newRequest(url+1235).content(content,"application/xml").method(HttpMethod.PUT).send();
		r = client.newRequest(url+1235).header(HttpHeader.IF_NONE_MATCH, etag).method(HttpMethod.DELETE).send();
		assertEquals(412, r.getStatus());
	}
}
