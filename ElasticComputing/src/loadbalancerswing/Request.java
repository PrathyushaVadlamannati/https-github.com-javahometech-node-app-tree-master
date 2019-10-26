
package loadbalancerswing;

/**
 *
 * @author prathyusha
 */
public class Request {
    
    private int requestId;
    private int processTime = 3000;   
    private static int countOfReq = 0;
    private long requestTime;
    private boolean processsed = false;
    private VM vm;
    
    //Default constructor to update the request Id
    public Request() {
        countOfReq ++;
        this.requestId = countOfReq;
        this.requestTime = System.currentTimeMillis();
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getProcessTime() {
        return processTime;
    }

    public void setProcessTime(int processTime) {
        this.processTime = processTime;
    }

    public static int getCountOfReq() {
        return countOfReq;
    }

    public static void setCountOfReq(int countOfReq) {
        Request.countOfReq = countOfReq;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public boolean isProcesssed() {
        return processsed;
    }

    public void setProcesssed(boolean processsed) {
        this.processsed = processsed;
    }

    public VM getVm() {
        return vm;
    }

    public void setVm(VM vm) {
        this.vm = vm;
    }    
    
}
