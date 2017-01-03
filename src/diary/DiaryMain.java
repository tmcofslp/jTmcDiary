package diary;
import java.awt.EventQueue;
import java.io.FileNotFoundException;

public class DiaryMain 
{
	static DiaryController controller;
	public static final String DB_PATH = "/media/veracrypt2/diary/diary.db";
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try {
					@SuppressWarnings("unused")
					DBController dbc = DBController.getInstance(DB_PATH);
					controller = new DiaryController();
					controller.ShowView();
				} catch (FileNotFoundException ex) {
					System.err.println("Database file \"" + DB_PATH + "\" not found!");
				}
				catch (Exception e) {
					System.err.println("general exception");
					e.printStackTrace();
				}
			}
		});
	}
}
