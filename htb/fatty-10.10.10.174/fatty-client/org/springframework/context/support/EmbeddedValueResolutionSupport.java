/*    */ package org.springframework.context.support;
/*    */ 
/*    */ import org.springframework.context.EmbeddedValueResolverAware;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.StringValueResolver;
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
/*    */ public class EmbeddedValueResolutionSupport
/*    */   implements EmbeddedValueResolverAware
/*    */ {
/*    */   @Nullable
/*    */   private StringValueResolver embeddedValueResolver;
/*    */   
/*    */   public void setEmbeddedValueResolver(StringValueResolver resolver) {
/* 38 */     this.embeddedValueResolver = resolver;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected String resolveEmbeddedValue(String value) {
/* 49 */     return (this.embeddedValueResolver != null) ? this.embeddedValueResolver.resolveStringValue(value) : value;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/support/EmbeddedValueResolutionSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */