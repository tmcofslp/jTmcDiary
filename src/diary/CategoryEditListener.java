package diary;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;



public class CategoryEditListener implements DocumentListener {

	protected DiaryModell Dm;
	public CategoryEditListener(DiaryModell dm) {
		super();
		setDm(dm);
	}

	public DiaryModell getDm() {
		return Dm;
	}

	public void setDm(DiaryModell dm) {
		Dm = dm;
	}

	@Override
	public void changedUpdate(DocumentEvent e) 
	{

		UpdateDm(e);
	}

	@Override
	public void insertUpdate(DocumentEvent e) 
	{
		
		UpdateDm(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) 
	{
	
		UpdateDm(e);	
	}
	
	protected void UpdateDm(DocumentEvent e) 
	{
		if (Dm != null) {
			
		}
	}
}
