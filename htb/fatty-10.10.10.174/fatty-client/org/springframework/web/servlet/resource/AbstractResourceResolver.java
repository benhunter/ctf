/*    */ package org.springframework.web.servlet.resource;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.core.io.Resource;
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
/*    */ public abstract class AbstractResourceResolver
/*    */   implements ResourceResolver
/*    */ {
/* 37 */   protected final Log logger = LogFactory.getLog(getClass());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Resource resolveResource(@Nullable HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
/* 45 */     return resolveResourceInternal(request, requestPath, locations, chain);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String resolveUrlPath(String resourceUrlPath, List<? extends Resource> locations, ResourceResolverChain chain) {
/* 53 */     return resolveUrlPathInternal(resourceUrlPath, locations, chain);
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   protected abstract Resource resolveResourceInternal(@Nullable HttpServletRequest paramHttpServletRequest, String paramString, List<? extends Resource> paramList, ResourceResolverChain paramResourceResolverChain);
/*    */   
/*    */   @Nullable
/*    */   protected abstract String resolveUrlPathInternal(String paramString, List<? extends Resource> paramList, ResourceResolverChain paramResourceResolverChain);
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/resource/AbstractResourceResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */