/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package root;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Letty
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
