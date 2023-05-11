package io.netty.example.timeTask;


import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;

import java.util.concurrent.TimeUnit;

public class MyTimerTask {

    private final HashedWheelTimer timer = new HashedWheelTimer();

    public void start() {
        TimerTask task = new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                // 在这里编写定时任务的代码
                System.out.println("定时任务执行了！");
                // 继续添加下一个定时任务
                timer.newTimeout(this, 1, TimeUnit.SECONDS);
            }
        };
        Timeout timeout = timer.newTimeout(task, 1, TimeUnit.SECONDS);
        timeout.cancel();
    }

    public void stop() {
        timer.stop();
    }

    public static void main(String[] args) {
        MyTimerTask myTimerTask = new MyTimerTask();
        myTimerTask.start();
    }
}