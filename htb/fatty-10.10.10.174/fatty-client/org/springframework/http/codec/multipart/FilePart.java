/*    */ package org.springframework.http.codec.multipart;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.nio.file.Path;
/*    */ import reactor.core.publisher.Mono;
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
/*    */ public interface FilePart
/*    */   extends Part
/*    */ {
/*    */   String filename();
/*    */   
/*    */   default Mono<Void> transferTo(File dest) {
/* 50 */     return transferTo(dest.toPath());
/*    */   }
/*    */   
/*    */   Mono<Void> transferTo(Path paramPath);
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/multipart/FilePart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */