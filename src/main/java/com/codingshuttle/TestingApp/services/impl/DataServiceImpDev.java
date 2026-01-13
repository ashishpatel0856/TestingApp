package com.codingshuttle.TestingApp.services.impl;

import com.codingshuttle.TestingApp.services.DataService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
public class DataServiceImpDev implements DataService {
    @Override
    public String getData() {
        return "Dev data";
    }
}
