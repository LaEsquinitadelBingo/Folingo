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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class Bingo extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btn2;
	private JButton btn1;
	private JButton btn3;
	private JButton btn4;
	private JButton btn5;
	private JButton btn6;
	private JButton btn7;
	private JButton btn8;
	private JButton btn9;
	private JButton btn10;
	private JButton btn11;
	private JButton btn12;
	private JButton btn13;
	private JButton btn14;
	private JButton btn15;
	private JTextPane textNumero;
	private JTextPane textAnterior;
	private JButton btnJugar;
	private JButton btnLBingo;
	private JButton btnLinea;
	private String[] preguntas = new String[50];
	private String[] respuestas = new String[50];
	private JButton[] arrayBotones;
	private int[] numerosCarton = new int[15];
	private int numBolas = 0;
	private Timer timer;
	private boolean cantoLinea = false;
	private BufferedReader scArchivo;
	private BufferedReader scArchivoControl;
	private File fichero = new File("data/bolas.txt");
	private FileWriter fwArchivoControl;
	private File ficheroControl = new File("data/control.txt");
	private FileWriter fwArchivoUsers;
	private File ficheroUsers = new File("data/users.txt");
	private int bola=0;
	private int anterior = 0;

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
		for (int i=0;i<preguntas.length;i++) {
			preguntas[i] = "";
		}
		for (int i=0;i<respuestas.length;i++) {
			respuestas[i] = "";
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 627, 377);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textNumero = new JTextPane();
		textNumero.setFont(new Font("Tahoma", Font.PLAIN, 30));
		textNumero.setEditable(false);
		textNumero.setBounds(10, 11, 441, 120);
		contentPane.add(textNumero);
		StyledDocument doc = textNumero.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
		btn1 = new JButton("");
		btn1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btn1.setBounds(10, 144, 89, 64);
		contentPane.add(btn1);
		
		btn2 = new JButton("");
		btn2.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btn2.setBounds(98, 144, 89, 64);
		contentPane.add(btn2);
		
		btn3 = new JButton("");
		btn3.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btn3.setBounds(186, 144, 89, 64);
		contentPane.add(btn3);
		
		btn4 = new JButton("");
		btn4.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btn4.setBounds(274, 144, 89, 64);
		contentPane.add(btn4);
		
		btn5 = new JButton("");
		btn5.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btn5.setBounds(362, 144, 89, 64);
		contentPane.add(btn5);
		
		btn6 = new JButton("");
		btn6.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btn6.setBounds(10, 207, 89, 64);
		contentPane.add(btn6);
		
		btn7 = new JButton("");
		btn7.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btn7.setBounds(98, 207, 89, 64);
		contentPane.add(btn7);
		
		btn8 = new JButton("");
		btn8.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btn8.setBounds(186, 207, 89, 64);
		contentPane.add(btn8);
		
		btn9 = new JButton("");
		btn9.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btn9.setBounds(274, 207, 89, 64);
		contentPane.add(btn9);
		
		btn10 = new JButton("");
		btn10.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btn10.setBounds(362, 207, 89, 64);
		contentPane.add(btn10);
		
		btn11 = new JButton("");
		btn11.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btn11.setBounds(10, 270, 89, 64);
		contentPane.add(btn11);
		
		btn12 = new JButton("");
		btn12.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btn12.setBounds(98, 270, 89, 64);
		contentPane.add(btn12);
		
		btn13 = new JButton("");
		btn13.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btn13.setBounds(186, 270, 89, 64);
		contentPane.add(btn13);
		
		btn14 = new JButton("");
		btn14.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btn14.setBounds(274, 270, 89, 64);
		contentPane.add(btn14);
		
		btn15 = new JButton("");
		btn15.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btn15.setBounds(362, 270, 89, 64);
		contentPane.add(btn15);
		
		textAnterior = new JTextPane();
		textAnterior.setBounds(472, 11, 129, 120);
		contentPane.add(textAnterior);
		StyledDocument doc2 = textAnterior.getStyledDocument();
		SimpleAttributeSet center2 = new SimpleAttributeSet();
		StyleConstants.setAlignment(center2, StyleConstants.ALIGN_CENTER);
		doc2.setParagraphAttributes(0, doc2.getLength(), center2, false);
		
		btnLinea = new JButton("LINEA");
		btnLinea.setBounds(517, 144, 84, 64);
		contentPane.add(btnLinea);
		btnLinea.setEnabled(true);
		
		btnLBingo = new JButton("FOLINGO");
		btnLBingo.setBounds(517, 207, 84, 64);
		contentPane.add(btnLBingo);
		btnLBingo.setEnabled(true);
		
		btnJugar = new JButton("JUGAR");
		btnJugar.setBounds(517, 270, 84, 64);
		contentPane.add(btnJugar);
		crearArrayBotones();
		registrarEventos();
	}
	// FIN DEL CONSTRUCTOR
	
	public void crearCarton() {	
		int numero;
		boolean esta = false;
		for(int i=0;i<numerosCarton.length;i++) {
			
			numero = (int) (Math.random()*50+1);
			for(int j=0;j<i;j++) {
				if (numero == numerosCarton[j]) {
					esta = true;
				}
			}
			if (esta) {
				i--;
				esta = false;
			}
			else numerosCarton[i] = numero;
		}
		
		for (int x = 0; x < numerosCarton.length; x++) {
	        // Aquí "y" se detiene antes de llegar
	        // a length - 1 porque dentro del for, accedemos
	        // al siguiente elemento con el índice actual + 1
	        for (int y = 0; y < numerosCarton.length - 1; y++) {
	            int elementoActual =numerosCarton[y],
	                    elementoSiguiente = numerosCarton[y + 1];
	            if (elementoActual > elementoSiguiente) {
	                // Intercambiar
	            	numerosCarton[y] = elementoSiguiente;
	            	numerosCarton[y + 1] = elementoActual;
	            }
	        }
	    }
		
		for(int i=0;i<numerosCarton.length;i++) {
			arrayBotones[i].setText("<html><center>" + respuestas[numerosCarton[i]-1] + "<br>" + numerosCarton[i] + "</html>");
			arrayBotones[i].setEnabled(false);
			arrayBotones[i].setBackground(null);

		}
		
		numBolas=0;
		cantoLinea = false;
	}
	
	
	public void tengoLinea(){
		for (int i=0;i<11;i+=5) {
			if (arrayBotones[i].getBackground()==Color.GREEN && arrayBotones[i+1].getBackground()==Color.GREEN && arrayBotones[i+2].getBackground()==Color.GREEN && arrayBotones[i+3].getBackground()==Color.GREEN && arrayBotones[i+4].getBackground()==Color.GREEN) {
				btnLinea.setEnabled(true);
			}
		}
	}
	
	public void tengoBingo() {
		boolean tengo = true;
		for (int i=0;i<arrayBotones.length;i++) {
			if (arrayBotones[i].getBackground()!=Color.GREEN) tengo = false;
		}
		if(tengo) btnLBingo.setEnabled(true);
	}
	private void comprobarBotones(int bola) {
		for (int i = 0;i<numerosCarton.length;i++) {
			if (numerosCarton[i] == bola) {
				arrayBotones[i].setEnabled(true);
			} else arrayBotones[i].setEnabled(false);
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
			    		timer.stop();
			    	} else if(control == -1) {
			    		
			    	}
				} catch (NumberFormatException e1) {
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    	if (anterior!=0 && bola!=anterior) textAnterior.setText((anterior) + " - " + respuestas[anterior-1] + "");
		    	try {
		    		anterior = bola;
		    		bola = Integer.parseInt(scArchivo.readLine());	
			    	textNumero.setText("" + preguntas[bola-1] + "\r\n" + (bola) + " - " + respuestas[bola-1] + "");
			    	comprobarBotones(bola);
				} catch (NumberFormatException e1) {
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    }
		});

		
		//REGISTRAR LOS EVENTOS actionPerformed PARA LOS 9 BOTONES
		for (int i = 0; i < arrayBotones.length; i++) {
			arrayBotones[i].addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					JButton btn=(JButton)e.getSource();	
					btn.setBackground(Color.GREEN);
					btn.setEnabled(false);
					if (!cantoLinea) tengoLinea();
					tengoBingo();
				}
			});
		}
		
		btnJugar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = (String)JOptionPane.showInputDialog(
	                    null,
	                    "Escriba aqui su nombre de Jugador",
	                    "Nombre de Jugador",
	                    JOptionPane.PLAIN_MESSAGE,
	                    null,
	                    null,
	                    "");

				//If a string was returned, say so.
				if ((s != null) && (s.length() > 0)) {
					setTitle("FOLINGO - " + s);
					try {
						fwArchivoUsers = new FileWriter(ficheroUsers,true);
						fwArchivoUsers.write(""+ s +"\n");
						fwArchivoUsers.close();
						scArchivoControl = new BufferedReader(new FileReader("data/control.txt"));
						scArchivo= new BufferedReader(new FileReader("data/bolas.txt"));
						if (scArchivo.readLine()==null) {
							timer.start();
							textNumero.setText("Esperando Inicio del Bingo");
							//DESACTIVAR EL PROPIO NUEVA PARTIDA
							btnJugar.setEnabled(false);
							crearCarton();	
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
				} else JOptionPane.showConfirmDialog(null, "El nombre de Usuario no es valido",
						"LINEA", JOptionPane.CLOSED_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
				
			}
		});
		
		btnLinea.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				timer.stop();
				try {
					fwArchivoControl = new FileWriter(ficheroControl,true);
					fwArchivoControl.write("1\n");
					fwArchivoControl.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
				JOptionPane.showConfirmDialog(null, "Enhorabuena has hecho linea.",
	                    "LINEA", JOptionPane.CLOSED_OPTION,
	                    JOptionPane.INFORMATION_MESSAGE);
				try {
					fwArchivoControl = new FileWriter(ficheroControl,true);
					fwArchivoControl.write("2\n");
					fwArchivoControl.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
				timer.start();
				btnLinea.setEnabled(false);
				cantoLinea = true;
			}
		});
		
		btnLBingo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				timer.stop();
				//DESACTIVAR EL PROPIO NUEVA PARTIDA
				try {
					fwArchivoControl = new FileWriter(ficheroControl,true);
					fwArchivoControl.write("3\n");
					fwArchivoControl.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
				JOptionPane.showConfirmDialog(null, "Enhorabuena has hecho linea.",
	                    "LINEA", JOptionPane.CLOSED_OPTION,
	                    JOptionPane.INFORMATION_MESSAGE);
				try {
					fwArchivoControl = new FileWriter(ficheroControl,true);
					fwArchivoControl.write("4\n");
					fwArchivoControl.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				btnLBingo.setEnabled(false);
				btnJugar.setEnabled(true);
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
