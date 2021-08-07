package com.stalkedbythestate.sbts.streamer;

import com.stalkedbythestate.sbts.json.ViewJSON;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.LinkedBlockingQueue;

public class RemoteCameraSpec {
    private String freakName;
    private SortedSet<Integer> cameraSet = new TreeSet<Integer>();
    private List<String> eventFilterList;
    private String startDateStr;
    private String endDateStr;
    private String times;
    private final LinkedBlockingQueue<ViewJSON> queue = new LinkedBlockingQueue<ViewJSON>();

    public String getFreakName() {
        return freakName;
    }

    public void setFreakName(final String freakName) {
        this.freakName = freakName;
    }

    public SortedSet<Integer> getCameraSet() {
        return cameraSet;
    }

    public void setCameraSet(final SortedSet<Integer> cameraSet) {
        this.cameraSet = cameraSet;
    }

    public LinkedBlockingQueue<ViewJSON> getQueue() {
        return queue;
    }

    public String getStartDateStr() {
        return startDateStr;
    }

    public void setStartDateStr(final String startDateStr) {
        this.startDateStr = startDateStr;
    }

    public String getEndDateStr() {
        return endDateStr;
    }

    public void setEndDateStr(final String endDateStr) {
        this.endDateStr = endDateStr;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(final String times) {
        this.times = times;
    }

    public List<String> getEventFilterList() {
        return eventFilterList;
    }

    public void setEventFilterList(final List<String> eventFilterList) {
        this.eventFilterList = eventFilterList;
    }

    @Override
    public String toString() {
        return "RemoteCameraSpec [cameraSet=" + cameraSet + ", endDateStr="
                + endDateStr + ", eventFilterList=" + eventFilterList
                + ", freakName=" + freakName + ", queue=" + queue
                + ", startDateStr=" + startDateStr + ", times=" + times + "]";
    }

}
