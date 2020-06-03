/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.asm.Opcodes;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.common.ExpressionUtils;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.SpelNode;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ public abstract class SpelNodeImpl
/*     */   implements SpelNode, Opcodes
/*     */ {
/*  47 */   private static final SpelNodeImpl[] NO_CHILDREN = new SpelNodeImpl[0];
/*     */ 
/*     */   
/*     */   protected final int pos;
/*     */   
/*  52 */   protected SpelNodeImpl[] children = NO_CHILDREN;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private SpelNodeImpl parent;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected volatile String exitTypeDescriptor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpelNodeImpl(int pos, SpelNodeImpl... operands) {
/*  71 */     this.pos = pos;
/*  72 */     if (!ObjectUtils.isEmpty((Object[])operands)) {
/*  73 */       this.children = operands;
/*  74 */       for (SpelNodeImpl operand : operands) {
/*  75 */         Assert.notNull(operand, "Operand must not be null");
/*  76 */         operand.parent = this;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean nextChildIs(Class<?>... classes) {
/*  86 */     if (this.parent != null) {
/*  87 */       SpelNodeImpl[] peers = this.parent.children;
/*  88 */       for (int i = 0, max = peers.length; i < max; i++) {
/*  89 */         if (this == peers[i]) {
/*  90 */           if (i + 1 >= max) {
/*  91 */             return false;
/*     */           }
/*  93 */           Class<?> peerClass = peers[i + 1].getClass();
/*  94 */           for (Class<?> desiredClass : classes) {
/*  95 */             if (peerClass == desiredClass) {
/*  96 */               return true;
/*     */             }
/*     */           } 
/*  99 */           return false;
/*     */         } 
/*     */       } 
/*     */     } 
/* 103 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final Object getValue(ExpressionState expressionState) throws EvaluationException {
/* 109 */     return getValueInternal(expressionState).getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public final TypedValue getTypedValue(ExpressionState expressionState) throws EvaluationException {
/* 114 */     return getValueInternal(expressionState);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWritable(ExpressionState expressionState) throws EvaluationException {
/* 120 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(ExpressionState expressionState, @Nullable Object newValue) throws EvaluationException {
/* 125 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.SETVALUE_NOT_SUPPORTED, new Object[] { getClass() });
/*     */   }
/*     */ 
/*     */   
/*     */   public SpelNode getChild(int index) {
/* 130 */     return this.children[index];
/*     */   }
/*     */ 
/*     */   
/*     */   public int getChildCount() {
/* 135 */     return this.children.length;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getObjectClass(@Nullable Object obj) {
/* 141 */     if (obj == null) {
/* 142 */       return null;
/*     */     }
/* 144 */     return (obj instanceof Class) ? (Class)obj : obj.getClass();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStartPosition() {
/* 149 */     return this.pos >> 16;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEndPosition() {
/* 154 */     return this.pos & 0xFFFF;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/* 164 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 175 */     throw new IllegalStateException(getClass().getName() + " has no generateCode(..) method");
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public String getExitDescriptor() {
/* 180 */     return this.exitTypeDescriptor;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected final <T> T getValue(ExpressionState state, Class<T> desiredReturnType) throws EvaluationException {
/* 185 */     return (T)ExpressionUtils.convertTypedValue(state.getEvaluationContext(), getValueInternal(state), desiredReturnType);
/*     */   }
/*     */   
/*     */   protected ValueRef getValueRef(ExpressionState state) throws EvaluationException {
/* 189 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.NOT_ASSIGNABLE, new Object[] { toStringAST() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract TypedValue getValueInternal(ExpressionState paramExpressionState) throws EvaluationException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void generateCodeForArguments(MethodVisitor mv, CodeFlow cf, Member member, SpelNodeImpl[] arguments) {
/* 205 */     String[] paramDescriptors = null;
/* 206 */     boolean isVarargs = false;
/* 207 */     if (member instanceof Constructor) {
/* 208 */       Constructor<?> ctor = (Constructor)member;
/* 209 */       paramDescriptors = CodeFlow.toDescriptors(ctor.getParameterTypes());
/* 210 */       isVarargs = ctor.isVarArgs();
/*     */     } else {
/*     */       
/* 213 */       Method method = (Method)member;
/* 214 */       paramDescriptors = CodeFlow.toDescriptors(method.getParameterTypes());
/* 215 */       isVarargs = method.isVarArgs();
/*     */     } 
/* 217 */     if (isVarargs) {
/*     */ 
/*     */       
/* 220 */       int p = 0;
/* 221 */       int childCount = arguments.length;
/*     */ 
/*     */       
/* 224 */       for (p = 0; p < paramDescriptors.length - 1; p++) {
/* 225 */         generateCodeForArgument(mv, cf, arguments[p], paramDescriptors[p]);
/*     */       }
/*     */       
/* 228 */       SpelNodeImpl lastChild = (childCount == 0) ? null : arguments[childCount - 1];
/* 229 */       String arrayType = paramDescriptors[paramDescriptors.length - 1];
/*     */ 
/*     */       
/* 232 */       if (lastChild != null && arrayType.equals(lastChild.getExitDescriptor())) {
/* 233 */         generateCodeForArgument(mv, cf, lastChild, paramDescriptors[p]);
/*     */       } else {
/*     */         
/* 236 */         arrayType = arrayType.substring(1);
/*     */         
/* 238 */         CodeFlow.insertNewArrayCode(mv, childCount - p, arrayType);
/*     */         
/* 240 */         int arrayindex = 0;
/* 241 */         while (p < childCount) {
/* 242 */           SpelNodeImpl child = arguments[p];
/* 243 */           mv.visitInsn(89);
/* 244 */           CodeFlow.insertOptimalLoad(mv, arrayindex++);
/* 245 */           generateCodeForArgument(mv, cf, child, arrayType);
/* 246 */           CodeFlow.insertArrayStore(mv, arrayType);
/* 247 */           p++;
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 252 */       for (int i = 0; i < paramDescriptors.length; i++) {
/* 253 */         generateCodeForArgument(mv, cf, arguments[i], paramDescriptors[i]);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void generateCodeForArgument(MethodVisitor mv, CodeFlow cf, SpelNodeImpl argument, String paramDesc) {
/* 263 */     cf.enterCompilationScope();
/* 264 */     argument.generateCode(mv, cf);
/* 265 */     String lastDesc = cf.lastDescriptor();
/* 266 */     Assert.state((lastDesc != null), "No last descriptor");
/* 267 */     boolean primitiveOnStack = CodeFlow.isPrimitive(lastDesc);
/*     */     
/* 269 */     if (primitiveOnStack && paramDesc.charAt(0) == 'L') {
/* 270 */       CodeFlow.insertBoxIfNecessary(mv, lastDesc.charAt(0));
/*     */     }
/* 272 */     else if (paramDesc.length() == 1 && !primitiveOnStack) {
/* 273 */       CodeFlow.insertUnboxInsns(mv, paramDesc.charAt(0), lastDesc);
/*     */     }
/* 275 */     else if (!paramDesc.equals(lastDesc)) {
/*     */       
/* 277 */       CodeFlow.insertCheckCast(mv, paramDesc);
/*     */     } 
/* 279 */     cf.exitCompilationScope();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/spel/ast/SpelNodeImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */