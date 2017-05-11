package xyz.monkeycoding.servers.controller;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import xyz.monkeycoding.servers.pojo.Quote;

/**
 * Created by czn on 2017/5/5.
 */
@Controller
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class.getCanonicalName());

    private AtomicInteger atomicInteger = new AtomicInteger();

    private final Queue<DeferredQuote> responseBodyQueue = new ConcurrentLinkedQueue<>();

    static class DeferredQuote extends DeferredResult<Quote> {
        private final String symbol;

        public DeferredQuote(String symbol) {
            this.symbol = symbol;
        }
    }

    @RequestMapping(value = "/token", method = RequestMethod.GET)
    @ResponseBody
    public String getToken() {
        return UUID.randomUUID().toString();
    }

    @RequestMapping(value = "/polling/{token}", method = RequestMethod.POST)
    @ResponseBody
    public DeferredQuote polling(@PathVariable("token") String token) {
        logger.info("polling start. {}", token);
        DeferredQuote result = new DeferredQuote(token);
        responseBodyQueue.add(result);
        return result;
    }

//    @Scheduled(fixedRate = 3000)
    public void processQueues() {
        logger.info("process start {}", System.currentTimeMillis());

        for (DeferredQuote result : responseBodyQueue) {
            // Quote quote = jpaStockQuoteRepository.findStock(result.symbol);
            Quote quote = new Quote();
            quote.setData(System.currentTimeMillis() + ": " + atomicInteger.getAndIncrement());
            result.setResult(quote);
            logger.info("result ==> {}", quote);
            responseBodyQueue.remove(result);
        }
    }
}
