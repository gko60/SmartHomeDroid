package at.htl.smarthome.commandservice;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Implementiert die Verarbeitung einer über TCP
 * empfangenen Anforderung eines Clients (Sensor, Aktor, ...).
 * Läuft nur einmal ==> kein Abbruch vorgesehen
 */
public class RequestReceiverThread implements Runnable {
    static final String LOG_TAG = RequestReceiverThread.class.getSimpleName();
    private BufferedReader input;
    private BufferedWriter output;
    private CommandInterpreterService commandInterpreterService;

    /**
     * Über Socket einen Schreib- und einen Lesestream erzeugen.
     *
     * @param commandInterpreterService Service zum Emfangen eines Requests
     * @param clientSocket              Socket für die eine Verbindung
     */
    public RequestReceiverThread(CommandInterpreterService commandInterpreterService, Socket clientSocket) {
        this.commandInterpreterService = commandInterpreterService;
        try {

            this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            Log.d(LOG_TAG, "Communication-Thread Constructor Exception: " + e.getMessage());
        }
    }

    /**
     * Request empfangen, Protokoll abarbeiten
     * 1. Request besteht aus Sender;Request  (z.B. LEDWALL,GetCommand)
     * Request wird an CommandInterpreterService zur Bearbeitung übergeben
     * Service delegiert Bearbeitung an konkreten Service (abhängig vom Sender)
     * Ergebnis der Bearbeitung (response) wird an Client übermittelt
     * 2. Response zurückschicken  (z.B. T;15.06.2015 an die LED-Wall, um den Text auszugeben)
     * 3. Result des Clients empfangen
     * 4. Acknowledgement an Client übermitteln
     * <p/>
     * Es entsteht ein Polling durch den Client, bei dem Client Aufträge (Command im Response)
     * erteilt werden können. Ergebnisse dieser Aufträge (z.B. Sensorwerte) werden vom Client
     * per result übremittelt und vom Server bestätigt (ack).
     * <p/>
     * Die zweite Variante der Kommunikation verwendet die Komponente (Sensor, Aktor) als
     * TCP-Server, der ständig auf Aufträge wartet. Dazu muss die Komponente dauernd
     * empfangsbereit (unter Strom) sein und von der Empfangsbereitschft nicht durch
     * zeitkritische Aufgaben (z.B. LED-Wall ansteuern) abgehalten werden.
     * <p/>
     * Wann ist welche Kommunikationsart zu verwenden?
     * Muss die Komponente rasch (unter 1 sec) reagieren können und ist sie dauerhaft
     * mit Strom versorgt ==> Komponente ist TCP-Server
     * (NT: eventuell hoher Energieverbrauch). Anwendungsbeispiel: 220V-Schaltaktor
     * <p/>
     * Sind die Aufträge an die Komponente nicht zeitkritisch ( > 10 sec), kann sich die
     * Komponente "schlafen" legen und damit Energie sparen (z.B. Wassertonne füllen)
     * oder sich anderen zeitkritischen Aufgaben widmen (z.B. LEDs an LED-Wall ansteuern).
     * In diesen Fällen "pollt" die Komponente den Server an, um direkt Meldungen
     * abzusetzen (z.B. Messwerte) oder um Aufträge anzufragen.
     */
    public void run() {
        try {
            String request = input.readLine();
            Log.d(LOG_TAG, "Thread Id: " + Thread.currentThread().getId());
            if (request == null || request.length() == 0) {
                Log.e(LOG_TAG, "request is null or empty!");
                return;
            } else {
                Log.d(LOG_TAG, "Request: " + request);
            }
            String[] elements = request.split(";");
            if (elements.length < 1) {
                Log.e(LOG_TAG, "Request hat nur " + elements.length + " Elemente!");
                return;
            }
            String command = elements[0];
            String response = "";
            switch (command) {
                case "LEDWALL":
                    response = LedWallService.getInstance().getCommand();
                    //response = "THello Ledwall!\r\n";
                    break;
            }
            output.write(response);
            output.flush();
            output.close();
            input.close();
            Log.d(LOG_TAG, "Response geschickt: " + response);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Communication-Thread Communication Exception: " + e.getMessage());
        }
    }

}
