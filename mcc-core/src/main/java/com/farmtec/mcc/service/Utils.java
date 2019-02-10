package com.farmtec.mcc.service;

import java.util.function.Predicate;

public class Utils {
    public  static <T>  boolean validateDto(T dto,Predicate<T> p)
    {
        return p.test(dto);
    }
}
