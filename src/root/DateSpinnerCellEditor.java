/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package root;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Letty
 */
public class DateSpinnerCellEditor extends AbstractCellEditor implements TableCellEditor{
    private JSpinner timespinner;
    private SpinnerDateModel SDM;
    public DateSpinnerCellEditor()
    {
        SDM = new SpinnerDateModel();
        timespinner = new JSpinner(SDM);
        timespinner.setEditor(new JSpinner.DateEditor(timespinner,"HH:mm"));
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return timespinner;
    }

    @Override
    public Object getCellEditorValue() {
        Date date = SDM.getDate();
        return (date.getHours() + ((date.getMinutes() < 10) ? ":0" : ":") + date.getMinutes());
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

}
