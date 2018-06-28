/**
 * This class provides a vertex object for graphs in the random graph diameter project.
 * 
 * @author Adam Warner
 * @modified 6/28/2018
 */

import java.util.Arrays;

public class RGVertex 
{
	int address, degree, size;
	int count = 0;
	boolean visited = false;
	int edge[];
	
	/**
	 * Constructor for the vertex object.
	 * @param index		The index of this vertex
	 * @param size		The size of the graph 
	 */
	public RGVertex(int index, int size)
	{
		address = index;
		this.size = size;
		edge = new int [size];
		Arrays.fill(edge, 0);
	}
				
	/**
	 * Assign edges and increase degree.
	 * 
	 * @param index		The index of this vertex
	 */
	public void setEdge(int index)
	{
		edge[index] = 1;
		degree++;
	}
	
	/**
	 * Returns the next edge connected to this vertex, or -1 if no edges are left.
	 * 
	 * @return	next		The address of the next vertex, or -1
	 */
	public int getNextEdge()
	{
		int next = -1;

		while (next < 0)
		{
			if (count < size)
			{
				if (edge[count] == 1)
				{
					next = count;
				}
				count++;
			} else
				break;
		}
		return next;
	}
	
	/**
	 * Reset tracking information for the next search.
	 */
	public void reset()
	{
		count = 0;
		visited = false;
	}
	
	/**
	 * Print a representation of this vertex to the console.
	 */
	public void display()
	{
		System.out.println("Index: " + address + "   Degree: " + degree);
		for (int i = 0; i < size; i++)
		{
			if (edge[i] != 0)
				System.out.println("Edge: " + address + " " + i);
		}
	}
}
