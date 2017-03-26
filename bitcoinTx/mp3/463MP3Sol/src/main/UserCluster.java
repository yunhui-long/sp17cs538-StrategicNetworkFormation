package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserCluster {
	private Map<Long, List<String>> userMap; // Map a user id to a list of
												// bitcoin addresses
	private Map<String, Long> keyMap; // Map a bitcoin address to a user id
	private HashMap<Long, List<String>> inputTx; // user address,
	// transactionIndex
private List<String> outputAddr;

	public UserCluster() {
		inputTx = new HashMap<Long, List<String>>();
		outputAddr = new ArrayList<String>();
		userMap = new HashMap<Long, List<String>>();
		keyMap = new HashMap<String, Long>();
	}

	
	public void addInputAddress(String addr, long txIndex) {
		List<String> list;
		if ((list = inputTx.get(txIndex)) != null) {
			list.add(addr);
		} else {
			list = new ArrayList<String>();
			list.add(addr);
			inputTx.put(txIndex, list);
		}
	}

	public void addOutputAddress(String addr) {
		outputAddr.add(addr);
	}

	/**
	 * Read transactions from file
	 * 
	 * @param file
	 * @return true if read succeeds; false otherwise
	 */
	public boolean readTransactions(String file) {
		try {
			BufferedReader r = new BufferedReader(new FileReader(file));
			String nextLine;
			while ((nextLine = r.readLine()) != null) {
				String[] s = nextLine.split(" ");
				if (s.length < 5) {
					continue;
				}
				if (s[4].equals("in")) {
					addInputAddress(s[2], Long.valueOf(s[0]));
				} else if (s[4].equals("out")) {
					addOutputAddress(s[2]);
				} else {
					System.err.println("Unrecognized transaction type!");
					r.close();
					return false;
				}
			}
			r.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Merge addresses based on joint control
	 */
	public void mergeAddresses() {
		long currentUser = 0;
		// single-link process: create 'edge' (tuple) between a seed and each
		// other pubkey in transaction
		for (long txId : inputTx.keySet()) {
			List<String> inputs = inputTx.get(txId);
			String key1 = inputs.get(0);

			if (inputs.size() > 1) {
				for (int i = 1; i < inputs.size(); i++) {
					String key2 = inputs.get(i);
					// key1, key 2 is in keyMap
					boolean b1 = keyMap.keySet().contains(key1);
					boolean b2 = keyMap.keySet().contains(key2);
					if (!b1 && !b2) { // neither exists: add new group
						keyMap.put(key1, currentUser);
						keyMap.put(key2, currentUser);
						List<String> keys = new ArrayList<String>();
						keys.add(key1);
						keys.add(key2);
						userMap.put(currentUser, keys);
						System.out.println("add user " + currentUser);
						currentUser++;
					} else if (!b1 && b2) { // key2 exists, assign key1 = key2
						long user = keyMap.get(key2);
						keyMap.put(key1, user);
						userMap.get(user).add(key1);
					} else if (b1 && !b2) { // key1 exists, assign key2 = key1
						long user = keyMap.get(key1);
						keyMap.put(key2, user);
						System.out.println("get user " + user);
						userMap.get(user).add(key2);
					} else if (b1 && b2) { // both exists
						long u1 = keyMap.get(key1);
						long u2 = keyMap.get(key2);
						if (u1 != u2) {
							// not in the same group, join the group, assign the
							// minimum as the joined
							long min = Math.min(u1, u2);
							long max = Math.max(u1, u2);
							// move users from max to min, this is why we need
							// to maintain userMap, so we can reassign keys
							// quickly
							List<String> maxkeys = userMap.get(max);
							List<String> minkeys = userMap.get(min);
							for (String k : maxkeys) {
								keyMap.put(k, min);
								minkeys.add(k);
							}
							userMap.put(min,minkeys);
							userMap.remove(max);
							System.out.println("remove user " + max);
						}
					}
				}
			} else {
				if (!keyMap.keySet().contains(key1)) {
					// seed not in the keyMap, add seed
					keyMap.put(key1, currentUser);
					List<String> keys = new ArrayList<String>();
					keys.add(key1);
					userMap.put(currentUser, keys);
					currentUser++;
				}
			}
		}
		
		for (String out : outputAddr) {
			if(keyMap.containsKey(out)){
				continue;
			} else {
				List<String> keys = new ArrayList<String>();
				keys.add(out);
				userMap.put(currentUser, keys);
				keyMap.put(out, currentUser);
				currentUser++;
			}
		}

	}
	

	/**
	 * Return number of users (i.e., clusters) in the transaction dataset
	 * 
	 * @return number of users (i.e., clusters)
	 */
	public int getUserNumber() {
		return userMap.size();
	}

	/**
	 * Return the largest cluster
	 * 
	 * @return addresses in the largest cluster
	 */
	public int getLargestClusterSize() {
		int res = 0;
		for (Long u : userMap.keySet()) {
			if (userMap.get(u).size() > res) {
				res = userMap.get(u).size();
			}
		}
		
		return res;
	}

	public boolean writeUserMap(String file) {
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(file));
			for (long user : userMap.keySet()) {
				List<String> keys = userMap.get(user);
				w.write(user + " ");
				for (String k : keys) {
					w.write(k + " ");
				}
				w.newLine();
			}
			w.flush();
			w.close();
		} catch (IOException e) {
			System.err.println("Error in writing user list!");
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean writeKeyMap(String file) {
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(file));
			for (String key : keyMap.keySet()) {
				w.write(key + " " + keyMap.get(key) + "\n");
			}
			w.flush();
			w.close();
		} catch (IOException e) {
			System.err.println("Error in writing key map!");
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean writeUserGraph(String txFile, String userGraphFile) {
		try {
			BufferedReader r1 = new BufferedReader(new FileReader(txFile));
			Map<String, Long> txUserMap = new HashMap<String, Long>();
			String nextLine;
			while ((nextLine = r1.readLine()) != null) {
				String[] s = nextLine.split(" ");
				if (s.length < 5) {
					System.err.println("Invalid format: " + nextLine);
					r1.close();
					return false;
				}
				if (s[4].equals("in") && !txUserMap.containsKey(s[0])) { // new transaction 
					Long user;
					if ((user=keyMap.get(s[2])) == null) {
						System.err.println(s[2] + " is not in the key map!");
						r1.close();
						return false;
					}
					txUserMap.put(s[0], user);
				} 
			}
			r1.close();
			
			BufferedReader r2 = new BufferedReader(new FileReader(txFile));
			BufferedWriter w = new BufferedWriter(new FileWriter(userGraphFile));
			while ((nextLine = r2.readLine()) != null) {
				String[] s = nextLine.split(" ");
				if (s.length < 5) {
					System.err.println("Invalid format: " + nextLine);
					r2.close();
					w.flush();
					w.close();
					return false;
				}
				if (s[4].equals("out")) {
					if(txUserMap.get(s[0]) == null) {
						System.err.println("Did not find input transaction for Tx: " + s[0]);
						r2.close();
						w.flush();
						w.close();
						return false;
					}
					long inputUser = txUserMap.get(s[0]);
					Long outputUser;
					if ((outputUser=keyMap.get(s[2])) == null) {
						System.err.println(s[2] + " is not in the key map!");
						r2.close();
						w.flush();
						w.close();
						return false;
					}
					w.write(inputUser + "," + outputUser + "," + s[3] + "\n");
				} 
			}
			r2.close();
			w.flush();
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

}
