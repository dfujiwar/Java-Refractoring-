import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Miner_Full extends Miners {

    public Miner_Full(String id, Point position,
                  List<PImage> images, int resourceLimit, int resourceCount,
                  int actionPeriod, int animationPeriod)
    {
        super(id, position,images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }


    protected void executeMinerActivity(WorldModel world,
                                         ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<WorldEntity> fullTarget = world.findNearest(getPosition(),
                Blacksmith.class);

        if (fullTarget.isPresent() &&
                this.moveTo( world, fullTarget.get(), scheduler))
        {
            this.transform( world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this,
                    ActivityAction.createActivityAction( this, world, imageStore),
                    getActionPeriod());
        }
    }

    protected boolean transform( WorldModel world,
                                EventScheduler scheduler, ImageStore imageStore)
    {
        Miner_Not_Full miner = Miner_Not_Full.createMinerNotFull(getId(), getResourceLimit(),
                getPosition(), getActionPeriod(), getAnimationPeriod(),
                getImages());

        world.removeEntity( this);
        scheduler.unscheduleAllEvents( this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
        return true;
    }


    protected boolean moveTo( WorldModel world,
                                WorldEntity target, EventScheduler scheduler)
    {
        if (adjacent(getPosition(), target.getPosition()))
        {
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


    public static Miner_Full createMinerFull(String id, int resourceLimit,
                                         Point position, int actionPeriod, int animationPeriod,
                                         List<PImage> images)
    {
        return new Miner_Full(id, position, images,
                resourceLimit, resourceLimit, actionPeriod, animationPeriod);
    }


    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                ActivityAction.createActivityAction(this, world, imageStore),
                getActionPeriod());
        scheduler.scheduleEvent( this, AnimationAction.createAnimationAction(this, 0),
                getAnimationPeriod());

    }

}
