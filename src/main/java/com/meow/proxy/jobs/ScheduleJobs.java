package com.meow.proxy.jobs;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Jwnie on 2017/12/17.
 */
@Component
public class ScheduleJobs {
	
	@Scheduled(cron = "")
	public void proxyCrawl() {
	
	
	}
	
}
