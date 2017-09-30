/**
 * 
 */
package com.cmm.jft.data.extractor.marketdata;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.log4j.Level;

import com.cmm.jft.core.format.DateTimeFormatter;
import com.cmm.jft.core.format.DoubleFormatter;
import com.cmm.jft.core.format.FormatterFactory;
import com.cmm.jft.core.format.FormatterTypes;
import com.cmm.jft.core.format.IntFormatter;
import com.cmm.jft.data.files.CSV;
import com.cmm.jft.marketdata.MDEntry;
import com.cmm.jft.messaging.fix44.Fix44EngineMessageEncoder;
import com.cmm.jft.security.Security;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.enums.OrderTypes;
import com.cmm.jft.trading.enums.OrderValidityTypes;
import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.trading.enums.TradeTypes;
import com.cmm.jft.trading.exceptions.OrderException;
import com.cmm.jft.vo.Extractable;
import com.cmm.jft.vo.OrderEventVO;
import com.cmm.logging.Logging;



/**
 * <p>
 * <code>BovespaOfferFileExtractor.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 06/03/2015 16:17:16
 *
 */
public class BovespaOfferFileExtractor extends BovespaFileExtractor {
    
    
    private static long clOrdID = System.currentTimeMillis();
    
    public static void main(String[] args) {

	BovespaOfferFileExtractor bfe = new BovespaOfferFileExtractor();
	bfe.fileName = "D:\\Disco\\Users\\Cristiano\\Downloads\\BMF Files\\CPA_CONS_T1.csv";
	List<Extractable>ls = bfe.extract();
	System.out.println("Events: " + ls.size());

	List<MDEntry> events = new ArrayList(ls.size());
	ls.forEach(ex -> events.add((MDEntry) ex));

	Map<Long,List<MDEntry>> orders = 
		events.stream().collect(Collectors.groupingByConcurrent(MDEntry::getOrderID));
	
	System.out.println("Orders: " + orders.size());

	//agrupa por sequenceid e cria ofertas baseado  no comportamento registrado no log
	orders.forEach((id,lst) -> processEvents(lst));


    }

    private static void processEvents(List<MDEntry> events) {
	
	MDEntry entry = events.get(0);
	Date time = entry.getMdEntryDateTime();
	boolean sameTime = events.parallelStream().allMatch(evt -> evt.getMdEntryDateTime().equals(time));

	Fix44EngineMessageEncoder encoder = Fix44EngineMessageEncoder.getInstance();
	
	String senderLct = "";
	String brokerID = "";
	String traderID = "";
	
	
	try {
	    Orders o = new Orders(entry.getOrderID(), clOrdID+"", new Security(entry.getSymbol()), entry.getSide(), 
		    entry.getMdEntryPx(), entry.getMdEntrySize(), OrderTypes.Limit, traderID, brokerID, senderLct);

	    //no mesmo tempo, pode ser  FOK/ IOC
	    if(sameTime) {

		MDEntry lstEvt = events.get(events.size()-1); 

		//caso a ordem tenha sido enviada e nao tenha sido executada, sera uma ordem FOK
		if(lstEvt.getOrderEvent() == 3 && lstEvt.getOrderStatus() == 0 && lstEvt.getOrderStatus() == 4) {
		    o.setValidityType(OrderValidityTypes.FOK);


		}
		//ordem enviada e totamente executada no instante que eh disponibilizada
		else if(lstEvt.getOrderEvent() == 4 && lstEvt.getOrderStatus() == 2) {
		    o.setValidityType(OrderValidityTypes.FOK);

		}

		//caso a ordem seja enviada e nao tenha sido completamente executada, sera uma ordem FAK
		else if(lstEvt.getOrderEvent() == 3 && lstEvt.getOrderStatus() == 1) {
		    o.setValidityType(OrderValidityTypes.IOC);
		}

		//caso a ordem seja executada em mais de um nivel de preco, sera uma ordem a mercado 
		else {
		    o.setOrderType(OrderTypes.Market);
		    o.setPrice(lstEvt.getMdEntryPx());
		}
	    }
	    else {

		/*
		 * caso a ordem seja executada em mais de um nivel de preco, em tempos diferentes,
		 * sera uma ordem limitada, neutra ou agredida, que eh enviada e permanece no book
		 * ate a sua completa execucao
		 */
		o.setValidityType(OrderValidityTypes.DAY);

	    }
	    
	    System.out.println(encoder.newOrderSingle(o));

	}catch(OrderException e) {
	    e.printStackTrace();
	}

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cmm.jft.data.extractor.Extractor#extract()
     */
    @Override
    public List<Extractable> extract() {

	List<Extractable> bsEvents = new ArrayList<>(1000000);
	try {
	    DateTimeFormatter dtmf = (DateTimeFormatter) FormatterFactory.getFormatter(FormatterTypes.DATE_F8);
	    DateTimeFormatter dtf = (DateTimeFormatter) FormatterFactory.getFormatter(FormatterTypes.DATE_TIME_F8);
	    DateTimeFormatter tf = (DateTimeFormatter) FormatterFactory.getFormatter(FormatterTypes.TIME_F4);
	    IntFormatter intf = (IntFormatter) FormatterFactory.getFormatter(FormatterTypes.INT);

	    CSV csv = new CSV(fileName, ";", "RT", "RH");
	    while (csv.hasNext()) {
		String[] vs = csv.readLine();

		if (vs != null && vs[0] != null) {
		    MDEntry entry = new MDEntry();
		    entry.setEntryDate(dtmf.parse(vs[0]));
		    matcher = pTime.matcher(vs[6]);
		    if (matcher.find()) {
			vs[6] = matcher.group();
		    }

		    // layout dos arquivos foi alterado devido a mudanca para o
		    // sistema de negociacao Puma
		    if (vs.length == 12) {// /....!fk
			// -----------------------------------------------------------
			// Coluna Posicao Inicial Tamanho Descricao
			// -----------------------------------------------------------
			// Data Sessao 1 10 Data da Sessao
			// Papel 50 12 Codigo do Instrumento
			// Sequencia 63 10 Numero de Sequencia da Oferta
			// Preco Of.Compra 74 20 Preco da Oferta
			// Qtd.Total Of.Compra 95 18 Quantidade Total
			// Qtd.Negociada Of.Compra 114 18 Quantidade Negociada
			// Hora Prioridade 133 15 Hora de registro da oferta no
			// sistema (com a precisao Tandem, no formato,
			// HH:MM:SS.NNNNNN), utilizada como indicadora de
			// prioridade
			// Data de Entrada Of.Compra 149 19 Data/Hora de Entrada
			// da Oferta
			// Estado Of.Compra 169 1 Indicador de estado da ordem "
			// " - aceite "E" - eliminada (EOC) "G" - congelada "O"
			// - cancelada seguido de uma acao no instrumento (por
			// ex- Papel Reservado) "X" - totalmente executada "M" -
			// modificada "D" - disparada "A" - anulada (corretora)
			// "R" - rejeitada pelo Surveillance, seguido de um
			// congelamento. Apos 04/03/2013 devido a migracao para
			// o PUMA alguns ativos estarao valorizados com: 0 -
			// Novo / 1 - Negociada parcialmente / 2 - Totalmente
			// executada / 4 - Cancelada / 5 - Modificada / 8 -
			// Rejeitada / C - Expirada
			// Data Modif. Of.Compra 171 10 Data de Modificacao da
			// Oferta
			// Nr.Of.Compra Modif. 182 10 Numero da Oferta
			// Modificada
			// Hora Fim Tratam. Of.Compra 193 19 Hora de Fim de
			// Tratamento (contem Hora da Anulacao quando Indicador
			// de Estado da Orderm for igual a "A")

			entry.setSymbol(vs[1]);
			entry.setSide(Side.getByValue(vs[2]));
			entry.setOrderID(Long.parseLong(vs[3]));
			entry.setMdEntryID(Long.parseLong(vs[4]));
			entry.setOrderEvent(Integer.parseInt(vs[5]));
			entry.setMdEntryDateTime(tf.parse(vs[6]));
			entry.setMdEntryPx(Double.parseDouble(vs[8]));
			entry.setMdEntrySize(Integer.parseInt(vs[9]));
			entry.setTradeVolume(Integer.parseInt(vs[10]));

		    } else if (vs.length >= 14 && vs.length < 16) {
			// -----------------------------------------------------------
			// Coluna Posicao Inicial Tamanho Descricao
			// -----------------------------------------------------------
			// Data Sessao 1 10 Data da Sessao
			// Simbolo do Instrumento 12 50 Simbolo do Instrumento
			// Sentido Of.Compra 63 1 Indicador de sentido da ordem: "1" - compra / "2" - venda
			// Sequencia 65 15 Numero de Sequencia da Oferta
			// GenerationID - Of.Compra 81 15 Numero de geracao (GenerationID) da Oferta de Compra. Quando um negocio for gerado por 2 ofertas com quantidade escondida e isso gerar "n" linhas será gravado aqui a maior geracao.
			// Cod do Evento da Of.Compra 97 3 Codigo do Evento da Ordem: 
			// 1 - New / 
			// 2 - Update / 
			// 3 - Cancel - Solicitado pelo participante / 
			// 4 - Trade / 
			// 5 - Reentry - Processo interno (quantidade escondida) / 
			// 6 - New Stop Price / 
			// 7 - Reject / 
			// 8 - Remove - Removida pelo Sistema (final de dia ou quando e totalmente fechada)
			// 9 - Stop Price Triggered / 
			// 11 - Expire - Oferta com validade expirada.
			// Hora Prioridade 101 15 Hora de registro da oferta no sistema (no formato, HH:MM:SS.NNN), utilizada como indicadora de prioridade
			// Ind de Prioridade Of.Compra 117 10 Indicador de Prioridade. Alem do preco e a ordem para aparecer no Order Book.
			// Preco Of.Compra 128 20 Preco da Oferta
			// Qtd.Total Of.Compra 149 18 Quantidade Total da Oferta. Se tiver alteracao ela reflete a nova quantidade.
			// Qtd.Negociada Of.Compra 168 18 Quantidade Negociada
			// Data Oferta Compra 187 10 Data de Inclusao da Oferta. Pode ser uma data anterior a Data da Sessao, quando se tratar de uma Oferta com Validade.
			// Data de Entrada Of.Compra 198 19 Data/Hora de Entrada da Oferta (formato: DD/MM/AAAA HH:MM:SS)
			// Estado Of.Compra 218 1 Indicador de estado da ordem:
			// 0 - Novo / 
			// 1 - Negociada parcialmente / 
			// 2 - Totalmente executada / 
			// 4 - Cancelada / 
			// 5 - Modificada / 
			// 8 - Rejeitada / 
			// C - Expirada
			// Condicao Oferta 220 1 Codigo que identifica a condicao da oferta. 
			//Pode ser: 
			// 0 - Oferta Neutra - e aquela que entra no mercado e nao fecha com oferta existente. 
			// 1 - Oferta Agressora - e aquela que ingressa no mercado para fechar com uma oferta existente.
			// 2 - Oferta Agredida - e a oferta (existente) que e fechada com uma oferta agressora.
			
			entry.setSymbol(vs[1]);
			entry.setSide(Side.getByValue(vs[2]));
			entry.setOrderID(Long.parseLong(vs[3]));
			entry.setMdEntryID(Long.parseLong(vs[4]));
			entry.setOrderEvent(Integer.parseInt(vs[5]));
			entry.setMdEntryDateTime(tf.parse(vs[6]));
			entry.setMdEntryPx(Double.parseDouble(vs[8]));
			entry.setMdEntrySize(Integer.parseInt(vs[9]));
			entry.setTradeVolume(Integer.parseInt(vs[10]));
			entry.setOrderDate(dtf.parse(vs[12]));
			entry.setOrderStatus(vs[13].charAt(0));

		    } else if (vs.length >= 16) {
			// -----------------------------------------------------------
			// Coluna Posicao Inicial Tamanho Descricao
			// -----------------------------------------------------------
			// [0]Data Sessao 1 10 Data da Sessao
			// [1]Simbolo do Instrumento 12 50 Simbolo do Instrumento
			// [2]Sentido Of.Compra 63 1 Indicador de sentido da ordem: "1" - compra / "2" - venda
			// [3]Sequencia 65 15 Numero de Sequencia da Oferta
			// [4]GenerationID - Of.Compra 81 15 Numero de geracao (GenerationID) da Oferta de Compra. Quando um negocio for gerado por 2 ofertas com quantidade escondida e isso gerar "n" linhas será gravado aqui a maior geracao.
			// [5]Cod do Evento da Of.Compra 97 3 Codigo do Evento da Ordem: 1 - New / 2 - Update / 3 - Cancel - Solicitado pelo participante / 4 - Trade / 5 - Reentry - Processo interno (quantidade escondida) / 6 - New Stop Price / 7 - Reject / 8 - Remove - Removida pelo Sistema (final de dia ou quando e totalmente fechada) / 9 - Stop Price Triggered / 11 - Expire - Oferta com validade expirada.
			// [6]Hora Prioridade 101 15 Hora de registro da oferta no sistema (no formato, HH:MM:SS.NNN), utilizada como indicadora de prioridade
			// [7]Ind de Prioridade Of.Compra 117 10 Indicador de Prioridade. Alem do preco e a ordem para aparecer no Order Book.
			// [8]Preco Of.Compra 128 20 Preco da Oferta
			// [9]Qtd.Total Of.Compra 149 18 Quantidade Total da Oferta. Se tiver alteracao ela reflete a nova quantidade.
			// [10]Qtd.Negociada Of.Compra 168 18 Quantidade Negociada
			// [11]Data Oferta Compra 187 10 Data de Inclusao da Oferta. Pode ser uma data anterior a Data da Sessao, quando se tratar de uma Oferta com Validade.
			// [12]Data de Entrada Of.Compra 198 19 Data/Hora de Entrada da Oferta (formato: DD/MM/AAAA HH:MM:SS)
			// [13]Estado Of.Compra 218 1 Indicador de estado da ordem: 0 - Novo / 1 - Negociada parcialmente / 2 - Totalmente executada / 4 - Cancelada / 5 - Modificada / 8 - Rejeitada / C - Expirada
			// [14]Condicao Oferta 220 1 Codigo que identifica a condicao da oferta. Pode ser: 0 - Oferta Neutra - e aquela que entra no mercado e nao fecha com oferta existente. / 1 - Oferta Agressora - e aquela que ingressa no mercado para fechar com uma oferta existente. / 2 - Oferta Agredida - e a oferta (existente) que e fechada com uma oferta agressora.
			// [15]Corretora 222 8 Codigo que identifica univocamente a corretora - Disponivel a partir de 03/2014
			
			entry.setSymbol(vs[1]);
			entry.setSide(Side.getByValue(vs[2]));
			entry.setOrderID(Long.parseLong(vs[3]));
			entry.setMdEntryID(Long.parseLong(vs[4]));
			entry.setOrderEvent(Integer.parseInt(vs[5]));
			entry.setMdEntryDateTime(tf.parse(vs[6]));
			entry.setMdEntryPx(Double.parseDouble(vs[8]));
			entry.setMdEntrySize(Integer.parseInt(vs[9]));
			entry.setTradeVolume(Integer.parseInt(vs[10]));
			entry.setOrderDate(dtf.parse(vs[12]));
			entry.setOrderStatus(vs[13].charAt(0));
			entry.setOrderCondition(Integer.parseInt(vs[14]));

		    }

		    bsEvents.add(entry);
		}
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	}

	return bsEvents;
    }


}
