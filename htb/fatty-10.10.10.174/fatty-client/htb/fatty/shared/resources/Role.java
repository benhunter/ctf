/*    */ package htb.fatty.shared.resources;
/*    */ 
/*    */ import java.util.stream.IntStream;
/*    */ 
/*    */ 
/*    */ public class Role
/*    */ {
/*    */   int rid;
/*    */   String roleName;
/*    */   int[] allowedMethods;
/*    */   
/*    */   public Role(int rid, String roleName, int[] allowedMethods) {
/* 13 */     this.rid = rid;
/* 14 */     this.roleName = roleName;
/* 15 */     this.allowedMethods = allowedMethods;
/*    */   }
/*    */ 
/*    */   
/*    */   public static Role getAdminRole() {
/* 20 */     return new Role(0, "admin", new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 });
/*    */   }
/*    */ 
/*    */   
/*    */   public static Role getUserRole() {
/* 25 */     return new Role(0, "user", new int[] { 1, 2, 3, 4, 5, 6 });
/*    */   }
/*    */ 
/*    */   
/*    */   public static Role getAnonymous() {
/* 30 */     return new Role(0, "anonymous", new int[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getRoleName() {
/* 35 */     return this.roleName;
/*    */   }
/*    */ 
/*    */   
/*    */   public int[] getAllowedMethods() {
/* 40 */     return this.allowedMethods;
/*    */   }
/*    */ 
/*    */   
/*    */   public static Role getRoleByName(String name) {
/* 45 */     if (name.equalsIgnoreCase("admin"))
/* 46 */       return getAdminRole(); 
/* 47 */     if (name.equalsIgnoreCase("user")) {
/* 48 */       return getUserRole();
/*    */     }
/* 50 */     System.out.println(name);
/* 51 */     return getAnonymous();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isAllowed(int id) {
/* 57 */     return IntStream.of(this.allowedMethods).anyMatch(x -> (x == id));
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/htb/fatty/shared/resources/Role.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */