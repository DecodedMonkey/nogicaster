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
