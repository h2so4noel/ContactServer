package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Contact data access object that take cares of data accessing for Contact class.
 * Has its own list of contacts to access.
 * @author Pawin Suthipornopas 5510546123
 */
public class ContactDAO {
	private List<Contact> contacts = new ArrayList<Contact>();
	private int uniqueID = 1;
	/**
	 * Default constructor. Leave blank.
	 */
	public ContactDAO(){
	}
	
	/**
	 * Find the contact from the given id.
	 * @param id is the id of that contact
	 * @return the contact that was found.
	 */
	public Contact find(int id){
		return contacts.get(id);
	}
	
	/**
	 * Return every contacts available in List form.
	 * @return every contacts.
	 */
	public List<Contact> findAll(){
		return contacts;
	}
	
	/**
	 * Create a freshly new contact with unique ID.
	 */
	public void create(){
		Contact contact = new Contact();
		contact.setId(uniqueID);
		uniqueID++;
		
		contacts.add(contact);
	}
	
	/**
	 * Create a new contact from the given contact elements and information.
	 * @param id is id of this contact.
	 * @param title is title of this contact.
	 * @param name is name of this contact.
	 * @param email is email of this contact.
	 * @param phoneNumber is Phone Number of this contact.
	 * @return the id of this contact to indicate confirmation.
	 */
	public int createContact(int id, String title, String name, String email, int phoneNumber){
		Contact contact = new Contact();
		if(id == 0)
			id = uniqueID++;
		contact.setId(id);
		contact.setTitle(title);
		contact.setName(name);
		contact.setEmail(email);
		contact.setPhoneNumber(phoneNumber);
		
		contacts.add(contact);
		return id;
	}
	
	/**
	 * Save this contact to the list, and notify.
	 * @param contact is the contact to be saved.
	 * @return boolean expression to indicate saving process.
	 */
	public boolean save(Contact contact){
		return contacts.add(contact);
	}
	
	/**
	 * Delete the given contact id away from the list.
	 * @param id is the id of the contact that want to delete.
	 * @return boolean expression indicating the deleting process.
	 */
	public boolean delete(int id){
		for(int i = 0; i < contacts.size(); i++){
			if(id == contacts.get(i).getId()){
				contacts.remove(i);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Find the contact from the given title, and then return it.
	 * @param str is the title of that contact.
	 * @return the contact if it exists, otherwise return null.
	 */
	public Contact findTitle(String str){
		for(int i = 0; i < contacts.size(); i++){
			if(str.equals(contacts.get(i).getTitle())){
				return contacts.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Update existing contact if it exists, otherwise do nothing.
	 * @param contact is the contact to be updated.
	 * @return the id of the updated contact, otherwise return 0.
	 */
	public int updateContact(Contact contact){
		for(int i = 0; i < contacts.size(); i++){
			if(contacts.get(i).getId() == contact.getId()){
				contacts.get(contact.getId()).setTitle(contact.getTitle());
				contacts.get(contact.getId()).setName(contact.getName());
				contacts.get(contact.getId()).setEmail(contact.getEmail());
				contacts.get(contact.getId()).setPhoneNumber(contact.getPhoneNumber());
				return contact.getId();
			}
		}
		return 0;
	}
}
