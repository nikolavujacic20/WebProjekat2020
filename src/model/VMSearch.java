package model;

public class VMSearch {
    public String name;
    public int coreNumberFrom;
    public int coreNumberTo;
    public int RAMFrom;
    public int RAMTo;
    public int GPUFrom;
    public int GPUTo;

    public VMSearch() {
        super();
    }

    public VMSearch(String name, int coreNumberFrom, int coreNumberTo, int rAMFrom, int rAMTo, int gPUFrom, int gPUTo) {
        super();
        this.name = name;
        this.coreNumberFrom = coreNumberFrom;
        this.coreNumberTo = coreNumberTo;
        RAMFrom = rAMFrom;
        RAMTo = rAMTo;
        GPUFrom = gPUFrom;
        GPUTo = gPUTo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCoreNumberFrom() {
        return coreNumberFrom;
    }

    public void setCoreNumberFrom(int coreNumberFrom) {
        this.coreNumberFrom = coreNumberFrom;
    }

    public int getCoreNumberTo() {
        return coreNumberTo;
    }

    public void setCoreNumberTo(int coreNumberTo) {
        this.coreNumberTo = coreNumberTo;
    }

    public int getRAMFrom() {
        return RAMFrom;
    }

    public void setRAMFrom(int rAMFrom) {
        RAMFrom = rAMFrom;
    }

    public int getRAMTo() {
        return RAMTo;
    }

    public void setRAMTo(int rAMTo) {
        RAMTo = rAMTo;
    }

    public int getGPUFrom() {
        return GPUFrom;
    }

    public void setGPUFrom(int gPUFrom) {
        GPUFrom = gPUFrom;
    }

    public int getGPUTo() {
        return GPUTo;
    }

    public void setGPUTo(int gPUTo) {
        GPUTo = gPUTo;
    }

}
