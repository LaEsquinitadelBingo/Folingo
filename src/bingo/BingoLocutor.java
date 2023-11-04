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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTable;

public class BingoLocutor extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnAuto;
	private JButton btnParar;
	private JButton btnHistorial;
	private JButton btnBola;
	private JButton btnComenzar;
	private JButton btnSalir;
	private JLabel lblBola;
	private String[] preguntas = new String[50];
	private String[] respuestas = new String[50];
	private boolean[] haSalido = new boolean[50];
	private int[] numerosCarton = new int[15];
	private int numBolas = 0;
	private Timer timer;
	private Timer timerControl;
	private boolean cantoLinea = false;
	private File ficheroBolas = new File("data/bolas.txt");
	private BufferedWriter fwBolas;
	private FileWriter fwControl;
	private BufferedReader scFicheroControl;
	private File ficheroControl = new File("data/control.txt");
	private boolean auto = false;
	private JLabel lblAnterior;
	private int bola = 0;
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
		for (int i=0;i<preguntas.length;i++) {
			preguntas[i] = "";
		}
		for (int i=0;i<respuestas.length;i++) {
			respuestas[i] = "";
		}
		for (int i=0;i<haSalido.length;i++) {
			haSalido[i] = false;
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 847, 491);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		lblBola = new JLabel("Empezando la partida.");
		lblBola.setBounds(10, 14, 613, 427);
		lblBola.setHorizontalAlignment(SwingConstants.CENTER);
		lblBola.setFont(new Font("Tahoma", Font.PLAIN, 40));
		
		btnComenzar = new JButton("Empezar");
		btnComenzar.setBounds(633, 377, 89, 64);
		
		btnSalir = new JButton("Salir");
		btnSalir.setBounds(732, 377, 89, 64);
		
		lblAnterior = new JLabel("New label");
		lblAnterior.setBounds(633, 14, 188, 197);
		lblAnterior.setHorizontalAlignment(SwingConstants.CENTER);
		
		btnBola = new JButton("Sacar Bola");
		btnBola.setBounds(633, 301, 89, 64);
		btnBola.setEnabled(false);
		
		btnHistorial = new JButton("Historial");
		btnHistorial.setBounds(732, 301, 89, 64);
		btnHistorial.setEnabled(false);
		
		btnAuto = new JButton("Auto");
		btnAuto.setBounds(633, 225, 89, 64);
		btnAuto.setEnabled(false);
		
		btnParar = new JButton("Parar");
		btnParar.setBounds(732, 225, 89, 64);
		btnParar.setEnabled(false);
		contentPane.setLayout(null);
		contentPane.add(lblBola);
		contentPane.add(lblAnterior);
		contentPane.add(btnAuto);
		contentPane.add(btnParar);
		contentPane.add(btnBola);
		contentPane.add(btnHistorial);
		contentPane.add(btnComenzar);
		contentPane.add(btnSalir);
		
		JTable table = new JTable();
		table.setEnabled(false);
		table.setBounds(10, 14, 613, 427);
		contentPane.add(table);
		try {
			fwBolas = new BufferedWriter(new FileWriter(ficheroBolas,true));
			try {
				scFicheroControl = new BufferedReader(new FileReader("data/control.txt"));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		registrarEventos();
	}
	
	public void sacarBola() {
		if (numBolas==50) {
        	timer.stop();
        	lblBola.setText("No ha habido ningun ganador :(");
        	btnComenzar.setEnabled(true);
        	btnBola.setEnabled(false);
        	btnAuto.setEnabled(false);
        	btnParar.setEnabled(false);
    		timerControl.stop();
    		try {
				fwControl = new FileWriter(ficheroControl,true);
				fwControl.write("4\n");
				fwControl.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	return;
        }
		if (bola!=0) lblAnterior.setText((bola) + " - " + respuestas[bola-1] + "");
        do {
        	bola = ((int) (Math.random()*50+1));
        } while (haSalido[bola-1]);
        lblBola.setText("" + preguntas[bola-1] + "\r\n" + (bola) + " - " + respuestas[bola-1] + "");
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
		    	int control;
		    	try {
		    		control = Integer.parseInt(scFicheroControl.readLine());
			    	if (control == 1) {
			    		timer.stop();
			    		lblBola.setText("Alguien ha cantado linea.");
			    		btnBola.setEnabled(false);
			    		btnAuto.setEnabled(false);
			    	} else if(control == 2) {
			    		fwControl = new FileWriter(ficheroControl,true);
						fwControl.write("0\n");
						fwControl.close();
			    		lblBola.setText("Enhorabuena, la Linea es Correcta.");
			    	} else if(control == 3) {
			    		timer.stop();
			    		lblBola.setText("Alguien ha cantado bingo.");
			    		btnBola.setEnabled(false);
			    		btnAuto.setEnabled(false);
			    	} else if(control == 4) {
			    		lblBola.setText("El ganador ha sido X Jugador");
			    		timer.stop();
			    		timerControl.stop();
			    		btnComenzar.setEnabled(true);
			    		btnAuto.setEnabled(false);
						btnBola.setEnabled(false);
						btnHistorial.setEnabled(false);
						btnParar.setEnabled(false);
			    	} else {
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
					timerControl.start();
					try {
						new FileWriter(ficheroBolas, false).close();
						new FileWriter(ficheroControl, false).close();
						fwControl = new FileWriter(ficheroControl,true);
						fwControl.write("0\n");
						fwControl.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					//DESACTIVAR EL PROPIO NUEVA PARTIDA
					lblBola.setText("Sala Creada\nEsperando Jugadores.");
					lblAnterior.setText("");
					bola = 0;
					numBolas = 0;
					btnComenzar.setEnabled(false);
					btnAuto.setEnabled(true);
					btnBola.setEnabled(true);
					btnHistorial.setEnabled(true);
				}
			});
			
			btnSalir.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					//SALIR DEL PROGRAMA PIDIENDO CONFIRMACIÓN
					if(JOptionPane.showConfirmDialog(BingoLocutor.this, "¿Seguro que quieres salir?", "Aviso",
							JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
						try {
							fwBolas.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						System.exit(0);
					}
				}
			});
			btnBola.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					//SALIR DEL PROGRAMA PIDIENDO CONFIRMACIÓN
					sacarBola();
				}
			});
			
			btnAuto.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					//SALIR DEL PROGRAMA PIDIENDO CONFIRMACIÓN
					timer.start();
					auto = true;
					btnAuto.setEnabled(false);
					btnParar.setEnabled(true);
					btnBola.setEnabled(false);
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
