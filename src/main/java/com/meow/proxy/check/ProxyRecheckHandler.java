package com.meow.proxy.check;

import com.meow.proxy.base.Const;
import com.meow.proxy.entity.Proxy;
import com.meow.proxy.service.ProxyService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author Alex
 *         date:2017/12/19
 *         email:jwnie@foxmail.com
 */
@Component
public class ProxyRecheckHandler {
    private final static Logger LOG = LoggerFactory.getLogger(ProxyRecheckHandler.class);
    @Autowired
    ProxyService proxyService;
    ScheduledExecutorService scheduledExecutorService = null;
    BlockingDeque<Proxy> proxyBlockingDeque = null;
    private volatile boolean queueIsEmpty = Boolean.TRUE;

    public void handleMessage(ProxyRecheckSender proxyRecheckSender, List<Proxy> proxies) {
        int total = proxies.size();
        LOG.info("代理总数:" + total);
        if (total > 0) {
            synchronized (proxyBlockingDeque) {
                proxyBlockingDeque.addAll(proxies);
                queueIsEmpty = Boolean.FALSE;
            }
        }

        while (!queueIsEmpty) {
            try {
                Thread.sleep(30000);
                if (proxyBlockingDeque.size() <= 0) {
                    queueIsEmpty = Boolean.TRUE;
                }
            } catch (Exception e) {
                LOG.error("", e);
            }
        }
        proxyRecheckSender.process("完成！");

    }

    /**
     * bean初始化后执行指定操作
     */
    @PostConstruct
    public void initMethod() {
        int size = 2 * Const.CPU_AVAILABLEPROCESSORS;
        scheduledExecutorService = new ScheduledThreadPoolExecutor(size + 1, new BasicThreadFactory.Builder().namingPattern("定时检测入库代理线程池").daemon(Boolean.TRUE).build());
        proxyBlockingDeque = new LinkedBlockingDeque<Proxy>();
        for (int i = 0; i < Const.CPU_AVAILABLEPROCESSORS + 1; i++) {
            scheduledExecutorService.execute(new ProxyRecheckHandlerThread("校验代理线程【" + (i + 1) + "】"));
        }
        LOG.info("初始化完成!");
    }


    /**
     * bean销毁时释放资源
     */
    @PreDestroy
    public void destory() {
        //关闭线程池
        scheduledExecutorService.shutdown();
        LOG.info("关闭资源完毕");
    }


    class ProxyRecheckHandlerThread implements Runnable {
        private final Logger LOG = LoggerFactory.getLogger(ProxyRecheckHandlerThread.class);
        private final static int BATCH_UPDATE_SIEZ = 50;
        private String threadName;
        private List<Proxy> proxies = new ArrayList<>(100);


        public ProxyRecheckHandlerThread(String threadName) {
            this.threadName = threadName;
        }

        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            while (true) {
                doReCheckProxies();
            }
        }

        public void doReCheckProxies() {
            ProxyCheck proxyCheck = ProxyCheck.getInstance();
            try {
                while (!queueIsEmpty) {
                    Proxy proxy = proxyBlockingDeque.poll();
                    if (proxy != null) {
                        long begin = System.currentTimeMillis();
                        boolean valid = proxyCheck.checkProxyBySocket(new HttpHost(proxy.getIp(), proxy.getPort()), false);
                        long end = System.currentTimeMillis();
                        proxy.setValid(valid);
                        if (!valid) {
                            proxy.setInvalidTime(end);
                            if (proxy.getLastSurviveTime() == null || proxy.getLastSurviveTime() <= 0) {
                                proxy.setLastSurviveTime(end - proxy.getCheckTime());
                            }
                        }
                        proxy.setCheckStatus(1);
                        proxy.setCheckTime(begin);
                        proxy.setResponseTime(end - begin);
                        if (proxy.getLastSurviveTime() == null) {
                            proxy.setLastSurviveTime(-1L);
                        }
                        if (proxy.getInvalidTime() == null) {
                            proxy.setInvalidTime(-1L);
                        }
                        if (proxy.getValidTime() == null) {
                            proxy.setValidTime(1);
                        }

                        proxies.add(proxy);
                        if (proxies.size() >= BATCH_UPDATE_SIEZ) {
                            proxyService.updateProxies(proxies);
                            proxies.clear();
                            LOG.info("批量更新代理成功！");
                        }
                        LOG.info(this.threadName + " 校验代理结果>>> " + proxy.getIp() + ":" + proxy.getProtocolType() + ",是否有效: " + proxy.isValid());
                    }
                }
                if (CollectionUtils.isNotEmpty(proxies)) {
                    proxyService.updateProxies(proxies);
                    proxies.clear();
                }
                Thread.sleep(10000);
            } catch (Exception e) {
                LOG.error("", e);
            }
        }

    }

}
