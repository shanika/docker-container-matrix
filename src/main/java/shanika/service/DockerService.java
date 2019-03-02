package shanika.service;

import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.ContainerStats;
import com.spotify.docker.client.messages.Version;
import shanika.dto.DockerStats;

import java.util.List;

public interface DockerService {
    /**
     * Get Current Docker version
     *
     * @return {@link Version}
     * @throws DockerException
     * @throws InterruptedException
     */
    Version version() throws DockerException, InterruptedException;

    /**
     * Get Container Statistics
     *
     * @param containerId
     * @return {@link DockerStats}
     * @throws DockerException
     * @throws InterruptedException
     */
//    DockerStats getContainerStats(String containerId) throws DockerException, InterruptedException;
    ContainerStats getContainerStats(String containerId) throws DockerException, InterruptedException;

    List<Container> getContainers() throws DockerException, InterruptedException;
}
