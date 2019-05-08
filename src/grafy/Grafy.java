package grafy;
import java.awt.EventQueue;

import javax.swing.JFrame;

 
/*
 * G³ówna klasa dla projektu
 * Uruchamia program oraz tworzy i inicjuje okienko JFrame
 */
public class Grafy {

	private JFrame frame;
	private OptionPanel op;
	private Graph gr;
	private GraphPanel gp;
	
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Grafy window = new Grafy();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Grafy() {
		//konsturktor inicjuje okienko i elementy okienka 
		init();
	}

	/**
	 * Inicjalizuje elementy okienka
	 */
	private void init() {
		frame = new JFrame("Kolorowanie Grafu");
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//górny panel z opcjami progrtamu
		op = new OptionPanel(this, frame);
		frame.getContentPane().add(op);
		

		//obszar rysowania grafu
		gp = new GraphPanel();
		gp.setBounds(10,150,770,410);
		
		frame.getContentPane().add(gp);
	}

	/*
	 * Metoda generuje nowy graf przywo³ywana jest z poziomu obiektu op klasy OptionPanel
	 * po klikniêciu na przycisk generuj nowy graf.
	 */
	public void randomizeGraph(int v, int r, int r2) {

        gr = new Graph(v, r, r2);
		gp.setGraph(gr);
		gp.repaint();
		return;   
	}
	/*
	 * Metoda odœwie¿a pokolorowany graf przywo³ywana jest z poziomu obiektu op klasy OptionPanel
	 * po klikniêciu na przycisk KOLORUJ.
	 */
	public void koloruj() {
		gr.greedyPaintGraph();		//koloruj graf
		gp.repaint();
	}

	public boolean openFile(String sFile, int r, int r2) {
        gr = new Graph(sFile, frame, r, r2);
		gp.setGraph(gr);
		gp.repaint();
		
		return true;   
		
	}

	public void saveFile(String sFile) {
		if(gr == null)
			return;
		
		gr.saveGraph(sFile);
		
	}
	
	
}

