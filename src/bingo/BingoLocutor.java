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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.SwingConstants;

public class BingoLocutor extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnAuto;
	private JButton btnParar;
	private JButton btnHistorial;
	private JButton btnBola;
	private JButton btnComenzar;
	private JButton btnSalir;
	private JLabel lblNewLabel_1;
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
	private FileWriter fwBolas;
	private Scanner scFichero;
	private Scanner scFicheroControl;
	private File ficheroControl = new File("data/control.txt");
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
		contentPane.setLayout(null);
		
		lblBola = new JLabel("Bola");
		lblBola.setHorizontalAlignment(SwingConstants.CENTER);
		lblBola.setFont(new Font("Tahoma", Font.PLAIN, 40));
		lblBola.setBounds(10, 14, 613, 427);
		contentPane.add(lblBola);
		
		btnComenzar = new JButton("Empezar");
		btnComenzar.setBounds(633, 377, 89, 64);
		contentPane.add(btnComenzar);
		
		btnSalir = new JButton("Salir");
		btnSalir.setBounds(732, 377, 89, 64);
		contentPane.add(btnSalir);
		
		lblNewLabel_1 = new JLabel("New label");
		lblNewLabel_1.setBounds(633, 14, 188, 197);
		contentPane.add(lblNewLabel_1);
		
		btnBola = new JButton("Sacar Bola");
		btnBola.setBounds(633, 301, 89, 64);
		contentPane.add(btnBola);
		
		btnHistorial = new JButton("Historial");
		btnHistorial.setBounds(732, 301, 89, 64);
		contentPane.add(btnHistorial);
		
		btnAuto = new JButton("Auto");
		btnAuto.setBounds(633, 225, 89, 64);
		contentPane.add(btnAuto);
		
		btnParar = new JButton("Parar");
		btnParar.setBounds(732, 225, 89, 64);
		contentPane.add(btnParar);
		registrarEventos();
	}
	
	
	public void registrarEventos() {
		
		timer = new Timer (5000, new ActionListener ()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	if (numBolas==50) {
		        	timer.stop();
		        	JOptionPane.showConfirmDialog(null, "No ha habido ningun ganador :(",
		                    "CLOSED_OPTION", JOptionPane.CLOSED_OPTION,
		                    JOptionPane.INFORMATION_MESSAGE);
		        	btnComenzar.setEnabled(true);
		        	return;
		        }
		    	int bola;
		        do {
		        	bola = ((int) (Math.random()*50+1)) - 1;
		        } while (haSalido[bola]);
		        lblBola.setText("" + preguntas[bola] + "\r\n" + (bola) + " - " + respuestas[bola] + "");
		        haSalido[bola] = true;
		        try {
					fwBolas = new FileWriter(ficheroBolas);
		        	System.out.println(" "+bola);
					fwBolas.write(" "+bola);
					fwBolas.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        numBolas++;
		        
		     }
		});
		
		timerControl = new Timer (1000, new ActionListener ()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	try {
					scFicheroControl = new Scanner(ficheroControl);
					if (scFicheroControl.nextInt()==1) timer.stop();
					else timer.start();
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
					timer.start();
					try {
						fwBolas = new FileWriter(ficheroBolas);
						fwBolas.write(-1);
						fwBolas.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						fwBolas = new FileWriter(ficheroControl);
						fwBolas.write(0);
						fwBolas.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					//DESACTIVAR EL PROPIO NUEVA PARTIDA
					btnComenzar.setEnabled(false);
				}
			});
			
			/*btnLinea.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					timer.stop();
					//DESACTIVAR EL PROPIO NUEVA PARTIDA
					JOptionPane.showConfirmDialog(null, "Enhorabuena has hecho linea.",
		                    "LINEA", JOptionPane.CLOSED_OPTION,
		                    JOptionPane.INFORMATION_MESSAGE);
					timer.start();
					btnLinea.setEnabled(false);
					cantoLinea = true;
				}
			});*/
			
			/*btnLBingo.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					timer.stop();
					//DESACTIVAR EL PROPIO NUEVA PARTIDA
					JOptionPane.showConfirmDialog(null, "Enhorabuena has hecho bingo!!!!!!!!!!!",
		                    "LINEA", JOptionPane.CLOSED_OPTION,
		                    JOptionPane.INFORMATION_MESSAGE);
					timer.start();
					btnLBingo.setEnabled(false);
					btnJugar.setEnabled(true);
				}
			});*/
			
			btnSalir.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					//SALIR DEL PROGRAMA PIDIENDO CONFIRMACIÓN
					if(JOptionPane.showConfirmDialog(BingoLocutor.this, "¿Seguro que quieres salir?", "Aviso",
							JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
						System.exit(0);
					}
				}
			});
			
		}//FIN DE REGISTRAR EVENTOS
}
