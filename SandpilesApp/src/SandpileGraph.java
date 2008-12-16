//
//  SandpileGraph.java
//  Sandpiles
//
//  Created by Bryan Head on 9/22/08.
//  Copyright 2008 Reed College. All rights reserved.
//

import java.util.*;

public class SandpileGraph {
	private ArrayList<SandpileVertex> vertices;
	
	public SandpileGraph() {
		this.vertices = new ArrayList<SandpileVertex>();
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

	public void removeAllVertices() {
		this.vertices.clear();
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
		this.vertices.get(sourceVert).addOutgoingEdge(this.vertices.get(destVert), weight);
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
		this.vertices.get(sourceVert).removeEdge(this.vertices.get(destVert), weight);
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
	 * Sets the current overall config.
	 */
	public void setSand(List<Integer> sandConfig) {
		for(int i =0; i<sandConfig.size(); i++){
			this.setSand(i, sandConfig.get(i));
		}
	}
	
	public void setSandEverywhere(int amount) {
		for(SandpileVertex v : this.vertices){
			v.setSand(amount);
		}
	}
	
	public void addSandEverywhere(int amount) {
		for(SandpileVertex v : this.vertices) {
			v.addSand(amount);
		}
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
	public Set<Integer> getOutgoingVertices( int vert ){
		Set<SandpileVertex> outVerts = this.vertices.get(vert).getOutgoingVertices();
		Set<Integer> outVertIndices = new HashSet<Integer>(outVerts.size());
		for(SandpileVertex v : outVerts) {
			outVertIndices.add(vertices.indexOf(v));
		}
		return outVertIndices;
	}
	
	public Set<Integer> getIncomingVertices(int vert) {
		Set<Integer> incomingVertIndecies = new HashSet<Integer>();
		for(int i=0; i<this.vertices.size(); i++){
			if(this.vertices.get(i).weight(this.vertices.get(vert))>0){
				incomingVertIndecies.add(i);
			}
		}
		return incomingVertIndecies;
		
	}
	
	public boolean isSink(int vertIndex) {
		return this.vertices.get(vertIndex).isSink();
	}
	
	/**
	 * Fires the indicated vertex whether or not it is unstable.
	 */
	public void fireVertex(int vert) {
		this.vertices.get(vert).fire();
	}

	public void reverseFireVertex(int vert) {
		this.vertices.get(vert).reverseFire();
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
		Set<SandpileVertex> verticesToUpdate = new HashSet<SandpileVertex>();
		for( SandpileVertex v : this.vertices ){
			if(v.active())
				verticesToUpdate.add(v);
		}
		for( SandpileVertex v : verticesToUpdate)
			v.update();
	}
	
	public void stabilize() {
		while(!this.stable()) {
			this.update();
		}
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
	
	public List<Integer> getMaxConfig() {
		ArrayList<Integer> maxConfig = new ArrayList<Integer>(this.vertices.size());
		for(SandpileVertex v : this.vertices){
			maxConfig.add(v.degree()-1);
		}
		return maxConfig;
	}
	
	public List<Integer> getDualConfig() {
		ArrayList<Integer> dualConfig = new ArrayList<Integer>(this.vertices.size());
		for(SandpileVertex v : this.vertices) {
			dualConfig.add(v.degree() - 1 - v.sand());
		}
		return dualConfig;
	}
	
	public List<Integer> getCurrentConfig() {
		ArrayList<Integer> currentConfig = new ArrayList<Integer>(this.vertices.size());
		for(SandpileVertex v : this.vertices) {
			currentConfig.add(v.sand());
		}
		return currentConfig;
	}

	public List<Integer> getMinimalBurningConfig() {
		List<Integer> oldConfig = this.getCurrentConfig();
		this.setSandEverywhere(0);
		for(int i=0; i<this.vertices.size(); i++){
			reverseFireVertex(i);
		}
		int w = getInDebtVertex();
		while(w>-1){
			reverseFireVertex(w);
			w = getInDebtVertex();
		}
		List<Integer> burningConfig = this.getCurrentConfig();
		this.setSand(oldConfig);
		return burningConfig;
	}

	private int getInDebtVertex(){
		for(int i=0; i<this.vertices.size(); i++){
			if(this.getSand(i)<0&&!this.isSink(i)){
				return i;
			}
		}
		return -1;
	}
	
	public List<Integer> getIdentityConfig() {
		List<Integer> oldConfig = this.getCurrentConfig();
		List<Integer> maxConfig = this.getMaxConfig();
		this.setSand(maxConfig);
		this.addSand(maxConfig);
		this.stabilize();
		this.setSand(this.getDualConfig());
		this.addSand(maxConfig);
		this.stabilize();
		List<Integer> identity = this.getCurrentConfig();
		this.setSand(oldConfig);
		return identity;
	}
	
}
