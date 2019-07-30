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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HtmlHelper {

    public static String asHtmlPreviewPage(String content) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head>");
        sb.append("</head> <body>");
        sb.append(content);
        sb.append("</body></html>");
        Document jsoup = Jsoup.parse(sb.toString());
        return jsoup.body().text().substring(0, 150) + "...";
    }

    public static String asHtmlPage(String content, boolean isLandscape) {
        StringBuilder sb = new StringBuilder();
        sb.append(" <html><head>\n" +
                "<meta name='viewport' content='target-densityDpi=device-dpi'/>" +
                "<link href='file:///android_asset/css/item-view.css' type='text/css' rel='stylesheet'/>\n" +
                "</head> <body>");
        if (isLandscape) {
            sb.append("<div>\n" +
                    "      <div class='img-land'></div>\n" +
                    "  <div>");
        }

        sb.append(content);

        if (isLandscape) {
            sb.append("  </div>" +
                    " </div>");
        }
        sb.append("</body></html>");
        return sb.toString();
    }
}
