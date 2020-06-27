

public class ActivityAction implements Action {
    private WorldEntity entity;
    private WorldModel world;
    private ImageStore imageStore;
    private  int repeatCount;


    public   ActivityAction( WorldEntity entity, WorldModel world,
                    ImageStore imageStore, int repeatCount)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }


    public void executeAction( EventScheduler scheduler)
    {
        executeActivityAction(scheduler);

    }

    private void executeActivityAction(
            EventScheduler scheduler) {
        if (entity instanceof Miner_Full) {
            ((Miner_Full)entity).executeMinerActivity(world,
                    imageStore, scheduler);
        }

        if (entity instanceof Miner_Not_Full) {
            ((Miner_Not_Full)entity).executeMinerActivity(world,
                    imageStore, scheduler);
        }
        if (entity instanceof Ore) {
            ((Ore)entity).executeOreActivity(world, imageStore,
                    scheduler);
        }
        if (entity instanceof OreBlob) {
            ((OreBlob)entity).executeOreBlobActivity(world,
                    imageStore, scheduler);
        }
        if (entity instanceof Quake) {
            ((Quake)entity).executeQuakeActivity(world, imageStore,
                    scheduler);
        }
        if (entity instanceof Vein) {
            ((Vein)entity).executeVeinActivity(world, imageStore,
                    scheduler);
        }

    }

    public static Action createActivityAction(WorldEntity entity, WorldModel world,
                                              ImageStore imageStore)
    {
        return new ActivityAction(entity , world, imageStore, 0);
    }


}
