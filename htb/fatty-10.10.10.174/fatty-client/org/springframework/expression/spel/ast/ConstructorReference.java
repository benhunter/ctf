/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.ConstructorExecutor;
/*     */ import org.springframework.expression.ConstructorResolver;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.common.ExpressionUtils;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.SpelNode;
/*     */ import org.springframework.expression.spel.support.ReflectiveConstructorExecutor;
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
/*     */ public class ConstructorReference
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   private boolean isArrayConstructor = false;
/*     */   @Nullable
/*     */   private SpelNodeImpl[] dimensions;
/*     */   @Nullable
/*     */   private volatile ConstructorExecutor cachedExecutor;
/*     */   
/*     */   public ConstructorReference(int pos, SpelNodeImpl... arguments) {
/*  76 */     super(pos, arguments);
/*  77 */     this.isArrayConstructor = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstructorReference(int pos, SpelNodeImpl[] dimensions, SpelNodeImpl... arguments) {
/*  85 */     super(pos, arguments);
/*  86 */     this.isArrayConstructor = true;
/*  87 */     this.dimensions = dimensions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  96 */     if (this.isArrayConstructor) {
/*  97 */       return createArray(state);
/*     */     }
/*     */     
/* 100 */     return createNewInstance(state);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TypedValue createNewInstance(ExpressionState state) throws EvaluationException {
/* 111 */     Object[] arguments = new Object[getChildCount() - 1];
/* 112 */     List<TypeDescriptor> argumentTypes = new ArrayList<>(getChildCount() - 1);
/* 113 */     for (int i = 0; i < arguments.length; i++) {
/* 114 */       TypedValue childValue = this.children[i + 1].getValueInternal(state);
/* 115 */       Object value = childValue.getValue();
/* 116 */       arguments[i] = value;
/* 117 */       argumentTypes.add(TypeDescriptor.forObject(value));
/*     */     } 
/*     */     
/* 120 */     ConstructorExecutor executorToUse = this.cachedExecutor;
/* 121 */     if (executorToUse != null) {
/*     */       try {
/* 123 */         return executorToUse.execute(state.getEvaluationContext(), arguments);
/*     */       }
/* 125 */       catch (AccessException ex) {
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
/* 136 */         if (ex.getCause() instanceof java.lang.reflect.InvocationTargetException) {
/*     */           
/* 138 */           Throwable rootCause = ex.getCause().getCause();
/* 139 */           if (rootCause instanceof RuntimeException) {
/* 140 */             throw (RuntimeException)rootCause;
/*     */           }
/*     */           
/* 143 */           String str = (String)this.children[0].getValueInternal(state).getValue();
/* 144 */           throw new SpelEvaluationException(getStartPosition(), rootCause, SpelMessage.CONSTRUCTOR_INVOCATION_PROBLEM, new Object[] { str, 
/*     */                 
/* 146 */                 FormatHelper.formatMethodForMessage("", argumentTypes) });
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 151 */         this.cachedExecutor = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 156 */     String typeName = (String)this.children[0].getValueInternal(state).getValue();
/* 157 */     Assert.state((typeName != null), "No type name");
/* 158 */     executorToUse = findExecutorForConstructor(typeName, argumentTypes, state);
/*     */     try {
/* 160 */       this.cachedExecutor = executorToUse;
/* 161 */       if (executorToUse instanceof ReflectiveConstructorExecutor) {
/* 162 */         this.exitTypeDescriptor = CodeFlow.toDescriptor(((ReflectiveConstructorExecutor)executorToUse)
/* 163 */             .getConstructor().getDeclaringClass());
/*     */       }
/*     */       
/* 166 */       return executorToUse.execute(state.getEvaluationContext(), arguments);
/*     */     }
/* 168 */     catch (AccessException ex) {
/* 169 */       throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.CONSTRUCTOR_INVOCATION_PROBLEM, new Object[] { typeName, 
/*     */             
/* 171 */             FormatHelper.formatMethodForMessage("", argumentTypes) });
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ConstructorExecutor findExecutorForConstructor(String typeName, List<TypeDescriptor> argumentTypes, ExpressionState state) throws SpelEvaluationException {
/* 187 */     EvaluationContext evalContext = state.getEvaluationContext();
/* 188 */     List<ConstructorResolver> ctorResolvers = evalContext.getConstructorResolvers();
/* 189 */     for (ConstructorResolver ctorResolver : ctorResolvers) {
/*     */       try {
/* 191 */         ConstructorExecutor ce = ctorResolver.resolve(state.getEvaluationContext(), typeName, argumentTypes);
/* 192 */         if (ce != null) {
/* 193 */           return ce;
/*     */         }
/*     */       }
/* 196 */       catch (AccessException ex) {
/* 197 */         throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.CONSTRUCTOR_INVOCATION_PROBLEM, new Object[] { typeName, 
/*     */               
/* 199 */               FormatHelper.formatMethodForMessage("", argumentTypes) });
/*     */       } 
/*     */     } 
/* 202 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.CONSTRUCTOR_NOT_FOUND, new Object[] { typeName, 
/* 203 */           FormatHelper.formatMethodForMessage("", argumentTypes) });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 208 */     StringBuilder sb = new StringBuilder("new ");
/* 209 */     int index = 0;
/* 210 */     sb.append(getChild(index++).toStringAST());
/* 211 */     sb.append("(");
/* 212 */     for (int i = index; i < getChildCount(); i++) {
/* 213 */       if (i > index) {
/* 214 */         sb.append(",");
/*     */       }
/* 216 */       sb.append(getChild(i).toStringAST());
/*     */     } 
/* 218 */     sb.append(")");
/* 219 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TypedValue createArray(ExpressionState state) throws EvaluationException {
/*     */     Class<?> componentType;
/* 230 */     Object newArray, intendedArrayType = getChild(0).getValue(state);
/* 231 */     if (!(intendedArrayType instanceof String))
/* 232 */       throw new SpelEvaluationException(getChild(0).getStartPosition(), SpelMessage.TYPE_NAME_EXPECTED_FOR_ARRAY_CONSTRUCTION, new Object[] {
/*     */             
/* 234 */             FormatHelper.formatClassNameForMessage((intendedArrayType != null) ? intendedArrayType
/* 235 */               .getClass() : null)
/*     */           }); 
/* 237 */     String type = (String)intendedArrayType;
/*     */     
/* 239 */     TypeCode arrayTypeCode = TypeCode.forName(type);
/* 240 */     if (arrayTypeCode == TypeCode.OBJECT) {
/* 241 */       componentType = state.findType(type);
/*     */     } else {
/*     */       
/* 244 */       componentType = arrayTypeCode.getType();
/*     */     } 
/*     */     
/* 247 */     if (!hasInitializer()) {
/*     */       
/* 249 */       if (this.dimensions != null) {
/* 250 */         for (SpelNodeImpl dimension : this.dimensions) {
/* 251 */           if (dimension == null) {
/* 252 */             throw new SpelEvaluationException(getStartPosition(), SpelMessage.MISSING_ARRAY_DIMENSION, new Object[0]);
/*     */           }
/*     */         } 
/*     */       }
/* 256 */       TypeConverter typeConverter = state.getEvaluationContext().getTypeConverter();
/*     */ 
/*     */       
/* 259 */       if (this.dimensions.length == 1) {
/* 260 */         TypedValue o = this.dimensions[0].getTypedValue(state);
/* 261 */         int arraySize = ExpressionUtils.toInt(typeConverter, o);
/* 262 */         newArray = Array.newInstance(componentType, arraySize);
/*     */       }
/*     */       else {
/*     */         
/* 266 */         int[] dims = new int[this.dimensions.length];
/* 267 */         for (int d = 0; d < this.dimensions.length; d++) {
/* 268 */           TypedValue o = this.dimensions[d].getTypedValue(state);
/* 269 */           dims[d] = ExpressionUtils.toInt(typeConverter, o);
/*     */         } 
/* 271 */         newArray = Array.newInstance(componentType, dims);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 276 */       if (this.dimensions == null || this.dimensions.length > 1)
/*     */       {
/*     */         
/* 279 */         throw new SpelEvaluationException(getStartPosition(), SpelMessage.MULTIDIM_ARRAY_INITIALIZER_NOT_SUPPORTED, new Object[0]);
/*     */       }
/*     */       
/* 282 */       TypeConverter typeConverter = state.getEvaluationContext().getTypeConverter();
/* 283 */       InlineList initializer = (InlineList)getChild(1);
/*     */       
/* 285 */       if (this.dimensions[0] != null) {
/* 286 */         TypedValue dValue = this.dimensions[0].getTypedValue(state);
/* 287 */         int i = ExpressionUtils.toInt(typeConverter, dValue);
/* 288 */         if (i != initializer.getChildCount()) {
/* 289 */           throw new SpelEvaluationException(getStartPosition(), SpelMessage.INITIALIZER_LENGTH_INCORRECT, new Object[0]);
/*     */         }
/*     */       } 
/*     */       
/* 293 */       int arraySize = initializer.getChildCount();
/* 294 */       newArray = Array.newInstance(componentType, arraySize);
/* 295 */       if (arrayTypeCode == TypeCode.OBJECT) {
/* 296 */         populateReferenceTypeArray(state, newArray, typeConverter, initializer, componentType);
/*     */       }
/* 298 */       else if (arrayTypeCode == TypeCode.BOOLEAN) {
/* 299 */         populateBooleanArray(state, newArray, typeConverter, initializer);
/*     */       }
/* 301 */       else if (arrayTypeCode == TypeCode.BYTE) {
/* 302 */         populateByteArray(state, newArray, typeConverter, initializer);
/*     */       }
/* 304 */       else if (arrayTypeCode == TypeCode.CHAR) {
/* 305 */         populateCharArray(state, newArray, typeConverter, initializer);
/*     */       }
/* 307 */       else if (arrayTypeCode == TypeCode.DOUBLE) {
/* 308 */         populateDoubleArray(state, newArray, typeConverter, initializer);
/*     */       }
/* 310 */       else if (arrayTypeCode == TypeCode.FLOAT) {
/* 311 */         populateFloatArray(state, newArray, typeConverter, initializer);
/*     */       }
/* 313 */       else if (arrayTypeCode == TypeCode.INT) {
/* 314 */         populateIntArray(state, newArray, typeConverter, initializer);
/*     */       }
/* 316 */       else if (arrayTypeCode == TypeCode.LONG) {
/* 317 */         populateLongArray(state, newArray, typeConverter, initializer);
/*     */       }
/* 319 */       else if (arrayTypeCode == TypeCode.SHORT) {
/* 320 */         populateShortArray(state, newArray, typeConverter, initializer);
/*     */       } else {
/*     */         
/* 323 */         throw new IllegalStateException(arrayTypeCode.name());
/*     */       } 
/*     */     } 
/* 326 */     return new TypedValue(newArray);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateReferenceTypeArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer, Class<?> componentType) {
/* 332 */     TypeDescriptor toTypeDescriptor = TypeDescriptor.valueOf(componentType);
/* 333 */     Object[] newObjectArray = (Object[])newArray;
/* 334 */     for (int i = 0; i < newObjectArray.length; i++) {
/* 335 */       SpelNode elementNode = initializer.getChild(i);
/* 336 */       Object arrayEntry = elementNode.getValue(state);
/* 337 */       newObjectArray[i] = typeConverter.convertValue(arrayEntry, 
/* 338 */           TypeDescriptor.forObject(arrayEntry), toTypeDescriptor);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateByteArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer) {
/* 345 */     byte[] newByteArray = (byte[])newArray;
/* 346 */     for (int i = 0; i < newByteArray.length; i++) {
/* 347 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 348 */       newByteArray[i] = ExpressionUtils.toByte(typeConverter, typedValue);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateFloatArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer) {
/* 355 */     float[] newFloatArray = (float[])newArray;
/* 356 */     for (int i = 0; i < newFloatArray.length; i++) {
/* 357 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 358 */       newFloatArray[i] = ExpressionUtils.toFloat(typeConverter, typedValue);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateDoubleArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer) {
/* 365 */     double[] newDoubleArray = (double[])newArray;
/* 366 */     for (int i = 0; i < newDoubleArray.length; i++) {
/* 367 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 368 */       newDoubleArray[i] = ExpressionUtils.toDouble(typeConverter, typedValue);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateShortArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer) {
/* 375 */     short[] newShortArray = (short[])newArray;
/* 376 */     for (int i = 0; i < newShortArray.length; i++) {
/* 377 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 378 */       newShortArray[i] = ExpressionUtils.toShort(typeConverter, typedValue);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateLongArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer) {
/* 385 */     long[] newLongArray = (long[])newArray;
/* 386 */     for (int i = 0; i < newLongArray.length; i++) {
/* 387 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 388 */       newLongArray[i] = ExpressionUtils.toLong(typeConverter, typedValue);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateCharArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer) {
/* 395 */     char[] newCharArray = (char[])newArray;
/* 396 */     for (int i = 0; i < newCharArray.length; i++) {
/* 397 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 398 */       newCharArray[i] = ExpressionUtils.toChar(typeConverter, typedValue);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateBooleanArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer) {
/* 405 */     boolean[] newBooleanArray = (boolean[])newArray;
/* 406 */     for (int i = 0; i < newBooleanArray.length; i++) {
/* 407 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 408 */       newBooleanArray[i] = ExpressionUtils.toBoolean(typeConverter, typedValue);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateIntArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer) {
/* 415 */     int[] newIntArray = (int[])newArray;
/* 416 */     for (int i = 0; i < newIntArray.length; i++) {
/* 417 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 418 */       newIntArray[i] = ExpressionUtils.toInt(typeConverter, typedValue);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean hasInitializer() {
/* 423 */     return (getChildCount() > 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/* 428 */     if (!(this.cachedExecutor instanceof ReflectiveConstructorExecutor) || this.exitTypeDescriptor == null)
/*     */     {
/* 430 */       return false;
/*     */     }
/*     */     
/* 433 */     if (getChildCount() > 1) {
/* 434 */       for (int c = 1, max = getChildCount(); c < max; c++) {
/* 435 */         if (!this.children[c].isCompilable()) {
/* 436 */           return false;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 441 */     ReflectiveConstructorExecutor executor = (ReflectiveConstructorExecutor)this.cachedExecutor;
/* 442 */     if (executor == null) {
/* 443 */       return false;
/*     */     }
/* 445 */     Constructor<?> constructor = executor.getConstructor();
/* 446 */     return (Modifier.isPublic(constructor.getModifiers()) && 
/* 447 */       Modifier.isPublic(constructor.getDeclaringClass().getModifiers()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 452 */     ReflectiveConstructorExecutor executor = (ReflectiveConstructorExecutor)this.cachedExecutor;
/* 453 */     Assert.state((executor != null), "No cached executor");
/*     */     
/* 455 */     Constructor<?> constructor = executor.getConstructor();
/* 456 */     String classDesc = constructor.getDeclaringClass().getName().replace('.', '/');
/* 457 */     mv.visitTypeInsn(187, classDesc);
/* 458 */     mv.visitInsn(89);
/*     */ 
/*     */     
/* 461 */     SpelNodeImpl[] arguments = new SpelNodeImpl[this.children.length - 1];
/* 462 */     System.arraycopy(this.children, 1, arguments, 0, this.children.length - 1);
/* 463 */     generateCodeForArguments(mv, cf, constructor, arguments);
/* 464 */     mv.visitMethodInsn(183, classDesc, "<init>", CodeFlow.createSignatureDescriptor(constructor), false);
/* 465 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/spel/ast/ConstructorReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */