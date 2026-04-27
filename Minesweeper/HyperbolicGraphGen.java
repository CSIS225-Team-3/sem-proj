package Minesweeper;

import java.util.ArrayList;
import java.util.HashMap;
public class HyperbolicGraphGen
{
    static Tile gen()
    {
        Tile tile0 = new Tile();
        Tile tile1 = new Tile();
        tile0.addNeighbor(tile1);

        var v0 = new Vert();
        var v1 = new Vert();
        v0.addNeighbor(v1, tile0); //Bottom

        ExtrudeReturn e1 = Extrude3L(v0, v1); //First tile
        var v2 = e1.l;
        var v3 = e1.r;
        v0.markBuilder(v2, tile0); //Left
        v1.markBuilder(v3, tile0); //Right

        ExtrudeReturn e2 = Extrude3L(v2, v3); //2nd tile
        var v4 = e2.l;
        var v5 = e2.r;
        v4.markBuilder(v5, tile1);
        v3.markBuilder(v5, tile1);

        var l = v2;
        var r = v4;
        var nt = tile1;
        //for (int i = 0; i <= 58; i++)
        //for (int i = 0; i <= 10; i++)
        for (int i = 0; i <= 15; i++)
        {
            ExtrudeReturn e = Extrude(l, r, nt);
            l = e.l;
            r = e.r;
            nt = e.newTile;
        }

        return tile0;
    }

    

    static ExtrudeReturn Extrude3L(Vert parentL, Vert parentR)
    {
        //Full extrusion
        var nL = new Vert();
        var nR = new Vert();
        parentL.addNeighbor(nL, null);
        parentR.addNeighbor(nR, null);
        nL.addNeighbor(nR, null);
        return new ExtrudeReturn(nL, nR, null);
    }

    static ExtrudeReturn Extrude(Vert parentL, Vert parentR, Tile parentTile)
    {
        Tile newTile = new Tile();
        newTile.addNeighbor(parentTile);

        if (parentL.neighbors.size() == 3 || parentL.neighbors.size() == 4)
        {
            //Full extrusion
            var nL = new Vert();
            var nR = new Vert();
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
            parentL.unmarkBuilder(nL.getKey(), newTile);
            var nR = new Vert();
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

    public void addNeighbor(Vert neighbor, Tile tile)
    {
        if (neighbors.contains(neighbor) || neighbor.neighbors.contains(this))
            throw new IllegalStateException("Already a neighbor");

        neighbors.add(neighbor);
        neighbor.neighbors.add(this);

        if (tile != null)
            markBuilder(neighbor, tile);
    }

    public void removeNeighbor(Vert neighbor)
    {
        if (!neighbors.contains(neighbor))
            throw new IllegalStateException("Not a neighbor");

        neighbors.remove(neighbor);
        neighbor.neighbors.remove(this);
    }

    public void markBuilder(Vert neighbor, Tile tile)
    {
        if (!neighbors.contains(neighbor))
            throw new IllegalStateException("Not a neighbor");

        if (builderCons == null) new HashMap<>();
        builderCons.put(neighbor, tile);

        if (neighbor.builderCons == null) new HashMap<>();
        neighbor.builderCons.put(this, tile);
    }

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
}

class Tile
{
    static int instances = 0;
    int id = instances++;

    public ArrayList<Tile> neighbors = new ArrayList<>();

    public void addNeighbor(Tile neighbor)
    {
        if (neighbors.contains(neighbor) || neighbor.neighbors.contains(this))
            throw new IllegalStateException("Already a neighbor");

        neighbors.add(neighbor);
        neighbor.neighbors.add(this);
    }
}
