import processing.core.PImage;

import java.util.*;
import java.util.function.Function;

final class WorldModel
{
   private int numRows;
   private int numCols;
   private Background background[][];
   private WorldEntity occupancy[][];
   private Set<WorldEntity> entities;
   private static final int ORE_REACH = 1;

   private static final int PROPERTY_KEY = 0;

   private static final String BGND_KEY = "background";
   private static final int BGND_NUM_PROPERTIES = 4;
   private static final int BGND_ID = 1;
   private static final int BGND_COL = 2;
   private static final int BGND_ROW = 3;

   private static final String MINER_KEY = "miner";
   private static final int MINER_NUM_PROPERTIES = 7;
   private static final int MINER_ID = 1;
   private static final int MINER_COL = 2;
   private static final int MINER_ROW = 3;
   private static final int MINER_LIMIT = 4;
   private static final int MINER_ACTION_PERIOD = 5;
   private static final int MINER_ANIMATION_PERIOD = 6;

   private static final String OBSTACLE_KEY = "obstacle";
   private static final int OBSTACLE_NUM_PROPERTIES = 4;
   private static final int OBSTACLE_ID = 1;
   private static final int OBSTACLE_COL = 2;
   private static final int OBSTACLE_ROW = 3;

   private static final String ORE_KEY = "ore";
   private static final int ORE_NUM_PROPERTIES = 5;
   private static final int ORE_ID = 1;
   private static final int ORE_COL = 2;
   private static final int ORE_ROW = 3;
   private static final int ORE_ACTION_PERIOD = 4;

   private static final String SMITH_KEY = "blacksmith";
   private static final int SMITH_NUM_PROPERTIES = 4;
   private static final int SMITH_ID = 1;
   private static final int SMITH_COL = 2;
   private static final int SMITH_ROW = 3;

   private static final String VEIN_KEY = "vein";
   private static final int VEIN_NUM_PROPERTIES = 5;
   private static final int VEIN_ID = 1;
   private static final int VEIN_COL = 2;
   private static final int VEIN_ROW = 3;
   private static final int VEIN_ACTION_PERIOD = 4;



   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new WorldEntity[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public Background[][] getBackground() {
        return background;
    }

    public Set<WorldEntity> getEntities() {
        return entities;
    }

    public  Optional<Point> findOpenAround(Point pos)
   {
      for (int dy = -ORE_REACH; dy <= ORE_REACH; dy++)
      {
         for (int dx = -ORE_REACH; dx <= ORE_REACH; dx++)
         {
            Point newPt = new Point(pos.getX() + dx, pos.getY() + dy);
            if (withinBounds( newPt) &&
                    !isOccupied(newPt))
            {
               return Optional.of(newPt);
            }
         }
      }

      return Optional.empty();
   }


   public  void load(Scanner in, ImageStore imageStore)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            if (!processLine(in.nextLine(), imageStore))
            {
               System.err.println(String.format("invalid entry on line %d",
                       lineNumber));
            }
         }
         catch (NumberFormatException e)
         {
            System.err.println(String.format("invalid entry on line %d",
                    lineNumber));
         }
         catch (IllegalArgumentException e)
         {
            System.err.println(String.format("issue on line %d: %s",
                    lineNumber, e.getMessage()));
         }
         lineNumber++;
      }
   }

   private  boolean processLine(String line,
                                     ImageStore imageStore)
   {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[PROPERTY_KEY])
         {
            case BGND_KEY:
               return parseBackground(properties, imageStore);
            case MINER_KEY:
               return parseMiner(properties,  imageStore);
            case OBSTACLE_KEY:
               return parseObstacle(properties, imageStore);
            case ORE_KEY:
               return parseOre(properties,  imageStore);
            case SMITH_KEY:
               return parseSmith(properties, imageStore);
            case VEIN_KEY:
               return parseVein(properties,imageStore);
         }
      }

      return false;
   }

   private boolean parseBackground(String [] properties,
                                        ImageStore imageStore)
   {
      if (properties.length == BGND_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                 Integer.parseInt(properties[BGND_ROW]));
         String id = properties[BGND_ID];
         setBackground(pt,
                 new Background(id, imageStore.getImageList(id)));
      }

      return properties.length == BGND_NUM_PROPERTIES;
   }

   private boolean parseMiner(String [] properties,
                                    ImageStore imageStore)
   {
      if (properties.length == MINER_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[MINER_COL]),
                 Integer.parseInt(properties[MINER_ROW]));
         WorldEntity entity = Miner_Not_Full.createMinerNotFull(properties[MINER_ID],
                 Integer.parseInt(properties[MINER_LIMIT]),
                 pt,
                 Integer.parseInt(properties[MINER_ACTION_PERIOD]),
                 Integer.parseInt(properties[MINER_ANIMATION_PERIOD]),
                 imageStore.getImageList( MINER_KEY));
         tryAddEntity( entity);
      }

      return properties.length == MINER_NUM_PROPERTIES;
   }

   private  boolean parseObstacle(String [] properties,
                                       ImageStore imageStore)
   {
      if (properties.length == OBSTACLE_NUM_PROPERTIES)
      {
         Point pt = new Point(
                 Integer.parseInt(properties[OBSTACLE_COL]),
                 Integer.parseInt(properties[OBSTACLE_ROW]));
         WorldEntity entity = Obstacle.createObstacle(properties[OBSTACLE_ID],
                 pt, imageStore.getImageList( OBSTACLE_KEY));
         tryAddEntity(entity);
      }

      return properties.length == OBSTACLE_NUM_PROPERTIES;
   }

   private  boolean parseOre(String [] properties,
                                  ImageStore imageStore)
   {
      if (properties.length == ORE_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[ORE_COL]),
                 Integer.parseInt(properties[ORE_ROW]));
         WorldEntity entity = Ore.createOre(properties[ORE_ID],
                 pt, Integer.parseInt(properties[ORE_ACTION_PERIOD]),
                 imageStore.getImageList( ORE_KEY));
         tryAddEntity( entity);
      }

      return properties.length == ORE_NUM_PROPERTIES;
   }

   private boolean parseSmith(String [] properties,
                                    ImageStore imageStore)
   {
      if (properties.length == SMITH_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[SMITH_COL]),
                 Integer.parseInt(properties[SMITH_ROW]));
         WorldEntity entity = Blacksmith.createBlacksmith(properties[SMITH_ID],
                 pt, imageStore.getImageList( SMITH_KEY));
         tryAddEntity( entity);
      }

      return properties.length == SMITH_NUM_PROPERTIES;
   }

   private boolean parseVein(String [] properties,
                                   ImageStore imageStore)
   {
      if (properties.length == VEIN_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[VEIN_COL]),
                 Integer.parseInt(properties[VEIN_ROW]));
         WorldEntity entity = Vein.createVein(properties[VEIN_ID],
                 pt,
                 Integer.parseInt(properties[VEIN_ACTION_PERIOD]),
                 imageStore.getImageList(VEIN_KEY));
         tryAddEntity( entity);
      }

      return properties.length == VEIN_NUM_PROPERTIES;
   }

   private void tryAddEntity( WorldEntity entity)
   {
      if (isOccupied(entity.getPosition()))
      {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         throw new IllegalArgumentException("position occupied");
      }

      addEntity( entity);
   }

   public  boolean withinBounds( Point pos)
   {
      return pos.getY() >= 0 && pos.getY() < numRows &&
              pos.getX() >= 0 && pos.getX()< numCols;
   }

   public  boolean isOccupied(Point pos)
   {
      return withinBounds( pos) &&
              getOccupancyCell(pos) != null;
   }

   private Optional<WorldEntity> nearestEntity(List<WorldEntity> entities,
                                                Point pos)
   {
      if (entities.isEmpty())
      {
         return Optional.empty();
      }
      else
      {
         WorldEntity nearest = entities.get(0);
         int nearestDistance = Point.distanceSquared(nearest.getPosition(), pos);

         for (WorldEntity other : entities)
         {
            int otherDistance = Point.distanceSquared(other.getPosition(), pos);

            if (otherDistance < nearestDistance)
            {
               nearest = other;
               nearestDistance = otherDistance;
            }
         }

         return Optional.of(nearest);
      }
   }


   public Optional<WorldEntity> findNearest( Point pos,
                                              Class kind)
   {
      List<WorldEntity> ofType = new LinkedList<>();
      for (WorldEntity entity : entities)
      {
         if ( entity.getClass() == kind)
         {
            ofType.add(entity);
         }
      }

      return nearestEntity(ofType, pos);
   }

   public void addEntity(WorldEntity entity)
   {
      if (withinBounds( entity.getPosition()))
      {
         setOccupancyCell(entity.getPosition(), entity);
         entities.add(entity);
      }
   }


   public  void removeEntity(WorldEntity entity)
   {
      removeEntityAt(entity.getPosition());
   }

   public void removeEntityAt( Point pos)
   {
      if (withinBounds( pos)
              && getOccupancyCell(pos) != null)
      {
         WorldEntity entity = getOccupancyCell( pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
          entity.setPosition( new Point(-1, -1));
         entities.remove(entity);
         setOccupancyCell( pos, null);
      }
   }

   public Optional<WorldEntity> getOccupant( Point pos)
   {
      if (isOccupied( pos))
      {
         return Optional.of(getOccupancyCell( pos));
      }
      else
      {
         return Optional.empty();
      }
   }

   private  WorldEntity getOccupancyCell( Point pos)
   {
      return this.occupancy[pos.getY()][pos.getX()];
   }

   public  void setOccupancyCell( Point pos,
                                       WorldEntity entity)
   {
      this.occupancy[pos.getY()][pos.getX()] = entity;
   }

   public  Background getBackgroundCell( Point pos)
   {
      return this.background[pos.getY()][pos.getX()];
   }

   private void setBackgroundCell(Point pos,
                                        Background background)
   {
      this.background[pos.getY()][pos.getX()] = background;
   }


   private void setBackground(Point pos, Background background)
   {
      if (withinBounds( pos))
      {
         setBackgroundCell( pos, background);
      }
   }


   public Optional<PImage> getBackgroundImage(Point pos)
   {
      if (withinBounds(pos))
      {
         return Optional.of(getBackgroundCell(pos).getCurrentImage());
      }
      else
      {
         return Optional.empty();
      }
   }

}
