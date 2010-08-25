/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package root;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Letty
 */
public class CasterDefaultPropertyMaker {

    static String dirseperator = File.separator;
    static long G11epoch = 1201126860000L; //default epoch, applies to Mabinogi G11
    static long G11priceepoch = 1201132800000L; //default epoch for MabiG11

    public static void main(String[] args) {
        FileOutputStream fis = null;
        try {
            Properties prop = new Properties();

            fis = new FileOutputStream("." + dirseperator + "properties.txt");
            prop.put("gateepoch", new Long(G11epoch).toString());
            prop.put("priceepoch", new Long(G11priceepoch).toString());
            prop.put("enabledtools", Arrays.asList(new String[]{"Moongate Tool", "Pryce Tool", "Rua Tool", "Farm Tool"}).toString());
            prop.put("enabledalarms", Arrays.asList(new String[]{"Moongate", "Pryce", "Rua"}).toString());
            prop.put("moongateorder", Arrays.asList(new Integer[] {
                        5, 6, 0, 4, 3, 5, 1, 7, 2, 3, 5, 0,
                        4, 6, 3, 5, 2, 1, 7, 5, 4, 0, 1, 2}).toString());
            prop.put("priceorder", Arrays.asList(new Integer[] {
                0,1,2,3,4,5,6,7,8,9,10,11,12,1
            }).toString());
            prop.put("ruaorder", Arrays.asList(new Integer[] {
                0,0,0,1,0,0,0,0,1,0,0,1,0,1,1,0,1,1,1,0,0,0,0,0,0,0,1,0,1,0,0,1,0,0,0,1,0,0,0,1,0,0,1
            }).toString());
            prop.put("moongatenames", Arrays.asList(new String[]{
                "Tir Chonaill", "Dunbarton", "Bangor", "Emain Macha", "Taillteann", "Tara", "Ceo", "Port Ceann"
            }).toString());
            prop.put("pricenames", Arrays.asList(new String[]{
                "Outside Tir Chonaill Inn","Dugald Aisle Logging Camp Hut","Dunbarton East Potato Field",
                "Dragon Ruins - House at 5`o clock ","Bangor Bar","Sen Mag 5th house from West","Emain Macha - Alley Behind Weapon Shop",
                "Ceo island","Emain Macha - Island in South Pathway","Sen Mag 5th house from West","Dragon Ruins - House at 5`o clock",
                "Outside Barri Dungeon","Dunbarton School Stairway"
            }).toString());

            //prop.put("")
            prop.store(fis, null);
        } catch (FileNotFoundException exe) {
            Logger.getLogger(CasterDefaultPropertyMaker.class.getName()).log(Level.SEVERE, null, exe);
        } catch (IOException ex) {
            Logger.getLogger(CasterDefaultPropertyMaker.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(CasterDefaultPropertyMaker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
