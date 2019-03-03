package shanika.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;

import shanika.dto.DockerStats;
import shanika.service.DockerService;

@Component
public class PrintJob extends QuartzJobBean {

	private final DockerService dockerService;
	private final DockerClient dockerClient;

	public PrintJob(DockerService dockerService, DockerClient dockerClient) {
		this.dockerService = dockerService;
		this.dockerClient = dockerClient;
	}

	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		try {
			DockerStats containerStats = dockerService.getContainerStats(this.dockerClient.listContainers().get(0).id());
			System.out.println(containerStats);
		} catch (DockerException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
