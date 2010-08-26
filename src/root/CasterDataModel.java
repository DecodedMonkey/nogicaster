    /*
     * Bjorn Carandang
     * bac37@drexel.edu
     * CS338:GUI, Assignment [P3]
     */
package root;

import java.util.*;
import javax.swing.table.*;

/*
 * Manages most of the special calculations that we need to do.
 * Can handle a little bit of time operations, but isn't nearly
 * as fast on the uptake as the CasterClock, so repeated pings
 * result in stagnant data... for some reason. Better for calculating
 * the times for events.
 */

public class CasterDataModel {

    private Calendar localDateTime = Calendar.getInstance(TimeZone.getDefault()); //current time
    private Calendar serverDateTime = Calendar.getInstance(TimeZone.getTimeZone("GMT-7")); // server time
    private long timePerErinnHour = 90000; //1 min 30 s
    private long timePerErinnMinute = 1500; //1.5 s
    private long timePerErinnDay = 2160000; //36 min
    private int nGatesShown = 6;
    private int nPryceShown = 2;
    private int nRuaShown = 2;
//This value should be modified with regards to DST
    private long serverOffset = -6 * 60 * 60 * 1000 - (60 * 1000 * 60); //GMT - 7
    private long moonGateEpoch = 1201126860000L;// = DateFormat("Mar 23, 2008 22:21:00 GMT");
    private long pryceEpoch = 1201132800000L;// = Date.parse("Mar 24, 2008 00:00:00 GMT");
//List of Moongate locations
    private int[] moonGateList = {5, 6, 0, 4, 3, 5, 1, 7, 2, 3, 5, 0, 4, 6, 3, 5, 2, 1, 7, 5, 4, 0, 1, 2};
    private int[] pryceList = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 1};
    private int[] ruaList = {0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1};
    private String[] msg = {"Tir Chonaill", "Dunbarton", "Bangor", "Emain Macha", "Taillteann", "Tara", "Ceo", "Port Ceann"};
    private String[] pmsg = {"Outside Tir Chonaill Inn", "Dugald Aisle Logging Camp Hut", "Dunbarton East Potato Field", "Dragon Ruins - House at 5`o clock ", "Bangor Bar", "Sen Mag 5th house from West", "Emain Macha - Alley Behind Weapon Shop", "Ceo island", "Emain Macha - Island in South Pathway", "Sen Mag 5th house from West", "Dragon Ruins - House at 5`o clock", "Outside Barri Dungeon", "Dunbarton School Stairway"};
    private String[] rmsg = {"Resting", "Working"};
    private double[] pumpkinstats = {.99, .25, .35, .4};
    private double[] strawberrystats = {1.04, .4, .6, .4};
    private double[] tomatostats = {1.37, .1, .25, .4};
    private double[] eggplantstats = {.99, .15, .25, .4};
    private double[] cabbagestats = {1.11, .2, .3, .4};
    public String[] planttypes = {"Pumpkin", "Strawberry", "Tomato", "Eggplant", "Cabbage"};
    public String[] actiontypes = {"Water", "Fertilize", "Debug"};
    public String[] enabledActions = {"Moongate Tool", "Pryce Tool", "Rua Tool", "Farm Tool"};
    public String[] enabledAlarms = {"Moongate", "Pryce", "Rua"};

    public String GetGameTime() {
        return GetErinHour() + ((GetErinMinute() < 10) ? ":0" : ":") + GetErinMinute();
    }

    public long GetServerTicks() {
        return serverDateTime.getTimeInMillis() + serverOffset;
    }

// Formatted string (like how it appears on the website)
    public String GetLocalTime() {
        return localDateTime.get(Calendar.HOUR_OF_DAY) + ((localDateTime.get(Calendar.MINUTE) < 10) ? ":0" : ":") + localDateTime.get(Calendar.MINUTE);
    }

// Formatted string (like how it appears on the website)
    public String GetServerTime() {
        return serverDateTime.get(Calendar.HOUR_OF_DAY) + ((serverDateTime.get(Calendar.MINUTE) < 10) ? ":0" : ":") + serverDateTime.get(Calendar.MINUTE);
    }


    /**
     * Since for the most part we refresh the data model whenever we add/sub
     * a new row, I figured convenience functions to make us the TableModels
     * would make things easier.
     * 
     */
    public DefaultTableModel GetMoongateDefaultTableModel() {
        DefaultTableModel datamodel = new DefaultTableModel();

        datamodel.addColumn("Time");
        datamodel.addColumn("Destination");

        long time = GetServerTicks() - (long) (Math.floor(GetServerTicks() % timePerErinnDay)); //the server time for the current Erinn day's midnight
        time += ((GetErinHour() < 6) ? -6 * timePerErinnHour : 18 * timePerErinnHour) - serverOffset; //  so it's UTC again

        for (int i = 0; i < nGatesShown; time += timePerErinnDay, i++) {
            Date date = new Date(time); // so as not to worry about converting dates!
            Object[] data = {(date.getHours() + ((date.getMinutes() < 10) ? ":0" : ":") + date.getMinutes()), (msg[moonGateList[(GetMoongateTarget() + i) % moonGateList.length]])};
            datamodel.addRow(data);
        }


        return datamodel;
    }

    public DefaultTableModel GetPryceDefaultTableModel() {

        DefaultTableModel datamodel = new DefaultTableModel();

        datamodel.addColumn("Time");
        datamodel.addColumn("Destination");

        long time = GetServerTicks() - (long) (Math.floor(GetServerTicks() % timePerErinnDay)); //the server time for the current Erinn day's midnight
        time -= serverOffset;

        for (int i = 0; i < nPryceShown; time += timePerErinnDay, i++) {
            Date date = new Date(time); // so as not to worry about converting dates!
            Object[] data = {(date.getHours() + ((date.getMinutes() < 10) ? ":0" : ":") + date.getMinutes()), (pmsg[pryceList[(GetPryceTarget() + i) % pryceList.length]])};
            datamodel.addRow(data);
        }

        return datamodel;
    }

    public DefaultTableModel GetRuaDefaultTableModel() {
        DefaultTableModel datamodel = new DefaultTableModel();

        datamodel.addColumn("Time");
        datamodel.addColumn("Status");


        long time = GetServerTicks() - (long) (Math.floor(GetServerTicks() % timePerErinnDay)); //the server time for the current Erinn day's midnight
        time += ((GetErinHour() < 6) ? -6 * timePerErinnHour : 18 * timePerErinnHour) - serverOffset; //  so it's UTC again

        for (int i = 0; i < nRuaShown; time += timePerErinnDay, i++) {

            Date date = new Date(time); // so as not to worry about converting dates!
            Object[] data = {(date.getHours() + ((date.getMinutes() < 10) ? ":0" : ":") + date.getMinutes()), (rmsg[ruaList[(GetRuaTarget() + i) % ruaList.length]])};
            datamodel.addRow(data);
        }
        return datamodel;
    }

    private int GetErinHour() {
        return (int) ((long) (Math.floor(GetServerTicks() / timePerErinnHour)) % 24);
    }

    private int GetErinMinute() {
        return (int) ((long) (Math.floor(GetServerTicks() / timePerErinnMinute)) % 60);
    }

    private int GetMoongateTarget() {
        int target = (int) (Math.floor((GetServerTicks() - moonGateEpoch) / timePerErinnDay % moonGateList.length));

        if (target < 0) {
            target += moonGateList.length;
        }

        return target;

    }

    private int GetPryceTarget() {
        int target = (int) (Math.floor((GetServerTicks() - pryceEpoch) / timePerErinnDay % pryceList.length));

        if (target < 0) {
            target += pryceList.length;
        }

        return target;
    }

    private int GetRuaTarget() {
        int target = (int) (Math.floor((GetServerTicks() - moonGateEpoch) / timePerErinnDay % ruaList.length));

        if (target < 0) {
            target += ruaList.length;
        }

        return target;
    }

    public String GetFarmAction(int water, int fert, int bug, String croptype)
    {
        return actiontypes[GetFarmTarget(water, fert, bug, croptype)];
    }

    private int GetFarmTarget(int water, int fert, int bug, String croptype) {
        //{"Pumpkin", "Strawberry", "Tomato", "Eggplant", "Cabbage"};
        //{"Water", "Fertilize", "Debug"};
        double[] cropvars = null;
        if (croptype.contentEquals(planttypes[0])) {
            cropvars = this.pumpkinstats;
        }
        if (croptype.contentEquals(planttypes[1])) {
            cropvars = this.strawberrystats;
        }
        if (croptype.contentEquals(planttypes[2])) {
            cropvars = this.tomatostats;
        }
        if (croptype.contentEquals(planttypes[3])) {
            cropvars = this.eggplantstats;
        }
        if (croptype.contentEquals(planttypes[4])) {
            cropvars = this.cabbagestats;
        }

        double dehydration = cropvars[1] * (100 - water) / 100;
        double starvation = cropvars[2] * (100 - fert) / 100;
        double pestilence = cropvars[3] * (100 - bug) / 100;

        if (dehydration > starvation && dehydration > pestilence) {
            return 0;
        } else if (starvation > pestilence) {
            return 1;
        } else {
            return 2;
        }



    }

    public void SetNumberOfMoongatesShown(int num) {
        nGatesShown = num;
    }

    public void SetNumberOfRuaShown(int num) {
        nRuaShown = num;
    }

    public void SetNumberOfPryceShown(int num) {
        nPryceShown = num;
    }

    public int GetNumberOfMoongatesShown() {
        return nGatesShown;
    }

    public int GetNumberOfRuaShown() {
        return nRuaShown;
    }

    public int GetNumberOfPryceShown() {
        return nPryceShown;
    }

    public CasterDataModel() {
    }

    public CasterDataModel(Properties props) {
        if(this.localDateTime.getTimeZone().inDaylightTime(new Date()))
        this.serverDateTime = Calendar.getInstance(TimeZone.getTimeZone("GMT-7"));
        else Calendar.getInstance(TimeZone.getTimeZone("GMT-8")); //Dynamically take care of PST/PDT.

        this.moonGateEpoch = Long.parseLong(props.getProperty("gateepoch"));
        this.pryceEpoch = Long.parseLong(props.getProperty("priceepoch"));

        ArrayList<Integer> tempprycea = CstStatic.parseIntArray(props.getProperty("priceorder"));
        this.pryceList = new int[tempprycea.size()];
        for (int a = 0; a < tempprycea.size(); a++) {
            this.pryceList[a] = tempprycea.get(a);
        }

        ArrayList<Integer> tempgatea = CstStatic.parseIntArray(props.getProperty("moongateorder"));
        this.moonGateList = new int[tempgatea.size()];
        for (int a = 0; a < tempgatea.size(); a++) {
            this.moonGateList[a] = tempgatea.get(a);
        }

        ArrayList<Integer> tempruaa = CstStatic.parseIntArray(props.getProperty("ruaorder"));
        this.ruaList = new int[tempruaa.size()];
        for (int a = 0; a < tempruaa.size(); a++) {
            this.ruaList[a] = tempruaa.get(a);
        }

        this.msg = CstStatic.parseStrArray(props.getProperty("moongatenames")).toArray(new String[0]);
        this.pmsg = CstStatic.parseStrArray(props.getProperty("pricenames")).toArray(new String[0]);
        this.enabledActions = CstStatic.parseStrArray(props.getProperty("enabledtools")).toArray(new String[0]);
        this.enabledAlarms = CstStatic.parseStrArray(props.getProperty("enabledalarms")).toArray(new String[0]);
    }

    public String[] getMoongateList() {
        return msg;
    }

    public String[] getPryceList() {
        return pmsg;
    }

    public String[] getRuaList() {
        return rmsg;
    }

    public boolean isCurrentMoongate(String destination)
    {
        if(msg[moonGateList[(GetMoongateTarget()) % moonGateList.length]].contentEquals(destination))
        {
            return true;
        }
        return false;
    }

    public boolean isCurrentPryce(String destination)
    {
        if(pmsg[pryceList[(GetPryceTarget()) % pryceList.length]].contentEquals(destination))
        {
            return true;
        }
        return false;
    }

    public boolean isCurrentRua(String destination)
    {
        if(pmsg[pryceList[(GetRuaTarget()) % ruaList.length]].contentEquals(destination))
        {
            return true;
        }
        return false;
    }
}
