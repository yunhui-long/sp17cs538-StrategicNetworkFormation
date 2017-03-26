package main;

import info.blockchain.api.APIException;
import info.blockchain.api.blockexplorer.Block;
import info.blockchain.api.blockexplorer.BlockExplorer;
import info.blockchain.api.blockexplorer.Input;
import info.blockchain.api.blockexplorer.Output;
import info.blockchain.api.blockexplorer.Transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Checkpoint1 {

	private String hash = "000000000000000f5795bfe1de0381a44d4d5ea2ad81c21d77f275bffa03e8b3";
	private Block block;
	private BlockExplorer ex;
	private List<Transaction> transactions;

	public Checkpoint1() {
		ex = new BlockExplorer();
		try {
			System.out.println(hash);
			block = ex.getBlock(hash);
			transactions = block.getTransactions();
		} catch (APIException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Blocks-Q1: What is the size of this block?
	 * 
	 * Hint: Use method getSize() in Block.java
	 * 
	 * @return size of the block
	 */
	public long getBlockSize() {
		return block.getSize();
	}

	/**
	 * Blocks-Q2: What is the Hash of the previous block?
	 * 
	 * Hint: Use method getPreviousBlockHash() in Block.java
	 * 
	 * @return hash of the previous block
	 */
	public String getPrevHash() {
		return block.getPreviousBlockHash();
	}

	/**
	 * Blocks-Q3: How many transactions are included in this block?
	 * 
	 * Hint: To get a list of transactions in a block, use method
	 * getTransactions() in Block.java
	 * 
	 * @return number of transactions in current block
	 */
	public int getTxCount() {
		return transactions.size();
	}

	/**
	 * Transactions-Q1: Find the transaction with the most outputs, and list the
	 * Bitcoin addresses of all the outputs.
	 * 
	 * Hint: To get the bitcoin address of an Output object, use method
	 * getAddress() in Output.java
	 * 
	 * @return list of output addresses
	 */
	public List<String> getOutputAddresses() {
		int max = 0;
		List<Output> out = null;

		for (Transaction t : transactions) {
			if (t.getOutputs().size() > max) {
				out = t.getOutputs();
				max = out.size();
			}
		}

		List<String> res = new ArrayList<String>();
		for (Output o : out) {
			res.add(o.getAddress());
		}

		return res;
	}

	/**
	 * Transactions-Q2: Find the transaction with the most inputs, and list the
	 * Bitcoin addresses of the previous outputs linked with these inputs.
	 * 
	 * Hint: To get the previous output of an Input object, use method
	 * getPreviousOutput() in Input.java
	 * 
	 * @return list of input addresses
	 */
	public List<String> getInputAddresses() {
		int max = 0;
		List<Input> in = null;

		for (Transaction t : transactions) {
			if (t.getInputs().size() > max) {
				in = t.getInputs();
				max = in.size();
			}
		}

		List<String> res = new ArrayList<String>();
		for (Input i : in) {
			res.add(i.getPreviousOutput().getAddress());
		}

		return res;
	}

	/**
	 * Transactions-Q3: What is the bitcoin address that has received the
	 * largest amount of Satoshi in a single transaction?
	 * 
	 * Hint: To get the number of Satoshi received by an Output object, use
	 * method getValue() in Output.java
	 * 
	 * @return number of Satoshi
	 */
	public String getLargestRcv() {
		long max = 0L;
		String res = null;
		for (Transaction t : transactions) {
			for (Output o : t.getOutputs()) {
				if (o.getValue() > max) {
					res = o.getAddress();
					max = o.getValue();
				}
			}
		}
		return res;
	}

	/**
	 * Transactions-Q4: How many coinbase transactions are there in this block?
	 * 
	 * Hint: In a coinbase transaction, getPreviousOutput() == null
	 * 
	 * @return number of coin base transactions
	 */
	public int getCoinbaseCount() {
		int res = 0;
		for (Transaction t : transactions) {
			for (Input i : t.getInputs()) {
				if (i.getPreviousOutput() == null) {
					res++;
				}
			}
		}
		return res;
	}

	/**
	 * Transactions-Q5: What is the number of Satoshi generated in this block?
	 * 
	 * @return number of Satoshi generated
	 */
	public long getSatoshiGen() {
		long res = 0L;
		for (Transaction t : transactions) {
			for (Input i : t.getInputs()) {
				if (i.getPreviousOutput() == null) {
					List<Output> out = t.getOutputs();
					for (Output o : out) {
						res += o.getValue();
					}
				}
			}
		}
		return res;
	}

}
