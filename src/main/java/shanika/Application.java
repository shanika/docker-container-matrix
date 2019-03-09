package shanika;

import org.quartz.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import shanika.job.PrintJob;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public JobDetail sampleJobDetail() {
        return JobBuilder.newJob(PrintJob.class)
                .withIdentity("printJob")
                .usingJobData(PrintJob.PREV_TOTAL, 0l)
                .usingJobData(PrintJob.PREV_IDLE, 0l)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger sampleJobTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(1)
                .repeatForever();

        return TriggerBuilder
                .newTrigger()
                .forJob(sampleJobDetail())
                .withIdentity("printTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }

}
