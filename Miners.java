import processing.core.PImage;

import java.util.List;

public abstract class Miners extends EntityMoving {
    private int resourceLimit;
    private int resourceCount;


    public Miners(String id, Point position,
                      List<PImage> images, int resourceLimit, int resourceCount,
                      int actionPeriod, int animationPeriod)
    {
        super(id, position, images,actionPeriod,animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;

    }

    public int getResourceLimit() {
        return resourceLimit;
    }

    public int getResourceCount() {
        return resourceCount;
    }

    public void setResourceCount(int resourceCount) {
        this.resourceCount = resourceCount;
    }

    protected abstract void executeMinerActivity(WorldModel world,
                                                 ImageStore imageStore, EventScheduler scheduler);

    protected abstract boolean transform( WorldModel world,
                                       EventScheduler scheduler, ImageStore imageStore);

}
