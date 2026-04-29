package Minesweeper;

import java.util.ArrayList;
import java.util.HashMap;
public class HyperbolicGraphGen
{
    static Tile[] gen()
    {
        ArrayList<Tile> tiles = new ArrayList<>();

        var v0 = new Vert();
        var v1 = new Vert();
        
        ExtrudeReturn e1 = extrude3L(v0, v1); //First tile
        var v2 = e1.l;
        var v3 = e1.r;
        Tile tile0 = new Tile(new Vert[] { v0, v1, v2, v3 });
        tiles.add(tile0);
        v0.addNeighbor(v1, tile0, Direction.Down); //Bottom edge
        v0.markBuilder(v2, tile0, Direction.Left); //Left edge
        v1.markBuilder(v3, tile0, Direction.Right); //Right edge
        
        ExtrudeReturn e2 = extrude3L(v2, v3); //2nd tile
        var v4 = e2.l;
        var v5 = e2.r;
        Tile tile1 = new Tile(new Vert[] { v2, v3, v4, v5 });
        tiles.add(tile1);
        tile0.addNeighbor(tile1, Direction.Up, Direction.Down);
        v4.markBuilder(v5, tile1, Direction.Up); //Top edge
        v3.markBuilder(v5, tile1, Direction.Right); //Right edge


        var l = v2;
        var r = v4;
        var nt = tile1;
        // for (int i = 0; i <= 58; i++)
        for (int i = 0; i <= 10; i++)
        // for (int i = 0; i <= 15; i++)
        {
            ExtrudeReturn e = extrude(l, r, nt);
            l = e.l;
            r = e.r;
            nt = e.newTile;
            tiles.add(nt);
        }

        return tiles.toArray(new Tile[0]);
    }

    /**
     * A simplified version of extrude which is used for the initial setup
     * @param parentL The left vertex
     * @param parentR The right vertex
     * @return The new vertices
     */
    private static ExtrudeReturn extrude3L(Vert parentL, Vert parentR)
    {
        //Full extrusion
        var nL = new Vert();
        var nR = new Vert();
        parentL.addNeighbor(nL, null, null);
        parentR.addNeighbor(nR, null, null);
        nL.addNeighbor(nR, null, null);
        return new ExtrudeReturn(nL, nR, null);
    }

    static ExtrudeReturn extrude(Vert parentL, Vert parentR, Tile parentTile)
    {
        if (parentL.neighbors.size() == 3 || parentL.neighbors.size() == 4)
        {
            //Full extrusion
            var nL = new Vert();
            var nR = new Vert();

            Tile newTile = new Tile(new Vert[] { parentL, parentR, nL, nR });
            //idk if that ternary is correct
            parentTile.addNeighbor(newTile, parentL.neighbors.size() == 4 ? Direction.Left : Direction.Up, Direction.Down);

            parentL.addNeighbor(nL, null, null);
            parentR.addNeighbor(nR, newTile, Direction.Right);
            nL.addNeighbor(nR, newTile, Direction.Up);
            return new ExtrudeReturn(parentL, nL, newTile);
        }
        else if (parentL.neighbors.size() == 5)
        {
            //Merging extrusion
            //CCW
            var nL = parentL.builderCons.entrySet().iterator().next();
            var nR = new Vert();
            
            Tile newTile = new Tile(new Vert[] { parentL, parentR, nL.getKey(), nR });
            //I'm not certain if Left is always the correct direction, but I think it is
            parentTile.addNeighbor(newTile, Direction.Left, Direction.Down);

            parentL.unmarkBuilder(nL.getKey(), newTile);
            parentR.addNeighbor(nR, newTile, Direction.Right);
            nL.getKey().addNeighbor(nR, null, null);
            return new ExtrudeReturn(nL.getKey(), nR, newTile);
        }
        else
            throw new IllegalStateException("Uh oh");
    }
}

class ExtrudeReturn
{
    public Vert l;
    public Vert r;
    public Tile newTile;

    public ExtrudeReturn(Vert l, Vert r, Tile newTile)
    {
        this.l = l;
        this.r = r;
        this.newTile = newTile;
    }
}

class Vert
{
    static int instances = 0;
    int id = instances++;

    public ArrayList<Vert> neighbors = new ArrayList<>();
    //Vert => Tile
    public HashMap<Vert, BuilderData> builderCons = null;

    /** Tiles this vertex is part of */
    public ArrayList<Tile> tiles = new ArrayList<>();

    /**
     * Add a neighbor to this vertex
     * @param neighbor The neighboring vertex
     * @param tile If supplied, the edge between this vertex and the neighbor will be marked as a builder edge associated with this tile
     * @param dir Which side of the tile this edge is on
     */
    public void addNeighbor(Vert neighbor, Tile tile, Direction dir)
    {
        if (neighbors.contains(neighbor) || neighbor.neighbors.contains(this))
            throw new IllegalStateException("Already a neighbor");

        neighbors.add(neighbor);
        neighbor.neighbors.add(this);
        
        if (tile != null)
            markBuilder(neighbor, tile, dir);
    }

    /**
     * Marks an edge with a neighbor as a builder
     * @param neighbor The neighboring vertex
     * @param tile The tile associated with this builder edge
     * @param dir Which side of the tile this edge is on
     */
    public void markBuilder(Vert neighbor, Tile tile, Direction dir)
    {
        if (!neighbors.contains(neighbor))
            throw new IllegalStateException("Not a neighbor");

        BuilderData data = new BuilderData();
        data.tile = tile;
        data.dir = dir;

        if (builderCons == null) builderCons = new HashMap<>();
        builderCons.put(neighbor, data);

        if (neighbor.builderCons == null) neighbor.builderCons = new HashMap<>();
        neighbor.builderCons.put(this, data);
    }

    /**
     * Unmarks an edge with a neighbor as a builder
     * @param neighbor The neighboring vertex
     * @param newTile The new tile that should be associated with the edge's other tile
     */
    public void unmarkBuilder(Vert neighbor, Tile newTile)
    {
        if (!neighbors.contains(neighbor))
            throw new IllegalStateException("Not a neighbor");

        if (builderCons == null || neighbor.builderCons == null)
            throw new IllegalStateException("Error state? Or not a builder? idk");

        BuilderData data = builderCons.remove(neighbor);
        neighbor.builderCons.remove(this);

        //The old tile should always be left of the new one since we're going CCW
        data.tile.addNeighbor(newTile, data.dir, Direction.Left);

        if (builderCons.size() == 0)
            builderCons = null;
        if (neighbor.builderCons.size() == 0)
            neighbor.builderCons = null;
    }

    public void addTile(Tile tile)
    {
        tiles.add(tile);
    }

    private class BuilderData {
        public Tile tile;
        public Direction dir;
    }
}

class Tile
{
    static int instances = 0;
    int id = instances++;

    /** Direct neighbors */
    public ArrayList<Tile> neighbors = new ArrayList<>();

    public Tile relDown;
    public Tile relLeft;
    public Tile relRight;
    public Tile relUp;

    public Vert[] vertices;

    public Tile(Vert[] vertices){
        this.vertices = vertices;
        for (Vert v : vertices)
            v.addTile(this);
    }

    /**
     * Adds a neighbor to this tile
     * @param neighbor The neighbor to add
     * @param selfDir Which (relative) side of this tile that neighbor is on
     * @param neighborsDir Which (relative) side of the neighbor that this tile is on
     */
    public void addNeighbor(Tile neighbor, Direction selfDir, Direction neighborsDir)
    {
        if (neighbors.contains(neighbor) || neighbor.neighbors.contains(this))
            throw new IllegalStateException("Already a neighbor");

        neighbors.add(neighbor);
        neighbor.neighbors.add(this);

        setRelSide(neighbor, selfDir);
        neighbor.setRelSide(this, neighborsDir);
    }

    private void setRelSide(Tile neighbor, Direction dir)
    {
        if (dir == Direction.Up){
            if (relUp != null)
                throw new IllegalStateException("Already have an up neighbor");
            relUp = neighbor;
            return;
        }
        if (dir == Direction.Down){
            if (relDown != null)
                throw new IllegalStateException("Already have a down neighbor");
            relDown = neighbor;
            return;
        }
        if (dir == Direction.Left){
            if (relLeft != null)
                throw new IllegalStateException("Already have a left neighbor");
            relLeft = neighbor;
            return;
        }
        if (dir == Direction.Right){
            if (relRight != null)
                throw new IllegalStateException("Already have a right neighbor");
            relRight = neighbor;
            return;
        }
        throw new IllegalStateException("Invalid direction");
    }
}

enum Direction {
    Up, Down, Left, Right
}
