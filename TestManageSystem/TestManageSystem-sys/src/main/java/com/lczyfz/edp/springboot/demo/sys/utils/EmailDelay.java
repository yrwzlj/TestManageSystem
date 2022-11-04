package com.lczyfz.edp.springboot.demo.sys.utils;

import io.lettuce.core.resource.Delay;
import lombok.Data;

import java.time.Duration;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @Author YRW
 * @Date 2022/10/10
 * @Description com.lczyfz.edp.springboot.sys.controller
 * @version: 1.0
 */
@Data
public class EmailDelay implements Delayed {

    private String orderId;

    private long timeout;

    public EmailDelay(String orderId, long timeout) {

        this.orderId = orderId;

        this.timeout = timeout + System.nanoTime();

    }

    public int compareTo(Delayed other) {

        if (other == this) {
            return 0;
        }

        EmailDelay t = (EmailDelay) other;

        long d = (getDelay(TimeUnit.NANOSECONDS) - t

                .getDelay(TimeUnit.NANOSECONDS));

        return (d == 0) ? 0 : ((d < 0) ? -1 : 1);

    }

    // 返回距离你自定义的超时时间还有多少

    public long getDelay(TimeUnit unit) {

        return unit.convert(timeout - System.nanoTime(), TimeUnit.NANOSECONDS);

    }

}



