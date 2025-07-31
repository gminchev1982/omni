package com.minchev.omni.schedule;

import com.minchev.omni.entity.Scheduler;
import com.minchev.omni.repository.SchedulerRepository;
import com.minchev.omni.service.CountryService;
import com.minchev.omni.service.ShareService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Optional;

@Component
public class ShareScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ShareScheduler.class);
    private static final String SCHEDULER_SHARE_KEY = "share";
    private static final String SCHEDULER_SHARE_SORT = "id";
    private static final int PAGE_SIZE_COUNT = 500;
    private static final int PAGE_DEFAULT = 1;

    private final ShareService shareService;
    private final CountryService countryService;
    private final SchedulerRepository schedulerRepository;

    public ShareScheduler(ShareService shareService,
                          CountryService countryService,
                          SchedulerRepository schedulerRepository) {
        this.shareService = shareService;
        this.countryService = countryService;
        this.schedulerRepository = schedulerRepository;
    }

    @Scheduled(cron = "${scheduler.cron}")
    @Transactional
    void shareTask() {

        var schedule = getSchedulerByKey(SCHEDULER_SHARE_KEY);
        var countries =
                countryService.getCountryShareList(PageRequest.of(schedule.getCurrentPage(), PAGE_SIZE_COUNT,
                        Sort.by(SCHEDULER_SHARE_SORT).ascending()));

        if (!CollectionUtils.isEmpty(countries)) {
            var response = shareService.shareData(countries);

            if (HttpStatus.OK.equals(response.getStatusCode())) {
                schedule.setCurrentPage(schedule.getCurrentPage() + 1);
                schedulerRepository.save(schedule);
            }

            logger.info("Scheduler shares countries : {} with response: {}", countries,  response);
        } else {
            logger.info("No found countries : {} ", countries);
        }
    }

    private Scheduler getSchedulerByKey(String schedule_key) {
        Optional<Scheduler> optionalScheduler = schedulerRepository.findByKey(schedule_key);

        return optionalScheduler.isPresent() ? optionalScheduler.get() : new Scheduler(SCHEDULER_SHARE_KEY, PAGE_DEFAULT);
    }
}
