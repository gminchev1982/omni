package com.minchev.omni.schedule;

import com.minchev.omni.mapper.CountryMapper;
import com.minchev.omni.service.CountryService;
import com.minchev.omni.service.ShareService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class ShareScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ShareScheduler.class);
    private final ShareService shareService;
    private final CountryService countryService;
    private final CountryMapper countryMapper;

    public ShareScheduler(ShareService shareService, CountryService countryService, CountryMapper countryMapper) {
        this.shareService = shareService;
        this.countryService = countryService;
        this.countryMapper = countryMapper;
    }

    @Scheduled(cron = "${scheduler.cron}")
    void shareTask() throws Exception {

        var response =
                shareService.shareData(countryMapper.toCountryShareList(countryService.getCountries()));
        logger.info("Scheduler shares response: " + response);
    }
}
