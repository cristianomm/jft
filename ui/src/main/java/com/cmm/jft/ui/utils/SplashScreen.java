package com.cmm.jft.ui.utils;
import java.awt.*;
import javax.swing.*;

public class SplashScreen extends JWindow {

    private int duration;

    public SplashScreen(int d) {
	duration = d;
    }

    // Este � um m�todo simples para mostrar uma tela de apresent��o
    // no centro da tela durante a quantidade de tempo passada no construtor
    public void showSplash() {        
	JPanel content = (JPanel)getContentPane();
	content.setBackground(Color.white);

	// Configura a posi��o e o tamanho da janela
	int width = 450;
	int height =115;
	setSize(width, height);
	setLocationRelativeTo(null);
	
	// Constr�i o splash screen
	JLabel label = new JLabel(new ImageIcon("devmedia.gif"));
	JLabel copyrt = new JLabel("Copyright 2006, DevMedia", JLabel.CENTER);
	copyrt.setFont(new Font("Sans-Serif", Font.BOLD, 12));
	content.add(label, BorderLayout.CENTER);
	content.add(copyrt, BorderLayout.SOUTH);
	Color oraRed = new Color(156, 20, 20,  255);
	content.setBorder(BorderFactory.createLineBorder(oraRed, 10));        
	// Torna vis�vel
	setVisible(true);

	// Espera ate que os recursos estejam carregados
	try { Thread.sleep(duration); } catch (Exception e) {}        
	setVisible(false);        
    }

    public void showSplashAndExit() {        
	showSplash();
	System.exit(0);        
    }

    public static void main(String[] args) {        
	// Mostra uma imagem com o t�tulo da aplica��o 
	SplashScreen splash = new SplashScreen(10000);
	splash.showSplashAndExit();        
    }
}
