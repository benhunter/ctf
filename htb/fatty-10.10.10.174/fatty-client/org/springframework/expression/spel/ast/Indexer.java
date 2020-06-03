/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.PropertyAccessor;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.support.ReflectivePropertyAccessor;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ public class Indexer
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   @Nullable
/*     */   private String cachedReadName;
/*     */   @Nullable
/*     */   private Class<?> cachedReadTargetType;
/*     */   @Nullable
/*     */   private PropertyAccessor cachedReadAccessor;
/*     */   @Nullable
/*     */   private String cachedWriteName;
/*     */   @Nullable
/*     */   private Class<?> cachedWriteTargetType;
/*     */   @Nullable
/*     */   private PropertyAccessor cachedWriteAccessor;
/*     */   @Nullable
/*     */   private IndexedType indexedType;
/*     */   
/*     */   private enum IndexedType
/*     */   {
/*  58 */     ARRAY, LIST, MAP, STRING, OBJECT;
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
/*     */   public Indexer(int pos, SpelNodeImpl expr) {
/*  94 */     super(pos, new SpelNodeImpl[] { expr });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/* 100 */     return getValueRef(state).getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(ExpressionState state, @Nullable Object newValue) throws EvaluationException {
/* 105 */     getValueRef(state).setValue(newValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(ExpressionState expressionState) throws SpelEvaluationException {
/* 110 */     return true;
/*     */   }
/*     */   
/*     */   protected ValueRef getValueRef(ExpressionState state) throws EvaluationException {
/*     */     TypedValue indexValue;
/*     */     Object index;
/* 116 */     TypedValue context = state.getActiveContextObject();
/* 117 */     Object target = context.getValue();
/* 118 */     TypeDescriptor targetDescriptor = context.getTypeDescriptor();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 123 */     if (target instanceof Map && this.children[0] instanceof PropertyOrFieldReference) {
/* 124 */       PropertyOrFieldReference reference = (PropertyOrFieldReference)this.children[0];
/* 125 */       index = reference.getName();
/* 126 */       indexValue = new TypedValue(index);
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/* 132 */         state.pushActiveContextObject(state.getRootContextObject());
/* 133 */         indexValue = this.children[0].getValueInternal(state);
/* 134 */         index = indexValue.getValue();
/* 135 */         Assert.state((index != null), "No index");
/*     */       } finally {
/*     */         
/* 138 */         state.popActiveContextObject();
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 143 */     if (target == null) {
/* 144 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.CANNOT_INDEX_INTO_NULL_VALUE, new Object[0]);
/*     */     }
/*     */     
/* 147 */     Assert.state((targetDescriptor != null), "No type descriptor");
/*     */ 
/*     */     
/* 150 */     if (target instanceof Map) {
/* 151 */       Object key = index;
/* 152 */       if (targetDescriptor.getMapKeyTypeDescriptor() != null) {
/* 153 */         key = state.convertValue(key, targetDescriptor.getMapKeyTypeDescriptor());
/*     */       }
/* 155 */       this.indexedType = IndexedType.MAP;
/* 156 */       return new MapIndexingValueRef(state.getTypeConverter(), (Map)target, key, targetDescriptor);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 161 */     if (target.getClass().isArray() || target instanceof Collection || target instanceof String) {
/* 162 */       int idx = ((Integer)state.convertValue(index, TypeDescriptor.valueOf(Integer.class))).intValue();
/* 163 */       if (target.getClass().isArray()) {
/* 164 */         this.indexedType = IndexedType.ARRAY;
/* 165 */         return new ArrayIndexingValueRef(state.getTypeConverter(), target, idx, targetDescriptor);
/*     */       } 
/* 167 */       if (target instanceof Collection) {
/* 168 */         if (target instanceof List) {
/* 169 */           this.indexedType = IndexedType.LIST;
/*     */         }
/* 171 */         return new CollectionIndexingValueRef((Collection)target, idx, targetDescriptor, state
/* 172 */             .getTypeConverter(), state.getConfiguration().isAutoGrowCollections(), state
/* 173 */             .getConfiguration().getMaximumAutoGrowSize());
/*     */       } 
/*     */       
/* 176 */       this.indexedType = IndexedType.STRING;
/* 177 */       return new StringIndexingLValue((String)target, idx, targetDescriptor);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 183 */     TypeDescriptor valueType = indexValue.getTypeDescriptor();
/* 184 */     if (valueType != null && String.class == valueType.getType()) {
/* 185 */       this.indexedType = IndexedType.OBJECT;
/* 186 */       return new PropertyIndexingValueRef(target, (String)index, state
/* 187 */           .getEvaluationContext(), targetDescriptor);
/*     */     } 
/*     */     
/* 190 */     throw new SpelEvaluationException(
/* 191 */         getStartPosition(), SpelMessage.INDEXING_NOT_SUPPORTED_FOR_TYPE, new Object[] { targetDescriptor });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/* 196 */     if (this.indexedType == IndexedType.ARRAY) {
/* 197 */       return (this.exitTypeDescriptor != null);
/*     */     }
/* 199 */     if (this.indexedType == IndexedType.LIST) {
/* 200 */       return this.children[0].isCompilable();
/*     */     }
/* 202 */     if (this.indexedType == IndexedType.MAP) {
/* 203 */       return (this.children[0] instanceof PropertyOrFieldReference || this.children[0].isCompilable());
/*     */     }
/* 205 */     if (this.indexedType == IndexedType.OBJECT)
/*     */     {
/* 207 */       return (this.cachedReadAccessor != null && this.cachedReadAccessor instanceof ReflectivePropertyAccessor.OptimalPropertyAccessor && 
/*     */         
/* 209 */         getChild(0) instanceof StringLiteral);
/*     */     }
/* 211 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 216 */     String descriptor = cf.lastDescriptor();
/* 217 */     if (descriptor == null)
/*     */     {
/* 219 */       cf.loadTarget(mv);
/*     */     }
/*     */     
/* 222 */     if (this.indexedType == IndexedType.ARRAY) {
/*     */       int insn;
/* 224 */       if ("D".equals(this.exitTypeDescriptor)) {
/* 225 */         mv.visitTypeInsn(192, "[D");
/* 226 */         insn = 49;
/*     */       }
/* 228 */       else if ("F".equals(this.exitTypeDescriptor)) {
/* 229 */         mv.visitTypeInsn(192, "[F");
/* 230 */         insn = 48;
/*     */       }
/* 232 */       else if ("J".equals(this.exitTypeDescriptor)) {
/* 233 */         mv.visitTypeInsn(192, "[J");
/* 234 */         insn = 47;
/*     */       }
/* 236 */       else if ("I".equals(this.exitTypeDescriptor)) {
/* 237 */         mv.visitTypeInsn(192, "[I");
/* 238 */         insn = 46;
/*     */       }
/* 240 */       else if ("S".equals(this.exitTypeDescriptor)) {
/* 241 */         mv.visitTypeInsn(192, "[S");
/* 242 */         insn = 53;
/*     */       }
/* 244 */       else if ("B".equals(this.exitTypeDescriptor)) {
/* 245 */         mv.visitTypeInsn(192, "[B");
/* 246 */         insn = 51;
/*     */       }
/* 248 */       else if ("C".equals(this.exitTypeDescriptor)) {
/* 249 */         mv.visitTypeInsn(192, "[C");
/* 250 */         insn = 52;
/*     */       } else {
/*     */         
/* 253 */         mv.visitTypeInsn(192, "[" + this.exitTypeDescriptor + (
/* 254 */             CodeFlow.isPrimitiveArray(this.exitTypeDescriptor) ? "" : ";"));
/*     */         
/* 256 */         insn = 50;
/*     */       } 
/* 258 */       SpelNodeImpl index = this.children[0];
/* 259 */       cf.enterCompilationScope();
/* 260 */       index.generateCode(mv, cf);
/* 261 */       cf.exitCompilationScope();
/* 262 */       mv.visitInsn(insn);
/*     */     
/*     */     }
/* 265 */     else if (this.indexedType == IndexedType.LIST) {
/* 266 */       mv.visitTypeInsn(192, "java/util/List");
/* 267 */       cf.enterCompilationScope();
/* 268 */       this.children[0].generateCode(mv, cf);
/* 269 */       cf.exitCompilationScope();
/* 270 */       mv.visitMethodInsn(185, "java/util/List", "get", "(I)Ljava/lang/Object;", true);
/*     */     
/*     */     }
/* 273 */     else if (this.indexedType == IndexedType.MAP) {
/* 274 */       mv.visitTypeInsn(192, "java/util/Map");
/*     */ 
/*     */       
/* 277 */       if (this.children[0] instanceof PropertyOrFieldReference) {
/* 278 */         PropertyOrFieldReference reference = (PropertyOrFieldReference)this.children[0];
/* 279 */         String mapKeyName = reference.getName();
/* 280 */         mv.visitLdcInsn(mapKeyName);
/*     */       } else {
/*     */         
/* 283 */         cf.enterCompilationScope();
/* 284 */         this.children[0].generateCode(mv, cf);
/* 285 */         cf.exitCompilationScope();
/*     */       } 
/* 287 */       mv.visitMethodInsn(185, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
/*     */ 
/*     */     
/*     */     }
/* 291 */     else if (this.indexedType == IndexedType.OBJECT) {
/* 292 */       ReflectivePropertyAccessor.OptimalPropertyAccessor accessor = (ReflectivePropertyAccessor.OptimalPropertyAccessor)this.cachedReadAccessor;
/*     */       
/* 294 */       Assert.state((accessor != null), "No cached read accessor");
/* 295 */       Member member = accessor.member;
/* 296 */       boolean isStatic = Modifier.isStatic(member.getModifiers());
/* 297 */       String classDesc = member.getDeclaringClass().getName().replace('.', '/');
/*     */       
/* 299 */       if (!isStatic) {
/* 300 */         if (descriptor == null) {
/* 301 */           cf.loadTarget(mv);
/*     */         }
/* 303 */         if (descriptor == null || !classDesc.equals(descriptor.substring(1))) {
/* 304 */           mv.visitTypeInsn(192, classDesc);
/*     */         }
/*     */       } 
/*     */       
/* 308 */       if (member instanceof Method) {
/* 309 */         mv.visitMethodInsn(isStatic ? 184 : 182, classDesc, member.getName(), 
/* 310 */             CodeFlow.createSignatureDescriptor((Method)member), false);
/*     */       } else {
/*     */         
/* 313 */         mv.visitFieldInsn(isStatic ? 178 : 180, classDesc, member.getName(), 
/* 314 */             CodeFlow.toJvmDescriptor(((Field)member).getType()));
/*     */       } 
/*     */     } 
/*     */     
/* 318 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 323 */     StringBuilder sb = new StringBuilder("[");
/* 324 */     for (int i = 0; i < getChildCount(); i++) {
/* 325 */       if (i > 0) {
/* 326 */         sb.append(",");
/*     */       }
/* 328 */       sb.append(getChild(i).toStringAST());
/*     */     } 
/* 330 */     sb.append("]");
/* 331 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setArrayElement(TypeConverter converter, Object ctx, int idx, @Nullable Object newValue, Class<?> arrayComponentType) throws EvaluationException {
/* 338 */     if (arrayComponentType == boolean.class) {
/* 339 */       boolean[] array = (boolean[])ctx;
/* 340 */       checkAccess(array.length, idx);
/* 341 */       array[idx] = ((Boolean)convertValue(converter, newValue, (Class)Boolean.class)).booleanValue();
/*     */     }
/* 343 */     else if (arrayComponentType == byte.class) {
/* 344 */       byte[] array = (byte[])ctx;
/* 345 */       checkAccess(array.length, idx);
/* 346 */       array[idx] = ((Byte)convertValue(converter, newValue, (Class)Byte.class)).byteValue();
/*     */     }
/* 348 */     else if (arrayComponentType == char.class) {
/* 349 */       char[] array = (char[])ctx;
/* 350 */       checkAccess(array.length, idx);
/* 351 */       array[idx] = ((Character)convertValue(converter, newValue, (Class)Character.class)).charValue();
/*     */     }
/* 353 */     else if (arrayComponentType == double.class) {
/* 354 */       double[] array = (double[])ctx;
/* 355 */       checkAccess(array.length, idx);
/* 356 */       array[idx] = ((Double)convertValue(converter, newValue, (Class)Double.class)).doubleValue();
/*     */     }
/* 358 */     else if (arrayComponentType == float.class) {
/* 359 */       float[] array = (float[])ctx;
/* 360 */       checkAccess(array.length, idx);
/* 361 */       array[idx] = ((Float)convertValue(converter, newValue, (Class)Float.class)).floatValue();
/*     */     }
/* 363 */     else if (arrayComponentType == int.class) {
/* 364 */       int[] array = (int[])ctx;
/* 365 */       checkAccess(array.length, idx);
/* 366 */       array[idx] = ((Integer)convertValue(converter, newValue, (Class)Integer.class)).intValue();
/*     */     }
/* 368 */     else if (arrayComponentType == long.class) {
/* 369 */       long[] array = (long[])ctx;
/* 370 */       checkAccess(array.length, idx);
/* 371 */       array[idx] = ((Long)convertValue(converter, newValue, (Class)Long.class)).longValue();
/*     */     }
/* 373 */     else if (arrayComponentType == short.class) {
/* 374 */       short[] array = (short[])ctx;
/* 375 */       checkAccess(array.length, idx);
/* 376 */       array[idx] = ((Short)convertValue(converter, newValue, (Class)Short.class)).shortValue();
/*     */     } else {
/*     */       
/* 379 */       Object[] array = (Object[])ctx;
/* 380 */       checkAccess(array.length, idx);
/* 381 */       array[idx] = convertValue(converter, newValue, arrayComponentType);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Object accessArrayElement(Object ctx, int idx) throws SpelEvaluationException {
/* 386 */     Class<?> arrayComponentType = ctx.getClass().getComponentType();
/* 387 */     if (arrayComponentType == boolean.class) {
/* 388 */       boolean[] arrayOfBoolean = (boolean[])ctx;
/* 389 */       checkAccess(arrayOfBoolean.length, idx);
/* 390 */       this.exitTypeDescriptor = "Z";
/* 391 */       return Boolean.valueOf(arrayOfBoolean[idx]);
/*     */     } 
/* 393 */     if (arrayComponentType == byte.class) {
/* 394 */       byte[] arrayOfByte = (byte[])ctx;
/* 395 */       checkAccess(arrayOfByte.length, idx);
/* 396 */       this.exitTypeDescriptor = "B";
/* 397 */       return Byte.valueOf(arrayOfByte[idx]);
/*     */     } 
/* 399 */     if (arrayComponentType == char.class) {
/* 400 */       char[] arrayOfChar = (char[])ctx;
/* 401 */       checkAccess(arrayOfChar.length, idx);
/* 402 */       this.exitTypeDescriptor = "C";
/* 403 */       return Character.valueOf(arrayOfChar[idx]);
/*     */     } 
/* 405 */     if (arrayComponentType == double.class) {
/* 406 */       double[] arrayOfDouble = (double[])ctx;
/* 407 */       checkAccess(arrayOfDouble.length, idx);
/* 408 */       this.exitTypeDescriptor = "D";
/* 409 */       return Double.valueOf(arrayOfDouble[idx]);
/*     */     } 
/* 411 */     if (arrayComponentType == float.class) {
/* 412 */       float[] arrayOfFloat = (float[])ctx;
/* 413 */       checkAccess(arrayOfFloat.length, idx);
/* 414 */       this.exitTypeDescriptor = "F";
/* 415 */       return Float.valueOf(arrayOfFloat[idx]);
/*     */     } 
/* 417 */     if (arrayComponentType == int.class) {
/* 418 */       int[] arrayOfInt = (int[])ctx;
/* 419 */       checkAccess(arrayOfInt.length, idx);
/* 420 */       this.exitTypeDescriptor = "I";
/* 421 */       return Integer.valueOf(arrayOfInt[idx]);
/*     */     } 
/* 423 */     if (arrayComponentType == long.class) {
/* 424 */       long[] arrayOfLong = (long[])ctx;
/* 425 */       checkAccess(arrayOfLong.length, idx);
/* 426 */       this.exitTypeDescriptor = "J";
/* 427 */       return Long.valueOf(arrayOfLong[idx]);
/*     */     } 
/* 429 */     if (arrayComponentType == short.class) {
/* 430 */       short[] arrayOfShort = (short[])ctx;
/* 431 */       checkAccess(arrayOfShort.length, idx);
/* 432 */       this.exitTypeDescriptor = "S";
/* 433 */       return Short.valueOf(arrayOfShort[idx]);
/*     */     } 
/*     */     
/* 436 */     Object[] array = (Object[])ctx;
/* 437 */     checkAccess(array.length, idx);
/* 438 */     Object retValue = array[idx];
/* 439 */     this.exitTypeDescriptor = CodeFlow.toDescriptor(arrayComponentType);
/* 440 */     return retValue;
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkAccess(int arrayLength, int index) throws SpelEvaluationException {
/* 445 */     if (index > arrayLength) {
/* 446 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.ARRAY_INDEX_OUT_OF_BOUNDS, new Object[] {
/* 447 */             Integer.valueOf(arrayLength), Integer.valueOf(index)
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   private <T> T convertValue(TypeConverter converter, @Nullable Object value, Class<T> targetType) {
/* 453 */     T result = (T)converter.convertValue(value, 
/* 454 */         TypeDescriptor.forObject(value), TypeDescriptor.valueOf(targetType));
/* 455 */     if (result == null) {
/* 456 */       throw new IllegalStateException("Null conversion result for index [" + value + "]");
/*     */     }
/* 458 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private class ArrayIndexingValueRef
/*     */     implements ValueRef
/*     */   {
/*     */     private final TypeConverter typeConverter;
/*     */     
/*     */     private final Object array;
/*     */     
/*     */     private final int index;
/*     */     private final TypeDescriptor typeDescriptor;
/*     */     
/*     */     ArrayIndexingValueRef(TypeConverter typeConverter, Object array, int index, TypeDescriptor typeDescriptor) {
/* 473 */       this.typeConverter = typeConverter;
/* 474 */       this.array = array;
/* 475 */       this.index = index;
/* 476 */       this.typeDescriptor = typeDescriptor;
/*     */     }
/*     */ 
/*     */     
/*     */     public TypedValue getValue() {
/* 481 */       Object arrayElement = Indexer.this.accessArrayElement(this.array, this.index);
/* 482 */       return new TypedValue(arrayElement, this.typeDescriptor.elementTypeDescriptor(arrayElement));
/*     */     }
/*     */ 
/*     */     
/*     */     public void setValue(@Nullable Object newValue) {
/* 487 */       TypeDescriptor elementType = this.typeDescriptor.getElementTypeDescriptor();
/* 488 */       Assert.state((elementType != null), "No element type");
/* 489 */       Indexer.this.setArrayElement(this.typeConverter, this.array, this.index, newValue, elementType.getType());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isWritable() {
/* 494 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class MapIndexingValueRef
/*     */     implements ValueRef
/*     */   {
/*     */     private final TypeConverter typeConverter;
/*     */     
/*     */     private final Map map;
/*     */     
/*     */     @Nullable
/*     */     private final Object key;
/*     */     
/*     */     private final TypeDescriptor mapEntryDescriptor;
/*     */ 
/*     */     
/*     */     public MapIndexingValueRef(TypeConverter typeConverter, @Nullable Map map, Object key, TypeDescriptor mapEntryDescriptor) {
/* 514 */       this.typeConverter = typeConverter;
/* 515 */       this.map = map;
/* 516 */       this.key = key;
/* 517 */       this.mapEntryDescriptor = mapEntryDescriptor;
/*     */     }
/*     */ 
/*     */     
/*     */     public TypedValue getValue() {
/* 522 */       Object value = this.map.get(this.key);
/* 523 */       Indexer.this.exitTypeDescriptor = CodeFlow.toDescriptor(Object.class);
/* 524 */       return new TypedValue(value, this.mapEntryDescriptor.getMapValueTypeDescriptor(value));
/*     */     }
/*     */ 
/*     */     
/*     */     public void setValue(@Nullable Object newValue) {
/* 529 */       if (this.mapEntryDescriptor.getMapValueTypeDescriptor() != null) {
/* 530 */         newValue = this.typeConverter.convertValue(newValue, TypeDescriptor.forObject(newValue), this.mapEntryDescriptor
/* 531 */             .getMapValueTypeDescriptor());
/*     */       }
/* 533 */       this.map.put(this.key, newValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isWritable() {
/* 538 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class PropertyIndexingValueRef
/*     */     implements ValueRef
/*     */   {
/*     */     private final Object targetObject;
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private final EvaluationContext evaluationContext;
/*     */     
/*     */     private final TypeDescriptor targetObjectTypeDescriptor;
/*     */ 
/*     */     
/*     */     public PropertyIndexingValueRef(Object targetObject, String value, EvaluationContext evaluationContext, TypeDescriptor targetObjectTypeDescriptor) {
/* 556 */       this.targetObject = targetObject;
/* 557 */       this.name = value;
/* 558 */       this.evaluationContext = evaluationContext;
/* 559 */       this.targetObjectTypeDescriptor = targetObjectTypeDescriptor;
/*     */     }
/*     */ 
/*     */     
/*     */     public TypedValue getValue() {
/* 564 */       Class<?> targetObjectRuntimeClass = Indexer.this.getObjectClass(this.targetObject);
/*     */       try {
/* 566 */         if (Indexer.this.cachedReadName != null && Indexer.this.cachedReadName.equals(this.name) && Indexer.this
/* 567 */           .cachedReadTargetType != null && Indexer.this
/* 568 */           .cachedReadTargetType.equals(targetObjectRuntimeClass)) {
/*     */           
/* 570 */           PropertyAccessor accessor = Indexer.this.cachedReadAccessor;
/* 571 */           Assert.state((accessor != null), "No cached read accessor");
/* 572 */           return accessor.read(this.evaluationContext, this.targetObject, this.name);
/*     */         } 
/* 574 */         List<PropertyAccessor> accessorsToTry = AstUtils.getPropertyAccessorsToTry(targetObjectRuntimeClass, this.evaluationContext
/* 575 */             .getPropertyAccessors());
/* 576 */         for (PropertyAccessor accessor : accessorsToTry) {
/* 577 */           if (accessor.canRead(this.evaluationContext, this.targetObject, this.name)) {
/* 578 */             if (accessor instanceof ReflectivePropertyAccessor) {
/* 579 */               accessor = ((ReflectivePropertyAccessor)accessor).createOptimalAccessor(this.evaluationContext, this.targetObject, this.name);
/*     */             }
/*     */             
/* 582 */             Indexer.this.cachedReadAccessor = accessor;
/* 583 */             Indexer.this.cachedReadName = this.name;
/* 584 */             Indexer.this.cachedReadTargetType = targetObjectRuntimeClass;
/* 585 */             if (accessor instanceof ReflectivePropertyAccessor.OptimalPropertyAccessor) {
/* 586 */               ReflectivePropertyAccessor.OptimalPropertyAccessor optimalAccessor = (ReflectivePropertyAccessor.OptimalPropertyAccessor)accessor;
/*     */               
/* 588 */               Member member = optimalAccessor.member;
/* 589 */               Indexer.this.exitTypeDescriptor = CodeFlow.toDescriptor((member instanceof Method) ? ((Method)member)
/* 590 */                   .getReturnType() : ((Field)member).getType());
/*     */             } 
/* 592 */             return accessor.read(this.evaluationContext, this.targetObject, this.name);
/*     */           }
/*     */         
/*     */         } 
/* 596 */       } catch (AccessException ex) {
/* 597 */         throw new SpelEvaluationException(Indexer.this.getStartPosition(), ex, SpelMessage.INDEXING_NOT_SUPPORTED_FOR_TYPE, new Object[] { this.targetObjectTypeDescriptor
/* 598 */               .toString() });
/*     */       } 
/* 600 */       throw new SpelEvaluationException(Indexer.this.getStartPosition(), SpelMessage.INDEXING_NOT_SUPPORTED_FOR_TYPE, new Object[] { this.targetObjectTypeDescriptor
/* 601 */             .toString() });
/*     */     }
/*     */ 
/*     */     
/*     */     public void setValue(@Nullable Object newValue) {
/* 606 */       Class<?> contextObjectClass = Indexer.this.getObjectClass(this.targetObject);
/*     */       try {
/* 608 */         if (Indexer.this.cachedWriteName != null && Indexer.this.cachedWriteName.equals(this.name) && Indexer.this
/* 609 */           .cachedWriteTargetType != null && Indexer.this
/* 610 */           .cachedWriteTargetType.equals(contextObjectClass)) {
/*     */           
/* 612 */           PropertyAccessor accessor = Indexer.this.cachedWriteAccessor;
/* 613 */           Assert.state((accessor != null), "No cached write accessor");
/* 614 */           accessor.write(this.evaluationContext, this.targetObject, this.name, newValue);
/*     */           return;
/*     */         } 
/* 617 */         List<PropertyAccessor> accessorsToTry = AstUtils.getPropertyAccessorsToTry(contextObjectClass, this.evaluationContext
/* 618 */             .getPropertyAccessors());
/* 619 */         for (PropertyAccessor accessor : accessorsToTry) {
/* 620 */           if (accessor.canWrite(this.evaluationContext, this.targetObject, this.name)) {
/* 621 */             Indexer.this.cachedWriteName = this.name;
/* 622 */             Indexer.this.cachedWriteTargetType = contextObjectClass;
/* 623 */             Indexer.this.cachedWriteAccessor = accessor;
/* 624 */             accessor.write(this.evaluationContext, this.targetObject, this.name, newValue);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 629 */       } catch (AccessException ex) {
/* 630 */         throw new SpelEvaluationException(Indexer.this.getStartPosition(), ex, SpelMessage.EXCEPTION_DURING_PROPERTY_WRITE, new Object[] { this.name, ex
/* 631 */               .getMessage() });
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isWritable() {
/* 637 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class CollectionIndexingValueRef
/*     */     implements ValueRef
/*     */   {
/*     */     private final Collection collection;
/*     */     
/*     */     private final int index;
/*     */     
/*     */     private final TypeDescriptor collectionEntryDescriptor;
/*     */     
/*     */     private final TypeConverter typeConverter;
/*     */     
/*     */     private final boolean growCollection;
/*     */     
/*     */     private final int maximumSize;
/*     */ 
/*     */     
/*     */     public CollectionIndexingValueRef(Collection collection, int index, TypeDescriptor collectionEntryDescriptor, TypeConverter typeConverter, boolean growCollection, int maximumSize) {
/* 660 */       this.collection = collection;
/* 661 */       this.index = index;
/* 662 */       this.collectionEntryDescriptor = collectionEntryDescriptor;
/* 663 */       this.typeConverter = typeConverter;
/* 664 */       this.growCollection = growCollection;
/* 665 */       this.maximumSize = maximumSize;
/*     */     }
/*     */ 
/*     */     
/*     */     public TypedValue getValue() {
/* 670 */       growCollectionIfNecessary();
/* 671 */       if (this.collection instanceof List) {
/* 672 */         Object o = ((List)this.collection).get(this.index);
/* 673 */         Indexer.this.exitTypeDescriptor = CodeFlow.toDescriptor(Object.class);
/* 674 */         return new TypedValue(o, this.collectionEntryDescriptor.elementTypeDescriptor(o));
/*     */       } 
/* 676 */       int pos = 0;
/* 677 */       for (Object o : this.collection) {
/* 678 */         if (pos == this.index) {
/* 679 */           return new TypedValue(o, this.collectionEntryDescriptor.elementTypeDescriptor(o));
/*     */         }
/* 681 */         pos++;
/*     */       } 
/* 683 */       throw new IllegalStateException("Failed to find indexed element " + this.index + ": " + this.collection);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setValue(@Nullable Object newValue) {
/* 688 */       growCollectionIfNecessary();
/* 689 */       if (this.collection instanceof List) {
/* 690 */         List<Object> list = (List)this.collection;
/* 691 */         if (this.collectionEntryDescriptor.getElementTypeDescriptor() != null) {
/* 692 */           newValue = this.typeConverter.convertValue(newValue, TypeDescriptor.forObject(newValue), this.collectionEntryDescriptor
/* 693 */               .getElementTypeDescriptor());
/*     */         }
/* 695 */         list.set(this.index, newValue);
/*     */       } else {
/*     */         
/* 698 */         throw new SpelEvaluationException(Indexer.this.getStartPosition(), SpelMessage.INDEXING_NOT_SUPPORTED_FOR_TYPE, new Object[] { this.collectionEntryDescriptor
/* 699 */               .toString() });
/*     */       } 
/*     */     }
/*     */     
/*     */     private void growCollectionIfNecessary() {
/* 704 */       if (this.index >= this.collection.size()) {
/* 705 */         if (!this.growCollection)
/* 706 */           throw new SpelEvaluationException(Indexer.this.getStartPosition(), SpelMessage.COLLECTION_INDEX_OUT_OF_BOUNDS, new Object[] {
/* 707 */                 Integer.valueOf(this.collection.size()), Integer.valueOf(this.index)
/*     */               }); 
/* 709 */         if (this.index >= this.maximumSize) {
/* 710 */           throw new SpelEvaluationException(Indexer.this.getStartPosition(), SpelMessage.UNABLE_TO_GROW_COLLECTION, new Object[0]);
/*     */         }
/* 712 */         if (this.collectionEntryDescriptor.getElementTypeDescriptor() == null) {
/* 713 */           throw new SpelEvaluationException(Indexer.this
/* 714 */               .getStartPosition(), SpelMessage.UNABLE_TO_GROW_COLLECTION_UNKNOWN_ELEMENT_TYPE, new Object[0]);
/*     */         }
/* 716 */         TypeDescriptor elementType = this.collectionEntryDescriptor.getElementTypeDescriptor();
/*     */         try {
/* 718 */           Constructor<?> ctor = ReflectionUtils.accessibleConstructor(elementType.getType(), new Class[0]);
/* 719 */           int newElements = this.index - this.collection.size();
/* 720 */           while (newElements >= 0) {
/* 721 */             this.collection.add(ctor.newInstance(new Object[0]));
/* 722 */             newElements--;
/*     */           }
/*     */         
/* 725 */         } catch (Throwable ex) {
/* 726 */           throw new SpelEvaluationException(Indexer.this.getStartPosition(), ex, SpelMessage.UNABLE_TO_GROW_COLLECTION, new Object[0]);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isWritable() {
/* 733 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class StringIndexingLValue
/*     */     implements ValueRef
/*     */   {
/*     */     private final String target;
/*     */     
/*     */     private final int index;
/*     */     private final TypeDescriptor typeDescriptor;
/*     */     
/*     */     public StringIndexingLValue(String target, int index, TypeDescriptor typeDescriptor) {
/* 747 */       this.target = target;
/* 748 */       this.index = index;
/* 749 */       this.typeDescriptor = typeDescriptor;
/*     */     }
/*     */ 
/*     */     
/*     */     public TypedValue getValue() {
/* 754 */       if (this.index >= this.target.length())
/* 755 */         throw new SpelEvaluationException(Indexer.this.getStartPosition(), SpelMessage.STRING_INDEX_OUT_OF_BOUNDS, new Object[] {
/* 756 */               Integer.valueOf(this.target.length()), Integer.valueOf(this.index)
/*     */             }); 
/* 758 */       return new TypedValue(String.valueOf(this.target.charAt(this.index)));
/*     */     }
/*     */ 
/*     */     
/*     */     public void setValue(@Nullable Object newValue) {
/* 763 */       throw new SpelEvaluationException(Indexer.this.getStartPosition(), SpelMessage.INDEXING_NOT_SUPPORTED_FOR_TYPE, new Object[] { this.typeDescriptor
/* 764 */             .toString() });
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isWritable() {
/* 769 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/spel/ast/Indexer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */