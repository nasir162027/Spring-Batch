package com.naba.service;

import com.naba.request.JobParamsRequest;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JobService {

    @Autowired
    JobLauncher jobLauncher;

    @Qualifier("taskletJob")
    @Autowired
    Job taskletJob;

    @Qualifier("chunkJob")
    @Autowired
    Job chunkJob;
    @Async
    public void startJob(String jobName, List<JobParamsRequest> jobParamsRequests){
        Map<String, JobParameter> jobParameterMap=new HashMap<>();
        jobParameterMap.put("CurrentTime",new JobParameter(System.currentTimeMillis()));

        jobParamsRequests.stream().forEach(jobParamsRequest ->{
            jobParameterMap.put(jobParamsRequest.getParamKey(),
                    new JobParameter(jobParamsRequest.getParamValue()));
        } );

        JobParameters jobParameters=new JobParameters(jobParameterMap);
        try {
            JobExecution jobExecution=null ;

            if (jobName.equals( "Tasklet Job" )) {
               jobExecution= jobLauncher.run( taskletJob, jobParameters );
            } else if (jobName.equals( "Chunk oriented job" )) {
                jobExecution= jobLauncher.run( chunkJob, jobParameters );
            }
            System.out.println("Job Execution Id: "+jobExecution.getJobId());
        }catch (Exception ex){
            System.out.println("Exception While Starting Job!! ");
        }
    }

}
