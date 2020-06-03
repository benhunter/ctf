/*    */ package org.springframework.context.annotation;
/*    */ 
/*    */ import org.springframework.core.env.Profiles;
/*    */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*    */ import org.springframework.util.MultiValueMap;
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
/*    */ class ProfileCondition
/*    */   implements Condition
/*    */ {
/*    */   public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
/* 36 */     MultiValueMap<String, Object> attrs = metadata.getAllAnnotationAttributes(Profile.class.getName());
/* 37 */     if (attrs != null) {
/* 38 */       for (Object value : attrs.get("value")) {
/* 39 */         if (context.getEnvironment().acceptsProfiles(Profiles.of((String[])value))) {
/* 40 */           return true;
/*    */         }
/*    */       } 
/* 43 */       return false;
/*    */     } 
/* 45 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/ProfileCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */