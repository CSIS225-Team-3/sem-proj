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
        v0.addNeighbor(v1, tile0); //Bottom edge
        v0.markBuilder(v2, tile0); //Left edge
        v1.markBuilder(v3, tile0); //Right edge
        
        ExtrudeReturn e2 = extrude3L(v2, v3); //2nd tile
        var v4 = e2.l;
        var v5 = e2.r;
        Tile tile1 = new Tile(new Vert[] { v2, v3, v4, v5 });
        tiles.add(tile1);
        tile0.addNeighbor(tile1);
        v4.markBuilder(v5, tile1); //Top edge
        v3.markBuilder(v5, tile1); //Right edge


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

    static ExtrudeReturn extrude3L(Vert parentL, Vert parentR)
    {
        //Full extrusion
        var nL = new Vert();
        var nR = new Vert();
        parentL.addNeighbor(nL, null);
        parentR.addNeighbor(nR, null);
        nL.addNeighbor(nR, null);
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
            newTile.addNeighbor(parentTile);

            parentL.addNeighbor(nL, null);
            parentR.addNeighbor(nR, newTile);
            nL.addNeighbor(nR, newTile);
            return new ExtrudeReturn(parentL, nL, newTile);
        }
        else if (parentL.neighbors.size() == 5)
        {
            //Merging extrusion
            //CCW
            var nL = parentL.builderCons.entrySet().iterator().next();
            var nR = new Vert();
            
            Tile newTile = new Tile(new Vert[] { parentL, parentR, nL.getKey(), nR });
            newTile.addNeighbor(parentTile);

            parentL.unmarkBuilder(nL.getKey(), newTile);
            parentR.addNeighbor(nR, newTile);
            nL.getKey().addNeighbor(nR, null);
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
    public HashMap<Vert, Tile> builderCons = null;

    /** Tiles this vertex is part of */
    public ArrayList<Tile> tiles = new ArrayList<>();

    /**
     * Add a neighbor to this vertex
     * @param neighbor The neighboring vertex
     * @param tile If supplied, the edge between this vertex and the neighbor will be marked as a builder edge associated with this tile
     */
    public void addNeighbor(Vert neighbor, Tile tile)
    {
        if (neighbors.contains(neighbor) || neighbor.neighbors.contains(this))
            throw new IllegalStateException("Already a neighbor");

        neighbors.add(neighbor);
        neighbor.neighbors.add(this);
        
        if (tile != null)
            markBuilder(neighbor, tile);
    }

    /**
     * Marks an edge with a neighbor as a builder
     * @param neighbor The neighboring vertex
     * @param tile The tile associated with this builder edge
     */
    public void markBuilder(Vert neighbor, Tile tile)
    {
        if (!neighbors.contains(neighbor))
            throw new IllegalStateException("Not a neighbor");

        if (builderCons == null) builderCons = new HashMap<>();
        builderCons.put(neighbor, tile);

        if (neighbor.builderCons == null) neighbor.builderCons = new HashMap<>();
        neighbor.builderCons.put(this, tile);
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

        Tile tile = builderCons.remove(neighbor);
        neighbor.builderCons.remove(this);

        tile.addNeighbor(newTile);

        if (builderCons.size() == 0)
            builderCons = null;
        if (neighbor.builderCons.size() == 0)
            neighbor.builderCons = null;
    }

    public void addTile(Tile tile)
    {
        tiles.add(tile);
    }
}

class Tile
{
    static int instances = 0;
    int id = instances++;

    /** Direct neighbors */
    public ArrayList<Tile> neighbors = new ArrayList<>();

    public Vert[] vertices;

    public Tile(Vert[] vertices){
        this.vertices = vertices;
        for (Vert v : vertices)
            v.addTile(this);
    }

    public void addNeighbor(Tile neighbor)
    {
        if (neighbors.contains(neighbor) || neighbor.neighbors.contains(this))
            throw new IllegalStateException("Already a neighbor");

        neighbors.add(neighbor);
        neighbor.neighbors.add(this);
    }
}
