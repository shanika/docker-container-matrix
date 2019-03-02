package shanika.controllers;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Version;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Home {

    private final DockerClient dockerClient;

    public Home(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    @RequestMapping("/")
    public Version dockerVersion() throws DockerException, InterruptedException {
        return dockerClient.version();
    }
}
