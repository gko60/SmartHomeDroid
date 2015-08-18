package de.elv.homematic.android.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

/**
 * Utilities for showing notifications in the GUI.
 */
public class UiUtils {
    /**
     * Shows a toast (notification at the bottom).
     *
     * @param v   Outer GUI member of the toast.
     * @param msg Message to show.
     */
    public static void showToast(View v, String msg) {
        showToast(v.getContext(), msg);
    }

    /**
     * Shows a toast (notification at the bottom).
     *
     * @param context Outer GUI member of the toast.
     * @param msg     Message to show.
     */
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Shows a alert.
     *
     * @param context Outer GUI member of the alert.
     * @param title   Title of the alert.
     * @param message Message to show.
     */
    public static void showAlert(Context context, String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}