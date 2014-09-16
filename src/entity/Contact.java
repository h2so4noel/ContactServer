package entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Contact class to store requested elements in the web service.
 * @author Pawin Suthipornopas 5510546123
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Contact {
	
	@XmlAttribute
	private int id;
	private String title;
	private String name;
	private String email;
	private int phoneNumber;
	
	/**
	 * Blank constructor.
	 */
	public Contact(){
	}
	
	/**
	 * Below are getters and setters for this class attributes.
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
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
