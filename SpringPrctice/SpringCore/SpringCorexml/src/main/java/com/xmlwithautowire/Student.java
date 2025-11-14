package com.xmlwithautowire;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//lambok
@Data
@NoArgsConstructor
@AllArgsConstructor
//spring bean
@Component
public class Student {
    @Value("01")
    private int studentId;
    @Value("suresh")
    private String studentName;
    @Autowired
    private Address address;
}
