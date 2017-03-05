package wfaaz.monitor;

import wfaaz.call.Caller;

/**
 * Created by wfAaz on 05.03.2017.
 */
public interface CallerListener {
    void onNewCaller(Caller caller);
}
