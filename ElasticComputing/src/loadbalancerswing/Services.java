
package loadbalancerswing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author prathyusha
 */
public class Services {

    private static Services services; // creates its own instance
    static List<VM> vmmachines = new ArrayList<VM>(); // starts with 5 Servers
    private static Thread[] machinethread = new Thread[5];
    private static int[] serverSizes = new int[vmmachines.size()];
    private int reqInProcCount = 0;
    private int reqProcessedCount = 0;
    private int serversInProccess = 0;
    private int reqProccessingTime = 0;
    private long StorePreviousProccessTime = 0; //previous 100 requests proccess time
    private long AvgProccessTime = 0;
    private int sizeofmcQ;
    private int vmID = 1;
    private static Map<Integer, Integer> VMDetailsMap = new HashMap<>();

    //Set the serverid to the new server
    public Services() {
        for (int i = 0; i < 5; i++) {
            VM v = new VM();
            v.setVmId(i);
            vmmachines.add(i, v);
        }
    }

    public static Services getInstance() {
        if (services == null) {
            services = new Services();
        }
        return services;
    }
    
// This method is used to check if the virtual machine is in use, if so create new virtual machine 
    public VM availableVM() {
        if (!vmmachines.get(0).isInUse()) {

            useVM(vmmachines.get(0));
            machinethread[0] = new Thread(vmmachines.get(0));
            machinethread[0].start();
            return vmmachines.get(0);
        } else if (vmmachines.get(0).getProcessingQ().size() != sizeofmcQ) {

            return vmmachines.get(0);
        }
        for (int i = 1; i < vmmachines.size(); i++) {
            if (vmmachines.get(i).isInUse()) {
                if (vmmachines.get(i).getProcessingQ().size() != sizeofmcQ) {

                    return vmmachines.get(i);
                }
            } else {

                useVM(vmmachines.get(i));
                machinethread[i] = new Thread(vmmachines.get(i));
                machinethread[i].start();
                return vmmachines.get(i);
            }
        }

        return null;
    }

    public void useVM(VM vm) {
        //vm.setVmId(vmID);
        vm.setInUse(true);
        serversInProccess++;
        //vmID++;
    }

    public void freeVM(VM vm) {
        if (serversInProccess > 1) {
            serversInProccess--;
            vm.setInUse(false);
        }
    }

    // This method is used to add extra virtual machines - autoscaling
    public void addVm(int extraServer) {

            while (extraServer != 0) {
                VM v = new VM();
                v.setVmId(vmmachines.size());
                vmmachines.add(v);
                useVM(vmmachines.get(vmmachines.size() - 1));
                Thread t = new Thread(vmmachines.get(vmmachines.size() - 1));
                t.start();
                extraServer--;

            }

        
    }

    public static Services getServices() {
        return services;
    }

    public static void setServices(Services services) {
        Services.services = services;
    }

    public static List<VM> getVmmachines() {
        return vmmachines;
    }

    public static void setVmmachines(List<VM> vmmachines) {
        Services.vmmachines = vmmachines;
    }

    public static Thread[] getMachinethread() {
        return machinethread;
    }

    public static void setMachinethread(Thread[] machinethread) {
        Services.machinethread = machinethread;
    }

    public int vmmachineSize() {
        return vmmachines.size();
    }

    public Map<Integer, Integer> getServerDetails() {
        for (int i = 0; i < vmmachines.size(); i++) {
            VMDetailsMap.put(vmmachines.get(i).getVmId(), vmmachines.get(i).getProcessingQ().size());
            System.out.println("vmmachines"+ vmmachines.get(i).getVmId()+ " | " +  vmmachines.get(i).getProcessingQ().size());
           
        }

        return VMDetailsMap;
    }

    public int getReqInProcessCount() {
        reqInProcCount = 0;
        for (int i = 0; i < vmmachines.size(); i++) {
            reqInProcCount += vmmachines.get(i).getProcessingQ().size();
        }

        return reqInProcCount;
    }

    public void setReqInProcessCount(int reqInProcCount) {
        this.reqInProcCount = reqInProcCount;
    }

    public int getReqprocessedCount() {
        return reqProcessedCount;
    }

    public void setReqprocessedCount(int reqprocessedCount) {
        this.reqProcessedCount = reqprocessedCount;
    }

    public int getServersInProccess() {
        return serversInProccess;
    }

    public void setServersInProccess(int serversInProccess) {
        this.serversInProccess = serversInProccess;
    }

    public int getReqProccessingTime() {
        return reqProccessingTime;
    }

    public void setReqProccessingTime(int proccessingTime) {
        this.reqProccessingTime = proccessingTime;
    }

    public long getStorePreviousProccessTime() {
        return StorePreviousProccessTime;
    }

    public void setStorePreviousProccessTime(long StorePreviousProccessTime) {
        this.StorePreviousProccessTime = StorePreviousProccessTime;
    }

    public long getAvgProccessTime() {
        return AvgProccessTime;
    }

    public void setAvgProccessTime(long AvgProccessTime) {
        this.AvgProccessTime = AvgProccessTime;
    }

    public int getSizeofmcQ() {
        return sizeofmcQ;
    }

    public void setSizeofmcQ(int sizeofmcQ) {
        this.sizeofmcQ = sizeofmcQ;
    }

    public void clearAllQueue() {
        for (int i = 0; i < vmmachines.size(); i++) {
            VM mc = vmmachines.get(i);
            mc.getProcessingQ().clear();
            mc = null;
        }
    }
}
