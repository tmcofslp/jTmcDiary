package diary;

import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.event.ListDataListener;

public class CategoryModell extends DefaultComboBoxModel<String>{
	private static final long serialVersionUID = 2L;
	private Vector<String> _data = new Vector<String>();
	int index=-1;
	protected static DBController _db;
	
	
	
	public CategoryModell() throws RuntimeException, FileNotFoundException 
	{
		_db = DBController.getInstance();
		ResetModell();
		System.out.println("CategoryModell::CategoryModell call!");
	}
	
	public String getElementAt(int index) {
	        return _data.elementAt(index);
	}
	 
	 public boolean ResetModell() 
	 {
			boolean ret = false;
			try {
				Statement stmt = _db.GetConnection().createStatement();
				ResultSet rs = stmt.executeQuery("select Category from DiaryTable group by Category");
				if (_data.size() > 0) {
					_data.clear();
				}
				_data.add("");			
				while(rs.next()) {
					_data.add((String) (rs.getString(1)));

				}
				fireContentsChanged(this, 0,_data.size() - 1);
				

				
			} catch (Exception ex) {
				System.err.println("Categorymodell error resetting db backbone");
				ex.printStackTrace();
				throw new RuntimeException();
				
			}
			
			return ret;	
	 }
	
	 @Override
	public int getSize() {
		 return _data.size();
	}

	@Override
	public void addListDataListener(ListDataListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeListDataListener(ListDataListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getSelectedItem() {
		// TODO Auto-generated method stub
		if (index >= 0) {
			return getElementAt(index);
		}
		return null;
	}

	@Override
	public void setSelectedItem(Object arg0) {
		for (int i =0; i < getSize(); i++) {
			if (getElementAt(i).equals(arg0)) {
				index = i;
				return;
			}
		}
	}

	@Override
	public void addElement(String anObject) {
		// TODO Auto-generated method stub
		super.addElement(anObject);
		ResetModell();
	//	fireContentsChanged(arg0, arg1, arg2);
	}
	
	
	public List<String> getStringList() {
		return _data;
	}
}
