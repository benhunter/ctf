/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import io.netty.buffer.ByteBufInputStream;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.http.FullHttpResponse;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Map;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
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
/*    */ @Deprecated
/*    */ class Netty4ClientHttpResponse
/*    */   extends AbstractClientHttpResponse
/*    */ {
/*    */   private final ChannelHandlerContext context;
/*    */   private final FullHttpResponse nettyResponse;
/*    */   private final ByteBufInputStream body;
/*    */   @Nullable
/*    */   private volatile HttpHeaders headers;
/*    */   
/*    */   public Netty4ClientHttpResponse(ChannelHandlerContext context, FullHttpResponse nettyResponse) {
/* 53 */     Assert.notNull(context, "ChannelHandlerContext must not be null");
/* 54 */     Assert.notNull(nettyResponse, "FullHttpResponse must not be null");
/* 55 */     this.context = context;
/* 56 */     this.nettyResponse = nettyResponse;
/* 57 */     this.body = new ByteBufInputStream(this.nettyResponse.content());
/* 58 */     this.nettyResponse.retain();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getRawStatusCode() throws IOException {
/* 64 */     return this.nettyResponse.getStatus().code();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getStatusText() throws IOException {
/* 69 */     return this.nettyResponse.getStatus().reasonPhrase();
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpHeaders getHeaders() {
/* 74 */     HttpHeaders headers = this.headers;
/* 75 */     if (headers == null) {
/* 76 */       headers = new HttpHeaders();
/* 77 */       for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)this.nettyResponse.headers()) {
/* 78 */         headers.add(entry.getKey(), entry.getValue());
/*    */       }
/* 80 */       this.headers = headers;
/*    */     } 
/* 82 */     return headers;
/*    */   }
/*    */ 
/*    */   
/*    */   public InputStream getBody() throws IOException {
/* 87 */     return (InputStream)this.body;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 92 */     this.nettyResponse.release();
/* 93 */     this.context.close();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/Netty4ClientHttpResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */