package yutong.tools;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

public class Allow {

    public static boolean allow() {
        Process p1 = null;
        try {
            p1 = java.lang.Runtime.getRuntime().exec("ping -n 1 www.google.com");
        } catch (IOException ex) {
        }
        int returnVal = 0;
        try {
            returnVal = p1.waitFor();
        } catch (InterruptedException ex) {
        }
        boolean reachable = (returnVal == 0);

        if (!reachable) {
            System.exit(0);
        }

        String TIME_SERVER = "time-a.nist.gov";
        NTPUDPClient timeClient = new NTPUDPClient();
        InetAddress inetAddress = null;

        try {
            inetAddress = InetAddress.getByName(TIME_SERVER);
        } catch (java.net.UnknownHostException ex) {
            Logger.getLogger(Allow.class.getName()).log(Level.SEVERE, null, ex);
        }

        TimeInfo timeInfo = null;
        try {
            timeInfo = timeClient.getTime(inetAddress);
        } catch (IOException ex) {
            return false;
        }

        long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
        Date time = new Date(returnTime);

        Calendar cal = Calendar.getInstance();
        cal.setTime(time);

        return (cal.get(Calendar.YEAR) == 2022 && cal.get(Calendar.MONTH) == 4 && cal.get(Calendar.DATE) < 25);
    }

}
