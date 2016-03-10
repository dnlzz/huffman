import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.PriorityQueue;

class Node implements Comparable<Node> {
	public int data;
	public int freq;
	public Node left, right;

	public Node(int newData, int newFreq, Node newLeft, Node newRight) {
		data = newData;
		freq = newFreq;
		left = newLeft;
		right = newRight;
	}

	public boolean isLeaf() {
		return (left == null) && (right == null);
	}

	@Override
	public int compareTo(Node o) {
		// TODO Auto-generated method stub
		return this.freq - o.freq;
	}
}


public class binTest {

	final static int NUM_CHAR = 256;

	private static void encodeFile(String fn) {
		try {
			byte[] bytes = Files.readAllBytes(Paths.get(fn));
			int[] freq = new int[NUM_CHAR];
			int[] charLocs = new int[bytes.length];
			for (int i = 0; i < bytes.length; i++)
				if (bytes[i] > 0)
					freq[bytes[i]]++;
				else {
					byte b = bytes[i]; //element in the byte array read from stream
					int d = b & 0xFF;
					freq[d]++;
				}

			int z = 0;
			for (int i=0; i < freq.length; i++) {
				if (freq[i] > 0) {	
					charLocs[z] = i;
					z++;
				}
			}
			
			Node root = buildMinHeap(charLocs, freq);
	        
			String[] st = new String[NUM_CHAR];
			
	        buildCode(st, root, "");
	        
	        writeTree(root);

	        System.out.println("Before: " + bytes.length * 8 + " bits");
	        
	        for (int i = 0; i < bytes.length; i++) {
	            if (bytes[i] > 0) {
	        	String code = st[bytes[i]];
	        	System.out.print(code + "  :  ");
	               	 /* 
		            for (int j = 0; j < code.length(); j++) {
		                if (code.charAt(j) == '0') {
		                	//System.out.print("0");
		                }
		                else if (code.charAt(j) == '1') {
		                	//System.out.print("1");
		                }
		                else throw new IllegalStateException("Illegal state");
		            }
          */
	            }
	        }
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
			

    private static Node buildMinHeap(int[] charLocs, int[] freq) {

        // need to implement minHeap PQueue
        PriorityQueue<Node> pq = new PriorityQueue<Node>();
        
        int j = 0;
        for (char i = 0; i < NUM_CHAR; i++)
            if (freq[i] > 0) {
            	//System.out.println(charLocs[j]);
                pq.add(new Node(charLocs[j], freq[i], null, null));
                j++;
            }
       
        
        // merge two smallest trees
        while (pq.size() > 1) {
            Node left  = pq.poll();
            Node right = pq.poll();
            Node parent = new Node(left.data + right.data, left.freq + right.freq, left, right);
            pq.add(parent);
        }
        return pq.poll();
    }
	
    private static void writeTree(Node x) {
        if (x.isLeaf()) {
            //System.out.println(x.data + " : " + x.freq);
            return;
        }
        writeTree(x.left);
        writeTree(x.right);
    }
    
    // make a lookup table from symbols and their encodings
    private static void buildCode(String[] st, Node x, String s) {
        if (!x.isLeaf()) {
            buildCode(st, x.left,  s + '0');
            buildCode(st, x.right, s + '1');
        }
        else {
            st[x.data] = s;
            
        }        
    }
	
	public static void main(String[] args) {

		//need to use cmd line args***
		//encodeFile("prog2test.txt");
		encodeFile("house-06.jpg");

		/*
		 *        
		if (args[0].equals("henc"))
			encodeFile(args[1]);
        else if (args[0].equals("hdec"))
        	decodeFile(args[1]);
        else
        	throw new IllegalArgumentException("Illegal command line argument");
		 * 
		 * */
	}


}