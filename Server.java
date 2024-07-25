import java.net.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.io.*;


public class Server {
    private ServerSocket tcpserversocket;
    private Socket tcpclientSocket;
    private ArrayList<TCBServer> list1;
    private ListenThread Listening;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private void startServer(int port) throws IOException {
        try{

            tcpserversocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
    
            tcpclientSocket = tcpserversocket.accept();
            System.out.println("Client connected: " + tcpclientSocket.getInetAddress());

            out = new ObjectOutputStream(tcpclientSocket.getOutputStream());
            in = new ObjectInputStream(tcpclientSocket.getInputStream());

            list1 = new ArrayList<TCBServer>();
            TCBServer connect = new TCBServer(1,tcpserversocket.getLocalPort(),1,tcpclientSocket.getLocalPort(), 1);
            list1.add(connect);

            list1.get(0).setStateServer(2);//set to listening

            Listening = new ListenThread(tcpclientSocket, this);
            Listening.start();


        }catch(IOException e){
            e.printStackTrace();
        }       
    }


    public class ListenThread extends Thread {
        private Socket clientSocket;
        Segment messageRec;
     

    public ListenThread(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
    
    }

    @Override
    public void run(){

        try {

               
            while (true) {
                try{
                    messageRec = (Segment) in.readObject();
                    } catch (ClassNotFoundException e) {
        
                    }
                //System.out.println("Received message from client: " + messageRec.type);
                System.out.println(messageRec.type);
                if (messageRec.type == 0) {
                    list1.get(0).setStateServer(3); //set to connected
                    System.out.println("Connected");
                    // Create a message to send to the client
                    Segment messageSYNACK = new Segment(0,0 , 0, 0, 1, 0, 0, 0);
                    
                    // Convert the message to bytes and write it to the output stream
                    
                    out.writeObject(messageSYNACK);
                    
                } else if (messageRec.type == 2) {
                    System.out.println("in type 2");
                    // Create a message to send to the client
                    list1.get(0).setStateServer(1);//set to disonnected
                   

                    Segment messageFINACK = new Segment(0,0 , 0, 0, 3, 0, 0, 0);

                    System.out.println("Sent FINACK");

                    // Convert the message to bytes and write it to the output stream
                    out.writeObject(messageFINACK);
                    list1.get(0).setStateServer(4);//set to closewait


                    // Create a new timer
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        public void run() {
                            try {
                                System.out.println("1 second has passed - disconnecting");
                                list1.get(0).setStateServer(1);//set to disconnect
                                in.close();
                                out.close();
                                //clientSocket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 1000);

                    break;

                        // Wait for the timer to expire or for a "FIN" message to be received

                        // Cancel the timer if a "FIN" message was received before it expired
                        // if (timer != null) {
                        //     timer.cancel();
                        // }

                        // Close the input stream and client socket to disconnect the client and free up the thread
                        // in.close();
                        // outputStream.close();
                        //clientSocket.close();
                       
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    
    }
}


    private int closeServer(){
        int ans = 0;
        if(!(list1.get(0).getStateServer() == 1)) {//if not a disconnect
            try {
                tcpclientSocket.close();
                tcpserversocket.close();
                ans = 1;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            ans = -1;
        }
        return ans;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        int ans = 0;
        int port = 59091;
        Server server = new Server();
        server.startServer(port);
        //ServerSocket srtserver = server.createSockSRTServer(88);
        Thread.sleep(100000);
        ans = server.closeServer();
        
    }
}