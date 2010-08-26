    /*
     * Bjorn Carandang
     * bac37@drexel.edu
     * CS338:GUI, Assignment [P3]
     */

package root;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/*
 * Represents the JComboBox containing the valid moongate types given the
 * Properties file for the AlarmClockTable.
 */
public class MoongateCellEditor extends AbstractCellEditor implements TableCellEditor{

    JComboBox mygatebox;

    public MoongateCellEditor(JComboBox jcb)
    {
        mygatebox = jcb;
    }

    @Override
    public Object getCellEditorValue() {
        return mygatebox.getSelectedItem();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return mygatebox;
    }

    

}
