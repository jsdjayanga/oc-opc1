package org.wso2.oc;

/**
 * Created by jayanga on 3/10/14.
 */

import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.server.admin.common.IServerAdmin;
import org.wso2.oc.publisher.StatPublisher;

import java.util.Timer;
import java.util.TimerTask;


/**
 * The Declarative Service Component for Hello Service
 *
 * @scr.component name="org.wso2.oc.TestOCServiceComponent"
 * immediate="true"
 * @scr.reference name="org.wso2.carbon.server.admin.common"
 * interface="org.wso2.carbon.server.admin.common.IServerAdmin"
 * cardinality="1..1" policy="dynamic"
 * bind="setAdminService"
 * unbind="unsetAdminService"
 *
 */
public class TestOCServiceComponent {

    static IServerAdmin serverAdmin;
    Timer timer;

    protected void activate(ComponentContext ctxt) {
        System.out.println("TestOCServiceComponent is Activated");

        TimerTask tasknew = new StatPublisher(serverAdmin);
        timer = new Timer();
        timer.schedule(tasknew, 100, 5000);

        System.out.println("TestOCServiceComponent Other work");
    }

    protected void deactivate(ComponentContext ctxt) {
        System.out.println("TestOCServiceComponent is Deactivated");
        timer.cancel();
        timer = null;
    }

    protected void setAdminService(IServerAdmin serverAdmin) {
        System.out.println("Acquiring HelloService");
        this.serverAdmin = serverAdmin;
    }

    protected void unsetAdminService(IServerAdmin serverAdmin) {
        System.out.println("Releasing HelloService");
        this.serverAdmin = null;
    }

    public static IServerAdmin getServerAdmin(){
        if (serverAdmin != null){
            return serverAdmin;
        }else{
            System.out.println("============server admin is not yet ready=============");
        }

        return null;
    }
}
