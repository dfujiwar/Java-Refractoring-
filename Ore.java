import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class Ore extends EntityAction{

    private static final String BLOB_KEY = "blob";
    private static final String BLOB_ID_SUFFIX = " -- blob";
    private static final int BLOB_PERIOD_SCALE = 4;
    private static final int BLOB_ANIMATION_MIN = 50;
    private static final int BLOB_ANIMATION_MAX = 150;
    private static final Random rand = new Random();


    public Ore(String id, Point position,
                  List<PImage> images, int actionPeriod)
    {
        super(id, position, images,actionPeriod);
    }


    public void executeOreActivity( WorldModel world,
                                     ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = this.getPosition();  // store current position before removing

        world.removeEntity (this);
        scheduler.unscheduleAllEvents( this);

        OreBlob blob = OreBlob.createOreBlob(getId() + BLOB_ID_SUFFIX,
                pos, getActionPeriod() / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN +
                        rand.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN),
                imageStore.getImageList( BLOB_KEY));

        world.addEntity( blob);
        blob.scheduleActions(scheduler, world, imageStore);
    }


    public static Ore createOre(String id, Point position, int actionPeriod,
                                   List<PImage> images)
    {
        return new Ore(id, position, images, actionPeriod);
    }

    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(  this,
                ActivityAction.createActivityAction( this,world, imageStore), getActionPeriod());

    }



}

