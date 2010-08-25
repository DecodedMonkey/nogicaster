/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package root;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Letty
 */
public class CasterProperties {

    Properties internalprop;

    public CasterProperties(String filepath) throws IOException
    {
        FileInputStream fis = new FileInputStream(filepath);
        internalprop.load(fis);
        fis.close();
    }

    public String getProperty(String key)
    {
        return internalprop.getProperty(key, "");
    }

}
