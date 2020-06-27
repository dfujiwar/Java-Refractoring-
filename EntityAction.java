import processing.core.PImage;

import java.util.List;

public abstract class EntityAction extends WorldEntity{
    private final int actionPeriod;

    public EntityAction(String id, Point position,
               List<PImage> images, int actionPeriod)
    {
        super(id, position, images);
        this.actionPeriod = actionPeriod;
    }

    public int getActionPeriod() {
        return actionPeriod;
    }

    protected abstract void scheduleActions(EventScheduler scheduler,WorldModel world, ImageStore imageStore);
}
