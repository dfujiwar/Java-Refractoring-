import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Miner_Not_Full extends Miners{

    public Miner_Not_Full( String id, Point position,
                  List<PImage> images, int resourceLimit, int resourceCount,
                  int actionPeriod, int animationPeriod)
    {
       super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }


    public void executeMinerActivity(
            WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<WorldEntity> notFullTarget = world.findNearest(getPosition(),
                Ore.class);

        if (!notFullTarget.isPresent() ||
                !this.moveTo(world, notFullTarget.get(), scheduler) ||
                !this.transform( world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    ActivityAction.createActivityAction( this, world, imageStore),
                    getActionPeriod());
        }
    }
    protected boolean transform( WorldModel world,
                                      EventScheduler scheduler, ImageStore imageStore)
    {
        if (getResourceCount() >= getResourceLimit())
        {
            Miner_Full miner = Miner_Full.createMinerFull(getId(), getResourceLimit(),
                    getPosition(), getActionPeriod(), getAnimationPeriod(),
                    getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents( this);

            world.addEntity( miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }


    protected boolean moveTo(WorldModel world,
                                  WorldEntity target, EventScheduler scheduler)
    {
        if (adjacent(getPosition(),target.getPosition()))
        {
            setResourceCount(getResourceCount()+1);
            world.removeEntity( target);
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
                    scheduler.unscheduleAllEvents( occupant.get());
                }

                moveEntity(world,nextPos);
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

        if (horiz == 0 || world.isOccupied( newPos))
        {
            int vert = Integer.signum(destPos.getY() - getPosition().getY());
            newPos = new Point(getPosition().getX(),
                    getPosition().getY() + vert);

            if (vert == 0 || world.isOccupied(newPos))
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
                ActivityAction.createActivityAction(this, world, imageStore),
                getActionPeriod());
        scheduler.scheduleEvent( this,
                AnimationAction.createAnimationAction(this, 0), getAnimationPeriod());

    }



    public static Miner_Not_Full createMinerNotFull(String id, int resourceLimit,
                                            Point position, int actionPeriod, int animationPeriod,
                                            List<PImage> images)
    {
        return new Miner_Not_Full(id, position, images,
                resourceLimit, 0, actionPeriod, animationPeriod);
    }

}
