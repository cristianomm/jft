/**
 * 
 */
package com.cmm.jft.data.extractor.marketdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.cmm.jft.core.format.DateTimeFormatter;
import com.cmm.jft.core.format.FormatterFactory;
import com.cmm.jft.core.format.FormatterTypes;
import com.cmm.jft.core.vo.Extractable;
import com.cmm.jft.data.files.CSV;

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
			FileOutputStream fos = new FileOutputStream(new File(fileName
					+ ".re"));
			DateTimeFormatter dtf = (DateTimeFormatter) FormatterFactory
					.getFormatter(FormatterTypes.DATE_F8);

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

					if (sBuffer.capacity() <= 100000) {
						/*
						String line = dtf.format(sessionDate) + ";" + tradeTime
								+ ";" + securityID + ";" + price + ";" + volume
								+ ";" + buySeqID + ";" + sellSeqID + "\n";
						*/
						sBuffer.append(";");
						sBuffer.append(dtf.format(sessionDate));
						sBuffer.append(";");
						sBuffer.append(tradeTime);
						sBuffer.append(";");
						sBuffer.append(securityID);
						sBuffer.append(";");
						sBuffer.append(price);
						sBuffer.append(";");
						sBuffer.append(volume);
						sBuffer.append(";");
						sBuffer.append(buySeqID);
						sBuffer.append(";");
						sBuffer.append(sellSeqID);
						sBuffer.append("\n");
						
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
