package org.mannasecurity.redis;

import org.mannasecurity.domain.TaskRequest;

/**
 * Created by jtmelton on 6/21/17.
 */
public interface TaskProcessor {

    public void process(TaskRequest request);

}
