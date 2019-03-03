package shanika.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DockerStats {

	@JsonProperty("cpu_usage")
	private double cpu_usage;

	@JsonProperty("memory_usage")
	private String memory_usage;

	@JsonProperty("network_usage")
	private String network_usage;

	public DockerStats(double cpu_usage, String memory_usage, String network_usage) {
		this.cpu_usage = cpu_usage;
		this.memory_usage = memory_usage;
		this.network_usage = network_usage;
	}

	public double getCpu_usage() {
		return cpu_usage;
	}

	public void setCpu_usage(double cpu_usage) {
		this.cpu_usage = cpu_usage;
	}

	public String getMemory_usage() {
		return memory_usage;
	}

	public void setMemory_usage(String memory_usage) {
		this.memory_usage = memory_usage;
	}

	public String getNetwork_usage() {
		return network_usage;
	}

	public void setNetwork_usage(String network_usage) {
		this.network_usage = network_usage;
	}

	@Override
	public String toString() {
		return "DockerStats [cpu_usage=" + cpu_usage + ", memory_usage=" + memory_usage + ", network_usage="
				+ network_usage + "]";
	}

}
