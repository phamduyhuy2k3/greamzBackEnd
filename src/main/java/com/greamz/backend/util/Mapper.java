package com.greamz.backend.util;

import org.modelmapper.ModelMapper;

public class Mapper {
    private static final ModelMapper modelMapper=new ModelMapper();
    public static <T, R> R mapObject(T sourceObject, Class<R> destinationClass) {
        return modelMapper.map(sourceObject, destinationClass);
    }

}
