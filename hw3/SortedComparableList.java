import java.util.Formatter;

/**
 * SortedComparableList.java
 * A list of Comparables in ascending order.
 */
public class SortedComparableList {
	/** First element of list. */
	public Comparable head;
	/** Remaining elements of list. */
	public SortedComparableList tail;

	/** A list with head HEAD0 and tail TAIL0. */
	public SortedComparableList(Comparable head0, SortedComparableList tail0) {
		this.head = head0;
		this.tail = tail0;
	}

	/** A list with null tail, and head = 0. */
	public SortedComparableList(){
		this.head = 0;
		this.tail = null;
	}

	/** Inserts Comparable c into its correct location in this list. */
	public void insert(Comparable c) { 
		if (c != null) {
			SortedComparableList pointer = this;
			if (c.compareTo(pointer.head) < 0) {
				pointer.tail = new SortedComparableList(pointer.head, pointer.tail);
				pointer.head = c;
			}
			else {
				while (pointer.tail != null && c.compareTo(pointer.tail.head) > 0) {
					pointer = pointer.tail;
				}
				pointer.tail = new SortedComparableList(c, pointer.tail);
			}
		}
	}

	/** Returns the i-th int in this list.
	 *  The first element, which is in location 0, is the 0th element.
	 *  Assume i takes on the values [0, length of list - 1]. */
	public Comparable get(int i) {
		int j = 0;
		SortedComparableList pointer = this;
		while (j < i) {
			pointer = pointer.tail;
			j += 1;
		}
		return pointer.head;
	}

	/** Adds every item in THAT to this list. */
	public void extend(SortedComparableList that) {
		while (that != null) {
			this.insert(that.head);
			that = that.tail;
		}
	}

	/** Returns a list consisting of the elements of L starting from
		* position START, and going all the way to the END. The head of the
		* list L is the 0th element of the list.
		*
		* This method should NOT modify L. */
	public static SortedComparableList subTail(SortedComparableList L, int start) {
		SortedComparableList buildUp = new SortedComparableList();
		int i = 0;
		while (i < start) {
			L = L.tail;
			i += 1;
		}
		buildUp.extend(L);
		return buildUp.tail;
	}

	/** Returns the sublist consisting of LEN items from list L,
	 *  beginning with item START (where the first item is 0).
	 *
	 *  Does not modify the original list elements.
	 *  Assume START and END are >= 0.
	 */
	public static SortedComparableList sublist(SortedComparableList L, int start, int len) {
		SortedComparableList buildUp = subTail(L, start);
		SortedComparableList pointer = buildUp;
		int i = 0;
		if (len == 0) {
			return null;
		}
		while (i < len-1) {
			pointer = pointer.tail;
			i += 1;
		}
		pointer.tail = null;
		return buildUp;
	}

	/** Removes items from L at position len+1 and later. */
	public static void expungeTail(SortedComparableList L, int len) {
		if (L == null) {
			return;
		}
		SortedComparableList pointer = L;
		int i = 0;
		if (len == 0) {
			pointer.tail = null;
			return;
		}
		while (i < len && pointer.tail != null) {
			pointer = pointer.tail;
			i += 1;
		}
		pointer.tail = null;
	}

    /**
     *  Takes this list and, wherever two or more consecutive items are
     *  equal, it removes duplicates so that only one consecutive copy
     *  remains. No two consecutive items in this list are equals at the
     *  end of this method.
     *
     *  You can assume the list is in sorted order when this method is
     *  called.
     *
     *  For example, if the input list is [ 0 0 0 0 1 1 3 3 3 4 ], the
     *  output list is [ 0 1 3 4 ].
     **/
    public void squish() {
    	SortedComparableList pointer = this;
    	while (pointer.tail != null) {
    		if (pointer.head.compareTo(pointer.tail.head) == 0) {
    			if (pointer.tail.tail != null) {
    				pointer.tail = pointer.tail.tail;
    			}
    			else {
    				pointer.tail = null;
    			}
    		}
    		else {
    			pointer = pointer.tail;
    		}
    	}
    }

    /** Duplicates each Comparable so that for every original
     *  Comparable, there will end up being two consecutive Comparables.
     *
     *  You can assume the list is in sorted order when this method is
     *  called.
     *
     *  For example, if the input list is [ 2 3 4 7 ], the
     *  output list is [ 2 2 3 3 4 4 7 7 ].
     *
     *  NOTE: Do not try to make copies of the Comparables. Set
     *  the HEAD variable equal to the HEAD variable you are trying to
     *  duplicate.
     **/
    public void twin() {
    	SortedComparableList pointer = this;
        while (pointer != null) {
        	pointer.tail = new SortedComparableList(pointer.head, pointer.tail);
        	pointer = pointer.tail.tail;
        }
    }

	/** Returns NULL if no cycle exists, else returns cycle location. */
	private int detectCycles(SortedComparableList A) {
		SortedComparableList tortoise = A;
		SortedComparableList hare = A;
		if (A == null) {
				return 0;
		}
		int cnt = 0;
		while (true) {
				cnt++;
				if (hare.tail != null) {
						hare = hare.tail.tail;
				}
				else{
						return 0;
				}
				tortoise = tortoise.tail;
				if (tortoise == null || hare == null) {
						return 0;
				}
				if (hare == tortoise) {
						return cnt;
				}
		}
	}

	/** Returns true iff X is a SortedComparableList containing the
	 *  same sequence of Comparables as THIS. Cannot handle cycles. */
	public boolean equals(Object x) {
			if (!(x instanceof SortedComparableList)) {
					return false;
			}
			SortedComparableList L = (SortedComparableList) x;
			SortedComparableList p;
			for (p = this; p != null && L != null; p = p.tail, L = L.tail) {
					if (p.head != L.head) {
							return false;
					}
			}
			if (p != null || L != null) {
					return false;
			}
			return true;
	}

	@Override
	public String toString() {
			Formatter out = new Formatter();
			String sep;
			sep = "(";
			int cycleLocation = detectCycles(this);
			int cnt = 0;
			for (SortedComparableList p = this; p != null; p = p.tail) {
					out.format("%s%d", sep, p.head);
					sep = ", ";

					cnt++;
					if ((cnt > cycleLocation) && (cycleLocation > 0)) {
							out.format("... (cycle exists) ...");
							break;
					}
			}
			out.format(")");
			return out.toString();
	}
}