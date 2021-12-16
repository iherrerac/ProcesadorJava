
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
// (16/12/2021) Agregamos funcionalidad menu de herramientas Colores (215 -220)
// (16/12/2021) Agregamos menu de herramientas y funcionalidad  Alineacion (-)
// (16/12/2021) Rehacemos el switch de la funcion estiloListener para que este mas optimizada
// (16/12/2021) Creamos metodo menuHerraBoton para optimizar el codigo del menu Herramientas
// (16/12/2021) Creamos metodo menuBoton para optimizar el codigo de creacion de los JMenuItems del menu (Edicion,Fuentes)


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
		//Paneles
		panelNorte= new JPanel(new GridLayout(2,1,0,1));
		add(panelNorte,BorderLayout.NORTH);
		
		//Llamada a metodos
		cuadroTexto();
		menu();
		MenuEmergente();
		menuHerramientas();

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
//			JMenuItem mItemEdicion = new JMenuItem(menuEdicion[i],new ImageIcon(iconosMenuEdicion[i]));
//			edicion.add(mItemEdicion);
			menuBoton(menuEdicion[i],new ImageIcon(iconosMenuEdicion[i]),"mEdicion");
		}
		//Fuentes
		//Menu popup que muestra todas las fuentes
		fuente.getPopupMenu().setLayout(new GridLayout(20,10));
		for (int i = 0; i < nombresDeFuentes.length; i++) {
//			JMenuItem fuenteMenu = new JMenuItem(nombresDeFuentes[i]);
//			fuenteMenu.addActionListener(new StyledEditorKit.FontFamilyAction("cambiafuente",nombresDeFuentes[i]));
//			fuente.add(fuenteMenu);
			menuBoton(nombresDeFuentes[i],new ImageIcon(),"mFuentes");
		}

		//Estilos 
		for (int i = 0; i < setEstilos.length; i++) {
			JCheckBoxMenuItem estiloMenu = new JCheckBoxMenuItem (setEstilos[i],new ImageIcon(iconosMenuEstilos[i]));
			estiloMenu.addActionListener(menuListener("Estilo",setEstilos[i]));
			
			if (setEstilos[i].equalsIgnoreCase("negrita")){
				//Agregar atajo teclado CTRL+N 
				estiloMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_DOWN_MASK));
			} else if (setEstilos[i].equalsIgnoreCase("cursiva")){
				//Agregar atajo teclado CTRL+K
				estiloMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K,InputEvent.CTRL_DOWN_MASK));
			} else {
				estiloMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_DOWN_MASK));
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
			itemEmergente.addActionListener(menuListener("Estilo",setEstilos[i]));
			menuEmergente.add(itemEmergente);	
		}
		//asociamos el JTextPane con el menu emergente.
		cuadroText.setComponentPopupMenu(menuEmergente);
	}
	//Agregamos Barra Herramientas
	public void menuHerramientas() {
		menuHerra = new JToolBar();
		for ( int i = 0; i < setEstilos.length; i++) {
//			JButton menuHerraEstiloBoton = new JButton(setEstilos[i], new ImageIcon(iconosMenuEstilos[i]));
//			menuHerraEstiloBoton.addActionListener(menuListener("Estilo",setEstilos[i]));
//			menuHerra.add(menuHerraEstiloBoton);
			menuHerraBoton(setEstilos[i],new ImageIcon(iconosMenuEstilos[i]),"mhEstilo");
		}
		menuHerra.addSeparator();
		for (int i = 0; i < menuColor.length; i++) {
//			JButton menuHerraColorBoton = new JButton("",new ImageIcon(iconosMenucolor[i]));
//			menuHerraColorBoton.addActionListener(menuListener("Color",menuColor[i]));
//			menuHerra.add(menuHerraColorBoton);
			menuHerraBoton(menuColor[i],new ImageIcon(iconosMenucolor[i]),"mhColor");
		}
		menuHerra.addSeparator();
		for (int i = 0; i < menuAlinea.length; i++) {
//			JButton menuHerraAlineaBoton = new JButton("",new ImageIcon(iconosMenuAlinea[i]));
//			menuHerraAlineaBoton.addActionListener(menuListener("Alinea",menuAlinea[i]));
//			menuHerra.add(menuHerraAlineaBoton);
			menuHerraBoton(menuAlinea[i],new ImageIcon(iconosMenuAlinea[i]),"mhAlinea");
		}
		
		panelNorte.add(menuHerra);
		
	}
	public void menuBoton(String nombreBoton, ImageIcon icono, String opcion) {
		JMenuItem menuItem = new JMenuItem(nombreBoton,icono);
		menuItem.addActionListener(menuListener(opcion,nombreBoton));
		switch (opcion) {
		case "mFuentes":
			fuente.add(menuItem);
			break;
		case "mEdicion":
			edicion.add(menuItem);
			break;
			
		default:
			break;
		}
		
	}
	
	public void menuHerraBoton(String nombreBoton, ImageIcon icono, String opcion) {
		JButton menuHerraJButton = new JButton("",icono);
		menuHerraJButton.addActionListener(menuListener(opcion,nombreBoton));
		menuHerra.add(menuHerraJButton);	
	}
	
	public void cuadroTexto() {
		cuadroText= new JTextPane();
		//central.add(cuadroText);
		//cuadroText.setMinimumSize(new Dimension(central.getParent().getWidth(), 0));
		add(cuadroText,BorderLayout.CENTER);
		//cuadroText.setMaximumSize(new Dimension(central.getParent().getWidth(), 0));
	}
	
	
	//Metodo para determinar el oyente de estilo
	public ActionListener menuListener(String opcion, String accion) {
		switch (opcion) {
		case "mFuentes":
			return new StyledEditorKit.FontFamilyAction("cambiafuente",accion);
		
		case "mEdicion"://TODO Pendiente
			return new StyledEditorKit.FontFamilyAction("cambiafuente",accion);
			
		case"mhEstilo":
			
			if(accion.equalsIgnoreCase("Negrita")){
				return new StyledEditorKit.BoldAction();
			} else if(accion.equalsIgnoreCase("Cursiva")){
				return new StyledEditorKit.ItalicAction();
			} else if (accion.equalsIgnoreCase("Subrallado")){
					return new StyledEditorKit.UnderlineAction();
			}
		
		case "mhColor":
			//Un for que recorre array de menuColor lo compara con el boton y establece la accion
			for (int i=0; i<menuColor.length;i++) {
				if (accion.equalsIgnoreCase(menuColor[i])){
					return new StyledEditorKit.ForegroundAction(menuColor[i],setColor[i]);
				}
			}
		case "mhAlinea":
			for (int i = 0; i < menuAlinea.length; i++) {
					if(accion.equalsIgnoreCase(menuAlinea[i])) {
						return new StyledEditorKit.AlignmentAction(menuAlinea[i],i);
					}
			}
			
		default:
			return null;
		}
		
	}
	
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
	private String[] menuAlinea = {"Izquierda","Centrar","Derecha","Justificar"};
	
	private Color[] setColor= {Color.BLACK,Color.ORANGE,Color.PINK,Color.GREEN,Color.BLUE,Color.YELLOW,Color.WHITE};
	private int[] setTamanos= {8,10,12,14,16,18,20,22,24};
	//Iconos
	private String[] iconosMenuEdicion = {"bin/iconos/cortar.png","bin/iconos/copiar.png","bin/iconos/pegar.png","bin/iconos/insertar.png"};
	private String[] iconosMenuEstilos = {"bin/iconos/negrita.png","bin/iconos/cursiva.png","bin/iconos/subrallado.jpg"};
	private String[] iconosMenucolor = {"bin/iconos/colores/negro.png","bin/iconos/colores/naranja.png","bin/iconos/colores/rosa.png","bin/iconos/colores/verde.png","bin/iconos/colores/azul.png","bin/iconos/colores/amarillo.png","bin/iconos/colores/blanco.png"};
	private String[] iconosMenuAlinea = {"bin/iconos/alineacion/AIzquierda.png","bin/iconos/alineacion/ACentrar.png","bin/iconos/alineacion/ADerecha.png", "bin/iconos/alineacion/AJustificada.png"};

	private JTextPane cuadroText;

	
	//private JPanel central;
//	private int es;
//	private int checkNegrita;
//	private int checkCursiva;
//	private int checkSubrallado;
}