/**
 * 
 */
package com.cmm.jft.data.dde;

/**
 * <p>
 * <code>TZDDEFields.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 30/07/2013 00:19:50
 * 
 */
public enum TZDDEFields {

	ABE("Valor da Abertura do dia no ativo."), NOM(
			"Nome do Ativo (Exemplo: Petrobr�s"), DAT(
			"Data que em que neg�cio est� sendo negociado"), FEC(
			"Valor do fechamento anterior do ativo"), HOR(
			"Hora da �ltima negocia��o"), MAX("Valor M�ximo do Dia"), MIN(
			"Valor M�nimo do Dia"), NEG("Quantidade de neg�cios no dia"), QTT(
			"Quantidade Negociada"), ULT("�ltimo Pre�o negociado"), VAR(
			"Porcentagem entre o valor de fechamento do dia anterior e o valor atual"), VFT(
			"Volume Financeiro Total do Dia"), QUL(
			"Quantidade do �ltimo Neg�cio"), VAP(
			"Varia��o do valor entre o fechamento e o �ltimo pre�o do preg�o"), VFU(
			"Volume financeiro do �ltimo neg�cio"), OCPx(
			"Oferta de Compra no livro de ofertas"), OVDx(
			"Oferta de Venda no livro de ofertas"), QOCx(
			"Quantidade da Oferta de Compra para este determinado pre�o"), QOVx(
			"Quantidade da Oferta de Venda para este determinado pre�o"), NOCx(
			"N�mero de Ofertas de Compra para este determinado pre�o"), NOVx(
			"N�mero de Ofertas de Venda para este determinado pre�o"), SEL(
			"Corretora de Venda"), BUY("Corret de Compra"), BDE("Dias �teis"), FOQ(
			"Forma de Cota��o"), IOO("Indicador da Op��o"), ISI("C�digo ISIN"), BOL(
			"Bolsa"), PEO("Pre�o de Exerc�cio"), DEO("Expira��o da Op��o"), SET(
			"Setor"), SUS("Sub-Setor"), SEG("Segmento"), LOT("Lote Padr�o"), POO(
			"C�digo do Ativo da Op��o"), TIP("Tipo"), BUC(
			"C�digo da Corretora de Compra"), SEC(
			"C�digo da Corretora de Venda");

	String description;

	TZDDEFields(String descr) {
		this.description = descr;
	}

}
