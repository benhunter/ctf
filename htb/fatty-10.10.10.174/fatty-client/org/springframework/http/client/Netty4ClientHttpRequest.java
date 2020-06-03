/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import io.netty.bootstrap.Bootstrap;
/*     */ import io.netty.buffer.ByteBufOutputStream;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.SimpleChannelInboundHandler;
/*     */ import io.netty.handler.codec.http.DefaultFullHttpRequest;
/*     */ import io.netty.handler.codec.http.FullHttpRequest;
/*     */ import io.netty.handler.codec.http.FullHttpResponse;
/*     */ import io.netty.handler.codec.http.HttpMethod;
/*     */ import io.netty.handler.codec.http.HttpVersion;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URI;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.util.concurrent.ListenableFuture;
/*     */ import org.springframework.util.concurrent.SettableListenableFuture;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ class Netty4ClientHttpRequest
/*     */   extends AbstractAsyncClientHttpRequest
/*     */   implements ClientHttpRequest
/*     */ {
/*     */   private final Bootstrap bootstrap;
/*     */   private final URI uri;
/*     */   private final HttpMethod method;
/*     */   private final ByteBufOutputStream body;
/*     */   
/*     */   public Netty4ClientHttpRequest(Bootstrap bootstrap, URI uri, HttpMethod method) {
/*  66 */     this.bootstrap = bootstrap;
/*  67 */     this.uri = uri;
/*  68 */     this.method = method;
/*  69 */     this.body = new ByteBufOutputStream(Unpooled.buffer(1024));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpMethod getMethod() {
/*  75 */     return this.method;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMethodValue() {
/*  80 */     return this.method.name();
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURI() {
/*  85 */     return this.uri;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientHttpResponse execute() throws IOException {
/*     */     try {
/*  91 */       return (ClientHttpResponse)executeAsync().get();
/*     */     }
/*  93 */     catch (InterruptedException ex) {
/*  94 */       Thread.currentThread().interrupt();
/*  95 */       throw new IOException("Interrupted during request execution", ex);
/*     */     }
/*  97 */     catch (ExecutionException ex) {
/*  98 */       if (ex.getCause() instanceof IOException) {
/*  99 */         throw (IOException)ex.getCause();
/*     */       }
/*     */       
/* 102 */       throw new IOException(ex.getMessage(), ex.getCause());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected OutputStream getBodyInternal(HttpHeaders headers) throws IOException {
/* 109 */     return (OutputStream)this.body;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ListenableFuture<ClientHttpResponse> executeInternal(HttpHeaders headers) throws IOException {
/* 114 */     SettableListenableFuture<ClientHttpResponse> responseFuture = new SettableListenableFuture();
/*     */     
/* 116 */     ChannelFutureListener connectionListener = future -> {
/*     */         if (future.isSuccess()) {
/*     */           Channel channel = future.channel();
/*     */           
/*     */           channel.pipeline().addLast(new ChannelHandler[] { (ChannelHandler)new RequestExecuteHandler(responseFuture) });
/*     */           
/*     */           FullHttpRequest nettyRequest = createFullHttpRequest(headers);
/*     */           channel.writeAndFlush(nettyRequest);
/*     */         } else {
/*     */           responseFuture.setException(future.cause());
/*     */         } 
/*     */       };
/* 128 */     this.bootstrap.connect(this.uri.getHost(), getPort(this.uri)).addListener((GenericFutureListener)connectionListener);
/* 129 */     return (ListenableFuture<ClientHttpResponse>)responseFuture;
/*     */   }
/*     */ 
/*     */   
/*     */   private FullHttpRequest createFullHttpRequest(HttpHeaders headers) {
/* 134 */     HttpMethod nettyMethod = HttpMethod.valueOf(this.method.name());
/*     */     
/* 136 */     String authority = this.uri.getRawAuthority();
/* 137 */     String path = this.uri.toString().substring(this.uri.toString().indexOf(authority) + authority.length());
/*     */     
/* 139 */     DefaultFullHttpRequest defaultFullHttpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, nettyMethod, path, this.body.buffer());
/*     */     
/* 141 */     defaultFullHttpRequest.headers().set("Host", this.uri.getHost() + ":" + getPort(this.uri));
/* 142 */     defaultFullHttpRequest.headers().set("Connection", "close");
/* 143 */     headers.forEach((headerName, headerValues) -> nettyRequest.headers().add(headerName, headerValues));
/* 144 */     if (!defaultFullHttpRequest.headers().contains("Content-Length") && this.body.buffer().readableBytes() > 0) {
/* 145 */       defaultFullHttpRequest.headers().set("Content-Length", Integer.valueOf(this.body.buffer().readableBytes()));
/*     */     }
/*     */     
/* 148 */     return (FullHttpRequest)defaultFullHttpRequest;
/*     */   }
/*     */   
/*     */   private static int getPort(URI uri) {
/* 152 */     int port = uri.getPort();
/* 153 */     if (port == -1) {
/* 154 */       if ("http".equalsIgnoreCase(uri.getScheme())) {
/* 155 */         port = 80;
/*     */       }
/* 157 */       else if ("https".equalsIgnoreCase(uri.getScheme())) {
/* 158 */         port = 443;
/*     */       } 
/*     */     }
/* 161 */     return port;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class RequestExecuteHandler
/*     */     extends SimpleChannelInboundHandler<FullHttpResponse>
/*     */   {
/*     */     private final SettableListenableFuture<ClientHttpResponse> responseFuture;
/*     */ 
/*     */     
/*     */     public RequestExecuteHandler(SettableListenableFuture<ClientHttpResponse> responseFuture) {
/* 173 */       this.responseFuture = responseFuture;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void channelRead0(ChannelHandlerContext context, FullHttpResponse response) throws Exception {
/* 178 */       this.responseFuture.set(new Netty4ClientHttpResponse(context, response));
/*     */     }
/*     */ 
/*     */     
/*     */     public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
/* 183 */       this.responseFuture.setException(cause);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/Netty4ClientHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */