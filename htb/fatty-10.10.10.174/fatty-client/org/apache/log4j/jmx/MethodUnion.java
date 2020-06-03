/*    */ package org.apache.log4j.jmx;
/*    */ 
/*    */ import java.lang.reflect.Method;
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
/*    */ class MethodUnion
/*    */ {
/*    */   Method readMethod;
/*    */   Method writeMethod;
/*    */   
/*    */   MethodUnion(Method readMethod, Method writeMethod) {
/* 28 */     this.readMethod = readMethod;
/* 29 */     this.writeMethod = writeMethod;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/apache/log4j/jmx/MethodUnion.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */