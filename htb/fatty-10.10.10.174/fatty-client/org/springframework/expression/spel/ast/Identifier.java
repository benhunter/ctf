/*    */ package org.springframework.expression.spel.ast;
/*    */ 
/*    */ import org.springframework.expression.TypedValue;
/*    */ import org.springframework.expression.spel.ExpressionState;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Identifier
/*    */   extends SpelNodeImpl
/*    */ {
/*    */   private final TypedValue id;
/*    */   
/*    */   public Identifier(String payload, int pos) {
/* 35 */     super(pos, new SpelNodeImpl[0]);
/* 36 */     this.id = new TypedValue(payload);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toStringAST() {
/* 42 */     return String.valueOf(this.id.getValue());
/*    */   }
/*    */ 
/*    */   
/*    */   public TypedValue getValueInternal(ExpressionState state) {
/* 47 */     return this.id;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/spel/ast/Identifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */