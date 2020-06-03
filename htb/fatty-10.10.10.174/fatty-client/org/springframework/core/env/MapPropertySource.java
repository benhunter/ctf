/*    */ package org.springframework.core.env;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ public class MapPropertySource
/*    */   extends EnumerablePropertySource<Map<String, Object>>
/*    */ {
/*    */   public MapPropertySource(String name, Map<String, Object> source) {
/* 35 */     super(name, source);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object getProperty(String name) {
/* 42 */     return this.source.get(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean containsProperty(String name) {
/* 47 */     return this.source.containsKey(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public String[] getPropertyNames() {
/* 52 */     return StringUtils.toStringArray(this.source.keySet());
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/env/MapPropertySource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */