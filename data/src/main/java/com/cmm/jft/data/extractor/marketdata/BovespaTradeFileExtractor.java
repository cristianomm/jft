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
import java.util.Properties;

import org.apache.log4j.Level;

import com.cmm.jft.core.format.DateTimeFormatter;
import com.cmm.jft.core.format.DoubleFormatter;
import com.cmm.jft.core.format.FormatterFactory;
import com.cmm.jft.core.format.FormatterTypes;
import com.cmm.jft.core.format.IntFormatter;
import com.cmm.jft.core.vo.Extractable;
import com.cmm.jft.core.vo.OrderEventVO;
import com.cmm.jft.core.vo.TradeVO;
import com.cmm.jft.data.files.CSV;
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
		
		try {
			StringBuffer sBuffer = new StringBuffer(100000);
			FileOutputStream fos = new FileOutputStream(new File(fileName + ".re"));
			
			DateTimeFormatter dtf = (DateTimeFormatter) FormatterFactory.getFormatter(FormatterTypes.DATE_TIME_F8);
			DateTimeFormatter tf = (DateTimeFormatter) FormatterFactory.getFormatter(FormatterTypes.TIME_F3);
			DoubleFormatter df = (DoubleFormatter) FormatterFactory.getFormatter(FormatterTypes.DOUBLE);
			IntFormatter intf = (IntFormatter) FormatterFactory.getFormatter(FormatterTypes.INT);
			
			CSV csv = new CSV(fileName, ";", "RT", "RH");
			while (csv.hasNext()) {
				String[] vs = csv.readLine();

				if (vs != null && vs[0] != null) {
					Date sessionDate = dtf.parse(vs[0]);
					String securityID = "";
					String price = "";
					String volume = "";
					String tradeDate = "";
					String tradeTime = "";
					String buySeqID = "";
					String sellSeqID = "";
					

					// layout dos arquivos foi alterado devido a mudanca para o
					// sistema de negociacao Puma
					if (vs.length == 12) {// /....!fk
					// ------------------------------------------------------
					// Coluna Posi��o Inicial Tamanho Descri��o
					// ------------------------------------------------------
					// Data Sess�o 1 10 Data de sess�o
					// Papel 12 50 C�digo do papel
					// Nr.Neg�cio 63 7 N�mero do neg�cio OBS: Para as ofertas do
					// Soma com data entre novembro/1996 e julho/2000 (IBM), foi
					// gerado um sequencial de 10 em 10 por papel. O n�mero do
					// boleto original que identificava o neg�cio foi jogado no
					// campo QTE_MINI_LOTE_PAD devido ao seu tamanho (9 bytes).
					// Pre�o Neg�cio 71 20 Pre�o do neg�cio
					// Quantidade 92 18 Quantidade negociada
					// Hora 111 12 Hor�rio da negocia��o (no formato
					// HH:MM:SS.NNN)
					// Ind.Anula��o 124 1 Indicador de anula��o: " " - Neg�cio
					// "A" - Neg�cio anulado "X" - Complemento de anula��o. Ap�s
					// 04/03/2013 devido a migra��o para o PUMA alguns ativos
					// estar�o valorizados com: 1 - "Ativo" / 2 - "Cancelado".
					// Data Oferta Compra 126 10 Data da oferta de compra
					// Seq.Oferta Compra 137 10 N�mero sequencial da oferta de
					// compra
					// Data Oferta Venda 148 10 Data da oferta de venda
					// Seq.Oferta Venda 159 10 N�mero sequencial da oferta de
					// venda

						securityID = vs[1];
						price = vs[3];
						volume = vs[4];
						tradeTime = vs[5];
						tradeDate = vs[0];
						buySeqID = vs[8];
						sellSeqID = vs[10];

					} else if (vs.length >= 15) {
//						-----------------------------------------------------------
//						Coluna                 Posi��o Inicial  Tamanho   Descri��o
//						-----------------------------------------------------------
//						[0]Data Sess�o                          1       10   Data de sess�o
//						[1]S�mbolo do Instrumento              12       50   S�mbolo do Instrumento
//						[2]Nr.Neg�cio                          63       10   N�mero do neg�cio
//						[3]Pre�o Neg�cio                       74       20   Pre�o do neg�cio
//						[4]Quantidade                          95       18   Quantidade negociada
//						[5]Hora                               114       15   Hor�rio da negocia��o (formato HH:MM:SS.NNN)
//						[6]Ind.Anula��o                       127        1   Indicador de Anula��o: "1" - ativo / "2" - cancelado
//						[7]Data Oferta Compra                 129       10   Data da oferta de compra
//						[8]Seq.Oferta Compra                  140       15   N�mero sequencial da oferta de compra
//						[9]GenerationID - Of.Compra           156       15   N�mero de gera��o (GenerationID) da Oferta de compra. Quando um neg�cio for gerado por 2 ofertas com quantidade escondida e isso gerar "n" linhas ser� gravado aqui a maior gera��o
//						[10]Condi��o Oferta de Compra          172        1   C�digo que identifica a condi��o da oferta de compra. Pode ser: 0 - Oferta Neutra - � aquela que entra no mercado e n�o fecha com oferta existente. / 1 - Oferta Agressora - � aquela que ingressa no mercado para fechar com uma oferta existente. / 2 - Oferta Agredida - � a oferta (existente) que � fechada com uma oferta agressora.
//						[11]Data Oferta Venda                  174       10   Data da oferta de venda
//						[12]Seq.Oferta Venda                   185       15   N�mero sequencial da oferta de venda
//						[13]GenerationID - Of.Venda            201       15   N�mero de gera��o (GenerationID) da Oferta de venda. Quando um neg�cio for gerado por 2 ofertas com quantidade escondida e isso gerar "n" linhas ser� gravado aqui a maior gera��o
//						[14]Condi��o Oferta de Venda           217        1   C�digo que identifica a condi��o da oferta de venda. Pode ser: 0 - Oferta Neutra - � aquela que entra no mercado e n�o fecha com oferta existente. / 1 - Oferta Agressora - � aquela que ingressa no mercado para fechar com uma oferta existente. / 2 - Oferta Agredida - � a oferta (existente) que � fechada com uma oferta agressora.
//						[15]Indicador de direto                219        1   C�digo que identifica se o neg�cio direto foi intencional: 1 - Intencional / 0 - N�o Intencional
//						[16]Corretora Compra                   221        8   C�digo de identifica��o da corretora de compra - Dispon�vel a partir de 03/2014
//						[17]Corretora Venda                    230        8   C�digo de identifica��o da corretora de venda - Dispon�vel a partir de 03/2014
						
						tradeDate = vs[0];
						securityID = vs[1];
						//tradeNum = vs[2];
						price = vs[3];
						volume = vs[4];
						tradeTime = vs[5];
						
						buySeqID = vs[8];
						sellSeqID = vs[12];
						
					}
					
				}
			}

			fos.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	public List<Extractable> extracts() {
		
		List<Extractable> bsEvents = new ArrayList<>(1000000);
		try {
			DateTimeFormatter dtf = (DateTimeFormatter) FormatterFactory.getFormatter(FormatterTypes.DATE_TIME_F8);
			DateTimeFormatter tf = (DateTimeFormatter) FormatterFactory.getFormatter(FormatterTypes.TIME_F3);
			DoubleFormatter df = (DoubleFormatter) FormatterFactory.getFormatter(FormatterTypes.DOUBLE);
			IntFormatter intf = (IntFormatter) FormatterFactory.getFormatter(FormatterTypes.INT);
			
			CSV csv = new CSV(fileName, ";", "RT", "RH");
			while (csv.hasNext()) {
				String[] vs = csv.readLine();

				if (vs != null && vs[0] != null) {
					
					TradeVO tradeVO = new TradeVO();
										
					// layout dos arquivos foi alterado devido a mudanca para o
					// sistema de negociacao Puma
					if (vs.length == 12) {// /....!fk
//						-----------------------------------------------------------
//						Coluna                 Posicao Inicial  Tamanho   Descricao
//						-----------------------------------------------------------
//						Data Sessao                          1       10   Data da Sessao
//						Papel                               50       12   Codigo do Instrumento
//						Sequencia                           63       10   Numero de Sequencia da Oferta
//						Preco Of.Compra                     74       20   Preco da Oferta
//						Qtd.Total Of.Compra                 95       18   Quantidade Total
//						Qtd.Negociada Of.Compra            114       18   Quantidade Negociada
//						Hora Prioridade                    133       15   Hora de registro da oferta no sistema (com a precisao Tandem, no formato, HH:MM:SS.NNNNNN), utilizada como indicadora de prioridade
//						Data de Entrada Of.Compra          149       19   Data/Hora de Entrada da Oferta
//						Estado Of.Compra                   169        1   Indicador de estado da ordem " " - aceite "E" - eliminada (EOC) "G" - congelada "O" - cancelada seguido de uma acao no instrumento (por ex- Papel Reservado) "X" - totalmente executada "M" - modificada "D" - disparada "A" - anulada (corretora) "R" - rejeitada pelo Surveillance, seguido de um congelamento. Apos 04/03/2013 devido a migracao para o PUMA alguns ativos estarao valorizados com: 0 - Novo / 1 - Negociada parcialmente / 2 - Totalmente executada / 4 - Cancelada / 5 - Modificada / 8  - Rejeitada / C - Expirada
//						Data Modif. Of.Compra              171       10   Data de Modificacao da Oferta
//						Nr.Of.Compra Modif.                182       10   Numero da Oferta Modificada
//						Hora Fim Tratam. Of.Compra         193       19   Hora de Fim de Tratamento (contem Hora da Anulacao quando Indicador de Estado da Orderm for igual a "A")
						
//						eventVO.orderID = vs[2];
//						eventVO.price = df.parse(vs[3]);
//						eventVO.volume = df.parse(vs[4]);
//						eventVO.tradedVolume = df.parse(vs[5]);
//						eventVO.orderTime = tf.parse(vs[6]);
//						eventVO.orderDate = dtf.parse(vs[7]);
//						eventVO.orderStatus = vs[8];

					} else if (vs.length >= 16) {
//						-----------------------------------------------------------
//						Coluna                 Posicao Inicial  Tamanho   Descricao
//						-----------------------------------------------------------
//						Data Sessao                          1       10   Data de sessao
//						Simbolo do Instrumento              12       50   Simbolo do Instrumento
//						Nr.Negocio                          63       10   Numero do negocio
//						Preco Negocio                       74       20   Preço do negocio
//						Quantidade                          95       18   Quantidade negociada
//						Hora                               114       15   Horario da negociacao (formato HH:MM:SS.NNN)
//						Ind.Anulacao                       127        1   Indicador de Anulacao: "1" - ativo / "2" - cancelado
//						Data Oferta Compra                 129       10   Data da oferta de compra
//						8Seq.Oferta Compra                  140       15   Numero sequencial da oferta de compra
//						GenerationID - Of.Compra           156       15   Numero de geração (GenerationID) da Oferta de compra. Quando um negocio for gerado por 2 ofertas com quantidade escondida e isso gerar "n" linhas sera gravado aqui a maior geracao
//						Condicao Oferta de Compra          172        1   Codigo que identifica a condicao da oferta de compra. Pode ser: 0 - Oferta Neutra - e aquela que entra no mercado e nao fecha com oferta existente. / 1 - Oferta Agressora - e aquela que ingressa no mercado para fechar com uma oferta existente. / 2 - Oferta Agredida - e a oferta (existente) que e fechada com uma oferta agressora.
//						Data Oferta Venda                  174       10   Data da oferta de venda
//						Seq.Oferta Venda                   185       15   Numero sequencial da oferta de venda
//						GenerationID - Of.Venda            201       15   Numero de geração (GenerationID) da Oferta de venda. Quando um negócio for gerado por 2 ofertas com quantidade escondida e isso gerar "n" linhas sera gravado aqui a maior geracao
//						Condicao Oferta de Venda           217        1   Codigo que identifica a condicao da oferta de venda. Pode ser: 0 - Oferta Neutra - e aquela que entra no mercado e não fecha com oferta existente. / 1 - Oferta Agressora - e aquela que ingressa no mercado para fechar com uma oferta existente. / 2 - Oferta Agredida - e a oferta (existente) que e fechada com uma oferta agressora.
//						Indicador de direto                219        1   Codigo que identifica se o negocio direto foi intencional: 1 - Intencional / 0 - Nao Intencional
//						Corretora Compra                   221        8   Codigo de identificação da corretora de compra - Disponivel a partir de 03/2014
//						Corretora Venda                    230        8   Codigo de identificação da corretora de venda - Disponivel a partir de 03/2014

						tradeVO.tradeDate = dtf.parse(vs[0]);
						tradeVO.price = df.parse(vs[3]);
						tradeVO.volume = df.parse(vs[4]);
						tradeVO.tradeTime = tf.parse(vs[5]);
						
						tradeVO.buyBroker = intf.parse(vs[16]);
						tradeVO.buyOrderID = vs[8];
						tradeVO.buySecOrderID = vs[9];
						
						tradeVO.buyBroker = intf.parse(vs[17]);
						tradeVO.buyOrderID = vs[12];
						tradeVO.buySecOrderID = vs[13];
						
					}
					
					bsEvents.add(tradeVO);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}
		
		return bsEvents;
	}
	

}
