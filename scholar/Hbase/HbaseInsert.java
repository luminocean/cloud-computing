package cn.edu.nju.hadoop_test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

public class HbaseInsert {
	private static Configuration config = HBaseConfiguration.create();

	@SuppressWarnings("deprecation")
	public static void createTable() throws MasterNotRunningException, ZooKeeperConnectionException, IOException {
		HBaseAdmin admin = new HBaseAdmin(config);
		try {
			TableName tableName = TableName.valueOf("cite");
			HTableDescriptor htd = new HTableDescriptor(tableName);
			HColumnDescriptor hcd = new HColumnDescriptor("type");
			htd.addFamily(hcd);
			admin.createTable(htd);
		} finally {
			// TODO: handle finally clause
			admin.close();
		}

	}

	public static void insertAPA() throws MasterNotRunningException, ZooKeeperConnectionException, IOException {
		String path = "/resources/APA.txt";
		String type = "APA";
		insert(path, type);
	}

	public static void insertChicago() throws MasterNotRunningException, ZooKeeperConnectionException, IOException {
		String path = "/resources/Chicago.txt";
		String type = "Chicago";
		insert(path, type);
	}

	public static void insertMLA() throws MasterNotRunningException, ZooKeeperConnectionException, IOException {
		String path = "/resources/MLA.txt";
		String type = "MLA";
		insert(path, type);
	}

	@SuppressWarnings("deprecation")
	public static void insert(String path, String type)
			throws MasterNotRunningException, ZooKeeperConnectionException, IOException {
		HBaseAdmin admin = new HBaseAdmin(config);
		TableName tableName = TableName.valueOf("cite");
		HTable table = new HTable(config, tableName);
		String filepath = HbaseInsert.class.getResource(path).getFile();
		File file = new File(filepath);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			int i = 0;
			while ((line = reader.readLine()) != null) {
				if (line.length() == 0) {
					continue;
				}
				byte[] row = Bytes.toBytes(i);
				Put put = new Put(row);
				byte[] columnFamily = Bytes.toBytes("type");
				byte[] qualifier = Bytes.toBytes(type);
				byte[] value = Bytes.toBytes(line);
				put.add(columnFamily, qualifier, value);
				table.put(put);
				i++;
			}
			reader.close();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			table.close();
			admin.close();
		}

	}

	@SuppressWarnings("deprecation")
	public static void insertBibtex() throws MasterNotRunningException, ZooKeeperConnectionException, IOException {
		String path = "/resources/BibTex.txt";
		String type = "BibTex";

		HBaseAdmin admin = new HBaseAdmin(config);
		TableName tableName = TableName.valueOf("cite");
		HTable table = new HTable(config, tableName);
		String filepath = HbaseInsert.class.getResource(path).getFile();
		File file = new File(filepath);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			String text = "";
			int i = 0;
			while ((line = reader.readLine()) != null) {
				if (line.length() == 0) {
					byte[] row = Bytes.toBytes(i);
					Put put = new Put(row);
					byte[] columnFamily = Bytes.toBytes("type");
					byte[] qualifier = Bytes.toBytes(type);
					byte[] value = Bytes.toBytes(text);
					put.add(columnFamily, qualifier, value);
					table.put(put);
					text = "";
					i++;
				} else {
					text += (line+"#");
				}
			}
			reader.close();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			table.close();
			admin.close();
		}

	}

	public static void main(String[] args) throws IOException {
		HbaseInsert.createTable();
		HbaseInsert.insertAPA();
		HbaseInsert.insertChicago();
		HbaseInsert.insertMLA();
		HbaseInsert.insertBibtex();
	}
}
