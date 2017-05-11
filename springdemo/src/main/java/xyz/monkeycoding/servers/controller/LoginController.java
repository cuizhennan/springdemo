package xyz.monkeycoding.servers.controller;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @Use
 * @Project springdemo
 * @Author Created by CZN on 2017/5/11.
 */
@Controller
@RequestMapping("/api")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class.getCanonicalName());

    private final Map<String, DeferredResult<String>> chatRequests = new ConcurrentHashMap<>();

    @RequestMapping(value = "/polling/{token}", method = RequestMethod.POST)
    @ResponseBody
    public DeferredResult pollingM(@PathVariable("token") final String token) {
        logger.info("polling token info: {}, \ntime: {}", token, new Date().toLocaleString());
        final DeferredResult<String> result = new DeferredResult<>(1000 * 10L);
        this.chatRequests.put(token, result);

        result.onCompletion(new Runnable() {
            @Override
            public void run() {
                chatRequests.remove(token);
                logger.info("Remove request from queue! {}, \nsuccess time: {}", token, new Date().toLocaleString());
            }
        });

        result.onTimeout(new Runnable() {
            @Override
            public void run() {
                logger.info("this timeout {}, time: {}", token, new Date().toLocaleString());
                result.setErrorResult("408");
            }
        });

        return result;
    }

    @RequestMapping(value = "/write/token", method = RequestMethod.POST)
    @ResponseBody
    public String writerTokenStaus(@RequestParam("token") String token) {
        logger.info("write token info: {} target => {}", token, chatRequests.keySet());
        DeferredResult<String> result = chatRequests.get(token);
        if (result != null) {
            result.setResult(token);
        }
        return token;
    }

    @Scheduled(fixedRate = 5000)
    public void processQueues() {
        logger.info("process start {}", System.currentTimeMillis());
        Set<String> sets = chatRequests.keySet();
        for (String s : sets) {
            DeferredResult<String> deferredResult = chatRequests.get(s);
            deferredResult.setResult(s);
        }
    }
}