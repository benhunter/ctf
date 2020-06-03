/*    */ package htb.fatty.client.run;
/*    */ 
/*    */ import htb.fatty.client.gui.ClientGuiTest;
/*    */ import htb.fatty.shared.logging.FattyLogger;
/*    */ 
/*    */ 
/*    */ public class Starter
/*    */ {
/*    */   public static void main(String[] argv) {
/* 10 */     FattyLogger logger = new FattyLogger();
/* 11 */     logger.logInfo("[+] Fatty starts running. Run Fatty, run!");
/*    */ 
/*    */     
/* 14 */     logger.logInfo("[+] Starting UI!");
/* 15 */     ClientGuiTest ui = new ClientGuiTest();
/* 16 */     ui.setVisible(true);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/htb/fatty/client/run/Starter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */