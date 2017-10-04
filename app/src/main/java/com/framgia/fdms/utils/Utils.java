package com.framgia.fdms.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import com.framgia.fdms.data.model.Respone;
import io.reactivex.Observable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.framgia.fdms.utils.Constant.PERCENT;
import static com.framgia.fdms.utils.Constant.TITLE_NOW;

/**
 * Created by MyPC on 05/05/2017.
 */

public class Utils {
    private static final String TIME_ZONE_GMT = "GMT";
    public static final String INPUT_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final String FORMAT_DATE_DD_MM_YYYY = "dd/MM/yyyy";

    public static <T> Observable<T> getResponse(Respone<T> listRespone) {
        if (listRespone == null) {
            return Observable.error(new NullPointerException());
        } else if (listRespone.isError()) {
            return Observable.error(new NullPointerException("ERROR" + listRespone.getStatus()));
        } else {
            return Observable.just(listRespone.getData());
        }
    }

    public static String getStringPercent(int count, int total) {
        float percent;
        if (total == 0) {
            percent = 0;
        } else {
            percent = (float) count / total * 100f;
        }
        return String.format("%.1f", percent) + PERCENT;
    }

    public static String getStringDate(Date date) {
        if (date == null) return TITLE_NOW;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

    public static String dateToString(Date date) {
        if (date == null) date = new Date();
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        return formatter.format(date);
    }

    public static String getPathFromUri(Context context, Uri uri) {
        if (context == null || uri == null) return null;
        String result = null;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            result = uri.getPath();
        } else {
            if (cursor.moveToFirst()) {
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
                cursor.close();
            }
        }
        return result;
    }

    public static String formatPrice(String priceStr) {
        try {
            double price = Double.parseDouble(priceStr);
            return String.format(Locale.US, "%,.0f", price);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return priceStr;
        }
    }

    public static String stringBoughtDateDevice(Date date) {
        if (date == null) return "";
        SimpleDateFormat formatter = new SimpleDateFormat("dd - MM - yyyy", Locale.getDefault());
        return formatter.format(date);
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity == null) return;
        InputMethodManager inputMethodManager =
            (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static String getStringFromList(List<String> strings) {
        if (strings == null || strings.size() == 0) {
            return "";
        }
        String result = "";
        for (String str : strings) {
            result += str + "\n";
        }
        return result;
    }

    public static String convertUiFormatToDataFormat(String time, String inputFormat,
        String outputFormat) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        TimeZone gmtTime = TimeZone.getTimeZone(TIME_ZONE_GMT);
        SimpleDateFormat sdf = new SimpleDateFormat(inputFormat, Locale.getDefault());
        sdf.setTimeZone(gmtTime);
        SimpleDateFormat newSdf = new SimpleDateFormat(outputFormat, Locale.getDefault());
        newSdf.setTimeZone(gmtTime);
        try {
            return newSdf.format(sdf.parse(time));
        } catch (ParseException e) {
            return null;
        }
    }
}
