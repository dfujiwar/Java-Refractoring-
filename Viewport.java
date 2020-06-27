final class Viewport
{
   private int row;
   private int col;
   private int numRows;
   private int numCols;

   public Viewport(int numRows, int numCols)
   {
      this.numRows = numRows;
      this.numCols = numCols;
   }


   public int getCol() {
      return col;
   }

   public int getRow() {
      return row;
   }

   public int getNumCols() {
      return numCols;
   }

   public int getNumRows() {
      return numRows;
   }

   public void shift(int newcol, int newrow)
   {
      col = newcol;
      row = newrow;
   }

   public boolean contains( Point p)
   {
      return p.getY() >= row && p.getY() < row + numRows &&
              p.getX() >= col && p.getX() < col + numCols;
   }


   public  Point viewportToWorld(int newcol, int newrow)
   {
      return new Point(newcol + col, newrow + row);
   }

   public Point worldToViewport( int newcol, int newrow)
   {
      return new Point(newcol - col, newrow - row);
   }


}
