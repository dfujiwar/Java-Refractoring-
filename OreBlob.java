import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class OreBlob extends EntityMoving {
    private static final String QUAKE_KEY = "quake";

    public OreBlob( String id, Point position,
                  List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super( id, position,images,actionPeriod, animationPeriod);
    }

    protected boolean moveTo(WorldModel world,
                                  WorldEntity target, EventScheduler scheduler)
    {
        if (adjacent(getPosition(),target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = this.nextPosition( world, target.getPosition());

            if (!getPosition().equals(nextPos))
            {
                Optional<WorldEntity> occupant = world.getOccupant( nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                moveEntity(world, nextPos);
            }
            return false;
        }
    }


    protected Point nextPosition( WorldModel world,
                                        Point destPos)
    {
        int horiz = Integer.signum(destPos.getX() - getPosition().getX());
        Point newPos = new Point(getPosition().getX() + horiz,
                getPosition().getY());

        Optional<WorldEntity> occupant = world.getOccupant(newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.get() instanceof Ore)))
        {
            int vert = Integer.signum(destPos.getY() - getPosition().getY());
            newPos = new Point(getPosition().getX(), getPosition().getY() + vert);
            occupant = world.getOccupant( newPos);

            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.get() instanceof Ore)))
            {
                newPos = getPosition();
            }
        }

        return newPos;
    }




    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent( this,
                ActivityAction.createActivityAction( this,world, imageStore),
                getActionPeriod());
        scheduler.scheduleEvent( this,
                AnimationAction.createAnimationAction(this,0),getAnimationPeriod());

    }


    public void executeOreBlobActivity(WorldModel world,
                                       ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<WorldEntity> blobTarget = world.findNearest(
                getPosition(), Vein.class);
        long nextPeriod = getActionPeriod();

        if (blobTarget.isPresent())
        {
            Point tgtPos = blobTarget.get().getPosition();

            if (this.moveTo( world, blobTarget.get(), scheduler))
            {
                Quake quake = Quake.createQuake(tgtPos,
                        imageStore.getImageList( QUAKE_KEY));

                world.addEntity( quake);
                nextPeriod += getActionPeriod();
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent( this,
                ActivityAction.createActivityAction( this,world, imageStore),
                nextPeriod);
    }


    public static OreBlob createOreBlob(String id, Point position,
                                       int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new OreBlob( id, position, images, actionPeriod, animationPeriod);
    }




}
