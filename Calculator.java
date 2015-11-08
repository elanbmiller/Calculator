import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class calculator {

	protected lStack<String> stack;
	protected String in = "";
	protected String out = "";
	private List<String> tokenizedPostfix=  new ArrayList<>();
	static final String OPS1 = "+-*/%(";
	static final String binOPS = "*/%)";
	static final String justBinOps = "*/%";
	static final String chars = "1234567890Xx";
	static final String allChars = "1234567890Xx+-*/%()[ ]";
	static final String OPS = "+-*/%()_";

	public calculator(String input){
		this.in = input;
		this.stack = new lStack<String>();
	}

	/*-----------------------------------------------------------------------------
	Converts String in to Postfix notation using helper method precedence
	precondition: Must have instantiated String in in the main method (done)
	postcondition: returns String value of Postfix notation of String in. Also populates
	List<String> tokenizedPostfix for later use in evaluating postfix
	-----------------------------------------------------------------------------*/

	public String conversion(){

		String accumulator = "";

		for(int i = 0; i < in.length(); i++){	
			char ch = in.charAt(i);

			switch(ch){
			case ' ': 
				break;
			case '+':
			case '-':
				int precedence = 1; 

				if(accumulator.isEmpty()){
					// Ignore unary plus
					if(ch == '+')
						break;

					ch = '_';
					precedence = 4;
				}else{
					tokenizedPostfix.add(accumulator);
					accumulator = "";
				}
				precedence("" + ch, precedence);
				break;
			case '(':
				stack.push(ch+"");
				break;
			case ')':

				if(!accumulator.isEmpty()){
					tokenizedPostfix.add(accumulator);
					accumulator = "";
				}
				while(!stack.isEmpty()){
					String temp = stack.pop();
					if(temp.equals("(")){
						break;
					}
					else{
						tokenizedPostfix.add(temp);
					}
				}
				break;

			case '*':
			case '/':
			case '%':

				tokenizedPostfix.add(accumulator);
				accumulator = "";

				precedence("" + ch, 2);
				break;
			case 'q':
			case 'Q':
				System.exit(0);
			default:
				accumulator += "" + ch;
				break;
			}
		}

		if(!accumulator.isEmpty())
			tokenizedPostfix.add(accumulator);

		while(!stack.isEmpty())
			tokenizedPostfix.add(stack.pop());

		String out = "";

		for (String s: tokenizedPostfix)
			out += " " + s;

		return out;
	}

	/*-----------------------------------------------------------------------------
	Helper method for conversion(). Decides what to do with a specific operator given its precedence. Should it 
	push to the stack or pop?

	Precondition: Infix String 'in' has been entered correctly. Receive String x and int prec as parameters
	used in deciding whether to push x to the stack or pop something from the stack before push

	Postcondition: Stack either pops an element and pushes x or simply pushes x
	-----------------------------------------------------------------------------*/

	public void precedence(String x, int prec){
		if(stack.isEmpty()){
			stack.push(x);
		}
		else{
			while(!stack.isEmpty()){
				String top = stack.peek();
				if(top.equals("(")){
					stack.push(x);
					break;
				}
				else{
					int newPrec;
					if(top.equals("+") || top.equals("-"))
						newPrec = 1;
					else
						newPrec = 2;
					if(newPrec < prec){
						stack.push(x);
						break;
					}
					else{
						tokenizedPostfix.add(stack.pop());
						stack.push(x);
						break;
					}
				}
			}
		}
	}


	public static boolean isOperator(String op){
		return  OPS.contains(op);
	}

	public static int solveExpression(String operator, int x, int y){
		int ans = 0;
		switch(operator){
		case "+": ans = x+y;
		break;
		case "-": ans = x-y;
		break;
		case "*": ans = x*y;
		break;
		case "/": ans = x/y;
		break;
		case "%": ans = x%y;
		break;
		}
		return ans;
	}


	/*-----------------------------------------------------------------------------
	Method to evaluate postfix notation.  Returns an integer value.

	Precondition: Infix has been converted to postfix and put in tokenizedPostfix. Receives 
	this tokenizedPostfix List<String>tokens. Then runs evaluate postfix algorith (iterate through tokens
	if an operator is hit, other than pop 2 ints from stack and evaluate and push them. If "_" is encountered
	append it to the top item in the stack. If an integer is hit, push it to the stack.
	Lastly, pop the integer from the stack

	Postcondition: Stack is empty and an integer value of postfix notation is received
	-----------------------------------------------------------------------------*/

	public static int evaluatePost(List<String> tokens){
		lStack<String> holder = new lStack<String>();

		for(int i = 0; i < tokens.size(); i++){

			if(isOperator(tokens.get(i))){

				//If the operator = "_"
				if( tokens.get(i).equals("_")){
					String y = holder.pop();
					holder.push(String.valueOf( - Integer.parseInt(y) ));
				}
				else{

					String y = holder.pop();
					String x = holder.pop();

					int solve = solveExpression(tokens.get(i), Integer.parseInt(x), Integer.parseInt(y));

					holder.push(String.valueOf(solve));
				}
			}

			else{
				holder.push(tokens.get(i));
			}
		}
		return Integer.parseInt(holder.pop());
	}

	/*-----------------------------------------------------------------------------
	Basic method to replace infix notation with x value entered by user

	Precondition: List<String> infix is entered by user. Also, in main method when this function
	is called, the 'y' supplied is entered by user to replace x. Then function just iterates through
	infix and replaces x or X with whatever the user has entered for x.

	Postcondition: List<String>infix has all x's replaced with a value
	-----------------------------------------------------------------------------*/


	public static void replace(List<String> infix, String y){
		for(int i = 0; i < infix.size(); i++){
			if(infix.get(i).equals("x")||infix.get(i).equals("X")){
				infix.set(i, String.valueOf(y));
			}
		}
	}

	/*-----------------------------------------------------------------------------
	Determines if infix string has illegal characters such as a + at the end or a * at the beginning

	param: String infix
	-----------------------------------------------------------------------------*/

	public static boolean hasIllegalChar(String infix){
		if(OPS1.contains(infix.substring(infix.length()-1, infix.length())) || binOPS.contains
				(infix.subSequence(0, 1)))
			return true;

		for(int i = 0; i < infix.length(); i++){
			if(!allChars.contains(infix.substring(i, i + 1))){
				return true;
			}
		}
		return false;
	}

	/*-----------------------------------------------------------------------------
	Determines whether parentheses are correct in infix expression (makes sure same amount of 
	parentheses and equal amounts of both)

	param: String infix
	-----------------------------------------------------------------------------*/

	public static boolean isCorrectParen(String infix){
		int leftParen = 0;
		int rightParen = 0;
		for(int i = 0; i < infix.length(); i++){
			if(infix.charAt(i) == '(')
				leftParen += 1;
			if(infix.charAt(i) == ')')
				rightParen += 1;
		}
		return (leftParen == rightParen);
	}




	public static void main(String[] args) {

		String input, xVal, output;
		Scanner in = new Scanner(System.in);
		while(true){
			System.out.print("Enter Infix Expression: ");
			input = in.nextLine();

			if(hasIllegalChar(input)){
				System.out.println("Error In Expression");
				System.exit(1);
			}
			if(!isCorrectParen(input)){
				System.out.println("Error In Expression");
				System.exit(1);
			}


			calculator postfix = new calculator(input);
			String thePost = postfix.conversion();

			System.out.println("Converted Expression: " + thePost);

			System.out.println("Enter a Value for X: ");
			xVal = in.nextLine();

			replace(postfix.tokenizedPostfix, xVal);

			System.out.println("Answer: " + evaluatePost(postfix.tokenizedPostfix));

		}
	}
}
