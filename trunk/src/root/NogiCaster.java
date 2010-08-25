/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package root;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import root.TestClocks.TickAction;

/**
 *
 * @author Letty
 */
public class NogiCaster {

    int offset = 3;
    //ArrayList<String> EnabledTools = new ArrayList<String>(Arrays.asList(new String[]{"Moongate Tool"}));
    static String defaultlocation = "." + File.separator + "properties.txt";

    public NogiCaster(String propertiespath)
    {
        FileInputStream fis = null;
        try {
            Properties properties = new Properties();
            fis = new FileInputStream(propertiespath);
            properties.load(fis);
            fis.close();
            CasterClock c = new CasterClock(offset);
            CasterMainFrame cmf = new CasterMainFrame(c, properties);
            cmf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            cmf.setVisible(true);
            cmf.setLocation(300, 300);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NogiCaster.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NogiCaster.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(NogiCaster.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    public static void main(String args[])
    {
        NogiCaster n = new NogiCaster(defaultlocation);

    }

}
