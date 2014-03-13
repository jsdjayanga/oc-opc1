package org.wso2.oc.receiver;


import org.apache.axiom.om.OMElement;
import org.wso2.carbon.core.AbstractAdmin;
import org.wso2.carbon.server.admin.common.IServerAdmin;
import org.wso2.oc.TestOCServiceComponent;

/**
 * Created by jayanga on 3/11/14.
 */
public class OCAgentServer extends AbstractAdmin {

    public boolean forcefulRestart(OMElement element){

        System.out.println("=======F=restarting=============:" + getUsername());

        IServerAdmin serverAdmin = TestOCServiceComponent.getServerAdmin();
        if (serverAdmin != null){
            try {
                serverAdmin.restart();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public boolean gracefulRestart(OMElement element){

        System.out.println("======G==restarting=============:" + getUsername());

        IServerAdmin serverAdmin = TestOCServiceComponent.getServerAdmin();
        if (serverAdmin != null){
            try {
                serverAdmin.restartGracefully();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public boolean forcefulShutdown(OMElement element){

        System.out.println("=======F=shutdown=============:" + getUsername());

        IServerAdmin serverAdmin = TestOCServiceComponent.getServerAdmin();
        if (serverAdmin != null){
            try {
                serverAdmin.shutdown();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public boolean gracefulShutdown(OMElement element){

        System.out.println("=======G=shutdown=============:" + getUsername());

        IServerAdmin serverAdmin = TestOCServiceComponent.getServerAdmin();
        if (serverAdmin != null){
            try {
                serverAdmin.shutdownGracefully();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

}
