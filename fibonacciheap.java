import java.util.HashMap;
import java.util.Set;

//create class node and its variables-data,degree,child,parent,left,right,childcut
class Node{
    int data;
    int degree;
    Node child;
    Node parent;
    Node left;
    Node right;
    boolean childcut;
};

public class fibonacciheap {
    private static Node heap=null;

    // newnode() function creates a newnode and initializes its variables
    public static Node newnode(int x){
        Node node = new Node();
        node.data=x;
        node.degree=0;
        node.child=null;
        node.parent=null;
        node.left=null;
        node.right=null;
        node.childcut=false;
        return node;
    }
    // function for inserting into fibonacci heap
    public static void insert(Node a){
        //if heap pointer is not null then insert the node into right of heap and update the circular linkedlist
        if(heap!=null) {
            a.left=heap;
            a.right=heap.right;
            heap.right.left=a;
            heap.right=a;
            if(a.data>heap.data){
                heap=a;
            }
        }
        //if heap is empty then make the inserted node to be heap
        else{
            heap = a;
            a.left = a;
            a.right = a;
        }
    }
    //function for melding nodes a,b; a is parent of b
    public static Node meld(Node a,Node b){
        // if a has child then include b in circular linked list of a
        if(a.child!=null){
            b.parent=a;
            a.degree++;
            b.left=a.child;
            b.right=a.child.right;
            a.child.right.left=b;
            a.child.right=b;

        }
        // if a has no child then make b child of a
        else{
            a.child=b;
            b.parent=a;
            a.degree++;
            b.left=b;
            b.right=b;
        }
        return a;
    }

//function for storing melded trees in a degree table
//degree table is implemented in hashmap with key as degree and value as node

    public static void degreetable(Node a, HashMap<Integer,Node> dtable){
// removing the subtree from with node a and insert it into dtable
        a.right=null;
        a.left=null;
        a.parent=null;
        a.childcut=false;
// if dtable doesnt contain the entry with same degree then insert this subtree into dtable
        if (!dtable.containsKey(a.degree)){

            dtable.put(a.degree,a);
        }
// if dtable already has a entry with same degree then meld those two trees and insert into dtable with updated degree
        else{
            Node b = dtable.get(a.degree);
            dtable.remove(b.degree);
            if(a.data>b.data)
                degreetable(meld(a,b),dtable);
            else
                degreetable(meld(b,a),dtable);
        }
    }
    // function for increasing data field of a given node
    public static void increasekey(Node key,int value){
        key.data = key.data+value;
// if key has a parent and if data in parent is less than updated data in child then call the cascading key function
        if(key.parent != null ){
            if(key.parent.data<key.data){
// call cascading cut recursively until parent of key is null
                while(key.parent!=null){
                    key =  cascadingcut(key);
                    if((key.childcut == false)){
                        key.childcut=true;
                        break;
                    }
                }
            }
        }
// if key is in the top node list then check and update heap if required
        else{
            if(key.data>heap.data)
                heap=key;
        }
    }

    // function for extracting max of the heap
    public static Node removemax(){
        // using a new hashmap-dtable for storing unique degree keys
        HashMap<Integer, Node> dtable = new HashMap<Integer, Node>();
        Node a;
        Node b = heap;
        b=b.right;
// add all top level nodes to dtable
        if(heap!=null){
            while(b!=heap){
                a=b;
                b=b.right;
                degreetable(a,dtable);
            }
        }

        b = heap.child;
        if(b!=null){
            a=b;
            b=b.right;
            degreetable(a,dtable);
        }
        while(b!=heap.child && heap.child!=null){
            a=b;
            b=b.right;
            degreetable(a,dtable);
        }
        a=heap;
        heap=null;
// construct fibonacci heap using entries of dtable
        Set<Integer> Keys = dtable.keySet();
        for(Integer key : Keys){
            insert(dtable.get(key));
        }
// returning heapnode
        return a;

    }
    // function for implementing cascadingcut in fibonacci heap
    public static Node cascadingcut(Node a){
        Node p=a.parent;
        if(p.degree==1){
            p.child=null;
            p.degree--;
            a.left=null;
            a.right=null;
            a.parent=null;
            a.childcut=false;
        }

        else{
            a.right.left=a.left;
            a.left.right=a.right;
            if(p.child.data==a.data){
                p.child=a.right;
            }
            p.degree--;
            a.left=null;
            a.right=null;
            a.parent=null;
            a.childcut=false;


        }
//inserting back the removed subtree
        insert(a);
        return p;
    }
}
