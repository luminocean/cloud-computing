package cn.edu.nju.hadoop_test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

public class HbaseRead {
	@SuppressWarnings({ "unused", "deprecation", "resource" })
	public static void createBibTex() throws MasterNotRunningException, ZooKeeperConnectionException, IOException {
		String type = "BibTex";
		Configuration config = HBaseConfiguration.create();
		HBaseAdmin admin = new HBaseAdmin(config);
		TableName tableName = TableName.valueOf("cite");
		HTable table = new HTable(config, tableName);

		String filepath = "src/main/java/resources/" + type + "Copy.txt";
		File file = new File(filepath);
		if (!file.exists()) {
			file.createNewFile();
		}

		Scan scan = new Scan();
		scan.addColumn(Bytes.toBytes("type"), Bytes.toBytes(type));
		ResultScanner scanner = table.getScanner(scan);
		try {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				for (Result scannerResult : scanner) {
					String[] strs = Bytes.toString(scannerResult.value()).split("#");
					for (int i = 0; i < strs.length; i++) {
						writer.write(strs[i]);
						writer.newLine();
					}
					writer.newLine();
				}

				writer.flush();
				writer.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		} finally {
			scanner.close();
		}
	}

	public static void createAPA() throws IOException {
		create("APA");
	}

	public static void createChicago() throws IOException {
		create("Chicago");
	}

	public static void createMLA() throws IOException {
		create("MLA");
	}

	@SuppressWarnings({ "deprecation", "unused", "resource" })
	public static void create(String type) throws IOException {
		Configuration config = HBaseConfiguration.create();
		HBaseAdmin admin = new HBaseAdmin(config);
		TableName tableName = TableName.valueOf("cite");
		HTable table = new HTable(config, tableName);

		String filepath = "src/main/java/resources/" + type + "Copy.txt";
		File file = new File(filepath);
		if (!file.exists()) {
			file.createNewFile();
		}

		Scan scan = new Scan();
		scan.addColumn(Bytes.toBytes("type"), Bytes.toBytes(type));
		ResultScanner scanner = table.getScanner(scan);
		try {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				for (Result scannerResult : scanner) {
					writer.write((Bytes.toString(scannerResult.value())));
					writer.newLine();
					writer.newLine();
				}
				writer.flush();
				writer.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		} finally {
			scanner.close();
		}
	}

	public static void main(String[] args) throws MasterNotRunningException, ZooKeeperConnectionException, IOException {
		// TODO Auto-generated method stub
		createAPA();
		createBibTex();
		createChicago();
		createMLA();

	}
}
