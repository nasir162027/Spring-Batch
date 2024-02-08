package com.naba.writer;


import com.naba.model.Student;
import com.naba.model.StudentXml;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FirstItemWriter implements ItemWriter<StudentXml> {
    @Override
    public void write(List<? extends StudentXml> items) throws Exception {
        System.out.println("Inside Item Writer!! ");
        items.stream().forEach(System.out::println);
    }
}
