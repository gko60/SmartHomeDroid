package at.htl.smarthome.commandservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

/***
 * Implementiert einen Kommandointerpreter, der z.B.
 * auf Anfragen der LED-Wall die gewünschten Inhalte liefert
 * auf Anfragen der automatischen Zisternenbefüllung die Meldungen der
 * Gewichtserkennung der Wassertonne interpretiert und der Zisternenbefüllung
 * die entsprechenden Befehle sendet.
 * Binder folgende Möglichkeiten zur Verfügung:
 * - Registrieren eines Handlers um Meldungen im MainThread der Activity
 * im entsprechenden Fragment ausgeben zu können
 * - Befehle für LED-Wall aufbereiten
 */
public class CommandInterpreterService extends Service {
    private static final String LOG_TAG = CommandInterpreterService.class.getSimpleName();
    private final IBinder commandInterpreterServiceBinder = new CommandInterpreterServiceBinder();
    TcpCommandServer tcpCommandServer;
    private Handler activityNotificationHandler;

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(LOG_TAG, "onUnbind()");
        _stopCommandServer();
        Log.d(LOG_TAG, "onUnbind() CommandInterpreterService beendet");
        activityNotificationHandler = null;
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind()");
        _startCommandServer();
        Log.d(LOG_TAG, "onBind() CommandServer gestartet");
        return commandInterpreterServiceBinder;
    }

    @Override
    public void onCreate() {
        Log.d(LOG_TAG, "onCreate()");
        LedWallService.getInstance().setInfoCommand();  // startet mit aktuellen Infos
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "onDestroy()");
        super.onDestroy();
    }

    /**
     * Statusmeldungen an die Activity
     * zurückmelden
     *
     * @param notificationText Text
     */
    void sendNotificationToActivity(String notificationText) {
        final Message message = new Message();
        final Bundle bundle = new Bundle();
        bundle.putString("LogString", notificationText);
        message.setData(bundle);
        activityNotificationHandler.sendMessage(message);
    }


    /**
     * TCP-Server, der im Hintergrund auf Anforderungen lauscht
     * starten.
     */
    void _startCommandServer() {
        Log.d(LOG_TAG, "startCommandServer()");
        // Servicereferenz wird zum Versenden der SMS benötigt
        tcpCommandServer = new TcpCommandServer(this);
        tcpCommandServer.startTcpCommandServer();
    }

    /**
     * Hintergrundthread, der auf Anforderungen per TCP-IP wartet
     * wird beendet.
     */
    void _stopCommandServer() {
        Log.d(LOG_TAG, "stopCommandServer()");
        tcpCommandServer.stopTcpCommandServer();
    }

    /**
     * Binder implementiert die Schnittstellenmethoden des
     * Service (quasi Contract).
     * Nutzmethoden im Service bilden Wrapper für Servicemethoden.
     * Oft wird z.B. der Context benötigt, der im Binder nicht verfügbar ist
     */
    public class CommandInterpreterServiceBinder extends Binder {
        private final String LOG_TAG = CommandInterpreterService.class.getSimpleName();

        /**
         * Handler des Mainthread, der Notification entgegennimmt.
         *
         * @param callback Methode des Mainthread, der UI pflegt
         */
        public void setActivityNotificationHandler(final Handler callback) {
            Log.d(LOG_TAG, "setActivityNotificationHandler()");
            activityNotificationHandler = callback;
        }
    }

}
