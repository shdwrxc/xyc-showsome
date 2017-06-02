package org.xyc.showsome.pecan.system;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

//import org.hyperic.sigar.CpuPerc;
//import org.hyperic.sigar.Sigar;
//import org.hyperic.sigar.SigarException;

/**
 * created by wks on date: 2017/3/20
 * depend on sigar
 */
public class LocalUtil {

    public static final String UNKNOWN_HOST = "UNKNOWN_HOST";

    public static String retrieveMac() {
        try {
            byte[] mac = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
            String macStr = "";
            for (int i = 0; i < mac.length; i++) {
                if (i > 0) {
                    macStr += "-";
                }
                int temp = mac[i] & 0xff;
                String str = Integer.toHexString(temp);
                while (str.length() < 2) {
                    str = "0" + str;
                }
                macStr += str;
            }
            return macStr;
        } catch (Exception e) {
            System.out.println(e);
        }
        return UNKNOWN_HOST;
    }

//    public static void output() throws SigarException {
//        System.out.println(System.getProperty("java.library.path"));
//        Sigar sigar = new Sigar();
//
//        org.hyperic.sigar.CpuInfo[] infos =
//                sigar.getCpuInfoList();
//
//        CpuPerc[] cpus =
//                sigar.getCpuPercList();
//
//        org.hyperic.sigar.CpuInfo info = infos[0];
//        long cacheSize = info.getCacheSize();
//        System.out.println("Vendor........." + info.getVendor());
//        System.out.println("Model.........." + info.getModel());
//        System.out.println("Mhz............" + info.getMhz());
//        System.out.println("Total CPUs....." + info.getTotalCores());
//        if ((info.getTotalCores() != info.getTotalSockets()) ||
//                (info.getCoresPerSocket() > info.getTotalCores()))
//        {
//            System.out.println("Physical CPUs.." + info.getTotalSockets());
//            System.out.println("Cores per CPU.." + info.getCoresPerSocket());
//        }
//
//        if (cacheSize != Sigar.FIELD_NOTIMPL) {
//            System.out.println("Cache size...." + cacheSize);
//        }
//        System.out.println("");
//
////        if (!this.displayTimes) {
////            return;
////        }
//
//        for (int i=0; i<cpus.length; i++) {
//            System.out.println("CPU " + i + ".........");
////            output(cpus[i]);
//        }
//
//        System.out.println("Totals........");
//        output(sigar.getCpuPerc());
//    }

    public static void retrieveIp() throws Exception {
        Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        while (allNetInterfaces.hasMoreElements())
        {
            NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
            System.out.println(netInterface.getName());
            Enumeration addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements())
            {
                ip = (InetAddress) addresses.nextElement();
                if (ip != null && ip instanceof Inet4Address)
                {
                    System.out.println("本机的IP = " + ip.getHostAddress());
                }
            }
        }

        System.out.println(InetAddress.getLocalHost().getHostAddress());//获得本机IP
    }

    public static void main(String[] args) throws Exception {
        System.out.println(retrieveMac());
        retrieveIp();
    }
}
