/*    */ package org.springframework.context.annotation;
/*    */ 
/*    */ import kotlin.Metadata;
/*    */ import kotlin.jvm.functions.Function1;
/*    */ import kotlin.jvm.internal.Intrinsics;
/*    */ import org.jetbrains.annotations.NotNull;
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
/*    */ @Metadata(mv = {1, 1, 11}, bv = {1, 0, 2}, k = 2, d1 = {"\000\026\n\000\n\002\030\002\n\000\n\002\030\002\n\002\020\002\n\002\030\002\n\000\032\037\020\000\032\0020\0012\027\020\002\032\023\022\004\022\0020\001\022\004\022\0020\0040\003¢\006\002\b\005¨\006\006"}, d2 = {"AnnotationConfigApplicationContext", "Lorg/springframework/context/annotation/AnnotationConfigApplicationContext;", "configure", "Lkotlin/Function1;", "", "Lkotlin/ExtensionFunctionType;", "spring-context"})
/*    */ public final class AnnotationConfigApplicationContextExtensionsKt
/*    */ {
/*    */   @NotNull
/*    */   public static final AnnotationConfigApplicationContext AnnotationConfigApplicationContext(@NotNull Function1 configure) {
/* 27 */     Intrinsics.checkParameterIsNotNull(configure, "configure"); AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(); configure.invoke(annotationConfigApplicationContext); return annotationConfigApplicationContext;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/AnnotationConfigApplicationContextExtensionsKt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */