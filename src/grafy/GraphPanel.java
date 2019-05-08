/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grafy;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;


/*
 * klasa Graph buduje strukturê grafu
 * implementuje wszystkie algorytmy grafowe
 * listê s¹siedztwa, algorytm kolorowania, losowe generowanie grafu, zapis do pliku XML 
 */
public class GraphPanel extends javax.swing.JPanel {
    private Graph graf = null;
	private double skala = 1.0;
	private int centerR;

	/**
     * Konstruktor GrafPanel
     */
    public GraphPanel() {
        init();
    }
    public void setGraph(Graph g) {
        this.graf = g;
        
    }

    private void init() {

    	setBorder(BorderFactory.createLineBorder(Color.black));
        setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 704, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 522, Short.MAX_VALUE)
        );
    }

    
    	/*
    	 * Rysuje graf w panelu grafu
    	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
    	 */
	   @Override
	    public void paint(Graphics g) {
	       super.paint(g);    
	    
	       if(graf == null)
		      	return;
		           
	      int maxR = graf.maxR;//(getHeight() - 10)/2;
	      
	      //skaluj obrazek tak aby zawsze by³ widoczny na ekranie
	      skala  = (double)(getHeight() - 10)/2.0/(double)maxR;

	      centerR = (getHeight() - 10)/2;

	      //rysuj ko³o ograniczaj¹ce punkty 
	      g.drawOval((int)(((getWidth())/2 - centerR)), 5, (int)(centerR*2), (int)(maxR*2*skala));
	      

				if(graf != null) {
				   g.setColor(Color.green);
				   for (int i=0;i<graf.V;i++) {
					   for(int w : graf.adj[i]) {

						   //kolor krawêdzi
						   g.setColor(Color.green);
					        
					        //rysuje linie krawêdzi pomiêdzy wierzcho³kami
				            g.drawLine( (getWidth())/2 + (int)(( (int)graf.points.get(i).getX()) * skala), 
				            		5 + centerR +(int)((  (int)graf.points.get(i).getY()) * skala),
				            		(getWidth())/2 + (int)((  (int)graf.points.get(w).getX()) * skala), 
				            		5 + centerR +(int)(( (int)graf.points.get(w).getY()) * skala));
						}
				   }
              
                   g.setFont(new Font("Monospaced", Font.PLAIN, 12));
                   for (int key : graf.points.keySet()) {
                       Point p = graf.points.get(key);
                       //rysuje wierzcho³ek
                       
                       //kolor wierzcho³ka
                       g.setColor(graf.colors.get(key));
                       //wype³nienie
                       g.fillOval((getWidth())/2 +(int)(( (int)p.getX() )* skala) -10, 
								5 + centerR +(int)(p.getY() * skala) - 10, 20, 20);
                       g.setColor(Color.black);
                       //krawêdzie na czarno
                       g.drawOval((getWidth())/2 +(int)(p.getX() * skala) - 10, 
                    		   5 + centerR +(int)(((int)p.getY())* skala) - 10, 20, 20);

                       //wpisz nazwê (id) wierzcho³ka
                	   g.drawString(graf.int2Str.get(key), 
                			   getWidth()/2 +(int)(p.getX()* skala)-5, 
                			   5 + centerR +(int)(p.getY()* skala)+5);
                   }
               }

            }
}
