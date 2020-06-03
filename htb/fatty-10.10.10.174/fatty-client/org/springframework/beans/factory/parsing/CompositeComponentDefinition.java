/*    */ package org.springframework.beans.factory.parsing;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public class CompositeComponentDefinition
/*    */   extends AbstractComponentDefinition
/*    */ {
/*    */   private final String name;
/*    */   @Nullable
/*    */   private final Object source;
/* 41 */   private final List<ComponentDefinition> nestedComponents = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CompositeComponentDefinition(String name, @Nullable Object source) {
/* 50 */     Assert.notNull(name, "Name must not be null");
/* 51 */     this.name = name;
/* 52 */     this.source = source;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 58 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object getSource() {
/* 64 */     return this.source;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addNestedComponent(ComponentDefinition component) {
/* 73 */     Assert.notNull(component, "ComponentDefinition must not be null");
/* 74 */     this.nestedComponents.add(component);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ComponentDefinition[] getNestedComponents() {
/* 82 */     return this.nestedComponents.<ComponentDefinition>toArray(new ComponentDefinition[0]);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/parsing/CompositeComponentDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */