package com.aws.boot.angular.rest.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
* @author Krishna Bhat
*
*/

@Entity(name = "address")
public class Address {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
 
    @Column(name = "street", nullable = false, length=40)
    private String street;
 
    @Column(name = "town", nullable = false, length=40)
    private String town;
 
    @Column(name = "county", nullable = false, length=40)
    private String county;

    @Column(name = "postcode", nullable = false, length=40)
    private String postcode;
    
	public Address(){}
	 
    public Address(String street, String town, String county, String postCode) {
       this.street = street;
       this.town = town;
       this.county = county;
       this.postcode = postCode;
    }

	public long getId() {
		return id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
    
}
