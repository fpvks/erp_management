package erp_management.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import erp_management.dto.Department;
import erp_management.dto.Employee;
import erp_management.dto.Title;
import erp_management.jdbc.ConnectionProvider;
import erp_management.jdbc.LogUtil;
import erp_management.jdbc.MySQLJdbcUtil;

public class EmployeeDaoImpl implements EmployeeDao {

	@Override
	public List<Employee> selectEmployeeByAll() throws SQLException {
		LogUtil.prnLog("selectEmployeeByAll()");
		String sql = "select empno, empname, emptitle, titlename, salary, gender, empdept, deptname, floor, joindate"
				+ " from employee as e join title as t on e.emptitle = t.titleno join department as d on e.empdept = d.deptno";
		List<Employee> list = new ArrayList<>();

		try (Connection conn = MySQLJdbcUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			LogUtil.prnLog(pstmt);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					list.add(getEmployee(rs));
				}
			}
		}

		return list;
	}

	private Employee getEmployee(ResultSet rs) throws SQLException {
		String empNo = rs.getString("empno");
		String empName = rs.getString("empName");
		Title title = new Title(rs.getString("emptitle"), rs.getString("titlename"));
		int salary = rs.getInt("salary");
		String gender = rs.getString("gender");
		Department department = new Department(rs.getString("empdept"), rs.getString("deptname"), rs.getInt("floor"));
		Date date = rs.getDate("joindate");
		return new Employee(empNo, empName, title, salary, gender, department, date);
	}

	@Override
	public int insertEmployee(Employee emp) throws SQLException {
		LogUtil.prnLog("insertEmployee()");
		String sql = "insert into employee values(?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = MySQLJdbcUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, emp.getEmpNo());
			pstmt.setString(2, emp.getEmpName());
			pstmt.setString(3, emp.getTitle().getTitleNo());
			pstmt.setInt(4, emp.getSalary());
			pstmt.setString(5, emp.getGender());
			pstmt.setString(6, emp.getDepartment().getDeptNo());
			pstmt.setDate(7, new java.sql.Date(emp.getDate().getTime()));
			LogUtil.prnLog(pstmt);

			return pstmt.executeUpdate();
		}
	}

	@Override
	public int deleteEmployee(Employee emp) throws SQLException {
		LogUtil.prnLog("deleteEmployee()");
		String sql = "delete from employee where empno = ?";

		try (Connection conn = MySQLJdbcUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, emp.getEmpNo());
			LogUtil.prnLog(pstmt);

			return pstmt.executeUpdate();
		}
	}

	@Override
	public int updateEmployee(Employee emp) throws SQLException {
		LogUtil.prnLog("updateEmployee()");
		String sql = "update employee set empname = ?, emptitle = ?, salary = ?, gender = ?, empdept = ?, joindate = ? where empno = ?";

		try (Connection conn = MySQLJdbcUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(7, emp.getEmpNo());
			pstmt.setString(1, emp.getEmpName());
			pstmt.setString(2, emp.getTitle().getTitleNo());
			pstmt.setInt(3, emp.getSalary());
			pstmt.setString(4, emp.getGender());
			pstmt.setString(5, emp.getDepartment().getDeptNo());
			pstmt.setDate(6, new java.sql.Date(emp.getDate().getTime()));
			LogUtil.prnLog(pstmt);

			return pstmt.executeUpdate();
		}
	}

	@Override
	public String nextEmployeeNo() {
		String currentDate = LocalDate.now().getYear() + "";
		String sql = "select max(empno) as nextno from employee";
		String nextStr = null;
		try(Connection conn = ConnectionProvider.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()){
			LogUtil.prnLog(pstmt);
			if(rs.next()) {
				nextStr = String.format("E%3s%03d", (String) currentDate.substring(1), Integer.parseInt(rs.getString("nextno").substring(4)) + 1);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return nextStr;
	}
}
