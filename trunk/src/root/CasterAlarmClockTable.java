    /*
     * Bjorn Carandang
     * bac37@drexel.edu
     * CS338:GUI, Assignment [P3]
     */

package root;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellEditor;

/**
 *  Inspired by http://www.javaworld.com/javaworld/javatips/jw-javatip102.html.
 *  Trying to get special editors for each row, depending on the preceding
 *  cell's value. 
 */
public class CasterAlarmClockTable extends JTable {

    CasterAlarmClockRowEditor CACRE = null;

    public CasterAlarmClockTable( CasterAlarmClockRowEditor CACRE) {
        this.CACRE = CACRE;
    }

    @Override
    public boolean isCellEditable(int row, int col)
    {
        return true;
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        if (CACRE != null) {
            if (e.getType() == TableModelEvent.DELETE
                    && e.getColumn() == e.ALL_COLUMNS) //row removal
            {
                CACRE.removeEditorOnRow(e.getFirstRow());
            }
        }
        super.tableChanged(e);
    }

    //When the user defines an Editor type (Price, Rua, etc) we get and then
    //stand up the Editor.
    public void setCellEditor(int row, TableCellEditor tce)
    {
        CACRE.addEditorOnRow(row, tce);
    }

    //So say the user selects the third cell in a row. We need to stand up
    //the associated Editor ASAP.
    @Override
    public TableCellEditor getCellEditor(int row, int col) {
        if (this.CACRE == null || col != 2) {
            return super.getCellEditor(row, col);
        } else {
            TableCellEditor tce = CACRE.getEditorOnRow(row);
            if (tce != null) {
                return tce;
            } else {
                return super.getCellEditor(row, col);
            }
        }
    }
}
