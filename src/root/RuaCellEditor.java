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
 * Takes a ComboBox containing legal states for Rua (sleeping, awake/working) and
 * offers it to the AlarmClock as an editor.
 */
public class RuaCellEditor extends AbstractCellEditor implements TableCellEditor{

    JComboBox myRuabox;

    public RuaCellEditor(JComboBox jcb)
    {
        myRuabox = jcb;
    }

    @Override
    public Object getCellEditorValue() {
        return myRuabox.getSelectedItem();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return myRuabox;
    }



}
