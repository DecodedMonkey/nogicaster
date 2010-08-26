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
 * Takes a JComboBox containing legal Pryce locations, and offers it as a CellEditor.
 */
public class PryceCellEditor extends AbstractCellEditor implements TableCellEditor{

    JComboBox myprycebox;

    public PryceCellEditor(JComboBox jcb)
    {
        myprycebox = jcb;
    }

    @Override
    public Object getCellEditorValue() {
        return myprycebox.getSelectedItem();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return myprycebox;
    }



}
