/*    */ package org.springframework.core;
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
/*    */ public class DefaultParameterNameDiscoverer
/*    */   extends PrioritizedParameterNameDiscoverer
/*    */ {
/*    */   public DefaultParameterNameDiscoverer() {
/* 42 */     if (!GraalDetector.inImageCode()) {
/* 43 */       if (KotlinDetector.isKotlinReflectPresent()) {
/* 44 */         addDiscoverer(new KotlinReflectionParameterNameDiscoverer());
/*    */       }
/* 46 */       addDiscoverer(new StandardReflectionParameterNameDiscoverer());
/* 47 */       addDiscoverer(new LocalVariableTableParameterNameDiscoverer());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/DefaultParameterNameDiscoverer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */