package lt.imantasm.s_task.config;

import javax.annotation.PostConstruct;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class QuartzConfig {

    @Value("${jobs.dailyRatesFetchJob.cron}")
    private String cronExpression;


    private  final Scheduler scheduler;

    public JobDetail jobDetail() {
        return JobBuilder.newJob().ofType(DailyFetchRatesJob.class)
                         .storeDurably()
                         .withIdentity("DailyFetch_Job_Detail")
                         .withDescription("daily fetch service")
                         .build();
    }

    public Trigger dailyFetchJobTrigger() {
        return TriggerBuilder.newTrigger()
                             .forJob(jobDetail())
                             .withIdentity(jobDetail().getKey().getName())
                             .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                             .build();
    }

    @PostConstruct
    public void starScheduler() throws SchedulerException {
        scheduler.scheduleJob(jobDetail(),dailyFetchJobTrigger());
        scheduler.start();
    }

}
