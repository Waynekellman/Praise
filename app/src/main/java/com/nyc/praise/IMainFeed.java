package com.nyc.praise;

import java.util.Set;

/**
 * Created by Wayne Kellman on 7/25/18.
 */

interface IMainFeed {
    void locationAdded(String location);

    void addSetToPreferences(Set<String> locations);
}
