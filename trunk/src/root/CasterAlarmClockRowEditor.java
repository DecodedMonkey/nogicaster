    /*
     * Bjorn Carandang
     * bac37@drexel.edu
     * CS338:GUI, Assignment [P3]
     */


package root;

import java.util.Hashtable;
import javax.swing.table.TableCellEditor;

/**
 * Inspired by http://www.javaworld.com/javaworld/javatips/jw-javatip102.html
 * Well, more like ripped, but this is for educational/nonprofit purposes,
 * which is fine under the site's TOS.
 *
 * This gets matched up with the CasterAlarmClockTable, and manages the special
 * editors that the 3rd column of the table instantiates.
 */
public class CasterAlarmClockRowEditor {

    private Hashtable data;

    //Constructor.
    //Initializes Hashtable of TableCellEditors.
    public CasterAlarmClockRowEditor()
    {
        data = new Hashtable();
    }

    //Associates an Editor with a row.
    public void addEditorOnRow(int row, TableCellEditor tce)
    {
        data.put(row, tce);
    }

    //Gets an editor from a row. Called when the user selects a cell
    //on the third column.
    public TableCellEditor getEditorOnRow(int row)
    {
        return (TableCellEditor) data.get(row);
    }

    //Called whenever we need to get of the Editor associated with the row.
    public void removeEditorOnRow(int row)
    {
        data.remove(row);
    }



}
