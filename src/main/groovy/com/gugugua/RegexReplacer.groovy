package com.gugugua

import java.util.regex.Matcher


/**
 * @author cairne flashsword20@gmail.com
 * @date 2012-6-8
 */
class RegexReplacer {


    /**
     * @param matcher
     */
    public RegexReplacer(Matcher matcher) {
        super();
        this.matcher = matcher;
    }

    private final Matcher matcher;

    public String replace(Closure c) {
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, c(matcher.group(1)));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
