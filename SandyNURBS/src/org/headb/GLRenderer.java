/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.headb;
import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.j2d.TextRenderer;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.*;

/**
 *
 * @author headb
 */
public class GLRenderer extends MouseInputAdapter implements GLEventListener, MouseWheelListener{
	private GLCanvas canvas;
	private float zRot = 0.0f, xRot = 0.0f, yRot = 0.0f;
	private float zoom = -150.0f;

	/*private float[] controlPoints = {-10.0f, 0.0f, 0.0f,
									 -5.0f, -3.0f, 0.0f,
									 0.0f, 5.0f, 0.0f,
									 5.0f, -3.0f , 0.0f,
									 10.0f, 0.0f, 0.0f};*/
	private float[] controlPoints;
	private float[] rowKnots;
	private float[] colKnots;
	private int mouseX = 0;
	private int mouseY = 0;

	private GLUnurbs theNurb;

	private int[][] sandpileGrid;
	private int[][] lastSandpileGrid;
	private long delay = 100;
	private long lastTime = System.currentTimeMillis();
	private long lastRepaint = System.currentTimeMillis();
	private boolean paused = true;
	private boolean displayCtrlPts = false;
	private boolean showFPS = false;
	private boolean interpolate = true;
	private boolean showNURBS = true;

	private float scale = 5.0f;

	public GLRenderer(){

		sandpileGrid = new int[51][51];
		canvas = new GLCanvas();
		controlPoints = genControlPointsGrid(sandpileGrid.length,sandpileGrid[0].length);
		rowKnots = genUniformKnots(4,sandpileGrid.length);
		colKnots = genUniformKnots(4,sandpileGrid[0].length);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addMouseWheelListener(this);
		addSandEverywhere(6);
		lastSandpileGrid = sandpileGrid.clone();

		canvas.addKeyListener(new KeyAdapter(){
			@Override public void keyPressed(KeyEvent e){
				switch(e.getKeyCode()){
					case(KeyEvent.VK_0):
						setSandEverywhere(0); break;
					case(KeyEvent.VK_1):
						addSandEverywhere(1); break;
					case(KeyEvent.VK_P):
						paused = !paused; break;
					case(KeyEvent.VK_MINUS):
						delay+=5; System.err.println(delay); break;
					case(KeyEvent.VK_EQUALS):
						delay-=5; System.err.println(delay); break;
					case(KeyEvent.VK_C):
						displayCtrlPts = !displayCtrlPts; break;
					case(KeyEvent.VK_F):
						showFPS = !showFPS; break;
					case(KeyEvent.VK_I):
						interpolate = !interpolate; break;
					case(KeyEvent.VK_D):
						sandpileGrid[sandpileGrid.length/2][sandpileGrid[0].length/2]++; break;
					case(KeyEvent.VK_COMMA):
						scale -= 0.1f; break;
					case(KeyEvent.VK_PERIOD):
						scale += 0.1f; break;
					case(KeyEvent.VK_U):
						sandpileGrid = updateSand(sandpileGrid); break;
					case(KeyEvent.VK_N):
						showNURBS = !showNURBS; break;
					case(KeyEvent.VK_R):
						sandpileGrid[(int)(sandpileGrid.length*Math.random())][(int)(sandpileGrid[0].length*Math.random())]++;
				}
			}
		});
	}

	public void addSandEverywhere(int amount){
		for(int i = 0; i<sandpileGrid.length; i++){
			for(int j = 0; j<sandpileGrid[i].length; j++){
				sandpileGrid[i][j]+=amount;
			}
		}
	}
	public void setSandEverywhere(int amount){
		for(int i = 0; i<sandpileGrid.length; i++){
			for(int j = 0; j<sandpileGrid[i].length; j++){
				sandpileGrid[i][j]=amount;
			}
		}
	}

	public void sineNURBS(float[] ctrlPts, int h, int w, float phase, float amplitude){
		for(int i = 0; i<h; i++){
			for(int j = 0; j<w; j++){
				float theta1 = 2f*(float)Math.PI*((float)i)/((float)h);
				float theta2 = 2f*(float)Math.PI*((float)j)/((float)w);
				ctrlPts[i*w*3+j*3+2] = 0.5f*amplitude*(float)(Math.sin(phase+theta1)+Math.sin(phase+theta2));
			}
		}
	}

	public void sandNurbs(float[] ctrlPts, float scale, int[][] config){
		int h = config.length;
		int w = config[0].length;
		for(int i = 0; i<h; i++){
			for(int j = 0; j<w; j++){
				ctrlPts[i*w*3+j*3+2] = scale*config[i][j];
			}
		}
	}

	public void interpSandNurbs(float[] ctrlPts, float scale, int[][] lastConfig, int[][] curConfig, float transition){
		int h = curConfig.length;
		int w = curConfig[0].length;
		for(int i = 0; i<h; i++){
			for(int j = 0; j<w; j++){
				//System.err.println(scale*((1.0f- transition)*(float)lastConfig[i][j] + transition*(float)curConfig[i][j]));
				ctrlPts[i*w*3+j*3+2] = scale*((1.0f- transition)*(float)lastConfig[i][j] + transition*(float)curConfig[i][j]);
			}
		}
	}

	public int[][] updateSand(int[][] config) {
		int rows = config.length;
		int cols = config[0].length;
		int[][] nextConfig = new int[rows][cols];
		for(int r=0; r<rows; r++){
			for(int c=0; c<cols; c++){
				nextConfig[r][c]+=config[r][c];
				if(config[r][c]>3){
					nextConfig[r][c]-=4;
					if(r>0)
						nextConfig[r-1][c]+=1;
					if(r<rows-1)
						nextConfig[r+1][c]+=1;
					if(c>0)
						nextConfig[r][c-1]+=1;
					if(c<cols-1)
						nextConfig[r][c+1]+=1;
				}
			}
		}
		return nextConfig;
	}


	@Override public void mousePressed(MouseEvent e){
		//System.err.println("Pressed: "+ e.getX()+" "+e.getY());
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override public void mouseDragged(MouseEvent e){
		//System.err.println("Dragged: " + e.getX() + " " + e.getY());
		int deltaX = e.getX()-mouseX;
		int deltaY = e.getY()-mouseY;
		yRot += deltaX;
		xRot += deltaY;
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		//System.err.println(e.getUnitsToScroll());
		zoom -= e.getUnitsToScroll()*0.2f;
	}

	public GLCanvas getCanvas(){
		return canvas;
	}

    public void init(GLAutoDrawable drawable) {
        // Use debug pipeline
        // drawable.setGL(new DebugGL(drawable.getGL()));
		float mat_diffuse[] = { 0.7f, 0.7f, 0.7f, 1.0f };
		float mat_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		float mat_shininess[] = { 100.0f };


        GL gl = drawable.getGL();
		GLU glu = new GLU();

        System.err.println("INIT GL IS: " + gl.getClass().getName());

        // Enable VSync
        gl.setSwapInterval(1);

        // Setup the drawing area and shading mode
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel(GL.GL_SMOOTH); // try setting this to GL_FLAT and see what happens.

		//gl.glMaterialfv(gl.GL_FRONT,gl.GL_DIFFUSE, mat_diffuse,0);
		//gl.glMaterialfv(gl.GL_FRONT, gl.GL_SPECULAR, mat_specular, 0);
		//gl.glMaterialfv(gl.GL_FRONT, gl.GL_SHININESS, mat_shininess,0);

		gl.glColorMaterial(gl.GL_FRONT_AND_BACK, gl.GL_AMBIENT_AND_DIFFUSE);
		gl.glEnable(gl.GL_COLOR_MATERIAL);

		gl.glEnable(gl.GL_LIGHTING);
		gl.glEnable(gl.GL_LIGHT0);
		gl.glDepthFunc(gl.GL_LEQUAL);
		gl.glEnable(gl.GL_DEPTH_TEST);
		gl.glEnable(gl.GL_AUTO_NORMAL);
		gl.glEnable(gl.GL_NORMALIZE);

		theNurb = glu.gluNewNurbsRenderer();
		//glu.gluNurbsProperty(theNurb, glu.GLU_SAMPLING_TOLERANCE, 25.0f);
		//glu.gluNurbsProperty(theNurb, glu.GLU_DIs, zRot);
		//float[] control = {-5.0f, 0.0f, 0.0f, -2.5f, 5.0f, 0.0f,2.5f, -5.0f, 0.0f, 5.0f, 0.0f, 0.0f};
		//gl.glMap1f(gl.GL_MAP1_VERTEX_3, 0.0f, 1.0f, 3, 4, control,0);
		//gl.glEnable(gl.GL_MAP1_VERTEX_3);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		System.err.println("Reshape");
        GL gl = drawable.getGL();
        GLU glu = new GLU();

        if (height <= 0) { // avoid a divide by zero error!

            height = 1;
        }
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, h, 1.0, 2000.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

	public float[] genUniformKnots(int order, int numCtrlPts){
		float[] k = new float[order+numCtrlPts];
		float interval = 1.0f/(numCtrlPts-order+1);
		for(int i = 0; i<order; i++){
			k[i]=0.0f;
			k[order+numCtrlPts-i-1] = 1.0f;
		}
		for(int i = order; i<numCtrlPts;i++)
			k[i]=k[i-1]+interval;
		return k;
	}

	public float[] genControlPointsGrid(int rows, int cols){
		float[] ctrlPts = new float[rows*cols*3];
		for(int u=0;u<cols;u++){
			for(int v=0;v<rows;v++){
				float x = 2f*(u-cols/2f);
				float y = 2f*(v-rows/2f);
				ctrlPts[u*rows*3+v*3+0] = x;
				ctrlPts[u*rows*3+v*3+1] = y;
			}
		}
		return ctrlPts;
	}

	public void drawGridNURBS(GL gl, GLU glu, float[] ctrlPts, float[] rowKnots, float[] colKnots){
		glu.gluBeginSurface(theNurb);
				glu.gluNurbsSurface(theNurb,
						colKnots.length, colKnots,
						rowKnots.length, rowKnots,
						(rowKnots.length-4)*3,
						3,
						controlPoints,
						4, 4,
						gl.GL_MAP2_VERTEX_3);
		glu.gluEndSurface(theNurb);
	}

	public void setColorForVertex(GL gl, int sand){
		switch(sand){
			case 0: gl.glColor3f(0.2f,0.2f,0.2f); break;
			case 1: gl.glColor3f(0.0f, 0.0f, 1.0f); break;
			case 2: gl.glColor3f(0.0f, 1.0f, 1.0f); break;
			case 3: gl.glColor3f(0.0f, 1.0f, 0.0f); break;
			case 4: gl.glColor3f(1.0f, 0.0f, 0.0f); break;
			case 5: gl.glColor3f(1.0f, 1.0f, 0.0f); break;
			default: gl.glColor3f(1.0f, 1.0f, 1.0f);
		}
	}

	public void setInteropColorForVertex(GL gl, int lastSand, int sand, float transition){
		float[] lastC = getColorForVertex(lastSand);
		float[] curC = getColorForVertex(sand);
		float[] transC = new float[3];
		transC[0] = (1.0f - transition)*lastC[0] + transition* curC[0];
		transC[1] = (1.0f - transition)*lastC[1] + transition* curC[1];
		transC[2] = (1.0f - transition)*lastC[2] + transition* curC[2];
		gl.glColor3fv(transC, 0);
	}

	public float[] getColorForVertex(int sand){
		float[] c = new float[3];
		switch(sand){
			case 0: c[0]=0.2f;c[1]=0.2f;c[2]=0.2f; break;
			case 1: c[0]=0.0f;c[1]=0.0f;c[2]=1.0f; break;
			case 2: c[0]=0.0f;c[1]=1.0f;c[2]=1.0f; break;
			case 3: c[0]=0.0f;c[1]=1.0f;c[2]=0.0f; break;
			case 4: c[0]=1.0f;c[1]=0.0f;c[2]=0.0f; break;
			case 5: c[0]=1.0f;c[1]=1.0f;c[2]=0.0f; break;
			default: c[0]=1.0f;c[1]=1.0f;c[2]=1.0f;
		}
		return c;
	}

    public void display(GLAutoDrawable drawable) {
		//System.err.println("Display");

		//float[] knots = {0.0f, 0.0f, 0.0f, 0.0f, 0.143f, 0.286f, 0.429f, 0.572f, 0.714f, 0.858f, 1.0f, 1.0f, 1.0f, 1.0f};
		//for(float f : genUniformKnots(4,10))
		//	System.err.println(f);
		//sineNURBS(controlPoints, 50, 50, phase, 20.0f);
		if(showFPS){
			float fps = 1000.0f/(System.currentTimeMillis()-lastRepaint);
			System.err.println(fps);
		}
		float transition;
		if(delay<=0)
			transition = 1.0f;
		else
			transition = (float)(System.currentTimeMillis()-lastTime)/((float)delay);
		if(transition>1.0f) transition = 1.0f;
		if(transition>=1.0f && !paused){
			lastSandpileGrid = sandpileGrid;
			sandpileGrid = this.updateSand(sandpileGrid);
			lastTime = System.currentTimeMillis();
			transition = 0.0f;
		}

		if(System.currentTimeMillis() - lastRepaint < -delay)
			return;

		lastRepaint=System.currentTimeMillis();


		if(interpolate)
			interpSandNurbs(controlPoints, scale, lastSandpileGrid, sandpileGrid, transition);
		else
			sandNurbs(controlPoints, scale, sandpileGrid);
		//System.err.println(transition);
        GL gl = drawable.getGL();
		GLU glu = new GLU();
		GLUT glut = new GLUT();
		TextRenderer text = new TextRenderer(new java.awt.Font("Courier", java.awt.Font.PLAIN, 16));

        // Clear the drawing area
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        // Reset the current matrix to the "identity"
        gl.glLoadIdentity();

		
		gl.glPushMatrix();
			// Move the "drawing cursor" around
			gl.glTranslatef(0.0f, 0.0f, zoom);
			gl.glRotatef(xRot, 1.0f, 0.0f, 0.0f);
			gl.glRotatef(yRot, 0.0f, 1.0f, 0.0f);
			gl.glRotatef(zRot, 0.0f, 0.0f, 1.0f);
			//gl.glMapGrid1f(100, 0.0f, 1.0f);
			//gl.glEvalMesh1(gl.GL_LINE, 0, 100);
			//GLUnurbs nurbs = glu.gluNewNurbsRenderer();
			//float[] white = {1.0f, 1.0f, 1.0f};
			//gl.glMaterialfv(gl.GL_FRONT, gl.GL_AMBIENT_AND_DIFFUSE, white, 0);
			gl.glColor3f(0.5f, 0.5f, 0.5f);
			if(showNURBS)
				drawGridNURBS(gl, glu, controlPoints, rowKnots, colKnots);
			//gl.glTranslatef(98.0f, 0.0f, 0.0f);
			//drawGridNURBS(gl, glu, controlPoints, knots, knots);
			//gl.glColor3f(1.0f, 0.0f, 0.0f);
			//float[] red = {1.0f, 0.0f, 0.0f};
			//gl.glMaterialfv(gl.GL_FRONT, gl.GL_AMBIENT_AND_DIFFUSE, red, 0);
			if(displayCtrlPts){
				int rows = sandpileGrid.length;
				int cols = sandpileGrid[0].length;

				for(int c = 0; c<cols; c++){
					for(int r = 0; r<rows; r++){
						int i = r*cols*3 + c*3;
						gl.glPushMatrix();
							gl.glTranslatef(controlPoints[i],controlPoints[i+1],controlPoints[i+2]);
							if(interpolate)
								setInteropColorForVertex(gl,lastSandpileGrid[r][c],sandpileGrid[r][c],transition);
							else
								setColorForVertex(gl,sandpileGrid[r][c]);
							//gl.glMaterialfv(gl.GL_FRONT, gl.GL_EMISSION, getColorForVertex(sandpileGrid[r][c]),0);
							glut.glutSolidSphere(1.0f, 3, 3);
						gl.glPopMatrix();
					}
				}
			}/*
			gl.glColor3f(1.0f, 0.0f, 0.0f);
			gl.glBegin(gl.GL_POINTS);
				for(int i=0; i<controlPoints.length;i+=3){
					gl.glVertex3f(controlPoints[i],controlPoints[i+1],controlPoints[i+2]);
				}
			gl.glEnd();*/
				
			
		gl.glPopMatrix();

        // Flush all drawing operations to the graphics card
        gl.glFlush();
		//phase+=0.1f;
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
}
