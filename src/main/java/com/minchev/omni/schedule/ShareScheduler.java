package com.minchev.omni.schedule;

import com.minchev.omni.controller.FileController;
import com.minchev.omni.service.CountryService;
import com.minchev.omni.service.ShareService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

@Component
public class ShareScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ShareScheduler.class);
    private final ShareService shareService;
    private final CountryService countryService;

    public ShareScheduler(ShareService shareService, CountryService countryService) {
        this.shareService = shareService;
        this.countryService = countryService;
    }

    @Scheduled(cron = "${scheduler.cron}")
    void shareTask() throws Exception {
        var response = shareService.shareData(countryService.getCountyByPage());
        logger.info("Scheduler shares response: " + response);
    }
}
