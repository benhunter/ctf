/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Elvis
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   public Elvis(int pos, SpelNodeImpl... args) {
/*  40 */     super(pos, args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  53 */     TypedValue value = this.children[0].getValueInternal(state);
/*     */     
/*  55 */     if (!StringUtils.isEmpty(value.getValue())) {
/*  56 */       return value;
/*     */     }
/*     */     
/*  59 */     TypedValue result = this.children[1].getValueInternal(state);
/*  60 */     computeExitTypeDescriptor();
/*  61 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/*  67 */     return getChild(0).toStringAST() + " ?: " + getChild(1).toStringAST();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/*  72 */     SpelNodeImpl condition = this.children[0];
/*  73 */     SpelNodeImpl ifNullValue = this.children[1];
/*  74 */     return (condition.isCompilable() && ifNullValue.isCompilable() && condition.exitTypeDescriptor != null && ifNullValue.exitTypeDescriptor != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/*  81 */     computeExitTypeDescriptor();
/*  82 */     cf.enterCompilationScope();
/*  83 */     this.children[0].generateCode(mv, cf);
/*  84 */     String lastDesc = cf.lastDescriptor();
/*  85 */     Assert.state((lastDesc != null), "No last descriptor");
/*  86 */     CodeFlow.insertBoxIfNecessary(mv, lastDesc.charAt(0));
/*  87 */     cf.exitCompilationScope();
/*  88 */     Label elseTarget = new Label();
/*  89 */     Label endOfIf = new Label();
/*  90 */     mv.visitInsn(89);
/*  91 */     mv.visitJumpInsn(198, elseTarget);
/*     */     
/*  93 */     mv.visitInsn(89);
/*  94 */     mv.visitLdcInsn("");
/*  95 */     mv.visitInsn(95);
/*  96 */     mv.visitMethodInsn(182, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
/*  97 */     mv.visitJumpInsn(153, endOfIf);
/*  98 */     mv.visitLabel(elseTarget);
/*  99 */     mv.visitInsn(87);
/* 100 */     cf.enterCompilationScope();
/* 101 */     this.children[1].generateCode(mv, cf);
/* 102 */     if (!CodeFlow.isPrimitive(this.exitTypeDescriptor)) {
/* 103 */       lastDesc = cf.lastDescriptor();
/* 104 */       Assert.state((lastDesc != null), "No last descriptor");
/* 105 */       CodeFlow.insertBoxIfNecessary(mv, lastDesc.charAt(0));
/*     */     } 
/* 107 */     cf.exitCompilationScope();
/* 108 */     mv.visitLabel(endOfIf);
/* 109 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */   
/*     */   private void computeExitTypeDescriptor() {
/* 113 */     if (this.exitTypeDescriptor == null && (this.children[0]).exitTypeDescriptor != null && (this.children[1]).exitTypeDescriptor != null) {
/*     */       
/* 115 */       String conditionDescriptor = (this.children[0]).exitTypeDescriptor;
/* 116 */       String ifNullValueDescriptor = (this.children[1]).exitTypeDescriptor;
/* 117 */       if (ObjectUtils.nullSafeEquals(conditionDescriptor, ifNullValueDescriptor)) {
/* 118 */         this.exitTypeDescriptor = conditionDescriptor;
/*     */       }
/*     */       else {
/*     */         
/* 122 */         this.exitTypeDescriptor = "Ljava/lang/Object";
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/spel/ast/Elvis.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */