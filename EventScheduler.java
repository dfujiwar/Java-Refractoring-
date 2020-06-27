import com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm;

import java.util.*;

final class EventScheduler
{
   private PriorityQueue<Event> eventQueue;
   private Map<WorldEntity, List<Event>> pendingEvents;
   private double timeScale;


   public EventScheduler(double timeScale)
   {
      this.eventQueue = new PriorityQueue<>(new EventComparator());
      this.pendingEvents = new HashMap<>();
      this.timeScale = timeScale;
   }

   public void scheduleEvent(
                                    WorldEntity entity, Action action, long afterPeriod)
   {
      long time = System.currentTimeMillis() +
              (long)(afterPeriod * timeScale);
      Event event = new Event(action, time, entity);

      eventQueue.add(event);

      // update list of pending events for the given entity
      List<Event> pending = pendingEvents.getOrDefault(entity,
              new LinkedList<>());
      pending.add(event);
     pendingEvents.put(entity, pending);
   }

   public  void unscheduleAllEvents(
                                          WorldEntity entity)
   {
      List<Event> pending = pendingEvents.remove(entity);

      if (pending != null)
      {
         for (Event event : pending)
         {
           eventQueue.remove(event);
         }
      }
   }

   private void removePendingEvent(EventScheduler scheduler,
                                         Event event)
   {
      List<Event> pending = scheduler.pendingEvents.get(event.entity);

      if (pending != null)
      {
         pending.remove(event);
      }
   }

   public void updateOnTime( long time)
   {
      while (!eventQueue.isEmpty() &&
              eventQueue.peek().time < time)
      {
         Event next = eventQueue.poll();

         removePendingEvent( this, next);

         next.action.executeAction( this);
      }
   }


}
