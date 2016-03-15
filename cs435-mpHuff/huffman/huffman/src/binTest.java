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
		return this.freq - o.freq;
	}
}


public class binTest {

	final static int NUM_CHAR = 256;
	static Node root;
	static String output;
	static String decOut;
	
	private static String decodeFile(String fn) {
		decOut = "";
		String outStr = traverse(root, fn);
		return outStr;	
	}
	

	private static String encodeFile(String fn) throws Exception {
		try {
			byte[] bytes = Files.readAllBytes(Paths.get(fn));
			int[] freq = new int[NUM_CHAR];
			int[] charLocs = new int[bytes.length];
			for (int i = 0; i < bytes.length; i++)
				if (bytes[i] > 0)
					freq[bytes[i]]++;
				else {
					byte b = bytes[i];
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
			
			root = buildMinHeap(charLocs, freq);
	        
			String[] st = new String[NUM_CHAR];
			
			buildCode(st, root, "");
	        
	        writeTree(root);

	        System.out.println("Before: " + bytes.length * 8 + " bits");
	        	        
	        int count = 0;
	        output="";
	        
	        for (int i = 0; i < bytes.length; i++) {
	            if (bytes[i] > 0) {
	        	String code = st[bytes[i]];
	        	System.out.print(code);
	        	output += code;
	        	count += code.length();
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
	        System.out.println("\nAfter: " + count + " bits");
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
		return output;
	}
			

	public static String traverse (Node r, String fn){ // Each child of a tree is a root of its subtree.
		
		Node tmp = root;
		
		for (int i = 0; i < fn.length(); i++) {
			
			if (fn.charAt(i) == '0'){
		        tmp = tmp.left;
		    }		
		    
		    if (fn.charAt(i) == '1'){
		    	tmp = tmp.right;
		    }
		    
		    if (tmp.isLeaf()) {
		    	decOut+=charCodetoStr(tmp.data);
		    	tmp = root;
		    }
			
		}
		

		
	    
	    return decOut;
	}
	
	
    private static String charCodetoStr(int data) {
    	return Character.toString ((char) data);
	}


	private static Node buildMinHeap(int[] charLocs, int[] freq) throws Exception {

        // need to implement minHeap PQueue
        //PriorityQueue<Node> pq = new PriorityQueue<Node>();
		heapPQueue<Node> pq = new heapPQueue<Node>();
        
        int j = 0;
        for (char i = 0; i < NUM_CHAR; i++)
            if (freq[i] > 0) {
            	//System.out.println(charLocs[j]);
                pq.add(new Node(charLocs[j], freq[i], null, null));
                j++;
            }
       
        
        // merge two smallest trees
        while (pq.size() > 1) {
            Node left  = (Node) pq.remove();
            Node right = (Node) pq.remove();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.add(parent);
        }
        return (Node) pq.remove();
    }
	
    private static void writeTree(Node x) {
        if (x.isLeaf()) {
            //System.out.println(x.data);
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
	
	public static void main(String[] args) throws Exception {

		//need to use cmd line args***
		String enc=encodeFile("prog2test.txt");
		//String enc =encodeFile("house-06.jpg");
		System.out.println("ENC:  " + enc);
		String dec = decodeFile(enc);
		System.out.println("DEC:  " + dec);
		

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