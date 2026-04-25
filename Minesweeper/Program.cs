using CliWrap;
using DotNetGraph.Compilation;
using DotNetGraph.Core;
using DotNetGraph.Extensions;

namespace HyperbolicGraphGen;

internal class Program
{
    static void Main(string[] args)
    {
        var v = Gen();

        var graph = new DotGraph().WithIdentifier("MyGraph");
        var directedGraph = new DotGraph().WithIdentifier("MyDirectedGraph").Directed();

        Dictionary<Vert, DotNode> visited = [];
        HashSet<(DotNode, DotNode)> edges = [];

        Convert(v);

        DotNode Convert(Vert v)
        {
            if (visited.TryGetValue(v, out var existingNode))
                return existingNode;

            var nodeId = v.GetHashCode().ToString();
            var node = new DotNode()
                .WithIdentifier(nodeId)
                .WithShape(DotNodeShape.Ellipse)
                .WithLabel(v.id.ToString());

            graph.Elements.Add(node);
            visited.Add(v, node);

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

        //var dotResult = writer.GetStringBuilder().ToString();

        //File.WriteAllText("graph.dot", dotResult);

        //var result = Cli.Wrap(@"C:\Users\kaned\Desktop\Programming Tools\Graphviz\bin\dot.exe")
        //        .WithArguments(args => args
        //            .Add("-Tpng")
        //            .Add("graph.dot")
        //            .Add("-o")
        //            .Add("img.png"))
        //        .WithValidation(CommandResultValidation.ZeroExitCode)
        //        .ExecuteAsync();

        //Console.WriteLine($"Success: {result.Task.Result.IsSuccess}");
    }


    static Vert Gen()
    {
        var v1 = new Vert();
        var v2 = new Vert();
        v1.AddNeighbor(v2, true);

        //Core tile approach CCW
        (var tmp1, var tmp2) = Extrude3L(v1, v2); //First tile
        v1.MarkBuilder(tmp1);
        v2.MarkBuilder(tmp2);
        (v1, v2) = (tmp1, tmp2);

        (tmp1, Vert shell) = Extrude3L(v1, v2); //2nd tile
        tmp1.MarkBuilder(shell);
        v2.MarkBuilder(shell);
        v2 = tmp1;

        for (int i = 0; i < 1000; i++)
        {
            (v1, v2) = Extrude(v1, v2);
            Console.WriteLine($"{v1.id} {v2.id} {i}");
        }

        return v1;

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

        static (Vert l, Vert r) Extrude(Vert parentL, Vert parentR)
        {
            if (parentL.neighbors.Count is 3 or 4)
            {
                //Full extrusion
                var nL = new Vert();
                var nR = new Vert();
                parentL.AddNeighbor(nL);
                parentR.AddNeighbor(nR, true);
                nL.AddNeighbor(nR, true);
                return (parentL, nL);
            }
            else if (parentL.neighbors.Count == 5)
            {
                //Merging extrusion
                //CCW
                var nL = (parentL.builderCons ?? []).Single();
                parentL.UnmarkBuilder(nL);
                var nR = new Vert();
                parentR.AddNeighbor(nR, true);
                nL.AddNeighbor(nR);
                Console.WriteLine("C");
                return (nL, nR);
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
        public List<Vert>? builderCons = null;

        public void AddNeighbor(Vert neighbor, bool markBuilder = false)
        {
            if (neighbors.Contains(neighbor) || neighbor.neighbors.Contains(this))
                throw new Exception("Already a neighbor");

            neighbors.Add(neighbor);
            neighbor.neighbors.Add(this);

            if (markBuilder)
                MarkBuilder(neighbor);
        }

        public void MarkBuilder(Vert neighbor)
        {
            if (!neighbors.Contains(neighbor))
                throw new Exception("Not a neighbor");

            builderCons ??= [];
            builderCons.Add(neighbor);

            neighbor.builderCons ??= [];
            neighbor.builderCons.Add(this);
        }

        public void UnmarkBuilder(Vert neighbor)
        {
            if (!neighbors.Contains(neighbor))
                throw new Exception("Not a neighbor");

            if (builderCons == null || neighbor.builderCons == null)
                throw new Exception("Error state? Or not a builder? idk");

            builderCons.Remove(neighbor);
            neighbor.builderCons.Remove(this);

            if (builderCons.Count == 0)
                builderCons = null;
            if (neighbor.builderCons.Count == 0)
                neighbor.builderCons = null;
        }

        public Vert? GetSharedVert(Vert other)
        {
            foreach (var neighbor in neighbors)
                if (neighbor == other)
                    throw new Exception("Direct Neighbor");

            foreach (var neighbor in neighbors)
                if (other.neighbors.Contains(neighbor))
                    return neighbor;

            return null;
        }

        public override string ToString() => id.ToString();
    }
}
