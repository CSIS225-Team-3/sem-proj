using CliWrap;
using DotNetGraph.Compilation;
using DotNetGraph.Core;
using DotNetGraph.Extensions;

namespace HyperbolicGraphGen;

internal class Program
{
    static void Main(string[] args)
    {
        var (vertVer, tileVer) = Gen();


        //var tileVer = ConvertToTiles(vertVer);

        //Vert ConvertToTiles(Vert v)
        //{
        //    //var n1 = v.neighbors;
        //    ////2nd degree neighbors that aren't the parent
        //    //var n2 = n1.Select(n => n.neighbors.Where(n => n != v)).ToList();
        //    ////Closed neighbors
        //    //var n3 = n2.Where(n => n.Any(x => n1.Contains(x)))

        //    return v;
        //}






        var graph = new DotGraph().WithIdentifier("MyGraph");
        var directedGraph = new DotGraph().WithIdentifier("MyDirectedGraph");

        HashSet<(DotNode, DotNode)> edges = [];

        //Dictionary<Vert, DotNode> v_visited = [];
        //Convert(vertVer);

        Dictionary<Tile, DotNode> t_visited = [];
        Convert(tileVer);

        //DotNode Convert(Vert v)
        //{
        //    if (v_visited.TryGetValue(v, out var existingNode))
        //        return existingNode;

        //    var nodeId = v.GetHashCode().ToString();
        //    var node = new DotNode()
        //        .WithIdentifier(nodeId)
        //        .WithShape(DotNodeShape.Ellipse)
        //        .WithLabel(v.id.ToString());

        //    graph.Elements.Add(node);
        //    v_visited.Add(v, node);

        //    foreach (var neighbor in v.neighbors)
        //    {
        //        //Recurse for the neighbor's DotNode
        //        var neighborNode = Convert(neighbor);

        //        //A--B is the same as B--A, check to avoid duplicates
        //        if (!edges.Contains((node, neighborNode)) && !edges.Contains((neighborNode, node)))
        //        {
        //            var edge = new DotEdge()
        //                .From(node)
        //                .To(neighborNode);
        //            graph.Elements.Add(edge);
        //            edges.Add((node, neighborNode));
        //        }
        //    }

        //    return node;
        //}

        DotNode Convert(Tile v)
        {
            if (t_visited.TryGetValue(v, out var existingNode))
                return existingNode;

            var nodeId = v.GetHashCode().ToString();
            var node = new DotNode()
                .WithIdentifier(nodeId)
                .WithShape(DotNodeShape.Ellipse)
                .WithLabel(v.id.ToString());

            graph.Elements.Add(node);
            t_visited.Add(v, node);

            foreach (var neighbor in v.neighbors)
            {
                //Recurse for the neighbor's DotNode
                var neighborNode = Convert(neighbor);

                //A--B is the same as B--A, check to avoid duplicates
                if (!edges.Contains((node, neighborNode)) && !edges.Contains((neighborNode, node)))
                {
                    var edge = new DotEdge()
                        .From(node)
                        .To(neighborNode);
                    graph.Elements.Add(edge);
                    edges.Add((node, neighborNode));
                }
            }

            return node;
        }

        using var writer = new StringWriter();
        var context = new CompilationContext(writer, new CompilationOptions());
        graph.CompileAsync(context);

        var dotResult = writer.GetStringBuilder().ToString();

        File.WriteAllText("graph.dot", dotResult);

        var result = Cli.Wrap(@"C:\Users\kaned\Desktop\Programming Tools\Graphviz\bin\dot.exe")
                .WithArguments(args => args
                    .Add("-Tpng")
                    .Add("graph.dot")
                    .Add("-o")
                    .Add("img.png"))
                .WithValidation(CommandResultValidation.ZeroExitCode)
                .ExecuteAsync();

        Console.WriteLine($"Success: {result.Task.Result.IsSuccess}");
    }


    static (Vert vertGraph, Tile tileGraph) Gen()
    {
        Tile tile0 = new();
        Tile tile1 = new();
        tile0.AddNeighbor(tile1);

        var v0 = new Vert();
        var v1 = new Vert();
        v0.AddNeighbor(v1, tile0); //Bottom

        (var v2, var v3) = Extrude3L(v0, v1); //First tile
        v0.MarkBuilder(v2, tile0); //Left
        v1.MarkBuilder(v3, tile0); //Right

        (Vert v4, Vert v5) = Extrude3L(v2, v3); //2nd tile
        v4.MarkBuilder(v5, tile1);
        v3.MarkBuilder(v5, tile1);

        var l = v2;
        var r = v4;
        var nt = tile1;
        //for (int i = 0; i <= 58; i++)
        //for (int i = 0; i <= 10; i++)
        for (int i = 0; i <= 15; i++)
        {
            (l, r, nt) = Extrude(l, r, nt);
            Console.WriteLine($"{l.id} {r.id} {i}");
        }

        return (v0, tile0);

        static (Vert l, Vert r) Extrude3L(Vert parentL, Vert parentR)
        {
            if (parentR.neighbors.Count == 5)
                throw new Exception("Shouldn't be hit for these tests");
            else
            {
                //Full extrusion
                var nL = new Vert();
                var nR = new Vert();
                parentL.AddNeighbor(nL);
                parentR.AddNeighbor(nR);
                nL.AddNeighbor(nR);
                return (nL, nR);
            }
        }

        static (Vert l, Vert r, Tile newTile) Extrude(Vert parentL, Vert parentR, Tile parentTile)
        {
            Tile newTile = new();
            newTile.AddNeighbor(parentTile);

            if (parentL.neighbors.Count is 3 or 4)
            {
                //Full extrusion
                var nL = new Vert();
                var nR = new Vert();
                parentL.AddNeighbor(nL);
                parentR.AddNeighbor(nR, newTile);
                nL.AddNeighbor(nR, newTile);
                return (parentL, nL, newTile);
            }
            else if (parentL.neighbors.Count == 5)
            {
                //Merging extrusion
                //CCW
                var nL = (parentL.builderCons ?? []).Single();
                parentL.UnmarkBuilder(nL.Key, newTile);
                var nR = new Vert();
                parentR.AddNeighbor(nR, newTile);
                nL.Key.AddNeighbor(nR);
                Console.WriteLine("C");
                return (nL.Key, nR, newTile);
            }
            else
                throw new Exception("Uh oh");
        }
    }

    class Vert
    {
        static int instances = 0;
        public readonly int id = instances++;

        public List<Vert> neighbors = [];
        //Vert => Tile
        public Dictionary<Vert, Tile>? builderCons = null;

        public void AddNeighbor(Vert neighbor, Tile? tile = null)
        {
            if (neighbors.Contains(neighbor) || neighbor.neighbors.Contains(this))
                throw new Exception("Already a neighbor");

            neighbors.Add(neighbor);
            neighbor.neighbors.Add(this);

            if (tile != null)
                MarkBuilder(neighbor, tile);
        }

        public void RemoveNeighbor(Vert neighbor)
        {
            if (!neighbors.Contains(neighbor))
                throw new Exception("Not a neighbor");

            neighbors.Remove(neighbor);
            neighbor.neighbors.Remove(this);
        }

        public void MarkBuilder(Vert neighbor, Tile tile)
        {
            if (!neighbors.Contains(neighbor))
                throw new Exception("Not a neighbor");

            builderCons ??= [];
            builderCons.Add(neighbor, tile);

            neighbor.builderCons ??= [];
            neighbor.builderCons.Add(this, tile);
        }

        public void UnmarkBuilder(Vert neighbor, Tile newTile)
        {
            if (!neighbors.Contains(neighbor))
                throw new Exception("Not a neighbor");

            if (builderCons == null || neighbor.builderCons == null)
                throw new Exception("Error state? Or not a builder? idk");

            if (!builderCons.Remove(neighbor, out Tile? tile))
                throw new Exception();
            neighbor.builderCons.Remove(this);

            tile.AddNeighbor(newTile);

            if (builderCons.Count == 0)
                builderCons = null;
            if (neighbor.builderCons.Count == 0)
                neighbor.builderCons = null;
        }

        //public Vert? GetSharedVert(Vert other)
        //{
        //    foreach (var neighbor in neighbors)
        //        if (neighbor == other)
        //            throw new Exception("Direct Neighbor");

        //    foreach (var neighbor in neighbors)
        //        if (other.neighbors.Contains(neighbor))
        //            return neighbor;

        //    return null;
        //}

        public override string ToString() => id.ToString();
    }



    class Tile
    {
        static int instances = 0;
        public readonly int id = instances++;

        public List<Tile> neighbors = [];

        public void AddNeighbor(Tile neighbor)
        {
            if (neighbors.Contains(neighbor) || neighbor.neighbors.Contains(this))
                throw new Exception("Already a neighbor");

            neighbors.Add(neighbor);
            neighbor.neighbors.Add(this);
        }

        public override string ToString() => id.ToString();
    }
}
