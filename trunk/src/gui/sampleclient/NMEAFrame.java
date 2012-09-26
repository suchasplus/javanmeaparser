package gui.sampleclient;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import ocss.nmea.api.NMEAEvent;
import ocss.nmea.api.NMEAListener;
import ocss.nmea.api.NMEAReader;


public class NMEAFrame extends JFrame 
{
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel bottomPanel = new JPanel();
  JButton readButton = new JButton();

  String simulatorDataFile = null;
  
  TablePane tp = new TablePane();

  boolean reading = false;
  
  private transient CustomClient4Frame cc4f = null;
  private transient NMEAReader r            = null;
  private transient List<NMEAListener> NMEAListeners = null;
  
  JPanel topPanel = new JPanel();
  private JTextField portTextField = new JTextField();
  private JLabel portLabel = new JLabel();
  
  public NMEAFrame(String fName)
  {
    simulatorDataFile = fName;
    try
    {
      jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  protected void fireStopReading(NMEAEvent e)
  {
    for (int i=0; i<NMEAListeners.size(); i++)
    {
      NMEAListener l = NMEAListeners.get(i);
      l.stopReading(e);
    }
  }

  private void jbInit() throws Exception
  {
    portLabel.setEnabled(this.simulatorDataFile == null);
    portTextField.setEnabled(this.simulatorDataFile == null);
    this.getContentPane().setLayout(borderLayout1);
    this.setSize(new Dimension(400, 300));
    this.setTitle("NMEA Reader");
    readButton.setText("Read");
    readButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          readButton_actionPerformed(e);
        }
      });
    topPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)); // B&G Hydra
    portTextField.setText("COM1");
    portTextField.setPreferredSize(new Dimension(50, 20));
    portTextField.setHorizontalAlignment(JTextField.CENTER);
    portLabel.setText("Port:");
    bottomPanel.add(readButton, null);
    this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    this.getContentPane().add(tp, BorderLayout.CENTER);
    topPanel.add(portLabel, null);
    topPanel.add(portTextField, null);
    this.getContentPane().add(topPanel, BorderLayout.NORTH);
  }

  void readButton_actionPerformed(ActionEvent e)
  {     
    if (!reading)
    {
      reading = true;
      readButton.setText("Stop");

      cc4f = new CustomClient4Frame(tp);
      NMEAListeners = cc4f.getListeners();
      if (this.simulatorDataFile != null)
      {
        r = new CustomFileReader(NMEAListeners, this.simulatorDataFile);
        cc4f.setEOS("\n");
      }
      else
        r = new CustomSerialReader(NMEAListeners, this.portTextField.getText()); // Real Serial port
      cc4f.setReader(r);
      cc4f.initClient();
      cc4f.startWorking();
    }
    else
    {
      reading = false;
      readButton.setText("Read");
      fireStopReading(new NMEAEvent(this));
    }
  }
}