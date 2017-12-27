package com.meow.proxy.configure;

import com.meow.proxy.entity.Task;
import com.meow.proxy.enums.ProxySite;

import java.util.ArrayList;
import java.util.List;

/**
 * 待爬取代理网站的配置
 * Created by Jwnie on 2017/12/17.
 */
public class TaskHolder {
    private static TaskHolder ourInstance = new TaskHolder();

    public static TaskHolder getInstance() {
        return ourInstance;
    }

    private List<Task> taskList = new ArrayList<>(50);

    private TaskHolder() {
        this.taskList.add(new Task("http://www.xicidaili.com/", true, 2, "xicidailiDownLoader", "xicidailiExtractor", ProxySite.xicidaili.getProxySiteName()));
        this.taskList.add(new Task("http://www.goubanjia.com/", true, 10, "goubanjiaDownLoader", "goubanjiaExtractor", ProxySite.goubanjia.getProxySiteName()));
        this.taskList.add(new Task("http://www.ip3366.net", true, 4, "ip3366DownLoader", "ip3366Extractor", ProxySite.ip3366.getProxySiteName()));
        this.taskList.add(new Task("http://www.data5u.com/", true, 10, "data5uDownLoader", "data5uExtractor", ProxySite.data5u.getProxySiteName()));

        //境外的代理網站(部分url需要VPN)
        this.taskList.add(new Task("https://free-proxy-list.net", false, 1, "freeProxyListDownLoader", "freeProxyListExtractor", ProxySite.freeProxyList.getProxySiteName()));
    }

    public List<Task> getTaskList() {
        return taskList;
    }
}
