//
//  SandpileVertex.java
//  Sandpiles
//
//  Created by Bryan Head on 9/22/08.
//  Copyright 2008 Reed College. All rights reserved.
//

import java.util.*;

public class SandpileVertex {
	private int sand;
	private HashMap<SandpileVertex,Integer> outVerts;
	private int degree;
	
	public SandpileVertex() {
		sand = 0;
		degree = 0;
		outVerts = new HashMap<SandpileVertex,Integer>();
	}
	
	/**
	 * Returns the number of outgoing edges from this vertex.
	 */
	public int degree() {
		return degree;
	}

	public boolean isSink() {
		return degree==0;
	}
	
	/**
	 * Returns true if the vertex is unstable, false otherwise.
	 */
	public boolean active() {
		if(this.degree()==0)	return false;
		return sand >= this.degree();
	}
	
	/**
	 * Adds an outgoing edge to the vertex.
	 */
	public void addOutgoingEdge(SandpileVertex destVert) {
		addOutgoingEdge(destVert,1);
	}
	
	/**
	 * Adds an outgoing edge to the vertex of the specified weight. If there is already an
	 * edge of a particular weight from this vertex to the destination vertex,
	 * the weight of that edge will be increased by the specified amount. If
	 * a negative weight is given, the weight will decrease by that amount, removing
	 * the connection if the weight drops to zero or less.
	 */
	public void addOutgoingEdge(SandpileVertex destVert, int weight) {
		if(outVerts.containsKey(destVert)){
			outVerts.put(destVert, weight(destVert)+weight);
		}else{
			outVerts.put(destVert, weight);
		}
		degree+=weight;
		if(weight(destVert)<=0){
			removeVertex(destVert);
		}
	}
	
	/**
	 * Retrieves the set of the vertices which this vertex has outgoing edges to.
	 */
	public Set<SandpileVertex> getOutgoingVertices() {
		return outVerts.keySet();
	}
	
	/**
	 * Adds the specified amount of sand to this vertex.
	 */
	public void addSand(int amount) {
		this.setSand(this.sand() + amount);
	}
	
	/**
	 * Sets the amount of sand on this vertex to the given value.
	 */
	public void setSand(int amount) {
		this.sand = amount;
	}
	
	/**
	 * Retrieves the amount of sand on this vertex.
	 */
	public int sand() {
		return this.sand;
	}
	
	/**
	 * Fires the vertex, even if it is stable.  Could result in negative sand.
	 */
	public void fire() {
		for(SandpileVertex v : this.outVerts.keySet()) {
			v.addSand(this.outVerts.get(v));
		}
		this.sand-=this.degree();
	}

	/**
	 * Sucks sand from neighbors equal to what it would give it if it fired.  Could result in neighbors having negative sand.
	 */
	public void reverseFire() {
		for(SandpileVertex v : this.outVerts.keySet()) {
			v.addSand(-this.outVerts.get(v));
		}
		this.addSand(this.degree());
	}
	
	/**
	 * Fires the vertex if it is unstable.
	 */
	public void update() {
		if(this.active())
			this.fire();
	}
        
    /**
     * Returns the number of edges from this vertex to the given vertex.
     */
    public int weight(SandpileVertex vert) {
		if(outVerts.containsKey(vert)){
			return outVerts.get(vert);
		}else{
			return 0;
		}
    }
	
	/**
	 * Remove all references to a vertex.
	 */
	public void removeVertex(SandpileVertex v) {
		degree-=weight(v);
		outVerts.remove(v);
	}
	
	/**
	 * Remove an edges between this vertex and the indicated one.
	 */
	public void removeEdge(SandpileVertex v, int weight) {
		addOutgoingEdge(v, -weight);
	}

	public void removeEdge(SandpileVertex v){
		removeEdge(v,1);
	}
	

}
