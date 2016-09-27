package com.dev.bruno.utils;

import java.util.ArrayList;
import java.util.List;

public class SimilarityUtils {

	private static List<char[]> bigram(String input) {
		List<char[]> bigram = new ArrayList<>();
		for (int i = 0; i < input.length() - 1; i++) {
			char[] chars = new char[2];
			chars[0] = input.charAt(i);
			chars[1] = input.charAt(i + 1);
			bigram.add(chars);
		}
		return bigram;
	}

	public static double dice(String input1, String input2) {
		return dice(bigram(input1), bigram(input2));
	}

	public static double dice(List<char[]> bigram1, List<char[]> bigram2) {
		List<char[]> copy = new ArrayList<char[]>(bigram2);
		int matches = 0;
		for (int i = bigram1.size(); --i >= 0;) {
			char[] bigram = bigram1.get(i);

			for (int j = copy.size(); --j >= 0;) {
				char[] toMatch = copy.get(j);
				if (bigram[0] == toMatch[0] && bigram[1] == toMatch[1]) {
					copy.remove(j);
					matches += 2;
					break;
				}
			}
		}
		return (double) matches / (bigram1.size() + bigram2.size());
	}

	public static boolean similar(String input1, String input2) {
		return dice(bigram(input1), bigram(input2)) > 0.9d;
	}
}