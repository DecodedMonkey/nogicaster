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
