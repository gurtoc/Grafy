package grafy;

import java.util.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.awt.*;
import java.io.*;


/*
 * Klasa obudowuje strukturê grafu
 * zawiera wiêc listê s¹siedztwa, wspó³rzêdne wierzcho³ków
 * algorytm kolorowania grafu
 */
public class Graph  {
		int V;						//wierzcho³ki grafu
		LinkedList<Integer>[] adj;	//lista przystawania
        HashMap<Integer, Point> points = new HashMap<>();
        HashMap<Integer, Color> colors = new HashMap<>();	//tablica mapuj¹ca indeksy wierzcho³ków i kolorów
        HashMap<Integer, Integer> vertices = new HashMap<>();	//tablica mapuj¹ca indeksy wierzcho³ków i kolorów
        HashMap<String, Integer> str2Int = new HashMap<>();		//nazwa na id wierzcho³ka
        HashMap<Integer, String> int2Str = new HashMap<>();		//id wierzcho³ka na nazwê
		public int maxRSQ;
		public int maxR;
		int rr;
		private Color []coltab = null;
		boolean bKolor = false;
		
		//konstrutor odczytu XML
		public Graph(String sFile, JFrame frame, int r, int r2) {

			maxR = r2;  
			maxRSQ = maxR * maxR;
			rr = r;
			
			Random rnd = new Random();

			DocumentBuilder dBuilder;
			try {
				//tworzy strukturê DOM
				File fXmlFile = new File(sFile);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				dBuilder = dbFactory.newDocumentBuilder();
				//parsuje dokument
				Document doc = dBuilder.parse(fXmlFile);
				
				//sprawdza czy XML zawiere\a element gxl
				if("gxl".compareTo(doc.getDocumentElement().getNodeName()) != 0) {
					JOptionPane.showMessageDialog(frame, "Element korzenia dokumentu XML musi nazywaæ siê <gxl>");
					return;
				}
				NodeList nList = doc.getElementsByTagName("node");
				if(nList == null) {
					JOptionPane.showMessageDialog(frame, "Nie znaleziono w dokumencie elementu <graph>");
					return;
					
				}
				int v = 0;
				//iteruje po elementach <node>
				for (int temp = 0; temp < nList.getLength(); temp++) {

					Node nNode = nList.item(temp);
							
					//System.out.println("\nElement :" + nNode.getNodeName());
							
					if (nNode.getNodeType() == Node.ELEMENT_NODE 
							&& "node".compareTo(nNode.getNodeName()) == 0) {

						Element eElement = (Element) nNode;
						//System.out.println("id : " + eElement.getAttribute("id"));
						
						String sNazwa = eElement.getAttribute("id");
						
						str2Int.put(sNazwa, v);
						int2Str.put(v,  sNazwa);
						try {
							//System.out.println("x == " + eElement.getElementsByTagName("intX").item(0).getTextContent());
							
							int x = Integer.parseInt(eElement.getElementsByTagName("intX").item(0).getTextContent());
							int y = Integer.parseInt(eElement.getElementsByTagName("intY").item(0).getTextContent());
							colors.put(v, Color.yellow);
							points.put(v, new Point(x,y));
						}
						catch(NumberFormatException ex) {
							System.out.println("Generujê losowo po³o¿enie" );
							  
							  int x = rnd.nextInt(maxR*2) - maxR;
							  int y = rnd.nextInt(maxR*2) - maxR;
							  while(maxRSQ < x*x + y*y ) {
						    	  x = rnd.nextInt(maxR*2) - maxR;
						    	  y = rnd.nextInt(maxR*2) - maxR;
				  	    	  }
				              colors.put(v, Color.yellow);
				  	    	  points.put(v, new Point(x,y));

						}
						v++;
					}
				}
				V = v;
				adj =(LinkedList<Integer>[]) new LinkedList[V];
				for (int i=0;i<V;i++)
					adj[i]= new LinkedList<Integer>();

				
				nList = doc.getElementsByTagName("edge");
				if(nList == null) {
					JOptionPane.showMessageDialog(frame, "Nie znaleziono w dokumencie elementu <graph>");
					return;
					
				}
				//iteruje po elementach <edge> i tworzy krawêdzie
				for (int temp = 0; temp < nList.getLength(); temp++) {

					Node nNode = nList.item(temp);
							
					//System.out.println("\nElement :" + nNode.getNodeName());
							
					if (nNode.getNodeType() == Node.ELEMENT_NODE 
							&& "edge".compareTo(nNode.getNodeName()) == 0) {

						Element eElement = (Element) nNode;

//						System.out.println("id : " + eElement.getAttribute("id"));
//						System.out.println("from : " + eElement.getAttribute("from"));
//						System.out.println("to : " + eElement.getAttribute("to"));
						String sFrom = eElement.getAttribute("from").toString();
						String sTo = eElement.getAttribute("to").toString();
						int vFrom = str2Int.get(sFrom);
						int vTo = str2Int.get(sTo);
						dodajKrawedz(vTo,vFrom);
					}
				}
				generujKolorki();

				
			} catch (ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
			}
		}
		
		

	    /*
	     * generuje tablicê kolorów dla pokolorowania grafu
	     */
        private void generujKolorki() {
	
        	Random rnd = new Random();
            
            Color []ct = {Color.GREEN, Color.RED, Color.YELLOW, Color.CYAN, 
                Color.BLUE, Color.PINK, Color.WHITE, Color.MAGENTA, Color.LIGHT_GRAY, Color.ORANGE, Color.PINK, Color.WHITE, Color.GRAY};
            
            coltab = new Color[Math.max(V,  ct.length)]; 
            for(int i = 0; i < ct.length; i++)
            	coltab[i] = ct[i];

            //dodaj te kolory losowo
            for(int i = ct.length-1; i < V; i++)
            	coltab[i] = Color.getHSBColor(rnd.nextFloat(), 1.0f, 1.0f);

        }
        
        
        /*
         * Konstruktor dla randomizowanego grafu
         * v - liczba wierzcho³ków do wygenerowania
         * r1 - r
         * r2 - R
         */
		public Graph(int v, int r1, int r2) {
			
			V = v;
			
			//tworzy tablicê kolorów do pokolorowania grafu
			generujKolorki();
			
			
            maxR = r2;  
			maxRSQ = maxR * maxR;
			rr = r1;
			
			//int rad = (int)((double)maxR *.1);
			Random rnd = new Random();
			for(int i = 0 ; i < V; i++) {
				  
				  int x = rnd.nextInt(maxR*2) - maxR;
				  int y = rnd.nextInt(maxR*2) - maxR;
				  while(maxRSQ < x*x + y*y ) {
			    	  x = rnd.nextInt(maxR*2) - maxR;
			    	  y = rnd.nextInt(maxR*2) - maxR;
  	    	  }
              colors.put(i, Color.yellow);
  	    	  points.put(i, new Point(x,y));
  			  str2Int.put(i+"", i);
			  int2Str.put(i,  i+"");

  	    	  //rysuje wierzcho³ek
  	    	  //g.drawOval((getWidth())/2 + x-2, 5 + maxR +  y-2, 4, 4);
  	    	  //rysuje granice dla r
  	    	  //g.drawOval((getWidth())/2 + x-rad/2, 5 + maxR +  y - rad/2, rad, rad);
  	      }

            

            //<inicjacja> inicjuje tablicÄ™ na listÄ™ sÄ…siedztwa
			adj =(LinkedList<Integer>[]) new LinkedList[V];
			for (int i=0;i<V;i++)
				adj[i]= new LinkedList<Integer>();

			for( int p : points.keySet()) {
				for(int p2 : points.keySet()) {
					if(p != p2) {
						Point pp = points.get(p2);
						double dist = points.get(p).distance(pp);
						if(dist <= rr) {
							dodajKrawedz(p,p2);
						}
					}
				}
				
			}
		}

		/*
		 * Dodaje krawêdz do listy s¹siedztwa
		 * Graf jest nieskierowany dlatego 
		 * krawêdz jest dostêpna z obu wierzcho³ków 
		 */
		public void dodajKrawedz(Integer w, Integer v) {
            adj[v].addFirst((Integer)w);
			adj[w].addFirst((Integer)v);
		}
		
		
		/*
		 * Wrac iterator dla listy s¹siedztwa dla danego wierzcho³ka
		 */
		public Iterable<Integer> adj(int v){
			return adj[v];
		}
 

    /*
     * algorytm zach³annego kolorowania
     */
    void greedyPaintGraph() {

    	if(coltab == null)
    		return;
    	vertices.clear();
    	
    	//iteruj po wszystkich wierzcho³kach
        for(int i = 0; i < V; i++) {

            //generuj now¹ , pe³n¹ tablicê kolorów
            ArrayList<Color> al = new ArrayList<>(Arrays.asList(coltab));
 
            //iteruj po wszystkich s¹siadach wierzcho³ka i
            int vi =0;
            for (int w : adj(i)) {
                //usuñ z tablicy kolorów te kolory, które ju¿ s¹ u¿ywane przez s¹siadów
                al.remove(colors.get(w));
                vi++;
            }
            //zamieñ kolor wierzcho³ka na pierewszy wolny w tablicy kolorów
            colors.replace(i, al.get(0));
            //dodaje liczbe (oznaczaj¹c¹ kolor) do wierzcho³ka
            vertices.put(i, vi);

         }
        bKolor = true;	//oznacz graf jako pokolorowany
    }
    
    /*
     * Zapisuje graf do pliku gxl
     */
	public void saveGraph(String sFile) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			
			//element g³ówny pliku gxl
			Element rootElement = doc.createElement("gxl");
			Attr attr = doc.createAttribute("xmlns:xlink");
			attr.setValue("http://www.w3.org/1999/xlink");
			rootElement.setAttributeNode(attr);
			
			doc.appendChild(rootElement);
			Element graph = doc.createElement("graph");
			attr = doc.createAttribute("edgeids");
			attr.setValue("false");
			graph.setAttributeNode(attr);

			/*
			 * buduj wêz³y w plku XML
			 */

			for(int i = 0; i< V; i++) {
				Element node = doc.createElement("node");
				attr = doc.createAttribute("id");
				attr.setValue(int2Str.get(i));
				node.setAttributeNode(attr);
				
				//dodaj elementy pozycji wierzcho³ka intX i intY
				Element nodeX = doc.createElement("intX");
				Point p = points.get(i);
				nodeX.appendChild(doc.createTextNode(p.x +""));
				Element nodeY = doc.createElement("intY");
				nodeY.appendChild(doc.createTextNode(p.y+""));
				node.appendChild(nodeX);
				node.appendChild(nodeY);
				graph.appendChild(node);
			}
			
			
			/*
			 * buduj krawêdzie w plku XML
			 */
			for(int v = 0; v < V; v++) {
				for(Iterator<Integer> it=adj[v].iterator();it.hasNext();it.next()) {
					int w = (int)it.next();
					if(w == v)
						continue;
					
					Element edge = doc.createElement("edge");
					attr = doc.createAttribute("from");
					attr.setValue(w+"");
					edge.setAttributeNode(attr);
					
					attr = doc.createAttribute("to");
					attr.setValue(v+"");
					edge.setAttributeNode(attr);
					graph.appendChild(edge);

				}
			}

			rootElement.appendChild(graph);
			
			// zapisz zawartoœæ dokumentu do pliku XML
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			
			//dodaj wciêcia i znak nowej lini do elementów dokumentu (formatowanie XML)
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(sFile));

			transformer.transform(source, result);
			
		} catch (ParserConfigurationException | TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}