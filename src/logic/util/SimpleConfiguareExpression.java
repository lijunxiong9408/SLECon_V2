package logic.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * only support in Java 7 or larger.
 */
public class SimpleConfiguareExpression {
    public static class GrammarException extends RuntimeException {}

    public static class LexException extends RuntimeException {}

    private static class Token implements Cloneable {
        public TokenType type;
        public String data;
        public Boolean value;

        public Token(TokenType type, String data) {
            this.type = type;
            this.data = data;
        }
        
        public Token() {
        }
        
        public Token(TokenType type, Boolean value) {
            this.type = type;
            this.value = value;
        }

        public Token(TokenType type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return String.format("(%s %s)", type.name(), data);
        }
        
        @Override
        public Token clone() throws CloneNotSupportedException {
            return (Token) super.clone();
        }
    }
    
    public enum TokenType {
        TRUE("true"), 
        FALSE("false"), 
        CONSTANT("[_\\.A-Za-z0-9]+"),
        OP(">=|<=|=|>|<"), 
        OR("\\|\\|"), 
        AND("&&"), 
        NOT("!"), 
        OPENBRACKET("\\["), 
        CLOSEBRACKET("\\]"), 
        OPENPARENTHESE("\\("), 
        CLOSEPARENTHESE("\\)"),
        TERM, 
        TITLE, 
        VERSION, 
        END;
        
        private final String pattern;
        
        TokenType() {
            this(null);
        }
        TokenType(String pattern) {
            this.pattern = pattern;
        }
    }
    
    private Pattern pattern;
    private String expr;
    

    public SimpleConfiguareExpression() {
        initLexer();
    }
    
    public SimpleConfiguareExpression(String expr) {
        initLexer();
        this.inputs = tokenize(expr);
        this.expr = expr;
    }

    protected void initLexer() {
        StringBuffer tokenPatternsBuffer = new StringBuffer();
        for (TokenType tokenType : TokenType.values()) {
            if(tokenType.pattern!=null)
                tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
        }
        
        pattern = Pattern.compile(tokenPatternsBuffer.substring(1));
    }

    protected final ArrayList<Token> tokenize(String string) {
        ArrayList<Token> tokens = new ArrayList<Token>();
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) { 
            for(TokenType tokenType : TokenType.values()) {
                if(tokenType.pattern!=null && matcher.group(tokenType.name())!=null) {
                    tokens.add(new Token(tokenType, matcher.group(tokenType.name())));
                    break;
                }
            }
        }
        tokens.add(new Token(TokenType.END));
        return tokens;
    }

    
    private final Stack<Token> stack = new Stack<>();
    private List<Token> inputs;
    
    public Boolean evaluate() {
        return evaluate(null);
    }
    
    public Boolean evaluate(String string) {
        List<Token> inputs;
        if(string==null) {
            inputs = new ArrayList<>();
            for(int i=0; i<this.inputs.size(); i++) {
                try {
                    inputs.add(this.inputs.get(i).clone());
                } catch (CloneNotSupportedException e) {
                }
            }
        } else 
            inputs = tokenize(string);

        stack.clear();
        for(Token token : inputs) {
            stack.push(token);
            while(reduce());
        }
        
        if(stack.size()==1 && lastStack(0).type==TokenType.END)
            return lastStack(0).value; 
        throw new GrammarException();
    }
    
    private final Token lastStack(int index) {
        if(stack.size()-index<0)
            return new Token();
        return stack.get(stack.size()-index-1);
    }
    
    private final boolean matchStackPattern(TokenType... tokens) {
        if(stack.size() < tokens.length)
            return false;
        boolean result = true;
        for (int i=0; i<tokens.length; i++)
            result = result && lastStack(i).type==tokens[tokens.length-i-1];
        return result;
    }
    
    private void listStack() {
        boolean firstTime = true;
        for(Token token : stack) {
            if(firstTime)
                firstTime=false;
            else
                System.out.print(' ');
            System.out.print(token.type);
        }
        System.out.println();
    }
    
    private boolean reduce() {
        if(matchStackPattern(TokenType.OP, TokenType.CONSTANT)) {
            // VERSION ::= OP CONSTANT
            lastStack(0).type = TokenType.VERSION;
            return true;
        } else 
        if(matchStackPattern(TokenType.CONSTANT, TokenType.OPENBRACKET)) {
            // TITLE ::= CONSTANT OPENBRACKET
            lastStack(1).type = TokenType.TITLE;
            return true;
        }  else
        if(matchStackPattern(TokenType.TITLE, TokenType.OPENBRACKET, TokenType.OP, TokenType.VERSION, TokenType.CLOSEBRACKET)) {
            stack.pop();
            String ver = stack.pop().data;
            String op = stack.pop().data;
            stack.pop();
            String title = stack.pop().data;
            stack.push(new Token(TokenType.TERM, toValue(title, op, ver)));
            return true;
        } else
        if(matchStackPattern(TokenType.TERM, TokenType.AND, TokenType.TERM) 
                || matchStackPattern(TokenType.TERM, TokenType.OR, TokenType.TERM)) {
            // TERM AND TERM
            //   | TERM OR TERM
            boolean b = stack.pop().value;
            TokenType op = stack.pop().type;
            boolean a = stack.pop().value;
            switch(op) {
            case AND:
                stack.push(new Token(TokenType.TERM, a&&b));
                break;
            case OR:
                stack.push(new Token(TokenType.TERM, a||b));
                break;
            default:
                break;
            }
            return true;
        } else
        if(matchStackPattern(TokenType.NOT, TokenType.TERM)) {
            boolean a = stack.pop().value;
            stack.pop();
            stack.push(new Token(TokenType.TERM, !a));
            return true;
        } else
        if(matchStackPattern(TokenType.OPENPARENTHESE, TokenType.TERM, TokenType.CLOSEPARENTHESE)) {
            stack.pop();
            boolean a = stack.pop().value;
            stack.pop();
            stack.push(new Token(TokenType.TERM, a));
            return true;
        } else
        if(matchStackPattern(TokenType.TRUE)) {
            lastStack(0).type = TokenType.TERM;
            lastStack(0).value = true;
            return true;
        } else
        if(matchStackPattern(TokenType.FALSE)) {
            lastStack(0).type = TokenType.TERM;
            lastStack(0).value = false;
            return true;
        } else 
        if (matchStackPattern(TokenType.CONSTANT, TokenType.AND)
                || matchStackPattern(TokenType.CONSTANT, TokenType.OR)
                || matchStackPattern(TokenType.CONSTANT, TokenType.CLOSEPARENTHESE)
                || matchStackPattern(TokenType.CONSTANT, TokenType.END)) {
            String constant = lastStack(1).data;
            lastStack(1).value = toValue(constant);
            lastStack(1).type = TokenType.TERM;
            return true;
        } else 
        if (stack.size()==2 && matchStackPattern(TokenType.TERM, TokenType.END)) {
            stack.pop();
            lastStack(0).type = TokenType.END;
            
            return true;
        }
        
        if(matchStackPattern(TokenType.END)) {
            Token token = stack.pop();
            boolean result = reduce();
            stack.push(token);
            return result;
        }
        return false;
    }
    
    protected boolean toValue(String name, String op, String version) {
        throw new UnsupportedOperationException();
    }
    
    protected boolean toValue(String constant) {
        throw new UnsupportedOperationException();
    }

    public static void main(String... args) {
        SimpleConfiguareExpression ve = new SimpleConfiguareExpression("true");
        System.out.println(ve.evaluate());
    }
}
