/*    */ package org.springframework.ui;
/*    */ 
/*    */ import kotlin.Metadata;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Metadata(mv = {1, 1, 11}, bv = {1, 0, 2}, k = 2, d1 = {"\000\030\n\000\n\002\020\002\n\002\030\002\n\000\n\002\020\016\n\000\n\002\020\000\n\000\032\035\020\000\032\0020\001*\0020\0022\006\020\003\032\0020\0042\006\020\005\032\0020\006H\002¨\006\007"}, d2 = {"set", "", "Lorg/springframework/ui/ModelMap;", "attributeName", "", "attributeValue", "", "spring-context"})
/*    */ public final class ModelMapExtensionsKt
/*    */ {
/*    */   public static final void set(@NotNull ModelMap $receiver, @NotNull String attributeName, @NotNull Object attributeValue) {
/* 31 */     Intrinsics.checkParameterIsNotNull($receiver, "$receiver"); Intrinsics.checkParameterIsNotNull(attributeName, "attributeName"); Intrinsics.checkParameterIsNotNull(attributeValue, "attributeValue"); $receiver.addAttribute(attributeName, attributeValue);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/ui/ModelMapExtensionsKt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */