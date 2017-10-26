package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.CustomerExistException;
import util.exception.CustomerNotFoundException;
import util.exception.GeneralException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author Cloud
 */
@Stateless
@Local(CustomerControllerLocal.class)
@Remote(CustomerControllerRemote.class)

public class CustomerController implements CustomerControllerRemote, CustomerControllerLocal {

    @PersistenceContext(unitName = "OnlineAuctionSystem_OAS_-ejbPU")
    private EntityManager em;

    @Override
    public Customer createNewCustomer(Customer customer) throws CustomerExistException, GeneralException {
        
        try 
        { 
            em.persist(customer);
            em.flush();
            em.refresh(customer);
            
            return customer;
        }
        catch (PersistenceException ex) {
            if(ex.getCause() != null && 
                    ex.getCause().getCause() != null &&
                    ex.getCause().getCause().getClass().getSimpleName().equals("MySQLIntegrityConstraintViolationException")) 
                    { 
                        throw new CustomerExistException("Customer already exists");
                    }
            else {
                throw new GeneralException("An unexpected error has occured:" + ex.getMessage());
            }
        }
        
    }
    
    @Override
    public Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException {
        
        Query query = em.createQuery("SELECT c from Customer c WHERE c.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try {
            return (Customer)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer Username " + username + "does not exist");
        }
        
    }
    
    @Override
    public Customer customerLogin(String username, String password) throws InvalidLoginCredentialException {
        
        try {
            Customer customer = retrieveCustomerByUsername(username);
            
            if (customer.getPassword().equals(password)) {
                return customer;
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password");
            }
        }
        catch (CustomerNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password");
        }
        
    }
    
    @Override
    public void updateCustomer(Customer customer) {
        
        em.merge(customer);
        
    }
    
}