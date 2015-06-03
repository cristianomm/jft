/**
 * 
 */
package com.cmm.jft_ui.forms.binding;

import com.cmm.jft.core.format.Formatter;
import java.awt.Component;


/**
 * <p><code>Binder.java</code></p>
 * @author Cristiano M Martins
 * @version 28/01/2014 23:27:29
 *
 */
public interface Binder {

    /**
     * Atualiza a view com os dados do objeto passado por parametro.
     * @param object Objeto que possui as informacoes a serem passadas para a view.
     */
    void updateView(Object object);
    
    /**
     * Atualiza o modelo de acordo com os dados da interface.
     * @param object Objeto que ira receber as informacoes da view.
     */
    void updateModel(Object object);
    
    /**
     * Realiza a vinculacao entre um componente da interface e a propriedade da view.
     * @param property Propriedade do modelo a ser vinculada ao componente da view.
     * @param component Componente que sera vinculado a propriedade do modelo.
     */
    void register(String property, Component component);
    
    /**
     * Adiciona um formatador a uma propriedade.
     * @param property Propriedade que sera formatada.
     * @param formatter <code>Formatter</code> a ser utilizado para a propriedade.
     */
    void addFormatter(String property, Formatter formatter);
}
