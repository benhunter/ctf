package org.springframework.expression;

public interface ExpressionParser {
  Expression parseExpression(String paramString) throws ParseException;
  
  Expression parseExpression(String paramString, ParserContext paramParserContext) throws ParseException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/ExpressionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */