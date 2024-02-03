package com.naba.controller;


import com.naba.request.JobParamsRequest;
import com.naba.service.JobService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private JobOperator jobOperator;


    @GetMapping("/start/{jobName}")
    public String startJob(@PathVariable String jobName, @RequestBody List<JobParamsRequest> jobParamsRequests){
        jobService.startJob(jobName,jobParamsRequests);

        return "Job Started........";
    }

    @GetMapping("/stop/{jobExecutionId}")
    public String stopJob(@PathVariable long jobExecutionId){
        try {
            jobOperator.stop( jobExecutionId );
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "Job stopped........";
    }
}
