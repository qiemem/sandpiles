/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author headb
 */
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;
//import java.util.LinkedList;

public class SandpileGraph2 {
	private HashMap<String, HashMap<String,Integer>> outgoingEdges;		//has weight info
	private HashMap<String, HashSet<String>> incomingEdges;		//doesn't have weight info
	private HashMap<String, Integer> sandConfig;
	private HashMap<String, Integer> degree;	//to speed up stability checks.
	private HashSet<Integer> unstableVertices;
	
	public SandpileGraph2() {
		outgoingEdges = new HashMap<String, HashMap<String,Integer>>();
		sandConfig = new HashMap<String, Integer>();
		degree = new ArrayList<Integer>();
		unstableVertices = new HashSet<Integer>();
	}
	
	/**
	 * Adds a vertex to the graph.  Runs in linear time (I think).
	 */
	public void addVertex() {
		for( ArrayList<Integer> v : adjMatrix)
			v.add(0);
		
		ArrayList<Integer> newRow = new ArrayList<Integer>(adjMatrix.size());
		adjMatrix.add(newRow);
		for(int i=0; i<adjMatrix.size(); i++)
			newRow.add(0);
		sandConfig.add(0);
		degree.add(0);
	}
	
	/**
	 * Removes a vertex from the graph.
	 * Runs in quadratic time with respect to the number of vertices (removes an element from
	 * n ArrayLists).
	 */
	public void removeVertex(int vert) {
		adjMatrix.remove(vert);
		sandConfig.remove(vert);
		degree.remove(vert);
		for(int i = 0; i<adjMatrix.size();i++ ){
			degree.set(i, degree.get(i) - adjMatrix.get(i).get(vert) );
			adjMatrix.remove(vert);
			updateStability(i);
		}
		
	}
	
	/**
	 * Adds the given number of vertices to the graph.  Runs in amount*n.
	 */
	public void addVertices(int amount) {
		for(int i = 0; i<amount; i++)
			this.addVertex();
	}
	
	/**
	 * Adds an edge from the first vertex to the second.  If the edge already exists, increases its weight by 1.
	 * Runs in constant time.
	 */
	public void addEdge(int sourceVert, int destVert) {
		addEdge(sourceVert,destVert,1);
		
	}
	
	/**
	 * Adds an edge of the given weight.  If the edge already exists, adds weight to the edge's weight.
	 * Runs in constant time.
	 */
	public void addEdge(int sourceVert, int destVert, int weight) {
		adjMatrix.get(sourceVert).set(destVert, adjMatrix.get(sourceVert).get(destVert)+weight);
		degree.set(sourceVert, degree.get(sourceVert)+weight);
		updateStability(sourceVert);
	}
	
	/**
	 * Removes an edge from the first vertex to the second.  Runs in constant time.
	 */
	public void removeEdge(int sourceVert, int destVert) {
		degree.set(sourceVert, degree.get(sourceVert)-adjMatrix.get(sourceVert).get(destVert));
		adjMatrix.get(sourceVert).set(destVert, 0);
		updateStability(sourceVert);
	}
	
	/**
	 * Removes an edge of the given weight.  Runs in constant 
	 */
	public void removeEdge(int sourceVert, int destVert, int weight) {
		
		if( adjMatrix.get(sourceVert).get(destVert)<weight){
			removeEdge(sourceVert, destVert);
		}else {
			degree.set(sourceVert, degree.get(sourceVert)-weight);
			adjMatrix.get(sourceVert).set(destVert, adjMatrix.get(sourceVert).get(destVert)-weight);
		}
	}
	
	/**
	 * Adds the given amount of sand on the vertex indicated.
	 */
	public void addSand(int vert, int amount) {
		setSand(vert, sandConfig.get(vert)+amount);
		updateStability(vert);
	}
	
	/**
	 * Adds the given amount of sand in the list to the corresponding vertices.
	 */
	public void addSand(List<Integer> aSandConfig) {
		for( int i = 0; i<aSandConfig.size(); i++) {
			this.addSand(i, aSandConfig.get(i));
		}
	}
	
	/**
	 * Sets the amount of sand on the vertex to the given amount.
	 */
	public void setSand(int vert, int amount) {
		this.sandConfig.set(vert,amount);
		updateStability(vert);
	}
	
	/**
	 * Retrieves the amount of sand on the vertex indicated.
	 */
	public int getSand(int vert) {
		return this.sandConfig.get(vert);
	}
	
	/**
	 * Retrieves the number of outgoing edges of the given vertex.  Runs in constant time.
	 */
	public int degree(int vert) {
		return degree.get(vert);
	}
	
	/**
	 * Retrieves a list of vertices which the given vertex has outgoing edges to.
	 * Linear time, I think, but should be pretty fast in practice.
	 */
	public List<Integer> getOutgoingVertices( int vert ){
		ArrayList<Integer> edgeData = this.adjMatrix.get(vert);
		ArrayList<Integer> outVerts = new ArrayList<Integer>();
		for(int i = 0; i<edgeData.size(); i++) {
			if(edgeData.get(i)>0)
				outVerts.add(i);
		}
		return outVerts;
	}
	
	/**
	 * Fires the indicated vertex whether or not it is unstable.  Linear time, should be fast in practice.
	 */
	public void fireVertex(int vert) {
		ArrayList<Integer> edgeData = adjMatrix.get(vert);
		for(int i =0; i<edgeData.size(); i++ ) {
			if(edgeData.get(i)>0)
				addSand(i, edgeData.get(i));
		}
		addSand(vert, -degree.get(vert));
		updateStability(vert);
	}
	
	/**
	 * Returns true if the vertex is unstable, false otherwise.
	 */
	public boolean unstable(int vert) {
		return degree(vert)<=sandConfig.get(vert);
	}
	
	private void updateStability(int vert) {
		if(unstable(vert)) {
			unstableVertices.add(vert);
		}else{
			unstableVertices.remove(vert);
		}
	}
	
	/**
	 * Fires the indicated vertex if it is unstable.
	 */
	public void updateVertex(int vert) {
		if(unstable(vert))
			fireVertex(vert);
		updateStability(vert);
	}
	
	/**
	 * Fires all unstable vertices. Linear time.
	 */
	public void update() {
		//System.out.println(unstableVertices.toString());
		Object[] verticesToUpdate = unstableVertices.toArray();
		int n = unstableVertices.size();
		for( int i = 0; i<n; i++ )
			fireVertex(((Integer)verticesToUpdate[i]));
	}
	
	/**
	 * Returns true if all vertices are stable; false otherwise. Contant time.
	 */
	public boolean stable() {
		return unstableVertices.isEmpty();
	}
        
    /**
     * Returns the number of edges from the first vertex to the second. Constant time.
     */
    public int weight(int originVert, int destVert) {
        return adjMatrix.get(originVert).get(destVert);
    }

	/**
	 * Returns a string representation of the current sandpile configuration.
	 */
	public String configString() {
		String output = new String();
		for( int i = 0; i<sandConfig.size(); i++) {
			output = output.concat(i + " = " + sandConfig.get(i) +"\n");
		}
		return output;
	}
       /* 
    public String edgesString() {
	    String output = new String();
        for( int i = 0; i<vertices.size(); i++) {
            output = output.concat(i+ " " + vertices.get(i).degree()+"\n");
        }
        return output;
    }*/
}
