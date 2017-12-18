package com.meow.proxy.crawl;

import com.meow.proxy.download.BaseDownLoader;
import com.meow.proxy.entity.Proxy;
import com.meow.proxy.entity.Task;
import com.meow.proxy.extract.Extractor;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jwnie on 2017/12/17.
 */
@Component
public class ProxyCrawl {
	private final static Logger LOG = LoggerFactory.getLogger(ProxyCrawl.class);
	public  List<Proxy> crawl(Task task){
		List<Proxy> proxies = new ArrayList<>(50);
		try {
			BaseDownLoader downLoader = (BaseDownLoader)Class.forName(task.getDownLoadClassPath()).newInstance();
			Extractor extractor = (Extractor)Class.forName(task.getExtractClassPath()).newInstance();
			List<String> htmlContentList = downLoader.downLoad(task);
			proxies.addAll(extractor.extract(htmlContentList));
		} catch (ClassNotFoundException e) {
			LOG.error("未找到下载或抽取类",e);
		} catch (IllegalAccessException e) {
			LOG.error("",e);
		} catch (InstantiationException e) {
			LOG.error("",e);
		}
		return proxies;
	}
	
	
	public List<Proxy> crawl(List<Task> tasks){
		List<Proxy> proxies = new ArrayList<>(500);
		if(CollectionUtils.isNotEmpty(tasks)){
			for (Task task : tasks){
				proxies.addAll(crawl(task));
			}
		}
		return proxies;
	}
	
}
