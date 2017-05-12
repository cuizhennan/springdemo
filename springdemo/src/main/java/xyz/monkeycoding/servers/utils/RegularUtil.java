package xyz.monkeycoding.servers.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Use 正则工具
 * @Project rce-server
 * @Author Created by CZN on 2017/4/14.
 */
public final class RegularUtil {
    private static final Logger logger = LoggerFactory.getLogger(RegularUtil.class);

    private RegularUtil() {
    }

    /**
     * 正则匹配工具
     *
     * @param regexp
     * @param sourcestr
     * @return
     */
    public static boolean isMatch(String regexp, String sourcestr) {
        regexp = StringUtils.trimToNull(regexp);
        if (regexp == null) {
            return false;
        }
        try {
            Pattern pattern = Pattern.compile(regexp);
            Matcher matcher = pattern.matcher(sourcestr);
            return matcher.find();
        } catch (Exception ex) {
            logger.error("Regular expression not error: {} => {}", regexp, sourcestr, ex);
        }

        return false;
    }

    /**
     * 默认返回group为1的值
     *
     * @param regex
     * @param sourcestr
     * @return
     */
    public final static String regexMatch(String regex, String sourcestr) {
        regex = StringUtils.trimToNull(regex);
        if (regex != null) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(sourcestr);
            while (matcher.find()) {
                return matcher.group(1);
            }
        }

        logger.warn("Regular expression not match {} => {}", regex, sourcestr);
        return "";
    }

}
