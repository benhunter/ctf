/*    */ package org.springframework.http;
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
/*    */ public interface ZeroCopyHttpOutputMessage
/*    */   extends ReactiveHttpOutputMessage
/*    */ {
/*    */   default Mono<Void> writeWith(File file, long position, long count) {
/* 44 */     return writeWith(file.toPath(), position, count);
/*    */   }
/*    */   
/*    */   Mono<Void> writeWith(Path paramPath, long paramLong1, long paramLong2);
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/ZeroCopyHttpOutputMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */