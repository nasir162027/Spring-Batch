package com.naba.service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChunkJobScheduler {

    @Autowired
    JobLauncher jobLauncher;


    @Qualifier("chunkJob")
    @Autowired
    Job chunkJob;

    @Scheduled(cron ="0 0/1 * 1/1 * ?" )
    public void chunkJobStarter(){
        Map<String, JobParameter> jobParameterMap=new HashMap<>();
        jobParameterMap.put("CurrentTime",new JobParameter(System.currentTimeMillis()));

        JobParameters jobParameters=new JobParameters(jobParameterMap);
        try {
            JobExecution  jobExecution= jobLauncher.run( chunkJob, jobParameters );
            System.out.println("Job Execution Id: "+jobExecution.getJobId());
        }catch (Exception ex){
            System.out.println("Exception While Starting Job!! ");
        }
    }
}
