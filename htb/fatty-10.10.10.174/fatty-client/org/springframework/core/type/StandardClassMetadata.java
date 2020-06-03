/*     */ package org.springframework.core.type;
/*     */ 
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.LinkedHashSet;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class StandardClassMetadata
/*     */   implements ClassMetadata
/*     */ {
/*     */   private final Class<?> introspectedClass;
/*     */   
/*     */   public StandardClassMetadata(Class<?> introspectedClass) {
/*  43 */     Assert.notNull(introspectedClass, "Class must not be null");
/*  44 */     this.introspectedClass = introspectedClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Class<?> getIntrospectedClass() {
/*  51 */     return this.introspectedClass;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  57 */     return this.introspectedClass.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInterface() {
/*  62 */     return this.introspectedClass.isInterface();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAnnotation() {
/*  67 */     return this.introspectedClass.isAnnotation();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAbstract() {
/*  72 */     return Modifier.isAbstract(this.introspectedClass.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isConcrete() {
/*  77 */     return (!isInterface() && !isAbstract());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFinal() {
/*  82 */     return Modifier.isFinal(this.introspectedClass.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIndependent() {
/*  87 */     return (!hasEnclosingClass() || (this.introspectedClass
/*  88 */       .getDeclaringClass() != null && 
/*  89 */       Modifier.isStatic(this.introspectedClass.getModifiers())));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEnclosingClass() {
/*  94 */     return (this.introspectedClass.getEnclosingClass() != null);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getEnclosingClassName() {
/* 100 */     Class<?> enclosingClass = this.introspectedClass.getEnclosingClass();
/* 101 */     return (enclosingClass != null) ? enclosingClass.getName() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasSuperClass() {
/* 106 */     return (this.introspectedClass.getSuperclass() != null);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getSuperClassName() {
/* 112 */     Class<?> superClass = this.introspectedClass.getSuperclass();
/* 113 */     return (superClass != null) ? superClass.getName() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getInterfaceNames() {
/* 118 */     Class<?>[] ifcs = this.introspectedClass.getInterfaces();
/* 119 */     String[] ifcNames = new String[ifcs.length];
/* 120 */     for (int i = 0; i < ifcs.length; i++) {
/* 121 */       ifcNames[i] = ifcs[i].getName();
/*     */     }
/* 123 */     return ifcNames;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getMemberClassNames() {
/* 128 */     LinkedHashSet<String> memberClassNames = new LinkedHashSet<>(4);
/* 129 */     for (Class<?> nestedClass : this.introspectedClass.getDeclaredClasses()) {
/* 130 */       memberClassNames.add(nestedClass.getName());
/*     */     }
/* 132 */     return StringUtils.toStringArray(memberClassNames);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/type/StandardClassMetadata.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */