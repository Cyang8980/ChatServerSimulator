public class TCBServer {
    private int nodeIDServer;
    private int portNumServer;
    private int nodeIDClient;
    private int portNumClient;
    private int stateServer;

    public TCBServer(int nodeIDServer, int portNumServer, int nodeIDClient, int portNumClient, int stateServer) {
        this.nodeIDServer = nodeIDServer;
        this.portNumServer = portNumServer;
        this.nodeIDClient = nodeIDClient;
        this.portNumClient = portNumClient;
        this.stateServer = stateServer;
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

    public int getStateServer() {
        return stateServer;
    }

    public void setStateServer(int stateServer) {
        this.stateServer = stateServer;
    }
}