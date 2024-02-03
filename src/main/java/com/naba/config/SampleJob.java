package com.naba.config;


import com.naba.listener.FirstJobListener;
import com.naba.listener.FirstStepListener;
import com.naba.processor.FirstItemProcessor;
import com.naba.reader.FirstIItemReader;
import com.naba.service.FirstTasklet;
import com.naba.service.SecondTasklet;
import com.naba.service.ThirdTasklet;
import com.naba.writer.FirstItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
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

    @Autowired
    private FirstIItemReader firstIItemReader;

    @Autowired
    private FirstItemProcessor firstItemProcessor;

    @Autowired
    private FirstItemWriter firstItemWriter;



        @Bean
        public Job taskletJob(){
            return jobBuilderFactory.get("Tasklet Job")
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
    @Bean
    public Job chunkJob() {
        return jobBuilderFactory.get("Chunk oriented job")
                .incrementer(new RunIdIncrementer())
                .start(firstChunkStep())
                .next(secondStep())
                .build();
    }

    private Step firstChunkStep(){
        return stepBuilderFactory.get("First Chunk Step")
                .<Integer,Long>chunk(3)
                .reader(firstIItemReader)
                .processor(firstItemProcessor)
                .writer(firstItemWriter)
                .build();
    }

}
