/*     */ package org.springframework.context.expression;
/*     */ 
/*     */ import java.util.Map;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.CompilablePropertyAccessor;
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
/*     */ public class MapAccessor
/*     */   implements CompilablePropertyAccessor
/*     */ {
/*     */   public Class<?>[] getSpecificTargetClasses() {
/*  42 */     return new Class[] { Map.class };
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canRead(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
/*  47 */     return (target instanceof Map && ((Map)target).containsKey(name));
/*     */   }
/*     */ 
/*     */   
/*     */   public TypedValue read(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
/*  52 */     Assert.state(target instanceof Map, "Target must be of type Map");
/*  53 */     Map<?, ?> map = (Map<?, ?>)target;
/*  54 */     Object value = map.get(name);
/*  55 */     if (value == null && !map.containsKey(name)) {
/*  56 */       throw new MapAccessException(name);
/*     */     }
/*  58 */     return new TypedValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canWrite(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
/*  63 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(EvaluationContext context, @Nullable Object target, String name, @Nullable Object newValue) throws AccessException {
/*  71 */     Assert.state(target instanceof Map, "Target must be a Map");
/*  72 */     Map<Object, Object> map = (Map<Object, Object>)target;
/*  73 */     map.put(name, newValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/*  78 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getPropertyType() {
/*  83 */     return Object.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(String propertyName, MethodVisitor mv, CodeFlow cf) {
/*  88 */     String descriptor = cf.lastDescriptor();
/*  89 */     if (descriptor == null || !descriptor.equals("Ljava/util/Map")) {
/*  90 */       if (descriptor == null) {
/*  91 */         cf.loadTarget(mv);
/*     */       }
/*  93 */       CodeFlow.insertCheckCast(mv, "Ljava/util/Map");
/*     */     } 
/*  95 */     mv.visitLdcInsn(propertyName);
/*  96 */     mv.visitMethodInsn(185, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class MapAccessException
/*     */     extends AccessException
/*     */   {
/*     */     private final String key;
/*     */ 
/*     */ 
/*     */     
/*     */     public MapAccessException(String key) {
/* 110 */       super("");
/* 111 */       this.key = key;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getMessage() {
/* 116 */       return "Map does not contain a value for key '" + this.key + "'";
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/expression/MapAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */