package unikom.gery.damang.service.devices.qhybrid.requests.fossil_hr.application;

public class ApplicationInformation {
    String appName, version;
    int hash;
    byte fileHandle;

    public ApplicationInformation(String appName, String version, int hash, byte fileHandle) {
        this.appName = appName;
        this.version = version;
        this.hash = hash;
        this.fileHandle = fileHandle;
    }

    public String getAppName() {
        return appName;
    }

    public byte getFileHandle() {
        return fileHandle;
    }
}
