import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class EntityMoving extends EntityAnimated{
    public EntityMoving(String id, Point position,
                   List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }


    protected abstract boolean moveTo(WorldModel world, WorldEntity target, EventScheduler scheduler);



    protected abstract Point nextPosition( WorldModel world,
                                        Point destPos);

    public void moveEntity(WorldModel world, Point pos)
    {
        Point oldPos = getPosition();
        if (world.withinBounds( pos) && !pos.equals(oldPos))
        {
            world.setOccupancyCell(oldPos, null);
            world.removeEntityAt( pos);
            world.setOccupancyCell(pos,this);
            setPosition(pos);
        }
    }


    public boolean adjacent(Point p1, Point p2)
    {
        return ( p1.getX() == p2.getX() && Math.abs(p1.getY() - p2.getY()) == 1) ||
                (p1.getY() == p2.getY() && Math.abs(p1.getX() - p2.getX()) == 1);
    }


}
