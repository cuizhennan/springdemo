package xyz.monkeycoding.servers.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.async.DeferredResult;

import redis.clients.jedis.JedisPubSub;

/**
 * @Use
 * @Project springdemo
 * @Author Created by CZN on 2017/5/12.
 */
public class SubScribeMessage extends JedisPubSub {
    private static final Logger logger = LoggerFactory.getLogger(SubScribeMessage.class.getName());

    private DeferredResult<String> deferredResult;

    public SubScribeMessage(DeferredResult<String> deferredResult) {
        this.deferredResult = deferredResult;
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        logger.info("\n****subscribe channel: {}, message : {}", channel, message);
        deferredResult.setResult(message);
    }

    @Override
    public void onPUnsubscribe(String channel, int subscribedChannels) {
        logger.info("\nunsubscribe channel: {}, {}", channel, subscribedChannels);
    }
}
