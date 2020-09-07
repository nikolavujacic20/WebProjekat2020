package model;

public class CategoryVM {

    private String name;
    private int coreNumber;
    private int RAM;
    private int GPUcore;

    public CategoryVM() {
        super();
    }

    public CategoryVM(String name, int coreNumber, int rAM, int gPUcore) {
        super();
        this.name = name;
        this.coreNumber = coreNumber;
        RAM = rAM;
        GPUcore = gPUcore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCoreNumber() {
        return coreNumber;
    }

    public void setCoreNumber(int coreNumber) {
        this.coreNumber = coreNumber;
    }

    public int getRAM() {
        return RAM;
    }

    public void setRAM(int rAM) {
        RAM = rAM;
    }

    public int getGPUcore() {
        return GPUcore;
    }

    public void setGPUcore(int gPUcore) {
        GPUcore = gPUcore;
    }


}
