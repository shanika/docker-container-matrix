package shanika.configuration;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class AppConfiguration {
    @Bean
    public DockerClient getDockerClient() {
        return new DefaultDockerClient("unix:///var/run/docker.sock");
    }
}
