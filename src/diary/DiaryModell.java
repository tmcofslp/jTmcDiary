/**
 * 
 */
package diary;

import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.DateFormat;

import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import org.aspectj.runtime.internal.cflowstack.ThreadCounter;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;
/**
 * @author tmc
 *
 */
public class DiaryModell extends DefaultTableModel {
	/**
	 * 
	 */
	
	/*public class BinarySearch {
		  public static boolean contains(int[] a, int b) {
		    if (a.length == 0) {
		      return false;
		    }
		    int low = 0;
		    int high = a.length-1;

		    while(low <= high) {
		      int middle = (low+high) /2; 
		      if (b> a[middle]){
		        low = middle +1;
		      } else if (b< a[middle]){
		        high = middle -1;
		      } else { // The element has been found
		        return true; 
		      }
		    }
		    return false;
		  }
		} 
		*/
	
	final int SaveDatePoint= 100;
	private static final long serialVersionUID = 1L;
	//protected static String[][] _data = new String[50][50];
	protected static Vector<DiaryObject> _data;
	protected static String[] _column_names = {"Date", "Category", "Content"};
	protected static long lastUpdateInMs = 0;
	protected static updateThr _udthr;
	protected static String categoryFilter = "";
	protected static String wordFilter = "";
	
	public DiaryModell() {
		EraseCategoryFilter();
		EraseWordFilter();
	}
	public void SetCategoryFilter(String newFilter)
	{
		categoryFilter = newFilter;
	}

	public void SetWordFilter(String newFilter)
	{
		wordFilter = newFilter;
	}
	
	public String GetCategoryFilter()
	{
		return categoryFilter;
	}
	
	public String GetWordFilter()
	{
		return wordFilter;
	}
	
	public void EraseCategoryFilter()
	{
		SetCategoryFilter("");
	}
	
	public void EraseWordFilter() 
	{
		SetWordFilter("");
	}
	
	public String GetSelectSql()
    {
    	String ret = "select ID, Date, Category, Content from DiaryTable";
    	
    	if(GetCategoryFilter() != "" || GetWordFilter() != "") {
    		ret = ret + " where ";
       	
    		if(GetCategoryFilter() != "") {
    			ret = ret + "Category like '" + GetCategoryFilter() + "'";
    		
    			if (GetWordFilter() != "") {
    				ret = ret + " and ";
    			}
    	
    		}
    		
    		if(GetWordFilter() != "") {
    			ret = ret + "Content like '%" + GetWordFilter() + "%'";
    		}
    	
    	}
    	
    	ret = ret + " order by date(Date);";
    	
    	return ret;
    }

	class updateThr extends Thread  {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Iterator<DiaryObject> itr = _data.iterator();
			String strSqlTmp ="";
			boolean has_run = false;
			long rowsAffected;
			PreparedStatement pstmt;
			
			DiaryObject doTmp;
			while (!has_run) {
				if (Calendar.getInstance().getTimeInMillis() > lastUpdateInMs + SaveDatePoint){
					while(itr.hasNext()){
						doTmp = itr.next();
						if(doTmp.GetDirty()) {
								
							try {
								pstmt = _db.GetConnection().prepareStatement(
										"Update DiaryTable Set Date = ?, Category = ?, Content = ? where id = ?;");
								
								pstmt.setString(1, doTmp.getDateString());
								pstmt.setString(2, doTmp.getCategory());
								pstmt.setString(3, doTmp.getContent());
								pstmt.setString(4, doTmp.getID());
								rowsAffected = pstmt.executeUpdate();
								if (rowsAffected > 0) {
									System.out.println("Info: updated " + rowsAffected + " rows!");
									has_run = true;
								}
							} catch(SQLException ex) {
								System.err.println("error on update database");
								System.err.println("sql: " + strSqlTmp);
								ex.printStackTrace();
							}
							
						}
					}
		    	} else {
		    		try {
						TimeUnit.MILLISECONDS.sleep(SaveDatePoint);
						System.out.println("waiting " + SaveDatePoint + " milliseconds");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						System.err.println("error on waiting thread");
						e.printStackTrace();
					}
		    	}
			
			
			}
		
		}	
	}

	
	public void addRow(DiaryObject rowData)
	{
		addRow(rowData.GetRowObject());
	}
	@Override
	public void addRow(Object[] rowData) {
		// TODO Auto-generated method stub
		Statement stmt;
		try {
			stmt = _db.GetConnection().createStatement();
			int rowsAffected = stmt.executeUpdate("Insert into DiaryTable(ID, Date, Category, Content)" +
											"values ('" + rowData[0] + "','" + 
											rowData[1] + "','" + rowData[2] + "','" + 
											rowData[3] + "');");
			System.out.println("DiaryModell::addRow() " + rowsAffected + "rows affected");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("DiaryModell::addRow SQLException: ");
			e.printStackTrace();
			
		}
		
	
		//super.addRow(rowData);
	}
	@Override
	public void newRowsAdded(TableModelEvent e) {
		// TODO Auto-generated method stub
		super.newRowsAdded(e);
	}
	@Override
	public void removeRow(int row) {
		// TODO Auto-generated method stub
		Statement stmt;
		try {
			String id = _data.get(row).getID();
			
			stmt = _db.GetConnection().createStatement();
			
			int rowsAffected = stmt.executeUpdate("Delete from DiaryTable where ID like '" + id + "';");
			System.out.println("DiaryModell::addRow() " + rowsAffected + "rows affected");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("DiaryModell::addRow SQLException: ");
			e.printStackTrace();
			
		}
		

		//super.removeRow(row);
	}

	protected static DBController _db;
	static {
		try {
			_db =  DBController.getInstance();
			_data = new Vector<DiaryObject>();
			
		//	 System.out.println("dim(" + _data[2][2] + ")");
			 
		} catch (Exception e) {
			System.err.println("exception creating db connection");
			e.printStackTrace();
		}
	}

	
	//private String[] columnNames = ...//same as before...
		    //private Object[][] data = ...//same as before...
	public boolean ResetModell() 
	{
		boolean ret = false;
		
		try {
			Statement stmt = _db.GetConnection().createStatement();
		
			ResultSet rs = stmt.executeQuery(GetSelectSql());
			_data.clear();
			while(rs.next()) {
				_data.add(
							new DiaryObject(rs.getString("ID"), 
							rs.getString("Date"), 
							rs.getString("Category"), 
							rs.getString("Content")));
				
				
			}
			ret = true;
		} catch (Exception ex) {
			System.err.println("error resetting db backbone");
			ex.printStackTrace();
			throw new RuntimeException();
			
		}
		
		return ret;
	}
		    public int getColumnCount() {
		    	
		    	return 3;
		    		
		    }

		    public int getRowCount()
		    {
		    	return _data.size();
		    }
		    
		    public Object getValueAt(int row, int col) 
		    {
		    	switch (col) {
		    		case 0: return _data.get(row).getDateString(); 
		    		case 1: return _data.get(row).getCategory(); 
		    		case 2: return _data.get(row).getContent(); 
		    		default: return "";
		    	}
		    }

		    @Override
			public String getColumnName(int column) {
				// TODO Auto-generated method stub
				return _column_names[column];
			}
			@SuppressWarnings("unchecked")
			public Class getColumnClass(int c) {
		    	try {
		    		return getValueAt(0, c).getClass();
		    	} catch (Exception e) {
					 System.out.println("Error getting Column class for " + c + " from table");
					 e.printStackTrace();
					 return null;
				}
		    	
		    }

		    /*
		     * Don't need to implement this method unless your table's
		     * editable.
		     */
		    public boolean isCellEditable(int row, int col) {
		        //Note that the data/cell address is constant,
		        //no matter where the cell appears onscreen.
		       /* if (col < 2) {
		            return false;
		        } else {
		            return true;
		        }*/
		    	//@TODO change
		    	return false;
		    }

		    /*
		     * Don't need to implement this method unless your table's
		     * data can change.
		     */
		    public void setValueAt(Object value, int row, int col) {
		        //data[row][col] = value;
		        //fireTableCellUpdated(row, col);
		    	switch(col) {
		    		case 0 : {
		    			_data.get(row).setDateFromString((String) value);
		    			break;
		    		}
		    		case 1 : {
		    			_data.get(row).setCategory((String) value);
		    			break;
		    		}
		    		case 2 : {
		    			_data.get(row).setContent((String) value);
		    			break;
		    		}
		    		default : {
		    			System.err.println("DiaryModell::setValueAt() error, column " + col + " out of bounds");
		    			break;
		    		}
		    	}
		    	_data.get(row).SetDirty(true);
		    	Update();
		    }
		    
		    
		    protected void Update() 
		    {
		    	
		    	lastUpdateInMs = Calendar.getInstance().getTimeInMillis();
		    	if (_udthr == null) {
		    		_udthr = new updateThr();
		    		System.out.println("Updatethread created");
		    	} else {
		    		if (!_udthr.isAlive()) {
		    			_udthr = new updateThr();
		    		}
		    	}
		    			
		    	if(_udthr.isAlive()) {
		    		System.out.println("Updatethread running, doing nothing");
		    	} else {
		    		_udthr.start();
		    		System.out.println("Updatethread started");
		    	}
		    }
}

