package at.htl.smarthome.commandservice;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Startet einen TCP-Server, der im Hintergrund auf Anforderungen per IP
 * wartet. Jede Anforderung wird in einem eigenen CommunicationThread
 * behandelt.
 * Das Beenden TcpServerThreads erfolgt etwas brutal durch Schliessen
 * der IP-Verbindung. Ein Interrupt des Threads wirkt sich in der accept-Methode
 * nicht aus.
 */
public class TcpCommandServer {
    static final String LOG_TAG = TcpCommandServer.class.getSimpleName();
    static final int TCP_PORT = 5678;
    final Thread tcpServerThread;
    ServerSocket serverSocket;

    public TcpCommandServer(final CommandInterpreterService commandInterpreterService) {
        // Server, der im Hintergrund auf Sms-Anforderungen über TCP wartet
        tcpServerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isAborted = false;
                Log.d(LOG_TAG, "start IpListener");
                try {
                    serverSocket = new ServerSocket(TCP_PORT);
                    while (!isAborted /*!Thread.currentThread().isInterrupted() */) {
                        try {

                            Log.d(LOG_TAG, "TCP-Server wartet auf Verbindung");
                            // Unterbrechung des Wartens auf Client erfolgt durch Schliessen des Sockets
                            Socket clientSocket = serverSocket.accept(); // Auf Clientanfrage warten
                            Log.d(LOG_TAG, "Anfrage von Client akzeptiert");
                            RequestReceiverThread requestReceiverThread =
                                    new RequestReceiverThread(commandInterpreterService, clientSocket);
                            new Thread(requestReceiverThread).start();

                        } catch (Exception e) {
                            Log.d(LOG_TAG, "Exception in accept(): " + e.getLocalizedMessage());
                            isAborted = true;
                        }
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Exception in create serversocket: " + e.getLocalizedMessage());
                }
                Log.d(LOG_TAG, "Tcp-Server beendet");
            }
        });
    }

    /**
     * Der IP-Server wird gestartet. Er lauscht dann auf
     * dem vereinbarten Port auf SMS-Anforderungen
     */
    public void startTcpCommandServer() {
        Log.d(LOG_TAG, "startCommandServer()");
        tcpServerThread.start();
    }

    /**
     * Programm wurde beendet. Hintergrundprozess stoppen
     */
    public void stopTcpCommandServer() {
        Log.d(LOG_TAG, "stopCommandServer()");
        if (serverSocket != null) try {
            serverSocket.close();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Exception in close serversocket: " + e.getLocalizedMessage());
        }
    }


}
