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
		
		@SuppressWarnings("unchecked") // we did not use generics, so we have to supress this warning
		String CatContent = ((JComboBox<String>)e.getSource()).getSelectedItem().toString();
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
