package shanika.service;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.ContainerStats;
import com.spotify.docker.client.messages.Version;
import org.springframework.stereotype.Service;

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
    public ContainerStats getContainerStats(String containerId) throws DockerException, InterruptedException {
        return dockerClient.stats(containerId);
//        ContainerStats containerStats = dockerClient.stats(containerId);
//        return new DockerStats(containerStats.network(), containerStats.memoryStats(), containerStats.cpuStats());
    }

    @Override
    public List<Container> getContainers() throws DockerException, InterruptedException {
        return dockerClient.listContainers();
    }
}
