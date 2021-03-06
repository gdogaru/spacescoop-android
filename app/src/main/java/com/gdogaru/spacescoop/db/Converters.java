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
