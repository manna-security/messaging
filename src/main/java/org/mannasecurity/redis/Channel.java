package org.mannasecurity.redis;

/**
 * Created by jtmelton on 6/20/17.
 */
public enum Channel {

    // clone request based on schedule
    // - in  -> scheduler || GH service commit hook
    // - out -> GH service
    CLONE_REQUEST,

    // scan request after cloning complete through GH service
    // - in  -> GH service
    // - out -> orchestrator
    SCAN_REQUEST,

    // verify results after orchestrator has completed scans
    // - in  -> orchestrator
    // - out -> GH service
    VERIFY_RESULTS;
}
