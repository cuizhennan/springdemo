package xyz.monkeycoding.servers.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

/**
 * Created by czn on 2017/5/5.
 */
public class ServiceCommons extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(ServiceCommons.class.getName());

    private Jedis jedis;
    private SubScribeMessage subScribeMessage;

    public ServiceCommons(Jedis jedis, SubScribeMessage subScribeMessage) {
        this.jedis = jedis;
        this.subScribeMessage = subScribeMessage;
    }

    @Override
    public void run() {
        logger.info("listener start ...");
        jedis.psubscribe(subScribeMessage, "test");
        logger.info("listener end ...");
    }
}
