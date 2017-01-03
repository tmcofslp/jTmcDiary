package diary;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

class ContentDocListener implements DocumentListener {
    String newline = "\n";
    protected int colNum;
   
    
    public ContentDocListener() 
    {
    	
    }
    public ContentDocListener(int newColNum) {
    	setColNum(newColNum);
    }
    @Override
    public void insertUpdate(DocumentEvent e) {
        updateLog(e, "inserted into");
    }
    @Override
    public void removeUpdate(DocumentEvent e) {
        updateLog(e, "removed from");
    }
    @Override
    public void changedUpdate(DocumentEvent e) {
        //Plain text components do not fire these events
    }

    public void updateLog(DocumentEvent e, String action) {
        Document doc = e.getDocument();
        int changeLength = e.getLength();
        System.out.println(
            changeLength + " character" +
            ((changeLength == 1) ? " " : "s ") +
            action + doc.getProperty("name") + "." + newline +
            "  Text length = " + doc.getLength() + newline);
    }
    
    public void setColNum(int newColNum) {
    	colNum = newColNum;
    }
    
    public int getColNum() {
    	return colNum;
    }
	

}
