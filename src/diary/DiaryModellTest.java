package diary;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DiaryModellTest {

  @Test
  public void GetSelectSqlEmpty() {

	  DiaryModell _modell = new DiaryModell();
	  System.out.println("Empty: " + _modell.GetSelectSql());
	  Assert.assertTrue(_modell.GetSelectSql().compareTo("select ID, Date, Category, Content from DiaryTable;") == 0);
	  
  }
  
  @Test
  public void GetSelectSqlCat() 
  {
	  DiaryModell _modell = new DiaryModell();
	  _modell.SetCategoryFilter("Privat");
	  System.out.println("Cat: " + _modell.GetSelectSql());
	  Assert.assertTrue(_modell.GetSelectSql().compareTo("select ID, Date, Category, Content from DiaryTable where Category like 'Privat';") == 0);
  }
  
  @Test
  public void GetSelectSqlCont() 
  {
	  DiaryModell _modell = new DiaryModell();
	  _modell.SetWordFilter("blah");
	  System.out.println("Cont:"+ _modell.GetSelectSql());
	  Assert.assertTrue(_modell.GetSelectSql().compareTo("select ID, Date, Category, Content from DiaryTable where Content like '%blah%';") == 0);
  }
  @Test
  public void GetSelectSqlCatCont()
  {
	  DiaryModell _modell = new DiaryModell();
	  _modell.SetCategoryFilter("Privat");
	  _modell.SetWordFilter("blah");
	  System.out.println(_modell.GetSelectSql());
	  Assert.assertTrue(_modell.GetSelectSql().compareTo("select ID, Date, Category, Content from DiaryTable where Category like 'Privat' and Content like '%blah%';") == 0);
  }
}
