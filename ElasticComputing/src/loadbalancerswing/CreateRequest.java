
package loadbalancerswing;

/**
 *
 * @author prathyusha
 */
public class CreateRequest implements Runnable {

    int processingTime = 100;
    boolean stopCreation = false;
    boolean pauseCreation = false;
    int ReqArrivalRate = 0;

    public int getReqArrivalRate() {
        return ReqArrivalRate;
    }

    public void setReqArrivalRate(int noOfReqPerSec) {
        this.ReqArrivalRate = noOfReqPerSec;
    }

    @Override
    public void run() {
        stopCreation = false;
        while (!stopCreation) {
            Request req = new Request();
            req.setProcessTime(getProcessingTime());
            LoadBalanceAndAutoScale.getInstance().queueRequest(req);
            try {
                Thread.sleep(ReqArrivalRate);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("stopped here");

    }

    public int getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(int processingTime) {
        this.processingTime = processingTime;
    }

    synchronized public void stopMethod() {
        stopCreation = true;
    }

}
