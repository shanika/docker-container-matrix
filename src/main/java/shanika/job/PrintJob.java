package shanika.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.spotify.docker.client.exceptions.DockerException;

import shanika.dto.DockerStats;
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
			List<DockerStats> containerStats = dockerService.getContainerStats();
			System.out.println(containerStats);
		} catch (DockerException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
