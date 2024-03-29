package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import util.exception.EmployeeExistException;
import util.exception.EmployeeNotFoundException;
import util.exception.GeneralException;
import util.exception.InvalidLoginCredentialException;
import util.exception.PasswordDoesNotMatchException;

/**
 *
 * @author Edward
 */
public interface EmployeeControllerRemote {
    
    public Employee createNewEmployee(Employee employee) throws EmployeeExistException, GeneralException;
    
    public Employee retrieveEmployeeByEmployeeId(Long employeeId) throws EmployeeNotFoundException;
    
    public List<Employee> retrieveAllEmployees();
    
    public Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;
    
    public Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException;

    public void deleteEmployee(Long employeeId) throws EmployeeNotFoundException;
    
    public void updateEmployee(Employee employee);

}
