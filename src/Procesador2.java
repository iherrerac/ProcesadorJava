
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.StyledEditorKit;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.GridLayout;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

/*
 * Procesador de texto basico con StyledEditorKit;
 */

//TODO Menu Archivo (Guardar, Guardar como, Salir
//TODO Integrar las funciones de StyledEditorKit con la posibilidad de cambiar la fuente sin seleccionar
//TODO EN CURSO: Menu Edicion ( Cortar/Copiar/Pegar insertar imagen)
//TODO Cambiar el menu fuente a un desplegable, tamaño y estilo a un combobox en una toolbar

public class Procesador2 {

	public static void main(String[] args) {
		Marco1 marcoInicial = new Marco1();
		marcoInicial.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		marcoInicial.setExtendedState(JFrame.MAXIMIZED_BOTH);//Frame Maximizado
	}

}

class Marco1 extends JFrame{
	public Marco1 () {
		setTitle("Word");
		setBounds(100,100,600,400);

		//Establecemos icono
		setIconImage(new ImageIcon(getClass().getResource("icono.jpg")).getImage());
		add(new Panel1());
		setVisible(true);
	}
}

class Panel1 extends JPanel{
	public Panel1 () {
		setLayout(new BorderLayout());
	//Codigo para agregar a lamina central, pero complica el ejercicio, no toma el tamaño de la lamina
	//	central = new JPanel();
	//	add(central,BorderLayout.CENTER);
		//Menu principal
		menu=new JMenuBar();
		archivo= new JMenu("Archivo");
		edicion= new JMenu("Edicion");
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
		//Menu Archivo
		
		//Menu Edicion
		for (int i = 0; i < menuEdicion.length; i++) {
			JMenuItem mItemEdicion = new JMenuItem(menuEdicion[i],new ImageIcon("bin/iconos/"+iconosMenuEdicion[i]));
			edicion.add(mItemEdicion);
		}
		//Fuentes
		//Menu popup que muestra todas las fuentes
		fuente.getPopupMenu().setLayout(new GridLayout(20,10));
		for (int i = 0; i < nombresDeFuentes.length; i++) {
			JMenuItem fuenteMenu = new JMenuItem(nombresDeFuentes[i]);
			//fuenteMenu.addActionListener(new Fuente("fuente"));
			fuenteMenu.addActionListener(new StyledEditorKit.FontFamilyAction("cambiafuente",nombresDeFuentes[i]));
			fuente.add(fuenteMenu);
		}

		
		//Estilos 
		for (int i = 0; i < setEstilos.length; i++) {
			JMenuItem estiloMenu = new JMenuItem (setEstilos[i]);
			if (setEstilos[i].equalsIgnoreCase("negrita")){
				estiloMenu.addActionListener(new StyledEditorKit.BoldAction());
			} else {
				estiloMenu.addActionListener(new StyledEditorKit.ItalicAction());
			}
			estilo.add(estiloMenu);
		}

		//Tamaño 
		//Declaramos y agregamos botones tamaño
		for (int i = 0; i < setTamanos.length; i++) {
			JMenuItem tmenu = new JMenuItem(""+setTamanos[i]);
			//tmenu.addActionListener(new Fuente("Tamaños"));  
			tmenu.addActionListener(new StyledEditorKit.FontSizeAction("cambiatamaño", setTamanos[i]));  
			tamano.add(tmenu);
		}
		//---------------------------------------------
		
		menu.add(archivo);
		menu.add(edicion);
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

	
//	class Fuente implements ActionListener{
//		public Fuente (String accion) {
//			this.accion = accion;
//			editorestilo = new StyledEditorKit();
//		}
//		
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			
//			switch (accion) {
//			case "fuente":
//				cuadroText.setFont(new Font(e.getActionCommand(),cuadroText.getFont().getStyle(),cuadroText.getFont().getSize()));	
//				break;
//			case "estilos":
//				if (e.getActionCommand().equalsIgnoreCase("negrita")) {
//					if (checkNegrita == 0) {
//						es += Font.BOLD;
//						checkNegrita =1;
//					} else {
//						es-=Font.BOLD;
//						checkNegrita =0;
//					}
//				}
//				else if(e.getActionCommand().equalsIgnoreCase("cursiva")) {
//					if (checkCursiva == 0) {
//						es += Font.ITALIC;
//						checkCursiva =1;
//					} else {
//						es-=Font.ITALIC;
//						checkCursiva =0;
//					}
//				} 
//				cuadroText.setFont(new Font(cuadroText.getFont().getName(),es,cuadroText.getFont().getSize()));
//				break;
//			
//			case "Tamaños":
//				cuadroText.setFont(new Font(cuadroText.getFont().getName(),cuadroText.getFont().getStyle(),Integer.parseInt(e.getActionCommand())));
//				break;
//			
//			default:
//				break;
//			}
//
//		}
//		
//		private String accion;
//
//	}

	private JMenuBar menu;
	JMenu archivo,edicion,fuente,estilo,tamano;
	private String[] menuEdicion = {"Cortar","Copiar","Pergar","Insertar Imagen"};
	//Array con el nombre de las fuentes
	private String[] nombresDeFuentes = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	private String[] setEstilos = {"Negrita","Cursiva"};
	private String[] iconosMenuEdicion = {"cortar.png","copiar.png","pegar.png","insertar.png"};
	//Array con tamaños de fuente
	private int[] setTamanos= {8,10,12,14,16,18,20,22,24};
	private JTextPane cuadroText;
	private int es;
	private int checkNegrita;
	private int checkCursiva;
	private int checkSubrallado;

	//private JPanel central;
}