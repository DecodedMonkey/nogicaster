/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package root;

import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Letty
 */
public class CasterTableDataModel extends DefaultTableModel
{
    
    String[] enabledalarms;
    public CasterTableDataModel (CasterDataModel CDM)
    {
        enabledalarms = CDM.enabledAlarms;
        String[] in = {"Name", "Condition", "Requirement"};
        this.setColumnIdentifiers(in);
        

    }


}
