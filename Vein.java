import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Vein extends EntityAction{
    private static final String ORE_ID_PREFIX = "ore -- ";
    private static final int ORE_CORRUPT_MIN = 20000;
    private static final int ORE_CORRUPT_MAX = 30000;
    private static final String ORE_KEY = "ore";
    private static final Random rand = new Random();

    public Vein(String id, Point position,
                  List<PImage> images, int actionPeriod)
    {
      super(id,position, images, actionPeriod);
    }

    public static Vein createVein(String id, Point position, int actionPeriod,
                                    List<PImage> images)
    {
        return new Vein(id, position, images, actionPeriod);
    }

    public void executeVeinActivity( WorldModel world,
                                     ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(getPosition());

        if (openPt.isPresent())
        {
            Ore ore = Ore.createOre(ORE_ID_PREFIX + getId(),
                    openPt.get(), ORE_CORRUPT_MIN +
                            rand.nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                    imageStore.getImageList( ORE_KEY));
            world.addEntity(ore);
            ore.scheduleActions(scheduler,world, imageStore);
        }

        scheduler.scheduleEvent( this,
                ActivityAction.createActivityAction(this, world, imageStore),
                getActionPeriod());
    }

    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent( this,
                ActivityAction.createActivityAction(this, world, imageStore),
                getActionPeriod());
    }

}


