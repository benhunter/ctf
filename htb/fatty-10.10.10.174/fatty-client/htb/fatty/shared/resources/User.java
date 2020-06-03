/*     */ package htb.fatty.shared.resources;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import javax.xml.bind.DatatypeConverter;
/*     */ 
/*     */ 
/*     */ public class User
/*     */   implements Serializable
/*     */ {
/*     */   int uid;
/*     */   String username;
/*     */   String password;
/*     */   String email;
/*     */   Role role;
/*     */   
/*     */   public User(int uid, String username, String password, String email, Role role) {
/*  20 */     this.uid = uid;
/*  21 */     this.username = username;
/*     */     
/*  23 */     String hashString = this.username + password + "clarabibimakeseverythingsecure";
/*  24 */     MessageDigest digest = null;
/*     */     try {
/*  26 */       digest = MessageDigest.getInstance("SHA-256");
/*  27 */     } catch (NoSuchAlgorithmException e) {
/*  28 */       e.printStackTrace();
/*     */     } 
/*  30 */     byte[] hash = digest.digest(hashString.getBytes(StandardCharsets.UTF_8));
/*     */     
/*  32 */     this.password = DatatypeConverter.printHexBinary(hash);
/*  33 */     this.email = email;
/*  34 */     this.role = role;
/*     */   }
/*     */ 
/*     */   
/*     */   public User(int uid, String username, String password, String email, Role role, boolean hash) {
/*  39 */     this(uid, username, password, email, role);
/*  40 */     if (!hash) {
/*  41 */       this.password = password;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public User(String username, String password, boolean hash) {
/*  47 */     this(username, password);
/*  48 */     if (!hash) {
/*  49 */       this.password = password;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public User(String username, String password) {
/*  55 */     this(999, username, password, "", Role.getAnonymous());
/*     */   }
/*     */ 
/*     */   
/*     */   public User() {
/*  60 */     this(999, "", "", "", Role.getAnonymous());
/*     */   }
/*     */ 
/*     */   
/*     */   public static User anonymousUser() {
/*  65 */     User anonymousUser = new User(0, "anonymous", "anonymous", "anonymous@none.nonono", Role.getAnonymous());
/*  66 */     return anonymousUser;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUsername(String username) {
/*  71 */     this.username = username;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPassword(String password) {
/*  76 */     String hashString = this.username + password + "clarabibimakeseverythingsecure";
/*  77 */     MessageDigest digest = null;
/*     */     try {
/*  79 */       digest = MessageDigest.getInstance("SHA-256");
/*  80 */     } catch (NoSuchAlgorithmException e) {
/*  81 */       e.printStackTrace();
/*     */     } 
/*  83 */     byte[] hash = digest.digest(hashString.getBytes(StandardCharsets.UTF_8));
/*  84 */     this.password = DatatypeConverter.printHexBinary(hash);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUsername() {
/*  89 */     return this.username;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPassword() {
/*  94 */     return this.password;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRoleName() {
/*  99 */     return this.role.getRoleName();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRoleByName(String roleName) {
/* 104 */     this.role = Role.getRoleByName(roleName);
/*     */   }
/*     */ 
/*     */   
/*     */   public Role getRole() {
/* 109 */     return this.role;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/htb/fatty/shared/resources/User.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */