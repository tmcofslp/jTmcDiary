package diary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringEscapeUtils;

public class DiaryObject {
	protected Date _date;
	protected String Category;
	protected String Content;
	protected String ID;
	protected boolean _dirty = false;
	
	public DiaryObject(String date, String category, String content) 
	{
		UUID newID = UUID.randomUUID();
		
		_init(newID.toString(), date, category, content);
		
	}
	

	
	public DiaryObject(String iD, String date, String category, String content) 
	{
		_init(iD, date, category, content);
	}
	
	public void  _init(String iD, String date, String category, String content) 
	{
		
		ID = iD;
		_init(date, category, content);
	}

	
	
	private void _init(String date, String category, String content)
	{
		setDateFromString(DecodeForDB(date));
		Category = DecodeForDB(category);
		Content = DecodeForDB(content);
	}

	public boolean GetDirty() 
	{
		return _dirty;
	}
	
	public void SetDirty(Boolean newDirty)
	{
		_dirty = newDirty;
	}
	public String[] GetRowObject() 
	{
		String[] ret = {getID(), getDateString(), getCategory(), getContent() };
		return ret;
	}
	
	public Date getDate() {
		return _date;
	}
	
	public String getDateString()
	{
		return String.format("%tY-%tm-%td", _date, _date, _date);
		//return DateFormat.getDateInstance(DateFormat.MEDIUM).format(_date);
	}

	public void setDate(Date date) {
		_date = date;
	}
	
	public void setDateFromString(String date) {
		try {
			if (_date == null) {
				_date = new Date();
			}
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			
			if(_date == null) {
				sdf=new SimpleDateFormat("dd.MM.yyyy");
			}
			try {
				_date = sdf.parse(date);
			} catch (ParseException pe) {
				sdf=new SimpleDateFormat("dd.MM.yyyy");
				_date = sdf.parse(date);
				SetDirty(true);
				System.err.println("WARNING switching date format parse type");
			}
			
			
		} catch (Exception ex) {
			System.err.println("DiaryObject::setDateFromString(" + date + ") error parsing");
			ex.printStackTrace();
		}
		
	}

	public String getCategory() {
		return Category;
	}

	public void setCategory(String category) {
		Category = category;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}
	
	public String GetUpdateSqlPart() 
	{
		return EncodeForDB("Set Date='" + getDateString() + 
				"', Category='"+ getCategory() + 
				"', Content='" + getContent() + 
				"' where ID='" + getID() + "';"); 
	}
	
	protected String EncodeForDB(String strSource)
	{
	
		return StringEscapeUtils.escapeJava(strSource);
	}
	
	protected String DecodeForDB(String strSource)
	{
		return StringEscapeUtils.unescapeJava(strSource);
	}

}
