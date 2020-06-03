/*    */ package org.springframework.expression.spel.ast;
/*    */ 
/*    */ import org.springframework.asm.MethodVisitor;
/*    */ import org.springframework.expression.TypedValue;
/*    */ import org.springframework.expression.spel.CodeFlow;
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
/*    */ public class NullLiteral
/*    */   extends Literal
/*    */ {
/*    */   public NullLiteral(int pos) {
/* 32 */     super((String)null, pos);
/* 33 */     this.exitTypeDescriptor = "Ljava/lang/Object";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypedValue getLiteralValue() {
/* 39 */     return TypedValue.NULL;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 44 */     return "null";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCompilable() {
/* 49 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 54 */     mv.visitInsn(1);
/* 55 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/spel/ast/NullLiteral.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */