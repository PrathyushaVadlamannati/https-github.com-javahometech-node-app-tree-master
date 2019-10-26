
package loadbalancerswing;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author prathyusha
 */
public class VM implements Runnable {

    Queue<Request> processingQ;
    int vmId;
    private boolean inUse;

    public VM() {
        this.processingQ = new LinkedList<>();
        this.inUse = inUse;

    }

    public Queue<Request> getProcessingQ() {
        return processingQ;
    }

    public void setProcessingQ(Queue<Request> processingQ) {
        this.processingQ = processingQ;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    @Override
    public void run() {
        while (isInUse()) {
            if (getProcessingQ().isEmpty()) {
                //System.out.println("Server Q empty");
            } else {
                Request request = getProcessingQ().peek();
                if (request != null) {
                    processCurrentRequest(request);
                }
            }
        }
    }

    public int getVmId() {
        return vmId;
    }

    public void setVmId(int vmId) {
        this.vmId = vmId;
    }

    // This method is used to process the requests inside the server queue
    public void processCurrentRequest(Request request) {
        try {
            Thread.sleep(Services.getInstance().getReqProccessingTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        request.setProcesssed(true);
        DequeReq(request);
    }

  // This method is used to dequeue request from the server and calculate the average processing time of the request
    public void DequeReq(Request request) {
        Request req = request.getVm().getProcessingQ().poll();
        Services service = Services.getInstance();
        service.setReqInProcessCount(service.getReqInProcessCount() - 1);
        service.setReqprocessedCount(service.getReqprocessedCount() + 1);

        if (request.getVm().getProcessingQ().isEmpty()) {
            service.freeVM(request.getVm());
        }

        if (req != null) {
            long calculated_time = System.currentTimeMillis() - req.getRequestTime();
            long avgProcessingTime = service.getStorePreviousProccessTime();
            int ReqProccessedCount = service.getReqprocessedCount();

            if (ReqProccessedCount > 100) {
                service.setStorePreviousProccessTime(avgProcessingTime - service.getAvgProccessTime() + calculated_time);
                service.setAvgProccessTime(service.getStorePreviousProccessTime() / 100);
            } else {
                service.setStorePreviousProccessTime(avgProcessingTime + calculated_time);
                service.setAvgProccessTime(service.getStorePreviousProccessTime() / ReqProccessedCount);
            }
        }
    }

}
