package org.mannasecurity.kafka;

/**
 * Created by jtmelton on 4/4/17.
 */
public enum Topic {

    // clone request based on schedule
    // - in  -> scheduler
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
