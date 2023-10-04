import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * 
 * @author samuel vossen
 *
 */
public class Assignment5 {
	Random rn = new Random();
	static int[] random;

	public Assignment5() {
		this.rn = rn;
		this.random = new int[20];
	}

	/**
	 * this method creates a randomly generated page string of length length, and
	 * each number is between 0 and range
	 * 
	 * @param length
	 * @param range
	 */
	public void createRefString(int length, int range) {
		for (int i = 0; i < length - 1; i++) {
			random[i] = rn.nextInt(range);
		}
	}

	/**
	 * this method finds the number of page faults with first in first out
	 * 
	 * @param pages
	 * @param size
	 * @param cacheSize
	 * @return the number of page faults using first in first out
	 */
	static int FIFO(int pages[], int size, int cacheSize) {
		HashSet<Integer> hash = new HashSet<>(cacheSize);
		Queue<Integer> indexes = new LinkedList<>();
		int page_faults = 0;
		for (int i = 0; i < size; i++) {
			if (hash.size() < cacheSize) {
				if (!hash.contains(pages[i])) {
					hash.add(pages[i]);
					page_faults++;
					indexes.add(pages[i]);
				}
			} else {
				if (!hash.contains(pages[i])) {
					int val = indexes.peek();
					indexes.poll();
					hash.remove(val);
					hash.add(pages[i]);
					indexes.add(pages[i]);
					page_faults++;
				}
			}
		}

		return page_faults;
	}

	/**
	 * this method determines the number of page faults using a least recently used
	 * algorithm
	 * 
	 * @param pages
	 * @param size
	 * @param cacheSize
	 * @return the number of page faults
	 */
	static int LRU(int pages[], int size, int cacheSize) {
		HashSet<Integer> hash = new HashSet<>(cacheSize);
		HashMap<Integer, Integer> indexes = new HashMap<>();
		int page_faults = 0;
		for (int i = 0; i < size; i++) {
			if (hash.size() < cacheSize) {
				if (!hash.contains(pages[i])) {
					hash.add(pages[i]);
					page_faults++;
				}
				indexes.put(pages[i], i);
			} else {
				if (!hash.contains(pages[i])) {
					int lru = Integer.MAX_VALUE;
					int value = Integer.MIN_VALUE;
					Iterator<Integer> itr = hash.iterator();
					while (itr.hasNext()) {
						int temp = itr.next();
						if (indexes.get(temp) < lru) {
							lru = indexes.get(temp);
							value = temp;
						}
					}
					hash.remove(value);
					indexes.remove(value);
					hash.add(pages[i]);
					page_faults++;
				}
				indexes.put(pages[i], i);
			}
		}

		return page_faults;
	}

	/**
	 * this method finds the frame that will not be used in the near future
	 * 
	 * @param page
	 * @param frame
	 * @param pageN
	 * @param index
	 * @return the frame that will not be used in the near future.
	 */
	static int determine(int page[], int[] frame, int pageN, int index) {
		int soon = -1, furthest = index;
		for (int i = 0; i < frame.length; i++) {
			int j;
			for (j = index; j < pageN; j++) {
				if (frame[i] == page[j]) {
					if (j > furthest) {
						furthest = j;
						soon = i;
					}
					break;
				}
			}
			if (j == pageN)
				return i;
		}
		return (soon == -1) ? 0 : soon;
	}

	/**
	 * this method checks to see if a page exists in the cache
	 * 
	 * @param specificPage
	 * @param x
	 * @return
	 */
	static boolean find(int specificPage, int[] cache) {
		for (int i = 0; i < cache.length; i++)
			if (cache[i] == specificPage)
				return true;
		return false;
	}

	/**
	 * this method finds the number of page faults using an optimal algorithm
	 * 
	 * @param page
	 * @param pageN
	 * @param NumberOfFrames
	 * @return number of page faults
	 */
	static int OP(int page[], int pageN, int NumberOfFrames) {
		int[] frame = new int[NumberOfFrames];
		int hit = 0;
		int index = 0;
		for (int i = 0; i < pageN; i++) {
			if (find(page[i], frame)) {
				hit++;
				continue;
			}
			if (index < NumberOfFrames)
				frame[index++] = page[i];
			else {
				int j = determine(page, frame, pageN, i + 1);
				frame[j] = page[i];
			}
		}
		return pageN - hit;
	}

	/**
	 * this method turns an array into a string
	 * 
	 * @param array
	 * @return a string version of an array
	 */
	public static String toStringArray(int[] array) {
		String string = array[0] + "";
		for (int i = 1; i < 20; i++) {
			string = string + "," + array[i];
		}
		return string;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Samuel vossen, 3.28.2023, Assignment 5");
		Assignment5 run = new Assignment5();
		run.createRefString(20, 10);
		int[] y = { 0, 7, 0, 1, 2, 0, 8, 9, 0, 3, 0, 4, 5, 6, 7, 0, 8, 9, 1, 2 };
		int[] z = { 7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2, 1, 2, 0, 1, 7, 0, 1 };
		for (int i = 1; i < 8; i++) {
			System.out.println(
					"For " + i + " page frames, and using string page reference string " + toStringArray(run.random));

			System.out.println("FIFO had " + FIFO(run.random, 20, i) + " page faults");
			System.out.println("LRU had " + LRU(run.random, 20, i) + " page faults");
			System.out.println("Optimal had " + OP(run.random, 20, i) + " page faults");
			System.out.println("");
		}
		System.out.println("");
		for (int i = 1; i < 8; i++) {
			System.out.println("For " + i + " page frames, and using string page reference string " + toStringArray(y));
			System.out.println("FIFO had " + FIFO(y, 20, i) + " page faults");
			System.out.println("LRU had " + LRU(y, 20, i) + " page faults");
			System.out.println("Optimal had " + OP(y, 20, i) + " page faults");
			System.out.println("");
		}
		System.out.println("");
		for (int i = 1; i < 8; i++) {
			System.out.println("For " + i + " page frames, and using string page reference string " + toStringArray(z));
			System.out.println("FIFO had " + FIFO(z, 20, i) + " page faults");
			System.out.println("LRU had " + LRU(z, 20, i) + " page faults");
			System.out.println("Optimal had " + OP(z, 20, i) + " page faults");
			System.out.println("");
		}
	}

}
