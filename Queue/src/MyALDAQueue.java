/**
 * @author Angel Cardenas Martinez anca8079
 * Jobbat ihop med
 * @author Jacob Wik jawi1091 
 * @author Ken Segawa sase7636
 */

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;



/**
 * MyAldaQueue implements and Overrides methods defined in the ALDAQueue class
 */
public class MyALDAQueue<T> implements ALDAQueue<T>{

	private int modificationsCount;
	private int size;
	private int totalCapacity;
    private Node<T> first;
    private Node<T> last;
	
    
    /**
     * The MyALDAQueue Constructor instantiates and initiates: 
     * totalCapacity, size, first and last for usage
     * 
     * @throws IllegalArgumentException if capacity is negative, zero 
     * @param capacity this parameter represents the total capacity of the Queue instance 
     */
    public MyALDAQueue(int capacity) {
    	if(capacity < 1 || capacity == 0) throw new IllegalArgumentException();
    	this.totalCapacity = capacity;
    	this.size = 0;
    	this.first = null;
    	this.last = null;
        
    }
    
    /** 
     * private inner static class that instantiates a Node that holds infroamtion 
     * about itself and the next node
     * 
     * @param <T> defines the generic datatype that the structure will contain. 
     */
    private static class Node<T>{
        T data;
        Node<T> next;
        
        /**
         * Node constructor thati nstantiates the data and initiastes values
         * the next variable initiates by default to null
         * 
         * @param data represents the information contained by the node
         */
        private Node(T data) {
            this.data = data; 
            this.next = null;
        }
        
        /**
         * this method returns the Nodes contained data back to the caller.
         * 
         * @return returns the nodes data back to the caller
         */
        public T getData(){
        	return this.data;
        }
        
        /**
         * This method returns the next Node reference back to the caller
         * if the node doesn't contain a node reference the method will return a null value
         * 
         * @return returns reference to next node if exists, else null
         */
        public Node<T> getNext(){
        	return this.next;
        }
        
        
        /**
         * sets the value passed in the parameter as the next reference for this node 
         * 
         * @param nextNode contains the reference to the next value. 
         */
        public void setNext(Node<T> nextNode){
        	this.next = nextNode;
        }
    }
    
    /**
     * Implementationen baseras på sida 74 i boken
     * Delar av koden baserar sig på exemplerna som visas på sida 82 i boken. 
     */
    
    /**
     * private inner class implements the iterator class and defines the methpd's
     * Lets the user create an iterator that traverses the structures elements
     * checks if traversing is valid.
     *
     */
    private class QueueIterator implements Iterator<T>{
		
    	Node<T> prev;
    	Node<T> current;
    	int expectedModCount;
    	
    	
    	/**
    	 * QueueIterator constructor instantiates a reference to iterator of this structure
    	 * holds information regarding the first and next element in line
    	 * sets the initial value to the amount of times the data structure has been modified so far
    	 * 
    	 */
    	public QueueIterator() {
    		super(); // calls the object class to create a reference
    		prev = null;
    		current = MyALDAQueue.this.first;
    		expectedModCount = modificationsCount;
    	}
    	
    	
    	/**
    	 * while the next element holds an existing refrence the iterator will return a boolean value
    	 * it also takes care of making sure that the current caller is not hence null
    	 * 
    	 * @return returns the evaluated boolean value back to the caller
    	 */
    	@Override
		public boolean hasNext(){
//    		 if(currentNode!=null && currentNode.next!= null) return true;
//           else return false;
			 return current != null && current.getNext() != null;
			 
		}
    	
    	/**
    	 * returns a reference to the next Node in the defined sequence
    	 * @throws NoSuchElementException when the pointer to the pointer to the next Node is null
    	 * @throws ConcurrentModificationException when the amount of modifications exceeds the initial value
    	 * @return T value for usage back to the caller for use and not necessarily a Node. 
    	 */
		@Override
		public T next() {
		
			if(!hasNext()) {
				throw new NoSuchElementException();
			}
			if(modificationsCount != expectedModCount) {
				throw new ConcurrentModificationException();
			}
			
			if(prev == null){ 
				prev = current;
				return prev.getData();
			}
			current = current.next;
			T currentData = current.getData(); // Node data that will be returned.
			return currentData;
		}
		
	
		/**
		 * helper toString method used for debugging 
		 * 
		 * @return string representation of the values that are used for debugging.
		 */
		@Override
		public String toString(){
			return "current = " + this.current.getData() + " first = " + first.getData() + " last = " + last.getData();
		}
		
//		@Override
//		public void remove(){
//			if(modificationsCount != expectedModCount)
//				throw new ConcurrentModificationException();
//			
//		}
    	
    }

    /**
     * will add an element to the end of the queue
     * the first and last pointers will both point towards a single element if the queue is empty.
     * whenever an element is added the amount of modifications to the data structure will increase.
     * as the size of the elements increases in the list, the size variables increasese with it.
     * 
     * @throws IlleagalStateException if adding the data will exceed the current capacity
     * @throws NullPointerException if the data parameter is null.
     * @param data used for initializing a node that will contain the data passed in.
     */
    @Override
    public void add(T data){
    	if(this.size == this.totalCapacity) {
    		throw new IllegalStateException();
    	}else if(data == null){
    		throw new NullPointerException();
    	}
        Node<T> oldlast = this.last;
        this.last = new Node<T>(data);
        if(isEmpty()){ // if the list is empty both last and first point towards the single first element. 
        	this.first = this.last;
        	modificationsCount += 1;
        	size += 1;
        }else{
        	oldlast.setNext(this.last);
        	modificationsCount += 1;
        	size += 1;
        }
      

    }

    
    /**
     * Copies all the elements from the passed in data structure.
     * 
     * @param c collection that contains the items that will be copied over 
     */
    
    @Override
    public void addAll(Collection<? extends T> c) {
     
    	Iterator<? extends T> it = c.iterator();
    	while(it.hasNext()) {
    		T current = it.next();
    		add(current);
    	}
    }

    /**
     * will remove the first element in the list of references
     * assigns the next element after the first one to be the new first.
     * changes the amount of modifications if the elements are removed
     * 
     * @throws NoSuchElementException if the first value is null
     */
    @Override
    public T remove() {
     
    		if(this.first == null) {
    			throw new NoSuchElementException();
    		}
            T e = first.data;
            first = first.next;
            size -= 1;
            modificationsCount += 1;
            return e;
    }

    /**
     * returns the first element of the queue if the first element of the list is not null
     * otherwise a null value is returned
     * 
     * @return value stored in the front of the queue
     */
    @Override
    public T peek() {
      
        if(this.first != null) {
        	return this.first.getData();
        }else {
        	return null;
        }
    }

    
    /**
     * resets the data structure back to the initialization state.
     * modifications, size are set to 0, first and last are set to null;
     * 
     */
    @Override
    public void clear() {
      
    	this.modificationsCount = 0;
    	this.first = null;
    	this.last = first;
    	this.size = 0;
    	
    }

    
    /**
     * returns the amount of elements contained inside the list 
     * O(1) is constant time
     * 
     * @return current size of the data structure back to the caller.
     */
    @Override
    public int size() {
//    	int count = 0;
//    	Node<T> currentNode = first;
//    	for (int i = 0; i < totalCapacity; i++)
//    		if (currentNode.next != null) {
//    			count++;
//    		}
//    	return count;
    	return this.size;
    }
    
    
    /**
     * return a boolean value on whether the data structure contains any values or not. 
     * 
     * @return the state of the data structure, true if empty else false. 
     */
    @Override
    public boolean isEmpty() {
      
    	if (this.first == null) {
    		return true; 
    	}
        return false;
    }

    /**
     * checks if the amount of elements reach the total capacity of the data structure instance.
     * 
     * @return returns the evaluated value between size and total capacity, true if equal false otherwise. 
     */
    @Override
    public boolean isFull() {
    	
       
        return this.size == totalCapacity;
    }

    /**
     * returns the set size for the current instance of the data structure.
     * 
     * @return integer value of the total capacity
     */
    @Override
    public int totalCapacity() {
       
        return this.totalCapacity;
    }

    
    /**
     * return the available slots remaining for insertion
     * 
     * @return the size of the available slots.
     */
    @Override
    public int currentCapacity() {
       
        return this.totalCapacity - this.size;
    }

    


    
//    @Override
//    public int discriminate(T data) {
//        if(data == null) throw new NullPointerException("e is null, illegal argument");
//        int count = 0;
//        Node<T> temp;
//        Node<T> currentNode = first;
//        Node<T> previousNode = null;
//        if(isEmpty()) return count;
//        if(first == last && first.data.equals(data)) {
//            return count+1;
//        }
//        for (int i = 0; i < size(); i++) {
//            if(currentNode!= null) {
//                if(currentNode.data.equals(data)) {
//                    if(currentNode == first) { // om första element är discriminate
//                        count++;
//                        last.next = currentNode;
//                        temp = currentNode;
//                        remove();
//                        temp.next = null;
//                        last = temp;
//                        currentNode = first;
//                        size++;
//                    }else if(currentNode==last) {
//                        count++;
//                        return count;
//                    }else {
//                    count++;
//                    temp = currentNode;
//                    previousNode.next = temp.next;
//                    last.next = currentNode;
//                    last=currentNode;
//                    last.next=null;
//                    currentNode = previousNode.next;
//                    }
//                }
//                else {
//                        previousNode = currentNode;
//                        currentNode = currentNode.next;
//                }
//            }
//        }
//        return count;
//    }
    
//    @Override
//    public int discriminate(T e) {
//        // TODO Auto-generated method stub
//        if(e == null) throw new NullPointerException();
//    	int count = 0;
//        Node<T> temp;
//        Node<T> currentNode = first;
//        Node<T> previousNode = null;
//        if(isEmpty()) return count;
//        for (int i = 0; i < size(); i++) {
//            if(currentNode!= null) {
//                if(currentNode.data.equals(e)) {
//                    if(currentNode == first) { // om första element är discriminate
//                        count++;
//                        first = first.next;
//                        last.next = currentNode;
//                        last= currentNode;
//                        last.next = null;
//                        currentNode = first;
//                        
//                    }else if(currentNode==last) {
//                        count++;
//                        return count;
//                    }else {
//                    count++;
//                    temp = currentNode;
//                    previousNode.next = temp.next;
//                    last.next = currentNode;
//                    last=currentNode;
//                    last.next=null;
//                    currentNode = previousNode.next;
//                    //previousNode = temp;
//                    }
//                }
//                else{
//                        previousNode = currentNode;
//                        currentNode = currentNode.next;
//                }
//
//
//            }
//
//        }
//        return count;
//    }
//    private int checkDiscriminateOnNormalHead(Node<T> first, Node<T> initialLast, T e){
//    	int discriminated = 0;
//    	Node<T> current = first;
//       	while(current != null && current.next != null && current != initialLast) {
//       		if(current.next.getData() == e || current.next.getData().equals(e)){
//       			if(current.next == initialLast){
//       				discriminated += 1;
//       				return discriminated; 
//       			}
//       			discriminated += 1;
//       			last.next = current.next;
//    			last = current.next;
//    			current.next = current.next.next;
//    			last.next = null;
//       		}else {
//       			
//       			current = current.next;
//       		}
//       	}
//       	return discriminated;
//    }
//    private int checkDiscriminateOnHead(Node<T> first, Node<T> initialLast, T e){
//    	int discriminated = 0;
//    	if(this.first == null) return discriminated;
//    	if(first == last && first.data.equals(e)) return discriminated += 1;
//    	while(first != null && first.next != null) {
//    		if(first.getData() == e || first.getData().equals(e)){
//    			if(first == initialLast) {
//    				discriminated += 1;
//    				return discriminated;
//    			}
//    			discriminated += 1;
//    			last.next = first;
//    			last = first;
//    			first = first.next;
//    			last.next = null;
//    		}else {
//    			break;
//    		}
//    	}
//    	return discriminated;
//    }
    
    
    /**
	 * Move all elements equal to e to the end of the queue.
	 * 
	 * The method first checks the value to discriminate at beginning of the queue
	 * 
	 * Then, after the first element no longer points to a discriminate value
	 * the traverse continues at the last point and searches for more discriminate values.
	 * if the next element after the current Node contains the discriminate value,
	 * The pointer then points to the next next Node after current Node.
	 * 
	 * @param e element to discriminate
	 * @throws NullPointerException if e is null.
	 * @return the number of elements moved.
	 */
    public int discriminate(T e){
    	if(e == null) throw new NullPointerException();
    	int discriminated = 0;
    	Node<T> initialLast = last;
    	if(this.first == null)return discriminated;
    	if(first == last && first.data.equals(e)){
          return discriminated += 1;
    	}
    	while(first != null && first.next != null) {
    		if(first.getData() == e || first.getData().equals(e)){
    			if(first == initialLast) {
    				discriminated += 1;
    				return discriminated;
    			}
    			discriminated += 1;
    			last.next = first;
    			last = first;
    			first = first.next;
    			last.next = null;
    		}else {
    			break;
    		}
    	}
       	Node<T> current = first;
       	while(current != null && current.next != null && current != initialLast) {
       		if(current.next.getData() == e || current.next.getData().equals(e)){
       			if(current.next == initialLast){
       				discriminated += 1;
       				return discriminated; 
       			}
       			discriminated += 1;
       			last.next = current.next;
    			last = current.next;
    			current.next = current.next.next;
    			last.next = null;
       		}else {
       			
       			current = current.next;
       		}
       	}
       	return discriminated;
    }
    
// första försök
//    @Override
//    public int discriminate(T e) {
//    	int descriminated = 0;
//    	if(e == null) throw new NullPointerException();
//    	if(first == null) throw new NoSuchElementException();
//    	int amountToTraverse = size();
//    	Node<T> prev = null;
//    	Node<T> current = first;
//    	Node<T> nodeAfter = first.getNext();
//    	for(int i = 0; i < amountToTraverse; i++){
//    		if(first.getData() == e || first.getData().equals(e)){
//    			add(remove());
//    			prev = first;
//    			current = nodeAfter;
//    			if(nodeAfter != null) nodeAfter = nodeAfter.getNext();
//    		}
//    		else if(current == e || current.getData().equals(e)){
//    			add(current.getData());
//    			prev.setNext(nodeAfter);
//    		}
//    		prev = current;
//    		current = nodeAfter;
//    		if(nodeAfter != null)nodeAfter = nodeAfter.getNext();
//    	}
//    }


    /**
     * returns an instance of the implemented iterator in the data structure
     * 
     * @return return an iterator instance of QueueIterator
     */
	@Override
	public Iterator<T> iterator() {
		return new QueueIterator();
	}
	
	
	/**
	 * return a string representation of the Queue elements enclosed within brackets
	 * 
	 * @return custom string representation of the elements.
	 */
	@Override
	public String toString() {
        StringBuilder sb = new StringBuilder();
        String str;
        sb.append("[");
        Node <T> currentNode = first;
        for(int i = 0; i < size(); i++) {
            sb.append(currentNode.data);

            if(currentNode.next != null) sb.append(", ");
            currentNode=currentNode.next;
        }
        sb.append("]");
        str = sb.toString();
        return str;
    }
}