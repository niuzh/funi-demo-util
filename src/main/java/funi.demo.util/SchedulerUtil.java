package funi.demo.util;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author zhihuan.niu on 10/31/17.
 */
public class SchedulerUtil {

    public static void main(String[] args) {
        try {
            JobDetail jobDetail=new JobDetail("job",TestJob.class);
            CronTrigger cronTrigger=new CronTrigger("trigger");
            CronExpression cexp=new CronExpression("0/5 * * * * ?");
            cronTrigger.setCronExpression(cexp);
            SchedulerFactory schedulerFactory=new StdSchedulerFactory();
            Scheduler scheduler=schedulerFactory.getScheduler();
            scheduler.scheduleJob(jobDetail,cronTrigger);
            scheduler.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    class TestJob implements Job {
        //任务接口
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            System.out.println(jobExecutionContext.getJobRunTime());
        }
    }
}
