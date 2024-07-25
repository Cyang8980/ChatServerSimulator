import java.util.LinkedList;

public class TCBClient {
    public int nodeIDServer;
    public int portNumServer;
    public int nodeIDClient;
    public int portNumClient;
    public int stateClient;
    final static int CLOSED = 1;
    final static int SYNSENT = 2;
    final static int CONNECTED = 3;
    final static int FINWAIT = 4;
    int next_seqNum;
    LinkedList <SendBufferNode> sendBuffer; 
    int sendBufunSent;
    
    public TCBClient(int nodeIDServer, int portNumServer, int nodeIDClient, int portNumClient, int stateClient) {
        this.nodeIDServer = nodeIDServer;
        this.portNumServer = portNumServer;
        this.nodeIDClient = nodeIDClient;
        this.portNumClient = portNumClient;
        this.stateClient = stateClient;
    }
    public int getNodeIDServer() {
        return nodeIDServer;
    }

    public int getPortNumServer() {
        return portNumServer;
    }

    public int getNodeIDClient() {
        return nodeIDClient;
    }

    public int getPortNumClient() {
        return portNumClient;
    }

    public int getStateClient() {
        return stateClient;
    }

    public void setStateClient(int stateClient) {
       this.stateClient = stateClient;
    }
}