/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.function.Supplier;
/*     */ import kotlin.Metadata;
/*     */ import kotlin.PublishedApi;
/*     */ import kotlin.TypeCastException;
/*     */ import kotlin.Unit;
/*     */ import kotlin.collections.ArraysKt;
/*     */ import kotlin.jvm.functions.Function0;
/*     */ import kotlin.jvm.functions.Function1;
/*     */ import kotlin.jvm.internal.DefaultConstructorMarker;
/*     */ import kotlin.jvm.internal.FunctionReference;
/*     */ import kotlin.jvm.internal.Intrinsics;
/*     */ import kotlin.jvm.internal.Lambda;
/*     */ import kotlin.jvm.internal.Reflection;
/*     */ import kotlin.reflect.KDeclarationContainer;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionCustomizer;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
/*     */ import org.springframework.context.ApplicationContextInitializer;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
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
/*     */ @Metadata(mv = {1, 1, 11}, bv = {1, 0, 2}, k = 1, d1 = {"\000`\n\002\030\002\n\002\030\002\n\002\030\002\n\000\n\002\030\002\n\002\020\002\n\002\030\002\n\000\n\002\030\002\n\002\020\013\n\002\b\002\n\002\030\002\n\002\030\002\n\002\b\017\n\002\020\000\n\000\n\002\020\016\n\000\n\002\030\002\n\002\b\007\n\002\030\002\n\002\b\002\n\002\030\002\n\002\b\005\n\002\030\002\n\002\b\005\b\026\030\0002\b\022\004\022\0020\0020\001:\00256B4\022\027\020\003\032\023\022\004\022\0020\000\022\004\022\0020\0050\004¢\006\002\b\006\022\024\b\002\020\007\032\016\022\004\022\0020\b\022\004\022\0020\t0\004¢\006\002\020\nJ\001\020\033\032\0020\005\"\n\b\000\020\034\030\001*\0020\0352\n\b\002\020\036\032\004\030\0010\0372\n\b\002\020 \032\004\030\0010!2\n\b\002\020\"\032\004\030\0010\t2\n\b\002\020#\032\004\030\0010\t2\n\b\002\020$\032\004\030\0010\t2\n\b\002\020%\032\004\030\0010\0372\n\b\002\020&\032\004\030\0010\0372\n\b\002\020'\032\004\030\0010\0372\n\b\002\020(\032\004\030\0010)H\b¢\006\002\020*J\001\020\033\032\0020\005\"\n\b\000\020\034\030\001*\0020\0352\n\b\002\020\036\032\004\030\0010\0372\n\b\002\020 \032\004\030\0010!2\n\b\002\020\"\032\004\030\0010\t2\n\b\002\020#\032\004\030\0010\t2\n\b\002\020$\032\004\030\0010\t2\n\b\002\020%\032\004\030\0010\0372\n\b\002\020&\032\004\030\0010\0372\n\b\002\020'\032\004\030\0010\0372\n\b\002\020(\032\004\030\0010)2\016\b\004\020+\032\b\022\004\022\002H\0340,H\b¢\006\002\020-J8\020.\032\0020\0052\027\020\007\032\023\022\004\022\0020\b\022\004\022\0020\t0\004¢\006\002\b\0062\027\020\003\032\023\022\004\022\0020\000\022\004\022\0020\0050\004¢\006\002\b\006J\020\020/\032\0020\0052\006\020\022\032\0020\002H\026J'\0200\032\0020\0052\006\0200\032\0020\0372\027\020\003\032\023\022\004\022\0020\000\022\004\022\0020\0050\004¢\006\002\b\006J\033\0201\032\b\022\004\022\002H\03402\"\n\b\000\020\034\030\001*\0020\035H\bJ&\0203\032\002H\034\"\n\b\000\020\034\030\001*\0020\0352\n\b\002\020\036\032\004\030\0010\037H\b¢\006\002\0204R,\020\013\032\022\022\004\022\0020\0000\fj\b\022\004\022\0020\000`\r8\000X\004¢\006\016\n\000\022\004\b\016\020\017\032\004\b\020\020\021R\032\020\007\032\016\022\004\022\0020\b\022\004\022\0020\t0\004X\004¢\006\002\n\000R$\020\022\032\0020\0028\000@\000X.¢\006\024\n\000\022\004\b\023\020\017\032\004\b\024\020\025\"\004\b\026\020\027R\021\020\030\032\0020\b8F¢\006\006\032\004\b\031\020\032R\037\020\003\032\023\022\004\022\0020\000\022\004\022\0020\0050\004¢\006\002\b\006X\004¢\006\002\n\000¨\0067"}, d2 = {"Lorg/springframework/context/support/BeanDefinitionDsl;", "Lorg/springframework/context/ApplicationContextInitializer;", "Lorg/springframework/context/support/GenericApplicationContext;", "init", "Lkotlin/Function1;", "", "Lkotlin/ExtensionFunctionType;", "condition", "Lorg/springframework/core/env/ConfigurableEnvironment;", "", "(Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "children", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "children$annotations", "()V", "getChildren", "()Ljava/util/ArrayList;", "context", "context$annotations", "getContext", "()Lorg/springframework/context/support/GenericApplicationContext;", "setContext", "(Lorg/springframework/context/support/GenericApplicationContext;)V", "env", "getEnv", "()Lorg/springframework/core/env/ConfigurableEnvironment;", "bean", "T", "", "name", "", "scope", "Lorg/springframework/context/support/BeanDefinitionDsl$Scope;", "isLazyInit", "isPrimary", "isAutowireCandidate", "initMethodName", "destroyMethodName", "description", "role", "Lorg/springframework/context/support/BeanDefinitionDsl$Role;", "(Ljava/lang/String;Lorg/springframework/context/support/BeanDefinitionDsl$Scope;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/springframework/context/support/BeanDefinitionDsl$Role;)V", "function", "Lkotlin/Function0;", "(Ljava/lang/String;Lorg/springframework/context/support/BeanDefinitionDsl$Scope;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/springframework/context/support/BeanDefinitionDsl$Role;Lkotlin/jvm/functions/Function0;)V", "environment", "initialize", "profile", "provider", "Lorg/springframework/beans/factory/ObjectProvider;", "ref", "(Ljava/lang/String;)Ljava/lang/Object;", "Role", "Scope", "spring-context"})
/*     */ public class BeanDefinitionDsl
/*     */   implements ApplicationContextInitializer<GenericApplicationContext>
/*     */ {
/*     */   @NotNull
/*     */   private final ArrayList<BeanDefinitionDsl> children;
/*     */   @NotNull
/*     */   public GenericApplicationContext context;
/*     */   private final Function1<BeanDefinitionDsl, Unit> init;
/*     */   private final Function1<ConfigurableEnvironment, Boolean> condition;
/*     */   
/*     */   public BeanDefinitionDsl(@NotNull Function1<BeanDefinitionDsl, Unit> init, @NotNull Function1<ConfigurableEnvironment, Boolean> condition) {
/*  78 */     this.init = init; this.condition = condition;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  83 */     BeanDefinitionDsl beanDefinitionDsl = this; ArrayList<BeanDefinitionDsl> arrayList = new ArrayList();
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
/*     */   @Metadata(mv = {1, 1, 11}, bv = {1, 0, 2}, k = 3, d1 = {"\000\016\n\000\n\002\020\013\n\000\n\002\030\002\n\000\020\000\032\0020\0012\006\020\002\032\0020\003H\n¢\006\002\b\004"}, d2 = {"<anonymous>", "", "it", "Lorg/springframework/core/env/ConfigurableEnvironment;", "invoke"})
/*     */   static final class BeanDefinitionDsl$profile$beans$1
/*     */     extends Lambda
/*     */     implements Function1<ConfigurableEnvironment, Boolean>
/*     */   {
/*     */     public final boolean invoke(@NotNull ConfigurableEnvironment it) {
/* 264 */       Intrinsics.checkParameterIsNotNull(it, "it"); return ArraysKt.contains((Object[])it.getActiveProfiles(), this.$profile);
/*     */     }
/*     */     
/*     */     BeanDefinitionDsl$profile$beans$1(String param1String) {
/*     */       super(1);
/*     */     }
/*     */   }
/*     */   
/*     */   @Metadata(mv = {1, 1, 11}, bv = {1, 0, 2}, k = 3, d1 = {"\000\024\n\000\n\002\020\013\n\000\n\002\030\002\n\002\030\002\n\002\b\002\020\000\032\0020\0012\025\020\002\032\0210\003¢\006\f\b\004\022\b\b\005\022\004\b\b(\002¢\006\002\b\006"}, d2 = {"<anonymous>", "", "p1", "Lorg/springframework/core/env/ConfigurableEnvironment;", "Lkotlin/ParameterName;", "name", "invoke"})
/*     */   static final class BeanDefinitionDsl$environment$beans$1
/*     */     extends FunctionReference implements Function1<ConfigurableEnvironment, Boolean>
/*     */   {
/* 276 */     public final boolean invoke(@NotNull ConfigurableEnvironment p1) { Intrinsics.checkParameterIsNotNull(p1, "p1"); return ((Boolean)((Function1)this.receiver).invoke(p1)).booleanValue(); }
/*     */ 
/*     */     
/*     */     public final KDeclarationContainer getOwner() {
/*     */       return (KDeclarationContainer)Reflection.getOrCreateKotlinClass(Function1.class);
/*     */     }
/*     */     
/*     */     public final String getName() {
/*     */       return "invoke";
/*     */     }
/*     */     
/*     */     public final String getSignature() {
/*     */       return "invoke(Ljava/lang/Object;)Ljava/lang/Object;";
/*     */     }
/*     */     
/*     */     BeanDefinitionDsl$environment$beans$1(Function1 param1Function1) {
/*     */       super(1, param1Function1);
/*     */     }
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public final ArrayList<BeanDefinitionDsl> getChildren() {
/*     */     return this.children;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public final GenericApplicationContext getContext() {
/*     */     if (this.context == null)
/*     */       Intrinsics.throwUninitializedPropertyAccessException("context"); 
/*     */     return this.context;
/*     */   }
/*     */   
/*     */   public final void setContext(@NotNull GenericApplicationContext <set-?>) {
/*     */     Intrinsics.checkParameterIsNotNull(<set-?>, "<set-?>");
/*     */     this.context = <set-?>;
/*     */   }
/*     */   
/*     */   private final <T> ObjectProvider<T> provider() {
/*     */     BeanFactory $receiver$iv = (BeanFactory)getContext();
/* 315 */     Intrinsics.needClassReification(); Intrinsics.checkExpressionValueIsNotNull($receiver$iv.getBeanProvider(ResolvableType.forType((new BeanDefinitionDsl$provider$$inlined$getBeanProvider$1()).getType())), "getBeanProvider(Resolvab…Reference<T>() {}).type))"); return $receiver$iv.getBeanProvider(ResolvableType.forType((new BeanDefinitionDsl$provider$$inlined$getBeanProvider$1()).getType()));
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public final ConfigurableEnvironment getEnv() {
/*     */     if (this.context == null)
/*     */       Intrinsics.throwUninitializedPropertyAccessException("context"); 
/*     */     Intrinsics.checkExpressionValueIsNotNull(this.context.getEnvironment(), "context.environment");
/*     */     return this.context.getEnvironment();
/*     */   }
/*     */   
/*     */   @Metadata(mv = {1, 1, 11}, bv = {1, 0, 2}, k = 1, d1 = {"\000\f\n\002\030\002\n\002\020\020\n\002\b\004\b\001\030\0002\b\022\004\022\0020\0000\001B\007\b\002¢\006\002\020\002j\002\b\003j\002\b\004¨\006\005"}, d2 = {"Lorg/springframework/context/support/BeanDefinitionDsl$Scope;", "", "(Ljava/lang/String;I)V", "SINGLETON", "PROTOTYPE", "spring-context"})
/*     */   public enum Scope {
/*     */     SINGLETON, PROTOTYPE;
/*     */   }
/*     */   
/*     */   @Metadata(mv = {1, 1, 11}, bv = {1, 0, 2}, k = 1, d1 = {"\000\f\n\002\030\002\n\002\020\020\n\002\b\005\b\001\030\0002\b\022\004\022\0020\0000\001B\007\b\002¢\006\002\020\002j\002\b\003j\002\b\004j\002\b\005¨\006\006"}, d2 = {"Lorg/springframework/context/support/BeanDefinitionDsl$Role;", "", "(Ljava/lang/String;I)V", "APPLICATION", "SUPPORT", "INFRASTRUCTURE", "spring-context"})
/*     */   public enum Role {
/*     */     APPLICATION, SUPPORT, INFRASTRUCTURE;
/*     */   }
/*     */   
/*     */   private final <T> void bean(String name, Scope scope, Boolean isLazyInit, Boolean isPrimary, Boolean isAutowireCandidate, String initMethodName, String destroyMethodName, String description, Role role) {
/*     */     BeanDefinitionCustomizer customizer = new BeanDefinitionDsl$bean$customizer$1(scope, isLazyInit, isPrimary, isAutowireCandidate, initMethodName, destroyMethodName, description, role);
/*     */     if (name == null) {
/*     */       Intrinsics.reifiedOperationMarker(4, "T");
/*     */       Intrinsics.checkExpressionValueIsNotNull(BeanDefinitionReaderUtils.uniqueBeanName(Object.class.getName(), getContext()), "BeanDefinitionReaderUtil…class.java.name, context)");
/*     */     } 
/*     */     String beanName = BeanDefinitionReaderUtils.uniqueBeanName(Object.class.getName(), getContext());
/*     */     Intrinsics.reifiedOperationMarker(4, "T");
/*     */     getContext().registerBean(beanName, Object.class, new BeanDefinitionCustomizer[] { customizer });
/*     */   }
/*     */   
/*     */   @Metadata(mv = {1, 1, 11}, bv = {1, 0, 2}, k = 3, d1 = {"\000\024\n\000\n\002\020\002\n\000\n\002\020\000\n\000\n\002\030\002\n\000\020\000\032\0020\001\"\n\b\000\020\002\030\001*\0020\0032\006\020\004\032\0020\005H\n¢\006\002\b\006"}, d2 = {"<anonymous>", "", "T", "", "bd", "Lorg/springframework/beans/factory/config/BeanDefinition;", "customize"})
/*     */   public static final class BeanDefinitionDsl$bean$customizer$1 implements BeanDefinitionCustomizer {
/*     */     public final void customize(@NotNull BeanDefinition bd) {
/*     */       Intrinsics.checkParameterIsNotNull(bd, "bd");
/*     */       if (this.$scope != null) {
/*     */         BeanDefinitionDsl.Scope scope1 = this.$scope, it = scope1;
/*     */         String str1 = this.$scope.name();
/*     */         BeanDefinition beanDefinition = bd;
/*     */         if (str1 == null)
/*     */           throw new TypeCastException("null cannot be cast to non-null type java.lang.String"); 
/*     */         Intrinsics.checkExpressionValueIsNotNull(str1.toLowerCase(), "(this as java.lang.String).toLowerCase()");
/*     */         String str2 = str1.toLowerCase();
/*     */         beanDefinition.setScope(str2);
/*     */       } else {
/*     */       
/*     */       } 
/*     */       if (this.$isLazyInit != null) {
/*     */         Boolean bool = this.$isLazyInit;
/*     */         boolean it = bool.booleanValue();
/*     */         bd.setLazyInit(this.$isLazyInit.booleanValue());
/*     */       } else {
/*     */       
/*     */       } 
/*     */       if (this.$isPrimary != null) {
/*     */         Boolean bool = this.$isPrimary;
/*     */         boolean it = bool.booleanValue();
/*     */         bd.setPrimary(this.$isPrimary.booleanValue());
/*     */       } else {
/*     */       
/*     */       } 
/*     */       if (this.$isAutowireCandidate != null) {
/*     */         Boolean bool = this.$isAutowireCandidate;
/*     */         boolean it = bool.booleanValue();
/*     */         bd.setAutowireCandidate(this.$isAutowireCandidate.booleanValue());
/*     */       } else {
/*     */       
/*     */       } 
/*     */       if (this.$initMethodName != null) {
/*     */         String str1 = this.$initMethodName, it = str1;
/*     */         bd.setInitMethodName(this.$initMethodName);
/*     */       } else {
/*     */       
/*     */       } 
/*     */       if (this.$destroyMethodName != null) {
/*     */         String str1 = this.$destroyMethodName, it = str1;
/*     */         bd.setDestroyMethodName(this.$destroyMethodName);
/*     */       } else {
/*     */       
/*     */       } 
/*     */       if (this.$description != null) {
/*     */         String str1 = this.$description, it = str1;
/*     */         bd.setDescription(this.$description);
/*     */       } else {
/*     */       
/*     */       } 
/*     */       if (this.$role != null) {
/*     */         BeanDefinitionDsl.Role role1 = this.$role, it = role1;
/*     */         bd.setRole(this.$role.ordinal());
/*     */       } else {
/*     */       
/*     */       } 
/*     */     }
/*     */     
/*     */     public BeanDefinitionDsl$bean$customizer$1(BeanDefinitionDsl.Scope param1Scope, Boolean param1Boolean1, Boolean param1Boolean2, Boolean param1Boolean3, String param1String1, String param1String2, String param1String3, BeanDefinitionDsl.Role param1Role) {}
/*     */   }
/*     */   
/*     */   private final <T> void bean(String name, Scope scope, Boolean isLazyInit, Boolean isPrimary, Boolean isAutowireCandidate, String initMethodName, String destroyMethodName, String description, Role role, Function0 function) {
/*     */     BeanDefinitionCustomizer customizer = new BeanDefinitionDsl$bean$customizer$2(scope, isLazyInit, isPrimary, isAutowireCandidate, initMethodName, destroyMethodName, description, role);
/*     */     if (name == null) {
/*     */       Intrinsics.reifiedOperationMarker(4, "T");
/*     */       Intrinsics.checkExpressionValueIsNotNull(BeanDefinitionReaderUtils.uniqueBeanName(Object.class.getName(), getContext()), "BeanDefinitionReaderUtil…class.java.name, context)");
/*     */     } 
/*     */     String beanName = BeanDefinitionReaderUtils.uniqueBeanName(Object.class.getName(), getContext());
/*     */     Intrinsics.reifiedOperationMarker(4, "T");
/*     */     getContext().registerBean(beanName, Object.class, new BeanDefinitionDsl$bean$1(function), new BeanDefinitionCustomizer[] { customizer });
/*     */   }
/*     */   
/*     */   @Metadata(mv = {1, 1, 11}, bv = {1, 0, 2}, k = 3, d1 = {"\000\024\n\000\n\002\020\002\n\000\n\002\020\000\n\000\n\002\030\002\n\000\020\000\032\0020\001\"\n\b\000\020\002\030\001*\0020\0032\006\020\004\032\0020\005H\n¢\006\002\b\006"}, d2 = {"<anonymous>", "", "T", "", "bd", "Lorg/springframework/beans/factory/config/BeanDefinition;", "customize"})
/*     */   public static final class BeanDefinitionDsl$bean$customizer$2 implements BeanDefinitionCustomizer {
/*     */     public final void customize(@NotNull BeanDefinition bd) {
/*     */       Intrinsics.checkParameterIsNotNull(bd, "bd");
/*     */       if (this.$scope != null) {
/*     */         BeanDefinitionDsl.Scope scope1 = this.$scope, it = scope1;
/*     */         String str1 = this.$scope.name();
/*     */         BeanDefinition beanDefinition = bd;
/*     */         if (str1 == null)
/*     */           throw new TypeCastException("null cannot be cast to non-null type java.lang.String"); 
/*     */         Intrinsics.checkExpressionValueIsNotNull(str1.toLowerCase(), "(this as java.lang.String).toLowerCase()");
/*     */         String str2 = str1.toLowerCase();
/*     */         beanDefinition.setScope(str2);
/*     */       } else {
/*     */       
/*     */       } 
/*     */       if (this.$isLazyInit != null) {
/*     */         Boolean bool = this.$isLazyInit;
/*     */         boolean it = bool.booleanValue();
/*     */         bd.setLazyInit(this.$isLazyInit.booleanValue());
/*     */       } else {
/*     */       
/*     */       } 
/*     */       if (this.$isPrimary != null) {
/*     */         Boolean bool = this.$isPrimary;
/*     */         boolean it = bool.booleanValue();
/*     */         bd.setPrimary(this.$isPrimary.booleanValue());
/*     */       } else {
/*     */       
/*     */       } 
/*     */       if (this.$isAutowireCandidate != null) {
/*     */         Boolean bool = this.$isAutowireCandidate;
/*     */         boolean it = bool.booleanValue();
/*     */         bd.setAutowireCandidate(this.$isAutowireCandidate.booleanValue());
/*     */       } else {
/*     */       
/*     */       } 
/*     */       if (this.$initMethodName != null) {
/*     */         String str1 = this.$initMethodName, it = str1;
/*     */         bd.setInitMethodName(this.$initMethodName);
/*     */       } else {
/*     */       
/*     */       } 
/*     */       if (this.$destroyMethodName != null) {
/*     */         String str1 = this.$destroyMethodName, it = str1;
/*     */         bd.setDestroyMethodName(this.$destroyMethodName);
/*     */       } else {
/*     */       
/*     */       } 
/*     */       if (this.$description != null) {
/*     */         String str1 = this.$description, it = str1;
/*     */         bd.setDescription(this.$description);
/*     */       } else {
/*     */       
/*     */       } 
/*     */       if (this.$role != null) {
/*     */         BeanDefinitionDsl.Role role1 = this.$role, it = role1;
/*     */         bd.setRole(this.$role.ordinal());
/*     */       } else {
/*     */       
/*     */       } 
/*     */     }
/*     */     
/*     */     public BeanDefinitionDsl$bean$customizer$2(BeanDefinitionDsl.Scope param1Scope, Boolean param1Boolean1, Boolean param1Boolean2, Boolean param1Boolean3, String param1String1, String param1String2, String param1String3, BeanDefinitionDsl.Role param1Role) {}
/*     */   }
/*     */   
/*     */   @Metadata(mv = {1, 1, 11}, bv = {1, 0, 2}, k = 3, d1 = {"\000\f\n\002\b\002\n\002\020\000\n\002\b\002\020\000\032\002H\001\"\n\b\000\020\001\030\001*\0020\002H\n¢\006\004\b\003\020\004"}, d2 = {"<anonymous>", "T", "", "get", "()Ljava/lang/Object;"})
/*     */   public static final class BeanDefinitionDsl$bean$1<T> implements Supplier<T> {
/*     */     @NotNull
/*     */     public final T get() {
/*     */       return (T)this.$function.invoke();
/*     */     }
/*     */     
/*     */     public BeanDefinitionDsl$bean$1(Function0 param1Function0) {}
/*     */   }
/*     */   
/*     */   private final <T> T ref(String name) {
/*     */     String str = name;
/*     */     Intrinsics.reifiedOperationMarker(4, "T");
/*     */     Intrinsics.checkExpressionValueIsNotNull(getContext().getBean(Object.class), "context.getBean(T::class.java)");
/*     */     Intrinsics.reifiedOperationMarker(4, "T");
/*     */     Intrinsics.checkExpressionValueIsNotNull(getContext().getBean(name, Object.class), "context.getBean(name, T::class.java)");
/*     */     return (str == null) ? (T)getContext().getBean(Object.class) : (T)getContext().getBean(name, Object.class);
/*     */   }
/*     */   
/*     */   public final void profile(@NotNull String profile, @NotNull Function1<? super BeanDefinitionDsl, Unit> init) {
/*     */     Intrinsics.checkParameterIsNotNull(profile, "profile");
/*     */     Intrinsics.checkParameterIsNotNull(init, "init");
/*     */     BeanDefinitionDsl beans = new BeanDefinitionDsl(init, new BeanDefinitionDsl$profile$beans$1(profile));
/*     */     this.children.add(beans);
/*     */   }
/*     */   
/*     */   public final void environment(@NotNull Function1 condition, @NotNull Function1<? super BeanDefinitionDsl, Unit> init) {
/*     */     Intrinsics.checkParameterIsNotNull(condition, "condition");
/*     */     Intrinsics.checkParameterIsNotNull(init, "init");
/*     */     BeanDefinitionDsl beans = new BeanDefinitionDsl(init, new BeanDefinitionDsl$environment$beans$1(condition));
/*     */     this.children.add(beans);
/*     */   }
/*     */   
/*     */   public void initialize(@NotNull GenericApplicationContext context) {
/*     */     Intrinsics.checkParameterIsNotNull(context, "context");
/*     */     this.context = context;
/*     */     this.init.invoke(this);
/*     */     for (BeanDefinitionDsl child : this.children) {
/*     */       Intrinsics.checkExpressionValueIsNotNull(context.getEnvironment(), "context.environment");
/*     */       if (((Boolean)child.condition.invoke(context.getEnvironment())).booleanValue())
/*     */         child.initialize(context); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/support/BeanDefinitionDsl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */