package lt.imantasm.s_task.config;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lt.imantasm.s_task.util.FetchService;

@Component
@RequiredArgsConstructor
public class DailyFetchRatesJob implements Job {

    private final FetchService fetchService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        fetchService.fetchAndProcessCSV();
    }
}
