/*     */ package org.springframework.expression.spel.support;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.Property;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.PropertyAccessor;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.CompilablePropertyAccessor;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReflectivePropertyAccessor
/*     */   implements PropertyAccessor
/*     */ {
/*  65 */   private static final Set<Class<?>> ANY_TYPES = Collections.emptySet();
/*     */   private static final Set<Class<?>> BOOLEAN_TYPES;
/*     */   private final boolean allowWrite;
/*     */   
/*     */   static {
/*  70 */     Set<Class<?>> booleanTypes = new HashSet<>(4);
/*  71 */     booleanTypes.add(Boolean.class);
/*  72 */     booleanTypes.add(boolean.class);
/*  73 */     BOOLEAN_TYPES = Collections.unmodifiableSet(booleanTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   private final Map<PropertyCacheKey, InvokerPair> readerCache = new ConcurrentHashMap<>(64);
/*     */   
/*  81 */   private final Map<PropertyCacheKey, Member> writerCache = new ConcurrentHashMap<>(64);
/*     */   
/*  83 */   private final Map<PropertyCacheKey, TypeDescriptor> typeDescriptorCache = new ConcurrentHashMap<>(64);
/*     */   
/*  85 */   private final Map<Class<?>, Method[]> sortedMethodsCache = (Map)new ConcurrentHashMap<>(64);
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private volatile InvokerPair lastReadInvokerPair;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReflectivePropertyAccessor() {
/*  96 */     this.allowWrite = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReflectivePropertyAccessor(boolean allowWrite) {
/* 106 */     this.allowWrite = allowWrite;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?>[] getSpecificTargetClasses() {
/* 116 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canRead(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
/* 121 */     if (target == null) {
/* 122 */       return false;
/*     */     }
/*     */     
/* 125 */     Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/* 126 */     if (type.isArray() && name.equals("length")) {
/* 127 */       return true;
/*     */     }
/*     */     
/* 130 */     PropertyCacheKey cacheKey = new PropertyCacheKey(type, name, target instanceof Class);
/* 131 */     if (this.readerCache.containsKey(cacheKey)) {
/* 132 */       return true;
/*     */     }
/*     */     
/* 135 */     Method method = findGetterForProperty(name, type, target);
/* 136 */     if (method != null) {
/*     */ 
/*     */       
/* 139 */       Property property = new Property(type, method, null);
/* 140 */       TypeDescriptor typeDescriptor = new TypeDescriptor(property);
/* 141 */       method = ClassUtils.getInterfaceMethodIfPossible(method);
/* 142 */       this.readerCache.put(cacheKey, new InvokerPair(method, typeDescriptor));
/* 143 */       this.typeDescriptorCache.put(cacheKey, typeDescriptor);
/* 144 */       return true;
/*     */     } 
/*     */     
/* 147 */     Field field = findField(name, type, target);
/* 148 */     if (field != null) {
/* 149 */       TypeDescriptor typeDescriptor = new TypeDescriptor(field);
/* 150 */       this.readerCache.put(cacheKey, new InvokerPair(field, typeDescriptor));
/* 151 */       this.typeDescriptorCache.put(cacheKey, typeDescriptor);
/* 152 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 156 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypedValue read(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
/* 161 */     Assert.state((target != null), "Target must not be null");
/* 162 */     Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/*     */     
/* 164 */     if (type.isArray() && name.equals("length")) {
/* 165 */       if (target instanceof Class) {
/* 166 */         throw new AccessException("Cannot access length on array class itself");
/*     */       }
/* 168 */       return new TypedValue(Integer.valueOf(Array.getLength(target)));
/*     */     } 
/*     */     
/* 171 */     PropertyCacheKey cacheKey = new PropertyCacheKey(type, name, target instanceof Class);
/* 172 */     InvokerPair invoker = this.readerCache.get(cacheKey);
/* 173 */     this.lastReadInvokerPair = invoker;
/*     */     
/* 175 */     if (invoker == null || invoker.member instanceof Method) {
/* 176 */       Method method = (invoker != null) ? (Method)invoker.member : null;
/* 177 */       if (method == null) {
/* 178 */         method = findGetterForProperty(name, type, target);
/* 179 */         if (method != null) {
/*     */ 
/*     */           
/* 182 */           Property property = new Property(type, method, null);
/* 183 */           TypeDescriptor typeDescriptor = new TypeDescriptor(property);
/* 184 */           method = ClassUtils.getInterfaceMethodIfPossible(method);
/* 185 */           invoker = new InvokerPair(method, typeDescriptor);
/* 186 */           this.lastReadInvokerPair = invoker;
/* 187 */           this.readerCache.put(cacheKey, invoker);
/*     */         } 
/*     */       } 
/* 190 */       if (method != null) {
/*     */         try {
/* 192 */           ReflectionUtils.makeAccessible(method);
/* 193 */           Object value = method.invoke(target, new Object[0]);
/* 194 */           return new TypedValue(value, invoker.typeDescriptor.narrow(value));
/*     */         }
/* 196 */         catch (Exception ex) {
/* 197 */           throw new AccessException("Unable to access property '" + name + "' through getter method", ex);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 202 */     if (invoker == null || invoker.member instanceof Field) {
/* 203 */       Field field = (invoker == null) ? null : (Field)invoker.member;
/* 204 */       if (field == null) {
/* 205 */         field = findField(name, type, target);
/* 206 */         if (field != null) {
/* 207 */           invoker = new InvokerPair(field, new TypeDescriptor(field));
/* 208 */           this.lastReadInvokerPair = invoker;
/* 209 */           this.readerCache.put(cacheKey, invoker);
/*     */         } 
/*     */       } 
/* 212 */       if (field != null) {
/*     */         try {
/* 214 */           ReflectionUtils.makeAccessible(field);
/* 215 */           Object value = field.get(target);
/* 216 */           return new TypedValue(value, invoker.typeDescriptor.narrow(value));
/*     */         }
/* 218 */         catch (Exception ex) {
/* 219 */           throw new AccessException("Unable to access field '" + name + "'", ex);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 224 */     throw new AccessException("Neither getter method nor field found for property '" + name + "'");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canWrite(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
/* 229 */     if (!this.allowWrite || target == null) {
/* 230 */       return false;
/*     */     }
/*     */     
/* 233 */     Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/* 234 */     PropertyCacheKey cacheKey = new PropertyCacheKey(type, name, target instanceof Class);
/* 235 */     if (this.writerCache.containsKey(cacheKey)) {
/* 236 */       return true;
/*     */     }
/*     */     
/* 239 */     Method method = findSetterForProperty(name, type, target);
/* 240 */     if (method != null) {
/*     */       
/* 242 */       Property property = new Property(type, null, method);
/* 243 */       TypeDescriptor typeDescriptor = new TypeDescriptor(property);
/* 244 */       method = ClassUtils.getInterfaceMethodIfPossible(method);
/* 245 */       this.writerCache.put(cacheKey, method);
/* 246 */       this.typeDescriptorCache.put(cacheKey, typeDescriptor);
/* 247 */       return true;
/*     */     } 
/*     */     
/* 250 */     Field field = findField(name, type, target);
/* 251 */     if (field != null) {
/* 252 */       this.writerCache.put(cacheKey, field);
/* 253 */       this.typeDescriptorCache.put(cacheKey, new TypeDescriptor(field));
/* 254 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 258 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(EvaluationContext context, @Nullable Object target, String name, @Nullable Object newValue) throws AccessException {
/* 265 */     if (!this.allowWrite) {
/* 266 */       throw new AccessException("PropertyAccessor for property '" + name + "' on target [" + target + "] does not allow write operations");
/*     */     }
/*     */ 
/*     */     
/* 270 */     Assert.state((target != null), "Target must not be null");
/* 271 */     Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/*     */     
/* 273 */     Object possiblyConvertedNewValue = newValue;
/* 274 */     TypeDescriptor typeDescriptor = getTypeDescriptor(context, target, name);
/* 275 */     if (typeDescriptor != null) {
/*     */       try {
/* 277 */         possiblyConvertedNewValue = context.getTypeConverter().convertValue(newValue, 
/* 278 */             TypeDescriptor.forObject(newValue), typeDescriptor);
/*     */       }
/* 280 */       catch (EvaluationException evaluationException) {
/* 281 */         throw new AccessException("Type conversion failure", evaluationException);
/*     */       } 
/*     */     }
/*     */     
/* 285 */     PropertyCacheKey cacheKey = new PropertyCacheKey(type, name, target instanceof Class);
/* 286 */     Member cachedMember = this.writerCache.get(cacheKey);
/*     */     
/* 288 */     if (cachedMember == null || cachedMember instanceof Method) {
/* 289 */       Method method = (Method)cachedMember;
/* 290 */       if (method == null) {
/* 291 */         method = findSetterForProperty(name, type, target);
/* 292 */         if (method != null) {
/* 293 */           method = ClassUtils.getInterfaceMethodIfPossible(method);
/* 294 */           cachedMember = method;
/* 295 */           this.writerCache.put(cacheKey, cachedMember);
/*     */         } 
/*     */       } 
/* 298 */       if (method != null) {
/*     */         try {
/* 300 */           ReflectionUtils.makeAccessible(method);
/* 301 */           method.invoke(target, new Object[] { possiblyConvertedNewValue });
/*     */           
/*     */           return;
/* 304 */         } catch (Exception ex) {
/* 305 */           throw new AccessException("Unable to access property '" + name + "' through setter method", ex);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 310 */     if (cachedMember == null || cachedMember instanceof Field) {
/* 311 */       Field field = (Field)cachedMember;
/* 312 */       if (field == null) {
/* 313 */         field = findField(name, type, target);
/* 314 */         if (field != null) {
/* 315 */           cachedMember = field;
/* 316 */           this.writerCache.put(cacheKey, cachedMember);
/*     */         } 
/*     */       } 
/* 319 */       if (field != null) {
/*     */         try {
/* 321 */           ReflectionUtils.makeAccessible(field);
/* 322 */           field.set(target, possiblyConvertedNewValue);
/*     */           
/*     */           return;
/* 325 */         } catch (Exception ex) {
/* 326 */           throw new AccessException("Unable to access field '" + name + "'", ex);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 331 */     throw new AccessException("Neither setter method nor field found for property '" + name + "'");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @Nullable
/*     */   public Member getLastReadInvokerPair() {
/* 341 */     InvokerPair lastReadInvoker = this.lastReadInvokerPair;
/* 342 */     return (lastReadInvoker != null) ? lastReadInvoker.member : null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private TypeDescriptor getTypeDescriptor(EvaluationContext context, Object target, String name) {
/* 348 */     Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/*     */     
/* 350 */     if (type.isArray() && name.equals("length")) {
/* 351 */       return TypeDescriptor.valueOf(int.class);
/*     */     }
/* 353 */     PropertyCacheKey cacheKey = new PropertyCacheKey(type, name, target instanceof Class);
/* 354 */     TypeDescriptor typeDescriptor = this.typeDescriptorCache.get(cacheKey);
/* 355 */     if (typeDescriptor == null) {
/*     */       
/*     */       try {
/* 358 */         if (canRead(context, target, name) || canWrite(context, target, name)) {
/* 359 */           typeDescriptor = this.typeDescriptorCache.get(cacheKey);
/*     */         }
/*     */       }
/* 362 */       catch (AccessException accessException) {}
/*     */     }
/*     */ 
/*     */     
/* 366 */     return typeDescriptor;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Method findGetterForProperty(String propertyName, Class<?> clazz, Object target) {
/* 371 */     Method method = findGetterForProperty(propertyName, clazz, target instanceof Class);
/* 372 */     if (method == null && target instanceof Class) {
/* 373 */       method = findGetterForProperty(propertyName, target.getClass(), false);
/*     */     }
/* 375 */     return method;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Method findSetterForProperty(String propertyName, Class<?> clazz, Object target) {
/* 380 */     Method method = findSetterForProperty(propertyName, clazz, target instanceof Class);
/* 381 */     if (method == null && target instanceof Class) {
/* 382 */       method = findSetterForProperty(propertyName, target.getClass(), false);
/*     */     }
/* 384 */     return method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Method findGetterForProperty(String propertyName, Class<?> clazz, boolean mustBeStatic) {
/* 392 */     Method method = findMethodForProperty(getPropertyMethodSuffixes(propertyName), "get", clazz, mustBeStatic, 0, ANY_TYPES);
/*     */     
/* 394 */     if (method == null) {
/* 395 */       method = findMethodForProperty(getPropertyMethodSuffixes(propertyName), "is", clazz, mustBeStatic, 0, BOOLEAN_TYPES);
/*     */     }
/*     */     
/* 398 */     return method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Method findSetterForProperty(String propertyName, Class<?> clazz, boolean mustBeStatic) {
/* 406 */     return findMethodForProperty(getPropertyMethodSuffixes(propertyName), "set", clazz, mustBeStatic, 1, ANY_TYPES);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Method findMethodForProperty(String[] methodSuffixes, String prefix, Class<?> clazz, boolean mustBeStatic, int numberOfParams, Set<Class<?>> requiredReturnTypes) {
/* 414 */     Method[] methods = getSortedMethods(clazz);
/* 415 */     for (String methodSuffix : methodSuffixes) {
/* 416 */       for (Method method : methods) {
/* 417 */         if (isCandidateForProperty(method, clazz) && method.getName().equals(prefix + methodSuffix) && method
/* 418 */           .getParameterCount() == numberOfParams && (!mustBeStatic || 
/* 419 */           Modifier.isStatic(method.getModifiers())) && (requiredReturnTypes
/* 420 */           .isEmpty() || requiredReturnTypes.contains(method.getReturnType()))) {
/* 421 */           return method;
/*     */         }
/*     */       } 
/*     */     } 
/* 425 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Method[] getSortedMethods(Class<?> clazz) {
/* 432 */     return this.sortedMethodsCache.computeIfAbsent(clazz, key -> {
/*     */           Method[] methods = key.getMethods();
/*     */           Arrays.sort(methods, ());
/*     */           return methods;
/*     */         });
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
/*     */   protected boolean isCandidateForProperty(Method method, Class<?> targetClass) {
/* 449 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String[] getPropertyMethodSuffixes(String propertyName) {
/* 459 */     String suffix = getPropertyMethodSuffix(propertyName);
/* 460 */     if (suffix.length() > 0 && Character.isUpperCase(suffix.charAt(0))) {
/* 461 */       return new String[] { suffix };
/*     */     }
/* 463 */     return new String[] { suffix, StringUtils.capitalize(suffix) };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getPropertyMethodSuffix(String propertyName) {
/* 471 */     if (propertyName.length() > 1 && Character.isUpperCase(propertyName.charAt(1))) {
/* 472 */       return propertyName;
/*     */     }
/* 474 */     return StringUtils.capitalize(propertyName);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Field findField(String name, Class<?> clazz, Object target) {
/* 479 */     Field field = findField(name, clazz, target instanceof Class);
/* 480 */     if (field == null && target instanceof Class) {
/* 481 */       field = findField(name, target.getClass(), false);
/*     */     }
/* 483 */     return field;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Field findField(String name, Class<?> clazz, boolean mustBeStatic) {
/* 491 */     Field[] fields = clazz.getFields();
/* 492 */     for (Field field : fields) {
/* 493 */       if (field.getName().equals(name) && (!mustBeStatic || Modifier.isStatic(field.getModifiers()))) {
/* 494 */         return field;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 499 */     if (clazz.getSuperclass() != null) {
/* 500 */       Field field = findField(name, clazz.getSuperclass(), mustBeStatic);
/* 501 */       if (field != null) {
/* 502 */         return field;
/*     */       }
/*     */     } 
/* 505 */     for (Class<?> implementedInterface : clazz.getInterfaces()) {
/* 506 */       Field field = findField(name, implementedInterface, mustBeStatic);
/* 507 */       if (field != null) {
/* 508 */         return field;
/*     */       }
/*     */     } 
/* 511 */     return null;
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
/*     */   public PropertyAccessor createOptimalAccessor(EvaluationContext context, @Nullable Object target, String name) {
/* 527 */     if (target == null) {
/* 528 */       return this;
/*     */     }
/* 530 */     Class<?> clazz = (target instanceof Class) ? (Class)target : target.getClass();
/* 531 */     if (clazz.isArray()) {
/* 532 */       return this;
/*     */     }
/*     */     
/* 535 */     PropertyCacheKey cacheKey = new PropertyCacheKey(clazz, name, target instanceof Class);
/* 536 */     InvokerPair invocationTarget = this.readerCache.get(cacheKey);
/*     */     
/* 538 */     if (invocationTarget == null || invocationTarget.member instanceof Method) {
/* 539 */       Method method = (invocationTarget != null) ? (Method)invocationTarget.member : null;
/* 540 */       if (method == null) {
/* 541 */         method = findGetterForProperty(name, clazz, target);
/* 542 */         if (method != null) {
/* 543 */           TypeDescriptor typeDescriptor = new TypeDescriptor(new MethodParameter(method, -1));
/* 544 */           method = ClassUtils.getInterfaceMethodIfPossible(method);
/* 545 */           invocationTarget = new InvokerPair(method, typeDescriptor);
/* 546 */           ReflectionUtils.makeAccessible(method);
/* 547 */           this.readerCache.put(cacheKey, invocationTarget);
/*     */         } 
/*     */       } 
/* 550 */       if (method != null) {
/* 551 */         return (PropertyAccessor)new OptimalPropertyAccessor(invocationTarget);
/*     */       }
/*     */     } 
/*     */     
/* 555 */     if (invocationTarget == null || invocationTarget.member instanceof Field) {
/* 556 */       Field field = (invocationTarget != null) ? (Field)invocationTarget.member : null;
/* 557 */       if (field == null) {
/* 558 */         field = findField(name, clazz, target instanceof Class);
/* 559 */         if (field != null) {
/* 560 */           invocationTarget = new InvokerPair(field, new TypeDescriptor(field));
/* 561 */           ReflectionUtils.makeAccessible(field);
/* 562 */           this.readerCache.put(cacheKey, invocationTarget);
/*     */         } 
/*     */       } 
/* 565 */       if (field != null) {
/* 566 */         return (PropertyAccessor)new OptimalPropertyAccessor(invocationTarget);
/*     */       }
/*     */     } 
/*     */     
/* 570 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class InvokerPair
/*     */   {
/*     */     final Member member;
/*     */ 
/*     */     
/*     */     final TypeDescriptor typeDescriptor;
/*     */ 
/*     */ 
/*     */     
/*     */     public InvokerPair(Member member, TypeDescriptor typeDescriptor) {
/* 585 */       this.member = member;
/* 586 */       this.typeDescriptor = typeDescriptor;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class PropertyCacheKey
/*     */     implements Comparable<PropertyCacheKey>
/*     */   {
/*     */     private final Class<?> clazz;
/*     */     
/*     */     private final String property;
/*     */     private boolean targetIsClass;
/*     */     
/*     */     public PropertyCacheKey(Class<?> clazz, String name, boolean targetIsClass) {
/* 600 */       this.clazz = clazz;
/* 601 */       this.property = name;
/* 602 */       this.targetIsClass = targetIsClass;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 607 */       if (this == other) {
/* 608 */         return true;
/*     */       }
/* 610 */       if (!(other instanceof PropertyCacheKey)) {
/* 611 */         return false;
/*     */       }
/* 613 */       PropertyCacheKey otherKey = (PropertyCacheKey)other;
/* 614 */       return (this.clazz == otherKey.clazz && this.property.equals(otherKey.property) && this.targetIsClass == otherKey.targetIsClass);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 620 */       return this.clazz.hashCode() * 29 + this.property.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 625 */       return "CacheKey [clazz=" + this.clazz.getName() + ", property=" + this.property + ", " + this.property + ", targetIsClass=" + this.targetIsClass + "]";
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int compareTo(PropertyCacheKey other) {
/* 631 */       int result = this.clazz.getName().compareTo(other.clazz.getName());
/* 632 */       if (result == 0) {
/* 633 */         result = this.property.compareTo(other.property);
/*     */       }
/* 635 */       return result;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class OptimalPropertyAccessor
/*     */     implements CompilablePropertyAccessor
/*     */   {
/*     */     public final Member member;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final TypeDescriptor typeDescriptor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     OptimalPropertyAccessor(ReflectivePropertyAccessor.InvokerPair target) {
/* 658 */       this.member = target.member;
/* 659 */       this.typeDescriptor = target.typeDescriptor;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Class<?>[] getSpecificTargetClasses() {
/* 665 */       throw new UnsupportedOperationException("Should not be called on an OptimalPropertyAccessor");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canRead(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
/* 670 */       if (target == null) {
/* 671 */         return false;
/*     */       }
/* 673 */       Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/* 674 */       if (type.isArray()) {
/* 675 */         return false;
/*     */       }
/*     */       
/* 678 */       if (this.member instanceof Method) {
/* 679 */         Method method = (Method)this.member;
/* 680 */         String getterName = "get" + StringUtils.capitalize(name);
/* 681 */         if (getterName.equals(method.getName())) {
/* 682 */           return true;
/*     */         }
/* 684 */         getterName = "is" + StringUtils.capitalize(name);
/* 685 */         return getterName.equals(method.getName());
/*     */       } 
/*     */       
/* 688 */       Field field = (Field)this.member;
/* 689 */       return field.getName().equals(name);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public TypedValue read(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
/* 695 */       if (this.member instanceof Method) {
/* 696 */         Method method = (Method)this.member;
/*     */         try {
/* 698 */           ReflectionUtils.makeAccessible(method);
/* 699 */           Object value = method.invoke(target, new Object[0]);
/* 700 */           return new TypedValue(value, this.typeDescriptor.narrow(value));
/*     */         }
/* 702 */         catch (Exception ex) {
/* 703 */           throw new AccessException("Unable to access property '" + name + "' through getter method", ex);
/*     */         } 
/*     */       } 
/*     */       
/* 707 */       Field field = (Field)this.member;
/*     */       try {
/* 709 */         ReflectionUtils.makeAccessible(field);
/* 710 */         Object value = field.get(target);
/* 711 */         return new TypedValue(value, this.typeDescriptor.narrow(value));
/*     */       }
/* 713 */       catch (Exception ex) {
/* 714 */         throw new AccessException("Unable to access field '" + name + "'", ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean canWrite(EvaluationContext context, @Nullable Object target, String name) {
/* 721 */       throw new UnsupportedOperationException("Should not be called on an OptimalPropertyAccessor");
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(EvaluationContext context, @Nullable Object target, String name, @Nullable Object newValue) {
/* 726 */       throw new UnsupportedOperationException("Should not be called on an OptimalPropertyAccessor");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isCompilable() {
/* 731 */       return (Modifier.isPublic(this.member.getModifiers()) && 
/* 732 */         Modifier.isPublic(this.member.getDeclaringClass().getModifiers()));
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getPropertyType() {
/* 737 */       if (this.member instanceof Method) {
/* 738 */         return ((Method)this.member).getReturnType();
/*     */       }
/*     */       
/* 741 */       return ((Field)this.member).getType();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void generateCode(String propertyName, MethodVisitor mv, CodeFlow cf) {
/* 747 */       boolean isStatic = Modifier.isStatic(this.member.getModifiers());
/* 748 */       String descriptor = cf.lastDescriptor();
/* 749 */       String classDesc = this.member.getDeclaringClass().getName().replace('.', '/');
/*     */       
/* 751 */       if (!isStatic) {
/* 752 */         if (descriptor == null) {
/* 753 */           cf.loadTarget(mv);
/*     */         }
/* 755 */         if (descriptor == null || !classDesc.equals(descriptor.substring(1))) {
/* 756 */           mv.visitTypeInsn(192, classDesc);
/*     */         
/*     */         }
/*     */       }
/* 760 */       else if (descriptor != null) {
/*     */ 
/*     */         
/* 763 */         mv.visitInsn(87);
/*     */       } 
/*     */ 
/*     */       
/* 767 */       if (this.member instanceof Method) {
/* 768 */         mv.visitMethodInsn(isStatic ? 184 : 182, classDesc, this.member.getName(), 
/* 769 */             CodeFlow.createSignatureDescriptor((Method)this.member), false);
/*     */       } else {
/*     */         
/* 772 */         mv.visitFieldInsn(isStatic ? 178 : 180, classDesc, this.member.getName(), 
/* 773 */             CodeFlow.toJvmDescriptor(((Field)this.member).getType()));
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/spel/support/ReflectivePropertyAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */