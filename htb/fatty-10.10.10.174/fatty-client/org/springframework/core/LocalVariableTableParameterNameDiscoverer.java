/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.asm.ClassReader;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class LocalVariableTableParameterNameDiscoverer
/*     */   implements ParameterNameDiscoverer
/*     */ {
/*  58 */   private static final Log logger = LogFactory.getLog(LocalVariableTableParameterNameDiscoverer.class);
/*     */ 
/*     */   
/*  61 */   private static final Map<Member, String[]> NO_DEBUG_INFO_MAP = (Map)Collections.emptyMap();
/*     */ 
/*     */   
/*  64 */   private final Map<Class<?>, Map<Member, String[]>> parameterNamesCache = new ConcurrentHashMap<>(32);
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String[] getParameterNames(Method method) {
/*  70 */     Method originalMethod = BridgeMethodResolver.findBridgedMethod(method);
/*  71 */     Class<?> declaringClass = originalMethod.getDeclaringClass();
/*  72 */     Map<Member, String[]> map = this.parameterNamesCache.get(declaringClass);
/*  73 */     if (map == null) {
/*  74 */       map = inspectClass(declaringClass);
/*  75 */       this.parameterNamesCache.put(declaringClass, map);
/*     */     } 
/*  77 */     if (map != NO_DEBUG_INFO_MAP) {
/*  78 */       return map.get(originalMethod);
/*     */     }
/*  80 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String[] getParameterNames(Constructor<?> ctor) {
/*  86 */     Class<?> declaringClass = ctor.getDeclaringClass();
/*  87 */     Map<Member, String[]> map = this.parameterNamesCache.get(declaringClass);
/*  88 */     if (map == null) {
/*  89 */       map = inspectClass(declaringClass);
/*  90 */       this.parameterNamesCache.put(declaringClass, map);
/*     */     } 
/*  92 */     if (map != NO_DEBUG_INFO_MAP) {
/*  93 */       return map.get(ctor);
/*     */     }
/*  95 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<Member, String[]> inspectClass(Class<?> clazz) {
/* 103 */     InputStream is = clazz.getResourceAsStream(ClassUtils.getClassFileName(clazz));
/* 104 */     if (is == null) {
/*     */ 
/*     */       
/* 107 */       if (logger.isDebugEnabled()) {
/* 108 */         logger.debug("Cannot find '.class' file for class [" + clazz + "] - unable to determine constructor/method parameter names");
/*     */       }
/*     */       
/* 111 */       return NO_DEBUG_INFO_MAP;
/*     */     } 
/*     */     try {
/* 114 */       ClassReader classReader = new ClassReader(is);
/* 115 */       Map<Member, String[]> map = (Map)new ConcurrentHashMap<>(32);
/* 116 */       classReader.accept(new ParameterNameDiscoveringVisitor(clazz, map), 0);
/* 117 */       return map;
/*     */     }
/* 119 */     catch (IOException ex) {
/* 120 */       if (logger.isDebugEnabled()) {
/* 121 */         logger.debug("Exception thrown while reading '.class' file for class [" + clazz + "] - unable to determine constructor/method parameter names", ex);
/*     */       
/*     */       }
/*     */     }
/* 125 */     catch (IllegalArgumentException ex) {
/* 126 */       if (logger.isDebugEnabled()) {
/* 127 */         logger.debug("ASM ClassReader failed to parse class file [" + clazz + "], probably due to a new Java class file version that isn't supported yet - unable to determine constructor/method parameter names", ex);
/*     */       }
/*     */     } finally {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/* 134 */         is.close();
/*     */       }
/* 136 */       catch (IOException iOException) {}
/*     */     } 
/*     */ 
/*     */     
/* 140 */     return NO_DEBUG_INFO_MAP;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ParameterNameDiscoveringVisitor
/*     */     extends ClassVisitor
/*     */   {
/*     */     private static final String STATIC_CLASS_INIT = "<clinit>";
/*     */ 
/*     */     
/*     */     private final Class<?> clazz;
/*     */     
/*     */     private final Map<Member, String[]> memberMap;
/*     */ 
/*     */     
/*     */     public ParameterNameDiscoveringVisitor(Class<?> clazz, Map<Member, String[]> memberMap) {
/* 157 */       super(458752);
/* 158 */       this.clazz = clazz;
/* 159 */       this.memberMap = memberMap;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
/* 166 */       if (!isSyntheticOrBridged(access) && !"<clinit>".equals(name)) {
/* 167 */         return new LocalVariableTableParameterNameDiscoverer.LocalVariableTableVisitor(this.clazz, this.memberMap, name, desc, isStatic(access));
/*     */       }
/* 169 */       return null;
/*     */     }
/*     */     
/*     */     private static boolean isSyntheticOrBridged(int access) {
/* 173 */       return ((access & 0x1000 | access & 0x40) > 0);
/*     */     }
/*     */     
/*     */     private static boolean isStatic(int access) {
/* 177 */       return ((access & 0x8) > 0);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class LocalVariableTableVisitor
/*     */     extends MethodVisitor
/*     */   {
/*     */     private static final String CONSTRUCTOR = "<init>";
/*     */ 
/*     */     
/*     */     private final Class<?> clazz;
/*     */     
/*     */     private final Map<Member, String[]> memberMap;
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private final Type[] args;
/*     */     
/*     */     private final String[] parameterNames;
/*     */     
/*     */     private final boolean isStatic;
/*     */     
/*     */     private boolean hasLvtInfo = false;
/*     */     
/*     */     private final int[] lvtSlotIndex;
/*     */ 
/*     */     
/*     */     public LocalVariableTableVisitor(Class<?> clazz, Map<Member, String[]> map, String name, String desc, boolean isStatic) {
/* 207 */       super(458752);
/* 208 */       this.clazz = clazz;
/* 209 */       this.memberMap = map;
/* 210 */       this.name = name;
/* 211 */       this.args = Type.getArgumentTypes(desc);
/* 212 */       this.parameterNames = new String[this.args.length];
/* 213 */       this.isStatic = isStatic;
/* 214 */       this.lvtSlotIndex = computeLvtSlotIndices(isStatic, this.args);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitLocalVariable(String name, String description, String signature, Label start, Label end, int index) {
/* 219 */       this.hasLvtInfo = true;
/* 220 */       for (int i = 0; i < this.lvtSlotIndex.length; i++) {
/* 221 */         if (this.lvtSlotIndex[i] == index) {
/* 222 */           this.parameterNames[i] = name;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitEnd() {
/* 229 */       if (this.hasLvtInfo || (this.isStatic && this.parameterNames.length == 0))
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 234 */         this.memberMap.put(resolveMember(), this.parameterNames);
/*     */       }
/*     */     }
/*     */     
/*     */     private Member resolveMember() {
/* 239 */       ClassLoader loader = this.clazz.getClassLoader();
/* 240 */       Class<?>[] argTypes = new Class[this.args.length];
/* 241 */       for (int i = 0; i < this.args.length; i++) {
/* 242 */         argTypes[i] = ClassUtils.resolveClassName(this.args[i].getClassName(), loader);
/*     */       }
/*     */       try {
/* 245 */         if ("<init>".equals(this.name)) {
/* 246 */           return this.clazz.getDeclaredConstructor(argTypes);
/*     */         }
/* 248 */         return this.clazz.getDeclaredMethod(this.name, argTypes);
/*     */       }
/* 250 */       catch (NoSuchMethodException ex) {
/* 251 */         throw new IllegalStateException("Method [" + this.name + "] was discovered in the .class file but cannot be resolved in the class object", ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private static int[] computeLvtSlotIndices(boolean isStatic, Type[] paramTypes) {
/* 257 */       int[] lvtIndex = new int[paramTypes.length];
/* 258 */       int nextIndex = isStatic ? 0 : 1;
/* 259 */       for (int i = 0; i < paramTypes.length; i++) {
/* 260 */         lvtIndex[i] = nextIndex;
/* 261 */         if (isWideType(paramTypes[i])) {
/* 262 */           nextIndex += 2;
/*     */         } else {
/*     */           
/* 265 */           nextIndex++;
/*     */         } 
/*     */       } 
/* 268 */       return lvtIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     private static boolean isWideType(Type aType) {
/* 273 */       return (aType == Type.LONG_TYPE || aType == Type.DOUBLE_TYPE);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/LocalVariableTableParameterNameDiscoverer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */