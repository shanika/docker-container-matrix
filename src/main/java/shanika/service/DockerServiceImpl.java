package shanika.service;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.ContainerStats;
import com.spotify.docker.client.messages.Version;

import shanika.dto.DockerStats;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DockerServiceImpl implements DockerService {

	private final DockerClient dockerClient;

	public DockerServiceImpl(DockerClient dockerClient) {
		this.dockerClient = dockerClient;
	}

	@Override
	public Version version() throws DockerException, InterruptedException {
		return dockerClient.version();
	}

	@Override
	public DockerStats getContainerStats(String containerId) throws DockerException, InterruptedException {
		ContainerStats containerStats = dockerClient.stats(containerId);
		return getContainerStats(containerStats);
	}

	@Override
	public List<DockerStats> getContainerStats() throws DockerException, InterruptedException {
		List<Container> containers = dockerClient.listContainers();
		List<DockerStats> dockerStats = new ArrayList<>();
		for (Container container : containers) {
			ContainerStats containerStats = dockerClient.stats(container.id());
			dockerStats.add(getContainerStats(containerStats));
		}
		return dockerStats;
	}

	private DockerStats getContainerStats(ContainerStats containerStats) {
		double cpuUsage = containerStats.cpuStats().cpuUsage().totalUsage()
				/ containerStats.cpuStats().systemCpuUsage();
		String memoryUsage = FileUtils.byteCountToDisplaySize(containerStats.memoryStats().usage());
		String networkUsage = FileUtils
				.byteCountToDisplaySize(containerStats.networks().entrySet().iterator().next().getValue().rxBytes());
		return new DockerStats(cpuUsage, memoryUsage, networkUsage);
	}

}
