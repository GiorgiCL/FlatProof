package com.flatproof.storage;

public class StoredFileData {

    private final String originalFileName;
    private final String storedFileName;
    private final String storagePath;

    public StoredFileData(String originalFileName, String storedFileName, String storagePath) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.storagePath = storagePath;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public String getStoredFileName() {
        return storedFileName;
    }

    public String getStoragePath() {
        return storagePath;
    }
}