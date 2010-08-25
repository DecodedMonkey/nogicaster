package root;

/*
 * Class manages all of the actual time shenanigans.
 * Does not do any tomfoolery for Mabi time just yet.
 * 
 * We refer to "IRL time" as the local time of the user.
 * We refer to "server time" as PST/PDT, depending on DST.
 * We refer to "Erinn time" as the game time, as converted
 * from the server time.
 * 
 * A few notes about how time works for the application:
 * 1. We work in milliseconds. I'm following Kakurady's
 * code for clock, and I do not plan to deviate from it.
 * 2. Nexon's servers are located in some bunker in
 * California. As such, all times are in PST e.g. the day
 * changes at 0000 PST and 0300 EST. PST is -8.
 * 3. Erinn runs on a 90-second hour, starting from
 * their own original epoch. Other than that, time is
 * the same as IRL: 24 "hours", 60 "minutes" to an "hour".
 */

import java.util.Date;
import java.util.TimeZone;

public class CasterClock {
	
	TimeZone currentZone;
        int offset;
	
	//Sets up the local clocking mechanism.
	//Will draw out the current offset from GMT.
	//We can use this to figure out the offest
	//from Nexon's servers, which run on PST (-8, I think).
	public CasterClock(int offset)
	{
		currentZone = TimeZone.getDefault();
                this.offset = offset;
	}
	
	public long getEpochTime()
	{
		return System.currentTimeMillis();
	}
	
	
	//Returns the current time that Nexon runs on (PST).
	//Will take into account DST coorections, but only for the US.
        //Granted, this code is tuned for Nexon NA.
	public long getServerTime()
	{
		boolean is_DST = TimeZone.getDefault().inDaylightTime(new Date());
		
		if(is_DST)
		{
			return getEpochTime() + CstStatic.serverOffset + CstStatic.dstOffset; //-8 + 1
		}
		else
		{
			return getEpochTime() + CstStatic.serverOffset;
		}
	}

        public long getOffsetTime()
    {
            return getServerTime() + offset * CstStatic.ERINN_MINUTE;

        }
	
	public long getLocalTime()
	{
		return getEpochTime() + currentZone.getOffset(getEpochTime());
	}

        public int getServerHour()
        {
            return (int) (java.lang.Math.floor(new Double(getServerTime() / CstStatic.IRL_HOUR).doubleValue()) % 24);
        }

        public int getServerMinute()
        {
            return (int) (java.lang.Math.floor(new Double(getServerTime() / CstStatic.IRL_MINUTE).doubleValue())% 60);
        }

        public int getServerSecond()
    {
            return (int) (java.lang.Math.floor(new Double(getServerTime() / CstStatic.IRL_SECOND).doubleValue())% 60);
        }
	
	public int getErinnHour()
	{
		return (int) (java.lang.Math.floor(new Double(getOffsetTime() / CstStatic.ERINN_HOUR).doubleValue()) % 24);
	}
	
	public int getErinnMinute()
	{
		return (int) (java.lang.Math.floor(new Double(getOffsetTime() / CstStatic.ERINN_MINUTE).doubleValue()) % 60);
	}

        public int getLocalHour()
        {
            return (int) (java.lang.Math.floor(new Double(getLocalTime() / CstStatic.IRL_HOUR).doubleValue()) % 24);
        }

        public int getLocalMinute()
	{
		return (int) (java.lang.Math.floor(new Double(getLocalTime() / CstStatic.IRL_MINUTE).doubleValue()) % 60);
	}

}
