package com.cmm.jft.data.files;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;

import com.cmm.jft.data.extractor.marketdata.BovespaOfferFileExtractor;
import com.cmm.jft.data.extractor.marketdata.BovespaTradeFileExtractor;
import com.cmm.jft.model.marketdata.MDEntry;
import com.cmm.jft.model.trading.enums.Side;
import com.cmm.jft.vo.Extractable;
import com.google.common.io.Resources;

public class ParquetConverter implements Runnable {

	private static String destFolder = "conversion";
	private static String convertFolder = "converted";
	private static String filesFolder = "";
	
	public static void main(String[] args) {
		new ParquetConverter().convertFiles("/home/cristiano/Data/MarketData/");
	}

	public void convertFiles(String path) {

		new File(path + destFolder).mkdirs();

		try {
			BovespaTradeFileExtractor tradeFileExtractor = new BovespaTradeFileExtractor();
			BovespaOfferFileExtractor offerFileExtractor = new BovespaOfferFileExtractor();

			for (File file : new File(path + filesFolder).listFiles()) {
				if (file.isFile() && file.getName().endsWith(".zip")) {
					// extrai os arquivos contidos no zip e consolida em um unico arquivo
					List<String> files = Zimp.extract(file.getAbsolutePath(), path + destFolder);

					for (String fileName : files) {
						Schema schema = null;
						List<GenericData.Record> records = null;
						Schema tradeSchema = new Schema.Parser()
								.parse(Resources.getResource("avro_schemas/trade.avsc").openStream());
						Schema offerSchema = new Schema.Parser()
								.parse(Resources.getResource("avro_schemas/offer.avsc").openStream());

						Properties properties = new Properties();
						properties.put("filename", fileName);
						// caso seja arquivo de negociacao
						if (fileName.contains("NEG")) {
							schema = tradeSchema;
							tradeFileExtractor.config(properties);
							/*
							List<Extractable> registers = tradeFileExtractor.extract();
							
							for(Extractable e : registers) {
								MDEntry entry = ((MDEntry)e);
															
							}
							*/
							
							records = convertTrades((List) tradeFileExtractor.extract());
						} else {
							schema = offerSchema;
							offerFileExtractor.config(properties);
							records = convertOffers((List) offerFileExtractor.extract());
						}

						writeToParquet(records, schema, fileName);

						new File(fileName).delete();
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<GenericData.Record> convertTrades(List<MDEntry> entries) throws IOException {
		Schema schema = new Schema.Parser().parse(Resources.getResource("avro_schemas/trade.avsc").openStream());

		List<GenericData.Record> records = new ArrayList<GenericData.Record>();

		for (MDEntry entry : entries) {

			GenericData.Record record = new GenericData.Record(schema);
			
			record.put("symbol", entry.getSymbol());
			record.put("tradeid", entry.getTradeId());
			record.put("price", entry.getMdEntryPx());
			record.put("size", entry.getMdEntrySize());
			record.put("entrydate",	Integer.parseInt(entry.getMdEntryDateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));
			record.put("entrytime",	Date.from(entry.getMdEntryDateTime().atZone(ZoneId.systemDefault()).toInstant()).getTime());//entry.getMdEntryDateTime().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")));
			record.put("buyordid", entry.getBuyOrdId());
			record.put("buysecordid", entry.getBuySecOrdId());
			record.put("sellordid", entry.getSellOrdId());
			record.put("sellsecordid", entry.getSellSecOrdId());
			record.put("agressor", entry.getAgressor() + "");
			record.put("buyer", entry.getMdEntryBuyer());
			record.put("seller", entry.getMdEntrySeller());

			records.add(record);
		}

		return records;
	}

	public static List<GenericData.Record> convertOffers(List<MDEntry> entries) throws IOException {

		Schema schema = new Schema.Parser().parse(Resources.getResource("avro_schemas/offer.avsc").openStream());

		List<GenericData.Record> records = new ArrayList<GenericData.Record>();

		for (MDEntry entry : entries) {

			GenericData.Record record = new GenericData.Record(schema);
			
			record.put("symbol", entry.getSymbol());
			record.put("side", entry.getSide() == Side.BUY ? true : false);
			record.put("orderid", entry.getOrderId());
			record.put("mdentryid", entry.getMdEntryId());
			record.put("orderevent", entry.getOrderEvent());
			record.put("offerdate", Integer.parseInt(entry.getMdEntryDateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));
			record.put("offertime",	Date.from(entry.getMdEntryDateTime().atZone(ZoneId.systemDefault()).toInstant()).getTime());//.format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")));
			record.put("price", entry.getMdEntryPx());
			record.put("size", entry.getMdEntrySize());
			record.put("tradevolume", entry.getTradeVolume());
			record.put("orderdate", Integer.parseInt(entry.getOrderDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));
			record.put("orderstatus", entry.getOrderStatus() + "");
			record.put("ordercondition", entry.getOrderCondition());

			records.add(record);
		}

		return records;
	}

	public static void writeToParquet(List<GenericData.Record> recordList, Schema schema, String fileName) {
		// Path to Parquet file in HDFS
		String parquetFile = fileName + ".parquet";
		new File(parquetFile).delete();
		
		Path path = new Path(parquetFile);
		ParquetWriter<GenericData.Record> writer = null;
		// Creating ParquetWriter using builder
		try {
			writer = AvroParquetWriter.<GenericData.Record>builder(path)
					.withRowGroupSize(ParquetWriter.DEFAULT_BLOCK_SIZE).withPageSize(ParquetWriter.DEFAULT_PAGE_SIZE)
					.withSchema(schema).withConf(new Configuration()).withCompressionCodec(CompressionCodecName.SNAPPY)
					.withValidation(false).withDictionaryEncoding(false).build();

			for (GenericData.Record record : recordList) {
				try {
					writer.write(record);
				} catch (ClassCastException e) {
					System.out.println(record);
					e.printStackTrace();
					throw e;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void run() {

	}
}
