/*
 * Copyright (c) 2019 Gabriel Dogaru - gdogaru@gmail.com
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.gdogaru.spacescoop.util;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatUtil {
    public static final String DB_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String UI_DATE_FORMAT = "dd MMMM yyyy";
    public static final String XML_DATE_FORMAT = "EEE, d MMM yyyy HH:mm:ss z";

    public static String formatForUI(Date date, @Nullable String lang) {
        if (date == null) {
            return "";
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(UI_DATE_FORMAT, new Locale(lang == null ? "us" : lang));
            return dateFormat.format(date);
        } catch (Exception e) {
            return new SimpleDateFormat(UI_DATE_FORMAT, Locale.US).format(date);
        }
    }

    public static Date formatFromXML(String date) {
        Date result = null;
        if (date != null && date.length() > 0) {
            try {
                result = new SimpleDateFormat(XML_DATE_FORMAT, Locale.US).parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Nullable
    public static String formatForDb(@Nullable Date date) {
        if (date == null) return null;
        return new SimpleDateFormat(DB_DATE_FORMAT, Locale.US).format(date);
    }

    @Nullable
    public static Date dbStringToDate(@Nullable String s) {
        if (s == null) return null;
        try {
            return new SimpleDateFormat(DB_DATE_FORMAT, Locale.US).parse(s);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date dbStringToSafeDate(String s) {
        try {
            return new SimpleDateFormat(DB_DATE_FORMAT, Locale.US).parse(s);
        } catch (Exception e) {
            return new Date(0);
        }
    }
}
