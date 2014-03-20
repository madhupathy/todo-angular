package todo;

import todo.persistence.DBConnPool;
import todo.persistence.remoteException;
import java.sql.*;
import java.util.LinkedList;

public class todo {
	private static String SelectString = "Select id, text, priority, duedate from todo ";
	
	private int Id;
	private String text;
	private Date DueDate;
	private int Priority;
	private long dueDateLong;
	
	public long getDueDateLong() {
		return dueDateLong;
	}
	public void setDueDateLong(long dueDateLong) {
		this.dueDateLong = dueDateLong;
	}
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getDueDate() {
		return DueDate;
	}
	public void setDueDate(Date dueDate) {
		DueDate = dueDate;
	}
	public int getPriority() {
		return Priority;
	}
	public void setPriority(int priority) {
		Priority = priority;
	}
	
	public boolean getTodo(int Id) throws remoteException {
		
		String Query = SelectString;
		Query += "Where id = " + Id;
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DBConnPool.getConnection();
			ps = con.prepareStatement(Query);
			ps.executeQuery();
			ResultSet rs = ps.getResultSet();
			while (rs.next()) {
				setResults(this, rs);
			}
		} catch (SQLException sqe) {
			throw new remoteException(sqe.getMessage());
		} finally {
			try {
				ps.close();
				DBConnPool.release(con);
			} catch (Exception e) {
				throw new remoteException(e.getMessage());
			}
		}
		return true;
	}
	public static LinkedList getAllTodos() throws remoteException {
		LinkedList<todo> list = new LinkedList();
		
		String Query = SelectString;
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DBConnPool.getConnection();
			ps = con.prepareStatement(Query);
			ps.executeQuery();
			ResultSet rs = ps.getResultSet();
			while (rs.next()) {
				todo t = new todo();
				setResults(t, rs);
				list.add(t);
			}
		} catch (SQLException sqe) {
			throw new remoteException(sqe.getMessage());
		} finally {
			try {
				ps.close();
				DBConnPool.release(con);
			} catch (Exception e) {
				throw new remoteException(e.getMessage());
			}
		}
		return list;
	}
	
	public static LinkedList getTodos(String q, String sort, boolean desc, 
			int limit, int offset) throws remoteException {
		
		LinkedList<todo> list = new LinkedList();
		
		String Query = SelectString;
		if (!q.equals("") && !q.equals("undefined")) {
			Query += "Where text like '" + q + "%' ";
		}
		if (!sort.equals("")) {
			Query += "order by " + sort + " ";			
			Query += (desc)?"desc":"";			
		}
		Query += " limit " + offset + ", " + limit;
		
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DBConnPool.getConnection();
			ps = con.prepareStatement(Query);
			ps.executeQuery();
			ResultSet rs = ps.getResultSet();
			while (rs.next()) {
				todo t = new todo();
				setResults(t, rs);
				list.add(t);
			}
		} catch (SQLException sqe) {
			throw new remoteException(sqe.getMessage());
		} finally {
			try {
				ps.close();
				DBConnPool.release(con);
			} catch (Exception e) {
				throw new remoteException(e.getMessage());
			}
		}
		return list;
	}
	
	public static boolean delete(int id) throws remoteException {
		
		String Query = "delete from todo ";
		Query += "where id = " + id;
		
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DBConnPool.getConnection();
			ps = con.prepareStatement(Query);
			ps.executeUpdate();
			ResultSet rs = ps.getResultSet();
		} catch (SQLException sqe) {
			throw new remoteException(sqe.getMessage());
		} finally {
			try {
				ps.close();
				DBConnPool.release(con);
			} catch (Exception e) {
				throw new remoteException(e.getMessage());
			}
		}
		return true;
	}
	public boolean save() throws remoteException {
		
		String Query = "insert into todo ";
		Query += "(text, priority, duedate) ";
		Query += "values('" + getText() + "' ," + getPriority() + ",'" + getDueDate() + "')"; 
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DBConnPool.getConnection();
			ps = con.prepareStatement(Query);
			ps.executeUpdate();
			ResultSet rs = ps.getResultSet();
		} catch (SQLException sqe) {
			throw new remoteException(sqe.getMessage());
		} finally {
			try {
				ps.close();
				DBConnPool.release(con);
			} catch (Exception e) {
				throw new remoteException(e.getMessage());
			}
		}
		return true;
	}	
	public static void setResults(todo cBean, ResultSet rs)
			throws SQLException {
		cBean.setId(rs.getInt(1));
		cBean.setText(rs.getString(2));
		cBean.setPriority(rs.getInt(3));
		cBean.setDueDate(rs.getDate(4));
		cBean.setDueDateLong(cBean.getDueDate().getTime());
	}
}
