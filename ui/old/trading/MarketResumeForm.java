/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cmm.jft.ui.trading;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import com.cmm.jft.marketdata.service.Market;
import com.cmm.jft.marketdata.service.MarketDataService;
import com.cmm.jft.ui.ObjectForms;
import com.cmm.jft.ui.forms.AbstractForm;
import com.cmm.jft.ui.forms.Events;
import com.cmm.jft.ui.forms.FormsFactory;
import com.cmm.jft.ui.models.MarketResumeTableModel;

/**
 *
 * @author cristiano
 */
public class MarketResumeForm extends AbstractForm {

    /**
     * Creates new form SecurityStatusForm
     */
    public MarketResumeForm() {
	initComponents();
	tblResume.setModel(new MarketResumeTableModel());
	tblResume.setFillsViewportHeight(true);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblResume = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        btnBook = new javax.swing.JButton();
        btnTrades = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Market Resume");
        setPreferredSize(new java.awt.Dimension(850, 200));

        jScrollPane1.setToolTipText("");
        jScrollPane1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        tblResume.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Ativo", "Último", "Variação", "Compra", "Venda", "Mínimo", "Máximo", "Médio", "Abertura", "Fechamento", "Volume", "Negócios"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblResume.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane1.setViewportView(tblResume);

        jToolBar1.setRollover(true);

        btnBook.setText("Book");
        btnBook.setFocusable(false);
        btnBook.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBook.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnBook);

        btnTrades.setText("Trades");
        btnTrades.setFocusable(false);
        btnTrades.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTrades.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnTrades);

        jButton3.setText("jButton3");
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 850, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
	/* Set the Nimbus look and feel */
	//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
	/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
	 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
	 */
	try {
	    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
		if ("Nimbus".equals(info.getName())) {
		    javax.swing.UIManager.setLookAndFeel(info.getClassName());
		    break;
		}
	    }
	} catch (ClassNotFoundException ex) {
	    java.util.logging.Logger.getLogger(MarketResumeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (InstantiationException ex) {
	    java.util.logging.Logger.getLogger(MarketResumeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (IllegalAccessException ex) {
	    java.util.logging.Logger.getLogger(MarketResumeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (javax.swing.UnsupportedLookAndFeelException ex) {
	    java.util.logging.Logger.getLogger(MarketResumeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>

	/* Create and display the form */
	java.awt.EventQueue.invokeLater(new Runnable() {
	    public void run() {
		new MarketResumeForm().setVisible(true);
	    }
	});
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBook;
    private javax.swing.JButton btnTrades;
    private javax.swing.JButton jButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTable tblResume;
    // End of variables declaration//GEN-END:variables



    @Override
    public void addListeners() {
	tblResume.addKeyListener(new GerEvents(this));
	btnBook.addActionListener(new GerEvents(this));
	btnTrades.addActionListener(new GerEvents(this));
	super.addListeners();
    }

    @Override
    public void loadData() {
	// TODO Auto-generated method stub
	super.loadData();
    }


    private class GerEvents extends Events{

	/**
	 * @param frame
	 */
	public GerEvents(AbstractForm frame) {
	    super(frame);
	}


	/* (non-Javadoc)
	 * @see com.cmm.jft.ui.forms.Events#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

	    int selected = tblResume.getSelectedRow();
	    
	    if(selected >= 0) {
		
		Market mkt = ((MarketResumeTableModel)tblResume.getModel()).getMarket(selected);
		
		if (e.getSource() == btnBook) {
		    FormsFactory.openForm(ObjectForms.BOOK, mkt);
		}
		else if (e.getSource() == btnTrades) {
		    FormsFactory.openForm(ObjectForms.TIME_SALES, mkt);
		}
	    }
	}


	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent ke) {
	    // TODO Auto-generated method stub

	}


	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent ke) {

	}


	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent ke) {
	    if(ke.getKeyCode()==10) {
		int row = tblResume.getSelectedRow();
		if(row > 0) {
		    String symbol = (String) tblResume.getModel().getValueAt(row, 0);
		    if(symbol!=null && symbol.length()>4) {		    
			Market m = MarketDataService.getInstance().hasMarketSymbol(symbol);
			if(m!=null) {
			    ((MarketResumeTableModel)tblResume.getModel()).addMarket(m);
			    ((MarketResumeTableModel)tblResume.getModel()).addMarket(null);
			}
		    }
		}
	    }
	}

    }


}
