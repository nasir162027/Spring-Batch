package com.naba.config;


import com.naba.listener.FirstJobListener;
import com.naba.listener.FirstStepListener;
import com.naba.model.Student;
import com.naba.model.StudentJdbc;
import com.naba.model.StudentXml;
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
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.sql.DataSource;
import java.io.File;

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

    @Autowired
    private DataSource dataSource;


    @Bean
    public Job taskletJob() {
        return jobBuilderFactory.get( "Tasklet Job" )
                .start( firstStep() )
                .next( secondStep() )
                .next( thirdStep() )
                .listener( firstJobListener )
                .build();
    }


    private Step firstStep() {
        return stepBuilderFactory.get( "First Step" )
                .tasklet( firstTasklet )
                .listener( firstStepListener )
                .build();
    }

    private Step secondStep() {
        return stepBuilderFactory.get( "Second Step" )
                .tasklet( secondTasklet )
                .build();
    }

    private Step thirdStep() {
        return stepBuilderFactory.get( "Third Step" )
                .tasklet( thirdTasklet )
                .build();
    }

    @Bean
    public Job chunkJob() {
        return jobBuilderFactory.get( "Chunk oriented job" )
                .incrementer( new RunIdIncrementer() )
                .start( firstChunkStep() )
                .next( secondStep() )
                .build();
    }

    private Step firstChunkStep() {
        return stepBuilderFactory.get( "First Chunk Step" )
                .<StudentJdbc,StudentJdbc>chunk( 3)
                //.reader( flatFileItemReader() )
                //.reader( jsonItemReader() )
                //.reader(staxEventItemReader())
                .reader(jdbcJdbcCursorItemReader())
                //.processor( firstItemProcessor )
                .writer( firstItemWriter )
                .build();
    }

    public FlatFileItemReader<Student> flatFileItemReader() {
        FlatFileItemReader<Student> flatFileItemReader = new FlatFileItemReader<>();

        flatFileItemReader.setResource( new FileSystemResource( new File( "E:\\Spring Project\\Spring-Batch-Application\\src\\main\\resources\\InputFiles\\students.csv" ) ) );

        flatFileItemReader.setLineMapper( new DefaultLineMapper<Student>() {
            {
                setLineTokenizer( new DelimitedLineTokenizer() {
                    {
                        setNames( "ID", "First name", "Last name", "Email" );
                    }
                } );
                setFieldSetMapper( new BeanWrapperFieldSetMapper<Student>() {
                    {
                        setTargetType( Student.class );
                    }
                } );
            }
        } );
        flatFileItemReader.setLinesToSkip( 1 );
        return flatFileItemReader;
    }

    public JsonItemReader<Student> jsonItemReader(){
        JsonItemReader<Student> jsonItemReader=new JsonItemReader<>();
        jsonItemReader.setResource(new FileSystemResource(new File("E:\\Spring Project\\Spring-Batch-Application\\src\\main\\resources\\InputFiles\\students.json")));
        jsonItemReader.setJsonObjectReader( new JacksonJsonObjectReader<>(Student.class) );
        jsonItemReader.setMaxItemCount(8);
        jsonItemReader.setCurrentItemCount(3);
        return jsonItemReader;
    }

    public StaxEventItemReader<StudentXml> staxEventItemReader(){
        StaxEventItemReader<StudentXml> staxEventItemReader=new StaxEventItemReader<>();
        staxEventItemReader.setResource(new FileSystemResource( new File("E:\\Spring Project\\Spring-Batch-Application\\src\\main\\resources\\InputFiles\\students.xml") ));
        staxEventItemReader.setFragmentRootElementName("student");
        staxEventItemReader.setUnmarshaller(new Jaxb2Marshaller(){{
            setClassesToBeBound(StudentXml.class);
        }
        });
        staxEventItemReader.setCurrentItemCount(3);

        return staxEventItemReader;
    }

    public JdbcCursorItemReader<StudentJdbc> jdbcJdbcCursorItemReader(){
        JdbcCursorItemReader<StudentJdbc> jdbcJdbcCursorItemReader=new JdbcCursorItemReader<>();
        jdbcJdbcCursorItemReader.setDataSource(dataSource);
        jdbcJdbcCursorItemReader.setSql("select id,first_name as firstName,last_name as lastName, email from student");

        jdbcJdbcCursorItemReader.setRowMapper(new BeanPropertyRowMapper<StudentJdbc>(){
            {
                setMappedClass( StudentJdbc.class);
            }
        });
        jdbcJdbcCursorItemReader.setCurrentItemCount(5);
        jdbcJdbcCursorItemReader.setMaxItemCount(15);
        return jdbcJdbcCursorItemReader;
    }

}
