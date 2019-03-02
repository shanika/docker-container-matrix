package shanika.controllers;

import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerStats;
import com.spotify.docker.client.messages.Version;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shanika.service.DockerService;

@RestController
public class DockerController {

    private final DockerService dockerService;

    public DockerController(DockerService dockerService) {
        this.dockerService = dockerService;
    }

    @RequestMapping("/")
    public Version dockerVersion() throws DockerException, InterruptedException {
        return dockerService.version();
    }

    @RequestMapping("/stats")
    public ContainerStats getContainerStats() {
        try {
            return dockerService.getContainerStats(this.dockerService.getContainers().get(0).id());
        } catch (DockerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


}
