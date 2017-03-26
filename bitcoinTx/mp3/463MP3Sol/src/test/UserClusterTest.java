package test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import main.UserCluster;

public class UserClusterTest {
	public static void main(String[] args) {
		UserCluster uc = new UserCluster();
		uc.readTransactions("transactions-test.txt");
		uc.mergeAddresses();
		if (!uc.writeKeyMap("keyMap-test.txt")) {
			System.err.println("Cannot write to keyMap.txt!");
		}
		if (!uc.writeUserMap("userMap-test.txt")) {
			System.err.println("Cannot write to userMap.txt!");
		}
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(
					"cp2-test.txt"));
			w.write("1. " + uc.getUserNumber() + "\n");
			w.write("2. " + uc.getLargestClusterSize() + "\n");
			w.flush();
			w.close();
		} catch (IOException e) {
			System.err.println("Cannot write to cp2.txt!");
			e.printStackTrace();
		}
		
		if (!uc.writeUserGraph("transactions-test.txt", "userGraph-test.txt")){
			System.err.println("Cannot write user graph!");
		}

	}
}
