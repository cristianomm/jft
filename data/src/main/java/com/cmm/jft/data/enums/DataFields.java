package com.cmm.jft.data.enums;

public enum DataFields {

	/**
	 * Simbolo
	 */
	SYMBOL("Simbolo"), 

	/**
	 * Valor da Abertura do dia no ativo.
	 */
	OPN("Valor da Abertura do dia no ativo."), 

	/**
	 * Nome do Ativo
	 */
	NME("Nome do Ativo"),

	/**
	 * Data em que o ativo está sendo negociado
	 */
	DAT("Data em que o ativo está sendo negociado"), 

	/**
	 * Valor do fechamento anterior do ativo
	 */
	LST_CLOSE("Valor do fechamento anterior do ativo"), 

	/**
	 * Hora da última negociação
	 */
	TIME("Hora da última negociação"), 

	/**
	 * Valor Máximo do Dia
	 */
	HIGH("Valor Máximo do Dia"), 

	/**
	 * Valor Mínimo do Dia
	 */
	LOW("Valor Mínimo do Dia"), 

	/**
	 * Quantidade de negócios no dia
	 */
	TRADES("Quantidade de negócios no dia"), 

	/**
	 * Quantidade Negociada
	 */
	TRADED_QT("Quantidade Negociada"), 

	/**
	 * Último Preço negociado
	 */
	LST_PRICE("Último Preço negociado"), 

	/**
	 * Quantidade do Último Negócio
	 */
	LST_TRADE_QT("Quantidade do Último Negócio"), 

	/**
	 * Porcentagem entre o valor de fechamento do dia anterior e o valor atual
	 */
	VAR("Porcentagem entre o valor de fechamento do dia anterior e o valor atual"), 

	/**
	 * Volume Financeiro Total do Dia
	 */
	FVT("Volume Financeiro Total do Dia"), 

	/**
	 * Variação do valor entre o fechamento e o último preço do pregão
	 */
	VAP("Variação do valor entre o fechamento e o último preço do pregão"), 

	/**
	 * Volume financeiro do último negócio
	 */
	LFV("Volume financeiro do último negócio"), 

	/**
	 * Oferta de Compra no livro de ofertas
	 */
	BOPx("Oferta de Compra no livro de ofertas"), 

	/**
	 * Oferta de Venda no livro de ofertas
	 */
	SOPx("Oferta de Venda no livro de ofertas"), 

	/**
	 * Quantidade da Oferta de Compra para este determinado preço
	 */
	BOQTx("Quantidade da Oferta de Compra para este determinado preço"), 

	/**
	 * Quantidade da Oferta de Venda para este determinado preço
	 */
	SOQTx("Quantidade da Oferta de Venda para este determinado preço"), 

	/**
	 * Número de Ofertas de Compra para este determinado preço
	 */
	BONx("Número de Ofertas de Compra para este determinado preço"), 

	/**
	 * Número de Ofertas de Venda para este determinado preço
	 */
	SONx("Número de Ofertas de Venda para este determinado preço"), 

	/**
	 * Corretora de Venda
	 */
	SELL("Corretora de Venda"), 

	/**
	 * Corretora de Compra
	 */
	BUY("Corretora de Compra"), 

	/**
	 * Código ISIN
	 */
	ISI("Código ISIN"), 

	/**
	 * Bolsa
	 */
	MKT("Bolsa"), 

	/**
	 * Tipo
	 */
	TYPE("Tipo"), 

	/**
	 * Código da Corretora de Compra
	 */
	BBRK("Código da Corretora de Compra"), 

	/**
	 * Código da Corretora de Venda
	 */
	SBRK("Código da Corretora de Venda"),

	/**
	 * Quoted DateTime
	 */
	QDT("Quoted DateTime"), 

	/**
	 * Acquired DateTime 
	 */
	ADT("Acquired DateTime");

	String description;

	DataFields(String descr) {
		this.description = descr;
	}

}
