package org.wso2.oc.publisher;

import org.apache.abdera.i18n.text.CodepointIterator;
import org.wso2.carbon.server.admin.common.IServerAdmin;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;
import java.util.TimerTask;

/**
 * Created by jayanga on 3/11/14.
 */
public class StatPublisher extends TimerTask {

    IServerAdmin serverAdmin;

    long prevTime = 0;

    public StatPublisher(IServerAdmin serverAdmin){
        this.serverAdmin = serverAdmin;
    }

    @Override
    public void run() {
        if (serverAdmin != null){

            System.out.println("===========running...");
            try {
                System.out.println("=====================+:" + serverAdmin.getServerData().getCarbonVersion());
                System.out.println("=====================+:" + serverAdmin.getServerData().getServerUpTime().getSeconds());
                System.out.println("=====================+:" + serverAdmin.getServerData().getServerIp());
                System.out.println("=====================+:" + serverAdmin.getServerData().getServerName());
                System.out.println("=====================+:" + serverAdmin.getServerData().getRepoLocation());


                Runtime runtime = Runtime.getRuntime();
                System.out.println("\"=====================Mem==freemem=" + runtime.freeMemory()
                        + ", totalmem=" + runtime.totalMemory()
                        + ", maxmem=" + runtime.maxMemory()
                        + ", available proceses=" + runtime.availableProcessors());

                OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
                System.out.println("=====================OS==name=" + operatingSystemMXBean.getName()
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

                System.out.print(" All:" + cpuTime);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
