/*     */ package org.springframework.aop.aspectj.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import org.aspectj.lang.annotation.Aspect;
/*     */ import org.aspectj.lang.reflect.AjType;
/*     */ import org.aspectj.lang.reflect.AjTypeSystem;
/*     */ import org.aspectj.lang.reflect.PerClauseKind;
/*     */ import org.springframework.aop.ClassFilter;
/*     */ import org.springframework.aop.Pointcut;
/*     */ import org.springframework.aop.aspectj.AspectJExpressionPointcut;
/*     */ import org.springframework.aop.aspectj.TypePatternClassFilter;
/*     */ import org.springframework.aop.framework.AopConfigException;
/*     */ import org.springframework.aop.support.ComposablePointcut;
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
/*     */ public class AspectMetadata
/*     */   implements Serializable
/*     */ {
/*     */   private final String aspectName;
/*     */   private final Class<?> aspectClass;
/*     */   private transient AjType<?> ajType;
/*     */   private final Pointcut perClausePointcut;
/*     */   
/*     */   public AspectMetadata(Class<?> aspectClass, String aspectName) {
/*     */     AspectJExpressionPointcut ajexp;
/*  82 */     this.aspectName = aspectName;
/*     */     
/*  84 */     Class<?> currClass = aspectClass;
/*  85 */     AjType<?> ajType = null;
/*  86 */     while (currClass != Object.class) {
/*  87 */       AjType<?> ajTypeToCheck = AjTypeSystem.getAjType(currClass);
/*  88 */       if (ajTypeToCheck.isAspect()) {
/*  89 */         ajType = ajTypeToCheck;
/*     */         break;
/*     */       } 
/*  92 */       currClass = currClass.getSuperclass();
/*     */     } 
/*  94 */     if (ajType == null) {
/*  95 */       throw new IllegalArgumentException("Class '" + aspectClass.getName() + "' is not an @AspectJ aspect");
/*     */     }
/*  97 */     if ((ajType.getDeclarePrecedence()).length > 0) {
/*  98 */       throw new IllegalArgumentException("DeclarePrecendence not presently supported in Spring AOP");
/*     */     }
/* 100 */     this.aspectClass = ajType.getJavaClass();
/* 101 */     this.ajType = ajType;
/*     */     
/* 103 */     switch (this.ajType.getPerClause().getKind()) {
/*     */       case SINGLETON:
/* 105 */         this.perClausePointcut = Pointcut.TRUE;
/*     */         return;
/*     */       case PERTARGET:
/*     */       case PERTHIS:
/* 109 */         ajexp = new AspectJExpressionPointcut();
/* 110 */         ajexp.setLocation(aspectClass.getName());
/* 111 */         ajexp.setExpression(findPerClause(aspectClass));
/* 112 */         ajexp.setPointcutDeclarationScope(aspectClass);
/* 113 */         this.perClausePointcut = (Pointcut)ajexp;
/*     */         return;
/*     */       
/*     */       case PERTYPEWITHIN:
/* 117 */         this.perClausePointcut = (Pointcut)new ComposablePointcut((ClassFilter)new TypePatternClassFilter(findPerClause(aspectClass)));
/*     */         return;
/*     */     } 
/* 120 */     throw new AopConfigException("PerClause " + ajType
/* 121 */         .getPerClause().getKind() + " not supported by Spring AOP for " + aspectClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String findPerClause(Class<?> aspectClass) {
/* 129 */     String str = ((Aspect)aspectClass.<Aspect>getAnnotation(Aspect.class)).value();
/* 130 */     str = str.substring(str.indexOf('(') + 1);
/* 131 */     str = str.substring(0, str.length() - 1);
/* 132 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AjType<?> getAjType() {
/* 140 */     return this.ajType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getAspectClass() {
/* 147 */     return this.aspectClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAspectName() {
/* 154 */     return this.aspectName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Pointcut getPerClausePointcut() {
/* 162 */     return this.perClausePointcut;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPerThisOrPerTarget() {
/* 169 */     PerClauseKind kind = getAjType().getPerClause().getKind();
/* 170 */     return (kind == PerClauseKind.PERTARGET || kind == PerClauseKind.PERTHIS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPerTypeWithin() {
/* 177 */     PerClauseKind kind = getAjType().getPerClause().getKind();
/* 178 */     return (kind == PerClauseKind.PERTYPEWITHIN);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLazilyInstantiated() {
/* 185 */     return (isPerThisOrPerTarget() || isPerTypeWithin());
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
/* 190 */     inputStream.defaultReadObject();
/* 191 */     this.ajType = AjTypeSystem.getAjType(this.aspectClass);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/annotation/AspectMetadata.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */