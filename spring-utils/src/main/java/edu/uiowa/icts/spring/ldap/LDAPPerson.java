package edu.uiowa.icts.spring.ldap;

import java.util.Arrays;


public class LDAPPerson {

	private String fullName;
	private String lastName;
	private String description;
	private String[] roleNames;
	private String mail;
	private String givenName;
	private String displayName;
	private String dn;
	private String telephoneNumber;
	private String title;
	private String company;
	private String department;
	private String username;
	
	
	
	@Override
	public String toString() {
		return "Person [company=" + company + ", department=" + department
				+ ", description=" + description + ", displayName="
				+ displayName + ", dn=" + dn + ", fullName=" + fullName
				+ ", givenName=" + givenName + ", lastName=" + lastName
				+ ", mail=" + mail + ", roleNames="
				+ Arrays.toString(roleNames) + ", telephoneNumber="
				+ telephoneNumber + ", title=" + title + "]";
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String[] getRoleNames() {
		return roleNames;
	}
	public void setRoleNames(String[] roleNames) {
		this.roleNames = roleNames;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getGivenName() {
		return givenName;
	}
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getDn() {
		return dn;
	}
	public void setDn(String dn) {
		this.dn = dn;
	}
	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	

}
