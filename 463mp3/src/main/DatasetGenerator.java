package main;

import info.blockchain.api.blockexplorer.Block;
import info.blockchain.api.blockexplorer.BlockExplorer;
import info.blockchain.api.blockexplorer.Input;
import info.blockchain.api.blockexplorer.Output;
import info.blockchain.api.blockexplorer.SimpleBlock;
import info.blockchain.api.blockexplorer.Transaction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatasetGenerator {
	private String file;
	private String hash = "000000000000000f5795bfe1de0381a44d4d5ea2ad81c21d77f275bffa03e8b3";

	public DatasetGenerator(String file) {
		this.file = file;
	}

	public boolean writeExampleTransactions() {
		try {
			BlockExplorer blockExplorer = new BlockExplorer();
			BufferedWriter wTx = new BufferedWriter(new FileWriter(file));
			Block block = blockExplorer.getBlock(hash);
			List<Transaction> transactions = block.getTransactions();
			for (Transaction tx : transactions) {
				List<Input> inputs = tx.getInputs();
				if (inputs.isEmpty()
						|| inputs.get(0).getPreviousOutput() == null) {
					continue;
				}
				for (Input in : inputs) {
					wTx.write(generateInputRecord(tx.getIndex(), tx.getHash(),
							in.getPreviousOutput().getAddress(), in
									.getPreviousOutput().getValue())
							+ "\n");
				}
				List<Output> outputs = tx.getOutputs();
				for (Output out : outputs) {
					wTx.write(generateOutputRecord(tx.getIndex(), tx.getHash(),
							out.getAddress(), out.getValue()) + "\n");
				}
			}

			wTx.flush();
			wTx.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean writeTransactions() {
		String dateString1 = "10/25/2013";
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		try {
			Date date1 = dateFormat.parse(dateString1);
			BufferedWriter wTx = new BufferedWriter(new FileWriter(file));

			long time1 = date1.getTime();
			Timestamp ts1 = new Timestamp(time1);

			BlockExplorer blockExplorer = new BlockExplorer(null);

			// get all the blocks on one day
			List<SimpleBlock> blocks = blockExplorer
					.getBlocks(ts1.getTime() / 1000);
			List<Transaction> allDayTx = new ArrayList<Transaction>();
			System.out.println(blocks.size());
			int j = 0;
			for (SimpleBlock sb : blocks) {
				Block block = blockExplorer.getBlock(sb.getHash());
				System.out.println(j++);
				List<Transaction> transactions = block.getTransactions();
				allDayTx.addAll(transactions);
				for (Transaction tx : transactions) {
					List<Input> inputs = tx.getInputs();
					if (inputs.isEmpty()
							|| inputs.get(0).getPreviousOutput() == null) {
						continue;
					}
					for (Input in : inputs) {
						wTx.write(generateInputRecord(tx.getIndex(), tx
								.getHash(),
								in.getPreviousOutput().getAddress(), in
										.getPreviousOutput().getValue())
								+ "\n");
					}
					List<Output> outputs = tx.getOutputs();
					for (Output out : outputs) {
						wTx.write(generateOutputRecord(tx.getIndex(),
								tx.getHash(), out.getAddress(), out.getValue())
								+ "\n");
					}
				}
			}
			wTx.flush();
			wTx.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Generate a record in the transaction dataset
	 * 
	 * @param txIndex
	 *            Transaction index
	 * @param txHash
	 *            Transaction hash
	 * @param address
	 *            Previous output address of the input
	 * @param value
	 *            Number of Satoshi transferred
	 * @return A record of the input
	 */
	private String generateInputRecord(long txIndex, String txHash,
			String address, long value) {
		return txIndex + " " + txHash + " " + address + " " + value + " in";
	}

	/**
	 * Generate a record in the transaction dataset
	 * 
	 * @param txIndex
	 *            Transaction index
	 * @param txHash
	 *            Transaction hash
	 * @param address
	 *            Output bitcoin address
	 * @param value
	 *            Number of Satoshi transferred
	 * @return A record of the output
	 */
	private String generateOutputRecord(long txIndex, String txHash,
			String address, long value) {
		return txIndex + " " + txHash + " " + address + " " + value + " out";
	}

}
