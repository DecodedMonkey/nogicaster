/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainCaster.java
 *
 * Created on Aug 19, 2010, 12:31:43 PM
 */
package root;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.Timer;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author Administrator
 */
public class CasterMainFrame extends javax.swing.JFrame {

    CasterClock internalClock;
    String[] enabledtools;
    Properties internalprops;
    CasterDataModel CDM;
    TableColumn AlarmTypeColumn;
    String[] enabledalarms = null;
    JPopupMenu deletepopup;
    //Possible editors for the Condition Column
    JSpinner timespinner;
    JComboBox moongatecombo;
    JComboBox prycecombo;
    JComboBox ruacombo;

    AudioInputStream ais;
    AudioFormat audioformat;
    SourceDataLine sourceDataLine;

    public CasterMainFrame(CasterClock c, Properties props) {
        initComponents();
        internalClock = c;
        internalprops = props;
        enabledtools = CstStatic.parseStrArray(props.getProperty("enabledtools")).toArray(new String[0]);
        CDM = new CasterDataModel(props);
        TickAction t = new TickAction();
        new Timer((int) CstStatic.IRL_SECOND, t).start();

        AlarmTypeColumn = AlarmClockTable.getColumnModel().getColumn(1);
        ((DefaultTableModel) AlarmClockTable.getModel()).addRow(new String[]{"", "", ""});

        enabledalarms = new String[1];
        enabledalarms = (String[]) CstStatic.parseStrArray(props.getProperty("enabledalarms")).toArray(new String[0]);
        JComboBox AlarmTypeCombo = new JComboBox(
                enabledalarms);
        AlarmTypeColumn.setCellEditor(new DefaultCellEditor(AlarmTypeCombo));

        deletepopup = new JPopupMenu();
        JMenuItem delitem = new JMenuItem("Delete");
        JMenuItem additem = new JMenuItem("Add Alarm");
        delitem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ((DefaultTableModel) AlarmClockTable.getModel()).removeRow(AlarmClockTable.getSelectedRow());
                if (AlarmClockTable.getModel().getRowCount() == 0) {
                    ((DefaultTableModel) AlarmClockTable.getModel()).addRow(new String[]{"", "", ""});
                }
            }
        });
        additem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ((DefaultTableModel) AlarmClockTable.getModel()).addRow(new String[]{"", "", ""});
            }
        });
        deletepopup.add(additem);
        deletepopup.add(delitem);

        AlarmClockTable.getModel().addTableModelListener(new CasterAlarmTableModelListener());
        AlarmClockTable.addMouseListener(new PopupListener());

        moongatecombo = new JComboBox((String[]) CstStatic.parseStrArray((String) props.get("moongatenames")).toArray(new String[0]));
        prycecombo = new JComboBox((String[]) CstStatic.parseStrArray((String) props.get("pricenames")).toArray(new String[0]));
        ruacombo = new JComboBox(CDM.getRuaList());

    }

    /** Creates new form MainCaster */
    public CasterMainFrame() {
        //initComponents();
        throw new UnsupportedOperationException("Cannot create Frame w/o clock");
    }

    public void update(
            int ehour, int eminute) {
        String zero = "0";
        if (eminute > 10) {
            zero = "";
        }
        this.ErinnHourLabel.setText(Integer.toString(ehour));
        this.ErinnMinuteLabel.setText(zero + Integer.toString(eminute));


        DefaultTableModel dtm = (DefaultTableModel) AlarmClockTable.getModel();
        for (int rowindex = 0; rowindex < dtm.getRowCount(); rowindex++) {
            //System.out.println("whoo");
            String cell = (String) dtm.getValueAt(rowindex, 1);
            String localtime = internalClock.getLocalHour() + ((internalClock.getLocalMinute() < 10) ? ":0" : ":") + internalClock.getLocalMinute();
            String erinntime = internalClock.getErinnHour() + ((internalClock.getErinnMinute() < 10) ? ":0" : ":") + internalClock.getErinnMinute();
            //[Local Time, Erinn Time, Moongate, Pryce, Rua]
            //System.out.println(internalClock.getServerSecond());
            if (cell.contentEquals("Local Time")) { //both cell types use the same editor
                if (((String) dtm.getValueAt(rowindex, 2)).contentEquals(localtime) && internalClock.getServerSecond() < 1) {//4 rings
                    playAlarm();
                    //System.out.println("whoo");
                }
            }
            //System.out.println(internalClock.getErinnSecond());
            if (cell.contentEquals("Erinn Time")) { //both cell types use the same editor
                if (((String) dtm.getValueAt(rowindex, 2)).contentEquals(erinntime) && internalClock.getErinnSecond() < 30) { //Some mathematical nonsense I don't get.
                    //Toolkit.getDefaultToolkit().beep();
                    playAlarm();
                    System.out.println("gogogo!");
                    //System.out.println(dtm.getValueAt(rowindex, 2) + " " + ((String) dtm.getValueAt(rowindex, 2)).contentEquals(CDM.GetGameTime()) +  " " + CDM.GetGameTime());

                }
            }
//            if(cell.contentEquals("Moongate"))
//            System.out.println(erinntime + dtm.getValueAt(rowindex, 2));
            if (cell.contentEquals("Moongate") && erinntime.contentEquals("18:00") && CDM.isCurrentMoongate((String) dtm.getValueAt(rowindex, 2))) {
                playAlarm();
            }
            if (cell.contentEquals("Pryce") && erinntime.contentEquals("0:00") && CDM.isCurrentPryce((String) dtm.getValueAt(rowindex, 2))) {
                Toolkit.getDefaultToolkit().beep();
            }
            if (cell.contentEquals("Rua") && erinntime.contentEquals("18:00") && CDM.isCurrentRua((String) dtm.getValueAt(rowindex, 2))) {
                Toolkit.getDefaultToolkit().beep();
            }

        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        ErinnHourLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        ErinnMinuteLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        AlarmClockTable = new CasterAlarmClockTable(new CasterAlarmClockRowEditor());
        jMenuBar1 = new javax.swing.JMenuBar();
        FileMenu = new javax.swing.JMenu();
        NewWindowMenuItem = new javax.swing.JMenuItem();
        ExitMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(147, 301));

        jToolBar1.setRollover(true);

        ErinnHourLabel.setText("jLabel1");
        jToolBar1.add(ErinnHourLabel);

        jLabel2.setText(":");
        jToolBar1.add(jLabel2);

        ErinnMinuteLabel.setText("jLabel3");
        jToolBar1.add(ErinnMinuteLabel);

        AlarmClockTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Label", "Type", "Condition"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        AlarmClockTable.setColumnSelectionAllowed(true);
        jScrollPane1.setViewportView(AlarmClockTable);
        AlarmClockTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        FileMenu.setText("File");

        NewWindowMenuItem.setText("New Tool...");
        NewWindowMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewWindowMenuItemActionPerformed(evt);
            }
        });
        FileMenu.add(NewWindowMenuItem);

        ExitMenuItem.setText("Quit");
        FileMenu.add(ExitMenuItem);

        jMenuBar1.add(FileMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void NewWindowMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewWindowMenuItemActionPerformed
        String s = null;
            s = (String) JOptionPane.showInputDialog(
                    this,
                    "Please select a tool",
                    "Caster Tool Selection",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    enabledtools,
                    enabledtools[0]);
        if(s == null) return;
        if (s.contentEquals("Moongate Tool")) {
            makeMoongateTool();
        }
        if (s.contentEquals("Pryce Tool")) {
            makePryceTool();
        }
        if (s.contentEquals("Rua Tool")) {
            makeRuaTool();
        }
        if (s.contentEquals("Farm Tool")) {
            makeFarmTool();
        }

}//GEN-LAST:event_NewWindowMenuItemActionPerformed

    private void makePryceTool() {
        JFrame pryceframe = new JFrame("Pryce Tool");
        CasterPrycePanel pp = new CasterPrycePanel(CDM);
        pryceframe.add(pp);
        pryceframe.repaint();
        pryceframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pryceframe.setLocationRelativeTo(this);
        pryceframe.setSize(100, 150);
        pryceframe.setVisible(true);
    }

    private void makeFarmTool() {
        JFrame farmframe = new JFrame("Farm Tool");
        CasterFarmPanel pp = new CasterFarmPanel(CDM);
        farmframe.add(pp);
        farmframe.repaint();
        farmframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        farmframe.setLocationRelativeTo(this);
        farmframe.setSize(100, 150);
        farmframe.setVisible(true);
    }

    private void makeMoongateTool() {
        JFrame gateframe = new JFrame("Moongate Tool");
        CasterGatePanel gp = new CasterGatePanel(CDM);
        gateframe.add(gp);
        gateframe.repaint();
        gateframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gateframe.setLocationRelativeTo(this);
        gateframe.setSize(300, 300);
        gateframe.setVisible(true);
    }

    private void makeRuaTool() {
        JFrame ruaframe = new JFrame("Rua Tool");
        CasterRuaPanel gp = new CasterRuaPanel(CDM);
        ruaframe.add(gp);
        ruaframe.repaint();
        ruaframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ruaframe.setLocationRelativeTo(this);
        ruaframe.setSize(300, 300);
        ruaframe.setVisible(true);
    }

    public class TickAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            update(
                    internalClock.getErinnHour(),
                    internalClock.getErinnMinute());
            //System.out.println("Updated.");
        }
    }

    private class PopupListener extends MouseAdapter {
        //Sorry about nicking code from Oracle's website.
        //It's 1AM, lemme be. :-(

        @Override
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            //System.out.println("Triggered");
            if (e.isPopupTrigger()) {
                Point p = e.getPoint();
                int rownumber = AlarmClockTable.rowAtPoint(p);
                AlarmClockTable.getSelectionModel().setSelectionInterval(rownumber, rownumber);
                deletepopup.show(e.getComponent(),
                        e.getX(), e.getY());
            }
        }
    }

    private class CasterAlarmTableModelListener implements TableModelListener {

        @Override
        public void tableChanged(TableModelEvent e) {
            if (e.getColumn() == TableModelEvent.ALL_COLUMNS) {
                //System.out.println("For some reason, all columns got modified. Bye.");
                return; //I don't want to deal with a deletion?
            } else if (e.getColumn() == 1) //The alarm type combo
            {
                int rowindex = e.getFirstRow();
                DefaultTableModel dtm = (DefaultTableModel) AlarmClockTable.getModel();
                String cell = (String) dtm.getValueAt(rowindex, e.getColumn());
//                System.out.println(cell);
                //[Local Time, Erinn Time, Moongate, Pryce, Rua]
                CasterAlarmClockTable cact = (CasterAlarmClockTable) AlarmClockTable;
                if (cell.contentEquals("Local Time") || cell.contentEquals("Erinn Time")) { //both cell types use the same editor
                    if (cact.getCellEditor(rowindex, 2) instanceof DateSpinnerCellEditor) {//do nothing
                    } else {
                        cact.setCellEditor(rowindex, new DateSpinnerCellEditor());
                    }
                }
                if (cell.contentEquals("Moongate")) {
                    if (cact.getCellEditor(rowindex, 2) instanceof MoongateCellEditor) {
                        //do nothing
                    } else {
                        cact.setCellEditor(rowindex, new MoongateCellEditor(moongatecombo));
                    }
                }
                if (cell.contentEquals("Pryce")) {
                    if (cact.getCellEditor(rowindex, 2) instanceof PryceCellEditor) {
                        //do nothing
                    } else {
                        cact.setCellEditor(rowindex, new PryceCellEditor(prycecombo));
                    }
                }
                if (cell.contentEquals("Rua")) {
                    if (cact.getCellEditor(rowindex, 2) instanceof RuaCellEditor) {
                        //do nothing
                    } else {
                        cact.setCellEditor(rowindex, new RuaCellEditor(ruacombo));
                    }
                }
            }
        }
    }

    //Copyright 2003 Internet.com All rights reserved. Reprinted with permission from http://www.internet.com.
    //http://www.developer.com/java/other/article.php/2173111/Java-Sound-Playing-Back-Audio-Files-using-Java.htm
    public void playAlarm()
    {
        try
        {
            File soundfile = new File("knocking.wav");
            ais = AudioSystem.getAudioInputStream(soundfile);
            audioformat = ais.getFormat();
            DataLine.Info dataLineInfo =
                          new DataLine.Info(
                            SourceDataLine.class,
                                    audioformat);
            sourceDataLine =
             (SourceDataLine)AudioSystem.getLine(
                                   dataLineInfo);
            new PlayThread().start();

        }
        catch (LineUnavailableException ex) {
            Logger.getLogger(CasterMainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }        catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(CasterMainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }        catch (IOException ex) {
            Logger.getLogger(CasterMainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //=============================================//
//Inner class to play back the data from the
// audio file.
// Code from http://www.developer.com/java/other/article.php/2173111/Java-Sound-Playing-Back-Audio-Files-using-Java.htm
// What, you expect me to pull out how to do audio operations in Java out of a hat?
// Copyright 2003 Internet.com All rights reserved. Reprinted with permission from http://www.internet.com.
class PlayThread extends Thread{
  byte tempBuffer[] = new byte[10000];

  public void run(){
    try{
      sourceDataLine.open(audioformat);
      sourceDataLine.start();

      int cnt;
      while((cnt = ais.read(
           tempBuffer,0,tempBuffer.length)) != -1){
        if(cnt > 0){
          sourceDataLine.write(
                             tempBuffer, 0, cnt);
        }//end if
      }//end while
      //Block and wait for internal buffer of the
      // data line to empty.
      sourceDataLine.drain();
      sourceDataLine.close();
    }catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }//end catch
  }//end run
}//end inner class PlayThread
//===================================//

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable AlarmClockTable;
    private javax.swing.JLabel ErinnHourLabel;
    private javax.swing.JLabel ErinnMinuteLabel;
    private javax.swing.JMenuItem ExitMenuItem;
    private javax.swing.JMenu FileMenu;
    private javax.swing.JMenuItem NewWindowMenuItem;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
