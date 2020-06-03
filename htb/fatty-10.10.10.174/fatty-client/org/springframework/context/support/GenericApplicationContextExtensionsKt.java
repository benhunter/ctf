/*    */ package org.springframework.context.support;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.function.Supplier;
/*    */ import kotlin.Metadata;
/*    */ import kotlin.jvm.functions.Function1;
/*    */ import kotlin.jvm.internal.Intrinsics;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ import org.springframework.beans.factory.config.BeanDefinitionCustomizer;
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
/*    */ @Metadata(mv = {1, 1, 11}, bv = {1, 0, 2}, k = 2, d1 = {"\000:\n\000\n\002\030\002\n\000\n\002\030\002\n\002\020\002\n\002\030\002\n\002\b\002\n\002\020\000\n\000\n\002\020\021\n\002\030\002\n\002\b\002\n\002\030\002\n\002\b\002\n\002\020\016\n\002\b\004\032\037\020\000\032\0020\0012\027\020\002\032\023\022\004\022\0020\001\022\004\022\0020\0040\003¢\006\002\b\005\0322\020\006\032\0020\004\"\n\b\000\020\007\030\001*\0020\b*\0020\0012\022\020\t\032\n\022\006\b\001\022\0020\0130\n\"\0020\013H\b¢\006\002\020\f\032H\020\006\032\0020\004\"\n\b\000\020\007\030\001*\0020\b*\0020\0012\022\020\t\032\n\022\006\b\001\022\0020\0130\n\"\0020\0132\024\b\004\020\r\032\016\022\004\022\0020\016\022\004\022\002H\0070\003H\b¢\006\002\020\017\032:\020\006\032\0020\004\"\n\b\000\020\007\030\001*\0020\b*\0020\0012\006\020\020\032\0020\0212\022\020\t\032\n\022\006\b\001\022\0020\0130\n\"\0020\013H\b¢\006\002\020\022\032P\020\006\032\0020\004\"\n\b\000\020\007\030\001*\0020\b*\0020\0012\006\020\023\032\0020\0212\022\020\t\032\n\022\006\b\001\022\0020\0130\n\"\0020\0132\024\b\004\020\r\032\016\022\004\022\0020\016\022\004\022\002H\0070\003H\b¢\006\002\020\024¨\006\025"}, d2 = {"GenericApplicationContext", "Lorg/springframework/context/support/GenericApplicationContext;", "configure", "Lkotlin/Function1;", "", "Lkotlin/ExtensionFunctionType;", "registerBean", "T", "", "customizers", "", "Lorg/springframework/beans/factory/config/BeanDefinitionCustomizer;", "(Lorg/springframework/context/support/GenericApplicationContext;[Lorg/springframework/beans/factory/config/BeanDefinitionCustomizer;)V", "function", "Lorg/springframework/context/ApplicationContext;", "(Lorg/springframework/context/support/GenericApplicationContext;[Lorg/springframework/beans/factory/config/BeanDefinitionCustomizer;Lkotlin/jvm/functions/Function1;)V", "beanName", "", "(Lorg/springframework/context/support/GenericApplicationContext;Ljava/lang/String;[Lorg/springframework/beans/factory/config/BeanDefinitionCustomizer;)V", "name", "(Lorg/springframework/context/support/GenericApplicationContext;Ljava/lang/String;[Lorg/springframework/beans/factory/config/BeanDefinitionCustomizer;Lkotlin/jvm/functions/Function1;)V", "spring-context"})
/*    */ public final class GenericApplicationContextExtensionsKt
/*    */ {
/*    */   private static final <T> void registerBean(@NotNull GenericApplicationContext $receiver, BeanDefinitionCustomizer... customizers) {
/* 31 */     Intrinsics.reifiedOperationMarker(4, "T"); $receiver.registerBean(Object.class, Arrays.<BeanDefinitionCustomizer>copyOf(customizers, customizers.length));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static final <T> void registerBean(@NotNull GenericApplicationContext $receiver, String beanName, BeanDefinitionCustomizer... customizers) {
/* 43 */     Intrinsics.reifiedOperationMarker(4, "T"); $receiver.registerBean(beanName, Object.class, Arrays.<BeanDefinitionCustomizer>copyOf(customizers, customizers.length));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static final <T> void registerBean(@NotNull GenericApplicationContext $receiver, BeanDefinitionCustomizer[] customizers, Function1 function) {
/* 54 */     Intrinsics.reifiedOperationMarker(4, "T"); $receiver.registerBean(Object.class, new GenericApplicationContextExtensionsKt$registerBean$1($receiver, function), Arrays.<BeanDefinitionCustomizer>copyOf(customizers, customizers.length)); } @Metadata(mv = {1, 1, 11}, bv = {1, 0, 2}, k = 3, d1 = {"\000\f\n\002\b\002\n\002\020\000\n\002\b\002\020\000\032\002H\001\"\n\b\000\020\001\030\001*\0020\002H\n¢\006\004\b\003\020\004"}, d2 = {"<anonymous>", "T", "", "get", "()Ljava/lang/Object;"}) public static final class GenericApplicationContextExtensionsKt$registerBean$1<T> implements Supplier<T> { @NotNull public final T get() { return (T)this.$function.invoke(this.receiver$0); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public GenericApplicationContextExtensionsKt$registerBean$1(GenericApplicationContext param1GenericApplicationContext, Function1 param1Function1) {} }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static final <T> void registerBean(@NotNull GenericApplicationContext $receiver, String name, BeanDefinitionCustomizer[] customizers, Function1 function) {
/* 66 */     Intrinsics.reifiedOperationMarker(4, "T"); $receiver.registerBean(name, Object.class, new GenericApplicationContextExtensionsKt$registerBean$2($receiver, function), Arrays.<BeanDefinitionCustomizer>copyOf(customizers, customizers.length)); } @Metadata(mv = {1, 1, 11}, bv = {1, 0, 2}, k = 3, d1 = {"\000\f\n\002\b\002\n\002\020\000\n\002\b\002\020\000\032\002H\001\"\n\b\000\020\001\030\001*\0020\002H\n¢\006\004\b\003\020\004"}, d2 = {"<anonymous>", "T", "", "get", "()Ljava/lang/Object;"}) public static final class GenericApplicationContextExtensionsKt$registerBean$2<T> implements Supplier<T> { @NotNull public final T get() { return (T)this.$function.invoke(this.receiver$0); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public GenericApplicationContextExtensionsKt$registerBean$2(GenericApplicationContext param1GenericApplicationContext, Function1 param1Function1) {} }
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public static final GenericApplicationContext GenericApplicationContext(@NotNull Function1 configure) {
/* 77 */     Intrinsics.checkParameterIsNotNull(configure, "configure"); GenericApplicationContext genericApplicationContext = new GenericApplicationContext(); configure.invoke(genericApplicationContext); return genericApplicationContext;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/support/GenericApplicationContextExtensionsKt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */