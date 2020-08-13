package com.amamode.realestatemanager.utils;

import android.content.Context;
import android.net.wifi.WifiManager;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param dollars
     * @return
     */
    @NonNull
    public static BigDecimal convertDollarToEuro(@NonNull BigDecimal dollars) {
        double CHANGE_RATE_EURO = 0.812;
        return dollars.multiply(BigDecimal.valueOf(CHANGE_RATE_EURO).setScale(2, BigDecimal.ROUND_UP));
    }

    @NonNull
    public static BigDecimal convertEuroToDollar(@NonNull BigDecimal euros) {
        double CHANGE_RATE_DOLLAR = 1.0791;
        return euros.multiply(BigDecimal.valueOf(CHANGE_RATE_DOLLAR).setScale(2, BigDecimal.ROUND_UP));
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @return
     */
    public static String getTodayDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    public static Boolean isInternetAvailable(Context context){
        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        return wifi.isWifiEnabled();
    }
}
