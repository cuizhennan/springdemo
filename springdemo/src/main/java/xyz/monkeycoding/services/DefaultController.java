package xyz.monkeycoding.services;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by czn on 2017/5/4.
 */
@Controller
public class DefaultController {

    @RequestMapping("/")
    @ResponseBody
    public String check() {
        return "SUCCESS";
    }
}
