/*    */ package org.springframework.expression.spel.ast;
/*    */ 
/*    */ import org.springframework.asm.MethodVisitor;
/*    */ import org.springframework.expression.TypedValue;
/*    */ import org.springframework.expression.spel.CodeFlow;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ public class StringLiteral
/*    */   extends Literal
/*    */ {
/*    */   private final TypedValue value;
/*    */   
/*    */   public StringLiteral(String payload, int pos, String value) {
/* 37 */     super(payload, pos);
/*    */     
/* 39 */     String valueWithinQuotes = value.substring(1, value.length() - 1);
/* 40 */     valueWithinQuotes = StringUtils.replace(valueWithinQuotes, "''", "'");
/* 41 */     valueWithinQuotes = StringUtils.replace(valueWithinQuotes, "\"\"", "\"");
/*    */     
/* 43 */     this.value = new TypedValue(valueWithinQuotes);
/* 44 */     this.exitTypeDescriptor = "Ljava/lang/String";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypedValue getLiteralValue() {
/* 50 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 55 */     return "'" + getLiteralValue().getValue() + "'";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCompilable() {
/* 60 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 65 */     mv.visitLdcInsn(this.value.getValue());
/* 66 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/spel/ast/StringLiteral.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */