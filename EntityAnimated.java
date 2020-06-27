import processing.core.PImage;

import java.util.List;

public abstract class EntityAnimated extends EntityAction {
    private int animationPeriod;


    public EntityAnimated(String id, Point position,
                 List<PImage> images, int actionPeriod, int animationPeriod)
    {
       super(id, position, images, actionPeriod);
       this.animationPeriod = animationPeriod;
    }

    public void nextImage()
    {
        setImageIndex((getImageIndex() + 1) % getImages().size());
    }

    public int getAnimationPeriod() {
        return animationPeriod;
    }
}
