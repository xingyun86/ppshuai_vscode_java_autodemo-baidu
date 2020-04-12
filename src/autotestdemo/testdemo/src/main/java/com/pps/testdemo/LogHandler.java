package com.pps.testdemo;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

class LogHander extends Formatter {
    @Override
    public String format(LogRecord record) {
            return record.getLevel() + ":" + record.getMessage()+"\n";
    }
}