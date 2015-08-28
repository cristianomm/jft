/**
 * 
 */
package com.cmm.jft.data.extractor.marketdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Level;

import com.cmm.jft.core.format.BigDecimalFormatter;
import com.cmm.jft.core.format.DateTimeFormatter;
import com.cmm.jft.core.format.DoubleFormatter;
import com.cmm.jft.core.format.FormatterFactory;
import com.cmm.jft.core.format.FormatterTypes;
import com.cmm.jft.core.format.IntFormatter;
import com.cmm.jft.data.extractor.Extractable;
import com.cmm.jft.data.files.CSV;
import com.cmm.jft.data.vo.OrderEventVO;
import com.cmm.jft.db.DBFacade;
import com.cmm.jft.db.DBObject;
import com.cmm.logging.Logging;

/**
 * <p><code>BovespaBuyFileExtractor.java</code></p>
 * @author Cristiano M Martins
 * @version 06/03/2015 16:17:16
 *
 */
public class BovespaBuyFileExtractor extends BovespaFileExtractor {

	/* (non-Javadoc)
	 * @see com.cmm.jft.data.extractor.Extractor#extract()
	 */
	@Override
	public List<Extractable> extract() {
				
		try {
			StringBuffer sBuffer = new StringBuffer(100000);
			FileOutputStream fos = new FileOutputStream(new File(fileName + ".re"));
			DateTimeFormatter dtf = (DateTimeFormatter) FormatterFactory.getFormatter(FormatterTypes.DATE_F8);
			DoubleFormatter df = (DoubleFormatter) FormatterFactory.getFormatter(FormatterTypes.DOUBLE);

			CSV csv = new CSV(fileName, ";", "RT", "RH");
			while (csv.hasNext()) {
				String[] vs = csv.readLine();

				if (vs != null && vs[0] != null) {
					Date sessionDate = dtf.parse(vs[0]);
					String price = "";
					String externalID = "";
					String securityID = "";
					String volume = "";
					String orderDate = "";
					String orderTime = "";
					String orderStatus = "";
					String eventCode = "";
					String tradedVolume = "";
					String orderCondition = "";
					String broker = "";
					
					OrderEventVO eventVO = new OrderEventVO();

					// layout dos arquivos foi alterado devido a mudanca para o
					// sistema de negociacao Puma
					if (vs.length == 12) {// /....!fk
						// -----------------------------------------------------------
						// Coluna Posicao Inicial Tamanho Descricao
						// -----------------------------------------------------------
						// Data Sess�o 1 10 Data da Sess�o
						// Papel 50 12 C�digo do Instrumento
						// Sequ�ncia 63 10 N�mero de Sequ�ncia da Oferta
						// Pre�o Of.Compra 74 20 Pre�o da Oferta
						// Qtd.Total Of.Compra 95 18 Quantidade Total
						// Qtd.Negociada Of.Compra 114 18 Quantidade Negociada
						// Hora Prioridade 133 15 Hora de registro da oferta no
						// sistema (com a precis�o Tandem, no formato,
						// HH:MM:SS.NNNNNN), utilizada como indicadora de
						// prioridade
						// Data de Entrada Of.Compra 149 19 Data/Hora de Entrada
						// da Oferta
						// Estado Of.Compra 169 1 Indicador de estado da ordem
						// " " - aceite "E" - eliminada (EOC) "G" - congelada
						// "O" - cancelada seguido de uma a��o no instrumento
						// (por ex- Papel Reservado) "X" - totalmente executada
						// "M" - modificada "D" - disparada "A" - anulada
						// (corretora) "R" - rejeitada pelo Surveillance,
						// seguido de um congelamento. Ap�s 04/03/2013 devido a
						// migra��o para o PUMA alguns ativos estar�o
						// valorizados com: 0 - Novo / 1 - Negociada
						// parcialmente / 2 - Totalmente executada / 4 -
						// Cancelada / 5 - Modificada / 8 - Rejeitada / C -
						// Expirada
						// Data Modif. Of.Compra 171 10 Data de Modifica��o da
						// Oferta
						// Nr.Of.Compra Modif. 182 10 N�mero da Oferta
						// Modificada
						// Hora Fim Tratam. Of.Compra 193 19 Hora de Fim de
						// Tratamento (cont�m Hora da Anula��o quando Indicador
						// de Estado da Orderm for igual a "A")
						
						eventVO.securityID = vs[1];
						eventVO.externalID = vs[2];
						eventVO.price = df.parse(vs[3]);
						eventVO.volume = df.parse(vs[4]);
						eventVO.tradedVolume = vs[5];
						eventVO.orderTime = vs[6];
						eventVO.orderDate = vs[7].split(" ")[0];
						eventVO.orderStatus = vs[8];

					} else if (vs.length >= 14) {
						// -----------------------------------------------------------
						// Coluna Posi��o Inicial Tamanho Descri��o
						// -----------------------------------------------------------
						// Data Sess�o 1 10 Data da Sess�o
						// S�mbolo do Instrumento 12 50 S�mbolo do Instrumento
						// Sentido Of.Compra 63 1 Indicador de sentido da ordem:
						// "1" - compra / "2" - venda
						// Sequ�ncia 65 10 N�mero de Sequ�ncia da Oferta
						// GenerationID - Of.Compra 76 10 N�mero de gera��o
						// (GenerationID) da Oferta de Compra. Quando um neg�cio
						// for gerado por 2 ofertas com quantidade escondida e
						// isso gerar "n" linhas ser� gravado aqui a maior
						// gera��o.
						// C�d do Evento da Of.Compra 87 3 C�digo do Evento da
						// Ordem: 1 - New / 2 - Update / 3 - Cancel - Solicitado
						// pelo participante / 4 - Trade / 5 - Reentry -
						// Processo interno (quantidade escondida) / 6 - New
						// Stop Price / 7 - Reject / 8 - Remove - Removida pelo
						// Sistema (final de dia ou quando � totalmente fechada)
						// / 9 - Stop Price Triggered / 11 - Expire - Oferta com
						// validade expirada.
						// Hora Prioridade 91 15 Hora de registro da oferta no
						// sistema (no formato, HH:MM:SS.NNN), utilizada como
						// indicadora de prioridade
						// Ind de Prioridade Of.Compra 107 10 Indicador de
						// Prioridade. Al�m do pre�o � a ordem para aparecer no
						// Order Book.
						// Pre�o Of.Compra 118 20 Pre�o da Oferta
						// Qtd.Total Of.Compra 139 18 Quantidade Total da
						// Oferta. Se tiver altera��o ela reflete a nova
						// quantidade.
						// Qtd.Negociada Of.Compra 158 18 Quantidade Negociada
						// Data Oferta Compra 177 10 Data de Inclus�o da Oferta.
						// Pode ser uma data anterior � Data da Sess�o, quando
						// se tratar de uma Oferta com Validade.
						// Data de Entrada Of.Compra 188 19 Data/Hora de Entrada
						// da Oferta (formato: DD/MM/AAAA HH:MM:SS)
						// Estado Of.Compra 208 1 Indicador de estado da ordem:
						// 0 - Novo / 1 - Negociada parcialmente / 2 -
						// Totalmente executada / 4 - Cancelada / 5 - Modificada
						// / 8 - Rejeitada / C - Expirada
						// Condi��o Oferta 210 1 C�digo que identifica a
						// condi��o da oferta. Pode ser: 0 - Oferta Neutra - �
						// aquela que entra no mercado e n�o fecha com oferta
						// existente. / 1 - Oferta Agressora - � aquela que
						// ingressa no mercado para fechar com uma oferta
						// existente. / 2 - Oferta Agredida - � a oferta
						// (existente) que � fechada com uma oferta agressora.
						eventVO.securityID = vs[1];
						eventVO.externalID = vs[3];
						eventVO.orderEvent = vs[5];
						eventVO.orderTime = vs[6];
						eventVO.price = df.parse(vs[8]);
						eventVO.volume = df.parse(vs[9]);
						eventVO.tradedVolume = vs[10];
						eventVO.orderDate = vs[11];
						eventVO.orderStatus = vs[13];
						
					} else if (vs.length >= 16) {
						// -----------------------------------------------------------
						// Coluna Posi��o Inicial Tamanho Descri��o
						// -----------------------------------------------------------
//						Data Sessão                          1       10   Data da Sessão
//						Símbolo do Instrumento              12       50   Símbolo do Instrumento
//						Sentido Of.Compra                   63        1   Indicador de sentido da ordem: "1" - compra / "2" - venda
//						Sequência                           65       15   Número de Sequência da Oferta
//						GenerationID - Of.Compra            81       15   Número de geração (GenerationID) da Oferta de Compra. Quando um negócio for gerado por 2 ofertas com quantidade escondida e isso gerar "n" linhas será gravado aqui a maior geração.
//						Cód do Evento da Of.Compra          97        3   Código do Evento da Ordem: 1 - New / 2 - Update / 3 - Cancel - Solicitado pelo participante / 4 - Trade / 5 - Reentry - Processo interno (quantidade escondida) / 6 - New Stop Price / 7 - Reject / 8 - Remove - Removida pelo Sistema (final de dia ou quando é totalmente fechada) / 9 - Stop Price Triggered / 11 - Expire - Oferta com validade expirada.
//						Hora Prioridade                    101       15   Hora de registro da oferta no sistema (no formato, HH:MM:SS.NNN), utilizada como indicadora de prioridade
//						Ind de Prioridade Of.Compra        117       10   Indicador de Prioridade. Além do preço é a ordem para aparecer no Order Book.
//						Preço Of.Compra                    128       20   Preço da Oferta
//						Qtd.Total Of.Compra                149       18   Quantidade Total da Oferta. Se tiver alteração ela reflete a nova quantidade.
//						Qtd.Negociada Of.Compra            168       18   Quantidade Negociada
//						Data Oferta Compra                 187       10   Data de Inclusão da Oferta. Pode ser uma data anterior à Data da Sessão, quando se tratar de uma Oferta com Validade.
//						Data de Entrada Of.Compra          198       19   Data/Hora de Entrada da Oferta (formato: DD/MM/AAAA HH:MM:SS)
//						Estado Of.Compra                   218        1   Indicador de estado da ordem: 0 - Novo / 1 - Negociada parcialmente / 2 - Totalmente executada / 4 - Cancelada / 5 - Modificada / 8  - Rejeitada / C - Expirada
//						Condição Oferta                    220        1   Código que identifica a condição da oferta. Pode ser: 0 - Oferta Neutra - é aquela que entra no mercado e não fecha com oferta existente. / 1 - Oferta Agressora - é aquela que ingressa no mercado para fechar com uma oferta existente. / 2 - Oferta Agredida - é a oferta (existente) que é fechada com uma oferta agressora.
//						Corretora                          222        8   Código que identifica univocamente a corretora - Disponível a partir de 03/2014
						
						eventVO.securityID = vs[1];
						eventVO.externalID = vs[3];
						eventVO.orderEvent = vs[5];
						eventVO.orderTime = vs[6];
						eventVO.price = df.parse(vs[8]);
						eventVO.volume = df.parse(vs[9]);
						eventVO.tradedVolume = vs[10];
						eventVO.orderDate = vs[11];
						eventVO.orderStatus = vs[13];
						eventVO.orderCondition = vs[14];
						eventVO.broker = vs[15];
						
					}

					if (sBuffer.capacity() <= 100000) {
						
						
						
						
						sBuffer.append(dtf.format(sessionDate));
						sBuffer.append(";");
						sBuffer.append(securityID);
						sBuffer.append(";");
						sBuffer.append(externalID);
						sBuffer.append(";");
						sBuffer.append(eventCode);
						sBuffer.append(";");
						sBuffer.append(orderDate);
						sBuffer.append(";");
						sBuffer.append(orderTime);
						sBuffer.append(";");
						sBuffer.append(orderStatus);
						sBuffer.append(";");
						sBuffer.append(price);
						sBuffer.append(";");
						sBuffer.append(volume);
						sBuffer.append(";");
						sBuffer.append(tradedVolume);
						sBuffer.append(";");
						sBuffer.append(orderCondition);
						sBuffer.append(";");
						sBuffer.append(broker);
						sBuffer.append(";\n");
						
					} else {
						fos.write(sBuffer.toString().getBytes());
						sBuffer = new StringBuffer(100000);
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

}
