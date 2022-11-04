package com.lczyfz.edp.springboot.demo.sys.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Author YRW
 * @Date 2022/10/6
 * @Description com.lczyfz.edp.springboot.sys.controller
 * @version: 1.0
 */
public class DateUtil {

    public static boolean afterNow(Date date) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String format = now.format(dateTimeFormat);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse = new Date();

        try {
            parse = simpleDateFormat.parse(format);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return date.after(parse);
    }
}
