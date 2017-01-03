package diary;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

public class CategoryVwListener implements ActionListener{

	protected DiaryModell dm;
	
	public void SetDiaryModell(DiaryModell newDm)
	{
		dm = newDm;
	}
	
	public DiaryModell GetDiaryModell()
	{
		return dm;
	}
	
	public CategoryVwListener(DiaryModell newDm)
	{
		SetDiaryModell(newDm);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		String CatContent = ((String)((JComboBox<String>)e.getSource()).getSelectedItem());
		if(dm != null) {
			if(CatContent.length() > 0) {
				dm.SetCategoryFilter(CatContent);
			} else {
				dm.EraseCategoryFilter();
			}
			dm.ResetModell();
			dm.fireTableDataChanged();
		}
	
			
	}

}
