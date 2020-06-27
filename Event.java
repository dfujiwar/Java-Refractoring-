final class Event
{
   public Action action;
   public long time;
   public WorldEntity entity;

   public Event(Action action, long time, WorldEntity entity)
   {
      this.action = action;
      this.time = time;
      this.entity = entity;
   }
}
