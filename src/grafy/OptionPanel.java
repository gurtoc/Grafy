package grafy;

import java.awt.FileDialog;
import java.awt.Color;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


/*
 * Klasa rozbudowuje Panel opcji
 */
		
public class OptionPanel extends JPanel {

	private JButton btOpen, btSave, btPaint;
	private JButton btnRysujNowyGraf;
	private TextField txtV, txtR, txtr;
	private Grafy g = null;
	private JFrame frame = null;
	
	public OptionPanel(Grafy g, JFrame frame) {
		this.frame = frame;
		this.g = g;
		init();		//konstruktor inicjuje elementy panelu opcji
	}
	
	
	/**
	 * Inicjalizuje elementy okienka
	 */
	public void init() {
		
		setBounds(0, 0, 790, 130);
		setBorder(BorderFactory.createLineBorder(Color.black)); 
		setLayout(null);
		//pole tekstowe do wpisania iloœci wierzcho³ków
		txtV = new TextField();
		txtV.setBounds(163, 10, 45, 22);
		add(txtV);
		txtV.setText("10");
 	
		//pole do wpisania wartoœci 'r'
 		txtr = new TextField();
 		txtr.setText("80");
 		txtr.setBounds(163, 40, 45, 22);
 		add(txtr);
 		
		//pole do wpisania wartoœci 'R'
 		txtR = new TextField();
 		txtR.setText("200");
 		txtR.setBounds(163, 70, 45, 22);
 		add(txtR);

 		//etykiety dla pól
		Label label1 = new Label("Liczba wierzcho³ków");
		label1.setBounds(10, 10, 144, 22);
		label1.setAlignment(2);
		add(label1);

		Label label3 = new Label("r:");
		label3.setAlignment(2);
		label3.setBounds(10, 40, 144, 22);
		add(label3);

		Label label4 = new Label("R:");
		
		label4.setBounds(10, 70, 144, 22);
		add(label4);
		label4.setAlignment(2);
		
		//przyciski
		
		btPaint = new JButton("Koloruj graf");
		btPaint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(g == null)
					return;
				g.koloruj();	//koloruj graf po naciœniêciu
			}
		});
		
		btPaint.setBounds(620, 10, 150, 22);
		add(btPaint);
		
		btOpen = new JButton("Otwórz plik");
		btOpen.setEnabled(true);
		btOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					String sFile = openFile();
					if(sFile == null)
						return;
					int r = Integer.valueOf(txtr.getText());
					int R = Integer.valueOf(txtR.getText());

					if(g.openFile(sFile, r, R))		//wywo³aj funkcjê otwarcia pliku z poziomu obiektu klasy 'Grafy'
						btSave.setEnabled(true);
			      }
			
		});

		btOpen.setBounds(372, 20, 150, 22);
		add(btOpen);
		
		btSave = new JButton("Zapisz graf jako...");
		btSave.setEnabled(false);
		btSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					String sFile = saveFile();
					if(sFile == null)
						return;
					g.saveFile(sFile);		//zapisz graf do pliku XML
			      }
		});

		btSave.setBounds(372, 80, 150, 22);
		add(btSave);

		

		btnRysujNowyGraf = new JButton(" Generuj nowy graf ");
		btnRysujNowyGraf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//System.out.println("listener");
				int v= Integer.valueOf(txtV.getText());
				int r = Integer.valueOf(txtr.getText());
				int R = Integer.valueOf(txtR.getText());
				
				g.randomizeGraph(v, r, R);  		//tworzy losowy graf
				btSave.setEnabled(true);

			}
		});
		btnRysujNowyGraf.setBounds(620, 100, 150, 22);
		add(btnRysujNowyGraf);
	}
	
	
	/*
	 * Odpowiedna zdarzenie klikniêcia na przycisk Otwórz plik
	 */
    private String openFile() {
		FileDialog fd = new FileDialog(frame, "Otwórz plik gxl", FileDialog.LOAD);
		fd.setDirectory(System.getProperty("user.dir"));
        fd.setFilenameFilter(new FilenameFilter(){
	        @Override
	        public boolean accept(File dir, String name) {
	            return name.endsWith(".gxl");
	        }
	    });
	                
		fd.setVisible(true);
		String filePath = fd.getDirectory() + fd.getFile();
		return filePath;
	}
    
    
	/*
	 * Odpowiedna zdarzenie klikniêcia na przycisk Zapisz graf
	 */
    private String saveFile() {
		FileDialog fd = new FileDialog(frame, "Zapisz graf jako.", FileDialog.SAVE);
		fd.setDirectory(System.getProperty("user.dir"));
                fd.setFilenameFilter(new FilenameFilter(){
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".gxl");
                }
            });
		fd.setFile("graf_01.gxl");			//domyœlny plik zapisu
		fd.setVisible(true);
		String filePath = fd.getDirectory() + fd.getFile();
		return filePath;
	}
}
