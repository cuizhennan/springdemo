package xyz.monkeycoding.servers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by czn on 2017/5/4.
 */
@Controller
public class DefaultController {
    private static final Logger logger = LoggerFactory.getLogger(DefaultController.class.getName());

    @RequestMapping("/")
    @ResponseBody
    public String check() {
        logger.info("server check: success");
        return "SUCCESS";
    }
}
