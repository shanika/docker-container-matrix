package shanika.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Version;

import shanika.dto.DockerStats;
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

	@RequestMapping("/stats/{container_id}")
	public DockerStats getContainerStats(@PathVariable("container_id") String containerId) {
		try {
			return dockerService.getContainerStats(containerId);
		} catch (DockerException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/stats")
	public List<DockerStats> getContainerStats() {
		try {
			return dockerService.getContainerStats();
		} catch (DockerException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

}
