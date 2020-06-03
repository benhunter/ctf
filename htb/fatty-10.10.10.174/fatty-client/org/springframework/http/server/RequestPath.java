/*    */ package org.springframework.http.server;
/*    */ 
/*    */ import java.net.URI;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface RequestPath
/*    */   extends PathContainer
/*    */ {
/*    */   PathContainer contextPath();
/*    */   
/*    */   PathContainer pathWithinApplication();
/*    */   
/*    */   RequestPath modifyContextPath(String paramString);
/*    */   
/*    */   static RequestPath parse(URI uri, @Nullable String contextPath) {
/* 60 */     return new DefaultRequestPath(uri, contextPath);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/RequestPath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */