/**
 * 
 */
package com.cmm.jft.data.extractor.marketdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Level;

import com.cmm.jft.core.format.DateTimeFormatter;
import com.cmm.jft.core.format.DoubleFormatter;
import com.cmm.jft.core.format.FormatterFactory;
import com.cmm.jft.core.format.FormatterTypes;
import com.cmm.jft.core.format.IntFormatter;
import com.cmm.jft.data.files.CSV;
import com.cmm.jft.vo.Extractable;
import com.cmm.jft.vo.OrderEventVO;
import com.cmm.jft.vo.TradeVO;
import com.cmm.logging.Logging;

/**
 * <p><code>BovespaTradeFileExtractor.java</code></p>
 * @author Cristiano M Martins
 * @version 06/03/2015 16:09:20
 *
 */
public class BovespaTradeFileExtractor extends BovespaFileExtractor {


    /* (non-Javadoc)
     * @see com.cmm.jft.data.extractor.Extractor#extract()
     */
    @Override
    public List<Extractable> extract() {

	List<Extractable> bsEvents = new ArrayList<>(1000000);
	try {

	    DateTimeFormatter dtmf = (DateTimeFormatter) FormatterFactory.getFormatter(FormatterTypes.DATE_F8);
	    DateTimeFormatter tf = (DateTimeFormatter) FormatterFactory.getFormatter(FormatterTypes.TIME_F4);
	    DoubleFormatter df = (DoubleFormatter) FormatterFactory.getFormatter(FormatterTypes.DOUBLE);
	    IntFormatter intf = (IntFormatter) FormatterFactory.getFormatter(FormatterTypes.INT);

	    CSV csv = new CSV(fileName, ";", "RT", "RH");
	    while (csv.hasNext()) {
		String[] vs = csv.readLine();

		if (vs != null && vs[0] != null) {
		    

		    TradeVO eventVO = new TradeVO();
		    eventVO.sessionDate = dtmf.parse(vs[0]);

		    // layout dos arquivos foi alterado devido a mudanca para o
		    // sistema de negociacao Puma
		    if (vs.length == 12) {// /....!fk
			// ------------------------------------------------------
			// Coluna               Posicao Inicial  Tamanho  Descricao
			// ------------------------------------------------------
			//[0]Data Sessao                      1       10  Data de sessao
			//[1]Papel                           12       50  Codigo do papel
			//[2]Nr.Negocio                      63        7  Numero do negocio  OBS: Para as ofertas do Soma com data entre novembro/1996 e julho/2000 (IBM), foi gerado um sequencial de 10 em 10 por papel. O número do boleto original que identificava o negócio foi jogado no campo QTE_MINI_LOTE_PAD devido ao seu tamanho (9 bytes).
			//[3]Preço Negocio                   71       20  Preço do negócio
			//[4]Quantidade                      92       18  Quantidade negociada
			//[5]Hora                           111       12  Horario da negociacao (no formato HH:MM:SS.NNN)
			//[6]Ind.Anulacao                   124        1  Indicador de anulacao: " " - Negocio "A" - Negocio anulado "X" - Complemento de anulacao. Apos 04/03/2013 devido a migração para o PUMA alguns ativos estarão valorizados com: 1 - "Ativo" / 2 - "Cancelado".
			//[7]Data Oferta Compra             126       10  Data da oferta de compra
			//[8]Seq.Oferta Compra              137       10  Número sequencial da oferta de compra
			//[9]Data Oferta Venda              148       10  Data da oferta de venda
			//[10]Seq.Oferta Venda               159       10  Número sequencial da oferta de venda

			eventVO.securityID = vs[1];
			eventVO.tradeID = String.format("%1$07d", Integer.parseInt(vs[2]));
			eventVO.price = Double.parseDouble(vs[3]);
			eventVO.volume = Integer.parseInt(vs[4]);
			eventVO.tradeTime = tf.parse(vs[5]);
			eventVO.tradeDate = eventVO.sessionDate;
			eventVO.buyOrderID = vs[8];
			eventVO.sellOrderID = vs[10];

		    } else if (vs.length >= 15) {
			//-----------------------------------------------------------
			//Coluna                    Posicao Inicial  Tamanho   Descricao
			//-----------------------------------------------------------
			//[0]Data Sessao                          1       10   Data de sessco
			//[1]Simbolo do Instrumento              12       50   Simbolo do Instrumento
			//[2]Nr.Negocio                          63       10   Numero do negocio
			//[3]Preco Negocio                       74       20   Preco do negocio
			//[4]Quantidade                          95       18   Quantidade negociada
			//[5]Hora                               114       15   Horario da negociacao (formato HH:MM:SS.NNN)
			//[6]Ind.Anulacao                       127        1   Indicador de Anulacao: "1" - ativo / "2" - cancelado
			//[7]Data Oferta Compra                 129       10   Data da oferta de compra
			//[8]Seq.Oferta Compra                  140       15   Numero sequencial da oferta de compra
			//[9]GenerationID - Of.Compra           156       15   Numero de geracao (GenerationID) da Oferta de compra. Quando um neg�cio for gerado por 2 ofertas com quantidade escondida e isso gerar "n" linhas ser� gravado aqui a maior gera��o
			//[10]CondicaoOferta de Compra          172        1   Codigo que identifica a condico da oferta de compra. Pode ser: 0 - Oferta Neutra - � aquela que entra no mercado e n�o fecha com oferta existente. / 1 - Oferta Agressora - � aquela que ingressa no mercado para fechar com uma oferta existente. / 2 - Oferta Agredida - � a oferta (existente) que � fechada com uma oferta agressora.
			//[11]Data Oferta Venda                 174       10   Data da oferta de venda
			//[12]Seq.Oferta Venda                  185       15   Numero sequencial da oferta de venda
			//[13]GenerationID - Of.Venda           201       15   Numero de geraco (GenerationID) da Oferta de venda. Quando um neg�cio for gerado por 2 ofertas com quantidade escondida e isso gerar "n" linhas ser� gravado aqui a maior gera��o
			//[14]Condicao Oferta de Venda          217        1   Codigo que identifica a condico da oferta de venda. Pode ser: 0 - Oferta Neutra - � aquela que entra no mercado e n�o fecha com oferta existente. / 1 - Oferta Agressora - � aquela que ingressa no mercado para fechar com uma oferta existente. / 2 - Oferta Agredida - � a oferta (existente) que � fechada com uma oferta agressora.
			//[15]Indicador de direto               219        1   Codigo que identifica se o negocio direto foi intencional: 1 - Intencional / 0 - N�o Intencional
			//[16]Corretora Compra                  221        8   Codigo de identificaco da corretora de compra - Dispon�vel a partir de 03/2014
			//[17]Corretora Venda                   230        8   Codigo de identificaco da corretora de venda - Dispon�vel a partir de 03/2014

			eventVO.securityID = vs[1];
			eventVO.tradeID = String.format("%1$07d", Integer.parseInt(vs[2]));
			eventVO.price = Double.parseDouble(vs[3]);
			eventVO.volume = Integer.parseInt(vs[4]);
			eventVO.tradeTime = tf.parse(vs[5]);
			eventVO.tradeDate = eventVO.sessionDate;
			eventVO.buyOrderID = vs[8];
			eventVO.buySecOrderID = vs[9];
			if(vs[10].equals("1")) {
			    eventVO.agressor = 'B';
			}
			else if(vs[14].equals("1")){
			    eventVO.agressor = 'S';
			}
			
			eventVO.sellOrderID = vs[12];
			eventVO.sellSecOrderID = vs[13];
			
			eventVO.buyBroker = String.format("%1$04d", Integer.parseInt(vs[16]));
			eventVO.sellBroker = String.format("%1$04d", Integer.parseInt(vs[17]));
		    }

		    bsEvents.add(eventVO);

		}
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}

	return bsEvents;
    }

}
