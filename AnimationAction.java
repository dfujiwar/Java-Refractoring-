

public class AnimationAction implements Action {

    private WorldEntity entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public AnimationAction( WorldEntity entity, WorldModel world,
                  ImageStore imageStore, int repeatCount) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }


    public void executeAction(EventScheduler scheduler) {
        executeAnimationAction(scheduler);

    }

    private void executeAnimationAction(
            EventScheduler scheduler) {
        ((EntityAnimated)entity).nextImage();

        if (repeatCount != 1) {
            scheduler.scheduleEvent(entity,
                    createAnimationAction(entity,
                            Math.max(repeatCount - 1, 0)),
                    ((EntityAnimated)entity).getAnimationPeriod());
        }
    }

    public static Action createAnimationAction(WorldEntity entity, int repeatCount) {
        return new AnimationAction(entity, null, null, repeatCount);
    }

}