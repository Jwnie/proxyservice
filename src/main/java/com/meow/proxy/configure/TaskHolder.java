package com.meow.proxy.configure;

import com.meow.proxy.download.impl.XicidailiDownLoader;
import com.meow.proxy.entity.Task;
import com.meow.proxy.enums.ProxySite;
import com.meow.proxy.extract.impl.XicidailiExtractor;

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
		taskList.add(new Task("http://www.xicidaili.com/",false,4,XicidailiDownLoader.class.getCanonicalName(), XicidailiExtractor.class.getCanonicalName(), ProxySite.xicidaili.getProxySiteName()));
	}
	
	public List<Task> getTaskList() {
		return taskList;
	}
}
