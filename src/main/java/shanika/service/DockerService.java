package shanika.service;

import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Version;

import shanika.dto.DockerStats;

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
    DockerStats getContainerStats(String containerId) throws DockerException, InterruptedException;
    
    
    /**
     * Get All Container Statistics
     *
     * @param containerId
     * @return {@link DockerStats}
     * @throws DockerException
     * @throws InterruptedException
     */
    DockerStats getContainerStats() throws DockerException, InterruptedException;
}
