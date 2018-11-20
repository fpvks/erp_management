package erp_management.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp_management.dto.Department;
import erp_management.jdbc.LogUtil;
import erp_management.jdbc.MySQLJdbcUtil;

public class DepartmentDaoImpl implements DepartmentDao {

	@Override
	public List<Department> selectDepartmentByAll() {
		List<Department> list = new ArrayList<>();
		String sql = "select deptno, deptname, floor from department";

		try (Connection conn = MySQLJdbcUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			LogUtil.prnLog(pstmt);
			while (rs.next()) {
				list.add(getDepartment(rs));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		return list;
	}

	private Department getDepartment(ResultSet rs) throws SQLException {
		String deptNo = rs.getString("deptno");
		String deptName = rs.getString("deptname");
		int floor = rs.getInt("floor");

		return new Department(deptNo, deptName, floor);
	}

	@Override
	public int insertDepartment(Department department) throws SQLException {
		LogUtil.prnLog("insertDepartment()");
		String sql = "insert into department values(?, ?, ?)";
		int rowAffected = 0;

		try (Connection conn = MySQLJdbcUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, department.getDeptNo());
			pstmt.setString(2, department.getDeptName());
			pstmt.setInt(3, department.getFloor());
			LogUtil.prnLog(pstmt);
			rowAffected = pstmt.executeUpdate();
		}

		return rowAffected;
	}

	@Override
	public int deleteDepartment(Department department) throws SQLException {
		LogUtil.prnLog("deleteDepartment()");
		String sql = "delete from department where deptno = ?";
		int rowAffected = 0;

		try (Connection conn = MySQLJdbcUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setString(1, department.getDeptNo());
			LogUtil.prnLog(pstmt);
			rowAffected = pstmt.executeUpdate();
		}

		return rowAffected;
	}

	@Override
	public int updateDepartment(Department department) throws SQLException {
		LogUtil.prnLog("updateDepartment()");
		String sql = "update department set deptname = ?, floor = ? where deptno = ?";
		int rowAffected = 0;

		try (Connection conn = MySQLJdbcUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setString(3, department.getDeptNo());
			pstmt.setString(1, department.getDeptName());
			pstmt.setInt(2, department.getFloor());
			LogUtil.prnLog(pstmt);
			rowAffected = pstmt.executeUpdate();
		}

		return rowAffected;
	}

	@Override
	public Department selectDepartmentByNo(Department department) throws SQLException {
		LogUtil.prnLog("selectDepartmentByNo()");
		Department dept = null;
		String sql = "select deptno, deptname, floor from department where deptno = ?";

		try (Connection conn = MySQLJdbcUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, department.getDeptNo());
			LogUtil.prnLog(pstmt);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					dept = getDepartment(rs);
				}
			}
		}

		return dept;
	}

}