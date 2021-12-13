
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.GridLayout;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

/*
 * Procesador de texto basico con JTextPane
 */

public class Procesador {

	public static void main(String[] args) {
		Marco marcoInicial = new Marco();
		marcoInicial.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		marcoInicial.setExtendedState(JFrame.MAXIMIZED_BOTH);//Frame Maximizado
	}

}

class Marco extends JFrame{
	public Marco () {
		setTitle("Word");
		setBounds(100,100,600,400);

		//Establecemos icono
		setIconImage(new ImageIcon(getClass().getResource("icono.jpg")).getImage());
		add(new Panel());
		setVisible(true);
	}
}

class Panel extends JPanel{
	public Panel () {
		setLayout(new BorderLayout());
	//Codigo para agregar a lamina central, pero complica el ejercicio, no toma el tamaño de la lamina
	//	central = new JPanel();
	//	add(central,BorderLayout.CENTER);
		//Menu principal
		menu=new JMenuBar();
		fuente= new JMenu("Fuente");
		estilo= new JMenu("Estilo");
		tamano= new JMenu("Tamaño");
				
		cuadroTexto();
		menu();
		
		checkNegrita=0;
		checkCursiva=0;
		checkSubrallado=0;
		es=0;
	}
	
	public void menu(){

		//Fuentes
		//Menu popup que muestra todas las fuentes
		fuente.getPopupMenu().setLayout(new GridLayout(20,10));
		for (int i = 0; i < nombresDeFuentes.length; i++) {
			JMenuItem fuenteMenu = new JMenuItem(nombresDeFuentes[i]);
			fuenteMenu.addActionListener(new Fuente("fuente"));
			fuente.add(fuenteMenu);
		}

		
		//Estilos 
		for (int i = 0; i < setEstilos.length; i++) {
			JMenuItem estiloMenu = new JMenuItem (setEstilos[i]);
			estiloMenu.addActionListener(new Fuente("estilos"));
			estilo.add(estiloMenu);
		}

		//Tamaño 
		//Declaramos y agregamos botones tamaño
		for (int i = 0; i < setTamanos.length; i++) {
			JMenuItem tmenu = new JMenuItem(""+setTamanos[i]);
			tmenu.addActionListener(new Fuente("Tamaños"));  
			tamano.add(tmenu);
		}
		//---------------------------------------------
		
		menu.add(fuente);
		menu.add(estilo);
		menu.add(tamano);

		//---------------------------------------------
		
		add(menu,BorderLayout.NORTH);

	}
	public void cuadroTexto() {
		cuadroText= new JTextPane();
		//central.add(cuadroText);
		//cuadroText.setMinimumSize(new Dimension(central.getParent().getWidth(), 0));
		add(cuadroText,BorderLayout.CENTER);
		//cuadroText.setMaximumSize(new Dimension(central.getParent().getWidth(), 0));
	}
	
	
	class Fuente implements ActionListener{
		public Fuente (String accion) {
			this.accion = accion;

		}
		@Override
		public void actionPerformed(ActionEvent e) {

			if(accion.equalsIgnoreCase("fuente")) {
				
				cuadroText.setFont(new Font(e.getActionCommand(),cuadroText.getFont().getStyle(),cuadroText.getFont().getSize()));	
			} else if (accion.equalsIgnoreCase("estilos")) {
				
				if (e.getActionCommand().equalsIgnoreCase("negrita")) {
					if (checkNegrita == 0) {
						es += Font.BOLD;
						checkNegrita =1;
					} else {
						es-=Font.BOLD;
						checkNegrita =0;
					}
				}
				else if(e.getActionCommand().equalsIgnoreCase("cursiva")) {
					if (checkCursiva == 0) {
						es += Font.ITALIC;
						checkCursiva =1;
					} else {
						es-=Font.ITALIC;
						checkCursiva =0;
					}
				} 
				cuadroText.setFont(new Font(cuadroText.getFont().getName(),es,cuadroText.getFont().getSize()));
			} else cuadroText.setFont(new Font(cuadroText.getFont().getName(),cuadroText.getFont().getStyle(),Integer.parseInt(e.getActionCommand())));	
		}
		
		private String accion;

	}

	private JMenuBar menu;
	JMenu fuente,estilo,tamano;
	//Array con el nombre de las fuentes
	private String[] nombresDeFuentes = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	private String[] setEstilos = {"Negrita","Cursiva"};
	//Array con tamaños de fuente
	private int[] setTamanos= {8,10,12,14,16,18,20,22,24};
	private JTextPane cuadroText;
	private int es;
	private int checkNegrita;
	private int checkCursiva;
	private int checkSubrallado;

	//private JPanel central;
}