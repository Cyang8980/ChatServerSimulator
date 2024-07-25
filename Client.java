import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.io.*;

public class Client {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean connected;
    private ArrayList<TCBClient> list1;
    private Socket SRTClient;
    private ListenThread Listening;
    private int GBNWINDOW = 10;


    private void startOverlay(String address, int port) throws IOException {
        socket = new Socket(address, port);
        System.out.println("Connected to server at " + address + ":" + port);

        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        connected = true;
    }


    private void initSRTClient() {
        list1 = new ArrayList<TCBClient>();
        TCBClient connect = new TCBClient(0,0,0,0,0);
        list1.add(connect);
        
    }
 




    //Don't need right now
    /* 
    private Socket createSockSRTClient(int client_port){
         try {
         		list1.get(0)
            //SRTClient = new Socket("localhost",client_port);
            //System.out.println("Client SRT port created: " + client_port);
        } catch(IOException e) {
            e.printStackTrace();
        }

        return SRTClient; 
    }
    */

    //connectSRTClient
    private int connectSRTClient(int server_port) {

        System.out.println(list1.get(0));
        //start timer is missing
        list1.get(0).nodeIDServer = 1;
        list1.get(0).portNumClient = 88;
        list1.get(0).sendBuffer = new LinkedList<>(); //send buffer created

        boolean SYNACK = false;

        try {
            Segment messageSYN = new Segment(0 , 0 , 0, 0, 0, 0, 0, 0);
            System.out.println("Sending SYN");
            out.writeObject(messageSYN);
            list1.get(0).setStateClient(TCBClient.SYNSENT);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Listening = new ListenThread();
        Listening.start();


        return 0; //-1
    }

   




    private void stopClient() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    private void sendSRTClient(String senthis){
        char[] charArray = senthis.toCharArray();
    
        Segment datta = new Segment(0, 0, 0, senthis.length(), 4, 0, 0, charArray.length);
        datta.data = charArray;
    
        SendBufferNode node = new SendBufferNode(datta,0); //time sent is 0
        list1.get(0).sendBuffer.add(node);  
       
          
    }




    
    //Listening Thread
    public class ListenThread extends Thread {
        private Socket clientSocket;
        Segment messageRec;
     

    public ListenThread() {
    
    }

    @Override
    public void run(){

        try {
         
            
            

            //String inputLine;
            while (true) {

                try{
                    messageRec = (Segment) in.readObject();
                    } catch (ClassNotFoundException e) {
        
                    }
                System.out.println("Received message from client: " + messageRec.type);

                //Receive SYNACK
                if (messageRec.type == 1) {
                    list1.get(0).setStateClient(TCBClient.CONNECTED);//set to connected
                    // Create a message to send to the client
                    Segment messageSYN = new Segment(0, 0 , 0, 0, 1, 0, 0, 0);
                    System.out.println("Synack received");
                    // Convert the message to bytes and write it to the output stream
                    out.writeObject(messageSYN);
                    try{
                        Thread.sleep(10000);
                    }catch(Exception ex){
                        ex.printStackTrace();

                    }
                    
                System.out.println("Connected");

                sendSRTClient("hey"); 

                //use synchronize

                



                Thread thread = new Thread(() -> {
                    while (list1.get(0).sendBuffer.size() != 0) { //as soon as list isn't empty
                      for(int i = 0; i <= GBNWINDOW  ;i++ ){ //send data until sent-but-not-Acked segments reaches GBN_WINDOW
                            try {
                                out.writeObject(list1.get(0).sendBuffer.get(i));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //record the time
                      }       
                    }
                });
            
                thread.start();

                






                //Send Fin (2)
                Segment messageFIN = new Segment(0 , 0 , 0, 0, 2, 0, 0, 0);
                System.out.println("Sending FIN");
                out.writeObject(messageFIN);
                System.out.println("sent FIN");
                //Receive FINACK
                } else if (messageRec.type == 3) {
                    // Create a message to send to the client
                    list1.get(0).setStateClient(1);//set to disonnected
                   

                    Segment messageFINACK = new Segment(0, 0 , 0, 0, 3, 0, 0, 0);

                    // Convert the message to bytes and write it to the output stream
                    out.writeObject(messageFINACK);
                    list1.get(0).setStateClient(4);//set to closewait


                    // Create a new timer
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        public void run() {
                            try {
                                System.out.println("1 second has passed - disconnecting");
                                list1.get(0).setStateClient(1);//set to disconnect
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




/* 
    private int closeSRTServer(ServerSocket srtserver2){
        int ans = 0;
        if(!(list1.get(0).getStateServer() == 4)) {//if not a disconnect
            try {
                srtserver2.close();
                ans = 1;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            ans = -1;
        }
        return ans;
    }
    */

    public static void main(String[] args) throws IOException {
        String address = "localhost";
        int port = 59091;
        Client client = new Client();
        client.startOverlay(address, port);
        client.initSRTClient();
        client.connectSRTClient(port);
        //Socket socksr = client.createSockSRTClient(87);
        //client.sendMessage();
        try{
            Thread.sleep(15000);
        }catch(Exception e){
            e.printStackTrace();
        }
        client.stopClient();
    }

}