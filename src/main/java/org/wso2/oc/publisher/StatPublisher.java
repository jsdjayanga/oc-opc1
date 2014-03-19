package org.wso2.oc.publisher;

import org.wso2.carbon.server.admin.common.IServerAdmin;
import org.wso2.carbon.server.admin.service.ServerAdmin;
import org.wso2.oc.connection.OCClient;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;
import java.util.TimerTask;

/**
 * Created by jayanga on 3/11/14.
 */
public class StatPublisher extends TimerTask {

    IServerAdmin serverAdmin;
    OCClient ocClient;

    long prevTime = 0;

    public StatPublisher(IServerAdmin serverAdmin){
        this.serverAdmin = serverAdmin;
        ocClient = null;
    }

    @Override
    public void run() {
        if (serverAdmin != null){

            System.out.println("===========running...");
            try {

                if (ocClient == null){
                    ocClient = new OCClient(serverAdmin);
                }

                System.out.println("=====================ServerData:" + ((ServerAdmin)serverAdmin).getServerDataAsString());
                System.out.println("=====================ServerStatus:" + ((ServerAdmin)serverAdmin).getServerStatus());

                System.out.println("=====================Carbon Version:" + serverAdmin.getServerData().getCarbonVersion());
                System.out.println("=====================Up time : "
                        + " Days:" + serverAdmin.getServerData().getServerUpTime().getDays()
                        + ", Hours" + serverAdmin.getServerData().getServerUpTime().getHours()
                        + ", Minutes" + serverAdmin.getServerData().getServerUpTime().getMinutes()
                        + ", Seconds" + serverAdmin.getServerData().getServerUpTime().getSeconds());
                System.out.println("=====================Server IP:" + serverAdmin.getServerData().getServerIp());
                System.out.println("=====================Server Name:" + serverAdmin.getServerData().getServerName());
                System.out.println("=====================Repo Location:" + serverAdmin.getServerData().getRepoLocation());


                Runtime runtime = Runtime.getRuntime();
                System.out.println("=====================Mem==FreeMemory=" + runtime.freeMemory()
                        + ", totalmem=" + runtime.totalMemory()
                        + ", maxmem=" + runtime.maxMemory()
                        + ", available proceses=" + runtime.availableProcessors());

                OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
                System.out.println("=====================OS==Name=" + operatingSystemMXBean.getName()
                        + ", arch=" + operatingSystemMXBean.getArch()
                        + ", aval pro=" + operatingSystemMXBean.getAvailableProcessors()
                        + ", load average=" + operatingSystemMXBean.getSystemLoadAverage()
                        + ", version=" + operatingSystemMXBean.getVersion());

                ThreadMXBean threadMXBean  = ManagementFactory.getThreadMXBean();
                long[] thearIds  = threadMXBean.getAllThreadIds();
                long cpuTime = 0;

                System.out.print("=====================CPU===");

                for (long i : thearIds){
                    System.out.print("[" + i +":" + threadMXBean.getThreadCpuTime(i) + "]");

                    cpuTime += threadMXBean.getThreadCpuTime(i);
                }

                System.out.println(" All:" + cpuTime);

                if (ocClient != null) {
                    String data = "{\"statistics\": [{ \"servername\":\"" + serverAdmin.getServerData().getServerName() + "-" + OCClient.id
                                               + "\" , \"status\":\"" + ((ServerAdmin)serverAdmin).getServerStatus()
                                               + "\" , \"seconds\":\"" + serverAdmin.getServerData().getServerUpTime().getSeconds()
                                               + "\" },{ \"firstName\":\"Anna\" , \"lastName\":\"Smith\" },{ \"firstName\":\"Peter\" , \"lastName\":\"Jones\" }]}";
                    ocClient.sendUpdate(data);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
