package com.cmm.jft_ui.utils;
import java.awt.*;
import javax.swing.*;

public class SplashScreen extends JWindow {

    private int duration;

    public SplashScreen(int d) {
	duration = d;
    }

    // Este é um método simples para mostrar uma tela de apresentção
    // no centro da tela durante a quantidade de tempo passada no construtor
    public void showSplash() {        
	JPanel content = (JPanel)getContentPane();
	content.setBackground(Color.white);

	// Configura a posição e o tamanho da janela
	int width = 450;
	int height =115;
	setSize(width, height);
	setLocationRelativeTo(null);
	
	// Constrói o splash screen
	JLabel label = new JLabel(new ImageIcon("devmedia.gif"));
	JLabel copyrt = new JLabel("Copyright 2006, DevMedia", JLabel.CENTER);
	copyrt.setFont(new Font("Sans-Serif", Font.BOLD, 12));
	content.add(label, BorderLayout.CENTER);
	content.add(copyrt, BorderLayout.SOUTH);
	Color oraRed = new Color(156, 20, 20,  255);
	content.setBorder(BorderFactory.createLineBorder(oraRed, 10));        
	// Torna visível
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
	// Mostra uma imagem com o título da aplicação 
	SplashScreen splash = new SplashScreen(10000);
	splash.showSplashAndExit();        
    }
}
