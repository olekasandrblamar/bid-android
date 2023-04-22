package com.bid.app.util;

public class ProjectionPathInfo {
    public int pathId;
    public double delta;
    public ProjectionPathInfo(int pathId, double delta) {
        this.pathId = pathId;
        this.delta =delta;
    }

    public int getPathId() {
        return pathId;
    }

    public void setPathId(int pathId) {
        this.pathId = pathId;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }
}
