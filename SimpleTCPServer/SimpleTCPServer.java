/**
 * Created by Konstantin on 27.03.2017.
 */
import java.io.*;
import java.net.*;

public class SimpleTCPServer extends Thread {
    Socket s;
    int num;

    static String extractParams(String[] params, String paramKey){
        int i0=0;
        while(i0++ < params.length){
            if((params[i0-1]).equals(paramKey)){
                return((i0 == params.length)? null: params[i0]);
            }
        }

        return null;
    }

    public static void main(String[] args)
    {
        String sAddress = extractParams(args, "-a");
        if(sAddress==null){
            sAddress = "localhost";
        }

        String sPort = extractParams(args, "-p");

        if(sPort != null) {
            try {
                int i = 0; // connections counter

                // create server socket at the address / port passed in the argument string
                ServerSocket sSocket = new ServerSocket(new Integer(sPort), 0, InetAddress.getByName(sAddress));

                System.out.println(sSocket.getInetAddress().getHostAddress());

                System.out.println("Server has started. Listen to the port "+sPort);

                // listening to the port
                while (true) {
                    //waiting for a new connection request from the client.
                    // Upon receiving the request, the new thread for processing request starts, counter increases by 1.
                    new SimpleTCPServer(i, sSocket.accept());
                    i++;
                }
            } catch (Exception e) {
                System.out.println("init error: " + e);
            } // exceptions
        }
        else
            System.out.println("Arguments are missed!");
    }

    public SimpleTCPServer(int num, Socket s)
    {
        this.num = num;
        this.s = s;

        // The new thread is launched.
        setDaemon(true);
        setPriority(NORM_PRIORITY);
        start();
    }

    public static void main(String[] args)
    {
        int packetNum = 0;
        try
        {
            // The input data stream from socket
            InputStream is = s.getInputStream();
            // The output data stream to socket
            OutputStream os = s.getOutputStream();

            // 64 KB data buffer
            byte inData[] = new byte[64 * 1024];
            // Data from client is read. The result is amount of received data.
            int r = is.read(inData);

            // string contains data from the client
            String outData = new String(inData, 0, r);

            // Data about connection number are added
            outData = "Packet " + num + ": " + "\n" + outData;

            // data are output
            os.write(outData.getBytes());

            System.out.println("Received "+r+" bytes of data");
            // connection is closed
            s.close();
        }
        catch(Exception e)
        {System.out.println("init error: "+e);}
    }

}
