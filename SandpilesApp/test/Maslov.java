
import java.awt.*;
//import java.awt.event.*

public class Sandpile extends java.applet.Applet
		implements Runnable {
	//, ActionListener{

	int z[][];
	int z_c = 3;
	// z[][] -- the array storing sand column heights.
	// z_c -- the critcal value above which the column becomes unstable.
	int n_act[], current, next;
	int x_act[][], y_act[][];
	//  n_act[0 or 1] is the total number of current and new active sites
	//  x_act[][],y_act[][] are their coordinates (second index is 0 or 1)
	//  in the end each time step current and next switch places.
	int Lx, Ly;
	boolean x_periodic = false;
	boolean y_periodic = false;
	final String PARAM_Lx = "Lx";
	final String PARAM_Ly = "Ly";
	// system sizes Lx and Ly are described as
	// parameters Lx and Ly in the applet web page.
	//
	// Example:
	// <applet
	// code=Sandpile.class
	// width=500
	// height=500>
	// <param name=Lx value=50>
	// <param name=Ly value=50>
	// </applet>
	//
	// x_periodic, y_periodic define the open/periodic
	// boundary conditions
	int current_height;
	int time = 0;
	int dx, dy, x0, y0, x1, y1, xa0, ya0, xa1, ya1;
	//     dx,dy are bins in the graphics
	//     x0,x1,y0,y1 are coordinates of the image rectangle
	//     xa0, ya0, xa1, ya1 are coordinates of the clickable (active) area
	Panel p_north, p_south;
	TextField Lx_tf, Ly_tf;
	Checkbox cb0, cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb_add;
	Button x_periodicb, y_periodicb;
	final String periodic_s = " BC: Periodic ",  open_s = "   BC:Open   ";
	Button refreshb, fillb;
	Button startb, stepb, traceb, set_delayb;
	TextField delay_tf;
	// These are the Objects of GUI
	Thread sandpile_thread = null;
	int delay = 100;
	// This is the thread animating the main program. It is necessary so that
	//  applet does not freeze when thread is put on hold by delay=10 microseconds.
	boolean change_flag = false;
	boolean x_boundary_change_flag = false, y_boundary_change_flag = false;
	boolean fill_flag = false;
	boolean mouse_flag = false;
	int x_mouse, y_mouse;
	boolean add_grain_flag = true;
	boolean paused_flag = true;
	boolean step_by_step = true;
	boolean finished_painting = true;
	boolean step_finished = true;
	boolean trace_flag = false;
	boolean trace_change_flag = false;
	// Various flags
	Color trace_color = Color.cyan;
	// Color used to trace sites updated at least once since trace button was pressed
	Color background_color = Color.white;
	Graphics gr;
	Image img;
	//      separate Graphics and Image objects are used since if one wants
	//      reduce flicker by hiding incremental drawing.

	public void init() {
		String param;
		int tmp;
		param = getParameter(PARAM_Lx);
		if (param == null) {
			Lx = 20;
		} else {
			tmp = Integer.parseInt(param);
			if (tmp < 1) {
				tmp = 1;
			}
//			if (tmp>100) tmp=100;
			Lx = tmp;
		}

		param = getParameter(PARAM_Ly);
		if (param == null) {
			Ly = 20;
		} else {
			tmp = Integer.parseInt(param);
			if (tmp < 1) {
				tmp = 1;
			}
			Ly = tmp;
		}

		param = getParameter("x_periodic");
		if (Integer.parseInt(param) == 1) {
			x_periodic = true;
		} else {
			x_periodic = false;
		}

		param = getParameter("y_periodic");
		if (Integer.parseInt(param) == 1) {
			y_periodic = true;
		} else {
			y_periodic = false;
		}


		setBackground(background_color);

		setLayout(new BorderLayout());
		// overall layout of the applet

		p_north = new Panel();
		p_north.setLayout(new GridLayout(3, 1, 0, 0));
		// north panel added to "North" itself consists of two panels:
		// p_north1, p_north2

		Panel p_north1 = new Panel();

		p_north1.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));

		p_north1.add(new Label("Lx = ", Label.RIGHT));

		Lx_tf = new TextField(3);
		Lx_tf.setText(String.valueOf(Lx));
		p_north1.add(Lx_tf);

		if (x_periodic) {
			x_periodicb = new Button(periodic_s);
		} else {
			x_periodicb = new Button(open_s);
		}

		p_north1.add(x_periodicb);

		p_north1.add(new Label("Ly = ", Label.RIGHT));

		Ly_tf = new TextField(3);
		Ly_tf.setText(String.valueOf(Ly));
		p_north1.add(Ly_tf);

		if (y_periodic) {
			y_periodicb = new Button(periodic_s);
		} else {
			y_periodicb = new Button(open_s);
		}

		p_north1.add(y_periodicb);

		p_north.add(p_north1);


		Panel p_north2 = new Panel();
		p_north2.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));

		refreshb = new Button("Set new Lx,Ly");
		p_north2.add(refreshb);

		p_north2.add(new Label("Delay = ", Label.RIGHT));

		delay_tf = new TextField(4);
		delay_tf.setText(String.valueOf(delay));
		p_north2.add(delay_tf);

		p_north2.add(new Label(" msec", Label.LEFT));

		set_delayb = new Button("Set new delay");
		p_north2.add(set_delayb);

		p_north.add(p_north2);


		Panel p_north3 = new Panel();
//        p_north3.setLayout (new GridLayout (1,3,3,0));
		p_north3.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));

		startb = new Button("  Continue  ");
//		startb.setLabel("Start");
		p_north3.add(startb);
		//startb.setLabel("Start");

		stepb = new Button("  Next Step  ");
		p_north3.add(stepb);

		traceb = new Button(" Set trace on  ");
		p_north3.add(traceb);

		p_north.add(p_north3);

		add("North", p_north);

		p_south = new Panel();
		p_south.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));
		// South panel is simpler

		CheckboxGroup height_cbg;
		height_cbg = new CheckboxGroup();

		cb0 = new Checkbox("0", height_cbg, false);
		cb0.setBackground(height_color(0));
		p_south.add(cb0);


		cb1 = new Checkbox("1", height_cbg, false);
		cb1.setBackground(height_color(1));
		p_south.add(cb1);

		cb2 = new Checkbox("2", height_cbg, false);
		cb2.setBackground(height_color(2));
		p_south.add(cb2);

		cb3 = new Checkbox("3", height_cbg, false);
		cb3.setBackground(height_color(3));
		p_south.add(cb3);

		cb4 = new Checkbox("4", height_cbg, false);
		cb4.setBackground(height_color(4));
		p_south.add(cb4);

		cb5 = new Checkbox("5", height_cbg, false);
		cb5.setBackground(height_color(5));
		p_south.add(cb5);

		cb6 = new Checkbox("6", height_cbg, false);
		cb6.setBackground(height_color(6));
		p_south.add(cb6);

		cb7 = new Checkbox("7", height_cbg, false);
		cb7.setBackground(height_color(7));
		p_south.add(cb7);

		cb_add = new Checkbox("+1", height_cbg, true);
		p_south.add(cb_add);
		add_grain_flag = true;

		fillb = new Button("Fill");
		p_south.add(fillb);

		add("South", p_south);


		img = createImage(size().width, size().height);
		gr = img.getGraphics();

	}

	public boolean action(Event e, Object arg) {
		if (e.target == startb) {
			paused_flag = false;
			step_by_step = false;
			stepb.setLabel(" Pause ");
			return true;
		}

		if (e.target == stepb) {
			paused_flag = false;
			step_by_step = true;
			stepb.setLabel(" Next Step ");
			return true;
		}

		if (e.target == x_periodicb) {
			change_flag = true;
			x_boundary_change_flag = true;
			return true;
		}

		if (e.target == y_periodicb) {
			change_flag = true;
			y_boundary_change_flag = true;
			return true;
		}

		if (e.target == fillb) {
			change_flag = true;
			fill_flag = true;
			return true;

		}

		if (e.target == traceb) {
			change_flag = true;
			trace_change_flag = true;
			return true;

		}

		if (e.target == refreshb) {
			if (sandpile_thread != null) {
				sandpile_thread.stop();
				sandpile_thread = null;
			}

			int tmp = Integer.valueOf(Lx_tf.getText()).intValue();
			if (tmp > 0) {
				if (tmp > 100) {
					tmp = 100;
					Lx_tf.setText(String.valueOf(tmp));
				}
				Lx = tmp;
			}

			tmp = Integer.valueOf(Ly_tf.getText()).intValue();
			if (tmp > 0) {
				if (tmp > 100) {
					tmp = 100;
					Ly_tf.setText(String.valueOf(tmp));
				}
				Ly = tmp;
			}
			refresh();
			repaint();
			sandpile_thread = new Thread(this);
			sandpile_thread.start();
			return true;
		}

		if (e.target == set_delayb) {
			int tmp = Integer.valueOf(delay_tf.getText()).intValue();
			if (tmp < 0) {
				tmp = 0;
			}
			delay_tf.setText(String.valueOf(tmp));
			delay = tmp;
			return true;
		}


		if (e.target instanceof Checkbox) {
			Checkbox current_cb = (Checkbox) e.target;
			if (current_cb.getLabel() == "+1") {
				add_grain_flag = true;
			} else {
				add_grain_flag = false;
				String current_string = current_cb.getLabel();
				int tmp = Integer.valueOf(current_string).intValue();
				if (tmp < 0) {
					tmp = 0;
				}
				if (tmp > 7) {
					tmp = 7;
				}
				current_height = tmp;
			}

			return true;
		}

		return false;
	}

	public void refresh() {
		int x, y;

		gr.clearRect(0, 0, size().width, size().height);
		//      erases everything
		int x_size = size().width;
		int y_size = size().height;
		y_size += -p_north.bounds().height - p_south.bounds().height;

		dx = (int) (x_size / (Lx + 2));
		dy = (int) (y_size / (Ly + 2));
//        dx=(int)((size().width)/(Lx+2));
//        dy=(int)((0.7*size().height)/(Ly+2));
		//      size().width is a method of Applet class returning the
		//      width of the applet window

		if (dx < dy) {
			dy = dx;
		}
		if (dy < dx) {
			dx = dy;
		}
		// these commands make the (dx,dy) "pixel" to be square
		x0 = (int) (0.5 * (x_size - (Lx + 2) * dx + 1));
		y0 = (int) (p_north.bounds().height + 0.5 * (y_size - (Ly + 2) * dy + 1));
//        x0=(int)(0.5*(size().width - (Lx+2)*dx + 1));
//        y0=(int)(0.5*(size().height - (Ly+2)*dy + 1));
		x1 = x0 + (Lx + 2) * dx;
		y1 = y0 + (Ly + 2) * dy;
		//      these are the coordinates of 4 corners of the image

		xa0 = x0 + dx; //(int)(0.5*(size().width - Lx*dx + 1));
		ya0 = y0 + dy;//(int)(p_north.bounds().height+0.5*(y_size - Ly*dy + 1));
		xa1 = xa0 + Lx * dx;
		ya1 = ya0 + Ly * dy;
		//      these are the coordinates of the clickable (active) area
		//      of the image
		z = null;
		z = new int[Lx + 2][Ly + 2];

		for (x = 1; x < Lx + 1; ++x) {
			for (y = 1; y < Ly + 1; ++y) {
				z[x][y] = z_c;
				draw_cell(x, y);
			}
		}
		draw_boundaries();

		n_act = new int[2];
		x_act = new int[Lx * Ly][2];
		y_act = new int[Lx * Ly][2];
		current = 0;

		x = (int) (Lx + 1) / 2;
		y = (int) (Ly + 1) / 2;
		add_grain(x, y);

	}

	public void change_boundary_conditions() {
		if (x_boundary_change_flag) {
			boolean tmp = x_periodic;
			x_periodic = !tmp;
			draw_boundaries();

			if (x_periodic) {
				x_periodicb.setLabel(periodic_s);
			} else {
				x_periodicb.setLabel(open_s);
			}


			repaint();
			x_boundary_change_flag = false;
		}

		if (y_boundary_change_flag) {
			boolean tmp = y_periodic;
			y_periodic = !tmp;
			draw_boundaries();

			if (y_periodic) {
				y_periodicb.setLabel(periodic_s);
			} else {
				y_periodicb.setLabel(open_s);
			}

			repaint();
			y_boundary_change_flag = false;
		}
		change_flag = false;
	}

	public void fill() {

		for (int x = 1; x < Lx + 1; ++x) {
			for (int y = 1; y < Ly + 1; ++y) {
				set_height(x, y);
//				z[x][y] = z_c;
//				draw_cell(x,y);
			}
		}
		repaint();
		fill_flag = false;
		change_flag = false;
	}

	public void draw_boundaries() {
		int x, y;
		if (!x_periodic) {
			for (y = 1; y < Ly + 1; ++y) {
				z[0][y] = 0;
				draw_boundary_cell(0, y);
				z[Lx + 1][y] = 0;
				draw_boundary_cell(Lx + 1, y);
			}

			if (!y_periodic) {

				z[0][0] = 0;
				draw_boundary_cell(0, 0);
				z[Lx + 1][Ly + 1] = 0;
				draw_boundary_cell(Lx + 1, Ly + 1);
				z[0][Ly + 1] = 0;
				draw_boundary_cell(0, Ly + 1);
				z[Lx + 1][0] = 0;
				draw_boundary_cell(Lx + 1, 0);
			}
		} else {
			gr.setColor(getBackground());
			for (y = 0; y < Ly + 2; ++y) {
				gr.fillRect(x0, y0 + y * dy, dx, dy);
				gr.drawRect(x0, y0 + y * dy, dx, dy);

				gr.fillRect(x0 + (Lx + 1) * dx, y0 + y * dy, dx, dy);
				gr.drawRect(x0 + (Lx + 1) * dx, y0 + y * dy, dx, dy);
			}
			gr.setColor(Color.black);
			gr.drawLine(x0 + dx, y0 + dy, x0 + dx, y0 + (Ly + 1) * dy);
			gr.drawLine(x0 + (Lx + 1) * dx, y0 + dy, x0 + (Lx + 1) * dx, y0 + (Ly + 1) * dy);
		}

		if (!y_periodic) {
			for (x = 1; x < Lx + 1; ++x) {
				z[x][0] = 0;
				draw_boundary_cell(x, 0);
				z[x][Ly + 1] = 0;
				draw_boundary_cell(x, Ly + 1);
			}
			if (!x_periodic) {

				z[0][0] = 0;
				draw_boundary_cell(0, 0);
				z[Lx + 1][Ly + 1] = 0;
				draw_boundary_cell(Lx + 1, Ly + 1);
				z[0][Ly + 1] = 0;
				draw_boundary_cell(0, Ly + 1);
				z[Lx + 1][0] = 0;
				draw_boundary_cell(Lx + 1, 0);
			}
		} else {
			gr.setColor(getBackground());
			for (x = 0; x < Lx + 2; ++x) {
				gr.fillRect(x0 + x * dx, y0, dx, dy);
				gr.drawRect(x0 + x * dx, y0, dx, dy);

				gr.fillRect(x0 + x * dx, y0 + (Ly + 1) * dy, dx, dy);
				gr.drawRect(x0 + x * dx, y0 + (Ly + 1) * dy, dx, dy);
			}

			gr.setColor(Color.black);
			gr.drawLine(x0 + dx, y0 + dy, x0 + (Lx + 1) * dx, y0 + dy);
			gr.drawLine(x0 + dx, y0 + (Ly + 1) * dy, x0 + (Lx + 1) * dx, y0 + (Ly + 1) * dy);
			if (!x_periodic) {
				gr.drawLine(x0, y0 + dy, x0 + (Lx + 2) * dx, y0 + dy);
				gr.drawLine(x0, y0 + (Ly + 1) * dy, x0 + (Lx + 2) * dx, y0 + (Ly + 1) * dy);
			}
		}
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void add_grain(int x, int y) {
		z[x][y] += 1;
		draw_cell(x, y);

		if (z[x][y] == z_c + 1) {
			x_act[n_act[current]][current] = x;
			y_act[n_act[current]][current] = y;
			n_act[current] += 1;
		}
	}

	public void set_height(int x, int y) {
		if (add_grain_flag) {
			add_grain(x, y);
			return;
		} else {

			int old_height = z[x][y];
			z[x][y] = current_height;
			draw_cell(x, y);
			if ((current_height > z_c) & (old_height > z_c)) {
				return;
			}


			if (current_height > z_c) {
				x_act[n_act[current]][current] = x;
				y_act[n_act[current]][current] = y;
				n_act[current] += 1;
				return;
			}
			if (old_height > z_c) {
				int k = n_act[current] - 1;
				while ((x_act[k][current] != x) || (y_act[k][current] != y)) {
					k--;
				}
				for (int m = k + 1; m < n_act[current]; m++) {
					x_act[m - 1][current] = x_act[m][current];
					y_act[m - 1][current] = y_act[m][current];
				}
				n_act[current] -= 1;
				return;
			}


		}
	}

	public void start() {
		if (sandpile_thread == null) {

			sandpile_thread = new Thread(this);
			sandpile_thread.start();
		}
	}

	public void stop() {
		if (sandpile_thread != null) {
			sandpile_thread.stop();
			sandpile_thread = null;
		}

	}

	public void step() {
		boolean ind[][];
		int x, y, xp, xm, yp, ym;
		ind = new boolean[Lx + 2][Ly + 2];


		step_finished = false;

		time += 1;
		next = (current + 1) % 2;
		n_act[next] = 0;
		for (int k = 0; k < n_act[current]; ++k) {
			x = x_act[k][current];
			y = y_act[k][current];

			z[x][y] -= 4;
			z[x - 1][y] += 1;
			z[x + 1][y] += 1;
			z[x][y + 1] += 1;
			z[x][y - 1] += 1;
		}

		if (y_periodic) {
			for (x = 1; x < Lx + 1; x++) {

				z[x][1] += z[x][Ly + 1];
				z[x][Ly] += z[x][0];

			}
		}

		if (x_periodic) {
			for (y = 1; y < Ly + 1; y++) {

				z[1][y] += z[Lx + 1][y];
				z[Lx][y] += z[0][y];
			}
		}

		for (x = 1; x < Lx + 1; x++) {
			z[x][0] = 0;
			z[x][Ly + 1] = 0;
		}

		for (y = 1; y < Ly + 1; y++) {
			z[0][y] = 0;
			z[Lx + 1][y] = 0;
		}

		for (int k = 0; k < n_act[current]; ++k) {
			x = x_act[k][current];
			y = y_act[k][current];
			xp = (x % Lx) + 1;
			xm = ((Lx + x - 2) % Lx) + 1;
			yp = (y % Ly) + 1;
			ym = ((Ly + y - 2) % Ly) + 1;


			if (!ind[x][y]) {
				draw_cell(x, y);
				ind[x][y] = true;
				if (z[x][y] > z_c) {
					x_act[n_act[next]][next] = x;
					y_act[n_act[next]][next] = y;
					n_act[next] += 1;
				}
			}

			if (!ind[xp][y] && (x_periodic || xp != 1)) {
				draw_cell(xp, y);
				ind[xp][y] = true;
				if (z[xp][y] > z_c) {
					x_act[n_act[next]][next] = xp;
					y_act[n_act[next]][next] = y;
					n_act[next] += 1;
				}
			}

			if (!ind[xm][y] && (x_periodic || x != 1)) {
				draw_cell(xm, y);
				ind[xm][y] = true;
				if (z[xm][y] > z_c) {
					x_act[n_act[next]][next] = xm;
					y_act[n_act[next]][next] = y;
					n_act[next] += 1;
				}
			}


			if (!ind[x][yp] && (y_periodic || yp != 1)) {
				draw_cell(x, yp);
				ind[x][yp] = true;
				if (z[x][yp] > z_c) {
					x_act[n_act[next]][next] = x;
					y_act[n_act[next]][next] = yp;
					n_act[next] += 1;
				}
			}

			if (!ind[x][ym] && (y_periodic || y != 1)) {
				draw_cell(x, ym);
				ind[x][ym] = true;
				if (z[x][ym] > z_c) {
					x_act[n_act[next]][next] = x;
					y_act[n_act[next]][next] = ym;
					n_act[next] += 1;
				}
			}

		}

		current = (current + 1) % 2;
		step_finished = true;


	}

	public void draw_cell(int i, int j) {
		gr.setColor(height_color(z[i][j]));
		gr.fillRect(x0 + i * dx, y0 + j * dy, dx, dy);
		gr.setColor(Color.black);
		gr.drawRect(x0 + i * dx, y0 + j * dy, dx, dy);
	}

	public void draw_boundary_cell(int i, int j) {
		gr.setColor(Color.gray);
		gr.fillRect(x0 + i * dx, y0 + j * dy, dx, dy);
		gr.setColor(Color.black);
		gr.drawRect(x0 + i * dx, y0 + j * dy, dx, dy);
	}

	public Color height_color(int zz) {
		Color col;
		if (trace_flag) {
			col = trace_color;
			return col;
		}

		switch (zz) {
			case 0:
				col = Color.gray;
				return col;
			case 1:
				col = Color.red;
				return col;
			case 2:
				col = Color.blue;
				return col;
			case 3:
				col = Color.green;
				return col;
			case 4:
				col = Color.orange;
				return col;
			case 5:
				col = Color.yellow;
				return col;
			case 6:
				col = new Color(250, 150, 0).darker();
				return col;
			case 7:
				col = Color.orange.darker();
				return col;
			default:
				col = Color.white;
				return col;
		}
	}

	public void change_trace() {
		trace_flag = !trace_flag;
		if (!trace_flag) {
			for (int x = 1; x < Lx + 1; ++x) {
				for (int y = 1; y < Ly + 1; ++y) {
					draw_cell(x, y);
				}
			}
		}

		if (trace_flag) {
			traceb.setLabel(" Set trace off ");
		} else {
			traceb.setLabel(" Set trace on  ");
		}
		repaint();

		trace_change_flag = false;
		change_flag = false;
	}

	public void run() {
		refresh();
		repaint();
		while (true) {
			if (!paused_flag & finished_painting & !change_flag) {
				step();
				finished_painting = false;
				repaint();
				if (step_by_step) {
					paused_flag = true;
				}
			} else {
				if (change_flag) {
					if (x_boundary_change_flag || y_boundary_change_flag) {
						change_boundary_conditions();
					}

					if (fill_flag) {
						fill();
					}

					if (mouse_flag) {
						set_height(x_mouse, y_mouse);
						repaint();
						mouse_flag = false;
						change_flag = false;
					}

					if (trace_change_flag) {
						change_trace();
					}
				}
			}

			try {
				Thread.sleep(delay);
			} catch (InterruptedException ie) {
			}

		}
	}

	public void paint(Graphics g) {
		finished_painting = false;
		g.drawImage(img, 0, 0, this);
		finished_painting = true;
	}

	public boolean mouseDown(Event e, int xmd, int ymd) {
		if ((xa0 <= xmd) && (xmd <= xa1) && (ya0 <= ymd) && (ymd <= ya1)) {
			change_flag = true;
			mouse_flag = true;
			x_mouse = (xmd - x0) / dx;
			y_mouse = (ymd - y0) / dy;
			if (x_mouse < 1) {
				x_mouse = 1;
			} else if (x_mouse > Lx) {
				x_mouse = Lx;
			}
			if (y_mouse < 1) {
				y_mouse = 1;
			} else if (y_mouse > Ly) {
				y_mouse = Ly;
			}
		}
		return true;
	}

	public boolean mouseDrag(Event e, int xmd, int ymd) {
		if ((xa0 <= xmd) && (xmd <= xa1) && (ya0 <= ymd) && (ymd <= ya1)) {
			change_flag = true;
			mouse_flag = true;
			x_mouse = (int) (xmd - x0) / dx;
			y_mouse = (int) (ymd - y0) / dy;
			if (x_mouse < 1) {
				x_mouse = 1;
			} else if (x_mouse > Lx) {
				x_mouse = Lx;
			}
			if (y_mouse < 1) {
				y_mouse = 1;
			} else if (y_mouse > Ly) {
				y_mouse = Ly;
			}
		}
		return true;
	}
//     public boolean mouseUp(Event e,int xmd,int ymd)
//	 {
//	   repaint();
	//       mouse_flag=false;
	//	   change_flag=false;
	//     return true;
	//	 }
}
//THE END /////////////////////////////////////////////////