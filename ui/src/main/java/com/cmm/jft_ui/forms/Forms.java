package com.cmm.jft_ui.forms;


/**
 * <p><code>Forms.java</code></p>
 * @author Cristiano M Martins
 * @version 02/08/2013 01:48:29
 *
 */
public interface Forms {
    
    /**
     * Altera o objeto que contem informacoes para a tela
     * @param data
     */
    void setData(Object data);
    
    /**
     * Recupera os dados da interface e os passa ao objeto da tela
     */
    void parseData();

    /**
     * Adiciona Listeners nos comandos
     */
    void addListeners();

    /**
     * Carrega valores nos componentes
     */
    void loadData();

    /**
     * Salva o objeto associado a tela
     */
    void save();
    
    /**
     * Atualiza o objeto associado a tela
     */
    void update();
    
    /**
     * Remove o objeto associado a tela
     */
    void remove();
    
    /**
     * Executa acoes ao fechar a tela
     */
    void close();
    
    
    //boolean checkModification();
    
    void setState(FormStates state);
    
    /**
     * Ajusta a posicao onde a tela sera mostrada ao ser iniciada.
     * @param x Posicao x
     * @param y Posicao y
     */
    void setPosition(int x, int y);
}
