package com.naba.config;


import com.naba.listener.FirstJobListener;
import com.naba.listener.FirstStepListener;
import com.naba.service.FirstTasklet;
import com.naba.service.SecondTasklet;
import com.naba.service.ThirdTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SampleJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private FirstTasklet firstTasklet;

    @Autowired
    private SecondTasklet secondTasklet;

    @Autowired
    private ThirdTasklet thirdTasklet;

    @Autowired
    private FirstJobListener firstJobListener;

    @Autowired
    private FirstStepListener firstStepListener;



        @Bean
        public Job firstJob(){
            return jobBuilderFactory.get("First Job")
                    .start(firstStep())
                    .next(secondStep())
                    .next(thirdStep())
                    .listener(firstJobListener)
                    .build();
        }


    private Step firstStep(){
           return stepBuilderFactory.get("First Step")
                    .tasklet(firstTasklet)
                   .listener(firstStepListener)
                    .build();
    }

    private Step secondStep(){
        return stepBuilderFactory.get("Second Step")
                .tasklet(secondTasklet)
                .build();
    }
    private Step thirdStep(){
        return stepBuilderFactory.get("Third Step")
                .tasklet(thirdTasklet)
                .build();
    }
}
