package com.amamode.realestatemanager.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param dollars the amount in dollar
     * @return the new amount in euro
     */
    @NonNull
    public static BigDecimal convertDollarToEuro(@NonNull BigDecimal dollars) {
        double CHANGE_RATE_EURO = 0.85;
        return dollars.multiply(BigDecimal.valueOf(CHANGE_RATE_EURO).setScale(2, BigDecimal.ROUND_UP));
    }

    @NonNull
    public static BigDecimal convertEuroToDollar(@NonNull BigDecimal euros) {
        double CHANGE_RATE_DOLLAR = 1.18;
        return euros.multiply(BigDecimal.valueOf(CHANGE_RATE_DOLLAR).setScale(2, BigDecimal.ROUND_UP));
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @return the date of the day in string format
     */
    public static String getTodayDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context current context of the application
     * @return true if a connexion is available
     */
    public static Boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;

        if (connectivityManager != null) {
            activeNetwork = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        // OLD VERSION
        /*WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        return wifi.isWifiEnabled();*/
    }
}
