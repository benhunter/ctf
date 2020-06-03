/*    */ package org.springframework.expression.spel.ast;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.expression.PropertyAccessor;
/*    */ import org.springframework.lang.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AstUtils
/*    */ {
/*    */   public static List<PropertyAccessor> getPropertyAccessorsToTry(@Nullable Class<?> targetType, List<PropertyAccessor> propertyAccessors) {
/* 48 */     List<PropertyAccessor> specificAccessors = new ArrayList<>();
/* 49 */     List<PropertyAccessor> generalAccessors = new ArrayList<>();
/* 50 */     for (PropertyAccessor resolver : propertyAccessors) {
/* 51 */       Class<?>[] targets = resolver.getSpecificTargetClasses();
/* 52 */       if (targets == null) {
/* 53 */         generalAccessors.add(resolver);
/*    */         continue;
/*    */       } 
/* 56 */       if (targetType != null) {
/* 57 */         int pos = 0;
/* 58 */         for (Class<?> clazz : targets) {
/* 59 */           if (clazz == targetType) {
/* 60 */             specificAccessors.add(pos++, resolver);
/*    */           }
/* 62 */           else if (clazz.isAssignableFrom(targetType)) {
/*    */             
/* 64 */             generalAccessors.add(resolver);
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 70 */     List<PropertyAccessor> resolvers = new ArrayList<>(specificAccessors.size() + generalAccessors.size());
/* 71 */     resolvers.addAll(specificAccessors);
/* 72 */     resolvers.addAll(generalAccessors);
/* 73 */     return resolvers;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/spel/ast/AstUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */