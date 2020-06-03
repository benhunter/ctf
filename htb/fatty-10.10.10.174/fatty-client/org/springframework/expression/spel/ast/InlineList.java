/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.asm.ClassWriter;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelNode;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class InlineList
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   @Nullable
/*     */   private TypedValue constant;
/*     */   
/*     */   public InlineList(int pos, SpelNodeImpl... args) {
/*  46 */     super(pos, args);
/*  47 */     checkIfConstant();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkIfConstant() {
/*  57 */     boolean isConstant = true;
/*  58 */     for (int c = 0, max = getChildCount(); c < max; c++) {
/*  59 */       SpelNode child = getChild(c);
/*  60 */       if (!(child instanceof Literal)) {
/*  61 */         if (child instanceof InlineList) {
/*  62 */           InlineList inlineList = (InlineList)child;
/*  63 */           if (!inlineList.isConstant()) {
/*  64 */             isConstant = false;
/*     */           }
/*     */         } else {
/*     */           
/*  68 */           isConstant = false;
/*     */         } 
/*     */       }
/*     */     } 
/*  72 */     if (isConstant) {
/*  73 */       List<Object> constantList = new ArrayList();
/*  74 */       int childcount = getChildCount();
/*  75 */       for (int i = 0; i < childcount; i++) {
/*  76 */         SpelNode child = getChild(i);
/*  77 */         if (child instanceof Literal) {
/*  78 */           constantList.add(((Literal)child).getLiteralValue().getValue());
/*     */         }
/*  80 */         else if (child instanceof InlineList) {
/*  81 */           constantList.add(((InlineList)child).getConstantValue());
/*     */         } 
/*     */       } 
/*  84 */       this.constant = new TypedValue(Collections.unmodifiableList(constantList));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState expressionState) throws EvaluationException {
/*  90 */     if (this.constant != null) {
/*  91 */       return this.constant;
/*     */     }
/*     */     
/*  94 */     List<Object> returnValue = new ArrayList();
/*  95 */     int childCount = getChildCount();
/*  96 */     for (int c = 0; c < childCount; c++) {
/*  97 */       returnValue.add(getChild(c).getValue(expressionState));
/*     */     }
/*  99 */     return new TypedValue(returnValue);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 105 */     StringBuilder sb = new StringBuilder("{");
/*     */     
/* 107 */     int count = getChildCount();
/* 108 */     for (int c = 0; c < count; c++) {
/* 109 */       if (c > 0) {
/* 110 */         sb.append(",");
/*     */       }
/* 112 */       sb.append(getChild(c).toStringAST());
/*     */     } 
/* 114 */     sb.append("}");
/* 115 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConstant() {
/* 122 */     return (this.constant != null);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<Object> getConstantValue() {
/* 128 */     Assert.state((this.constant != null), "No constant");
/* 129 */     return (List<Object>)this.constant.getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/* 134 */     return isConstant();
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow codeflow) {
/* 139 */     String constantFieldName = "inlineList$" + codeflow.nextFieldId();
/* 140 */     String className = codeflow.getClassName();
/*     */     
/* 142 */     codeflow.registerNewField((cw, cflow) -> cw.visitField(26, constantFieldName, "Ljava/util/List;", null, null));
/*     */ 
/*     */     
/* 145 */     codeflow.registerNewClinit((mVisitor, cflow) -> generateClinitCode(className, constantFieldName, mVisitor, cflow, false));
/*     */ 
/*     */     
/* 148 */     mv.visitFieldInsn(178, className, constantFieldName, "Ljava/util/List;");
/* 149 */     codeflow.pushDescriptor("Ljava/util/List");
/*     */   }
/*     */   
/*     */   void generateClinitCode(String clazzname, String constantFieldName, MethodVisitor mv, CodeFlow codeflow, boolean nested) {
/* 153 */     mv.visitTypeInsn(187, "java/util/ArrayList");
/* 154 */     mv.visitInsn(89);
/* 155 */     mv.visitMethodInsn(183, "java/util/ArrayList", "<init>", "()V", false);
/* 156 */     if (!nested) {
/* 157 */       mv.visitFieldInsn(179, clazzname, constantFieldName, "Ljava/util/List;");
/*     */     }
/* 159 */     int childCount = getChildCount();
/* 160 */     for (int c = 0; c < childCount; c++) {
/* 161 */       if (!nested) {
/* 162 */         mv.visitFieldInsn(178, clazzname, constantFieldName, "Ljava/util/List;");
/*     */       } else {
/*     */         
/* 165 */         mv.visitInsn(89);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 170 */       if (this.children[c] instanceof InlineList) {
/* 171 */         ((InlineList)this.children[c]).generateClinitCode(clazzname, constantFieldName, mv, codeflow, true);
/*     */       } else {
/*     */         
/* 174 */         this.children[c].generateCode(mv, codeflow);
/* 175 */         String lastDesc = codeflow.lastDescriptor();
/* 176 */         if (CodeFlow.isPrimitive(lastDesc)) {
/* 177 */           CodeFlow.insertBoxIfNecessary(mv, lastDesc.charAt(0));
/*     */         }
/*     */       } 
/* 180 */       mv.visitMethodInsn(185, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
/* 181 */       mv.visitInsn(87);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/spel/ast/InlineList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */