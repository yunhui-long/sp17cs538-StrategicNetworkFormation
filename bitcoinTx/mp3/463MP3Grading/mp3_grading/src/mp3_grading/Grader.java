package mp3_grading;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Grader {

	public static String GradeCp1(String sub, String ans) {
		StringBuilder res = new StringBuilder();

		try {
			BufferedReader rsub = new BufferedReader(new FileReader(sub));
			BufferedReader rans = new BufferedReader(new FileReader(ans));

			String subline = "";
			String ansline = "";

			while (!subline.equals("3.1.2") && ((subline = rsub.readLine()) != null))
				;
			subline = rsub.readLine();

			while (!ansline.equals("3.1.2") && ((ansline = rans.readLine()) != null))
				;
			ansline = rans.readLine();

			res.append("3.1.2\n");

			for (int i = 1; i <= 3; i++) {
				if (ansline.trim().equals(subline.trim())) {
					res.append(i + ".correct\n");
				} else {
					res.append("Answer: " + subline.trim() + " Correct answer: " + ansline + "\n");
				}
				ansline = rans.readLine();
				subline = rsub.readLine();
			}

			while (!subline.equals("3.1.3") && ((subline = rsub.readLine()) != null))
				;
			subline = rsub.readLine();

			while (!ansline.equals("3.1.3") && ((ansline = rans.readLine()) != null))
				;
			ansline = rans.readLine();

			res.append("3.1.3\n");

			while (!subline.substring(0,2).equals("1.") && ((subline = rsub.readLine()) != null))
				;
			subline = rsub.readLine();

			while (!ansline.substring(0,2).equals("1.") && ((ansline = rans.readLine()) != null))
				;
			ansline = rans.readLine();

			Set<String> answers1 = new HashSet<String>();
			while (!ansline.substring(0, 2).equals("2.")) {
				answers1.add(ansline);
				ansline = rans.readLine();
			}

			Set<String> sub1 = new HashSet<String>();
			while (!subline.substring(0, 2).equals("2.")) {
				sub1.add(subline);
				subline = rsub.readLine();
			}

			if (answers1.equals(sub1)) {
				res.append("2.correct\n");
			} else {
				res.append("2.");
				for (String s : answers1) {
					res.append(s + " ");
				}
				res.append("\n");
			}

//			while (!ansline.substring(0,2).equals("2.") && ((ansline = rans.readLine()) != null))
//				;
//			ansline = rans.readLine();

			Set<String> answers2 = new HashSet<String>();
			while (!ansline.substring(0, 2).equals("3.")) {
				answers1.add(ansline);
				ansline = rans.readLine();
			}

			Set<String> sub2 = new HashSet<String>();
			while (!subline.substring(0, 2).equals("3.")) {
				sub1.add(subline);
				subline = rsub.readLine();
			}

			if (answers2.equals(sub2)) {
				res.append("3.correct\n");
			} else {
				res.append("3.");
				for (String s : answers1) {
					res.append(s + " ");
				}
				res.append("\n");
			}

			for (int i = 3; i <= 5; i++) {
				if (ansline.trim().equals(subline.trim())) {
					res.append(i + ".correct\n");
				} else {
					res.append("Answer: " + subline.trim() + " Correct answer: " + ansline + "\n");
				}
				ansline = rans.readLine();
				subline = rsub.readLine();
			}

			rsub.close();
			rans.close();

		} catch (IOException e) {
			return "File not found";
		}

		return res.toString();
	}

	public static String GradeCp2(String sub, String ans) {
		StringBuilder res = new StringBuilder();

		try {
			BufferedReader rsub = new BufferedReader(new FileReader(sub));
			BufferedReader rans = new BufferedReader(new FileReader(ans));

			String ansline = rans.readLine();
			String subline = rsub.readLine();

			for (int i = 1; i <= 2; i++) {
				System.out.println(subline);
				System.out.println(ansline);
				if (ansline.trim().equals(subline.trim())) {
					res.append(i + ".correct\n");
				} else {
					res.append("Answer: " + subline.trim() + " Correct answer: " + ansline + "\n");
				}
				ansline = rans.readLine();
				subline = rsub.readLine();
			}

			rsub.close();
			rans.close();

		} catch (IOException e) {
			return "File not found";
		}

		return res.toString();
	}

	public static Set<String> readKeyMap(String file) {
		Set<String> keyMap = new HashSet<String>();
		int total = 0;

		try {
			BufferedReader rans = new BufferedReader(new FileReader(file));

			String ansline;
//			Map<Integer, Integer> indexMap = new HashMap<Integer, Integer>();

			while ((ansline = rans.readLine()) != null) {
				String[] s = ansline.split(" ");
				if (s.length < 2) {
					continue;
				}
//				Integer i = Integer.valueOf(s[1]);
//				int newi = 0;
//				if (indexMap.containsKey(i)) {
//					newi = indexMap.get(i);
//				} else {
//					newi = total;
//					indexMap.put(i, total);
//					total++;
//				}

				keyMap.add(s[0]);
			}

			rans.close();
		} catch (IOException e) {
			return keyMap;
		}
		return keyMap;
	}

	public static String GradeKeyMap(String sub, String ans) {
		StringBuilder res = new StringBuilder();

		Set<String> subMap = readKeyMap(sub);
		Set<String> ansMap = readKeyMap(ans);
		
		boolean correct = true;
		for (String s:ansMap) {
			if (!subMap.contains(s)) {
				correct = false;
				res.append("Missing " + s + " for in keyMap.txt \n");
			}
//			if(!subMap.get(s).equals(ansMap.get(s))) {
//				correct = false;
//				res.append(s + " " + ansMap.get(s)+ "\n");
//			}
		}

		if (correct) {
			res.append("correct");
		}
		
		return res.toString();
	}
	

	public static void main(String[] args) {
		if (args.length < 3) {
			System.out.println("Usage: [submission_folder] [answer_folder] [grade_folder]");
			System.exit(1);
		}

		String sub = args[0];
		String ans = args[1];
		String grade = args[2];

		String subcp1 = sub + "/cp1.txt";
		String anscp1 = ans + "/cp1.txt";
		String subcp2 = sub + "/cp2.txt";
		String anscp2 = ans + "/cp2.txt";
		String subKeyMap = sub + "/keyMap.txt";
		String ansKeyMap = ans + "/keyMap.txt";
		String subUserMap = sub + "/userMap.txt";
		String ansUserMap = ans + "/userMap.txt";

		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(grade));
			w.write("=============cp1.txt===========");
			w.newLine();
			w.write(Grader.GradeCp1(subcp1, anscp1));
			w.newLine();

			w.write("=============cp2.txt===========");
			w.newLine();
			w.write(Grader.GradeCp2(subcp2, anscp2));
			w.newLine();

			w.flush();
			w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
