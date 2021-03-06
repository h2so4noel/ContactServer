package contact.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBElement;

import contact.entity.Contact;
import contact.service.jpa.JpaContactDao;

/**
 * Contact resource handles many request using RESTful web services provided by JAX-RS.
 * Handles most of the services that met the requirements of contacts web service including GET, POST, PUT and DELETE.
 * @author Pawin Suthipornopas 5510546123
 */
@Singleton
@Path("/contacts") 
public class ContactResource {
	private ContactDao dao = DaoFactory.getInstance().getContactDao();
	private CacheControl cc = new CacheControl();
	/**
	 * Default constructor, leave blank since Jersey required.
	 */
	public ContactResource(){
		cc.setMaxAge(99999);
	}
	
	/**
	 * GET method to return contact corresponding to the given title.
	 * @param str is the string the user quering in.
	 * @return Response to the user of type OK.
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response returnTitle(@QueryParam("title") String str){
		GenericEntity<List<Contact>> ent = new GenericEntity<List<Contact>>(dao.findAll()){};
		if(str == null)
			return Response.ok(ent).build();
		else
			return Response.ok(dao.findByTitle(str)).build();
	}
	
	/**
	 * GET method to return contact corresponding to the given id.
	 * @param id is the given integer of id.
	 * @return Response to the user of type OK.
	 */
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_XML)
	public Response returnContact(@PathParam("id") int id,@Context Request req){
		if(dao.find(id) == null)
			return Response.noContent().build();
		Response.ResponseBuilder rb = null;
		EntityTag etag = new EntityTag(dao.find(id).hashCode()+"");
		rb = req.evaluatePreconditions(etag);
		if(rb != null)
			return rb.cacheControl(cc).build();
		return Response.ok(dao.find(id)).cacheControl(cc).tag(etag).build();
	}
	
	/**
	 * POST method, given an XML type of request and create a contact with the information given.
	 * @param cont is JAXBElement contact, automatically convert XML element to Contact class.
	 * @return Response type of creating contact.
	 * @throws Exception in case of any number errors.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	public Response createContact(JAXBElement<Contact> cont) throws Exception{
		Contact contact = (Contact)cont.getValue();
		EntityTag etag = new EntityTag(contact.hashCode()+"");
		if(dao.find(contact.getId()) == null){
			System.out.println("Contact Created");
			dao.save(contact);
			return Response.created(new URI(contact.getId()+"")).type(MediaType.APPLICATION_XML).entity(contact).cacheControl(cc).tag(etag).build();
		}
		else
			return Response.status(Response.Status.CONFLICT).build();
	}
	
	/**
	 * PUT method, update the content by given XML type of request. Find that contact and update it with XML elements
	 * @param id is id given.
	 * @param cont is JAXBElement automatically convert from XML element to Contact class.
	 * @return OK type of response if the file is update successfully. Otherwise, indicate the user that there is no such content existed.
	 */
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_XML)
	public Response updateContact(@PathParam("id") int id, JAXBElement<Contact> cont, @Context Request req){
		if(dao.find(id) == null)
			return Response.status(Status.BAD_REQUEST).build();
		EntityTag etag = new EntityTag(dao.find(id).hashCode()+"");
		Response.ResponseBuilder rb = req.evaluatePreconditions(etag);
		if(rb != null)
			return rb.cacheControl(cc).tag(etag).build();
		Contact contact = (Contact)cont.getValue();
		dao.update(contact);
		return Response.ok().build();			
	}
	
	/**
	 * DELETE method, delete the content from the service by the given id.
	 * @param id is the id given to delete contact.
	 * @return OK type of response to indicate the deletion.
	 */
	@DELETE
	@Path("{id}")
	public Response delete(@PathParam("id") int id, @Context Request req){
		if(dao.find(id) != null){
			Response.ResponseBuilder rb = null;
			EntityTag etag = new EntityTag(dao.find(id).hashCode()+"");
			rb = req.evaluatePreconditions(etag);
			if(rb != null)
				return rb.cacheControl(cc).build();
		}
		dao.delete(id);
		return Response.ok().entity(id + " Deleted.").build();
	}
}
