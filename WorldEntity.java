import processing.core.PImage;

import java.util.List;

public abstract class WorldEntity {
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;

    public WorldEntity( final String id, Point position, final List<PImage> images){
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex =0;
    }

    public String getId() {
        return id;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public void setImageIndex(int index){
        this.imageIndex = index;
    }

    public List<PImage> getImages() {
        return images;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point pos){
        this.position = pos;
    }


    public PImage getCurrentImage()
    {
        return (images.get((imageIndex)));
    }

}
