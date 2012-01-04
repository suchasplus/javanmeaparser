package gui.sampleclient;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.ArrayList;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import ocss.nmea.api.NMEAReader;
import ocss.nmea.api.NMEAListener;
import ocss.nmea.api.NMEAEvent;

public class NMEAFrame extends JFrame 
{
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel bottomPanel = new JPanel();
  JButton readButton = new JButton();

  String simulatorDataFile = null;
  
  TablePane tp = new TablePane();

  boolean reading = false;
  
  private CustomClient4Frame cc4f = null;
  private NMEAReader r            = null;
  private List<NMEAListener> NMEAListeners = null;
  
  JPanel topPanel = new JPanel();
  JLabel jLabel1 = new JLabel();
  JTextField prefixFld = new JTextField();
  JLabel jLabel2 = new JLabel();
  JLabel nmeaLengthLabel = new JLabel();
  JLabel jLabel4 = new JLabel();
  private JTextField portTextField = new JTextField();
  private JLabel jLabel3 = new JLabel();
  
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
      NMEAListener l = (NMEAListener)NMEAListeners.get(i);
      l.stopReading(e);
    }
  }

  private void jbInit() throws Exception
  {
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
    topPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    jLabel1.setText("NMEA Device Prefix:");
    prefixFld.setText("II"); // B&G Hydra
    prefixFld.setPreferredSize(new Dimension(20, 20));
    prefixFld.setHorizontalAlignment(JTextField.CENTER);
    jLabel2.setText("(current NMEA stream length:");
    nmeaLengthLabel.setText("0");
    jLabel4.setText(")");
    portTextField.setText("COM1");
    portTextField.setPreferredSize(new Dimension(50, 20));
    portTextField.setHorizontalAlignment(JTextField.CENTER);
    jLabel3.setText("Port:");
    bottomPanel.add(readButton, null);
    this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    this.getContentPane().add(tp, BorderLayout.CENTER);
    topPanel.add(jLabel3, null);
    topPanel.add(portTextField, null);
    topPanel.add(jLabel1, null);
    topPanel.add(prefixFld, null);
    topPanel.add(jLabel2, null);
    topPanel.add(nmeaLengthLabel, null);
    topPanel.add(jLabel4, null);
    this.getContentPane().add(topPanel, BorderLayout.NORTH);
  }

  void readButton_actionPerformed(ActionEvent e)
  {     
    if (!reading)
    {
      reading = true;
      readButton.setText("Stop");

      cc4f = new CustomClient4Frame(this.prefixFld.getText(), 
                                    tp.getKeys(),
                                    tp);
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