/*    */ package org.springframework.web.bind.annotation;
/*    */ 
/*    */ import java.lang.annotation.Documented;
/*    */ import java.lang.annotation.ElementType;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
/*    */ import org.springframework.core.annotation.AliasFor;
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
/*    */ @Target({ElementType.TYPE, ElementType.METHOD})
/*    */ @Retention(RetentionPolicy.RUNTIME)
/*    */ @Documented
/*    */ public @interface CrossOrigin
/*    */ {
/*    */   @Deprecated
/* 57 */   public static final String[] DEFAULT_ORIGINS = new String[] { "*" };
/*    */ 
/*    */   
/*    */   @Deprecated
/* 61 */   public static final String[] DEFAULT_ALLOWED_HEADERS = new String[] { "*" };
/*    */   @Deprecated
/*    */   public static final boolean DEFAULT_ALLOW_CREDENTIALS = false;
/*    */   @Deprecated
/*    */   public static final long DEFAULT_MAX_AGE = 1800L;
/*    */   
/*    */   @AliasFor("origins")
/*    */   String[] value() default {};
/*    */   
/*    */   @AliasFor("value")
/*    */   String[] origins() default {};
/*    */   
/*    */   String[] allowedHeaders() default {};
/*    */   
/*    */   String[] exposedHeaders() default {};
/*    */   
/*    */   RequestMethod[] methods() default {};
/*    */   
/*    */   String allowCredentials() default "";
/*    */   
/*    */   long maxAge() default -1L;
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/bind/annotation/CrossOrigin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */