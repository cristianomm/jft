package com.cmm.jft.data.files;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;

import com.cmm.jft.model.marketdata.MDEntry;
import com.cmm.jft.model.trading.enums.Side;
import com.google.common.io.Resources;

public class ParquetConverter {

	public static void main(String[] args) throws IOException {

	}
	
	public void convertTrades(List<MDEntry> entries) throws IOException {
		Schema schema = new Schema.Parser().parse(Resources.getResource("avro_schemas/trade.avsc").openStream());

		List<GenericData.Record> records = new ArrayList<GenericData.Record>();
		
		for(MDEntry entry : entries) {
			
			GenericData.Record record = new GenericData.Record(schema);
			
			record.put("symbol", entry.getSymbol());
			record.put("tradeid", entry.getTradeId());
			record.put("price", entry.getMdEntryPx());
			record.put("size", entry.getMdEntrySize());
			record.put("datetime", entry.getMdEntryDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:S")));
			record.put("buyordid", entry.getBuyOrdId());
			record.put("buysecordid", entry.getBuySecOrdId());
			record.put("sellordid", entry.getSellOrdId());
			record.put("sellsecordid", entry.getSellSecOrdId());
			record.put("agressor", entry.getAgressor());
			record.put("buyer", entry.getMdEntryBuyer());
			record.put("seller", entry.getMdEntrySeller());
			
			records.add(record);
		}
		
		writeToParquet(records, schema, "trades");
	}
	
	public void convertOffers(List<MDEntry> entries) throws IOException {
		
		Schema schema = new Schema.Parser().parse(Resources.getResource("avro_schemas/offer.avsc").openStream());

		List<GenericData.Record> records = new ArrayList<GenericData.Record>();
		
		for(MDEntry entry : entries) {
			
			GenericData.Record record = new GenericData.Record(schema);
			
			record.put("symbol", entry.getSymbol());
			record.put("side", entry.getSide() == Side.BUY ? true : false);
			record.put("orderid", entry.getOrderId());
			record.put("mdentryid", entry.getMdEntryId());
			record.put("orderevent", entry.getOrderEvent());
			record.put("datetime", entry.getMdEntryDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:S")));
			record.put("price", entry.getMdEntryPx());
			record.put("size", entry.getMdEntrySize());
			record.put("tradevolume", entry.getTradeVolume());
			record.put("orderdate", entry.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			record.put("orderstatus", entry.getOrderStatus()+"");
			record.put("ordercondition", entry.getOrderCondition());
			
			records.add(record);
		}
		
		writeToParquet(records, schema, "offers");
	}
	
	private static void writeToParquet(List<GenericData.Record> recordList, Schema schema, String fileName) {
        // Path to Parquet file in HDFS
        Path path = new Path(fileName + ".parquet");
        ParquetWriter<GenericData.Record> writer = null;
        // Creating ParquetWriter using builder
        try {
            writer = AvroParquetWriter.
                <GenericData.Record>builder(path)
                .withRowGroupSize(ParquetWriter.DEFAULT_BLOCK_SIZE)
                .withPageSize(ParquetWriter.DEFAULT_PAGE_SIZE)
                .withSchema(schema)
                .withConf(new Configuration())
                .withCompressionCodec(CompressionCodecName.SNAPPY)
                .withValidation(false)
                .withDictionaryEncoding(false)
                .build();
            
            for (GenericData.Record record : recordList) {
                writer.write(record);
            }
            
        }catch(IOException e) {
            e.printStackTrace();
        }finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
