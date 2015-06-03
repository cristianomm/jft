/**
 * 
 */
package com.cmm.jft.data.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipException;

import com.cmm.jft.core.format.DateTimeFormatter;
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

	private class CD {
		String symbol;
		String code;
		Date date;
	}

	public static void main(String[] args) {

		System.exit(0);
		
		// new
		// PreProcessFiles().reLayoutBuySellTickData("C:\\Users\\Cristiano Martins\\Downloads\\BM&F Bovespa Tick Data\\072013\\OFER_CPA_20130701.TXT");
		// new
		// PreProcessFiles().extractCodesAndDates("C:\\Disco\\Workspaces\\JFT\\jft_core\\file\\HistoricalQuotes.csv");
		// new
		// PreProcessFiles().preProcessBovespaHistoricalFiles("C:\\Disco\\Bancos\\Bolsa\\BM&FBovespa\\Historico Bovespa\\COTAHIST");
		new PreProcessFiles()
				.preProcessTickDataFiles(
						"C:\\Users\\Cristiano Martins\\Downloads\\BM&F Bovespa Tick Data\\Vista",
						"C:\\Users\\Cristiano Martins\\Downloads\\BM&F Bovespa Tick Data\\Vista\\All\\");
	}
	
	
	
	
	
	/**
	 * Preprocessa os arquivos TickData, criando um unico arquivo para cada
	 * layout existente: Ofertas de Compra, Ofertas de Venda e Negocios. Sera
	 * criado um arquivo para cada mes
	 * 
	 * @param dir
	 *            Diretorio onde estao os arquivos.
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
				// extrai os arquivos contidos no zip e consolida em um unico arquivo
				String fName = extractAndConsolidate(file.getAbsolutePath(), destDir);

				// caso seja arquivo de negociacao
				if (fName.contains("NEG")) {
					reLayoutTradeTickData(fName);
				} else {
					reLayoutBuySellTickData(fName);
				}
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
			fName += zipFile.substring(zipFile.lastIndexOf('\\'),
					zipFile.lastIndexOf('.'))
					+ ".csv";
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
					// Coluna Posição Inicial Tamanho Descrição
					// ------------------------------------------------------
					// Data Sessão 1 10 Data de sessão
					// Papel 12 50 Código do papel
					// Nr.Negócio 63 7 Número do negócio OBS: Para as ofertas do
					// Soma com data entre novembro/1996 e julho/2000 (IBM), foi
					// gerado um sequencial de 10 em 10 por papel. O número do
					// boleto original que identificava o negócio foi jogado no
					// campo QTE_MINI_LOTE_PAD devido ao seu tamanho (9 bytes).
					// Preço Negócio 71 20 Preço do negócio
					// Quantidade 92 18 Quantidade negociada
					// Hora 111 12 Horário da negociação (no formato
					// HH:MM:SS.NNN)
					// Ind.Anulação 124 1 Indicador de anulação: " " - Negócio
					// "A" - Negócio anulado "X" - Complemento de anulação. Após
					// 04/03/2013 devido a migração para o PUMA alguns ativos
					// estarão valorizados com: 1 - "Ativo" / 2 - "Cancelado".
					// Data Oferta Compra 126 10 Data da oferta de compra
					// Seq.Oferta Compra 137 10 Número sequencial da oferta de
					// compra
					// Data Oferta Venda 148 10 Data da oferta de venda
					// Seq.Oferta Venda 159 10 Número sequencial da oferta de
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
//						Coluna                 Posição Inicial  Tamanho   Descrição
//						-----------------------------------------------------------
//						[0]Data Sessão                          1       10   Data de sessão
//						[1]Símbolo do Instrumento              12       50   Símbolo do Instrumento
//						[2]Nr.Negócio                          63       10   Número do negócio
//						[3]Preço Negócio                       74       20   Preço do negócio
//						[4]Quantidade                          95       18   Quantidade negociada
//						[5]Hora                               114       15   Horário da negociação (formato HH:MM:SS.NNN)
//						[6]Ind.Anulação                       127        1   Indicador de Anulação: "1" - ativo / "2" - cancelado
//						[7]Data Oferta Compra                 129       10   Data da oferta de compra
//						[8]Seq.Oferta Compra                  140       15   Número sequencial da oferta de compra
//						[9]GenerationID - Of.Compra           156       15   Número de geração (GenerationID) da Oferta de compra. Quando um negócio for gerado por 2 ofertas com quantidade escondida e isso gerar "n" linhas será gravado aqui a maior geração
//						[10]Condição Oferta de Compra          172        1   Código que identifica a condição da oferta de compra. Pode ser: 0 - Oferta Neutra - é aquela que entra no mercado e não fecha com oferta existente. / 1 - Oferta Agressora - é aquela que ingressa no mercado para fechar com uma oferta existente. / 2 - Oferta Agredida - é a oferta (existente) que é fechada com uma oferta agressora.
//						[11]Data Oferta Venda                  174       10   Data da oferta de venda
//						[12]Seq.Oferta Venda                   185       15   Número sequencial da oferta de venda
//						[13]GenerationID - Of.Venda            201       15   Número de geração (GenerationID) da Oferta de venda. Quando um negócio for gerado por 2 ofertas com quantidade escondida e isso gerar "n" linhas será gravado aqui a maior geração
//						[14]Condição Oferta de Venda           217        1   Código que identifica a condição da oferta de venda. Pode ser: 0 - Oferta Neutra - é aquela que entra no mercado e não fecha com oferta existente. / 1 - Oferta Agressora - é aquela que ingressa no mercado para fechar com uma oferta existente. / 2 - Oferta Agredida - é a oferta (existente) que é fechada com uma oferta agressora.
//						[15]Indicador de direto                219        1   Código que identifica se o negócio direto foi intencional: 1 - Intencional / 0 - Não Intencional
//						[16]Corretora Compra                   221        8   Código de identificação da corretora de compra - Disponível a partir de 03/2014
//						[17]Corretora Venda                    230        8   Código de identificação da corretora de venda - Disponível a partir de 03/2014
						
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

	}

	private void reLayoutBuySellTickData(String fileName) {

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
					String price = "";
					String externalID = "";
					String securityID = "";
					String volume = "";
					String orderDate = "";
					String orderTime = "";
					String orderStatus = "";
					String eventCode = "";
					String tradedVolume = "";

					// layout dos arquivos foi alterado devido a mudanca para o
					// sistema de negociacao Puma
					if (vs.length == 12) {// /....!fk
						// -----------------------------------------------------------
						// Coluna Posição Inicial Tamanho Descrição
						// -----------------------------------------------------------
						// Data Sessão 1 10 Data da Sessão
						// Papel 50 12 Código do Instrumento
						// Sequência 63 10 Número de Sequência da Oferta
						// Preço Of.Compra 74 20 Preço da Oferta
						// Qtd.Total Of.Compra 95 18 Quantidade Total
						// Qtd.Negociada Of.Compra 114 18 Quantidade Negociada
						// Hora Prioridade 133 15 Hora de registro da oferta no
						// sistema (com a precisão Tandem, no formato,
						// HH:MM:SS.NNNNNN), utilizada como indicadora de
						// prioridade
						// Data de Entrada Of.Compra 149 19 Data/Hora de Entrada
						// da Oferta
						// Estado Of.Compra 169 1 Indicador de estado da ordem
						// " " - aceite "E" - eliminada (EOC) "G" - congelada
						// "O" - cancelada seguido de uma ação no instrumento
						// (por ex- Papel Reservado) "X" - totalmente executada
						// "M" - modificada "D" - disparada "A" - anulada
						// (corretora) "R" - rejeitada pelo Surveillance,
						// seguido de um congelamento. Após 04/03/2013 devido a
						// migração para o PUMA alguns ativos estarão
						// valorizados com: 0 - Novo / 1 - Negociada
						// parcialmente / 2 - Totalmente executada / 4 -
						// Cancelada / 5 - Modificada / 8 - Rejeitada / C -
						// Expirada
						// Data Modif. Of.Compra 171 10 Data de Modificação da
						// Oferta
						// Nr.Of.Compra Modif. 182 10 Número da Oferta
						// Modificada
						// Hora Fim Tratam. Of.Compra 193 19 Hora de Fim de
						// Tratamento (contém Hora da Anulação quando Indicador
						// de Estado da Orderm for igual a "A")
						securityID = vs[1];
						externalID = vs[2];
						price = vs[3];
						volume = vs[4];
						tradedVolume = vs[5];
						orderTime = vs[6];
						orderDate = vs[7].split(" ")[0];
						orderStatus = vs[8];

					} else if (vs.length >= 14) {
						// -----------------------------------------------------------
						// Coluna Posição Inicial Tamanho Descrição
						// -----------------------------------------------------------
						// Data Sessão 1 10 Data da Sessão
						// Símbolo do Instrumento 12 50 Símbolo do Instrumento
						// Sentido Of.Compra 63 1 Indicador de sentido da ordem:
						// "1" - compra / "2" - venda
						// Sequência 65 10 Número de Sequência da Oferta
						// GenerationID - Of.Compra 76 10 Número de geração
						// (GenerationID) da Oferta de Compra. Quando um negócio
						// for gerado por 2 ofertas com quantidade escondida e
						// isso gerar "n" linhas será gravado aqui a maior
						// geração.
						// Cód do Evento da Of.Compra 87 3 Código do Evento da
						// Ordem: 1 - New / 2 - Update / 3 - Cancel - Solicitado
						// pelo participante / 4 - Trade / 5 - Reentry -
						// Processo interno (quantidade escondida) / 6 - New
						// Stop Price / 7 - Reject / 8 - Remove - Removida pelo
						// Sistema (final de dia ou quando é totalmente fechada)
						// / 9 - Stop Price Triggered / 11 - Expire - Oferta com
						// validade expirada.
						// Hora Prioridade 91 15 Hora de registro da oferta no
						// sistema (no formato, HH:MM:SS.NNN), utilizada como
						// indicadora de prioridade
						// Ind de Prioridade Of.Compra 107 10 Indicador de
						// Prioridade. Além do preço é a ordem para aparecer no
						// Order Book.
						// Preço Of.Compra 118 20 Preço da Oferta
						// Qtd.Total Of.Compra 139 18 Quantidade Total da
						// Oferta. Se tiver alteração ela reflete a nova
						// quantidade.
						// Qtd.Negociada Of.Compra 158 18 Quantidade Negociada
						// Data Oferta Compra 177 10 Data de Inclusão da Oferta.
						// Pode ser uma data anterior à Data da Sessão, quando
						// se tratar de uma Oferta com Validade.
						// Data de Entrada Of.Compra 188 19 Data/Hora de Entrada
						// da Oferta (formato: DD/MM/AAAA HH:MM:SS)
						// Estado Of.Compra 208 1 Indicador de estado da ordem:
						// 0 - Novo / 1 - Negociada parcialmente / 2 -
						// Totalmente executada / 4 - Cancelada / 5 - Modificada
						// / 8 - Rejeitada / C - Expirada
						// Condição Oferta 210 1 Código que identifica a
						// condição da oferta. Pode ser: 0 - Oferta Neutra - é
						// aquela que entra no mercado e não fecha com oferta
						// existente. / 1 - Oferta Agressora - é aquela que
						// ingressa no mercado para fechar com uma oferta
						// existente. / 2 - Oferta Agredida - é a oferta
						// (existente) que é fechada com uma oferta agressora.
						securityID = vs[1];
						externalID = vs[3];
						eventCode = vs[5];
						orderTime = vs[6];
						price = vs[8];
						volume = vs[9];
						tradedVolume = vs[10];
						orderDate = vs[11];
						orderStatus = vs[13];
					}

					if (sBuffer.capacity() <= 100000) {
						/*
						String line = dtf.format(sessionDate) + ";"
								+ securityID + ";" + externalID + ";"
								+ eventCode + ";" + orderDate + ";" + orderTime
								+ ";" + orderStatus + ";" + price + ";"
								+ volume + ";" + tradedVolume + "\n";
						*/					
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

	}

	/**
	 * Pre-processa os arquivos historicos da bovespa, criando um novo e unico
	 * arquivo onde tera os campos de interesse separados por ";"
	 * 
	 * @param dir
	 *            Diretorio onde estao os arquivos historicos
	 */
	public void preProcessBovespaHistoricalFiles(String dir) {

		if (!dir.endsWith("\\"))
			dir += "\\";

		try {
			StringBuffer buffer = new StringBuffer(10000);
			FileOutputStream fos = new FileOutputStream(new File(dir
					+ "Bovespa_Consolidated.csv"));
			InputRowReport impr = new InputRowReport();
			String header = "#Symbol;stockcode;Isin;companyName;CurrencyID;"
					+ "MarketTypeID;QDatetime;Fatquote;QHigh;QLow;QOpen;QClose;"
					+ "QAsk;QBid;PreMed;Volume;TradedUnits;TradedQuantity;"
					+ "ExPrice;ExPoint;ExDate;Prazot;SecType;SecSpeci;StockExchange\n";

			buffer.append(header);
			impr.startReport("Consolidated");

			HashMap<String, String> types = new HashMap<String, String>();
			CSV csv = new CSV(
					"C:\\Disco\\Workspaces\\JFT\\jft_core\\file\\SecurityTypes.csv",
					";");
			while (csv.hasNext()) {
				String[] vs = csv.readLine();
				// speci.add(vs[0]);
				types.put(vs[3], vs[3]);
			}

			for (File file : new File(dir).listFiles()) {
				// System.out.println(file.getName());
				// if(Integer.parseInt(file.getName().substring(10,
				// 14))>=2011)continue;

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

						line = sc.next();

						// registros que iniciam com 01....
						if (line.startsWith("01")) {
							linecount++;
							fileLineCount++;

							String Isin = (removeWhite(
									line.substring(230, 242), linecount));

							if (Isin.length() == 12) {

								impr.count("Consolidated");

								// tenta encontrar o papel e a empresa
								String Symbol = (removeWhite(
										line.substring(12, 24), linecount));
								String stockcode = removeWhite(
										line.substring(12, 16), linecount)
										.replace("'", "''");
								String companyName = removeWhite(
										line.substring(27, 39), linecount)
										.replace("'", "''");
								String CurrencyID = (line.substring(52, 56)
										.trim());
								Integer MarketTypeID = (Integer.parseInt(line
										.substring(24, 27)));

								int year, month, day;
								year = Integer.parseInt(line.substring(2, 6));
								month = Integer.parseInt(line.substring(6, 8));
								day = Integer.parseInt(line.substring(8, 10));

								String QDatetime = String.format(
										"%02d-%02d-%02d", year, month, day);

								// Date QDateTime = (new GregorianCalendar(year,
								// month-1, day).getTime());
								Integer Fatquote = (Integer.parseInt(line
										.substring(210, 217)));

								String value = line.substring(69, 82);
								value = value.substring(0, 11) + "."
										+ value.substring(11, value.length());
								BigDecimal QHigh = (new BigDecimal(value));

								value = line.substring(82, 95);
								value = value.substring(0, 11) + "."
										+ value.substring(11, value.length());
								BigDecimal QLow = (new BigDecimal(value));

								value = line.substring(56, 69);
								value = value.substring(0, 11) + "."
										+ value.substring(11, value.length());
								BigDecimal QOpen = (new BigDecimal(value));

								value = line.substring(108, 121);
								value = value.substring(0, 11) + "."
										+ value.substring(11, value.length());
								BigDecimal QClose = (new BigDecimal(value));

								value = line.substring(121, 134);
								value = value.substring(0, 11) + "."
										+ value.substring(11, value.length());
								BigDecimal QAsk = (new BigDecimal(value));

								value = line.substring(134, 147);
								value = value.substring(0, 11) + "."
										+ value.substring(11, value.length());
								BigDecimal QBid = (new BigDecimal(value));

								value = line.substring(95, 108);
								value = value.substring(0, 11) + "."
										+ value.substring(11, value.length());
								BigDecimal PreMed = (new BigDecimal(value));

								value = line.substring(170, 188);
								value = value.substring(0, 16) + "."
										+ value.substring(16, value.length());
								BigDecimal Volume = (new BigDecimal(value));
								Long TradedUnits = (Long.parseLong(line
										.substring(147, 152)));
								Long TradedQuantity = (Long.parseLong(line
										.substring(152, 170)));

								value = line.substring(188, 201);
								value = value.substring(0, 11) + "."
										+ value.substring(11, value.length());
								BigDecimal PreExe = (new BigDecimal(value));
								BigDecimal PtoExe = (new BigDecimal(""
										+ Double.parseDouble(line.substring(
												217, 230)) / 10000000f));
								String ExpDate = line.substring(202, 210);
								ExpDate = ExpDate.substring(0, 4) + "-"
										+ ExpDate.substring(4, 6) + "-"
										+ ExpDate.substring(6, 8);
								String Prazot = line.substring(49, 52);
								String secType = "";
								String secSpeci = "";// removeWhite(line.substring(39,49),
														// linecount);

								String[] r = getSecType(Isin, types);
								secType = r[0];
								secSpeci = r[1];

								// armazena a linha no buffer
								buffer.append(Symbol + ";" + stockcode + ";"
										+ Isin + ";" + companyName + ";"
										+ CurrencyID + ";" + MarketTypeID + ";"
										+ QDatetime + ";" + Fatquote + ";"
										+ QHigh + ";" + QLow + ";" + QOpen
										+ ";" + QClose + ";" + QAsk + ";"
										+ QBid + ";" + PreMed + ";" + Volume
										+ ";" + TradedUnits + ";"
										+ TradedQuantity + ";" + PreExe + ";"
										+ PtoExe + ";" + ExpDate + ";" + Prazot
										+ ";" + secType + ";" + secSpeci
										+ ";BM&FBOVESPA" + "\n");

							}
						}

					}

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

				// System.out.println("Arquivo "+ file.getName()
				// +" inserido com sucesso, n° de registros adicionados: " +
				// regcont + " linhas: " + linecount);
				System.out.println("Resgistros no arquivo:" + fileLineCount);
				fos.write(buffer.toString().getBytes());
				buffer = new StringBuffer(10000);
				fileLineCount = 0;

			}

			fos.close();
			System.out.println(impr.reportAll());

		} catch (Exception e) {

		}

	}

	public String[] getSecType(String isin, HashMap<String, String> types) {

		String secType = "";
		String secSpeci = "";

		String[] spcs = { "OR", "PA", "PB", "PC", "PD", "PE", "PF", "PG", "PH",
				"PR" };

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

		HashMap<String, CD> codes = new HashMap<String, CD>();
		while (csv.hasNext()) {
			String[] line = csv.readLine();

			CD cd = new CD();
			cd.code = line[1];
			cd.date = (Date) FormatterFactory.getFormatter(
					FormatterTypes.DATE_F8).parse(line[6]);
			cd.symbol = line[0];

			if (codes.containsKey(cd.symbol)) {
				if (codes.get(cd.symbol).date.after(cd.date)) {
					codes.put(cd.symbol, cd);
				}
			} else {
				codes.put(cd.symbol, cd);
			}

		}

		try {
			FileOutputStream fos = new FileOutputStream(
					new File(
							"C:\\Disco\\Workspaces\\JFT\\jft_core\\file\\CodesAndDates.csv"));
			DateTimeFormatter dtf = (DateTimeFormatter) FormatterFactory
					.getFormatter(FormatterTypes.DATE_F8);
			for (CD s : codes.values()) {
				try {
					if (s.date != null) {
						fos.write((s.symbol + ";" + s.code + ";"
								+ dtf.format(s.date) + "\n").getBytes());
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

	private String removeWhite(String txt, int line) {
		try {
			while (txt.charAt(txt.length() - 1) == ' ') {
				txt = txt.substring(0, txt.length() - 1);
			}
		} catch (StringIndexOutOfBoundsException e) {
			System.out.println("Empresa " + txt
					+ " não possui codigo de negociacao. Linha:" + line);
		}
		return txt;
	}

}
