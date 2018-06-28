/**
 * This class provides a graph object for use in the random graph diameter project.
 * 
 * @author Adam Warner
 * @modified 6/282018
 */

import java.util.LinkedList;
import java.util.Queue;

public class RGObject 
{
	int size;
	double diameter;
	RGVertex vertex[];
	Queue<RGVertex> queue = new LinkedList<RGVertex>();
	
	/**
	 * Constructor for the graph object.
	 * 
	 * @param size	The size of the graph.
	 */
	public RGObject(int size)
	{
		this.size = size;
		vertex = new RGVertex[size];
		for (int i = 0; i < size; i++)
		{
			vertex[i] = new RGVertex(i, size);
		}
	}
	
	/**
	 * Connects two vertices with an edge.
	 * 
	 * @param index1
	 * @param index2
	 */
	public void setEdge(int index1, int index2)
	{
		vertex[index1].setEdge(index2);
		vertex[index2].setEdge(index1);
	}
	
	/**
	 * Displays the degree and connecting edges of all vertices in this graph.
	 */
	public void display()
	{
		for (int i = 0; i < size; i++)
		{
			vertex[i].display();
		}
	}
	
	/**
	 * Finds the maximum diameter of the graph by comparing the radius of each vertex.
	 * 
	 * @return	diameter		Maximum diameter
	 */
	public double getDiameter()
	{
		int radius;
		
		for (int a = 0; a < size; a++)
		{
			for (int b = 0; b < size; b++)
			{
				if (a != b)
				{
					radius = this.search(a, b);
					if (radius > diameter)
						diameter = radius;
					
					this.resetVertices();
				}
			}
		}
		
		return diameter;
	}
	
	/**
	 * Resets search tracking information for all vertices.
	 */
	public void resetVertices()
	{
		for (int i = 0; i < size; i++)
		{
			vertex[i].reset();
		}
	}
	
	/**
	 * Perform a breadth-first search on the graph.
	 * 
	 * @param 	start		Initial vertex
	 * @param 	end			Final vertex
	 * @return	jumps		Number of jumps
	 */
	public int search(int start, int end)
	{
		int jumps = 0;
		int count = 0;
		int child = -2;
		RGVertex next;

		queue.add(vertex[start]);
		
		while (!queue.isEmpty())
		{
			next = queue.remove();
			
			if (count == 0)
			{
				count = (next.degree + 1);
				if (count != 1)
					jumps++;
				
			}
			
			if (next.address == end)
			{
				queue.clear();
				next.visited = true;
			} 
			
			//add vertices from edges		
			if (next.visited == false)
			{
				vertex[next.address].visited = true;
				while (child != -1)
				{
					child = next.getNextEdge();
					if (child > -1 && child < size)
					{
						queue.add(vertex[child]);
					}
				}
				child = -2;
				count--;
			}
		}
		
		return jumps;
	}
}
