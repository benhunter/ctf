/*    */ package org.springframework.core.io;
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
/*    */ public class FileSystemResourceLoader
/*    */   extends DefaultResourceLoader
/*    */ {
/*    */   protected Resource getResourceByPath(String path) {
/* 51 */     if (path.startsWith("/")) {
/* 52 */       path = path.substring(1);
/*    */     }
/* 54 */     return new FileSystemContextResource(path);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static class FileSystemContextResource
/*    */     extends FileSystemResource
/*    */     implements ContextResource
/*    */   {
/*    */     public FileSystemContextResource(String path) {
/* 65 */       super(path);
/*    */     }
/*    */ 
/*    */     
/*    */     public String getPathWithinContext() {
/* 70 */       return getPath();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/io/FileSystemResourceLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */