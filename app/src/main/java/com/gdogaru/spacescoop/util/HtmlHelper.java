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
