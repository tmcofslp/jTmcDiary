package diary;

import java.io.FileNotFoundException;

public class DiaryController {
	protected DiaryModell _modell = new DiaryModell();
	protected DiaryView _view = new DiaryView();
	protected CategoryModell _modell_cat;
	protected CategoryVwListener _cvwl = new CategoryVwListener(_modell);
	protected ContentVwListener _covwl = new ContentVwListener(_modell);
	
	public DiaryController() throws RuntimeException, FileNotFoundException
	{
		// TODO Auto-generated constructor stub
		
		_modell.ResetModell();
		System.out.println("rowcount="+_modell.getRowCount());
		_modell_cat = new CategoryModell();
		_view = new DiaryView(_modell, _modell_cat);
		_view.AddCategoryViewListener(_cvwl);
		_view.AddContentVwListener(_covwl);
		_view.setVisible(true);
	}
	
	public boolean ShowView() 
	{		
		try {
			//_view.setVisible(true);
			return true;
		} catch (Exception ex) {
			System.err.println("Error showing view");
			ex.printStackTrace();
			return false;
		}
	}
	
}
