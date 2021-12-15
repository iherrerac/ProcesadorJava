
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.text.StyledEditorKit;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.GridLayout;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.GraphicsEnvironment;

/*
 * Procesador de texto basico con StyledEditorKit;
 */

//TODO Menu Archivo (Guardar, Guardar como, Salir
//TODO Integrar las funciones de StyledEditorKit con la posibilidad de cambiar la fuente sin seleccionar
//TODO EN CURSO: Agregar funcionalidad al Menu Edicion ( Cortar/Copiar/Pegar insertar imagen)
//TODO Cambiar el menu fuente a un desplegable, tamaño y estilo a un combobox en una toolbar
//TODO el Menu emergente no interactua con los check del Menu Estilo
// (14/12/2021) Creamos menu edicion con imagenes. Agregamos JRadioButtonMenu y JCheckBoxMenu a tamaño y Estilo 
// (14/12/2021) Agregamos Menu Emergente en JTextPane con opciones Negrita/Cursiva 
// (14/12/2021) Agregamos Atajo de letras a los estilos (105 y 109)
// (15/12/2021) Agregamos una Barra de herramientas con Negrita y cursiva
// (15/12/2021) Agregamos una lamina panelNorte gridlayout
// (15/12/2021) Agregamos metodo estiloListener para no tener que repetir el if seleccionando el metodo al agregar el listener a menu,menuemergente o menuherramientas
// (15/12/2021) Agregamos menu de herramientas Colores

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
		panelNorte= new JPanel(new GridLayout(2,1,0,1));
	
		add(panelNorte,BorderLayout.NORTH);
		
	// Codigo para agregar a lamina central, pero complica el ejercicio, no toma el tamaño de la lamina
	//	central = new JPanel();
	//	add(central,BorderLayout.CENTER);
		

				
		cuadroTexto();
		menu();
		MenuEmergente();
		menuHerramientas();
		
//		checkNegrita=0;
//		checkCursiva=0;
//		checkSubrallado=0;
//		es=0;
	}
	
	public void menu(){
		//Menu principal
		menu=new JMenuBar();
		archivo= new JMenu("Archivo");
		edicion= new JMenu("Edicion");
		fuente= new JMenu("Fuente");
		estilo= new JMenu("Estilo");
		tamano= new JMenu("Tamaño");
		
		//Menu Archivo
		
		//Menu Edicion
		for (int i = 0; i < menuEdicion.length; i++) {
			JMenuItem mItemEdicion = new JMenuItem(menuEdicion[i],new ImageIcon(iconosMenuEdicion[i]));
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
			//JMenuItem estiloMenu = new JMenuItem (setEstilos[i]);
			JCheckBoxMenuItem estiloMenu = new JCheckBoxMenuItem (setEstilos[i],new ImageIcon(iconosMenuEstilos[i]));
			if (setEstilos[i].equalsIgnoreCase("negrita")){
				//Agregar atajo teclado CTRL+N 
				estiloMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_DOWN_MASK));
				estiloMenu.addActionListener(estiloListener(setEstilos[i]));
			} else if (setEstilos[i].equalsIgnoreCase("cursiva")){
				//Agregar atajo teclado CTRL+K
				estiloMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K,InputEvent.CTRL_DOWN_MASK));
				estiloMenu.addActionListener(estiloListener(setEstilos[i]));
			} else {
				estiloMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_DOWN_MASK));
				estiloMenu.addActionListener(estiloListener(setEstilos[i]));
			}
			estilo.add(estiloMenu);
		}

		//Tamaño 
		//Declaramos y agregamos botones tamaño
		ButtonGroup grupoTamMenu = new ButtonGroup();
		for (int i = 0; i < setTamanos.length; i++) {
			//JMenuItem tmenu = new JMenuItem(""+setTamanos[i]);
			JRadioButtonMenuItem tmenu = new JRadioButtonMenuItem(""+setTamanos[i]);
			//tmenu.addActionListener(new Fuente("Tamaños")); 
			grupoTamMenu.add(tmenu);
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
		
		panelNorte.add(menu,BorderLayout.NORTH);

	}
	
	//Menu Emergente
	public void MenuEmergente() {
		menuEmergente = new JPopupMenu();
		for (int i = 0; i < setEstilos.length; i++) {
			JMenuItem itemEmergente = new JMenuItem(setEstilos[i]);
			itemEmergente.addActionListener(estiloListener(setEstilos[i]));
			menuEmergente.add(itemEmergente);	
		}
		//asociamos el JTextPane con el menu emergente.
		cuadroText.setComponentPopupMenu(menuEmergente);
	}
	//Agregamos Barra Herramientas
	public void menuHerramientas() {
		menuHerra = new JToolBar();
		for ( int i = 0; i < setEstilos.length; i++) {
			JButton menuHerraEstiloBoton = new JButton(setEstilos[i], new ImageIcon(iconosMenuEstilos[i]));
			menuHerraEstiloBoton.addActionListener(estiloListener(setEstilos[i]));
			menuHerra.add(menuHerraEstiloBoton);
		}
		
		for (int i = 0; i < menuColor.length; i++) {
			JButton menuHerraColorBoton = new JButton("",new ImageIcon(iconosMenucolor[i]));
			menuHerraColorBoton.addActionListener(estiloListener(menuColor[i]));
			menuHerra.add(menuHerraColorBoton);
		}
		menuHerra.addSeparator();
		
		panelNorte.add(menuHerra);
		
	}
	
	public void cuadroTexto() {
		cuadroText= new JTextPane();
		//central.add(cuadroText);
		//cuadroText.setMinimumSize(new Dimension(central.getParent().getWidth(), 0));
		add(cuadroText,BorderLayout.CENTER);
		//cuadroText.setMaximumSize(new Dimension(central.getParent().getWidth(), 0));
	}
	
	
	//Metodo para determinar el oyente de estilo
	public ActionListener estiloListener(String estilo) {
		switch (estilo) {
		case "Negrita":
			return new StyledEditorKit.BoldAction();
			
		case "Cursiva":
			return new StyledEditorKit.ItalicAction();
		
		case "Subrallado":
			return new StyledEditorKit.UnderlineAction();	
		
		default:
			return null;
		}
		
//		if (estilo.equalsIgnoreCase("negrita")){
//			return new StyledEditorKit.BoldAction();
//		} else if(estilo.equalsIgnoreCase("cursiva")){
//			return new StyledEditorKit.ItalicAction();
//		}else return new StyledEditorKit.UnderlineAction();	
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
	//Declaracion de Variables
	//Paneles
	JPanel panelNorte;
	//Menus
	private JMenuBar menu;
	//Menu Emergente
	private JPopupMenu menuEmergente; 
	//Menu de Herramientas
	private JToolBar menuHerra;
	JMenu archivo,edicion,fuente,estilo,tamano;
	//Array con el nombre de las fuentes,Estilos tamaños
	private String[] nombresDeFuentes = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	private String[] menuEdicion = {"Cortar","Copiar","Pergar","Insertar Imagen"};
	private String[] setEstilos = {"Negrita","Cursiva","Subrallado"};
	private String[] menuColor = {"Negro","Naranja","Rosa","Verde","Azul","Amarillo","Blanco"};
	private Color[] setColor= {Color.BLACK,Color.ORANGE,Color.PINK,Color.GREEN,Color.BLUE,Color.YELLOW,Color.WHITE};
	private int[] setTamanos= {8,10,12,14,16,18,20,22,24};
	//Iconos
	private String[] iconosMenuEdicion = {"bin/iconos/cortar.png","bin/iconos/copiar.png","bin/iconos/pegar.png","bin/iconos/insertar.png"};
	private String[] iconosMenuEstilos = {"bin/iconos/negrita.png","bin/iconos/cursiva.png","bin/iconos/subrallado.jpg"};
	private String[] iconosMenucolor = {"bin/iconos/colores/negro.png","bin/iconos/colores/naranja.png","bin/iconos/colores/rosa.png","bin/iconos/colores/verde.png","bin/iconos/colores/azul.png","bin/iconos/colores/amarillo.png","bin/iconos/colores/blanco.png"};

	private JTextPane cuadroText;

	
	//private JPanel central;
//	private int es;
//	private int checkNegrita;
//	private int checkCursiva;
//	private int checkSubrallado;
}