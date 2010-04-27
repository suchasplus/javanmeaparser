package gui.sampleclient;

import java.awt.Graphics;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class TablePane
     extends JPanel 
{
  // Table Columns
  final static String SENTENCE_ID   = "Sentence ID";
  final static String VALUE         = "Value";

  final String[] names = {SENTENCE_ID,
                          VALUE};
  // Table content
  Object[][] data = new Object[0][names.length];
  TableModel dataModel;

  JTable table;

  // Some specific columns: Image list and Deployment check box
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel centerPanel = new JPanel();
  JPanel bottomPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JScrollPane centerScrollPane = null; // new JScrollPane();
  JPanel topPanel = new JPanel();
  JLabel jLabel1 = new JLabel();
  JButton addButton = new JButton();
  JButton removeButton = new JButton();

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
    jLabel1.setText("Set the sentence(s) you want to read");
    addButton.setText("Add");
    addButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          addButton_actionPerformed(e);
        }
      });
    removeButton.setText("Remove");
    removeButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          removeButton_actionPerformed(e);
        }
      });
    this.add(centerPanel, BorderLayout.CENTER);
    bottomPanel.add(addButton, null);
    bottomPanel.add(removeButton, null);
    this.add(bottomPanel, BorderLayout.SOUTH);
    topPanel.add(jLabel1, null);
    this.add(topPanel, BorderLayout.NORTH);
    initTable();
  }
  
  private void initTable()
  {
    // Init Table
    dataModel = new AbstractTableModel()
    {
      public int getColumnCount()
      { return names.length; }
      public int getRowCount()
      { return data.length; }
      public Object getValueAt(int row, int col)
      { return data[row][col]; }
      public String getColumnName(int column)
      { return names[column]; }
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
    Object[][] newData = new Object[len + 1][names.length];
    for (int i=0; i<len; i++)
    {
      for (int j=0; j<names.length; j++)
        newData[i][j] = data[i][j];
    }
    newData[len][0] = s;
    newData[len][1] = "";
    data = newData;
    ((AbstractTableModel)dataModel).fireTableDataChanged();
//  table.setRowHeight(table.getRowHeight()); // Otherwise, repaint doesn't happen !!
//  table.repaint();
  }

  private void removeCurrentLine()
  {
    int selectedRow = table.getSelectedRow();
//  System.out.println("Row " + selectedRow + " is selected");
    if (selectedRow < 0)
      JOptionPane.showMessageDialog(null,
                                    "Please choose a row to remove",
                                    "Removing an entry",
                                    JOptionPane.WARNING_MESSAGE);
    else
    {
      int l = data.length;
      Object[][] newData = new Object[l - 1][names.length];
      int oldInd, newInd;
      newInd = 0;
      for (oldInd=0; oldInd<l; oldInd++)
      {
        if (oldInd != selectedRow)
        {
          for (int j=0; j<names.length; j++)
            newData[newInd][j] = data[oldInd][j];
          newInd++;
        }
      }
      data = newData;
      ((AbstractTableModel)dataModel).fireTableDataChanged();
//    table.setRowHeight(table.getRowHeight()); // Otherwise, repaint doesn't happen !!
//    table.repaint();
    }
  }

  void addButton_actionPerformed(ActionEvent e)
  {
    try { addLineInTable(""); }
    catch (Exception ignore) {}
  }

  void removeButton_actionPerformed(ActionEvent e)
  {
    removeCurrentLine();
  }

  public String[] getKeys()
  {
    String[] sa = new String[data.length];
    for (int i=0; i<data.length; i++)
      sa[i] = new String((String)data[i][0]);
    return sa;
  }

  public void setValue(String key, String val)
  {
    for (int i=0; i<data.length; i++)
    {
      if (((String)data[i][0]).toUpperCase().equals(key))
      {
        data[i][1] = val;
        break;
      }
    }
    // Repaint
    ((AbstractTableModel)dataModel).fireTableDataChanged();
//    table.setRowHeight(table.getRowHeight()); // Otherwise, repaint doesn't happen !!
//    table.repaint();
  }
  
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    TableColumn column = table.getColumn(SENTENCE_ID);
    column.setPreferredWidth((int)(table.getWidth() / 4));    
    System.out.println("Table Width:" + (table.getWidth()));
    ((AbstractTableModel)dataModel).fireTableDataChanged();
    table.repaint();
  }
}