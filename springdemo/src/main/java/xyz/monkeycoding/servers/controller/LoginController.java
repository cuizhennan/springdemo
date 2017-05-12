package xyz.monkeycoding.servers.controller;

import java.util.Date;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncTask;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import xyz.monkeycoding.servers.commons.ServiceCommons;
import xyz.monkeycoding.servers.commons.SubScribeMessage;
import xyz.monkeycoding.servers.utils.RedisUtil;

/**
 * @Use
 * @Project springdemo
 * @Author Created by CZN on 2017/5/11.
 */
@Controller
@RequestMapping("/api")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class.getCanonicalName());

    private static JedisPool jedisPool = RedisUtil.getConnection();

    @RequestMapping(value = "/polling/{token}", method = RequestMethod.POST)
    @ResponseBody
    public DeferredResult pollingM(@PathVariable("token") final String token) {
        logger.info("polling token info: {}, \ntime: {}", token, new Date().toLocaleString());
        final DeferredResult<String> result = new DeferredResult<>(1000 * 10L);
        // this.chatRequests.put(token, result);

        final Jedis jedis = jedisPool.getResource();
        final SubScribeMessage subScribeMessage = new SubScribeMessage(result);
        new Thread(new ServiceCommons(jedis, subScribeMessage)).start();

        result.onCompletion(new Runnable() {
            @Override
            public void run() {
                // chatRequests.remove(token);
                subScribeMessage.punsubscribe("test");
                logger.info("Remove request from queue! {}, \nsuccess time: {}", token, new Date().toLocaleString());
            }
        });

        result.onTimeout(new Runnable() {
            @Override
            public void run() {
                logger.info("this timeout {}, time: {}", token, new Date().toLocaleString());
                subScribeMessage.punsubscribe("test");
                result.setErrorResult("408");
            }
        });

        return result;
    }

    @RequestMapping(value = "/write/token", method = RequestMethod.POST)
    @ResponseBody
    public WebAsyncTask<String> writerTokenStaus(@RequestParam("token") final String token) {
        logger.info("process start {}", System.currentTimeMillis());
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                Jedis jedis = jedisPool.getResource();
                jedis.publish("test", token + ":" + new Date().toLocaleString());
                return token;
            }
        };

        return new WebAsyncTask<>(callable);
    }

    @Scheduled(fixedRate = 5000)
    public void processQueues() {
        logger.info("process start {}", System.currentTimeMillis());
        Jedis jedis = jedisPool.getResource();
        jedis.publish("test", new Date().toLocaleString());
    }
}
