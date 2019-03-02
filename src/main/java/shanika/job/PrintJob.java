package shanika.job;

import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerStats;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import shanika.service.DockerService;

@Component
public class PrintJob extends QuartzJobBean {

	private final DockerService dockerService;

	public PrintJob(DockerService dockerService) {
		this.dockerService = dockerService;
	}

	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		try {
			ContainerStats containerStats = dockerService.getContainerStats(this.dockerService.getContainers().get(0).id());
			System.out.println(containerStats.cpuStats());
		} catch (DockerException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
