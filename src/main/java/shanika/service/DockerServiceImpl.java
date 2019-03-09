package shanika.service;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.ContainerStats;
import com.spotify.docker.client.messages.Version;
import org.springframework.stereotype.Service;
import shanika.dto.CpuStats;
import shanika.dto.DockerStats;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class DockerServiceImpl implements DockerService {

    private final DockerClient dockerClient;
    private final shanika.utils.FileUtils fileUtils;

    public DockerServiceImpl(DockerClient dockerClient, shanika.utils.FileUtils fileUtils) {
        this.dockerClient = dockerClient;
        this.fileUtils = fileUtils;
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

    @Override
    public long getMemoryStats() {
        String totalMemory = fileUtils.runRegexOnFile(TOTAL_MEMORY_PATTERN, "/proc/meminfo");
        long total = Long.parseLong(totalMemory) * 1024;

        String freeMemory = fileUtils.runRegexOnFile(FREE_MEMORY_PATTERN, "/proc/meminfo");
        long free = Long.parseLong(freeMemory) * 1024;

        String buffersMemory = fileUtils.runRegexOnFile(BUFFERS_MEMORY_PATTERN, "/proc/meminfo");
        long buffers = Long.parseLong(freeMemory) * 1024;

        String cachedMemory = fileUtils.runRegexOnFile(CACHED_MEMORY_PATTERN, "/proc/meminfo");
        long cached = Long.parseLong(freeMemory) * 1024;

        // Used memory (calculated as total - free - buffers - cache)
        long usageMemory = total - free - buffers - cached;
        return usageMemory;
    }

    @Override
    public CpuStats getCpuUsage(long prevTotal, long prevIdle) {
        float percent;
        String[] parsedJiffies = fileUtils.runRegexOnFile(CPU_JIFFIES_PATTERN, "/proc/stat").split("\\s+");
        long $IDLE = Long.parseLong(parsedJiffies[3]);

        // Calculate the total CPU time
        long $TOTAL = 0;
        for (int i = 0; i < parsedJiffies.length; i++) {
            $TOTAL += Long.parseLong(parsedJiffies[i]);
        }

        long DIFF_IDLE = $IDLE - prevIdle;
        long DIFF_TOTAL = $TOTAL - prevTotal;
        percent = (100 * ((float) DIFF_TOTAL - (float) DIFF_IDLE) / (float) DIFF_TOTAL);

        //
        CpuStats cpuStats = new CpuStats();
        cpuStats.setPercent(percent);
        cpuStats.setPrevIdle($IDLE);
        cpuStats.setPrevTotal($TOTAL);
        return cpuStats;
    }

    @Override
    public ArrayList<String> gatherNetworkUsage() {
        final ArrayList<String> data = new ArrayList<String>();
        String[] tempData;
        String[] tempFile;

        tempFile = getContents("/proc/net/dev").split(
                System.getProperty("line.separator"));
        // Skip the first two lines (headers)
        for (int i = 2; i < tempFile.length; i++) {
            // Parse /proc/net/dev to obtain network statistics.
            // Line e.g.:
            // lo: 4852 43 0 0 0 0 0 0 4852 43 0 0 0 0 0 0
            tempData = tempFile[i].replace(":", " ").split(
                    " ");
            data.addAll(Arrays.asList(tempData));
            data.removeAll(Collections.singleton(""));
        }
        return data;
    }

    private DockerStats getContainerStats(ContainerStats containerStats) {
        return null;
//        Long previousCPU = containerStats.precpuStats().cpuUsage().totalUsage();
//        float cpuDelta = (float) containerStats.cpuStats().cpuUsage().totalUsage() - (float) previousCPU;
//
//        Long previousSystem = containerStats.precpuStats().systemCpuUsage();
//        float systemDelta = (float) containerStats.cpuStats().systemCpuUsage() - (float) previousSystem;
//        int perCPU = containerStats.cpuStats().cpuUsage().percpuUsage().size();
//        double percent = ((double) cpuDelta / (double) systemDelta) * perCPU * 100;
//        String memoryUsage = FileUtils.byteCountToDisplaySize(containerStats.memoryStats().usage());
//        String networkUsage = FileUtils
//                .byteCountToDisplaySize(containerStats.networks().entrySet().iterator().next().getValue().rxBytes());
//        return new DockerStats(DoubleRounder.round(percent, 2), memoryUsage, networkUsage);
    }

    private String getContents(String path) {
        // ...checks on aFile are elided
        final StringBuilder contents = new StringBuilder();

        try {
            // use buffering, reading one line at a time
            // FileReader always assumes default encoding is OK!
            final BufferedReader input = new BufferedReader(new FileReader(
                    new File(path)));
            try {
                String line; // not declared within while loop
                /*
                 * readLine is a bit quirky : it returns the content of a line
                 * MINUS the newline. it returns null only for the END of the
                 * stream. it returns an empty String if two newlines appear in
                 * a row.
                 */
                while ((line = input.readLine()) != null) {
                    contents.append(line);
                    contents.append(System.getProperty("line.separator"));
                }
            } finally {
                input.close();

            }
        } catch (final IOException ex) {
            ex.printStackTrace();
        }

        return contents.toString();
    }


}
