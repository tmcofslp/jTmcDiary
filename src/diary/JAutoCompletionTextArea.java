package diary;

import java.awt.event.ActionEvent;
import java.util.Collections;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.text.BadLocationException;


public class JAutoCompletionTextArea extends JTextArea implements ListDataListener, 
DocumentListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String COMMIT_ACTION = "commit";
    private static enum Mode { INSERT, COMPLETION };
    //private List<String> words=new ArrayList<String>(5);
    private Mode mode = Mode.INSERT;
    
    private CategoryModell _model = null;
    
    protected void init() {
    	getDocument().addDocumentListener(this);
        
        InputMap im = getInputMap();
        ActionMap am = getActionMap();
        im.put(KeyStroke.getKeyStroke("ENTER"), COMMIT_ACTION);
        am.put(COMMIT_ACTION, new CommitAction());
    }
    public JAutoCompletionTextArea() {
    	init(); 
        
    }
    
    public JAutoCompletionTextArea(CategoryModell newModell) {
    	init();
    	setModel(newModell);    	
    }
    
    public void setModel(CategoryModell new_model) {
    	_model = new_model;
    }
    
//    public void AddSuggestion(String word) {
//    	words.add(word);
//    }
//    
//    public void SetCompletionList(List<String> newWords) {
//    	words = newWords;
//    }
//    
//    public void setSize(int size) {
//    	List<String> tmpWords = words;
//    	List<String> newWords = new ArrayList<String>(size);
//    	for(int i =0; i < Math.min(tmpWords.size(), size); i++) {
//    		newWords.add(tmpWords.get(i));
//    	}
//    	SetCompletionList(newWords);
//    }
//    
   

	public void insertUpdate(DocumentEvent ev) {
        System.out.println("insertUpdate() call!");
		if(_model == null) {
			//@TODO DK define exception for this
			System.err.println("JAutoCompletionTextArea.insertUpdate(): modell not defined!");
			return;
		}
        if (ev.getLength() != 1) {
            return;
        }
        
        int pos = ev.getOffset();
        String content = null;
        try {
            content = getText(0, pos + 1);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        
        // Find where the word starts
        int w;
        for (w = pos; w >= 0; w--) {
            if (! Character.isLetter(content.charAt(w))) {
                break;
            }
        }
        if (pos - w < 2) {
            // Too few chars
            return;
        }
        
        String prefix = content.substring(w + 1).toLowerCase();
        int n = Collections.binarySearch(_model.getStringList(), prefix);
        //if (n < 0 && -n <= words.size()) {
        if (n < 0 && -n <= _model.getSize()) {
            //String match = words.get(-n - 1);
        	String match = _model.getElementAt(-n -1);
            if (match.startsWith(prefix)) {
                // A completion is found
                String completion = match.substring(pos - w);
                // We cannot modify Document from within notification,
                // so we submit a task that does the change later
                SwingUtilities.invokeLater(
                        new CompletionTask(completion, pos + 1));
            }
        } else {
            // Nothing found
            mode = Mode.INSERT;
        }
    }
    
    private class CompletionTask implements Runnable {
        String completion;
        int position;
        
        CompletionTask(String completion, int position) {
            this.completion = completion;
            this.position = position;
        }
        
        public void run() {
            insert(completion, position);
            setCaretPosition(position + completion.length());
            moveCaretPosition(position);
            mode = Mode.COMPLETION;
        }
    }
    
    
    private class CommitAction extends AbstractAction {
    	@Override
    	public void actionPerformed(ActionEvent arg0) {
            if (mode == Mode.COMPLETION) {
                int pos = getSelectionEnd();
                insert(" ", pos);
                setCaretPosition(pos + 1);
                mode = Mode.INSERT;
            } else {
                replaceSelection("\n");
            }
        }

		
    }

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void intervalAdded(ListDataEvent e) {
		// TODO Auto-generated method stub
		// nothing todo..
		
	}

	@Override
	public void intervalRemoved(ListDataEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contentsChanged(ListDataEvent e) {
		// TODO Auto-generated method stub
		
	}
}
