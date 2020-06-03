/*    */ package org.springframework.jndi;
/*    */ 
/*    */ import javax.naming.NamingException;
/*    */ import org.springframework.core.NestedRuntimeException;
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
/*    */ public class JndiLookupFailureException
/*    */   extends NestedRuntimeException
/*    */ {
/*    */   public JndiLookupFailureException(String msg, NamingException cause) {
/* 42 */     super(msg, cause);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jndi/JndiLookupFailureException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */