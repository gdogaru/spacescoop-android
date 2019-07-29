package com.gdogaru.spacescoop.util;

import android.content.Context;
import android.content.Intent;

/**
 * User: morariuoana
 */
public class ShareLinkUtil {

    public static void shareLink(Context context, long id) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        intent.putExtra(Intent.EXTRA_TEXT, getLink(context, id));
        context.startActivity(Intent.createChooser(intent, "Share with"));
    }

    private static String getLink(Context context, long id) {
//        DatabaseHelper dbHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
//        Article news = dbHelper.getNewsDao().queryForId(id);
//        return news.getLink();
        return null;
    }

}
