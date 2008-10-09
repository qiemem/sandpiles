//
//  SandpileGraph.java
//  Sandpiles
//
//  Created by Bryan Head on 9/22/08.
//  Copyright 2008 Reed College. All rights reserved.
//

import java.util.Vector;
import java.util.List;

public class SandpileGraph {
	private Vector<SandpileVertex> vertices;
	
	public SandpileGraph() {
		this.vertices = new Vector<SandpileVertex>();
	}
	
	/**
	 * Adds a vertex to the graph.
	 */
	public void addVertex() {
		this.vertices.add(new SandpileVertex());
	}
	
	/**
	 * Removes a vertex from the graph.
	 */
	public void removeVertex(int i) {
		SandpileVertex vert = vertices.get(i);
		for(SandpileVertex v : vertices) {
			v.removeVertex(vert);
		}
		this.vertices.remove(i);
	}
	
	/**
	 * Adds the given number of vertices to the graph.
	 */
	public void addVertices(int amount) {
		for(int i = 0; i<amount; i++)
			this.addVertex();
	}
	
	/**
	 * Adds an edge from the first vertex to the second.
	 */
	public void addEdge(int sourceVert, int destVert) {
		this.vertices.get(sourceVert).addOutgoingEdge(this.vertices.get(destVert));
	}
	
	/**
	 * Adds an edge of the given weight.
	 */
	public void addEdge(int sourceVert, int destVert, int weight) {
		for( int i =0; i<weight; i++) {
			this.vertices.get(sourceVert).addOutgoingEdge(this.vertices.get(destVert));
		}
	}
	
	/**
	 * Removes an edge from the first vertex to the second
	 */
	public void removeEdge(int sourceVert, int destVert) {
		this.vertices.get(sourceVert).removeEdge(this.vertices.get(destVert));
	}
	
	/**
	 * Removes an edge of the given weight.
	 */
	public void removeEdge(int sourceVert, int destVert, int weight) {
		for(int i=0; i<weight; i++){
			this.vertices.get(sourceVert).removeEdge(this.vertices.get(destVert));
		}
	}
	
	/**
	 * Adds the given amount of sand on the vertex indicated.
	 */
	public void addSand(int vert, int amount) {
		this.vertices.get(vert).addSand(amount);
	}
	
	/**
	 * Adds the given amount of sand in the list to the corresponding vertices.
	 */
	public void addSand(List<Integer> sandConfig) {
		for( int i = 0; i<sandConfig.size(); i++) {
			this.addSand(i, sandConfig.get(i));
		}
	}
	
	/**
	 * Sets the amount of sand on the vertex to the given amount.
	 */
	public void setSand(int vert, int amount) {
		this.vertices.get(vert).setSand(amount);
	}
	
	/**
	 * Retrieves the amount of sand on the vertex indicated.
	 */
	public int getSand(int vert) {
		return this.vertices.get(vert).sand();
	}
	
	/**
	 * Retrieves the number of outgoing edges of the given vertex.
	 */
	public int degree(int vert) {
		return this.vertices.get(vert).degree();
	}
	
	/**
	 * Retrieves a list of vertices which the given vertex has outgoing edges to.
	 */
	public List<Integer> getOutgoingVertices( int vert ){
		List<SandpileVertex> outVerts = this.vertices.get(vert).getOutgoingVertices();
		Vector<Integer> outVertIndices = new Vector<Integer>(outVerts.size());
		for(SandpileVertex v : outVerts) {
			outVertIndices.add(vertices.indexOf(v));
		}
		return outVertIndices;
	}
	
	/**
	 * Fires the indicated vertex whether or not it is unstable.
	 */
	public void fireVertex(int vert) {
		this.vertices.get(vert).fire();
	}
	
	/**
	 * Fires the indicated vertex if it is unstable.
	 */
	public void updateVertex(int vert) {
		this.vertices.get(vert).update();
	}
	
	/**
	 * Fires all unstable vertices.
	 */
	public void update() {
		Vector<SandpileVertex> verticesToUpdate = new Vector<SandpileVertex>();
		for( SandpileVertex v : this.vertices ){
			if(v.active())
				verticesToUpdate.add(v);
		}
		for( SandpileVertex v : verticesToUpdate)
			v.update();
	}
	
	/**
	 * Returns true if all vertices are stable; false otherwise.
	 */
	public boolean stable() {
		for( SandpileVertex v : vertices ) {
			if(v.active())
				return false;
		}
		return true;
	}
        
    /**
     * Returns the number of edges from the first vertex to the second.
     */
    public int weight(int originVert, int destVert) {
        return vertices.get(originVert).weight( vertices.get(destVert) );
    }
	
	/**
	 * Returns a string representation of the current sandpile configuration.
	 */
	public String configString() {
		String output = new String();
		for( int i = 0; i<vertices.size(); i++) {
			output = output.concat(i + " = " + vertices.get(i).sand() +"\n");
		}
		return output;
	}
        
    public String edgesString() {
	    String output = new String();
        for( int i = 0; i<vertices.size(); i++) {
            output = output.concat(i+ " " + vertices.get(i).degree()+"\n");
        }
        return output;
    }
	
}
