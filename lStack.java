import java.util.ArrayList;
import java.util.EmptyStackException;

public class lStack<E> {

	protected ArrayList<E> list;

	public lStack(){
		list = new ArrayList<E>();
	}

	public void push(E something){
		list.add(something);
	}

	public E pop(){
		if(!isEmpty()){
			return 	list.remove(size() - 1);
		}
		else
			throw new EmptyStackException();
	}

	public Object[] toArray(){
		return list.toArray();
	}
	
	public E peek(){
		if(!isEmpty()){
			return list.get(size() - 1);
		}
		else
			throw new EmptyStackException();
	}
	
	public E peekSpecific(int n){
		return list.get(n);
	}

	public boolean isEmpty(){
		return list.size() == 0;
	}

	public int size(){
		return list.size();
	}

	public void displayStack(){
		for(int i = size(); i > 0; i--){
			System.out.println(peekSpecific(i - 1));
		}
	}
	
	@Override
	public String toString(){
		String result = "";
		for(int i = 0; i < size(); i++){
			result += peekSpecific(i) + " ";
		}
		return result;
	}
}
