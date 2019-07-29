package com.gdogaru.spacescoop.db;

import androidx.room.TypeConverter;

import com.gdogaru.spacescoop.util.FormatUtil;

import java.math.BigDecimal;
import java.util.Date;

public class Converters {

    @TypeConverter
    public BigDecimal bigDecimalFromString(String value) {
        return value == null ? null : new BigDecimal(value);
    }

    @TypeConverter
    public String bigDecimalToString(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return null;
        } else {
            return bigDecimal.toString();
        }
    }

    @TypeConverter
    public Date dateFromString(String value) {
        return FormatUtil.dbStringToDate(value);
    }

    @TypeConverter
    public String dateToString(Date value) {
        return FormatUtil.formatForDb(value);
    }

}
