package shanika.service;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.ContainerStats;
import com.spotify.docker.client.messages.Version;
import org.apache.commons.io.FileUtils;
import org.decimal4j.util.DoubleRounder;
import org.springframework.stereotype.Service;
import shanika.dto.DockerStats;

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
		Long previousCPU = containerStats.precpuStats().cpuUsage().totalUsage();
		float cpuDelta = (float) containerStats.cpuStats().cpuUsage().totalUsage() - (float) previousCPU;

		Long previousSystem = containerStats.precpuStats().systemCpuUsage();
		float systemDelta = (float) containerStats.cpuStats().systemCpuUsage() - (float) previousSystem;
		int perCPU = containerStats.cpuStats().cpuUsage().percpuUsage().size();
		double percent = ((double) cpuDelta / (double) systemDelta) * perCPU * 100;
		String memoryUsage = FileUtils.byteCountToDisplaySize(containerStats.memoryStats().usage());
		String networkUsage = FileUtils
				.byteCountToDisplaySize(containerStats.networks().entrySet().iterator().next().getValue().rxBytes());
		return new DockerStats(DoubleRounder.round(percent, 2), memoryUsage, networkUsage);
	}

}
