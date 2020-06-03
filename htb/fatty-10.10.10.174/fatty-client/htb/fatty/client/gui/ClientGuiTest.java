/*     */ package htb.fatty.client.gui;
/*     */ 
/*     */ import htb.fatty.client.connection.Connection;
/*     */ import htb.fatty.client.methods.Invoker;
/*     */ import htb.fatty.shared.message.MessageBuildException;
/*     */ import htb.fatty.shared.resources.User;
/*     */ import java.awt.Color;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.Font;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.io.IOException;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JLayeredPane;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JPasswordField;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.JTextPane;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClientGuiTest
/*     */   extends JFrame
/*     */ {
/*     */   private JPanel contentPane;
/*     */   private JTextField tfUsername;
/*     */   private JPasswordField tfPassword;
/*     */   private User user;
/*     */   private Connection conn;
/*     */   private Invoker invoker;
/*     */   private JTextField fileTextField;
/*     */   private JTextField textField_1;
/*     */   private JTextField textField_2;
/*  45 */   private String currentFolder = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/*  51 */     EventQueue.invokeLater(new Runnable() {
/*     */           public void run() {
/*     */             try {
/*  54 */               ClientGuiTest frame = new ClientGuiTest();
/*  55 */               frame.setVisible(true);
/*  56 */             } catch (Exception e) {
/*  57 */               e.printStackTrace();
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientGuiTest() {
/*  67 */     setDefaultCloseOperation(3);
/*  68 */     setBounds(100, 100, 872, 691);
/*     */     
/*  70 */     JMenuBar menuBar = new JMenuBar();
/*  71 */     setJMenuBar(menuBar);
/*     */     
/*  73 */     JMenu fileMenu = new JMenu("File");
/*  74 */     menuBar.add(fileMenu);
/*     */     
/*  76 */     JMenuItem exit = new JMenuItem("Exit");
/*  77 */     fileMenu.add(exit);
/*  78 */     exit.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/*  81 */             if (ClientGuiTest.this.conn != null) {
/*  82 */               ClientGuiTest.this.conn.logoff();
/*  83 */               ClientGuiTest.this.conn.close();
/*     */             } 
/*  85 */             ClientGuiTest.this.dispose();
/*  86 */             System.exit(0);
/*     */           }
/*     */         });
/*     */     
/*  90 */     JMenu profileMenu = new JMenu("Profile");
/*  91 */     menuBar.add(profileMenu);
/*     */     
/*  93 */     final JMenuItem whoami = new JMenuItem("Whoami");
/*  94 */     whoami.setEnabled(false);
/*  95 */     profileMenu.add(whoami);
/*     */     
/*  97 */     final JMenuItem changePassword = new JMenuItem("ChangePassword");
/*  98 */     changePassword.setEnabled(false);
/*  99 */     profileMenu.add(changePassword);
/*     */     
/* 101 */     JMenu statusMenu = new JMenu("ServerStatus");
/* 102 */     menuBar.add(statusMenu);
/*     */     
/* 104 */     final JMenuItem uname = new JMenuItem("Uname");
/* 105 */     uname.setEnabled(false);
/* 106 */     statusMenu.add(uname);
/*     */     
/* 108 */     final JMenuItem users = new JMenuItem("Users");
/* 109 */     users.setEnabled(false);
/* 110 */     statusMenu.add(users);
/*     */     
/* 112 */     final JMenuItem netstat = new JMenuItem("Nestat");
/* 113 */     netstat.setEnabled(false);
/* 114 */     statusMenu.add(netstat);
/*     */     
/* 116 */     final JMenuItem ipconfig = new JMenuItem("Ipconfig");
/* 117 */     ipconfig.setEnabled(false);
/* 118 */     statusMenu.add(ipconfig);
/*     */     
/* 120 */     JMenu fileBrowser = new JMenu("FileBrowser");
/* 121 */     menuBar.add(fileBrowser);
/*     */     
/* 123 */     final JMenuItem configs = new JMenuItem("Configs");
/* 124 */     configs.setEnabled(false);
/* 125 */     fileBrowser.add(configs);
/*     */     
/* 127 */     final JMenuItem notes = new JMenuItem("Notes");
/* 128 */     notes.setEnabled(false);
/* 129 */     fileBrowser.add(notes);
/*     */     
/* 131 */     final JMenuItem mail = new JMenuItem("Mail");
/* 132 */     mail.setEnabled(false);
/* 133 */     fileBrowser.add(mail);
/*     */     
/* 135 */     JMenu connectionTest = new JMenu("ConnectionTest");
/* 136 */     menuBar.add(connectionTest);
/*     */     
/* 138 */     final JMenuItem ping = new JMenuItem("Ping");
/* 139 */     ping.setEnabled(false);
/*     */     
/* 141 */     connectionTest.add(ping);
/*     */     
/* 143 */     JMenu help = new JMenu("Help");
/* 144 */     menuBar.add(help);
/*     */     
/* 146 */     JMenuItem contact = new JMenuItem("Contact");
/* 147 */     help.add(contact);
/*     */     
/* 149 */     JMenuItem about = new JMenuItem("About");
/* 150 */     help.add(about);
/* 151 */     this.contentPane = new JPanel();
/* 152 */     this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
/* 153 */     setContentPane(this.contentPane);
/* 154 */     this.contentPane.setLayout((LayoutManager)null);
/*     */     
/* 156 */     JLayeredPane layeredPane = new JLayeredPane();
/* 157 */     layeredPane.setBounds(222, 193, 1, 1);
/* 158 */     this.contentPane.add(layeredPane);
/*     */     
/* 160 */     final JPanel controlPanel = new JPanel();
/* 161 */     controlPanel.setBounds(0, 0, 872, 638);
/* 162 */     controlPanel.setVisible(false);
/* 163 */     this.contentPane.add(controlPanel);
/* 164 */     controlPanel.setLayout((LayoutManager)null);
/*     */     
/* 166 */     JPanel panel = new JPanel();
/* 167 */     panel.setBackground(Color.WHITE);
/* 168 */     panel.setBounds(12, 12, 848, 583);
/* 169 */     controlPanel.add(panel);
/* 170 */     panel.setLayout((LayoutManager)null);
/*     */     
/* 172 */     final JTextPane textPane = new JTextPane();
/* 173 */     textPane.setEditable(false);
/* 174 */     textPane.setBounds(12, 12, 824, 559);
/* 175 */     panel.add(textPane);
/*     */     
/* 177 */     this.fileTextField = new JTextField();
/* 178 */     this.fileTextField.setBounds(28, 607, 164, 25);
/* 179 */     controlPanel.add(this.fileTextField);
/* 180 */     this.fileTextField.setColumns(10);
/*     */     
/* 182 */     JButton openFileButton = new JButton("Open");
/* 183 */     openFileButton.setBounds(204, 607, 114, 25);
/* 184 */     controlPanel.add(openFileButton);
/*     */     
/* 186 */     JButton btnClear = new JButton("Clear");
/* 187 */     btnClear.setBounds(731, 607, 114, 25);
/* 188 */     controlPanel.add(btnClear);
/*     */     
/* 190 */     final JPanel LoginPanel = new JPanel();
/* 191 */     LoginPanel.setBounds(12, 12, 944, 844);
/* 192 */     this.contentPane.add(LoginPanel);
/* 193 */     LoginPanel.setLayout((LayoutManager)null);
/*     */     
/* 195 */     JLabel lblNewLabel = new JLabel("Username:");
/* 196 */     lblNewLabel.setFont(new Font("Dialog", 1, 14));
/* 197 */     lblNewLabel.setBounds(118, 197, 151, 68);
/* 198 */     LoginPanel.add(lblNewLabel);
/*     */     
/* 200 */     this.tfUsername = new JTextField();
/* 201 */     this.tfUsername.setBounds(294, 218, 396, 27);
/* 202 */     LoginPanel.add(this.tfUsername);
/* 203 */     this.tfUsername.setColumns(10);
/*     */     
/* 205 */     this.tfPassword = new JPasswordField();
/* 206 */     this.tfPassword.setColumns(10);
/* 207 */     this.tfPassword.setBounds(294, 280, 396, 27);
/* 208 */     LoginPanel.add(this.tfPassword);
/*     */     
/* 210 */     JButton btnNewButton = new JButton("Login ");
/* 211 */     btnNewButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/* 213 */             String username = ClientGuiTest.this.tfUsername.getText().trim();
/* 214 */             String password = new String(ClientGuiTest.this.tfPassword.getPassword());
/* 215 */             ClientGuiTest.this.user = new User();
/* 216 */             ClientGuiTest.this.user.setUsername(username);
/* 217 */             ClientGuiTest.this.user.setPassword(password);
/*     */             
/*     */             try {
/* 220 */               ClientGuiTest.this.conn = Connection.getConnection();
/* 221 */             } catch (htb.fatty.client.connection.Connection.ConnectionException e1) {
/* 222 */               JOptionPane.showMessageDialog(LoginPanel, "Connection Error!", "Error", 0);
/*     */ 
/*     */               
/*     */               return;
/*     */             } 
/*     */             
/* 228 */             if (ClientGuiTest.this.conn.login(ClientGuiTest.this.user)) {
/* 229 */               JOptionPane.showMessageDialog(LoginPanel, "Login Successful!", "Login", 1);
/*     */ 
/*     */ 
/*     */               
/* 233 */               LoginPanel.setVisible(false);
/*     */               
/* 235 */               String roleName = ClientGuiTest.this.conn.getRoleName();
/* 236 */               ClientGuiTest.this.user.setRoleByName(roleName);
/*     */               
/* 238 */               if (roleName.contentEquals("admin")) {
/* 239 */                 uname.setEnabled(true);
/* 240 */                 users.setEnabled(true);
/* 241 */                 netstat.setEnabled(true);
/* 242 */                 ipconfig.setEnabled(true);
/* 243 */                 changePassword.setEnabled(true);
/*     */               } 
/* 245 */               if (!roleName.contentEquals("anonymous")) {
/* 246 */                 whoami.setEnabled(true);
/* 247 */                 configs.setEnabled(true);
/* 248 */                 notes.setEnabled(true);
/* 249 */                 mail.setEnabled(true);
/* 250 */                 ping.setEnabled(true);
/*     */               } 
/* 252 */               ClientGuiTest.this.invoker = new Invoker(ClientGuiTest.this.conn, ClientGuiTest.this.user);
/* 253 */               controlPanel.setVisible(true);
/*     */             } else {
/*     */               
/* 256 */               JOptionPane.showMessageDialog(LoginPanel, "Login Failed!", "Login", 1);
/*     */ 
/*     */ 
/*     */               
/* 260 */               ClientGuiTest.this.conn.close();
/*     */             } 
/*     */           }
/*     */         });
/* 264 */     btnNewButton.setBounds(572, 339, 117, 25);
/* 265 */     LoginPanel.add(btnNewButton);
/*     */     
/* 267 */     JLabel lblPassword = new JLabel("Password:");
/* 268 */     lblPassword.setFont(new Font("Dialog", 1, 14));
/* 269 */     lblPassword.setBounds(118, 259, 151, 68);
/* 270 */     LoginPanel.add(lblPassword);
/*     */     
/* 272 */     final JPanel passwordChange = new JPanel();
/* 273 */     passwordChange.setBounds(0, 0, 860, 638);
/* 274 */     passwordChange.setVisible(false);
/* 275 */     this.contentPane.add(passwordChange);
/* 276 */     passwordChange.setLayout((LayoutManager)null);
/*     */     
/* 278 */     this.textField_1 = new JTextField();
/* 279 */     this.textField_1.setBounds(355, 258, 263, 29);
/* 280 */     passwordChange.add(this.textField_1);
/* 281 */     this.textField_1.setColumns(10);
/*     */     
/* 283 */     JLabel lblOldPassword = new JLabel("Old Password:");
/* 284 */     lblOldPassword.setFont(new Font("Dialog", 1, 14));
/* 285 */     lblOldPassword.setBounds(206, 265, 131, 17);
/* 286 */     passwordChange.add(lblOldPassword);
/*     */     
/* 288 */     JLabel lblNewPassword = new JLabel("New Password:");
/* 289 */     lblNewPassword.setFont(new Font("Dialog", 1, 14));
/* 290 */     lblNewPassword.setBounds(206, 322, 131, 15);
/* 291 */     passwordChange.add(lblNewPassword);
/*     */     
/* 293 */     this.textField_2 = new JTextField();
/* 294 */     this.textField_2.setBounds(355, 308, 263, 29);
/* 295 */     passwordChange.add(this.textField_2);
/* 296 */     this.textField_2.setColumns(10);
/*     */     
/* 298 */     JButton pwChangeButton = new JButton("Change");
/* 299 */     pwChangeButton.setBounds(575, 349, 114, 25);
/* 300 */     passwordChange.add(pwChangeButton);
/*     */     
/* 302 */     about.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 305 */             String response = ClientGuiTest.this.invoker.about();
/* 306 */             textPane.setText(response);
/*     */           }
/*     */         });
/*     */     
/* 310 */     contact.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 313 */             String response = ClientGuiTest.this.invoker.contact();
/* 314 */             textPane.setText(response);
/*     */           }
/*     */         });
/*     */     
/* 318 */     btnClear.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 321 */             textPane.setText("");
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 326 */     ping.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 329 */             String response = "";
/*     */             try {
/* 331 */               response = ClientGuiTest.this.invoker.ping();
/* 332 */             } catch (MessageBuildException|htb.fatty.shared.message.MessageParseException e1) {
/* 333 */               JOptionPane.showMessageDialog(controlPanel, "Failure during message building/parsing.", "Error", 0);
/*     */ 
/*     */             
/*     */             }
/* 337 */             catch (IOException e2) {
/* 338 */               JOptionPane.showMessageDialog(controlPanel, "Unable to contact the server. If this problem remains, please close and reopen the client.", "Error", 0);
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/* 343 */             textPane.setText(response);
/*     */           }
/*     */         });
/*     */     
/* 347 */     whoami.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 350 */             String response = "";
/*     */             try {
/* 352 */               response = ClientGuiTest.this.invoker.whoami();
/* 353 */             } catch (MessageBuildException|htb.fatty.shared.message.MessageParseException e1) {
/* 354 */               JOptionPane.showMessageDialog(controlPanel, "Failure during message building/parsing.", "Error", 0);
/*     */ 
/*     */             
/*     */             }
/* 358 */             catch (IOException e2) {
/* 359 */               JOptionPane.showMessageDialog(controlPanel, "Unable to contact the server. If this problem remains, please close and reopen the client.", "Error", 0);
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/* 364 */             textPane.setText(response);
/*     */           }
/*     */         });
/*     */     
/* 368 */     configs.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 371 */             String response = "";
/* 372 */             ClientGuiTest.this.currentFolder = "configs";
/*     */             try {
/* 374 */               response = ClientGuiTest.this.invoker.showFiles("configs");
/* 375 */             } catch (MessageBuildException|htb.fatty.shared.message.MessageParseException e1) {
/* 376 */               JOptionPane.showMessageDialog(controlPanel, "Failure during message building/parsing.", "Error", 0);
/*     */ 
/*     */             
/*     */             }
/* 380 */             catch (IOException e2) {
/* 381 */               JOptionPane.showMessageDialog(controlPanel, "Unable to contact the server. If this problem remains, please close and reopen the client.", "Error", 0);
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/* 386 */             textPane.setText(response);
/*     */           }
/*     */         });
/*     */     
/* 390 */     notes.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 393 */             String response = "";
/* 394 */             ClientGuiTest.this.currentFolder = "notes";
/*     */             try {
/* 396 */               response = ClientGuiTest.this.invoker.showFiles("notes");
/* 397 */             } catch (MessageBuildException|htb.fatty.shared.message.MessageParseException e1) {
/* 398 */               JOptionPane.showMessageDialog(controlPanel, "Failure during message building/parsing.", "Error", 0);
/*     */ 
/*     */             
/*     */             }
/* 402 */             catch (IOException e2) {
/* 403 */               JOptionPane.showMessageDialog(controlPanel, "Unable to contact the server. If this problem remains, please close and reopen the client.", "Error", 0);
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/* 408 */             textPane.setText(response);
/*     */           }
/*     */         });
/*     */     
/* 412 */     mail.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 415 */             String response = "";
/* 416 */             ClientGuiTest.this.currentFolder = "mail";
/*     */             try {
/* 418 */               response = ClientGuiTest.this.invoker.showFiles("mail");
/* 419 */             } catch (MessageBuildException|htb.fatty.shared.message.MessageParseException e1) {
/* 420 */               JOptionPane.showMessageDialog(controlPanel, "Failure during message building/parsing.", "Error", 0);
/*     */ 
/*     */             
/*     */             }
/* 424 */             catch (IOException e2) {
/* 425 */               JOptionPane.showMessageDialog(controlPanel, "Unable to contact the server. If this problem remains, please close and reopen the client.", "Error", 0);
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/* 430 */             textPane.setText(response);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 435 */     openFileButton.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 438 */             if (ClientGuiTest.this.currentFolder == null) {
/* 439 */               JOptionPane.showMessageDialog(controlPanel, "No folder selected! List a directory first!", "Error", 0);
/*     */ 
/*     */               
/*     */               return;
/*     */             } 
/*     */             
/* 445 */             String response = "";
/* 446 */             String fileName = ClientGuiTest.this.fileTextField.getText();
/* 447 */             fileName.replaceAll("[^a-zA-Z0-9.]", "");
/*     */             try {
/* 449 */               response = ClientGuiTest.this.invoker.open(ClientGuiTest.this.currentFolder, fileName);
/* 450 */             } catch (MessageBuildException|htb.fatty.shared.message.MessageParseException e1) {
/* 451 */               JOptionPane.showMessageDialog(controlPanel, "Failure during message building/parsing.", "Error", 0);
/*     */ 
/*     */             
/*     */             }
/* 455 */             catch (IOException e2) {
/* 456 */               JOptionPane.showMessageDialog(controlPanel, "Unable to contact the server. If this problem remains, please close and reopen the client.", "Error", 0);
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/* 461 */             textPane.setText(response);
/*     */           }
/*     */         });
/*     */     
/* 465 */     uname.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 468 */             String response = "";
/*     */             try {
/* 470 */               response = ClientGuiTest.this.invoker.uname();
/* 471 */             } catch (MessageBuildException|htb.fatty.shared.message.MessageParseException e1) {
/* 472 */               JOptionPane.showMessageDialog(controlPanel, "Failure during message building/parsing.", "Error", 0);
/*     */ 
/*     */             
/*     */             }
/* 476 */             catch (IOException e2) {
/* 477 */               JOptionPane.showMessageDialog(controlPanel, "Unable to contact the server. If this problem remains, please close and reopen the client.", "Error", 0);
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/* 482 */             textPane.setText(response);
/*     */           }
/*     */         });
/*     */     
/* 486 */     users.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 489 */             String response = "";
/*     */             try {
/* 491 */               response = ClientGuiTest.this.invoker.users();
/* 492 */             } catch (MessageBuildException|htb.fatty.shared.message.MessageParseException e1) {
/* 493 */               JOptionPane.showMessageDialog(controlPanel, "Failure during message building/parsing.", "Error", 0);
/*     */ 
/*     */             
/*     */             }
/* 497 */             catch (IOException e2) {
/* 498 */               JOptionPane.showMessageDialog(controlPanel, "Unable to contact the server. If this problem remains, please close and reopen the client.", "Error", 0);
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/* 503 */             textPane.setText(response);
/*     */           }
/*     */         });
/*     */     
/* 507 */     ipconfig.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 510 */             String response = "";
/*     */             try {
/* 512 */               response = ClientGuiTest.this.invoker.ipconfig();
/* 513 */             } catch (MessageBuildException|htb.fatty.shared.message.MessageParseException e1) {
/* 514 */               JOptionPane.showMessageDialog(controlPanel, "Failure during message building/parsing.", "Error", 0);
/*     */ 
/*     */             
/*     */             }
/* 518 */             catch (IOException e2) {
/* 519 */               JOptionPane.showMessageDialog(controlPanel, "Unable to contact the server. If this problem remains, please close and reopen the client.", "Error", 0);
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/* 524 */             textPane.setText(response);
/*     */           }
/*     */         });
/*     */     
/* 528 */     netstat.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 531 */             String response = "";
/*     */             try {
/* 533 */               response = ClientGuiTest.this.invoker.netstat();
/* 534 */             } catch (MessageBuildException|htb.fatty.shared.message.MessageParseException e1) {
/* 535 */               JOptionPane.showMessageDialog(controlPanel, "Failure during message building/parsing.", "Error", 0);
/*     */ 
/*     */             
/*     */             }
/* 539 */             catch (IOException e2) {
/* 540 */               JOptionPane.showMessageDialog(controlPanel, "Unable to contact the server. If this problem remains, please close and reopen the client.", "Error", 0);
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/* 545 */             textPane.setText(response);
/*     */           }
/*     */         });
/*     */     
/* 549 */     changePassword.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 552 */             controlPanel.setVisible(false);
/* 553 */             passwordChange.setVisible(true);
/*     */           }
/*     */         });
/*     */     
/* 557 */     pwChangeButton.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 560 */             JOptionPane.showMessageDialog(passwordChange, "Not implemented yet.", "Error", 0);
/*     */ 
/*     */ 
/*     */             
/* 564 */             passwordChange.setVisible(false);
/* 565 */             controlPanel.setVisible(true);
/*     */           }
/*     */         });
/*     */     
/* 569 */     addWindowListener(new WindowAdapter()
/*     */         {
/*     */           
/*     */           public void windowClosing(WindowEvent e)
/*     */           {
/* 574 */             System.out.println("Closed");
/* 575 */             if (ClientGuiTest.this.conn != null) {
/* 576 */               ClientGuiTest.this.conn.logoff();
/* 577 */               ClientGuiTest.this.conn.close();
/*     */             } 
/* 579 */             e.getWindow().dispose();
/* 580 */             System.exit(0);
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/htb/fatty/client/gui/ClientGuiTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */