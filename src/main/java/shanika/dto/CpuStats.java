package shanika.dto;

public class CpuStats {
    private float percent;
    private long prevTotal;
    private long prevIdle;

    public CpuStats() {
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public long getPrevTotal() {
        return prevTotal;
    }

    public void setPrevTotal(long prevTotal) {
        this.prevTotal = prevTotal;
    }

    public long getPrevIdle() {
        return prevIdle;
    }

    public void setPrevIdle(long prevIdle) {
        this.prevIdle = prevIdle;
    }

    @Override
    public String toString() {
        return "CpuStats{" +
                "percent=" + percent +
                ", prevTotal=" + prevTotal +
                ", prevIdle=" + prevIdle +
                '}';
    }
}
