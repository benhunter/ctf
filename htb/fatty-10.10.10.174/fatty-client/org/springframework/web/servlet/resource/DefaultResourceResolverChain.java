/*    */ package org.springframework.web.servlet.resource;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.ListIterator;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.springframework.core.io.Resource;
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
/*    */ class DefaultResourceResolverChain
/*    */   implements ResourceResolverChain
/*    */ {
/*    */   @Nullable
/*    */   private final ResourceResolver resolver;
/*    */   @Nullable
/*    */   private final ResourceResolverChain nextChain;
/*    */   
/*    */   public DefaultResourceResolverChain(@Nullable List<? extends ResourceResolver> resolvers) {
/* 45 */     resolvers = (resolvers != null) ? resolvers : Collections.<ResourceResolver>emptyList();
/* 46 */     DefaultResourceResolverChain chain = initChain(new ArrayList<>(resolvers));
/* 47 */     this.resolver = chain.resolver;
/* 48 */     this.nextChain = chain.nextChain;
/*    */   }
/*    */   
/*    */   private static DefaultResourceResolverChain initChain(ArrayList<? extends ResourceResolver> resolvers) {
/* 52 */     DefaultResourceResolverChain chain = new DefaultResourceResolverChain(null, null);
/* 53 */     ListIterator<? extends ResourceResolver> it = resolvers.listIterator(resolvers.size());
/* 54 */     while (it.hasPrevious()) {
/* 55 */       chain = new DefaultResourceResolverChain(it.previous(), chain);
/*    */     }
/* 57 */     return chain;
/*    */   }
/*    */   
/*    */   private DefaultResourceResolverChain(@Nullable ResourceResolver resolver, @Nullable ResourceResolverChain chain) {
/* 61 */     Assert.isTrue(((resolver == null && chain == null) || (resolver != null && chain != null)), "Both resolver and resolver chain must be null, or neither is");
/*    */     
/* 63 */     this.resolver = resolver;
/* 64 */     this.nextChain = chain;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Resource resolveResource(@Nullable HttpServletRequest request, String requestPath, List<? extends Resource> locations) {
/* 73 */     return (this.resolver != null && this.nextChain != null) ? this.resolver
/* 74 */       .resolveResource(request, requestPath, locations, this.nextChain) : null;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String resolveUrlPath(String resourcePath, List<? extends Resource> locations) {
/* 80 */     return (this.resolver != null && this.nextChain != null) ? this.resolver
/* 81 */       .resolveUrlPath(resourcePath, locations, this.nextChain) : null;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/resource/DefaultResourceResolverChain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */