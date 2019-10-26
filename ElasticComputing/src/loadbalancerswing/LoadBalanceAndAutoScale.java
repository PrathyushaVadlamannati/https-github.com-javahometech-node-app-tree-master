
package loadbalancerswing;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author prathyusha
 */
public class LoadBalanceAndAutoScale {

   
    private int ReqDispatchingBreak = 3; 
    private Timer ReqDispatchingTimer = new Timer();
    private int ServerReqQueueSize;
    private static int inputReqQSize = 50;
    private static LoadBalanceAndAutoScale lbas = null;
    public static Queue<Request> inReq = new LinkedList<>();

    public int getServerReqQueueSize() {
        return ServerReqQueueSize;
    }

    public void setServerReqQueueSize(int ServerReqProcessingRate) {
        this.ServerReqQueueSize = ServerReqProcessingRate;
    }

    private LoadBalanceAndAutoScale() {

    }

    // This method is used to dispatch the requests to the servers queue periodically
    public void startReqDispatch() {
        inReq.clear();
        ReqDispatchingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!inReq.isEmpty()) {
                    loadRequest();
                }
            }
        }, ReqDispatchingBreak, ReqDispatchingBreak);
    }

    public void stopLoadingRequests() {
        ReqDispatchingTimer.cancel();
        ReqDispatchingTimer = null;
        ReqDispatchingTimer = new Timer();
    }

    public static LoadBalanceAndAutoScale getInstance() {
        if (lbas == null) {
            lbas = new LoadBalanceAndAutoScale();
        }
        return lbas;
    }

    public void queueRequest(Request request) {
        if (inReq.size() == 0) {
            inReq.add(request);
        } else if (inReq.size() < inputReqQSize) {
            inReq.add(request);
        } else {
            System.out.println("The queue is full");
        }

    }

    // This method is used to load the requests to the available servers if not add extra servers 
    public void loadRequest() {
        if (!inReq.isEmpty()) {
            VM vm = Services.getInstance().availableVM();
            if (vm == null) {
                int extraServer = howManyServer(inReq.size(), ServerReqQueueSize);
                Services.getInstance().addVm(extraServer);

            } else {
                Request ReqObj = inReq.poll();
                ReqObj.setVm(vm);
                vm.getProcessingQ().add(ReqObj);

            }
        }
    }

    public static int howManyServer(int inReqsize, int ServerReqQueueSize) {
        
        int threshold = (inReqsize * 100 / inputReqQSize);
        if (threshold > 80) {
            return 1;
        } else {
            return 0;
        }

    }

}
