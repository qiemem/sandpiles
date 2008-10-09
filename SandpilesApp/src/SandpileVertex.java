//
//  SandpileVertex.java
//  Sandpiles
//
//  Created by Bryan Head on 9/22/08.
//  Copyright 2008 Reed College. All rights reserved.
//

import java.util.Vector;
import java.util.List;

public class SandpileVertex {
	private int sand;
	private Vector<SandpileVertex> outVerts;
	
	public SandpileVertex() {
		sand = 0;
		outVerts = new Vector<SandpileVertex>();
	}
	
	/**
	 * Returns the number of outgoing edges from this vertex.
	 */
	public int degree() {
		return outVerts.size();
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
		outVerts.add(destVert);
	}
	
	/**
	 * Adds an outgoing edge to the vertex of the specified weight.
	 * Currently, this just adds [weight] outgoing edges to the vertex.
	 */
	public void addOutgoingEdge(SandpileVertex destVert, int weight) {
		for(int i=0; i<weight; i++) {
			this.addOutgoingEdge(destVert);
		}
	}
	
	/**
	 * Retrieves a list of the vertices which this vertex has outgoing edges to.
	 */
	public List<SandpileVertex> getOutgoingVertices() {
		return outVerts;
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
		for(SandpileVertex v : this.outVerts) {
			v.addSand(1);
		}
		this.sand-=this.degree();
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
		int wt=0;
        for( SandpileVertex v : outVerts) {
			if( v == vert ) {
				wt++;
            }
        }
        return wt;
    }
	
	/**
	 * Remove all references to a vertex.
	 */
	public void removeVertex(SandpileVertex v) {
		boolean keepGoing = outVerts.remove(v);
		while(keepGoing){
			keepGoing = outVerts.remove(v);
		}
	}
	
	/**
	 * Remove an edges between this vertex and the indicated one.
	 */
	public void removeEdge(SandpileVertex v) {
		outVerts.remove(v);
	}
	

}
