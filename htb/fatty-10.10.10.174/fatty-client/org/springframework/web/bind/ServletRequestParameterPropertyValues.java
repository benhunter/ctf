/*    */ package org.springframework.web.bind;
/*    */ 
/*    */ import javax.servlet.ServletRequest;
/*    */ import org.springframework.beans.MutablePropertyValues;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.web.util.WebUtils;
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
/*    */ public class ServletRequestParameterPropertyValues
/*    */   extends MutablePropertyValues
/*    */ {
/*    */   public static final String DEFAULT_PREFIX_SEPARATOR = "_";
/*    */   
/*    */   public ServletRequestParameterPropertyValues(ServletRequest request) {
/* 53 */     this(request, null, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ServletRequestParameterPropertyValues(ServletRequest request, @Nullable String prefix) {
/* 65 */     this(request, prefix, "_");
/*    */   }
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
/*    */   public ServletRequestParameterPropertyValues(ServletRequest request, @Nullable String prefix, @Nullable String prefixSeparator) {
/* 80 */     super(WebUtils.getParametersStartingWith(request, (prefix != null) ? (prefix + prefixSeparator) : null));
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/bind/ServletRequestParameterPropertyValues.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */