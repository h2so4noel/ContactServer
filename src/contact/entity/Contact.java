package contact.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Contact class to store requested elements in the web service.
 * Contains Id, Title, Name, E-Mail and Phone Number of the contact.
 * @author Pawin Suthipornopas 5510546123
 */

@XmlRootElement(name="contact")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Contact {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@XmlAttribute
	private long id;
	private String title;
	private String name;
	private String email;
	private int phoneNumber;

	/**
	 * Blank constructor, just in case.
	 */
	public Contact(){
	}
	
	/**
	 * Constructor to create. Receives title, name and email.
	 */
	public Contact(String title, String name, String email){
		this.title = title;
		this.name = name;
		this.email = email;
	}
	
	/**
	 * Below are getters and setters for this class attributes.
	 */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
