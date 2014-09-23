package service;

import static org.junit.Assert.*;

import java.net.URI;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpResponse;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.Uri;
import org.junit.*;

import contact.entity.Contact;


public class ContactTest {
	
	int port = 8080;
	Server server = new Server( port );
	URI uri;
	HttpClient client = new HttpClient();
	
	Contact contact1 = new Contact();
	Contact contact2 = new Contact();
	Contact contact3 = new Contact();
	
	@Before
	public void beforeTest() throws Exception{
		//Start Jetty
		ServletContextHandler context = new ServletContextHandler( ServletContextHandler.SESSIONS );
		context.setContextPath("/");
		ServletHolder holder = new ServletHolder( org.glassfish.jersey.servlet.ServletContainer.class );
		holder.setInitParameter(ServerProperties.PROVIDER_PACKAGES, "src/contact.entity");
		context.addServlet( holder, "/*" );
		server.setHandler( context );
		System.out.println("Starting Jetty server on port " + port);
		server.start();
		
		//Setup Clients
		client.start();
		
		//Setup URI
		uri = new URI("http://localhost:8080/contacts");
	}
	
	@After
	public void afterTest() throws Exception {
		//Stop Jetty
		System.out.println("Stop Jetty");
		server.stop();
		client.stop();
	}

	@Test
	public void testGet1() throws Exception {
		ContentResponse r = client.GET(uri);
		assertEquals(200, r.getStatus());
	}

}
