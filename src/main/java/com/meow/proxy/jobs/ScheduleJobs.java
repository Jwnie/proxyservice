package com.meow.proxy.jobs;

import com.meow.proxy.configure.TaskHolder;
import com.meow.proxy.crawl.ProxyCrawl;
import com.meow.proxy.entity.Proxy;
import com.meow.proxy.entity.Task;
import com.meow.proxy.service.ProxyService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Jwnie on 2017/12/17.
 */
@Component
public class ScheduleJobs {
    private final static Logger LOG = LoggerFactory.getLogger(ScheduleJobs.class);
    @Autowired
    ProxyCrawl proxyCrawl;
    @Autowired
    ProxyService proxyService;

    @Scheduled(fixedRateString = "${com.meow.proxy.jobs.ScheduleJobs.proxyCrawl.period}")
    public void proxyCrawl() {
        LOG.info("Start to crawl valid proxy..");
        TaskHolder taskHolder = TaskHolder.getInstance();
        List<Task> taskList = taskHolder.getTaskList();
        if (CollectionUtils.isNotEmpty(taskList)) {
            List<Proxy> proxies = proxyCrawl.crawl(taskList);
            if (CollectionUtils.isNotEmpty(proxies)) {
                proxyService.saveProxies(proxies);
            }
            LOG.info("Save success valid proxies: " + proxies.size());
        }
    }

}
