package contact.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Contact class to store requested elements in the web service.
 * This is stored in List and does not know each other well in contact.
 * @author Pawin Suthipornopas 5510546123
 */
@XmlRootElement(name="contacts")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContactList {
	
	@XmlElement(name="contact")
	private List<Contact> contacts = new ArrayList<Contact>();
	
	public ContactList(){
		this(null);
	}
	
	public ContactList(List<Contact> contacts){
		this.contacts = contacts;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	
	/**
	 * Add a new contact list, return true if success.
	 * @param contact is the contact to be added.
	 * @return true if success, false if not.
	 */
	public boolean add(Contact contact){
		return contacts.add(contact);
	}
}