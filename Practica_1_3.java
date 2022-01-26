import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Practica_1_3 {
	static Fichas[][] tablero = new Fichas[3][3];

	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setTitle("Tres en Raya");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel laminaNorte = new JPanel();
		JPanel laminaCentro = new JPanel();
		JPanel laminaSur = new JPanel();

		crearLaminaNorteYCentro(laminaNorte, laminaCentro);
		crearLaminaSur(laminaSur);
		
		f.add(laminaNorte, BorderLayout.NORTH);
		f.add(laminaCentro, BorderLayout.CENTER);
		f.add(laminaSur, BorderLayout.SOUTH);

		ajustarFrame(f);
	}
	//Ajustamos el frame segun las especificaciones y para que salga en medio de la pantalla
	private static void ajustarFrame(JFrame f) {
		Toolkit pantalla = Toolkit.getDefaultToolkit();
		Dimension d = pantalla.getScreenSize();
		int alto = d.height;
		int ancho = d.width;
		f.setSize(450, 300);
		f.setLocation(ancho / 3, alto / 3);
		f.setResizable(false);
		f.setVisible(true);
	}
	//Creamos la parte sur del frame
	private static void crearLaminaSur(JPanel laminaSur) {
		JLabel textoS = new JLabel("Autor: Carlos Santos Vargas");

		laminaSur.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 8));
		laminaSur.setBackground(Color.BLACK);
		textoS.setFont(new Font("Arial", Font.BOLD, 10));
		textoS.setForeground(Color.WHITE);
		laminaSur.add(textoS);
	}
	//Creamos la parte norte y central del frame donde estará el tablero
	private static void crearLaminaNorteYCentro(JPanel laminaNorte, JPanel laminaCentro) {
		laminaNorte.setLayout(new FlowLayout(FlowLayout.CENTER, 36, 10));
		
		JLabel textoN = new JLabel("Bienvenido al Tres en Rayas");
		JButton comenzar = new JButton("Comenzar");
		
		textoN.setFont(new Font("Arial", Font.BOLD, 20));

		comenzar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				laminaCentro.removeAll();
				crearTablero(laminaCentro).updateUI();

			}
		});
		laminaNorte.add(textoN);
		laminaNorte.add(comenzar);
	}
	//Con este método creamos el tablero y el propio juego
	private static JPanel crearTablero(JPanel lamina) {
		lamina.setLayout(new GridLayout(3, 3));
		int contador = 0;
		for (int x = 0; x < tablero.length; x++) {
			for (int y = 0; y < tablero.length; y++) {
				contador++;
				tablero[x][y] = new Fichas(0);
				tablero[x][y].addActionListener(new Juego());
				lamina.add(tablero[x][y]);
			}
		}
		return lamina;
	}
}
//Clase que hereda de JButton con un entero para poder dar funcionalidad al tablero
class Fichas extends JButton {

	private int ficha;

	public Fichas(int ficha) {
		super();
		this.ficha = ficha;
	}

	public int getFicha() {
		return ficha;
	}

	public void setFicha(int ficha) {
		this.ficha = ficha;
	}
}
//Clase en la que se aplicará la funcionalidad como tal del tres en raya
class Juego implements ActionListener {

	static boolean turno = false;
	static int contadorFichas = 0;
	static ImageIcon jugador1 = new ImageIcon("src/jugador1.png");
	static ImageIcon jugador2 = new ImageIcon("src/jugador2.png");

	@Override
	public void actionPerformed(ActionEvent e) {
		
		//Ajustamos las imágenes al tamaño del botón
		
		int anchoBoton = ImageObserver.WIDTH * 45;
		int altoBoton = ImageObserver.HEIGHT * 25;
		
		Image imgJugador1 = jugador1.getImage();
		Image newImgJugador1 = imgJugador1.getScaledInstance(anchoBoton, altoBoton, Image.SCALE_SMOOTH);
		jugador1 = new ImageIcon(newImgJugador1);

		Image imgJugador2 = jugador2.getImage();
		Image newImgJugador2 = imgJugador2.getScaledInstance(anchoBoton, altoBoton, Image.SCALE_SMOOTH);
		jugador2 = new ImageIcon(newImgJugador2);
		
		//Asignamos las imágenes para que sean fichas
		for (int x = 0; x < Practica_1_3.tablero.length; x++) {
			for (int y = 0; y < Practica_1_3.tablero.length; y++) {

				if (e.getSource() == Practica_1_3.tablero[x][y]) {

					if (turno == false) {
						Practica_1_3.tablero[x][y].setFicha(0);
						Practica_1_3.tablero[x][y].setIcon(jugador1);
						Practica_1_3.tablero[x][y].setEnabled(false);
						Practica_1_3.tablero[x][y].setDisabledIcon(jugador1);
						turno = true;
						Practica_1_3.tablero[x][y].setFicha(1);
					} else {
						Practica_1_3.tablero[x][y].setFicha(0);
						Practica_1_3.tablero[x][y].setIcon(jugador2);
						Practica_1_3.tablero[x][y].setEnabled(false);
						Practica_1_3.tablero[x][y].setDisabledIcon(jugador2);
						turno = false;
						Practica_1_3.tablero[x][y].setFicha(2);
					}
					contadorFichas++;
				}
			}
		}
		
		//Cuando haya un mínimo de 5 fichas comprobará si hay un ganador, no tendría mucho sentido que comprobase habiendo solo una ficha
		if (contadorFichas >= 5) {
			if (comprobar() == true) {
				juegoTerminado();
				contadorFichas = 0;
			}
		}
	}
	//Comprobamos las diferentes posibilidades de ganar y la posibilidad de empate
	boolean comprobar() {

		for (int posicion = 0; posicion < Practica_1_3.tablero.length; posicion++) {
			// vertical
			if (Practica_1_3.tablero[0][posicion].getFicha() != 0 && Practica_1_3.tablero[1][posicion].getFicha() != 0
				&& Practica_1_3.tablero[2][posicion].getFicha() != 0) {

				if (Practica_1_3.tablero[0][posicion].getFicha() == Practica_1_3.tablero[1][posicion].getFicha()
					&& Practica_1_3.tablero[0][posicion].getFicha() == Practica_1_3.tablero[2][posicion].getFicha()) {

					if (Practica_1_3.tablero[0][posicion].getFicha() == 1) {
						JOptionPane.showMessageDialog(null, "Jugador 1 ha ganado");
						return true;
					} else {
						JOptionPane.showMessageDialog(null, "Jugador 2 ha ganado");
						return true;
					}
				}
			}
		}

		for (int posicion = 0; posicion < Practica_1_3.tablero.length; posicion++) {
			if (Practica_1_3.tablero[posicion][0].getFicha() != 0 && Practica_1_3.tablero[posicion][1].getFicha() != 0
				&& Practica_1_3.tablero[posicion][2].getFicha() != 0) {

				if (Practica_1_3.tablero[posicion][0].getFicha() == Practica_1_3.tablero[posicion][1].getFicha()
					&& Practica_1_3.tablero[posicion][0].getFicha() == Practica_1_3.tablero[posicion][2].getFicha()) {

					if (Practica_1_3.tablero[posicion][0].getFicha() == 1) {
						JOptionPane.showMessageDialog(null, "Jugador 1 ha ganado");
						return true;
					} else {
						JOptionPane.showMessageDialog(null, "Jugador 2 ha ganado");
						return true;
					}
				}
			}
		}
		if (Practica_1_3.tablero[0][0].getFicha() != 0 && Practica_1_3.tablero[1][1].getFicha() != 0
			&& Practica_1_3.tablero[2][2].getFicha() != 0) {

			if (Practica_1_3.tablero[0][0].getFicha() == Practica_1_3.tablero[1][1].getFicha()
				&& Practica_1_3.tablero[0][0].getFicha() == Practica_1_3.tablero[2][2].getFicha()) {

				if (Practica_1_3.tablero[0][0].getFicha() == 1) {
					JOptionPane.showMessageDialog(null, "Jugador 1 ha ganado");
					return true;
				} else {
					JOptionPane.showMessageDialog(null, "Jugador 2 ha ganado");
					return true;
				}
			}
		}

		if (Practica_1_3.tablero[2][0].getFicha() != 0 && Practica_1_3.tablero[1][1].getFicha() != 0
			&& Practica_1_3.tablero[0][2].getFicha() != 0) {

			if (Practica_1_3.tablero[2][0].getFicha() == Practica_1_3.tablero[1][1].getFicha()
				&& Practica_1_3.tablero[2][0].getFicha() == Practica_1_3.tablero[0][2].getFicha()) {

				if (Practica_1_3.tablero[2][0].getFicha() == 1) {
					JOptionPane.showMessageDialog(null, "Jugador 1 ha ganado");
					return true;
				} else {
					JOptionPane.showMessageDialog(null, "Jugador 2 ha ganado");
					return true;
				}
			}
		}
		if(comprobarEmpate() == true) {JOptionPane.showMessageDialog(null, "El juego ha acabao en empate");}

		return false;
	}

	private boolean comprobarEmpate() {
		for (int x = 0; x < Practica_1_3.tablero.length; x++) {
			for (int y = 0; y < Practica_1_3.tablero.length; y++) {
				if(Practica_1_3.tablero[x][y].isEnabled() == true) {
					return false;
				}
			}
		}
		return true;
	}
	//Bloqueamos y reiniciamos el tablero para cuando se termine una partida
	void juegoTerminado() {

		for (int x = 0; x < Practica_1_3.tablero.length; x++) {
			for (int y = 0; y < Practica_1_3.tablero.length; y++) {
				Practica_1_3.tablero[x][y].setEnabled(false);
				Practica_1_3.tablero[x][y].setFicha(0);
			}
		}
	}

}
