package diary;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

public class ContentVwListener implements ActionListener{


	protected DiaryModell dm;
	
	public ContentVwListener(DiaryModell newDm)
	{
		dm = newDm;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		// TODO Auto-generated method stub
		String contFilt = ((String)((JTextField)arg0.getSource()).getText());
		if (dm != null) {
			if(contFilt.length() > 0) {
				dm.SetWordFilter(contFilt);
			} else {
				dm.EraseWordFilter();
			}
			dm.ResetModell();
			dm.fireTableDataChanged();
		}
	}

}
