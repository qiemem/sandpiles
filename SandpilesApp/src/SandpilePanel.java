/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author headb
 */
import java.beans.*;
import java.io.Serializable;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

//import java.lang.Math;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;


public class SandpilePanel extends JPanel implements ActionListener, Serializable{
	public static final int ADD_VERT_STATE = 0;
	public static final int DEL_VERT_STATE = 1;
	public static final int ADD_EDGE_STATE = 2;
	public static final int DEL_EDGE_STATE = 3;
	public static final int ADD_UNDI_EDGE_STATE = 4;
	public static final int DEL_UNDI_EDGE_STATE = 5;
	public static final int ADD_SAND_STATE = 6;
	public static final int DEL_SAND_STATE = 7;
	public static final int MAKE_GRID_STATE = 8;
	
	private boolean repaint = true;
	private boolean labels = false;
	private boolean color = true;
	private boolean changingNodeSize = true;
	private boolean drawEdges = true;
	
    private SandpileGraph sg;
    
    static final int VERT_RADIUS = 10;
    static final Color[] SAND_COLOR = {Color.gray ,  Color.blue, Color.cyan ,Color.green, Color.red, Color.orange, Color.yellow};
	static final Color[] SAND_MONOCHROME = {new Color(25,25,25) , new Color(50,50,50), new Color(100,100,100), new Color(150,150,150), new Color(200,200,200), new Color(225,225,225), new Color(255,255,255)};

	private double scale = 1.0;
    
    ArrayList<Integer[]> vertexData;
	//only keeps track of the existence of edges, not of their weights
    ArrayList<HashSet<Integer>> edges;
    
    private int selectedVertex;
	
	
	public SandpilePanel() {
		initWithSandpileGraph(new SandpileGraph());
	}
    
    public SandpilePanel(SandpileGraph sg) {
		initWithSandpileGraph(sg);
    }
	
	private void initWithSandpileGraph(SandpileGraph sg) {
				//this.curState = ADD_VERT_STATE;
		
        this.sg = sg;
        vertexData = new ArrayList<Integer[]>();
        edges = new ArrayList<HashSet<Integer>>();
        //Integer[] pos1 = {20,20,0};
        //Integer[] pos2 = {20,50,0};
        //vertexData.add(pos1);
        //vertexData.add(pos2);
        
        //Integer[] e = {0,1};
        //edges.add(e);
        
        selectedVertex = -1;
        addMouseListener(new MouseInputAdapter() {
            public void mouseReleased(MouseEvent evt) {
				
				int x = evt.getX();
                int y = evt.getY();
                int touchVert = touchingVertex(x,y);
				if(touchVert<0) {
					selectedVertex = -1;
				}
				/*
				switch(curState) {
					case ADD_VERT_STATE:
						addVertMouseListener(evt);
						break;
					case DEL_VERT_STATE:
						delVertMouseListener(evt);
						break;
					case ADD_EDGE_STATE:
						addEdgeMouseListener(evt);
						break;
					case DEL_EDGE_STATE:
						delEdgeMouseListener(evt);
						break;
					case ADD_UNDI_EDGE_STATE:
						addUndiEdgeMouseListener(evt);
						break;
					case DEL_UNDI_EDGE_STATE:
						delUndiEdgeMouseListener(evt);
						break;
					//case ADD_SAND_STATE:
					//	addSandMouseListener(evt);
					//	break;
					//case DEL_SAND_STATE:
					//	delSandMouseListener(evt);
					//	break;
					//case MAKE_GRID_STATE:
					//	makeGrid(gridRows, gridCols, x,y);
					//	break;
				}*/
                repaint();
            }
			
			public void mouseDragged(MouseEvent evt) {
				System.out.println("Mouse drag");
				int x = evt.getX();
				int y = evt.getY();
				
				int touchVert = touchingVertex(x,y);
				if( touchVert>=0) {
					vertexData.get(touchVert)[0] = x;
					vertexData.get(touchVert)[1] = y;
				}
			}
			
        });
	}
	
	public void setRepaint(boolean val) {
		repaint = val;
		repaint();
	}
	
	public void setColor(boolean val) {
		color = val;
		repaint();
	}
	
	public void setLabels(boolean val) {
		labels = val;
		repaint();
	}

	public void setDrawEdges(boolean val){
		this.drawEdges = val;
		repaint();
	}

	public void setChangingNodeSize(boolean val) {
		changingNodeSize = val;
		repaint();
	}
    
    public void actionPerformed( ActionEvent evt) {
        update();
    }
    
    public void update() {
        sg.update();
        repaint();
    }
	
	/**
	 * @Override
	 */
    public void paintComponent(Graphics g) {
		if(!repaint)	return;
		//System.out.println("Repaint");
        super.paintComponent(g);
        
        updateSandCounts();
        
        if(drawEdges){
			for(int e1=0;e1<edges.size(); e1++) {
				for(Integer e2 : edges.get(e1)){
					Integer[] pos1 = vertexData.get(e1);
					Integer[] pos2 = vertexData.get(e2);
					g.setColor(Color.white);
					g.drawLine(scaleCoordinate(pos1[0]), scaleCoordinate(pos1[1]), scaleCoordinate(pos2[0]), scaleCoordinate(pos2[1]));
					g.setColor(Color.pink);
					if(labels)
						g.drawString(String.valueOf(sg.weight(e1,e2)), scaleCoordinate((int)(pos1[0]+0.8*(pos2[0]-pos1[0]))), scaleCoordinate((int)(pos1[1]+0.8*(pos2[1] - pos1[1]))));
				}
			}
		}

        for(int i = 0; i<vertexData.size(); i++) {
            Integer[] v = vertexData.get(i);
			int radius = VERT_RADIUS;
			
			if(changingNodeSize&&(sg.degree(i)>0 && sg.degree(i)>v[2]))
				radius = (int)(((float)v[2]+2)/(sg.degree(i)+2) * VERT_RADIUS);
            int colorNum = Math.max(0,Math.min(v[2],SAND_COLOR.length-1));
			if(color){
				g.setColor(SAND_COLOR[colorNum]);
			}else{
				g.setColor(SAND_MONOCHROME[colorNum]);
			}
			//g.setColor(new Color(+64));
            g.fillOval(scaleCoordinate(v[0]-radius), scaleCoordinate(v[1]-radius), scaleCoordinate(radius*2), scaleCoordinate(radius*2));
            if(i==selectedVertex){
                g.setColor(Color.cyan);
                g.drawOval(scaleCoordinate(v[0]-VERT_RADIUS), scaleCoordinate(v[1]-VERT_RADIUS), scaleCoordinate(VERT_RADIUS*2), scaleCoordinate(VERT_RADIUS*2));
            }
            g.setColor(Color.black);
			if(labels)
				g.drawString(String.valueOf(v[2]), scaleCoordinate(v[0]-VERT_RADIUS/2), scaleCoordinate(v[1]+VERT_RADIUS/2));
        }
    }
	
	public void addVertexControl(int x, int y) {
		int touchVert = touchingVertex(x,y);
		if(touchVert<0){
	        if(selectedVertex>=0) {
			    selectedVertex = -1;
            }else{
				//System.out.println("Adding vertex "+x+" "+y);
				addVertex(x,y);
            }
        }else{
			selectedVertex = touchVert;
		}
	}
	
	public void delVertexControl(int x, int y) {
		int touchVert = touchingVertex(x,y);
		if(touchVert>=0){
			//System.out.println("Del vertex "+x+" "+y);
			delVertex(touchVert);
		}else{
			selectedVertex = -1;
		}
	
	}
	
	public void addEdgeControl(int x, int y, int weight) {
		int touchVert = touchingVertex(x,y);
		if(touchVert>=0) {
			if(selectedVertex<0) {
				selectedVertex = touchVert;
			}else if(touchVert!=selectedVertex){
				addEdge(selectedVertex, touchVert, weight);
			}
		}else{
			selectedVertex=-1;
		}
	}
	
	public void delEdgeControl(int x, int y, int weight) {
		int touchVert = touchingVertex(x,y);
		if(touchVert>=0) {
			if(selectedVertex<0) {
				selectedVertex = touchVert;
			}else if(touchVert!=selectedVertex){
				delEdge(selectedVertex, touchVert, weight);
			}
		}else{
			selectedVertex = -1;
		}
	}
	
	public void addUndiEdgeControl(int x, int y, int weight) {
		int touchVert = touchingVertex(x,y);
		if(touchVert>=0) {
			if(selectedVertex<0) {
				selectedVertex = touchVert;
			}else if(touchVert!=selectedVertex){
				addEdge(selectedVertex, touchVert, weight);
				addEdge(touchVert, selectedVertex, weight);
			}
		}
	}
	
	public void delUndiEdgeControl(int x, int y, int weight) {
        int touchVert = touchingVertex(x,y);
		if(touchVert>=0) {
			if(selectedVertex<0) {
				selectedVertex = touchVert;
			}else if(touchVert!=selectedVertex){
				delEdge(selectedVertex, touchVert, weight);
				delEdge(touchVert, selectedVertex, weight);
			}
		}
	}
	
	public void addSandControl(int x, int y, int amount) {
		int touchVert = touchingVertex(x,y);
		if(touchVert>=0) {
			selectedVertex = touchVert;
			addSand(touchVert,amount);
		}
	}

	public void setSandControl(int x, int y, int amount) {
		int touchVert = touchingVertex(x,y);
		if(touchVert>=0) {
			selectedVertex = touchVert;
			setSand(touchVert,amount);
		}

	}
	
	public void makeGrid(int rows, int cols, int x, int y, int nBorder, int sBorder, int eBorder, int wBorder) {
		x = unscaleCoordinate(x);
		y = unscaleCoordinate(y);
		int gridSpacing = VERT_RADIUS*2;
		//int curVertDataSize = vertexData.size();
		int[][] gridRef = new int[rows][cols];
		int[] nBorderRef = new int[cols];
		int[] sBorderRef = new int[cols];
		int[] eBorderRef = new int[rows];
		int[] wBorderRef = new int[rows];
		
		//create vertices
		for(int i=0; i<rows; i++) {
			for(int j=0; j<cols; j++){
				gridRef[i][j]=vertexData.size();
				addVertexUnscaled(x+j*gridSpacing, y+i*gridSpacing);
			}
		}
		
		for(int i=0; i<cols; i++) {
			if(nBorder<2){
				nBorderRef[i]=vertexData.size();
				addVertexUnscaled(x+i*gridSpacing, y-gridSpacing);
			}
			if(sBorder<2){
				sBorderRef[i]=vertexData.size();
				addVertexUnscaled(x+i*gridSpacing, y+(rows)*gridSpacing);
			}
				
		}
		for(int i=0; i<rows; i++) {
			if(wBorder<2){
				wBorderRef[i]=vertexData.size();
				addVertexUnscaled(x-gridSpacing, y+i*gridSpacing);
			}
			if(eBorder<2){
				eBorderRef[i]=vertexData.size();
				addVertexUnscaled(x+(cols)*gridSpacing, y+i*gridSpacing);
			}	
		}
		//create edges
		for(int i=0; i<rows; i++) {
			for(int j=0; j<cols; j++){
				if(i==0) {
					if(nBorder==0){
						addEdge(gridRef[i][j], nBorderRef[j],1 );
					}else if(nBorder==1) {
						addEdge(gridRef[i][j], nBorderRef[j],1 );
						addEdge( nBorderRef[j], gridRef[i][j],1 );
					}
				}else{
					addEdge(gridRef[i][j], gridRef[i-1][j] );
				}
				
				if(i==rows-1){
					if(sBorder==0){
						addEdge(gridRef[i][j], sBorderRef[j],1 );
					}else if(sBorder==1) {
						addEdge(gridRef[i][j], sBorderRef[j],1 );
						addEdge( sBorderRef[j], gridRef[i][j],1 );
					}
				}else{
					addEdge(gridRef[i][j], gridRef[i+1][j] );
				}
				if(j==cols-1) {
					if(eBorder==0){
						addEdge(gridRef[i][j], eBorderRef[i],1 );
					}else if(eBorder==1) {
						addEdge(gridRef[i][j], eBorderRef[i],1 );
						addEdge( eBorderRef[i], gridRef[i][j],1 );
					}
				}else{
					addEdge(gridRef[i][j], gridRef[i][j+1] );
				}
				
				if(j==0){
					if(wBorder==0){
						addEdge(gridRef[i][j], wBorderRef[i],1 );
					}else if(wBorder==1) {
						addEdge(gridRef[i][j], wBorderRef[i],1 );
						addEdge( wBorderRef[i], gridRef[i][j],1 );
					}
				}else{
					addEdge(gridRef[i][j], gridRef[i][j-1] );
				}
				
			}
		}
		repaint();
	}
	
	public void makeHoneycomb( int radius, int x, int y, int borders){
		/*
		 * for borders:
		 * 0 - directed
		 * 1 - undirected
		 **/
		x = unscaleCoordinate(x);
		y = unscaleCoordinate(y);
		int gridSpacing = VERT_RADIUS*2;
		int curRowLength = radius;
		int[][] gridRef = new int[radius*2-1][radius*2-1];
		for(int i=0; i<radius*2-1; i++){
			for(int j=0; j<curRowLength; j++){
				gridRef[i][j]=vertexData.size();
				addVertexUnscaled(x+j*gridSpacing+(i+(radius-1)%2)%2*(gridSpacing/2) - curRowLength/2*(gridSpacing), y+i*(gridSpacing-4));
			}
			if(i<radius-1){
				curRowLength++;
			}else{
				curRowLength--;
			}
		}
		curRowLength = radius;
		for(int i=0; i<radius*2-1; i++){
			if(i==0||i==radius*2-2) continue;
			for(int j=0; j<curRowLength; j++){
				if(j==0) continue;
				addEdge(gridRef[i][j], gridRef[i][j-1]);
				addEdge(gridRef[i][j], gridRef[i][j+1]);
				if(i<radius-1){
					addEdge(gridRef[i][j], gridRef[i-1][j-1]);
					addEdge(gridRef[i][j], gridRef[i-1][j]);
					addEdge(gridRef[i][j], gridRef[i+1][j+1]);
					addEdge(gridRef[i][j], gridRef[i+1][j]);
				}else if(i==radius-1){
					addEdge(gridRef[i][j], gridRef[i-1][j-1]);
					addEdge(gridRef[i][j], gridRef[i-1][j]);
					addEdge(gridRef[i][j], gridRef[i+1][j-1]);
					addEdge(gridRef[i][j], gridRef[i+1][j]);
				}else{
					addEdge(gridRef[i][j], gridRef[i-1][j+1]);
					addEdge(gridRef[i][j], gridRef[i-1][j]);
					addEdge(gridRef[i][j], gridRef[i+1][j-1]);
					addEdge(gridRef[i][j], gridRef[i+1][j]);
				}
				
			}
			if(i<radius-1){
				curRowLength++;
			}else{
				curRowLength--;
			}
		}
		if(borders==1){
			for(int i=0;i<radius;i++){
				addEdge(gridRef[0][i], gridRef[1][i]);
				addEdge(gridRef[0][i], gridRef[1][i+1]);
				addEdge(gridRef[2*radius-2][i], gridRef[2*radius-3][i]);
				addEdge(gridRef[2*radius-2][i], gridRef[2*radius-3][i+1]);
				if(i>0){
					addEdge(gridRef[0][i], gridRef[0][i-1]);
					addEdge(gridRef[2*radius-2][i], gridRef[2*radius-2][i-1]);
				}
				if(i<radius-1){
					addEdge(gridRef[0][i], gridRef[0][i+1]);
					addEdge(gridRef[2*radius-2][i], gridRef[2*radius-2][i+1]);
				}
			}
			for(int i=1; i<radius-1;i++){
				addEdge(gridRef[i][0], gridRef[i-1][0]);
				addEdge(gridRef[i][0], gridRef[i+1][0]);
				addEdge(gridRef[i][0], gridRef[i][1]);
				addEdge(gridRef[i][0], gridRef[i+1][1]);

				addEdge(gridRef[radius+i-1][0], gridRef[radius+i][0]);
				addEdge(gridRef[radius+i-1][0], gridRef[radius+i-2][0]);
				addEdge(gridRef[radius+i-1][0], gridRef[radius+i-1][1]);
				addEdge(gridRef[radius+i-1][0], gridRef[radius+i-2][1]);

				addEdge(gridRef[i][radius+i-1], gridRef[i-1][radius+i-2]);
				addEdge(gridRef[i][radius+i-1], gridRef[i+1][radius+i]);
				addEdge(gridRef[i][radius+i-1], gridRef[i][radius+i-2]);
				addEdge(gridRef[i][radius+i-1], gridRef[i+1][radius+i-1]);

				addEdge(gridRef[radius+i-1][2*radius-i-2], gridRef[radius+i-2][2*radius-i-1]);
				addEdge(gridRef[radius+i-1][2*radius-i-2], gridRef[radius+i][2*radius-i-3]);
				addEdge(gridRef[radius+i-1][2*radius-i-2], gridRef[radius+i-1][2*radius-i-3]);
				addEdge(gridRef[radius+i-1][2*radius-i-2], gridRef[radius+i-2][2*radius-i-2]);
			}
			addEdge(gridRef[radius-1][0], gridRef[radius-2][0]);
			addEdge(gridRef[radius-1][0], gridRef[radius][0]);
			addEdge(gridRef[radius-1][0], gridRef[radius-1][1]);

			addEdge(gridRef[radius-1][2*radius-2], gridRef[radius-2][2*radius-3]);
			addEdge(gridRef[radius-1][2*radius-2], gridRef[radius][2*radius-3]);
			addEdge(gridRef[radius-1][2*radius-2], gridRef[radius-1][2*radius-3]);
		}
		
	}
	
	public void makeHexGrid(int rows, int cols, int x, int y, int nBorder, int sBorder, int eBorder, int wBorder) {
		x = unscaleCoordinate(x);
		y = unscaleCoordinate(y);
		int gridSpacing = VERT_RADIUS*2;
		//int curVertDataSize = vertexData.size();
		
		int[][] gridRef = new int[rows][cols];
		int[] nBorderRef = new int[cols+1];
		int[] sBorderRef = new int[cols+1];
		int[] eBorderRef = new int[rows];
		int[] wBorderRef = new int[rows];
		
		//create vertices
		for(int i=0; i<rows; i++) {
			for(int j=0; j<cols; j++){
				gridRef[i][j]=vertexData.size();
				addVertexUnscaled(x+j*gridSpacing + i%2*(gridSpacing/2), y+i*gridSpacing);
			}
		}
		
		for(int i=0; i<cols+1; i++) {
			if(nBorder<2){
				nBorderRef[i]=vertexData.size();
				addVertexUnscaled(x+i*gridSpacing-(gridSpacing/2), y-gridSpacing);
			}
			if(sBorder<2){
				sBorderRef[i]=vertexData.size();
				addVertexUnscaled(x+i*gridSpacing-(gridSpacing/2), y+(rows)*gridSpacing);
			}
				
		}
		
		for(int i=0; i<rows; i++) {
			if(wBorder<2){
				wBorderRef[i]=vertexData.size();
				addVertexUnscaled(x-gridSpacing + i%2*(gridSpacing/2), y+i*gridSpacing);
			}
			if(eBorder<2){
				eBorderRef[i]=vertexData.size();
				addVertexUnscaled(x+(cols)*gridSpacing + i%2*(gridSpacing/2), y+i*gridSpacing);
			}	
		}
		
		//create edges
		for(int i=0; i<rows; i++) {
			for(int j=0; j<cols; j++){
				if(i%2 == 0) {
					if(i==0) {
						if(nBorder==0){
							addEdge(gridRef[i][j], nBorderRef[j],1 );
							addEdge(gridRef[i][j], nBorderRef[j+1],1 );
						}else if(nBorder==1) {
							addEdge(gridRef[i][j], nBorderRef[j],1 );
							addEdge( nBorderRef[j], gridRef[i][j],1 );
							addEdge(gridRef[i][j], nBorderRef[j+1],1 );
							addEdge( nBorderRef[j+1], gridRef[i][j],1 );
						}
					}else{
						addEdge(gridRef[i][j], gridRef[i-1][j] );
						if(j==0){
							if(wBorder==0){
								addEdge(gridRef[i][j], wBorderRef[i-1] );
							}else if(wBorder==2){
								addEdge(gridRef[i][j], wBorderRef[i-1] );
								addEdge(wBorderRef[i-1], gridRef[i][j] );
							}
						}else{
							
							addEdge(gridRef[i][j], gridRef[i-1][j-1] );
						}
					}
					if(i==rows-1){
						if(sBorder==0){
							addEdge(gridRef[i][j], sBorderRef[j],1 );
							addEdge(gridRef[i][j], sBorderRef[j+1],1 );
						}else if(sBorder==1) {
							addEdge(gridRef[i][j], sBorderRef[j],1 );
							addEdge( sBorderRef[j], gridRef[i][j],1 );
							addEdge(gridRef[i][j], sBorderRef[j+1],1 );
							addEdge( sBorderRef[j+1], gridRef[i][j],1 );
						}
					}else{
						addEdge(gridRef[i][j], gridRef[i+1][j] );
						if(j==0){
							
							if(wBorder==0){
								addEdge(gridRef[i][j], wBorderRef[i+1] );
							}else if(wBorder==2){
								addEdge(gridRef[i][j], wBorderRef[i+1] );
								addEdge(wBorderRef[i+1], gridRef[i][j] );
							}
						}else{
							addEdge(gridRef[i][j], gridRef[i+1][j-1] );
						}
					}
				}else{
					if(i==rows-1){
						if(sBorder==0){
							addEdge(gridRef[i][j], sBorderRef[j],1 );
							addEdge(gridRef[i][j], sBorderRef[j+1],1 );
						}else if(sBorder==1) {
							addEdge(gridRef[i][j], sBorderRef[j],1 );
							addEdge( sBorderRef[j], gridRef[i][j],1 );
							addEdge(gridRef[i][j], sBorderRef[j+1],1 );
							addEdge( sBorderRef[j+1], gridRef[i][j],1 );
						}
						
					}else{
						if(j==cols-1){

							if(eBorder==0){
								addEdge(gridRef[i][j], eBorderRef[i+1] );
							}else if(eBorder==2){
								addEdge(gridRef[i][j], eBorderRef[i+1] );
								addEdge(eBorderRef[i+1], gridRef[i][j] );

							}
						}else{
							addEdge(gridRef[i][j], gridRef[i+1][j+1] );
						}
						addEdge(gridRef[i][j], gridRef[i+1][j] );
					}
					if(j==cols-1){
							
						if(eBorder==0){
							addEdge(gridRef[i][j], eBorderRef[i-1] );
						}else if(eBorder==2){
							addEdge(gridRef[i][j], eBorderRef[i-1] );
							addEdge(eBorderRef[i-1], gridRef[i][j] );
							
						}
					}else{
						addEdge(gridRef[i][j], gridRef[i-1][j+1] );
					}
					addEdge(gridRef[i][j], gridRef[i-1][j] );
				}
				if(j==cols-1) {
					if(eBorder==0){
						addEdge(gridRef[i][j], eBorderRef[i],1 );
					}else if(eBorder==1) {
						addEdge(gridRef[i][j], eBorderRef[i],1 );
						addEdge( eBorderRef[i], gridRef[i][j],1 );
					}
				}else{
					addEdge(gridRef[i][j], gridRef[i][j+1] );
				}

				if(j==0){
					if(wBorder==0){
						addEdge(gridRef[i][j], wBorderRef[i],1 );
					}else if(wBorder==1) {
						addEdge(gridRef[i][j], wBorderRef[i],1 );
						addEdge( wBorderRef[i], gridRef[i][j],1 );
					}
				}else{
					addEdge(gridRef[i][j], gridRef[i][j-1] );
				}
				
			}
		}
	}
	
	public void setToDualConfig() {
		/*for(int i = 0; i<vertexData.size(); i++){
			if(sg.degree(i)>0)
				sg.setSand(i, sg.degree(i)-1 - sg.getSand(i) );
		}*/
		sg.setSand(sg.getDualConfig());
		repaint();
	}
	
	public void addDualConfig() {
		sg.addSand(sg.getDualConfig());
		repaint();
	}
	
	public void setControlState(int controlState){
		//curState = controlState;
	}

	public void setScale(double scale){
		this.scale=scale;
		repaint();
	}

    public void updateSandCounts() {
        for(int i = 0; i<vertexData.size(); i++) {
            vertexData.get(i)[2]=sg.getSand(i);
        }
    }
	
	public void setToMaxStableConfig() {
		/*for(int i = 0; i<vertexData.size(); i++) {
			if(sg.degree(i)>0){
				//vertexData.get(i)[2] += sg.degree(i)-1;
				sg.addSand(i, sg.degree(i)-1);
			}
		}*/
		sg.setSand(sg.getMaxConfig());
		repaint();
	}
	
	public void addMaxStableConfig(){
		sg.addSand(sg.getMaxConfig());
		repaint();
	}
	
	public void addIdentity() {
		sg.addSand(sg.getIdentityConfig());
		repaint();
	}
	
	public void setToIdentity() {
		sg.setSand(sg.getIdentityConfig());
		repaint();
	}
	
	public void setSandEverywhere(int amount) {
		sg.setSandEverywhere(amount);
		repaint();
	}
	
	public void addSandEverywhere(int amount) {
		sg.addSandEverywhere(amount);
		repaint();
	}

	public void setToBurningConfig(){
		sg.setSand(sg.getMinimalBurningConfig());
		repaint();
	}

	public void addBurningConfig(){
		sg.addSand(sg.getMinimalBurningConfig());
		repaint();
	}

	public void clearSand() {
		for(int i = 0; i<vertexData.size(); i++) {
			vertexData.get(i)[2] = 0;
			sg.setSand(i, 0);
		}
		repaint();
	}
    
    public void addVertex(int x, int y) {
        sg.addVertex();
        Integer[] newPos = {unscaleCoordinate(x),unscaleCoordinate(y),0};
        vertexData.add(newPos);
		edges.add(new HashSet<Integer>());
        repaint();
    }

	private void addVertexUnscaled(int x, int y){
		sg.addVertex();
        Integer[] newPos = {x,y,0};
        vertexData.add(newPos);
		edges.add(new HashSet<Integer>());
        repaint();
	}
	
	public void delVertex(int v) {
		vertexData.remove(v);
		sg.removeVertex(v);
		edges.remove(v);
		for(int i = 0; i<edges.size(); i++){
			edges.get(i).remove(v);
			Set<Integer> updatedOutVerts = new HashSet<Integer>();
			for(Iterator<Integer> iter = edges.get(i).iterator(); iter.hasNext(); ){
				int u = iter.next();
				if(u>v){
					iter.remove();
					updatedOutVerts.add(u-1);
				}
			}
			edges.get(i).addAll(updatedOutVerts);
		}
	}

	public void delAllVertices() {
		vertexData.clear();
		edges.clear();
		sg.removeAllVertices();
		repaint();
	}

	public void addEdge(int originVert, int destVert) {
        addEdge(originVert, destVert, 1);
    }
	
    public void addEdge(int originVert, int destVert, int weight) {
        edges.get(originVert).add(destVert);
		sg.addEdge(originVert,destVert,weight);
    }
	
	public void delEdge(int originVert, int destVert) {
		this.delEdge(originVert, destVert, 1);
	}
	
	public void delEdge(int originVert, int destVert, int weight) {
		sg.removeEdge(originVert, destVert, weight);
		if(sg.weight(originVert,destVert)==0)
			edges.get(originVert).remove(destVert);
	}
    
    public void addSand(int vert, int amount) {
        sg.addSand(vert,amount);
        vertexData.get(vert)[2]+=amount;
    }

	public void setSand(int vert, int amount){
		sg.setSand(vert, amount);
		vertexData.get(vert)[2]=amount;
	}
    
    private int touchingVertex(int x, int y) {
        for( int i = 0; i<vertexData.size(); i++ ) {
            Integer[] v = vertexData.get(i);
            if(Math.sqrt((x-scaleCoordinate(v[0]))*(x-scaleCoordinate(v[0])) + (y-scaleCoordinate(v[1]))*(y-scaleCoordinate(v[1])))<=VERT_RADIUS) {
                return i;
            }
        }
        return -1;
    }

	private int scaleCoordinate(int coord){
		return (int)Math.ceil(coord*scale);
	}

	private int unscaleCoordinate(int coord){
		return (int) ((double)coord/scale);
	}
}
