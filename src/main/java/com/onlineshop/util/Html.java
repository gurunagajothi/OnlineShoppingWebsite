package com.onlineshop.util;

/** Escapes text before it is written into an HTML page (prevents XSS). */
public final class Html {
    private Html() {}

    public static String esc(Object value) {
        if (value == null) return "";
        String s = String.valueOf(value);
        StringBuilder b = new StringBuilder(s.length() + 16);
        for (char c : s.toCharArray()) {
            switch (c) {
                case '&':  b.append("&amp;");  break;
                case '<':  b.append("&lt;");   break;
                case '>':  b.append("&gt;");   break;
                case '"':  b.append("&quot;"); break;
                case '\'': b.append("&#39;");  break;
                default:   b.append(c);
            }
        }
        return b.toString();
    }
}
