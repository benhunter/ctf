/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class CompoundExpression
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   public CompoundExpression(int pos, SpelNodeImpl... expressionComponents) {
/*  37 */     super(pos, expressionComponents);
/*  38 */     if (expressionComponents.length < 2) {
/*  39 */       throw new IllegalStateException("Do not build compound expressions with less than two entries: " + expressionComponents.length);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ValueRef getValueRef(ExpressionState state) throws EvaluationException {
/*  47 */     if (getChildCount() == 1) {
/*  48 */       return this.children[0].getValueRef(state);
/*     */     }
/*     */     
/*  51 */     SpelNodeImpl nextNode = this.children[0];
/*     */     try {
/*  53 */       TypedValue result = nextNode.getValueInternal(state);
/*  54 */       int cc = getChildCount();
/*  55 */       for (int i = 1; i < cc - 1; i++) {
/*     */         
/*  57 */         try { state.pushActiveContextObject(result);
/*  58 */           nextNode = this.children[i];
/*  59 */           result = nextNode.getValueInternal(state);
/*     */ 
/*     */           
/*  62 */           state.popActiveContextObject(); } finally { state.popActiveContextObject(); }
/*     */       
/*     */       } 
/*     */       try {
/*  66 */         state.pushActiveContextObject(result);
/*  67 */         nextNode = this.children[cc - 1];
/*  68 */         return nextNode.getValueRef(state);
/*     */       } finally {
/*     */         
/*  71 */         state.popActiveContextObject();
/*     */       }
/*     */     
/*  74 */     } catch (SpelEvaluationException ex) {
/*     */       
/*  76 */       ex.setPosition(nextNode.getStartPosition());
/*  77 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  89 */     ValueRef ref = getValueRef(state);
/*  90 */     TypedValue result = ref.getValue();
/*  91 */     this.exitTypeDescriptor = (this.children[this.children.length - 1]).exitTypeDescriptor;
/*  92 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(ExpressionState state, @Nullable Object value) throws EvaluationException {
/*  97 */     getValueRef(state).setValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(ExpressionState state) throws EvaluationException {
/* 102 */     return getValueRef(state).isWritable();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 107 */     StringBuilder sb = new StringBuilder();
/* 108 */     for (int i = 0; i < getChildCount(); i++) {
/* 109 */       if (i > 0) {
/* 110 */         sb.append(".");
/*     */       }
/* 112 */       sb.append(getChild(i).toStringAST());
/*     */     } 
/* 114 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/* 119 */     for (SpelNodeImpl child : this.children) {
/* 120 */       if (!child.isCompilable()) {
/* 121 */         return false;
/*     */       }
/*     */     } 
/* 124 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 129 */     for (SpelNodeImpl child : this.children) {
/* 130 */       child.generateCode(mv, cf);
/*     */     }
/* 132 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/spel/ast/CompoundExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */