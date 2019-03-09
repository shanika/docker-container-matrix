package shanika.job;

import org.decimal4j.util.DoubleRounder;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import shanika.dto.CpuStats;
import shanika.dto.DockerStats;
import shanika.service.DockerService;

@Component
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PrintJob extends QuartzJobBean {
    public static final String PREV_TOTAL = "PREV_TOTAL";
    public static final String PREV_IDLE = "PREV_IDLE";
    private final DockerService dockerService;

    public PrintJob(DockerService dockerService) {
        this.dockerService = dockerService;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        long usageMemory = dockerService.getMemoryStats() / (1024 * 1024);
        long prevTotal = jobExecutionContext.getMergedJobDataMap().getLong(PREV_TOTAL);
        long prevIdle = jobExecutionContext.getMergedJobDataMap().getLong(PREV_IDLE);
        CpuStats cpuUsage = dockerService.getCpuUsage(prevTotal, prevIdle);
        // update back to next job execute
        jobExecutionContext.getJobDetail().getJobDataMap().put(PREV_TOTAL, cpuUsage.getPrevTotal());
        jobExecutionContext.getJobDetail().getJobDataMap().put(PREV_IDLE, cpuUsage.getPrevIdle());
        //
        DockerStats dockerStats = new DockerStats();
        dockerStats.setCpu_usage(DoubleRounder.round(cpuUsage.getPercent(), 2));
        dockerStats.setUsageMemory(usageMemory);
        dockerStats.setNetwork_usage("");
        System.out.println(dockerStats);
    }
}
