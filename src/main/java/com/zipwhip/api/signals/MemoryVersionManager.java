package com.zipwhip.api.signals;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jed
 * Date: 6/27/11
 * Time: 6:13 PM
 * <p/>
 * Simple in-memory signal version manager for use in testing.
 */
public class MemoryVersionManager implements VersionManager {

    private Map<String, Long> memoryVersions = new HashMap<String, Long>();

    @Override
    public Map<String, Long> getVersions() {
        return memoryVersions;
    }

    @Override
    public boolean setVersion(String versionKey, Long newVersion) {

        Long previousVersion = memoryVersions.put(versionKey, newVersion);

        if (previousVersion != null && previousVersion > newVersion) {
            memoryVersions.put(versionKey, previousVersion);
            return false;
        }
        return true;
    }

    @Override
    public void clearVersions() {
        memoryVersions.clear();
    }

}
