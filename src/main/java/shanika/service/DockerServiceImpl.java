package shanika.service;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.ContainerStats;
import com.spotify.docker.client.messages.Version;
import org.springframework.stereotype.Service;
import shanika.dto.CpuStats;
import shanika.dto.DockerStats;
import shanika.dto.NetDeviceUsage;
import shanika.utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
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
        long buffers = Long.parseLong(buffersMemory) * 1024;

        String cachedMemory = fileUtils.runRegexOnFile(CACHED_MEMORY_PATTERN, "/proc/meminfo");
        long cached = Long.parseLong(cachedMemory) * 1024;

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
    public long getNetworkStats() {
        long networkUsage = 0;
        try {
            List<String> lines = FileUtils.readTextFileAsList(NETWORK_STATS);
            List<NetDeviceUsage> devices = new ArrayList<>();
            for (int i = 2; i < lines.size(); i++) {
                devices.add(computeNDeviceUsage(lines.get(i)));
            }
            // cumulative
            if (devices.isEmpty()) {
                return 0;
            }

            for (NetDeviceUsage netDeviceUsage : devices) {
                networkUsage += netDeviceUsage.getTxBytes();
            }
            return networkUsage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return networkUsage;
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

    public static NetDeviceUsage computeNDeviceUsage(String deviceLine) {
        NetDeviceUsage device = new NetDeviceUsage();
        String[] splits = deviceLine.split("\\s+");

        device.setDeviceName(splits[1].replace(":", ""));

        device.setRxBytes(Long.parseLong(splits[2]));
        device.setRxPackets(Long.parseLong(splits[3]));
        device.setRxErrs(Long.parseLong(splits[4]));
        device.setRxDrop(Long.parseLong(splits[5]));
        device.setRxFifo(Long.parseLong(splits[6]));
        device.setRxframe(Long.parseLong(splits[7]));
        device.setRxCompressed(Long.parseLong(splits[8]));
        device.setRxMultiCast(Long.parseLong(splits[9]));

        device.setTxBytes(Long.parseLong(splits[10]));
        device.setTxPackets(Long.parseLong(splits[11]));
        device.setTxErrs(Long.parseLong(splits[12]));
        device.setTxDrop(Long.parseLong(splits[13]));
        device.setTxFifo(Long.parseLong(splits[14]));
        device.setTxColls(Long.parseLong(splits[15]));
        device.setTxCarrier(Long.parseLong(splits[16]));
        if (splits.length >= 18) {
            device.setTxCompressed(Long.parseLong(splits[17]));
        }

        return device;
    }
}
