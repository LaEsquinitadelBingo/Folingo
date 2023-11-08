package bingo;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.MetalButtonUI;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;

import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.GridBagLayout;
import net.miginfocom.swing.MigLayout;
import java.awt.SystemColor;
import java.awt.Dimension;

public class Bingo extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btn2;
	private JButton btn1;
	private JButton btn5;
	private JButton btn6;
	private JButton btn7;
	private JButton btn10;
	private JButton btn11;
	private JButton btn12;
	private JButton btn15;
	private JTextPane textNumero;
	private JTextPane textAnterior;
	private JButton btnJugar;
	private JButton btnLBingo;
	private JButton btnLinea;
	private String[] preguntas = new String[50];
	private String[][] respuestas = new String[5][50];
	private JButton[] arrayBotones;
	private int[] numerosCarton = new int[15];
	private int numBolas = 0;
	private Timer timer;
	private Timer parpadeo;
	private boolean cantoLinea = false;
	private BufferedReader scArchivo;
	private BufferedReader scArchivoControl;
	private File fichero;
	private FileWriter fwArchivoControl;
	private File ficheroControl;
	private FileWriter fwArchivoUsers;
	private File ficheroUsers;
	private File ficheroPreguntas;
	private int bola=0;
	private int anterior = 0;
	private JButton btn14;
	private JButton btn13;
	private JButton btn9;
	private JButton btn8;
	private JButton btn4;
	private JButton btn3;
	private JButton botonParpadeando;
	private String usuario;
	private int preguntaLinea, preguntaBingo;
	private int penalizacion = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Bingo frame = new Bingo();
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
	public Bingo() {
		setTitle("FOLINGO");
		UIManager.put("textInactiveText", new ColorUIResource(Color.BLACK));


		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 691, 383);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[89px,grow][89px,grow][89px,grow][89px,grow][89px,grow][89px,grow][89px,grow]", "[120px,grow][64px,grow][62px,grow][64px,grow]"));
		
		textNumero = new JTextPane();
		textNumero.setBackground(SystemColor.control);
		textNumero.setFont(new Font("Tahoma", Font.PLAIN, 45));
		textNumero.setEditable(false);
		contentPane.add(textNumero, "cell 0 0 5 1,alignx center,aligny center");
		StyledDocument doc = textNumero.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
		btn1 = new JButton("");
		btn1.setFocusable(false);
		btn1.setMinimumSize(new Dimension(100, 9));
		btn1.setBackground(SystemColor.activeCaption);
		btn1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(btn1, "cell 0 1,grow");
		
		btn2 = new JButton("");
		btn2.setFocusable(false);
		btn2.setMinimumSize(new Dimension(100, 9));
		btn2.setBackground(SystemColor.activeCaption);
		btn2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(btn2, "cell 1 1,grow");
		
		btn3 = new JButton("");
		btn3.setFocusable(false);
		btn3.setMinimumSize(new Dimension(100, 9));
		btn3.setBackground(SystemColor.activeCaption);
		btn3.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(btn3, "cell 2 1,grow");
		
		btn4 = new JButton("");
		btn4.setFocusable(false);
		btn4.setMinimumSize(new Dimension(100, 9));
		btn4.setBackground(SystemColor.activeCaption);
		btn4.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(btn4, "cell 3 1,grow");
		
		btn5 = new JButton("");
		btn5.setFocusable(false);
		btn5.setMinimumSize(new Dimension(100, 9));
		btn5.setBackground(SystemColor.activeCaption);
		btn5.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(btn5, "cell 4 1,grow");
		
		btn6 = new JButton("");
		btn6.setFocusable(false);
		btn6.setMinimumSize(new Dimension(100, 9));
		btn6.setBackground(SystemColor.activeCaption);
		btn6.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(btn6, "cell 0 2,grow");
		
		btn7 = new JButton("");
		btn7.setFocusable(false);
		btn7.setMinimumSize(new Dimension(100, 9));
		btn7.setBackground(SystemColor.activeCaption);
		btn7.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(btn7, "flowx,cell 1 2,grow");
		
		btn8 = new JButton("");
		btn8.setFocusable(false);
		btn8.setMinimumSize(new Dimension(100, 9));
		btn8.setBackground(SystemColor.activeCaption);
		btn8.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(btn8, "cell 2 2,grow");
		
		btn9 = new JButton("");
		btn9.setFocusable(false);
		btn9.setMinimumSize(new Dimension(100, 9));
		btn9.setBackground(SystemColor.activeCaption);
		btn9.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(btn9, "cell 3 2,grow");
		
		btn10 = new JButton("");
		btn10.setFocusable(false);
		btn10.setMinimumSize(new Dimension(100, 9));
		btn10.setBackground(SystemColor.activeCaption);
		btn10.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(btn10, "cell 4 2,grow");
		
		btnLBingo = new JButton("FOLINGO");
		contentPane.add(btnLBingo, "cell 6 2,grow");
		btnLBingo.setEnabled(false);
		
		btn11 = new JButton("");
		btn11.setFocusable(false);
		btn11.setMinimumSize(new Dimension(100, 9));
		btn11.setBackground(SystemColor.activeCaption);
		btn11.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(btn11, "cell 0 3,grow");
		
		btn12 = new JButton("");
		btn12.setFocusable(false);
		btn12.setMinimumSize(new Dimension(100, 9));
		btn12.setBackground(SystemColor.activeCaption);
		btn12.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(btn12, "cell 1 3,grow");
		
		btn13 = new JButton("");
		btn13.setFocusable(false);
		btn13.setMinimumSize(new Dimension(100, 9));
		btn13.setBackground(SystemColor.activeCaption);
		btn13.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(btn13, "cell 2 3,grow");
		
		btn14 = new JButton("");
		btn14.setFocusable(false);
		btn14.setMinimumSize(new Dimension(100, 9));
		btn14.setBackground(SystemColor.activeCaption);
		btn14.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(btn14, "cell 3 3,grow");
		
		btn15 = new JButton("");
		btn15.setFocusable(false);
		btn15.setMinimumSize(new Dimension(100, 9));
		btn15.setBackground(SystemColor.activeCaption);
		btn15.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(btn15, "cell 4 3,grow");
		
		textAnterior = new JTextPane();
		textAnterior.setBackground(SystemColor.control);
		textAnterior.setEditable(false);
		contentPane.add(textAnterior, "cell 5 0 2 1,alignx center,aligny center");
		StyledDocument doc2 = textAnterior.getStyledDocument();
		SimpleAttributeSet center2 = new SimpleAttributeSet();
		StyleConstants.setAlignment(center2, StyleConstants.ALIGN_CENTER);
		doc2.setParagraphAttributes(0, doc2.getLength(), center2, false);
		
		btnLinea = new JButton("LINEA");
		contentPane.add(btnLinea, "cell 6 1,grow");
		btnLinea.setEnabled(false);
		
		btnJugar = new JButton("JUGAR");
		contentPane.add(btnJugar, "cell 6 3,grow");
		crearArrayBotones();
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setDialogTitle("Elige la carpeta compartida:");
		try {
			fileChooser.setCurrentDirectory(new File(new File(".").getCanonicalPath()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
		    File selectedFile = fileChooser.getSelectedFile();
		    System.out.println("Selected file: " + selectedFile.getAbsolutePath().replace('\\', '/') + "/data/bolas.txt");
		    fichero = new File(selectedFile.getAbsolutePath().replace('\\', '/')+"/data/bolas.txt");
			ficheroControl = new File(selectedFile.getAbsolutePath().replace('\\', '/')+"/data/control.txt");
			ficheroUsers = new File(selectedFile.getAbsolutePath().replace('\\', '/')+"/data/users.txt");
			ficheroPreguntas = new File(selectedFile.getAbsolutePath().replace('\\', '/')+"/data/Preguntas.txt");
		} else System.exit(1);
		try {
			Scanner sc;
			try {
				sc = new Scanner(new InputStreamReader(new FileInputStream(ficheroPreguntas), "UTF-8"));
				int numPreguntas = 0;
				String resp;
				while (sc.hasNext()) {	
					int correcta = 0;
					preguntas[numPreguntas] = sc.nextLine();
					for(int i = 0;i<4; i++) {
						resp = sc.nextLine();
						if (resp.toLowerCase().contains("(Correcta)".toLowerCase())) {
							correcta = i;
							resp = resp.substring(0,resp.length()-10);
							respuestas[i][numPreguntas] = resp;
						} else {
							respuestas[i][numPreguntas] = resp;
						}
					}
					respuestas[4][numPreguntas] = "" + correcta;
					numPreguntas++;
					if (numPreguntas==50) break;
				}
				sc.close();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (FileNotFoundException e) {
			JOptionPane.showConfirmDialog(null, "La ruta no es valida",
					"LINEA", JOptionPane.CLOSED_OPTION,
					JOptionPane.INFORMATION_MESSAGE);
			System.exit(1);
		}
		registrarEventos();
	}
	// FIN DEL CONSTRUCTOR
	
	public void crearCarton() {	
		for (int i = 1; i<6;i++) {
			generarNumeros(i);
		}
		
		for(int i=0;i<numerosCarton.length;i++) {
			arrayBotones[i].setText("<html><center>" + numerosCarton[i] + "</html>");
			arrayBotones[i].setEnabled(false);
			arrayBotones[i].setBackground(null);

		}
		
		numBolas=0;
		cantoLinea = false;
	}
	
	public void generarNumeros(int columna) {
		int numero;
		columna = columna -1;
		boolean esta = false;
		for(int i=0;i<3;i++) {		
			numero = (int) (Math.random()*10+(columna*10+1));
			for(int j=0;j<i;j++) {
				if (numero == numerosCarton[columna+(j*5)]) {
					esta = true;
				}
			}
			if (esta) {
				i--;
				esta = false;
			}
			else numerosCarton[columna+(i*5)] = numero;	
		}
		for (int x = 0; x < 3; x++) {
	        for (int y = 0; y < 2; y++) {
	            int elementoActual = numerosCarton[columna+(y*5)],
	            elementoSiguiente = numerosCarton[(columna+(y*5)) + 5];
	            if (elementoActual > elementoSiguiente) {
	            	numerosCarton[columna+(y*5)] = elementoSiguiente;
	            	numerosCarton[(columna+(y*5)) + 5] = elementoActual;
	            }
	        }
	    }
	}
	
	public boolean estaNombre(String nombre) {
		try {
			Scanner sc;
			String comprobar;
			try {
				sc = new Scanner(new InputStreamReader(new FileInputStream(ficheroUsers), "UTF-8"));
				while (sc.hasNext()) {	
					comprobar = sc.nextLine();
					if (comprobar.equalsIgnoreCase(nombre)) return true;
				}
				sc.close();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public void tengoLinea(){
		if (penalizacion == 0) {
			for (int i=0;i<11;i+=5) {
				if (arrayBotones[i].getBackground()==Color.GREEN && arrayBotones[i+1].getBackground()==Color.GREEN && arrayBotones[i+2].getBackground()==Color.GREEN && arrayBotones[i+3].getBackground()==Color.GREEN && arrayBotones[i+4].getBackground()==Color.GREEN) {
					btnLinea.setEnabled(true);
					preguntaLinea = ((int) (Math.random()*5)) + i;
				}
			}
		}
	}
	
	public void tengoBingo() {
		boolean tengo = true;
		for (int i=0;i<arrayBotones.length;i++) {
			if (arrayBotones[i].getBackground()!=Color.GREEN) tengo = false;
		}
		if(tengo) {
			btnLBingo.setEnabled(true);
			preguntaBingo = (int) (Math.random()*15);
		}
	}
	
	private void comprobarBotones(int bola) {	
		for (int i = 0;i<numerosCarton.length;i++) {
			if (numerosCarton[i] == bola) {
				arrayBotones[i].setEnabled(true);
				arrayBotones[i].setBackground(SystemColor.activeCaption);
				botonParpadeando = arrayBotones[i];
				parpadeo.start();
			} else {
				//arrayBotones[i].setEnabled(false);
				if (arrayBotones[i].isEnabled()) arrayBotones[i].setContentAreaFilled(false);
				if (arrayBotones[i].getBackground()!=Color.GREEN) arrayBotones[i].setBackground(null);
			}
		}	
	}
	
	private void crearArrayBotones() {
		arrayBotones=new JButton[15];
		arrayBotones[0]=btn1;
		arrayBotones[1]=btn2;
		arrayBotones[2]=btn3;
		arrayBotones[3]=btn4;
		arrayBotones[4]=btn5;
		arrayBotones[5]=btn6;
		arrayBotones[6]=btn7;
		arrayBotones[7]=btn8;
		arrayBotones[8]=btn9;
		arrayBotones[9]=btn10;
		arrayBotones[10]=btn11;
		arrayBotones[11]=btn12;
		arrayBotones[12]=btn13;
		arrayBotones[13]=btn14;
		arrayBotones[14]=btn15;
	}
	
	public void registrarEventos() {
		
		timer = new Timer (300, new ActionListener ()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	int control;
		    	try {
		    		control = Integer.parseInt(scArchivoControl.readLine());
		    		if(control == 2) {
			    		cantoLinea = true;
			    		btnLinea.setEnabled(false);
			    	} else if(control == 4) {
			    		btnLBingo.setEnabled(false);
			    		btnLinea.setEnabled(false);
			    		btnJugar.setEnabled(true);
			    		parpadeo.stop();
			    		timer.stop();
			    	} else if(control == -1) {
			    		
			    	}
				} catch (NumberFormatException e1) {
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    	if (anterior!=0 && bola!=anterior) {
		    		textAnterior.setText((anterior)+ "");
		    		if (penalizacion > 0) penalizacion--;
		    	}
		    	try {
		    		anterior = bola;
		    		bola = Integer.parseInt(scArchivo.readLine());	
			    	textNumero.setText("" + (bola) + "");
			    	if (bola!=anterior) {
			    		parpadeo.stop();
			    		botonParpadeando=null;
			    	}
			    	comprobarBotones(bola);
				} catch (NumberFormatException e1) {
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    }
		});
		
		
		parpadeo = new Timer (500, new ActionListener ()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	if (botonParpadeando.getBackground()==SystemColor.activeCaption) {
		    		botonParpadeando.setBackground(null);
		    	} else botonParpadeando.setBackground(SystemColor.activeCaption);
		    }
		});
		
		//REGISTRAR LOS EVENTOS actionPerformed PARA LOS 9 BOTONES
		for (int i = 0; i < arrayBotones.length; i++) {
			arrayBotones[i].addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (timer.isRunning()) {
					JButton btn=(JButton)e.getSource();	
						parpadeo.stop();
						btn.setContentAreaFilled(true);
						btn.setBackground(Color.GREEN);
						btn.setEnabled(false);
						if (!cantoLinea) tengoLinea();
						tengoBingo();
					}
				}
			});
		}
		
		btnJugar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
					try {
						//scArchivoControl = new BufferedReader(new FileReader("data/control.txt"));
						FileInputStream fis = new FileInputStream(ficheroControl);
						InputStreamReader isr = new InputStreamReader(fis);
						scArchivoControl = new BufferedReader(isr);
						//scArchivo= new BufferedReader(new FileReader("data/bolas.txt"));
						fis = new FileInputStream(fichero);
						isr = new InputStreamReader(fis);
						scArchivo = new BufferedReader(isr);
						if (scArchivo.readLine()==null) {
							String s = (String)JOptionPane.showInputDialog(
				                    null,
				                    "Escriba aqui su nombre de Jugador",
				                    "Nombre de Jugador",
				                    JOptionPane.PLAIN_MESSAGE,
				                    null,
				                    null,
				                    "");
							
							//If a string was returned, say so.
							if ((s != null) && (s.length() > 0) && !estaNombre(s)) {
							fwArchivoUsers = new FileWriter(ficheroUsers,true);
							fwArchivoUsers.write("" + s +"\n");
							fwArchivoUsers.close();
							setTitle("FOLINGO - " + s);
							usuario = s;
							timer.start();
							textNumero.setText("Esperando Inicio del Bingo");
							//DESACTIVAR EL PROPIO NUEVA PARTIDA
							btnJugar.setEnabled(false);
							crearCarton();	
							} else JOptionPane.showConfirmDialog(null, "El nombre de Usuario no es valido",
									"LINEA", JOptionPane.CLOSED_OPTION,
									JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane.showConfirmDialog(null, "Lo sentimos, No hay sala creada, espere a la siguiente.",
									"LINEA", JOptionPane.CLOSED_OPTION,
									JOptionPane.INFORMATION_MESSAGE);
						}
					} catch (FileNotFoundException e1) {
						JOptionPane.showConfirmDialog(null, "FOLINGO NO PUEDE ACCEDER A LA RED.",
								"LINEA", JOptionPane.CLOSED_OPTION,
								JOptionPane.INFORMATION_MESSAGE);;    
					} catch (NumberFormatException e1) {

					} catch (IOException e1) {

					}	
			}
		});
		
		btnLinea.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				timer.stop();
				try {
					fwArchivoControl = new FileWriter(ficheroControl,true);
					fwArchivoControl.write("1\n" + usuario + "\n" + (numerosCarton[preguntaLinea]-1) + "\n");
					fwArchivoControl.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				btnLinea.setEnabled(false);
				String[] opciones = new String[4];
				String respu = preguntas[numerosCarton[preguntaLinea]-1] +"\n";
				for(int i = 0;i<4;i++) {
					switch (i) {
						case 0:
							opciones[i]="A ";
							respu += "\nA->" + respuestas[i][numerosCarton[preguntaLinea]-1];
							break;
						case 1:
							opciones[i]="B ";
							respu += "\nB-> " + respuestas[i][numerosCarton[preguntaLinea]-1];
							break;
						case 2:
							opciones[i]="C";
							respu += "\nC-> " + respuestas[i][numerosCarton[preguntaLinea]-1];
							break;
						case 3:
							opciones[i]="D";
							respu += "\nD-> " + respuestas[i][numerosCarton[preguntaLinea]-1];
							break;
					
					}
				}
				int selection = JOptionPane.showOptionDialog(null, respu, "Responda para acertar el Folingo!" , 
						JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);
				if (selection == Integer.valueOf(respuestas[4][numerosCarton[preguntaLinea]-1])) {
					try {
						fwArchivoControl = new FileWriter(ficheroControl,true);
						System.out.println("Linea");
						fwArchivoControl.write("2\n");
						fwArchivoControl.close();
						textNumero.setText("LINEA CORRECTA");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	
					timer.start();
					cantoLinea = true;
				} else {
					try {
						fwArchivoControl = new FileWriter(ficheroControl,true);		
						fwArchivoControl.write("5\n");
						fwArchivoControl.close();
						textNumero.setText("Lo Sentimos, la linea no es correcta.");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	
					timer.start();	
					penalizacion = 3;
				}
				/*JOptionPane.showConfirmDialog(null, "Enhorabuena has hecho linea.",
	                    "LINEA", JOptionPane.CLOSED_OPTION,
	                    JOptionPane.INFORMATION_MESSAGE);*/
				
			}
		});
		
		btnLBingo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				timer.stop();
				//DESACTIVAR EL PROPIO NUEVA PARTIDA
				try {
					fwArchivoControl = new FileWriter(ficheroControl,true);
					System.out.println("Bingo");
					fwArchivoControl.write("3\n" + usuario + "\n" + (numerosCarton[preguntaBingo]-1) + "\n");
					fwArchivoControl.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				btnLBingo.setEnabled(false);
				String[] opciones = new String[4];
				String respu =preguntas[numerosCarton[preguntaBingo]-1] +"\n";
				for(int i = 0;i<4;i++) {
					switch (i) {
						case 0:
							opciones[i]="A";
							respu += "\nA-> " + respuestas[i][numerosCarton[preguntaBingo]-1];
							break;
						case 1:
							opciones[i]="B";
							respu += "\nB-> " + respuestas[i][numerosCarton[preguntaBingo]-1];
							break;
						case 2:
							opciones[i]="C";
							respu += "\nC-> " + respuestas[i][numerosCarton[preguntaBingo]-1];
							break;
						case 3:
							opciones[i]="D";
							respu += "\nD-> " + respuestas[i][numerosCarton[preguntaBingo]-1];
							break;
					
					}
				}
				int selection = JOptionPane.showOptionDialog(null, respu, "Responda para acertar el Folingo!", 
						JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);
				if (selection == Integer.valueOf(respuestas[4][numerosCarton[preguntaBingo]-1])) {
					try {
						fwArchivoControl = new FileWriter(ficheroControl,true);
						fwArchivoControl.write("4\n"+usuario+"\n");
						fwArchivoControl.close();
						textNumero.setText("HAS CANTADO FOLINGO");
						btnJugar.setEnabled(true);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	
				} else {
					try {
						fwArchivoControl = new FileWriter(ficheroControl,true);
						fwArchivoControl.write("6\n");
						fwArchivoControl.close();
						textNumero.setText("Lo Sentimos, el Folingo no es correcto.");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	
					penalizacion = 3;
					timer.start();
				}


				}
			});
		
		/*btnSalir.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//SALIR DEL PROGRAMA PIDIENDO CONFIRMACIÓN
				if(JOptionPane.showConfirmDialog(Bingo.this, "¿Seguro que quieres salir?", "Aviso",
						JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		})*/
		;
		
	}//FIN DE REGISTRAR EVENTOS
}
