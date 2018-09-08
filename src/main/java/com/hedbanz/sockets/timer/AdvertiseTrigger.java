package com.hedbanz.sockets.timer;

import com.hedbanz.sockets.service.AdvertiseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Component
@Qualifier("advertiseTrigger")
public class AdvertiseTrigger implements Trigger{
    private final AdvertiseService advertiseService;
    private final Logger log = LoggerFactory.getLogger(AdvertiseTrigger.class);
    @Autowired
    public AdvertiseTrigger(AdvertiseService advertiseService) {
        this.advertiseService = advertiseService;
    }

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        Calendar nextExecutionTime =  new GregorianCalendar();
        Date lastActualExecutionTime = triggerContext.lastActualExecutionTime();
        nextExecutionTime.setTime(lastActualExecutionTime != null ? lastActualExecutionTime : new Date());
        nextExecutionTime.add(Calendar.MILLISECOND, advertiseService.getAdvertiseRate()*60*1000); //you can get the value from wherever you want
        log.info("Get next time of triggering it's: " + nextExecutionTime.getTime());
        return nextExecutionTime.getTime();
    }
}
