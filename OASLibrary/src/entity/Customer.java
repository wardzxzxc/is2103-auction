/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Edward
 */
@Entity
public class Customer implements Serializable {
    

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    @Column(nullable = false, length = 32, unique = true)
    private String username;
    @Column(nullable = false, length = 12)
    private String password;
    @Column(length = 32, nullable = false)
    private String firstName;
    @Column(length = 32, nullable = false)
    private String lastName;
    @Column(length = 32, nullable = false, unique = true)
    private String contactNumber;
    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal creditCurrBalance;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<CreditTransaction> creditTransactions;
    @OneToMany(mappedBy = "winner")
    private List<AuctionListing> auctionsWon;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Bid> bids;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Address> addresses;
    
    public Customer() {
        this.creditCurrBalance = new BigDecimal("0.0000");
        this.creditTransactions = new ArrayList<>();
        this.auctionsWon = new ArrayList<>();
        this.bids = new ArrayList<>();
        this.addresses = new ArrayList<>();
    }

    public Customer(String username, String password, String firstName, String lastName, String contactNumber) {
        this();
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
    }

    public List<CreditTransaction> getCreditTransactions() {
        return creditTransactions;
    }
    
    public void setCreditTransactions(List<CreditTransaction> creditTransactions) {
        this.creditTransactions = creditTransactions;
    }

    public List<AuctionListing> getAuctionsWon() {
        return auctionsWon;
    }
    
    public void setAuctionsWon(List<AuctionListing> auctionsWon) {
        this.auctionsWon = auctionsWon;
    }

    public List<Bid> getBids() {
        return bids;
    }
    
    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    
    public BigDecimal getCreditCurrBalance() {
        return creditCurrBalance;
    }
    
    public void setCreditCurrBalance(BigDecimal creditCurrBalance) {
        this.creditCurrBalance = creditCurrBalance;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getContactNumber() {
        return contactNumber;
    }
    
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
     
        return true;
    }

    @Override
    public String toString() {
        return "entity.Customer[ id=" + customerId + " ]";
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the addresses
     */
    public List<Address> getAddresses() {
        return addresses;
    }

    /**
     * @param addresses the addresses to set
     */
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
    
}