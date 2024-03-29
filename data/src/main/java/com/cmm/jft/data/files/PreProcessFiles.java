/**
 * 
 */
package com.cmm.jft.data.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipException;

import com.cmm.jft.core.format.FormatterFactory;
import com.cmm.jft.core.format.FormatterTypes;
import com.cmm.jft.core.util.InputRowReport;

/**
 * <p>
 * <code>PreProcessFiles.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 12/09/2013 02:26:26
 *
 */
public class PreProcessFiles {

	private class Line {
		String code;
		LocalDateTime date;		
		
		String securityID = "";
		double price;
		int volume;
		String sessionDate = "";
		String tradeDate = "";
		String tradeTime = "";
		int tradeNum;
		int tradedVolume;
		long buySeqID;
		long sellSeqID;
		char isDirect;
		
		int eventCode;
		long externalID;
		String orderTime = "";
		String orderDate = "";
		String orderStatus="";
	}

	public static void main(String[] args) {
		/*
		new PreProcessFiles().preProcessTickDataFiles(
				"C:\\Users\\Cristiano M Martins\\Data\\B3",
				"C:\\Users\\Cristiano M Martins\\Data\\B3\\Consolidated");
		*/
		new PreProcessFiles().preProcessBovespaHistoricalFiles("C:\\Users\\Cristiano M Martins\\Downloads\\MarketData\\Bovespa-Historico\\Consolidated");
				
	}

	/**
	 * Preprocessa os arquivos TickData, criando um unico arquivo para cada layout
	 * existente: Ofertas de Compra, Ofertas de Venda e Negocios. Sera criado um
	 * arquivo para cada mes
	 * 
	 * @param dir Diretorio onde estao os arquivos.
	 */
	public void preProcessTickDataFiles(String dir, String destDir) {

		if (!dir.endsWith("\\"))
			dir += "\\";
		if (!destDir.endsWith("\\"))
			destDir += "\\";

		new File(destDir).mkdirs();

		// somente arquivos .zip
		for (File file : new File(dir).listFiles()) {
			if (file.isFile() && file.getName().endsWith(".zip")) {
				// extrai os arquivos contidos no zip e consolida em um unico
				// arquivo
				String fName = extractAndConsolidate(file.getAbsolutePath(), destDir);

				// caso seja arquivo de negociacao
				if (fName.contains("NEG")) {
					reLayoutTradeTickData(fName);
				} else {
					reLayoutBuySellTickData(fName);
				}
				
				new File(fName).delete();
			}
		}

	}

	private String extractAndConsolidate(String zipFile, String destDir) {

		String fName = "";
		try {
			int len = 1000000;
			StringBuffer buffer = new StringBuffer(len);
			List<String> files = Zimp.extract(zipFile, "");
			fName = destDir;
			fName += zipFile.substring(zipFile.lastIndexOf('\\')+1, zipFile.lastIndexOf('.')) + ".csv";
			FileOutputStream fos = new FileOutputStream(new File(fName));

			for (String fn : files) {

				Scanner sc = new Scanner(new File(fn));
				sc.useDelimiter("\n");
				while (sc.hasNext()) {
					String line = sc.next();
					if (!(line.startsWith("RH") || line.startsWith("RT"))) {
						buffer.append(line + "\n");

						if (buffer.length() >= len) {
							fos.write(buffer.toString().getBytes());
							buffer = null;
							buffer = new StringBuffer(len);
						}
					}
				}
				sc.close();
				fos.write(buffer.toString().getBytes());
				buffer = null;
				buffer = new StringBuffer(len);

				// apaga o arquivo extraido do zip
				new File(fn).delete();
			}
			// apaga o diretorio
			new File(zipFile.substring(0, zipFile.lastIndexOf('.'))).delete();

			fos.close();
			System.gc();

		} catch (ZipException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fName;
	}

	private void reLayoutTradeTickData(String fileName) {

		try {
			StringBuffer sBuffer = new StringBuffer(100000);
			FileOutputStream fos = new FileOutputStream(new File(fileName + ".re"));
			SimpleDateFormat sdf = new SimpleDateFormat(FormatterTypes.DATE_F8.getFormat());
			
			CSV csv = new CSV(fileName, ";", "RT", "RH");
			while (csv.hasNext()) {
				String[] vs = csv.readLine();

				if (vs != null && vs[0] != null) {
					LocalDate sessionDate = sdf.parse(vs[0]).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					
					Line line = new Line();

					// layout dos arquivos foi alterado devido a mudanca para o
					// sistema de negociacao Puma
					if (vs.length == 12) {// /....!fk
						// ------------------------------------------------------
						// Coluna Posi��o Inicial Tamanho Descri��o
						// ------------------------------------------------------
						// Data Sess�o 1 10 Data de sess�o
						// Papel 12 50 C�digo do papel
						// Nr.Neg�cio 63 7 N�mero do neg�cio OBS: Para as
						// ofertas do
						// Soma com data entre novembro/1996 e julho/2000 (IBM),
						// foi
						// gerado um sequencial de 10 em 10 por papel. O n�mero
						// do
						// boleto original que identificava o neg�cio foi jogado
						// no
						// campo QTE_MINI_LOTE_PAD devido ao seu tamanho (9
						// bytes).
						// Pre�o Neg�cio 71 20 Pre�o do neg�cio
						// Quantidade 92 18 Quantidade negociada
						// Hora 111 12 Hor�rio da negocia��o (no formato
						// HH:MM:SS.NNN)
						// Ind.Anula��o 124 1 Indicador de anula��o: " " -
						// Neg�cio
						// "A" - Neg�cio anulado "X" - Complemento de anula��o.
						// Ap�s
						// 04/03/2013 devido a migra��o para o PUMA alguns
						// ativos
						// estar�o valorizados com: 1 - "Ativo" / 2 -
						// "Cancelado".
						// Data Oferta Compra 126 10 Data da oferta de compra
						// Seq.Oferta Compra 137 10 N�mero sequencial da oferta
						// de
						// compra
						// Data Oferta Venda 148 10 Data da oferta de venda
						// Seq.Oferta Venda 159 10 N�mero sequencial da oferta
						// de
						// venda
						line.securityID = vs[1];
						line.tradeNum = Integer.parseInt(vs[2]);
						line.price = Double.parseDouble(vs[3]);
						line.volume = Integer.parseInt(vs[4]);
						line.tradeTime = vs[5];
						line.tradeDate = vs[0];
						line.buySeqID = Long.parseLong(vs[8]);
						line.sellSeqID = Long.parseLong(vs[10]);

					} else if (vs.length >= 15) {
						// -----------------------------------------------------------
						// Coluna Posi��o Inicial Tamanho Descri��o
						// -----------------------------------------------------------
						// [0]Data Sess�o 1 10 Data de sess�o
						// [1]S�mbolo do Instrumento 12 50 S�mbolo do
						// Instrumento
						// [2]Nr.Neg�cio 63 10 N�mero do neg�cio
						// [3]Pre�o Neg�cio 74 20 Pre�o do neg�cio
						// [4]Quantidade 95 18 Quantidade negociada
						// [5]Hora 114 15 Hor�rio da negocia��o (formato
						// HH:MM:SS.NNN)
						// [6]Ind.Anula��o 127 1 Indicador de Anula��o: "1" -
						// ativo / "2" - cancelado
						// [7]Data Oferta Compra 129 10 Data da oferta de compra
						// [8]Seq.Oferta Compra 140 15 N�mero sequencial da
						// oferta de compra
						// [9]GenerationID - Of.Compra 156 15 N�mero de gera��o
						// (GenerationID) da Oferta de compra. Quando um neg�cio
						// for gerado por 2 ofertas com quantidade escondida e
						// isso gerar "n" linhas ser� gravado aqui a maior
						// gera��o
						// [10]Condi��o Oferta de Compra 172 1 C�digo que
						// identifica a condi��o da oferta de compra. Pode ser:
						// 0 - Oferta Neutra - � aquela que entra no mercado e
						// n�o fecha com oferta existente. / 1 - Oferta
						// Agressora - � aquela que ingressa no mercado para
						// fechar com uma oferta existente. / 2 - Oferta
						// Agredida - � a oferta (existente) que � fechada com
						// uma oferta agressora.
						// [11]Data Oferta Venda 174 10 Data da oferta de venda
						// [12]Seq.Oferta Venda 185 15 N�mero sequencial da
						// oferta de venda
						// [13]GenerationID - Of.Venda 201 15 N�mero de gera��o
						// (GenerationID) da Oferta de venda. Quando um neg�cio
						// for gerado por 2 ofertas com quantidade escondida e
						// isso gerar "n" linhas ser� gravado aqui a maior
						// gera��o
						// [14]Condi��o Oferta de Venda 217 1 C�digo que
						// identifica a condi��o da oferta de venda. Pode ser: 0
						// - Oferta Neutra - � aquela que entra no mercado e n�o
						// fecha com oferta existente. / 1 - Oferta Agressora -
						// � aquela que ingressa no mercado para fechar com uma
						// oferta existente. / 2 - Oferta Agredida - � a oferta
						// (existente) que � fechada com uma oferta agressora.
						// [15]Indicador de direto 219 1 C�digo que identifica
						// se o neg�cio direto foi intencional: 1 - Intencional
						// / 0 - N�o Intencional
						// [16]Corretora Compra 221 8 C�digo de identifica��o da
						// corretora de compra - Dispon�vel a partir de 03/2014
						// [17]Corretora Venda 230 8 C�digo de identifica��o da
						// corretora de venda - Dispon�vel a partir de 03/2014

						line.tradeDate = vs[0];
						line.securityID = vs[1];
						line.tradeNum = Integer.parseInt(vs[2]);
						line.price = Double.parseDouble(vs[3]);
						line.volume = Integer.parseInt(vs[4]);
						line.tradeTime = vs[5];

						line.buySeqID = Long.parseLong(vs[8]);
						line.sellSeqID = Long.parseLong(vs[12]);
						line.isDirect = vs[15].charAt(0);
					}

					sBuffer.append(line.tradeDate);
					sBuffer.append(";");
					sBuffer.append(line.tradeTime);
					sBuffer.append(";");
					sBuffer.append(line.tradeNum);
					sBuffer.append(";");
					sBuffer.append(line.securityID);
					sBuffer.append(";");
					sBuffer.append(line.price);
					sBuffer.append(";");
					sBuffer.append(line.volume);
					sBuffer.append(";");
					sBuffer.append(line.buySeqID);
					sBuffer.append(";");
					sBuffer.append(line.sellSeqID);
					sBuffer.append(";");
					sBuffer.append(line.isDirect);
					sBuffer.append("\n");
					
					saveBuffer(fos, sBuffer, false);					
				}
			}
			
			saveBuffer(fos, sBuffer, true);
			
			fos.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	private void saveBuffer(FileOutputStream stream, StringBuffer buffer, boolean isClosing) throws IOException {
		
		if(buffer.capacity() > 100000 || isClosing) {
			stream.write(buffer.toString().getBytes());
			buffer.delete(0, buffer.capacity());
		}
	}
	
	private void reLayoutBuySellTickData(String fileName) {

		try {
			StringBuffer sBuffer = new StringBuffer(100000);
			FileOutputStream fos = new FileOutputStream(new File(fileName + ".re"));
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern(FormatterTypes.DATE_F8.getFormat());
			SimpleDateFormat sdf = new SimpleDateFormat(FormatterTypes.DATE_F8.getFormat());
			
			CSV csv = new CSV(fileName, ";", "RT", "RH");
			while (csv.hasNext()) {
				String[] vs = csv.readLine();

				if (vs != null && vs[0] != null) {
					LocalDate sessionDate = sdf.parse(vs[0]).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();;
					Line line = new Line();					
					
					// layout dos arquivos foi alterado devido a mudanca para o
					// sistema de negociacao Puma
					if (vs.length == 12) {// /....!fk
						// -----------------------------------------------------------
						// Coluna Posi��o Inicial Tamanho Descri��o
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
						line.sessionDate = vs[0];
						line.securityID = vs[1];
						line.externalID = Long.parseLong(vs[2]);
						line.price = Double.parseDouble(vs[3]);
						line.volume = Integer.parseInt(vs[4]);
						line.tradedVolume = Integer.parseInt(vs[5]);
						line.orderTime = vs[6];
						line.orderDate = vs[7].split(" ")[0];
						line.orderStatus = vs[8];

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
						line.sessionDate = vs[0];
						line.securityID = vs[1];
						line.externalID = Long.parseLong(vs[3]);
						line.eventCode = Integer.parseInt(vs[5]);
						line.orderTime = vs[6];
						line.price = Double.parseDouble(vs[8]);
						line.volume = Integer.parseInt(vs[9]);
						line.tradedVolume = Integer.parseInt(vs[10]);
						line.orderDate = vs[11];
						line.orderStatus = vs[13];
					}

					sBuffer.append(line.sessionDate);
					sBuffer.append(";");
					sBuffer.append(line.securityID);
					sBuffer.append(";");
					sBuffer.append(line.externalID);
					sBuffer.append(";");
					sBuffer.append(line.eventCode);
					sBuffer.append(";");
					sBuffer.append(line.orderDate);
					sBuffer.append(";");
					sBuffer.append(line.orderTime);
					sBuffer.append(";");
					sBuffer.append(line.orderStatus);
					sBuffer.append(";");
					sBuffer.append(line.price);
					sBuffer.append(";");
					sBuffer.append(line.volume);
					sBuffer.append(";");
					sBuffer.append(line.tradedVolume);
					sBuffer.append(";\n");
					
					saveBuffer(fos, sBuffer, false);
				}
			}
			
			saveBuffer(fos, sBuffer, true);

			fos.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Pre-processa os arquivos historicos da bovespa, criando um novo e unico
	 * arquivo onde tera os campos de interesse separados por ";"
	 * 
	 * @param dir Diretorio onde estao os arquivos historicos
	 */
	public void preProcessBovespaHistoricalFiles(String dir) {

		if (!dir.endsWith("\\"))
			dir += "\\";

		try {
			StringBuffer buffer = new StringBuffer(10000);
			FileOutputStream fos = new FileOutputStream(new File(dir + "Bovespa_Consolidated.csv"));
			InputRowReport impr = new InputRowReport();
			String header = "#Symbol;stockcode;Isin;companyName;CurrencyID;"
					+ "MarketTypeID;QDatetime;Fatquote;QHigh;QLow;QOpen;QClose;"
					+ "QAsk;QBid;PreMed;Volume;TradedUnits;TradedQuantity;"
					+ "ExPrice;ExPoint;ExDate;Prazot;SecType;SecSpeci;StockExchange\n";

			buffer.append(header);
			impr.startReport("Consolidated");

			HashMap<String, String> types = new HashMap<String, String>();
			CSV csv = new CSV(dir + "SecurityTypes.csv", ";");
			while (csv.hasNext()) {
				String[] vs = csv.readLine();
				// speci.add(vs[0]);
				types.put(vs[3], vs[3]);
			}

			for (File file : new File(dir).listFiles()) {
				System.out.println("Lendo arquivo: " + file.getName());

				int linecount = 0;
				int fileLineCount = 0;
				Scanner sc;
				String line;
				try {
					sc = new Scanner(file);
					sc.useDelimiter("\n");
					// para cada linha do arquivo
					while (sc.hasNext()) {

						line = sc.nextLine();

						// registros que iniciam com 01....
						if (line.startsWith("01")) {
							linecount++;
							fileLineCount++;

							String isin = (line.substring(230, 242).stripTrailing());

							if (isin.length() == 12) {

								impr.count("Consolidated");

								// tenta encontrar o papel e a empresa
								String symbol = (line.substring(12, 24).stripTrailing());
								
								if(symbol.contains(" ")) {
									symbol = isin.substring(2, 6) + symbol.substring(4); 
								}
								
								String stockcode = line.substring(12, 16).stripTrailing().replace("'", "''");
								String companyName = line.substring(27, 39).stripTrailing().replace("'", "''");
								String CurrencyID = (line.substring(52, 56).trim());
								Integer MarketTypeID = (Integer.parseInt(line.substring(24, 27)));

								int year, month, day;
								year = Integer.parseInt(line.substring(2, 6));
								month = Integer.parseInt(line.substring(6, 8));
								day = Integer.parseInt(line.substring(8, 10));

								String QDatetime = String.format("%02d-%02d-%02d", year, month, day);

								// LocalDateTime QDateTime = (new GregorianCalendar(year,
								// month-1, day).getTime());
								Integer Fatquote = (Integer.parseInt(line.substring(210, 217)));

								String value = line.substring(69, 82);
								value = value.substring(0, 11) + "." + value.substring(11, value.length());
								BigDecimal QHigh = (new BigDecimal(value));

								value = line.substring(82, 95);
								value = value.substring(0, 11) + "." + value.substring(11, value.length());
								BigDecimal QLow = (new BigDecimal(value));

								value = line.substring(56, 69);
								value = value.substring(0, 11) + "." + value.substring(11, value.length());
								BigDecimal QOpen = (new BigDecimal(value));

								value = line.substring(108, 121);
								value = value.substring(0, 11) + "." + value.substring(11, value.length());
								BigDecimal QClose = (new BigDecimal(value));

								value = line.substring(121, 134);
								value = value.substring(0, 11) + "." + value.substring(11, value.length());
								BigDecimal QAsk = (new BigDecimal(value));

								value = line.substring(134, 147);
								value = value.substring(0, 11) + "." + value.substring(11, value.length());
								BigDecimal QBid = (new BigDecimal(value));

								value = line.substring(95, 108);
								value = value.substring(0, 11) + "." + value.substring(11, value.length());
								BigDecimal PreMed = (new BigDecimal(value));

								value = line.substring(170, 188);
								value = value.substring(0, 16) + "." + value.substring(16, value.length());
								BigDecimal Volume = (new BigDecimal(value));
								Long TradedUnits = (Long.parseLong(line.substring(147, 152)));
								Long TradedQuantity = (Long.parseLong(line.substring(152, 170)));

								value = line.substring(188, 201);
								value = value.substring(0, 11) + "." + value.substring(11, value.length());
								BigDecimal PreExe = (new BigDecimal(value));
								BigDecimal PtoExe = (new BigDecimal("" + Double.parseDouble(line.substring(217, 230)) / 10000000f));
								String ExpDate = line.substring(202, 210);
								ExpDate = ExpDate.substring(0, 4) + "-" + ExpDate.substring(4, 6) + "-"	+ ExpDate.substring(6, 8);
								String Prazot = line.substring(49, 52);
								String secType = "";
								String secSpeci = "";// removeWhite(line.substring(39,49),
								// linecount);

								String[] r = getSecType(isin, types);
								secType = r[0];
								secSpeci = r[1];

								// armazena a linha no buffer
								buffer.append(
								symbol + ";" + 
								stockcode + ";" + 
								isin + ";" + 
								companyName + ";" + 
								CurrencyID + ";" + 
								MarketTypeID + ";" + 
								QDatetime + ";" + 
								Fatquote + ";"+ 
								QHigh + ";" + 
								QLow + ";" + 
								QOpen + ";" + 
								QClose + ";" + 
								QAsk + ";" + 
								QBid + ";" + 
								PreMed + ";" + 
								Volume + ";" + 
								TradedUnits + ";" + 
								TradedQuantity + ";" + 
								PreExe + ";" + 
								PtoExe + ";" + 
								ExpDate + ";" + 
								Prazot + ";" + 
								secType + ";" + 
								secSpeci + ";B3" + "\n");

							}
						}
					}

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

				// System.out.println("Arquivo "+ file.getName()
				// +" inserido com sucesso, n� de registros adicionados: " +
				// regcont + " linhas: " + linecount);
				System.out.println("Resgistros no arquivo:" + fileLineCount);
				saveBuffer(fos, buffer, false);
				
				fileLineCount = 0;
			}

			saveBuffer(fos, buffer, true);
			
			fos.close();
			System.out.println(impr.reportAll());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String[] getSecType(String isin, HashMap<String, String> types) {

		String secType = "";
		String secSpeci = "";

		String[] spcs = { "OR", "PA", "PB", "PC", "PD", "PE", "PF", "PG", "PH", "PR" };

		// BR AAAA BBB CC N
		secType = isin.substring(6, 9);
		while (secType.length() > 0) {
			if (types.containsKey(secType)) {
				break;
			}
			secType = secType.substring(0, secType.length() - 1);
		}

		if (secType.equalsIgnoreCase("acn")) {
			secSpeci = isin.substring(9, isin.length());
			for (String s : spcs) {
				if (secSpeci.contains(s)) {
					secSpeci = s;
					break;
				}
			}
		}

		return new String[] { secType, secSpeci };
	}

	public void extractCodesAndDates(String fileName) {
		CSV csv = new CSV(fileName, ";");

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(FormatterTypes.DATE_F8.getFormat());
		HashMap<String, Line> codes = new HashMap<String, Line>();
		while (csv.hasNext()) {
			String[] line = csv.readLine();

			Line cd = new Line();
			cd.code = line[1];
			cd.date = LocalDateTime.parse(line[6], dateTimeFormatter);
			cd.securityID = line[0];

			if (codes.containsKey(cd.securityID)) {
				if (codes.get(cd.securityID).date.isAfter(cd.date)) {
					codes.put(cd.securityID, cd);
				}
			} else {
				codes.put(cd.securityID, cd);
			}
		}

		try {
			FileOutputStream fos = new FileOutputStream(
					new File("C:\\Disco\\Workspaces\\JFT\\jft_core\\file\\CodesAndDates.csv"));
			SimpleDateFormat sdf = new SimpleDateFormat(FormatterTypes.DATE_F8.getFormat());
			
			for (Line s : codes.values()) {
				try {
					if (s.date != null) {
						fos.write((s.securityID + ";" + s.code + ";" + sdf.format(s.date) + "\n").getBytes());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			fos.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
