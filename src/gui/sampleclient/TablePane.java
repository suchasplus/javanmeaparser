package gui.sampleclient;

import java.awt.BorderLayout;
import java.awt.Graphics;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;


public class TablePane
     extends JPanel 
{
  @SuppressWarnings("compatibility:1863034823770827688")
  public final static long serialVersionUID = 1L;

  // Table Columns
  private final static String SENTENCE_ID   = "Sentence ID";
  private final static String VALUE         = "Value";

  private final static String[] NAMES = {SENTENCE_ID,
                                         VALUE};
  // Table content
  private transient Object[][] data = new Object[0][NAMES.length];
  private transient Map<String, String> dataMap = new HashMap<String, String>();
  private transient TableModel dataModel;

  private JTable table;

  // Some specific columns: Image list and Deployment check box
  private BorderLayout borderLayout1 = new BorderLayout();
  private JPanel centerPanel = new JPanel();
  private JPanel bottomPanel = new JPanel();
  private BorderLayout borderLayout2 = new BorderLayout();
  private JScrollPane centerScrollPane = null; // new JScrollPane();
  private JPanel topPanel = new JPanel();

  public TablePane()
  {
    try
    {
      jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception
  {
    this.setLayout(borderLayout1);
    centerPanel.setLayout(borderLayout2);
    this.add(centerPanel, BorderLayout.CENTER);
    this.add(bottomPanel, BorderLayout.SOUTH);
    this.add(topPanel, BorderLayout.NORTH);
    initTable();
  }
  
  private void initTable()
  {
    // Init Table
    dataModel = new AbstractTableModel()
    {
        @SuppressWarnings("compatibility:9070027359489543434")
        public final static long serialVersionUID = 1L;

      public int getColumnCount()
      { return NAMES.length; }
      public int getRowCount()
      { return data.length; }
      public Object getValueAt(int row, int col)
      { return data[row][col]; }
      public String getColumnName(int column)
      { return NAMES[column]; }
      public Class getColumnClass(int c)
      {
        return getValueAt(0, c).getClass();
      }
      public boolean isCellEditable(int row, int col)
      { 
        return col == 0; 
      }
      public void setValueAt(Object aValue, int row, int column)
      { data[row][column] = aValue; }
    };
    table = new JTable(dataModel);
    centerScrollPane = new JScrollPane(table);
    centerPanel.add(centerScrollPane, BorderLayout.CENTER);
  }

  private void addLineInTable(String s)
  {
    int len = data.length;
    Object[][] newData = new Object[len + 1][NAMES.length];
    for (int i=0; i<len; i++)
    {
      for (int j=0; j<NAMES.length; j++)
        newData[i][j] = data[i][j];
    }
    newData[len][0] = s;
    newData[len][1] = "";
    data = newData;
    ((AbstractTableModel)dataModel).fireTableDataChanged();
//  table.setRowHeight(table.getRowHeight()); // Otherwise, repaint doesn't happen !!
//  table.repaint();
  }

  public void setValue(String key, String val)
  {
    dataMap.put(key, val);
    data = new Object[dataMap.size()][NAMES.length]; 
    Set<String> keys = dataMap.keySet();
    int i = 0;
    for (String k : keys)  
    {
      data[i][0] = k;
      data[i][1] = dataMap.get(k);
      i++;
    }
    // Repaint
    ((AbstractTableModel)dataModel).fireTableDataChanged();
  }
  
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    TableColumn column = table.getColumn(SENTENCE_ID);
    column.setPreferredWidth((int)(table.getWidth() / 4));    
//  System.out.println("Table Width:" + (table.getWidth()));
    ((AbstractTableModel)dataModel).fireTableDataChanged();
    table.repaint();
  }
}