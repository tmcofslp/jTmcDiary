package diary;
import diary.CategoryModell;
import diary.DiaryModell;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;
import javax.swing.JTable;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.text.DateFormat;

import javax.swing.ListSelectionModel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class DiaryView extends JFrame {

	private JPanel contentPane;
	private static JTable table;
	private static JComboBox<String> cboCategoryVw = new JComboBox<String>();
	private static JTextField txtContentVw = new JTextField();
	private static JComboBox<String> cboCategoryEdit = new JComboBox<String>();
	private static JTextField txtDateEdit = new JTextField();
	private static JTextArea txtContentEdit = new JTextArea();
	private static JScrollPane scrContentEdit = new JScrollPane();
	private static JButton btnAdd = new JButton("add");
	private static JButton btnDel = new JButton("delete");
	private static CategoryModell _category_modell;
	private static DiaryModell _diary_modell;
	private static boolean _updateContentEdit = true;
	private JTextField textField;
	protected JScrollPane spTable;
	private static boolean updateContentEdit = true;
	
	
	private static CategoryEditListener _catEditLister = new CategoryEditListener(_diary_modell) 
	{
		@Override
		protected void UpdateDm(DocumentEvent e)  
		{
			try {
				if (_diary_modell != null && table.getSelectedRow() >= 0) {
					final int oldRow = table.getSelectedRow();
					updateContentEdit = false;
					//_category_modell.addElement(e.getDocument().getText(0, e.getDocument().getLength()));
					//_category_modell.ResetModell();
					
					//cboCategoryEdit.revalidate();
					System.out.println("document=" + e.getDocument().getText(0, e.getDocument().getLength()));
					
					_diary_modell.fireTableDataChanged();
					_diary_modell.setValueAt(e.getDocument().getText(0, e.getDocument().getLength()), oldRow, 1);
					SelectRow(oldRow);
					_category_modell.ResetModell();
					_category_modell.addElement(e.getDocument().getText(0, e.getDocument().getLength()));
					cboCategoryEdit.setModel(_category_modell);
					cboCategoryEdit.setSelectedItem(e.getDocument().getText(0, e.getDocument().getLength()));
						
					//_category_modell.f
					updateContentEdit = true;
				}
			} catch (Exception ex) {
				System.err.println("error in updating content");
				ex.printStackTrace();
			}
		
			
		}
	};
	
	private static ContentDocListener _doclistener = new ContentDocListener() {
		@Override
	    public void updateLog(DocumentEvent e, String action) {
			try {
				
				if (_diary_modell != null && table.getSelectedRow() >= 0) {
					final int oldRow = table.getSelectedRow();
					updateContentEdit = false;
					//_updateContentEdit = false;
					//txtContentEdit.getDocument().removeDocumentListener(_doclistener);
					// remove document listener tempory for not getting updates into table as ring
						_diary_modell.fireTableDataChanged();
						_diary_modell.setValueAt(e.getDocument().getText(0, e.getDocument().getLength()), oldRow, 2);

						SelectRow(oldRow);
					//	txtContentEdit.getDocument().addDocumentListener(_doclistener);
						//_updateContentEdit = true;
				   updateContentEdit = true;
					
				}
				
				} catch (Exception ex) {
					System.err.println("error in updating content");
					ex.printStackTrace();
				}
			
		}
		

	};
	 
	static {
	btnAdd.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			
			if(_diary_modell != null ) {
				//Date today;
				
				String catToAdd;
				
				System.out.println("index=" + cboCategoryVw.getSelectedIndex());
				if (cboCategoryVw.getSelectedIndex() <= 0) {
					catToAdd = "";
				} else {
					catToAdd = (String) cboCategoryVw.getSelectedItem();
				}
		
				java.util.Calendar cal = java.util.Calendar.getInstance();
				java.util.Date utilDate = cal.getTime();
				
				_diary_modell.addRow(
		 
						new DiaryObject(
								DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date(utilDate.getTime())),
								catToAdd, "")
						
						);
				_diary_modell.ResetModell();
				_diary_modell.fireTableDataChanged();
				SelectRow(_diary_modell.getRowCount() - 1);
				//table.setRowSelectionInterval(_diary_modell.getRowCount() - 1, table.getRowCount() - 1);
			}
			//_diary_modell.addRow({);
		}
	});
	
	btnDel.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			
			if(_diary_modell != null && table.getSelectedRow() >=0) {
				_diary_modell.removeRow(table.getSelectedRow());
				ResetModels();
				_diary_modell.fireTableDataChanged();
				SelectRow(_diary_modell.getRowCount() -1 );
				//table.setRowSelectionInterval(_diary_modell.getRowCount() - 1, table.getRowCount() - 1);
			}
		}
	});
	
	txtDateEdit.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			if (_diary_modell != null && table.getSelectedRow() >= 0) {
				int oldRow = table.getSelectedRow();
				
				_diary_modell.setValueAt(txtDateEdit.getText(), table.getSelectedRow(), 0);
				_diary_modell.fireTableDataChanged();
				SelectRow(oldRow);
				
			}
		}
		
	});
	cboCategoryEdit.addKeyListener(new KeyAdapter() {
		
	
		@Override
		public void keyPressed (KeyEvent e) {
			if (!e.isActionKey()) {
				super.keyPressed(e);
				
			}
		}
		/*@Override
		public void keyTyped(KeyEvent e) {
			if(_diary_modell != null && table.getSelectedRow() >= 0) {
				int oldRow = table.getSelectedRow();
				
				_diary_modell.setValueAt((String) cboCategoryEdit.getEditor().getItem(), table.getSelectedRow() , 1);
				_diary_modell.fireTableDataChanged();
				SelectRow(oldRow);
				//table.setRowSelectionInterval(oldRow, oldRow);
				_category_modell.ResetModell();
				
			}
		}*/
	});
	
	cboCategoryEdit.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
		
			if(_diary_modell != null && table.getSelectedRow() >= 0) {
				int oldRow = table.getSelectedRow();
				
				_diary_modell.setValueAt((String) cboCategoryEdit.getSelectedItem(), table.getSelectedRow() , 1);
				_diary_modell.fireTableDataChanged();
				SelectRow(oldRow);
				
			}
		}
	});
	
	txtContentEdit.getDocument().addDocumentListener(_doclistener);
	((JTextComponent) cboCategoryEdit.getEditor().getEditorComponent()).getDocument().addDocumentListener(_catEditLister);
}
	public void AddContentVwListener(ActionListener l)
	{
		txtContentVw.addActionListener(l);
	}
	public void AddCategoryViewListener(ActionListener l)
	{
		cboCategoryVw.addActionListener(l);
	}
	
	protected static void SelectRow(int nRow) {
		if (nRow > 0) {
			table.setRowSelectionInterval(nRow, nRow);
			JViewport viewport = (JViewport) table.getParent();
            Rectangle rect = table.getCellRect(table.getSelectedRow(), 0, true);
            Point pt = viewport.getViewPosition(); 
            rect.setLocation(rect.x-pt.x, rect.y-pt.y);
           // rect.setLeft(0);
           // rect.setWidth(1);
            Rectangle r2 = viewport.getVisibleRect();
            //if()
            //if()
            if(!r2.contains(rect)) {
            
            	table.scrollRectToVisible(new Rectangle(rect.x, rect.y, (int) r2.getWidth(), (int) r2.getHeight()));
            }
		}
	}
	protected static void ResetModels() 
	{
		_diary_modell.ResetModell();
		_category_modell.ResetModell();
		_diary_modell.fireTableDataChanged();
		
	}
	public CategoryModell GetCategoryModell() 
	{
		return _category_modell;
	}
	
	public void SetCategoryModell(CategoryModell new_category_modell) 
	{
		_category_modell = new_category_modell;
		
		if(cboCategoryVw != null) {
			System.out.println("DiaryView::SetCategoryModell size=" + _category_modell.getSize());
			cboCategoryVw.setModel(_category_modell);
			cboCategoryEdit.setModel(_category_modell);
		} else {
			System.out.println("DiaryView::SetCategoryModel is null!!!");
		}
		
	}
	
	public DiaryModell GetDiaryModell() 
	{
		return _diary_modell;
	}
	
	public void SetDiaryModell(DiaryModell new_diary_modell) 
	{
		_diary_modell = new_diary_modell;
		
		if (table != null) {
			table.setModel(_diary_modell);
		}
	}
	/**
	 * Launch the application.
	 */
	
	

	/**
	 * Create the frame.
	 */
	protected void CreateControls() 
	{
		setMinimumSize(new Dimension(640,480));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JButton button;
		Container pane = getContentPane();
		//JPanel pane 
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowHeights = new int[]{0, 78, 0};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0};
		pane.setLayout(gridBagLayout);
		GridBagConstraints c = new GridBagConstraints();
		GridBagConstraints d = new GridBagConstraints();
		GridBagConstraints gbConVw = new GridBagConstraints();

		GridBagConstraints e = new GridBagConstraints();
		e.anchor = GridBagConstraints.EAST;
		GridBagConstraints f = new GridBagConstraints();
		GridBagConstraints g = new GridBagConstraints();
		GridBagConstraints gbch = new GridBagConstraints();
		GridBagConstraints gbci = new GridBagConstraints();
		gbci.weighty = 100.0;
		GridBagConstraints gbcj = new GridBagConstraints();
		JSplitPane splitPane; 
		JSplitPane spEdit;
		GridBagLayout gbl_pnlTbl = new GridBagLayout();
		JPanel pnlTbl = new JPanel(gbl_pnlTbl);
		JPanel pnlEdit = new JPanel(new GridBagLayout());
		GridBagConstraints gbcTbl = new GridBagConstraints();
		gbcTbl.weighty = 100.0;
		gbcTbl.weightx = 100.0;
		GridBagConstraints gbcEdit = new GridBagConstraints();

		c.weightx = 0.5;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START;
		c.gridx = 0;
		c.gridy = 0;
		System.out.println("listener added");
		
		pane.add(btnAdd, c);
	
	//	button = new JButton("Button 2");
		d.fill = GridBagConstraints.HORIZONTAL;
		d.weightx = 0.5;
		d.gridx = 1;
		d.gridy = 0;
		pane.add(btnDel, d);
	
		//button = new JButton("Button 3");
		
		gbConVw.fill = GridBagConstraints.HORIZONTAL;
		gbConVw.gridwidth=1;
		gbConVw.weightx = 0.5;
		gbConVw.gridx = 2;
		gbConVw.gridy = 0;
		pane.add(txtContentVw, gbConVw);
		
		e.fill = GridBagConstraints.EAST;
		e.anchor = GridBagConstraints.NORTHEAST;
		e.gridwidth=1;
		e.weightx = 0.5;
		e.gridx = 3;
		e.gridy = 0;
		
		pane.add(cboCategoryVw, e);
		//pane.add(button, e);
	
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// add select row event
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
	        public void valueChanged(ListSelectionEvent event) {
	            // do some actions here, for example
	            // print first column value from selected row
	        	ShowRow(table.getSelectedRow());
	        }
	    });
		spTable = new JScrollPane(table);
		
		
		if(GetDiaryModell() != null) {
			table.setModel(GetDiaryModell());
			//table.setRowSelectionInterval(_diary_modell.getRowCount() -1 , _diary_modell.getRowCount() -1 );
			SelectRow(_diary_modell.getRowCount() - 1);
			System.out.println("setting modell");
		}
		
		spEdit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		spEdit.setDividerLocation(200);
		gbcTbl.fill = GridBagConstraints.BOTH;
	//	gbcTbl.ipady = 40;
		gbcTbl.gridx = 0;
		gbcTbl.gridy = 0;
		
		pnlTbl.add(spTable, gbcTbl);
		
		g.fill = GridBagConstraints.HORIZONTAL;
		//g.ipady = 40;
		g.weightx = 40.0;
		g.gridx = 0;
		g.gridy = 0;
		g.gridwidth = 2;
		
		//pane.add(txtDateEdit, g);
		
		//gbch.fill = GridBagConstraints.HORIZONTAL;
		//gbch.weightx = 0.0;
		//gbch.gridx = 1;
		//gbch.gridy = 2;
		cboCategoryEdit.setEditable(true);
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                txtDateEdit, cboCategoryEdit);
		//splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);
		pnlEdit.add(splitPane, g);
		

		gbcEdit.fill = GridBagConstraints.BOTH;
		gbcEdit.ipady = 40;
		gbcEdit.anchor = GridBagConstraints.SOUTHEAST;
		gbcEdit.weightx = 40.0;
//		gbci.weighty = 10.0;
		gbcEdit.gridx = 2;
		gbcEdit.gridy = 0;
	
		gbci.fill = GridBagConstraints.BOTH;
		gbci.ipady = 40;
		gbci.anchor = GridBagConstraints.SOUTHEAST;
		gbci.weightx = 80.0;
//		gbci.weighty = 10.0;
		gbci.gridx = 2;
		gbci.gridy = 0;
		gbci.gridwidth = 2;
		txtContentEdit.setLineWrap(true);
		scrContentEdit.setViewportView(txtContentEdit);
		pnlEdit.add(scrContentEdit, gbci);
		
		//button = new JButton("Long-Named Button 4");
		f.fill = GridBagConstraints.BOTH;
		f.ipady = 40;      //make this component tall
		f.weightx = 0.0;
		f.gridwidth = 4;
		f.gridx = 0;
		f.gridy = 1;
		
		spEdit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlTbl, pnlEdit);
		spEdit.setDividerLocation(200);
		
		pane.add(spEdit, f);
	
		g.fill = GridBagConstraints.HORIZONTAL;
		//g.ipady = 40;
		g.weightx = 40.0;
		g.gridx = 0;
		g.gridy = 2;
		g.gridwidth = 2;
		
		//pane.add(txtDateEdit, g);
		
		//gbch.fill = GridBagConstraints.HORIZONTAL;
		//gbch.weightx = 0.0;
		//gbch.gridx = 1;
		//gbch.gridy = 2;
//		cboCategoryEdit.setEditable(true);
//		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
//                txtDateEdit, cboCategoryEdit);
//		//splitPane.setOneTouchExpandable(true);
//		splitPane.setDividerLocation(150);
//		pane.add(splitPane, g);
//		
//
//		gbci.fill = GridBagConstraints.BOTH;
//		gbci.ipady = 40;
//		gbci.anchor = GridBagConstraints.SOUTHEAST;
//		gbci.weightx = 40.0;
////		gbci.weighty = 10.0;
//		gbci.gridx = 2;
//		gbci.gridy = 2;
//		gbci.gridwidth = 2;
//		scrContentEdit.getViewport().add(txtContentEdit);
//		pane.add(scrContentEdit, gbci);
		
		/*button = new JButton("5");
		g.fill = GridBagConstraints.HORIZONTAL;
		g.ipady = 0;       //reset to default
		g.weighty = 1.0;   //request any extra vertical space
		g.anchor = GridBagConstraints.PAGE_END; //bottom of space
		g.insets = new Insets(10,0,0,0);  //top padding
		g.gridx = 1;       //aligned with button 2
		g.gridwidth = 2;   //2 columns wide
		g.gridy = 2;       //third row
		pane.add(button, g);
	*/
		
		
		
	}
	
	public DiaryView() 
	{
		CreateControls();
	}
	
	public DiaryView(DiaryModell new_diary_modell, 
			CategoryModell new_category_modell) 
	{
		SetDiaryModell(new_diary_modell);
		SetCategoryModell(new_category_modell);
		CreateControls();
	}
	protected void ShowRow(int row) 
	{
		
		if (row >= 0 && row < _diary_modell.getRowCount()) {
			// @TODO implement last save

			
			//if (_updateContentEdit) {
			if (updateContentEdit) {
				// TODO remove and add documentlistener for cboCategoryEdit
				cboCategoryEdit.getEditor().setItem(_diary_modell.getValueAt(row, 1));
				txtDateEdit.setText(_diary_modell.getValueAt(row, 0).toString());
				txtContentEdit.getDocument().removeDocumentListener(_doclistener);
				
				txtContentEdit.setText(_diary_modell.getValueAt(row, 2).toString());
				txtContentEdit.getDocument().addDocumentListener(_doclistener);
			}
			//}
			//cboCategoryEdit.setv
			//cboCategoryEdit.set
			//cboCategoryEdit.setee
		}
	}

}
