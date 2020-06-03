/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import io.netty.bootstrap.Bootstrap;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.EventLoopGroup;
/*     */ import io.netty.channel.nio.NioEventLoopGroup;
/*     */ import io.netty.channel.socket.SocketChannel;
/*     */ import io.netty.channel.socket.SocketChannelConfig;
/*     */ import io.netty.channel.socket.nio.NioSocketChannel;
/*     */ import io.netty.handler.codec.http.HttpClientCodec;
/*     */ import io.netty.handler.codec.http.HttpObjectAggregator;
/*     */ import io.netty.handler.ssl.SslContext;
/*     */ import io.netty.handler.ssl.SslContextBuilder;
/*     */ import io.netty.handler.timeout.ReadTimeoutHandler;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.net.ssl.SSLException;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class Netty4ClientHttpRequestFactory
/*     */   implements ClientHttpRequestFactory, AsyncClientHttpRequestFactory, InitializingBean, DisposableBean
/*     */ {
/*     */   public static final int DEFAULT_MAX_RESPONSE_SIZE = 10485760;
/*     */   private final EventLoopGroup eventLoopGroup;
/*     */   private final boolean defaultEventLoopGroup;
/*  78 */   private int maxResponseSize = 10485760;
/*     */   
/*     */   @Nullable
/*     */   private SslContext sslContext;
/*     */   
/*  83 */   private int connectTimeout = -1;
/*     */   
/*  85 */   private int readTimeout = -1;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private volatile Bootstrap bootstrap;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Netty4ClientHttpRequestFactory() {
/*  96 */     int ioWorkerCount = Runtime.getRuntime().availableProcessors() * 2;
/*  97 */     this.eventLoopGroup = (EventLoopGroup)new NioEventLoopGroup(ioWorkerCount);
/*  98 */     this.defaultEventLoopGroup = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Netty4ClientHttpRequestFactory(EventLoopGroup eventLoopGroup) {
/* 109 */     Assert.notNull(eventLoopGroup, "EventLoopGroup must not be null");
/* 110 */     this.eventLoopGroup = eventLoopGroup;
/* 111 */     this.defaultEventLoopGroup = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxResponseSize(int maxResponseSize) {
/* 122 */     this.maxResponseSize = maxResponseSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSslContext(SslContext sslContext) {
/* 131 */     this.sslContext = sslContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConnectTimeout(int connectTimeout) {
/* 140 */     this.connectTimeout = connectTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReadTimeout(int readTimeout) {
/* 149 */     this.readTimeout = readTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 155 */     if (this.sslContext == null) {
/* 156 */       this.sslContext = getDefaultClientSslContext();
/*     */     }
/*     */   }
/*     */   
/*     */   private SslContext getDefaultClientSslContext() {
/*     */     try {
/* 162 */       return SslContextBuilder.forClient().build();
/*     */     }
/* 164 */     catch (SSLException ex) {
/* 165 */       throw new IllegalStateException("Could not create default client SslContext", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
/* 172 */     return createRequestInternal(uri, httpMethod);
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncClientHttpRequest createAsyncRequest(URI uri, HttpMethod httpMethod) throws IOException {
/* 177 */     return createRequestInternal(uri, httpMethod);
/*     */   }
/*     */   
/*     */   private Netty4ClientHttpRequest createRequestInternal(URI uri, HttpMethod httpMethod) {
/* 181 */     return new Netty4ClientHttpRequest(getBootstrap(uri), uri, httpMethod);
/*     */   }
/*     */   
/*     */   private Bootstrap getBootstrap(URI uri) {
/* 185 */     boolean isSecure = (uri.getPort() == 443 || "https".equalsIgnoreCase(uri.getScheme()));
/* 186 */     if (isSecure) {
/* 187 */       return buildBootstrap(uri, true);
/*     */     }
/*     */     
/* 190 */     Bootstrap bootstrap = this.bootstrap;
/* 191 */     if (bootstrap == null) {
/* 192 */       bootstrap = buildBootstrap(uri, false);
/* 193 */       this.bootstrap = bootstrap;
/*     */     } 
/* 195 */     return bootstrap;
/*     */   }
/*     */ 
/*     */   
/*     */   private Bootstrap buildBootstrap(final URI uri, final boolean isSecure) {
/* 200 */     Bootstrap bootstrap = new Bootstrap();
/* 201 */     ((Bootstrap)((Bootstrap)bootstrap.group(this.eventLoopGroup)).channel(NioSocketChannel.class))
/* 202 */       .handler((ChannelHandler)new ChannelInitializer<SocketChannel>()
/*     */         {
/*     */           protected void initChannel(SocketChannel channel) throws Exception {
/* 205 */             Netty4ClientHttpRequestFactory.this.configureChannel(channel.config());
/* 206 */             ChannelPipeline pipeline = channel.pipeline();
/* 207 */             if (isSecure) {
/* 208 */               Assert.notNull(Netty4ClientHttpRequestFactory.this.sslContext, "sslContext should not be null");
/* 209 */               pipeline.addLast(new ChannelHandler[] { (ChannelHandler)Netty4ClientHttpRequestFactory.access$000(this.this$0).newHandler(channel.alloc(), this.val$uri.getHost(), this.val$uri.getPort()) });
/*     */             } 
/* 211 */             pipeline.addLast(new ChannelHandler[] { (ChannelHandler)new HttpClientCodec() });
/* 212 */             pipeline.addLast(new ChannelHandler[] { (ChannelHandler)new HttpObjectAggregator(Netty4ClientHttpRequestFactory.access$100(this.this$0)) });
/* 213 */             if (Netty4ClientHttpRequestFactory.this.readTimeout > 0) {
/* 214 */               pipeline.addLast(new ChannelHandler[] { (ChannelHandler)new ReadTimeoutHandler(Netty4ClientHttpRequestFactory.access$200(this.this$0), TimeUnit.MILLISECONDS) });
/*     */             }
/*     */           }
/*     */         });
/*     */     
/* 219 */     return bootstrap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void configureChannel(SocketChannelConfig config) {
/* 228 */     if (this.connectTimeout >= 0) {
/* 229 */       config.setConnectTimeoutMillis(this.connectTimeout);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() throws InterruptedException {
/* 236 */     if (this.defaultEventLoopGroup)
/*     */     {
/* 238 */       this.eventLoopGroup.shutdownGracefully().sync();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/Netty4ClientHttpRequestFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */