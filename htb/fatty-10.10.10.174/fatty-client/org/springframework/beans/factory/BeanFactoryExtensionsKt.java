/*    */ package org.springframework.beans.factory;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import kotlin.Metadata;
/*    */ import kotlin.jvm.internal.Intrinsics;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ import org.springframework.core.ParameterizedTypeReference;
/*    */ import org.springframework.core.ResolvableType;
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
/*    */ @Metadata(mv = {1, 1, 11}, bv = {1, 0, 2}, k = 2, d1 = {"\000&\n\002\b\002\n\002\020\000\n\002\030\002\n\002\b\002\n\002\020\021\n\002\b\002\n\002\020\016\n\002\b\002\n\002\030\002\n\000\032\036\020\000\032\002H\001\"\n\b\000\020\001\030\001*\0020\002*\0020\003H\b¢\006\002\020\004\0322\020\000\032\002H\001\"\n\b\000\020\001\030\001*\0020\002*\0020\0032\022\020\005\032\n\022\006\b\001\022\0020\0020\006\"\0020\002H\b¢\006\002\020\007\032&\020\000\032\002H\001\"\n\b\000\020\001\030\001*\0020\002*\0020\0032\006\020\b\032\0020\tH\b¢\006\002\020\n\032\037\020\013\032\b\022\004\022\002H\0010\f\"\n\b\000\020\001\030\001*\0020\002*\0020\003H\b¨\006\r"}, d2 = {"getBean", "T", "", "Lorg/springframework/beans/factory/BeanFactory;", "(Lorg/springframework/beans/factory/BeanFactory;)Ljava/lang/Object;", "args", "", "(Lorg/springframework/beans/factory/BeanFactory;[Ljava/lang/Object;)Ljava/lang/Object;", "name", "", "(Lorg/springframework/beans/factory/BeanFactory;Ljava/lang/String;)Ljava/lang/Object;", "getBeanProvider", "Lorg/springframework/beans/factory/ObjectProvider;", "spring-beans"})
/*    */ public final class BeanFactoryExtensionsKt
/*    */ {
/*    */   private static final <T> T getBean(@NotNull BeanFactory $receiver) {
/* 28 */     Intrinsics.reifiedOperationMarker(4, "T"); Intrinsics.checkExpressionValueIsNotNull($receiver.getBean(Object.class), "getBean(T::class.java)"); return (T)$receiver.getBean(Object.class);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static final <T> T getBean(@NotNull BeanFactory $receiver, String name) {
/* 39 */     Intrinsics.reifiedOperationMarker(4, "T"); Intrinsics.checkExpressionValueIsNotNull($receiver.getBean(name, Object.class), "getBean(name, T::class.java)"); return (T)$receiver.getBean(name, Object.class);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static final <T> T getBean(@NotNull BeanFactory $receiver, Object... args) {
/* 49 */     Intrinsics.reifiedOperationMarker(4, "T"); Intrinsics.checkExpressionValueIsNotNull($receiver.getBean(Object.class, Arrays.copyOf(args, args.length)), "getBean(T::class.java, *args)"); return (T)$receiver.getBean(Object.class, Arrays.copyOf(args, args.length));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static final <T> ObjectProvider<T> getBeanProvider(@NotNull BeanFactory $receiver) {
/* 60 */     Intrinsics.needClassReification(); Intrinsics.checkExpressionValueIsNotNull($receiver.getBeanProvider(ResolvableType.forType((new BeanFactoryExtensionsKt$getBeanProvider$1()).getType())), "getBeanProvider(Resolvab…Reference<T>() {}).type))"); return (ObjectProvider)$receiver.getBeanProvider(ResolvableType.forType((new BeanFactoryExtensionsKt$getBeanProvider$1()).getType()));
/*    */   }
/*    */   
/*    */   @Metadata(mv = {1, 1, 11}, bv = {1, 0, 2}, k = 1, d1 = {"\000\013\n\000\n\002\030\002\n\000*\001\000\b\n\030\0002\b\022\004\022\0028\0000\001¨\006\002"}, d2 = {"org/springframework/beans/factory/BeanFactoryExtensionsKt$getBeanProvider$1", "Lorg/springframework/core/ParameterizedTypeReference;", "spring-beans"})
/*    */   public static final class BeanFactoryExtensionsKt$getBeanProvider$1 extends ParameterizedTypeReference<T> {}
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/BeanFactoryExtensionsKt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */