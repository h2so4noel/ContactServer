package service;

import static org.junit.Assert.*;

import javax.ws.rs.core.Response.Status;

import jetty.JettyMain;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Server;
import org.junit.*;

import contact.service.DaoFactory;

/**
 * JUnit test to test the Contact server in various request.
 * Send the request to the service by sending GET, PUT, POST and DELETE. Two times for each test.
 * @author Pawin Suthipornopas 5510546123
 *
 */

public class ContactTest {
	
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
//JIM: added because your tests were failing.
// In fact, the tests should not depend or assume a particular implementation.
		DaoFactory.setFactory( new contact.service.mem.MemDaoFactory() );
		//Start Jetty
		JettyMain.startServer(8080);
		server = new Server(8080);
		
		//Setup Clients
		client.start();
//BAD: you are *assuming* the URL.  Better to get it from the server.

		//Setup URL
		url = "http://localhost:8080/contacts/";
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
	 * First GET test method.
	 * Simply request GET without any path or parameters.
	 * @throws Exception
	 */
	@Test
	public void testGet1() throws Exception {
		ContentResponse r = client.GET(url);
		System.out.println(r.getStatus());
		assertEquals(Status.OK.getStatusCode(), r.getStatus());
	}
	
	/**
	 * Second GET test method.
	 * Request it along with 10000 ID path. Should return error.
	 * @throws Exception
	 */
	@Test
	public void testGet2() throws Exception {
//POOR: assuming this id is not in service
		ContentResponse r = client.GET(url+10000);
		System.out.println(r.getStatus());
		assertEquals(204, r.getStatus());
	}
	
	/**
	 * First POST test method.
	 * Create a new Contact using StringContentProvider and send in as XML Application.
	 * Should return a CREATED response code.
	 * @throws Exception
	 */
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
//POOR: again assuming the id of contact
		Request req = client.newRequest(url+9595);
		req = req.method(HttpMethod.DELETE);
		r = req.send();
	}
	
	/**
	 * Second POST test method.
	 * Send in a new Contact the same way as previous, but there is already id = 9999 in the database.
	 * So this one should return a false response.
	 * @throws Exception
	 */
	@Test
	public void testPost2() throws Exception {
//POOR: don't assume this id exists
		// id = 9999 already exist.
		StringContentProvider contact = new StringContentProvider("<contact id=\"9999\">" +
																"<title>test title</title>" +
																"<name>contact'sadasdasdme</name>" +
																"<email>contact mail address</email>" +
																"<phoneNumber>contact's telephone number</phoneNumber>"+
																"</contact>");
		ContentResponse r = client.newRequest(url).content(contact, "application/xml").method(HttpMethod.POST).send();
		assertEquals(409, r.getStatus());
	}
	
	/**
	 * First PUT test method
	 * Try editing current id = 1234.
	 * Should return OK since 1234 exists.
	 * @throws Exception
	 */
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

	/**
	 * Second PUT test method.
	 * Same as previous method, but the id 1239 doesn't exist so this one should get error response.
	 * @throws Exception
	 */
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
		assertEquals(400, res.getStatus());
	}
	
	/**
	 * First DELETE test method.
	 * Add new contact and delete it out using the indicated id.
	 * Should return OK to confirm the deletion.
	 * @throws Exception
	 */
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
	
	/**
	 * Second DELETE test method.
	 * Simply try to delete something without a correct path.
	 * Should return 405 response.
	 * @throws Exception
	 */
	@Test
	public void testDelete2() throws Exception {
		//Test here failed because of no path.
		Request req = client.newRequest(url);
		req = req.method(HttpMethod.DELETE);
		ContentResponse r = req.send();
		assertEquals(405, r.getStatus());
	}
}
