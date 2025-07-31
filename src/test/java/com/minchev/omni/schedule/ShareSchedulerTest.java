package com.minchev.omni.schedule;

import com.minchev.omni.dto.CountryShareDto;
import com.minchev.omni.entity.Scheduler;
import com.minchev.omni.repository.SchedulerRepository;
import com.minchev.omni.service.CountryService;
import com.minchev.omni.service.ShareService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ShareSchedulerTest {

    @InjectMocks
    private ShareScheduler shareScheduler;

    @Mock
    private ShareService shareService;

    @Mock
    private CountryService countryService;

    @Mock
    private SchedulerRepository schedulerRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

     @Test
    public void shareTask_NotFoundScheduleStep_callExternalServiceAndSaveStep() throws Exception {

        var schedule = new Scheduler();
        schedule.setCurrentPage(1);
        schedule.setKey("key");

        CountryShareDto countryShareDto = new CountryShareDto();
        countryShareDto.setName("skks");

        when(schedulerRepository.findByKey("share")).thenReturn(Optional.empty());
        when(shareService.shareData(anyList())).thenReturn(ResponseEntity.status(HttpStatus.OK).body("so"));
        when(schedulerRepository.save(any())).thenReturn(new Scheduler());
        when(countryService.getCountryShareList(any(Pageable.class))).thenReturn(List.of(countryShareDto));

        shareScheduler.shareTask();

        verify(schedulerRepository).save(any(Scheduler.class));
        verify(shareService).shareData(anyList());
    }

    @Test
    public void shareTask_FoundScheduleStep_callExternalServiceAndSaveStep() throws Exception {

        var schedule = new Scheduler();
        schedule.setId(1l);
        schedule.setCurrentPage(1);
        schedule.setKey("share");

        CountryShareDto countryShareDto = new CountryShareDto();
        countryShareDto.setName("skks");

        when(schedulerRepository.findByKey("share")).thenReturn(Optional.of(schedule));
        when(countryService.getCountryShareList(any(Pageable.class))).thenReturn(List.of(countryShareDto));
        when(shareService.shareData(anyList())).thenReturn(ResponseEntity.status(HttpStatus.OK).body("so"));
        when(schedulerRepository.save(any())).thenReturn(new Scheduler());

        shareScheduler.shareTask();

        verify(schedulerRepository).save(any(Scheduler.class));
    }

    @Test
    public void shareTask_NoFounndServer_noSaveSchedulerStep() throws Exception {

        var schedule = new Scheduler();
        schedule.setCurrentPage(1);
        schedule.setKey("key");

        CountryShareDto countryShareDto = new CountryShareDto();
        countryShareDto.setName("skks");

        when(schedulerRepository.findByKey("share")).thenReturn(Optional.empty());
        when(countryService.getCountryShareList(any(Pageable.class))).thenReturn(List.of(countryShareDto));
        when(shareService.shareData(anyList())).thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("so"));
        shareScheduler.shareTask();

        verify(schedulerRepository, never()).save(any(Scheduler.class));
    }

    @Test
    public void shareTask_NoFoundCountries_noSaveSchedulerStep() throws Exception {

        var schedule = new Scheduler();
        schedule.setCurrentPage(1);
        schedule.setKey("key");

        when(schedulerRepository.findByKey("share")).thenReturn(Optional.empty());
        when(countryService.getCountryShareList(any(Pageable.class))).thenReturn(List.of());
        when(shareService.shareData(anyList())).thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("so"));
        shareScheduler.shareTask();

        verify(schedulerRepository, never()).save(any(Scheduler.class));
        verify(shareService, never()).shareData(anyList());
    }

}
