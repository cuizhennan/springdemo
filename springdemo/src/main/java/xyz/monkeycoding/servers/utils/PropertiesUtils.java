package xyz.monkeycoding.servers.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Ues load 资源文件
 * @Author Created by CZN on 2017/4/14.
 */
public class PropertiesUtils {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);

    private static final String DEFAULT_LOAD_FILENAME = "config.properties";

    private static final String CHECK_FILE_SUBFIX_REGEX = ".*\\.properties$";

    private static Properties properties;
    static {
        properties = PropHolder.INSTANCE.loadProps(DEFAULT_LOAD_FILENAME);
    }

    public PropertiesUtils() {
    }

    private static class PropHolder {
        private static final PropertiesUtils INSTANCE = new PropertiesUtils();
    }

    public static String getKey(String key) {
        return properties.getProperty(key);
    }

    public static Properties loadProps(String resourceName) {
        if (!RegularUtil.isMatch(CHECK_FILE_SUBFIX_REGEX, resourceName)) {
            logger.error("Only support file suffix .properties, But this file suffix error: [{}]", resourceName);
            throw new IllegalArgumentException("Not Support File Type");
        }

        Properties prop = new Properties();
        InputStream in = PropertiesUtils.class.getClassLoader().getResourceAsStream(resourceName);
        try {
            prop.load(in);
            in.close();
            return prop;
        } catch (IOException ex) {
            logger.error("load config fill failed: ", ex);
        }
        return null;
    }
}
