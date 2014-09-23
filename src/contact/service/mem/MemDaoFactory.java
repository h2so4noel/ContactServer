package contact.service.mem;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlRootElement;

import contact.entity.Contact;
import contact.entity.ContactList;
import contact.service.*;
import contact.service.mem.MemContactDao;

/**
 * MemDaoFactory is a factory for getting instances of entity DAO object
 * that use memory-based persistence, which isn't really persistence at all!
 * 
 * @see contact.service.DaoFactory
 * @version 2014.09.19
 * @author jim
 */

public class MemDaoFactory extends DaoFactory {
	/** instance of the entity DAO */
	private ContactDao daoInstance;
	
	public MemDaoFactory() {
		daoInstance = new MemContactDao();
		loadXmlFile();
	}
	
	/**
	 * Load old XML file and read its data.
	 */
	private void loadXmlFile(){
		String path = "C://Java//eclipse_proj//ContactServer//contact.xml";
		File file = new File(path);
		try {
			JAXBContext context = JAXBContext.newInstance(ContactList.class);
			Unmarshaller um = context.createUnmarshaller();
			ContactList list = (ContactList)um.unmarshal(file);
			if(list!=null) {
				List<Contact> contacts = list.getContacts();
				if(contacts!=null) {
					for(Contact c: contacts) {
						daoInstance.save(c);
					}
				}
			}
		} catch (JAXBException je) {
			je.printStackTrace();
		}
	}
		
	
	@Override
	public ContactDao getContactDao() {
		return daoInstance;
	}
	
	@Override
	public void shutdown() {
		System.out.println("Shutdown Initiated");
		try {
			JAXBContext context = JAXBContext.newInstance(ContactList.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			ContactList list = new ContactList(daoInstance.findAll());
			FileOutputStream output = new FileOutputStream("C://Java//eclipse_proj//ContactServer//contact.xml");
			marshaller.marshal(list, output);
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}