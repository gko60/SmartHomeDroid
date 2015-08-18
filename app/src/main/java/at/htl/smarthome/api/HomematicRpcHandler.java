package at.htl.smarthome.api;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.homematic.bidcos.rpc.DeviceDescription;
import com.homematic.bidcos.rpc.RpcException;
import com.homematic.bidcos.rpc.RpcHandler;
import com.homematic.bidcos.rpc.RpcProxy;
import com.homematic.bidcos.rpc.RpcProxyFactory;
import com.homematic.bidcos.rpc.RpcServer;
import com.homematic.bidcos.rpc.RpcServerFactory;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import at.htl.smarthome.repository.WeatherRepository;

/**
 * Asynchronous task to receive events from HomeMatic CCU. Only
 * the XML-RPC method "event" is supported.
 */
public class HomematicRpcHandler extends AsyncTask<Void, Void, Void> implements RpcHandler {

    /**
     * Interface ID of this application to identify received events.
     */
    public final static String InterfaceID = "MyID";
    private static final String LOG_TAG = HomematicRpcHandler.class.getSimpleName();
    /**
     * XML-RPC-Communication (client & server) to HomeMatic CCU.
     */
    private RpcProxy rpcProxy;
    /**
     * XML-RPC server to receive events from HomeMatic CCU.
     */
    private RpcServer server;


    /**
     * Initializes client for XML-RPC communication and registers
     * as event server at HomeMatic CCU.
     *
     * @param url Connection URL to the HomeMatic CCU
     * @throws MalformedURLException Connection URL is in a bad format.
     */
    public void initXmlRpcClient(String url) throws MalformedURLException {
        Log.d(LOG_TAG, "initXmlRpcClient, start");
        this.rpcProxy = RpcProxyFactory.create(url);
        this.rpcProxy.setTimeout(15000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            this.execute();
        }
    }

    /* (non-Javadoc)
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected Void doInBackground(Void... arg0) {
        Log.d(LOG_TAG, "doInBackground() start");
        try {
            this.server = RpcServerFactory.create(2001, this);
            this.server.start();
            rpcProxy.init(this.server.getUrl(), InterfaceID);
            while (true) {
                Thread.sleep(200);
            }
        } catch (Exception ex) {
            Log.e(LOG_TAG, "doInBackground() try exception: " + ex.getLocalizedMessage());
            return null;
        } finally {
            if (this.server != null) {
                try {
                    rpcProxy.shutdown(this.server.getUrl());
                    this.server.stop();
                } catch (RpcException e) {
                    Log.e(LOG_TAG, "doInBackground() finally exception: " + e.getLocalizedMessage());
                } catch (NullPointerException e) {
                    Log.e(LOG_TAG, "doInBackground() NullPointerException" + e.getLocalizedMessage());
                }
            }
        }
    }

    /* (non-Javadoc)
    * @see com.homematic.bidcos.rpc.RpcHandler#event(java.lang.String, java.lang.String, java.lang.String, java.lang.Object)
    */
    @Override
    public void event(String interfaceId, String address, String valueKey, Object value) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        Log.d(LOG_TAG, "IncomingEvent: " +
                String.format("%s,%s,%s,%s", strDate, address, valueKey, value.toString()));
        WeatherRepository.getInstance().addHomematicSensorValue(address, valueKey, value.toString());
        //! Repository.getInstance().insertLogEntry(new LogEntry(address, valueKey,value.toString()));
    }

    /* (non-Javadoc)
     * @see com.homematic.bidcos.rpc.RpcHandler#listDevices(java.lang.String)
     */
    @Override
    public List<DeviceDescription> listDevices(String interfaceId) {
        return null;
    }

    /* (non-Javadoc)
     * @see com.homematic.bidcos.rpc.RpcHandler#newDevices(java.lang.String, com.homematic.bidcos.rpc.DeviceDescription[])
     */
    @Override
    public void newDevices(String interfaceId, DeviceDescription[] descriptions) {
    }

    /* (non-Javadoc)
     * @see com.homematic.bidcos.rpc.RpcHandler#deleteDevice(java.lang.String, java.lang.String[])
     */
    @Override
    public void deleteDevice(String interfaceId, String[] addresses) {
    }

    /* (non-Javadoc)
     * @see com.homematic.bidcos.rpc.RpcHandler#updateDevice(java.lang.String, java.lang.String, int)
     */
    @Override
    public void updateDevice(String interfaceId, String address, int hint) {
    }


}
