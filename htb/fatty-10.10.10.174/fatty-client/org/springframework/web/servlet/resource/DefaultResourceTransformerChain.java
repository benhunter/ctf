/*    */ package org.springframework.web.servlet.resource;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ class DefaultResourceTransformerChain
/*    */   implements ResourceTransformerChain
/*    */ {
/*    */   private final ResourceResolverChain resolverChain;
/*    */   @Nullable
/*    */   private final ResourceTransformer transformer;
/*    */   @Nullable
/*    */   private final ResourceTransformerChain nextChain;
/*    */   
/*    */   public DefaultResourceTransformerChain(ResourceResolverChain resolverChain, @Nullable List<ResourceTransformer> transformers) {
/* 50 */     Assert.notNull(resolverChain, "ResourceResolverChain is required");
/* 51 */     this.resolverChain = resolverChain;
/* 52 */     transformers = (transformers != null) ? transformers : Collections.<ResourceTransformer>emptyList();
/* 53 */     DefaultResourceTransformerChain chain = initTransformerChain(resolverChain, new ArrayList<>(transformers));
/* 54 */     this.transformer = chain.transformer;
/* 55 */     this.nextChain = chain.nextChain;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private DefaultResourceTransformerChain initTransformerChain(ResourceResolverChain resolverChain, ArrayList<ResourceTransformer> transformers) {
/* 61 */     DefaultResourceTransformerChain chain = new DefaultResourceTransformerChain(resolverChain, null, null);
/* 62 */     ListIterator<? extends ResourceTransformer> it = transformers.listIterator(transformers.size());
/* 63 */     while (it.hasPrevious()) {
/* 64 */       chain = new DefaultResourceTransformerChain(resolverChain, it.previous(), chain);
/*    */     }
/* 66 */     return chain;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultResourceTransformerChain(ResourceResolverChain resolverChain, @Nullable ResourceTransformer transformer, @Nullable ResourceTransformerChain chain) {
/* 72 */     Assert.isTrue(((transformer == null && chain == null) || (transformer != null && chain != null)), "Both transformer and transformer chain must be null, or neither is");
/*    */ 
/*    */     
/* 75 */     this.resolverChain = resolverChain;
/* 76 */     this.transformer = transformer;
/* 77 */     this.nextChain = chain;
/*    */   }
/*    */ 
/*    */   
/*    */   public ResourceResolverChain getResolverChain() {
/* 82 */     return this.resolverChain;
/*    */   }
/*    */ 
/*    */   
/*    */   public Resource transform(HttpServletRequest request, Resource resource) throws IOException {
/* 87 */     return (this.transformer != null && this.nextChain != null) ? this.transformer
/* 88 */       .transform(request, resource, this.nextChain) : resource;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/resource/DefaultResourceTransformerChain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */