package com.college.transfer.beans;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperBean {

    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
