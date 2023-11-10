package bingo;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.border.LineBorder;
import net.miginfocom.swing.MigLayout;
import java.awt.Component;
import java.awt.Desktop;

import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import java.awt.SystemColor;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.Dimension;


public class BingoLocutor extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnAuto;
	private JButton btnParar;
	private JButton btnHistorial;
	private JButton btnBola;
	private JButton btnComenzar;
	private JButton btnSalir;
	private JTextPane lblBola;
	private String[] preguntas = new String[50];
	private String[][] respuestas = new String[5][50];
	private boolean[] haSalido = new boolean[50];
	private int[] numerosCarton = new int[15];
	private int numBolas = 0;
	
	// Creamos 3 timers diferentes, uno para controlar el estado de la partida, otro para simular el sacar una bola cada 5 segundos y un ultimo que estara activo mientras estemos empezando una partida
	// para poder leer en tiempo real los usuarios que se van a uniendo a ella.
	private Timer timer;
	private Timer timerControl;
	private Timer timerUsers;
	
	// Necesitamos poder leer y escribir en diferentes archivos para simular una conexion
	// En el caso del fichero de bolas y del de control, queremos poder escribir y leer en tiempo real, para ello necesitamos BufferedReaders
	// que mas adelante iniciaremos con InputStreams que son capaces de recibir los cambiso a un fichero en tiempo real.
	private BufferedWriter fwBolas;
	private FileWriter fwControl;
	private BufferedReader scFicheroControl;
	private Scanner scUsers;
	private File ficheroBolas = new File("data/bolas.txt");
	private File ficheroControl = new File("data/control.txt");
	private File ficheroUsers = new File("data/users.txt");
	private File ficheroPreguntas = new File("data/Preguntas.txt");
	private boolean auto = false;
	private int bola = 0;
	private String historial;
	private JScrollPane scrollPane;
	private JTextPane lblAnterior;
	private int control;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BingoLocutor frame = new BingoLocutor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public BingoLocutor() {
		setTitle("FOLINGO");
		UIManager.put("textInactiveText", new ColorUIResource(Color.BLACK));
		
		// Lo primero que hacemos al construir el bombo es leer tanto las preguntas como las respuestas
		// Usaremos un scammer que creamos en un InputStream del archivo ficheroPreguntas. Tenemos que decirle que tipo de codificacion tiene el archivo
		// Porque si no no podra leer las ñ ni los acentos. En nuestro caso es UTF-8
		try {
			Scanner sc;
			sc = new Scanner(new InputStreamReader(new FileInputStream(ficheroPreguntas), "UTF-8"));
			int numPreguntas = 0;
			String resp;
			
			// Recorremos el archivo y vamos recogiendo los datos.
			// Las preguntas tienen la misma estructura siempre:
			// La primera linea es la pregunta, las 4 lineas siguientes son las opciones
			// Buscamos cual de las respuestas contiene el string "(Correcta)" y guardamos en la 5 posicion de las respuestas un String con el numero de la respuesta correcta
			while (sc.hasNext()) {	
				int correcta = 0;
				preguntas[numPreguntas] = sc.nextLine();
				for(int i = 0;i<4; i++) {
					resp = sc.nextLine();
					if (resp.toLowerCase().contains("(Correcta)".toLowerCase())) {
						correcta = i;
						resp = resp.substring(0,resp.length()-10);
						respuestas[i][numPreguntas] = resp;
					}
				}
				respuestas[4][numPreguntas] = "" + correcta;
				numPreguntas++;
				if (numPreguntas==50) break;
			}
			sc.close();
		} catch (UnsupportedEncodingException e) {
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 932, 531);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		lblBola = new JTextPane();
		lblBola.setBackground(SystemColor.control);
		lblBola.setText("BIENVENIDO A FOLINGO");
		lblBola.setEditable(false);
		lblBola.setFont(new Font("Britannic Bold", Font.PLAIN, 40));
		
		// Para alinear ciertos elementos necesitamos cambiar los stilos del frame
		// para ello leemos el StyleDocument y lo modificamos directamente. Esto cambiara los estilos del frame del cual hemos cogido el StyleDocument
		StyledDocument doc = lblBola.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
		btnComenzar = new JButton("Empezar");
		
		btnSalir = new JButton("Salir");
		
		btnBola = new JButton("Sacar Bola");
		btnBola.setEnabled(false);
		
		btnHistorial = new JButton("Preguntas");
		btnHistorial.setEnabled(false);
		
		btnAuto = new JButton("Auto");
		btnAuto.setEnabled(false);
		
		btnParar = new JButton("Parar");
		btnParar.setEnabled(false);
		
		// Hemos usado MIG Layout porque es un layout responsivo que nos permite decidir que columnas o filas crecen y decrecen y cuales no.
		contentPane.setLayout(new MigLayout("", "[613px,grow][:100px:100px][:10px:10px][:100px:100px]", "[197px,grow][1px][64px][1px][64px][1px][64px]"));
		contentPane.add(lblBola, "cell 0 0 1 7,alignx center,aligny center");
		
		scrollPane = new JScrollPane();
		scrollPane.setMaximumSize(new Dimension(32767, 400));
		scrollPane.setAutoscrolls(true);
		contentPane.add(scrollPane, "cell 1 0 3 1,grow");
		
		
		lblAnterior = new JTextPane();
		lblAnterior.setEditable(false);
		lblAnterior.setBackground(SystemColor.control);
		scrollPane.setViewportView(lblAnterior);
		contentPane.add(btnAuto, "cell 1 2,grow");
		contentPane.add(btnParar, "cell 3 2,grow");
		contentPane.add(btnBola, "cell 1 4,grow");
		contentPane.add(btnHistorial, "cell 3 4,grow");
		contentPane.add(btnComenzar, "cell 1 6,grow");
		contentPane.add(btnSalir, "cell 3 6,grow");
		
		// Por ultimo iniciamos fwBolas, que sera el Writer que usaremos para escribir las bolas que vamos sacando en el archivo
		try {
			fwBolas = new BufferedWriter(new FileWriter(ficheroBolas,true));
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		registrarEventos();
	}
	
	//Funcion terminarJuego que se usa para parar los timers y ponernos en una situacion en la que tanto el bombo como los cartones sepan que no hay partida creada.
	public void terminarJuego() {
		timer.stop();
    	timerControl.stop();
    	btnComenzar.setEnabled(true);
    	btnBola.setEnabled(false);
    	btnAuto.setEnabled(false);
    	btnParar.setEnabled(false);
    	numBolas = 0;
		try {
			// Durante el proceso de terminar Juego necesitamos dejar en blanco el Fichero Users para que la siguiente partida no tenga los usuarios de la anterior
			// Con Filewriter me estaba dando problemas a la hora de borrar el contenido de archivos ocultos asique encontre esta solucion
			// que es usar un RandomAccessFile con derechos de escritura y lectura y ponerle el tamaño a 0. Este borra el contenido del archivo sin borrar el archivo.
			RandomAccessFile raf = new RandomAccessFile(ficheroUsers, "rw");
			raf.setLength(0); // Esto borra el contenido del archivo
			raf.close();
			
			// En el caso del resto de ficheros siemplemente pongo los estados en los cuales se que no hay partida creada.
			fwControl = new FileWriter(ficheroControl,true);
			fwControl.write("4\n");
			fwControl.close();
			fwControl = new FileWriter(ficheroBolas,true);
			fwControl.write("-1\n");
			fwControl.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		btnComenzar.setText("Empezar");
	}
	
	// Funcion sacarBola para sacar bolas del bombo
	public void sacarBola() {
		// Primero veremos si nos quedan bolas por sacar, si no nos quedan mandaremos una carita triste y terminaremos el Juego.
		if (numBolas==50) {
        	lblBola.setText("No ha habido ningun ganador :(");
        	terminarJuego();
        	return;
        }
		// Cada vez que saquemos bola, lo primero que hacemos es poner la bola anterior y su respuesta en el Historial
		if (bola!=0) {
			String respCorrecta;
	        respCorrecta = respuestas[Integer.valueOf(respuestas[4][bola-1])][bola-1];
			lblAnterior.setText(historial + "\n" + (bola) + " - " + respCorrecta + "" + lblAnterior.getText().substring(19));
			// El historial estan en un JScrollPane para que podamos scrollear hacia abajo para encontrar todas las bolas que hayan salido
			// El problema es que cada bola que sacas por defecto te deja el scrollbar abajo del todo. La solucion que encontre es
			// ponerle la verticalScrollBar a 0 para que se ponga arriba y que pase despues de que cambie algo.
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				   public void run() { 
				       scrollPane.getVerticalScrollBar().setValue(0);
				   }
				});
		}
		
		// Lo siguiente es generar una bola, para ello generamos una bola random del 1 al 50
		// y comprobamos que no haya salido ya esa bola, para ello tenemos un array de booleans haSalido que en cada bola
		//  guarda si ha salido o no
        do {
        	bola = ((int) (Math.random()*50+1));
        } while (haSalido[bola-1]);
        
        // Por ultimo con la bola ya correcta escribimos el numero, la pregunta y la respuesta, y cambiamos el Valor de haSalido en su posicion
        String respCorrecta;
        respCorrecta = respuestas[Integer.valueOf(respuestas[4][bola-1])][bola-1];
        lblBola.setText("Bola nº:"+ (bola) + "\n"+ preguntas[bola-1] + "\n" + respCorrecta + "");
        haSalido[bola-1] = true;
        
        // Finalmente escribimos la bola para que el resto de clientes pueda leerla y sumamos 1 bola al contador de bolas que hemos sacado
        try {
			fwBolas.write("\r\n"+bola);
			fwBolas.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        numBolas++;
	}
	
	public void registrarEventos() {
		
		// Un timer que mientras esta activo, saca una bola cada 5 segundos
		timer = new Timer (5000, new ActionListener ()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	sacarBola();   
		     }
		});
		
		// El timerControl es el que se encarga de controlar en que estado se encuentra la partida
		// En nuestro caso 0: Jugando, 1:Comprobar Linea, 2; Linea Correcta, 3: Comprobar Bingo, 4: Bingo Correcto, 5: Linea Incorrecta, 6:Bingo Incorrecto
		timerControl = new Timer (300, new ActionListener ()
		{
		    public void actionPerformed(ActionEvent e)
		    {	
		    	try {
		    		control = Integer.parseInt(scFicheroControl.readLine());
			    	if (control == 1) {
			    		// En el caso 1, escribimos que estamos comprobando la lina y paramos el auto para que deje de sacar bolas
			    		// Tambien leemos el usuario que canto la linea. Nos aseguramos que los botones de auto y sacar bolas estan inactivos
			    		timer.stop();
			    		String usuario = scFicheroControl.readLine();
			    		int pregunta = Integer.parseInt(scFicheroControl.readLine());
			    		lblBola.setText("Comprobando Linea de "+ usuario + "\r\n"+preguntas[pregunta]);
			    		btnBola.setEnabled(false);
			    		btnAuto.setEnabled(false);
			    	} else if(control == 2) {
			    		// En el caso 2, escribimos que es correcta la linea y escribimos en control 0 para continuar Jugando
			    		fwControl = new FileWriter(ficheroControl,true);
						fwControl.write("0\n");
						fwControl.close();
			    		lblBola.setText("Enhorabuena, la Linea es Correcta.");
			    	} else if(control == 3) {
			    		// En el caso 3, escribimos que estamos comprobando el Bingo y paramos el auto para que deje de sacar bolas
			    		// Tambien leemos el usuario que canto el Bingo. Nos aseguramos que los botones de auto y sacar bolas estan inactivos
			    		timer.stop();
			    		String usuario = scFicheroControl.readLine();
			    		int pregunta = Integer.parseInt(scFicheroControl.readLine());
			    		lblBola.setText("Comprobando Folingo de "+ usuario + "\r\n"+preguntas[pregunta]);
			    		btnBola.setEnabled(false);
			    		btnAuto.setEnabled(false);
			    	} else if(control == 4) {
			    		// En el caso 4, escribimos que es correcta la linea y escribimos en control 0 para continuar Jugando
			    		String usuario = scFicheroControl.readLine();
			    		lblBola.setText("El ganador ha sido " + usuario + "");
			    		terminarJuego();
			    	} else if(control == 5) {
			    		// En el caso 5, escribimos que la linea no es correcta y escribimos en control 0 para continuar Jugando
			    		fwControl = new FileWriter(ficheroControl,true);
						fwControl.write("0\n");
						fwControl.close();
			    		lblBola.setText("Lo sentimos, la linea no es correcta.");
			    	} else if(control == 6) {
			    		// En el caso 6, escribimos que el bingo no es correcta y escribimos en control 0 para continuar Jugando
			    		fwControl = new FileWriter(ficheroControl,true);
						fwControl.write("0\n");
						fwControl.close();
			    		lblBola.setText("Lo sentimos, el Bingo no es correcta.");
			    	}
			    	else {
			    		// en el resto de los casos Continuamos el auto si estabamos en el y si no activamos los botones de auto y de bola.
			    		if (auto) timer.start();
			    		else {
			    			btnBola.setEnabled(true);
			    			btnAuto.setEnabled(true);
			    		}
			    	}
				} catch (NumberFormatException e1) {
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		     }
		});
		
		// Este timer se ejecuta durante la creacion de partida para actualizar la lista de jugadores.
		timerUsers = new Timer (500, new ActionListener ()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	try {
					scUsers =  new Scanner(ficheroUsers);
					String usuarios = "Partida Creada\r\n";
					while (scUsers.hasNext()) {
						usuarios = usuarios + scUsers.next() + "\n";
					}
					
					lblBola.setText(usuarios);
					scUsers.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    	
		    	
		    }
		    
		});

			
			btnComenzar.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					// El boton de empezar tiene 2 modos, cuando ya se esta ejecutando una partida sirve para terminarla
					
					if (!btnComenzar.getText().equals("Empezar")) {
						terminarJuego();
						
					} else {
						
					// El otro caso es el de iniciar programa
						try {
							
							// Primero miramos el archivo de users, si hay usuarios es que ya se esta jugando una partida, si ya hay partida cerramos el rpograma
							Scanner scUsers =  new Scanner(ficheroUsers);
							if (scUsers.hasNext()) {
								JOptionPane.showConfirmDialog(null, "Lo sentimos, Ya se esta jugando una partida, espera a que termine.",
										"LINEA", JOptionPane.CLOSED_OPTION,
										JOptionPane.INFORMATION_MESSAGE);
								System.exit(4);
							}
							
							// Luego iniciamos los archivos para que esten con los datos de una partida nueva usando el metodo anteriormente explicado de RandomAccessFile
							timerControl.start();
							RandomAccessFile raf = new RandomAccessFile(ficheroUsers, "rw");
							raf.setLength(0); // Esto borra el contenido del archivo
							raf.writeBytes( "Jugadores:\n");
							raf.close();
							raf = new RandomAccessFile(ficheroBolas, "rw");
							raf.setLength(0); // Esto borra el contenido del archivo
							raf.close();
							raf = new RandomAccessFile(ficheroControl, "rw");
							raf.setLength(0); // Esto borra el contenido del archivo
							raf.close();
							fwControl = new FileWriter(ficheroControl,true);
							fwControl.write("0\n");
							fwControl.close();
							
							// Iniciamos el reader del fichero de control.
							try {
								scFicheroControl = new BufferedReader(new FileReader("data/control.txt"));
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}	
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
					// Por ultimo reseteamos todas las variables usadas para controlar el bingo a los valores default eh iniciamos el timer de usuarios.
					for(int i=0;i<haSalido.length;i++) haSalido[i]=false;
					timerUsers.start();
					lblAnterior.setText("Historial de Bolas:");
					historial = "Historial de Bolas:";
					bola = 0;
					numBolas = 0;
					
					// Cambiamos el boton para que se pueda usar para terminar la partida.
					btnComenzar.setText("Terminar");
					btnAuto.setEnabled(true);
					btnBola.setEnabled(true);
					btnHistorial.setEnabled(true);
				}}
			});
			
			// Boton de salir, simplemente terminamos la partida para que se puedan crear nuevas y cerramos el programa.
			btnSalir.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					//SALIR DEL PROGRAMA PIDIENDO CONFIRMACIÓN
					if(JOptionPane.showConfirmDialog(BingoLocutor.this, "¿Seguro que quieres salir?", "Aviso",
							JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
						terminarJuego();
						System.exit(0);
					}
				}
			});
			
			// Esto se usa para controlar el boton de la x que cierra la ventana, hace lo mismo que el boton salir.
			this.addWindowListener(new java.awt.event.WindowAdapter() {
				@Override
				public void windowClosing(java.awt.event.WindowEvent windowEvent) {
					if(JOptionPane.showConfirmDialog(BingoLocutor.this, "¿Seguro que quieres salir?", "Aviso",
							JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
						terminarJuego();
						System.exit(0);
					}
				}});
			
			// Boton sacar bola, saca una bola y para el auto del Bingo.
			btnBola.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//SALIR DEL PROGRAMA PIDIENDO CONFIRMACIÓN
					if (timerUsers.isRunning()) timerUsers.stop();
					sacarBola();
				}
			});
			
			// El boton auto, inicia el timer de las bolas de 5 segundos y ponemos la variable auto a true para saber que estamos en auto
			// Desactivamos los botones que no se puedan usar en auto.
			btnAuto.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					//SALIR DEL PROGRAMA PIDIENDO CONFIRMACIÓN
					timer.start();
					if (timerUsers.isRunning()) timerUsers.stop();
					auto = true;
					btnAuto.setEnabled(false);
					btnParar.setEnabled(true);
					btnBola.setEnabled(false);
				}
			});
			
			//  Un boton que abre el archivo que tenemos de las preguntas para que el profesor pueda consultar.
			btnHistorial.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					try {

			            File objetofile = new File ("data/Preguntas.docx");
			            Desktop.getDesktop().open(objetofile);

			     }catch (IOException ex) {


			     }
				}
			});
			
			// Solo se puede pulsar en auto y sirve para parar el timer de las bolas y poner auto a false, tambien activa los botones que e pueden usar en manual.
			btnParar.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					//SALIR DEL PROGRAMA PIDIENDO CONFIRMACIÓN
					timer.stop();
					auto = false;
					btnParar.setEnabled(false);
					btnAuto.setEnabled(true);
					btnBola.setEnabled(true);
				}
			});
			

			
		}//FIN DE REGISTRAR EVENTOS
}
