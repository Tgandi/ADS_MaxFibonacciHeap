import java.util.*;
import java.io.*;



public class keywordcounter{

    public static void main(String[] args){

        fibonacciheap fbheap = new fibonacciheap();
        String  inputfile = args[0];
  // hashmap is to store keyword, node with key as keyword
  // hashmap3 is to store keyword, node with key as node
        HashMap<String, Node> hashmap = new HashMap<String, Node>();
        HashMap<Node, String> hashmap3 = new HashMap<Node, String>();

        File outfile = new File("output_file.txt");
        BufferedWriter fileout = null;
        try {

            BufferedReader br = new BufferedReader(new FileReader(inputfile));
            fileout = new BufferedWriter(new FileWriter(outfile));
            String line ;
    // read everyline as string split by space
            while (( line = br.readLine()) != null) {
                String[] read = line.split("\\s+");
                if(read[0].equalsIgnoreCase("stop") ){
                    System.out.println("Success");
                    return;
                }
    // if hashmap doesnt the keyword then insert it into hashmap
    // if first character is not $ then perform removemax
                if (!hashmap.containsKey(read[0])) {
                    if (read[0].contains("$")) {
                        Node node=fbheap.newnode(Integer.parseInt(read[1]));
                        fbheap.insert(node);
                        hashmap.put(read[0], node);
                        hashmap3.put(node,read[0]);
                    }
           //performing remove max and storing the nodes
                    else {
                        HashMap<Integer, Node> hashmap2 = new HashMap<Integer, Node>();
                        int op = Integer.parseInt(read[0]);
                        while(op>0){
                            Node temp = fbheap.removemax();
                            temp.degree=0;
                            temp.childcut=false;
                            temp.right=null;
                            temp.left=null;
                            temp.child=null;
                            Set<String> Keys = hashmap.keySet();
                            fileout.write(hashmap3.get(temp).substring(1));
                            if(op>1){
                                fileout.write(",");
                            }

//                            for(String key : Keys){
//                                if(hashmap.get(key) == temp){
//                                    fileout.write(key.substring(1));
//                                    fileout.write(",");
//                                };
//                            }
                            hashmap2.put(op,temp);
                            op--;

                        }
            // inserting back the removed nodes stored in hashmap2
                        fileout.write("\n");
                        Set<Integer> keyword = hashmap2.keySet();
                        for(Integer key : keyword){
                            fbheap.insert(hashmap2.get(key));
                        }
                    }

                } else {
                    Node p = hashmap.get(read[0]);
                    fbheap.increasekey(p, Integer.parseInt(read[1]));
                }
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally{
            if (fileout != null) {
                try {
                    fileout.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
