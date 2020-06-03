/*    */ package org.apache.log4j;
/*    */ 
/*    */ import java.util.Vector;
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
/*    */ class ProvisionNode
/*    */   extends Vector
/*    */ {
/*    */   private static final long serialVersionUID = -4479121426311014469L;
/*    */   
/*    */   ProvisionNode(Logger logger) {
/* 27 */     addElement((E)logger);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/apache/log4j/ProvisionNode.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */