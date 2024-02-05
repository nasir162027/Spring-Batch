package com.naba.writer;


import com.naba.model.Student;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FirstItemWriter implements ItemWriter<Student> {
    @Override
    public void write(List<? extends Student> items) throws Exception {
        System.out.println("Inside Item Writer!! ");
        items.stream().forEach(System.out::println);
    }
}
