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
	private Timer timer;
	private Timer timerControl;
	private Timer timerUsers;
	private int contUsers = 0;
	private boolean cantoLinea = false;
	private File ficheroBolas = new File("data/bolas.txt");
	private BufferedWriter fwBolas;
	private FileWriter fwControl;
	private BufferedReader scFicheroControl;
	private Scanner scUsers;
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
		try {
			Scanner sc;
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
		try {
			fwBolas = new BufferedWriter(new FileWriter(ficheroBolas,true));
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		registrarEventos();
	}
	
	public void terminarJuego() {
		timer.stop();
    	timerControl.stop();
    	btnComenzar.setEnabled(true);
    	btnBola.setEnabled(false);
    	btnAuto.setEnabled(false);
    	btnParar.setEnabled(false);
    	numBolas = 0;
		try {
			RandomAccessFile raf = new RandomAccessFile(ficheroUsers, "rw");
			raf.setLength(0); // Esto borra el contenido del archivo
			raf.close();
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
	
	public void sacarBola() {
		if (numBolas==50) {
        	lblBola.setText("No ha habido ningun ganador :(");
        	terminarJuego();
        	return;
        }
		if (bola!=0) {
			String respCorrecta;
	        respCorrecta = respuestas[Integer.valueOf(respuestas[4][bola-1])][bola-1];
			lblAnterior.setText(historial + "\n" + (bola) + " - " + respCorrecta + "" + lblAnterior.getText().substring(19));
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				   public void run() { 
				       scrollPane.getVerticalScrollBar().setValue(0);
				   }
				});
		}
        do {
        	bola = ((int) (Math.random()*50+1));
        } while (haSalido[bola-1]);
        String respCorrecta;
        respCorrecta = respuestas[Integer.valueOf(respuestas[4][bola-1])][bola-1];
        lblBola.setText("Bola nº:"+ (bola) + "\n"+ preguntas[bola-1] + "\n" + respCorrecta + "");
        haSalido[bola-1] = true;
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
		
		timer = new Timer (5000, new ActionListener ()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	sacarBola();   
		     }
		});
		
		timerControl = new Timer (300, new ActionListener ()
		{
		    public void actionPerformed(ActionEvent e)
		    {	
		    	try {
		    		control = Integer.parseInt(scFicheroControl.readLine());
			    	if (control == 1) {
			    		timer.stop();
			    		lblBola.setText("Alguien ha cantado linea.");
			    		String usuario = scFicheroControl.readLine();
			    		int pregunta = Integer.parseInt(scFicheroControl.readLine());
			    		lblBola.setText("Comprobando Linea de "+ usuario + "\r\n"+preguntas[pregunta]);
			    		btnBola.setEnabled(false);
			    		btnAuto.setEnabled(false);
			    	} else if(control == 2) {
			    		fwControl = new FileWriter(ficheroControl,true);
						fwControl.write("0\n");
						fwControl.close();
			    		lblBola.setText("Enhorabuena, la Linea es Correcta.");
			    	} else if(control == 3) {
			    		timer.stop();
			    		lblBola.setText("Alguien ha cantado Folingo.");
			    		String usuario = scFicheroControl.readLine();
			    		int pregunta = Integer.parseInt(scFicheroControl.readLine());
			    		lblBola.setText("Comprobando Folingo de "+ usuario + "\r\n"+preguntas[pregunta]);
			    		btnBola.setEnabled(false);
			    		btnAuto.setEnabled(false);
			    	} else if(control == 4) {
			    		String usuario = scFicheroControl.readLine();
			    		lblBola.setText("El ganador ha sido " + usuario + "");
			    		terminarJuego();
			    	} else if(control == 5) {
			    		fwControl = new FileWriter(ficheroControl,true);
						fwControl.write("0\n");
						fwControl.close();
			    		lblBola.setText("Lo sentimos, la linea no es correcta.");
			    	} else if(control == 6) {
			    		fwControl = new FileWriter(ficheroControl,true);
						fwControl.write("0\n");
						fwControl.close();
			    		lblBola.setText("Lo sentimos, el Bingo no es correcta.");
			    	}
			    	else {
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
		
		timerUsers = new Timer (500, new ActionListener ()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	contUsers=0;
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
	//REGISTRAR LOS EVENTOS actionPerformed PARA LOS 9 BOTONES
			/*for (int i = 0; i < arrayBotones.length; i++) {
				arrayBotones[i].addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						JButton btn=(JButton)e.getSource();	
						btn.setBackground(Color.GREEN);
						btn.setEnabled(false);
						
					}
				});
			}*/
			
			btnComenzar.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					
					if (!btnComenzar.getText().equals("Empezar")) {
						terminarJuego();
						
					} else {
						try {
							Scanner scUsers =  new Scanner(ficheroUsers);
							if (scUsers.hasNext()) {
								JOptionPane.showConfirmDialog(null, "Lo sentimos, Ya se esta jugando una partida, espera a que termine.",
										"LINEA", JOptionPane.CLOSED_OPTION,
										JOptionPane.INFORMATION_MESSAGE);
								System.exit(4);
							}
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
					for(int i=0;i<haSalido.length;i++) haSalido[i]=false;
					timerUsers.start();
					//DESACTIVAR EL PROPIO NUEVA PARTIDA
					lblAnterior.setText("Historial de Bolas:");
					historial = "Historial de Bolas:";
					bola = 0;
					numBolas = 0;
					btnComenzar.setText("Terminar");
					btnAuto.setEnabled(true);
					btnBola.setEnabled(true);
					btnHistorial.setEnabled(true);
				}}
			});
			
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
			
			this.addWindowListener(new java.awt.event.WindowAdapter() {
				@Override
				public void windowClosing(java.awt.event.WindowEvent windowEvent) {
					if(JOptionPane.showConfirmDialog(BingoLocutor.this, "¿Seguro que quieres salir?", "Aviso",
							JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
						terminarJuego();
						System.exit(0);
					}
				}});
			
			btnBola.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//SALIR DEL PROGRAMA PIDIENDO CONFIRMACIÓN
					if (timerUsers.isRunning()) timerUsers.stop();
					sacarBola();
				}
			});
			
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
			
			btnHistorial.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					try {

			            File objetofile = new File ("data/Preguntas.docx");
			            Desktop.getDesktop().open(objetofile);

			     }catch (IOException ex) {

			            System.out.println(ex);

			     }
				}
			});
			
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
