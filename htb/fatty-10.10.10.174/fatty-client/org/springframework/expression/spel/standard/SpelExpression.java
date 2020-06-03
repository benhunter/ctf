/*     */ package org.springframework.expression.spel.standard;
/*     */ 
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.Expression;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.common.ExpressionUtils;
/*     */ import org.springframework.expression.spel.CompiledExpression;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelCompilerMode;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.SpelNode;
/*     */ import org.springframework.expression.spel.SpelParserConfiguration;
/*     */ import org.springframework.expression.spel.ast.SpelNodeImpl;
/*     */ import org.springframework.expression.spel.support.StandardEvaluationContext;
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
/*     */ public class SpelExpression
/*     */   implements Expression
/*     */ {
/*     */   private static final int INTERPRETED_COUNT_THRESHOLD = 100;
/*     */   private static final int FAILED_ATTEMPTS_THRESHOLD = 100;
/*     */   private final String expression;
/*     */   private final SpelNodeImpl ast;
/*     */   private final SpelParserConfiguration configuration;
/*     */   @Nullable
/*     */   private EvaluationContext evaluationContext;
/*     */   @Nullable
/*     */   private CompiledExpression compiledAst;
/*  72 */   private volatile int interpretedCount = 0;
/*     */ 
/*     */ 
/*     */   
/*  76 */   private volatile int failedAttempts = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpelExpression(String expression, SpelNodeImpl ast, SpelParserConfiguration configuration) {
/*  83 */     this.expression = expression;
/*  84 */     this.ast = ast;
/*  85 */     this.configuration = configuration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEvaluationContext(EvaluationContext evaluationContext) {
/*  94 */     this.evaluationContext = evaluationContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EvaluationContext getEvaluationContext() {
/* 102 */     if (this.evaluationContext == null) {
/* 103 */       this.evaluationContext = (EvaluationContext)new StandardEvaluationContext();
/*     */     }
/* 105 */     return this.evaluationContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExpressionString() {
/* 113 */     return this.expression;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getValue() throws EvaluationException {
/* 119 */     if (this.compiledAst != null) {
/*     */       try {
/* 121 */         EvaluationContext context = getEvaluationContext();
/* 122 */         return this.compiledAst.getValue(context.getRootObject().getValue(), context);
/*     */       }
/* 124 */       catch (Throwable ex) {
/*     */         
/* 126 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 127 */           this.interpretedCount = 0;
/* 128 */           this.compiledAst = null;
/*     */         }
/*     */         else {
/*     */           
/* 132 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 137 */     ExpressionState expressionState = new ExpressionState(getEvaluationContext(), this.configuration);
/* 138 */     Object result = this.ast.getValue(expressionState);
/* 139 */     checkCompile(expressionState);
/* 140 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T getValue(@Nullable Class<T> expectedResultType) throws EvaluationException {
/* 147 */     if (this.compiledAst != null) {
/*     */       try {
/* 149 */         EvaluationContext context = getEvaluationContext();
/* 150 */         Object result = this.compiledAst.getValue(context.getRootObject().getValue(), context);
/* 151 */         if (expectedResultType == null) {
/* 152 */           return (T)result;
/*     */         }
/*     */         
/* 155 */         return (T)ExpressionUtils.convertTypedValue(
/* 156 */             getEvaluationContext(), new TypedValue(result), expectedResultType);
/*     */       
/*     */       }
/* 159 */       catch (Throwable ex) {
/*     */         
/* 161 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 162 */           this.interpretedCount = 0;
/* 163 */           this.compiledAst = null;
/*     */         }
/*     */         else {
/*     */           
/* 167 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 172 */     ExpressionState expressionState = new ExpressionState(getEvaluationContext(), this.configuration);
/* 173 */     TypedValue typedResultValue = this.ast.getTypedValue(expressionState);
/* 174 */     checkCompile(expressionState);
/* 175 */     return (T)ExpressionUtils.convertTypedValue(expressionState
/* 176 */         .getEvaluationContext(), typedResultValue, expectedResultType);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getValue(Object rootObject) throws EvaluationException {
/* 182 */     if (this.compiledAst != null) {
/*     */       try {
/* 184 */         return this.compiledAst.getValue(rootObject, getEvaluationContext());
/*     */       }
/* 186 */       catch (Throwable ex) {
/*     */         
/* 188 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 189 */           this.interpretedCount = 0;
/* 190 */           this.compiledAst = null;
/*     */         }
/*     */         else {
/*     */           
/* 194 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 200 */     ExpressionState expressionState = new ExpressionState(getEvaluationContext(), toTypedValue(rootObject), this.configuration);
/* 201 */     Object result = this.ast.getValue(expressionState);
/* 202 */     checkCompile(expressionState);
/* 203 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T getValue(Object rootObject, @Nullable Class<T> expectedResultType) throws EvaluationException {
/* 210 */     if (this.compiledAst != null) {
/*     */       try {
/* 212 */         Object result = this.compiledAst.getValue(rootObject, getEvaluationContext());
/* 213 */         if (expectedResultType == null) {
/* 214 */           return (T)result;
/*     */         }
/*     */         
/* 217 */         return (T)ExpressionUtils.convertTypedValue(
/* 218 */             getEvaluationContext(), new TypedValue(result), expectedResultType);
/*     */       
/*     */       }
/* 221 */       catch (Throwable ex) {
/*     */         
/* 223 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 224 */           this.interpretedCount = 0;
/* 225 */           this.compiledAst = null;
/*     */         }
/*     */         else {
/*     */           
/* 229 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 235 */     ExpressionState expressionState = new ExpressionState(getEvaluationContext(), toTypedValue(rootObject), this.configuration);
/* 236 */     TypedValue typedResultValue = this.ast.getTypedValue(expressionState);
/* 237 */     checkCompile(expressionState);
/* 238 */     return (T)ExpressionUtils.convertTypedValue(expressionState
/* 239 */         .getEvaluationContext(), typedResultValue, expectedResultType);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getValue(EvaluationContext context) throws EvaluationException {
/* 245 */     Assert.notNull(context, "EvaluationContext is required");
/*     */     
/* 247 */     if (this.compiledAst != null) {
/*     */       try {
/* 249 */         return this.compiledAst.getValue(context.getRootObject().getValue(), context);
/*     */       }
/* 251 */       catch (Throwable ex) {
/*     */         
/* 253 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 254 */           this.interpretedCount = 0;
/* 255 */           this.compiledAst = null;
/*     */         }
/*     */         else {
/*     */           
/* 259 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 264 */     ExpressionState expressionState = new ExpressionState(context, this.configuration);
/* 265 */     Object result = this.ast.getValue(expressionState);
/* 266 */     checkCompile(expressionState);
/* 267 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T getValue(EvaluationContext context, @Nullable Class<T> expectedResultType) throws EvaluationException {
/* 274 */     Assert.notNull(context, "EvaluationContext is required");
/*     */     
/* 276 */     if (this.compiledAst != null) {
/*     */       try {
/* 278 */         Object result = this.compiledAst.getValue(context.getRootObject().getValue(), context);
/* 279 */         if (expectedResultType != null) {
/* 280 */           return (T)ExpressionUtils.convertTypedValue(context, new TypedValue(result), expectedResultType);
/*     */         }
/*     */         
/* 283 */         return (T)result;
/*     */       
/*     */       }
/* 286 */       catch (Throwable ex) {
/*     */         
/* 288 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 289 */           this.interpretedCount = 0;
/* 290 */           this.compiledAst = null;
/*     */         }
/*     */         else {
/*     */           
/* 294 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 299 */     ExpressionState expressionState = new ExpressionState(context, this.configuration);
/* 300 */     TypedValue typedResultValue = this.ast.getTypedValue(expressionState);
/* 301 */     checkCompile(expressionState);
/* 302 */     return (T)ExpressionUtils.convertTypedValue(context, typedResultValue, expectedResultType);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getValue(EvaluationContext context, Object rootObject) throws EvaluationException {
/* 308 */     Assert.notNull(context, "EvaluationContext is required");
/*     */     
/* 310 */     if (this.compiledAst != null) {
/*     */       try {
/* 312 */         return this.compiledAst.getValue(rootObject, context);
/*     */       }
/* 314 */       catch (Throwable ex) {
/*     */         
/* 316 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 317 */           this.interpretedCount = 0;
/* 318 */           this.compiledAst = null;
/*     */         }
/*     */         else {
/*     */           
/* 322 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 327 */     ExpressionState expressionState = new ExpressionState(context, toTypedValue(rootObject), this.configuration);
/* 328 */     Object result = this.ast.getValue(expressionState);
/* 329 */     checkCompile(expressionState);
/* 330 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T getValue(EvaluationContext context, Object rootObject, @Nullable Class<T> expectedResultType) throws EvaluationException {
/* 339 */     Assert.notNull(context, "EvaluationContext is required");
/*     */     
/* 341 */     if (this.compiledAst != null) {
/*     */       try {
/* 343 */         Object result = this.compiledAst.getValue(rootObject, context);
/* 344 */         if (expectedResultType != null) {
/* 345 */           return (T)ExpressionUtils.convertTypedValue(context, new TypedValue(result), expectedResultType);
/*     */         }
/*     */         
/* 348 */         return (T)result;
/*     */       
/*     */       }
/* 351 */       catch (Throwable ex) {
/*     */         
/* 353 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 354 */           this.interpretedCount = 0;
/* 355 */           this.compiledAst = null;
/*     */         }
/*     */         else {
/*     */           
/* 359 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 364 */     ExpressionState expressionState = new ExpressionState(context, toTypedValue(rootObject), this.configuration);
/* 365 */     TypedValue typedResultValue = this.ast.getTypedValue(expressionState);
/* 366 */     checkCompile(expressionState);
/* 367 */     return (T)ExpressionUtils.convertTypedValue(context, typedResultValue, expectedResultType);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getValueType() throws EvaluationException {
/* 373 */     return getValueType(getEvaluationContext());
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getValueType(Object rootObject) throws EvaluationException {
/* 379 */     return getValueType(getEvaluationContext(), rootObject);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getValueType(EvaluationContext context) throws EvaluationException {
/* 385 */     Assert.notNull(context, "EvaluationContext is required");
/* 386 */     ExpressionState expressionState = new ExpressionState(context, this.configuration);
/* 387 */     TypeDescriptor typeDescriptor = this.ast.getValueInternal(expressionState).getTypeDescriptor();
/* 388 */     return (typeDescriptor != null) ? typeDescriptor.getType() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getValueType(EvaluationContext context, Object rootObject) throws EvaluationException {
/* 394 */     ExpressionState expressionState = new ExpressionState(context, toTypedValue(rootObject), this.configuration);
/* 395 */     TypeDescriptor typeDescriptor = this.ast.getValueInternal(expressionState).getTypeDescriptor();
/* 396 */     return (typeDescriptor != null) ? typeDescriptor.getType() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public TypeDescriptor getValueTypeDescriptor() throws EvaluationException {
/* 402 */     return getValueTypeDescriptor(getEvaluationContext());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public TypeDescriptor getValueTypeDescriptor(Object rootObject) throws EvaluationException {
/* 409 */     ExpressionState expressionState = new ExpressionState(getEvaluationContext(), toTypedValue(rootObject), this.configuration);
/* 410 */     return this.ast.getValueInternal(expressionState).getTypeDescriptor();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public TypeDescriptor getValueTypeDescriptor(EvaluationContext context) throws EvaluationException {
/* 416 */     Assert.notNull(context, "EvaluationContext is required");
/* 417 */     ExpressionState expressionState = new ExpressionState(context, this.configuration);
/* 418 */     return this.ast.getValueInternal(expressionState).getTypeDescriptor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public TypeDescriptor getValueTypeDescriptor(EvaluationContext context, Object rootObject) throws EvaluationException {
/* 426 */     Assert.notNull(context, "EvaluationContext is required");
/* 427 */     ExpressionState expressionState = new ExpressionState(context, toTypedValue(rootObject), this.configuration);
/* 428 */     return this.ast.getValueInternal(expressionState).getTypeDescriptor();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(Object rootObject) throws EvaluationException {
/* 433 */     return this.ast.isWritable(new ExpressionState(
/* 434 */           getEvaluationContext(), toTypedValue(rootObject), this.configuration));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(EvaluationContext context) throws EvaluationException {
/* 439 */     Assert.notNull(context, "EvaluationContext is required");
/* 440 */     return this.ast.isWritable(new ExpressionState(context, this.configuration));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(EvaluationContext context, Object rootObject) throws EvaluationException {
/* 445 */     Assert.notNull(context, "EvaluationContext is required");
/* 446 */     return this.ast.isWritable(new ExpressionState(context, toTypedValue(rootObject), this.configuration));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(Object rootObject, @Nullable Object value) throws EvaluationException {
/* 451 */     this.ast.setValue(new ExpressionState(
/* 452 */           getEvaluationContext(), toTypedValue(rootObject), this.configuration), value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(EvaluationContext context, @Nullable Object value) throws EvaluationException {
/* 457 */     Assert.notNull(context, "EvaluationContext is required");
/* 458 */     this.ast.setValue(new ExpressionState(context, this.configuration), value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(EvaluationContext context, Object rootObject, @Nullable Object value) throws EvaluationException {
/* 465 */     Assert.notNull(context, "EvaluationContext is required");
/* 466 */     this.ast.setValue(new ExpressionState(context, toTypedValue(rootObject), this.configuration), value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkCompile(ExpressionState expressionState) {
/* 476 */     this.interpretedCount++;
/* 477 */     SpelCompilerMode compilerMode = expressionState.getConfiguration().getCompilerMode();
/* 478 */     if (compilerMode != SpelCompilerMode.OFF) {
/* 479 */       if (compilerMode == SpelCompilerMode.IMMEDIATE) {
/* 480 */         if (this.interpretedCount > 1) {
/* 481 */           compileExpression();
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 486 */       else if (this.interpretedCount > 100) {
/* 487 */         compileExpression();
/*     */       } 
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
/*     */   public boolean compileExpression() {
/* 500 */     if (this.failedAttempts > 100)
/*     */     {
/* 502 */       return false;
/*     */     }
/* 504 */     if (this.compiledAst == null) {
/* 505 */       synchronized (this.expression) {
/*     */         
/* 507 */         if (this.compiledAst != null) {
/* 508 */           return true;
/*     */         }
/* 510 */         SpelCompiler compiler = SpelCompiler.getCompiler(this.configuration.getCompilerClassLoader());
/* 511 */         this.compiledAst = compiler.compile(this.ast);
/* 512 */         if (this.compiledAst == null) {
/* 513 */           this.failedAttempts++;
/*     */         }
/*     */       } 
/*     */     }
/* 517 */     return (this.compiledAst != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void revertToInterpreted() {
/* 526 */     this.compiledAst = null;
/* 527 */     this.interpretedCount = 0;
/* 528 */     this.failedAttempts = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpelNode getAST() {
/* 535 */     return (SpelNode)this.ast;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 545 */     return this.ast.toStringAST();
/*     */   }
/*     */   
/*     */   private TypedValue toTypedValue(@Nullable Object object) {
/* 549 */     return (object != null) ? new TypedValue(object) : TypedValue.NULL;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/spel/standard/SpelExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */