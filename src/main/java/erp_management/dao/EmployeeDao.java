package erp_management.dao;

import java.sql.SQLException;
import java.util.List;

import erp_management.dto.Employee;

public interface EmployeeDao {
	List<Employee> selectEmployeeByAll() throws SQLException;

	int insertEmployee(Employee emp) throws SQLException;

	int deleteEmployee(Employee emp) throws SQLException;

	int updateEmployee(Employee emp) throws SQLException;
	
	String nextEmployeeNo();
}
