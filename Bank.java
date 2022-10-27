package simple_bank_app;
import java.sql.*;
import java.util.Scanner;

public class Bank {
	static Scanner sc=new Scanner(System.in);
	static Connection con = connection.getConnection();
	static String sql = "";
	public static boolean createAccount(String cname, int pass_code) throws SQLException // create account function
    {
        try {
            // query
            Statement st = con.createStatement();
            sql = "INSERT INTO customer(cname,balance,pass_code) values('"+ cname + "', "+ 1000 + "," + pass_code + ")";
 
            // Execution
            if (st.executeUpdate(sql) == 1) {
                System.out.println(cname+ ",✔ : Account Created Successfully!");
                return true;
            }
            // return
        }
        catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("⚠: Account Creation Failed!");
            
        }
       
        return false;
    }
	public static boolean loginAccount(String name, int passCode) // login method
    {
        try {
            
            // query
            sql = "select * from customer where cname='"+ name + "' and pass_code=" + passCode;
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            // Execution
        
            if (rs.next()) {
                
                int ch;
                int amt = 0;
                int senderAc = rs.getInt("ac_no");
                int receiveAc;
                String receiveName;
                while (true) {
                    try {
                        System.out.println("\nHello, "+ rs.getString("cname"));
                        System.out.println("\n1)Transfer Money");
                        System.out.println("2)View Balance");
                        System.out.println("5)LogOut");
 
                        System.out.print("\n Enter Choice:");
                        ch=sc.nextInt();
                        if (ch == 1) {
                            System.out.print("Enter Receiver  A/c No:");
                            receiveAc=sc.nextInt();
                            System.out.print("Enter Receiver Name:");
                            receiveName=sc.next();
                            sql="select * from customer where ac_no="+receiveAc+" and cname='"+receiveName+"'";
                            PreparedStatement sst=con.prepareStatement(sql);
                            ResultSet rrs=sst.executeQuery();
                            if(rrs.next()) {
                            System.out.print("Enter Amount:");
                            amt=sc.nextInt();
                            if (transferMoney(senderAc, receiveAc,amt)) {
                                System.out.println("✔ : Money Sent Successfully!\n");
                            }
                            else {
                                System.out.println("⚠ :  Failed!\n");
                            }
                            }
                            else
                            	System.out.println("Please enter Correct Account number and Name");
                        }
                        else if (ch == 2) {
 
                          getBalance(senderAc);
                        }
                        else if (ch == 5) {
                            break;
                        }
                        else {
                            System.out.println("Err : Enter Valid input!\n");
                        }
                    }
                    catch (Exception e) {
                    	System.out.println("Please enter correct input/data");
                    	sc.next();
                    continue;
                    }
                }
              
            }
            else {
            	
                return false;
            }
           
            // return
            return true;
            
        }
        catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Username Not Available!");
        }
        catch (Exception e) {
            e.printStackTrace();
            sc.next();
        }
        return false;
    }
	public static void getBalance(int acNo) // fetch balance method
    {
        try {
 
            // query
            sql = "select * from customer where ac_no="+ acNo;
            PreparedStatement st = con.prepareStatement(sql);
 
            ResultSet rs = st.executeQuery(sql);
            System.out.println(
                "-----------------------------------------------------------");
            System.out.printf("%12s %10s %10s\n","Account No", "Name","Balance");
 
            // Execution
 
            while (rs.next()) {
                System.out.printf("%12d %10s %10d.00\n",
                                  rs.getInt("ac_no"),
                                  rs.getString("cname"),
                                  rs.getInt("balance"));
            }
            System.out.println(
                "-----------------------------------------------------------\n");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
	public static boolean transferMoney(int sender_ac,int reveiver_ac,int amount)throws SQLException // transfer money method
    {
      
        try {
            con.setAutoCommit(false );
            sql = "select * from customer where ac_no="+ sender_ac;
            PreparedStatement ps= con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
 
            if (rs.next()) {
                if (rs.getInt("balance") < amount) {
                    System.out.println("Insufficient Balance!");
                    return false;
                }
            }
 
            Statement st = con.createStatement();
 
            // debit
            con.setSavepoint();
 
            sql = "update customer set balance=balance-"+ amount + " where ac_no=" + sender_ac;
            if (st.executeUpdate(sql) == 1) {
                System.out.println("Amount Debited!");
            }
 
            // credit
            sql = "update customer set balance=balance+"+ amount + " where ac_no=" + reveiver_ac;
            st.executeUpdate(sql);
 
            con.commit();
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            con.rollback();
        }
        // return
        return false;
    
    }
    

	public static void main(String[] args){
		
        String cname = "";
        int pass_code;
        int ch;
 
        while (true) {
        	System.out.println("==================");
            System.out.println("1)Create Account");
            System.out.println("2)Login Account");
 
                try {
                System.out.print("\nEnter Input:"); //user input
                ch=sc.nextInt();
                
                
 
                switch (ch) {
                case 1:
                    try {
                        System.out.print("\nEnter Unique UserName:");
                        cname=sc.next();
                        System.out.print("Enter New Password:");
                        pass_code= sc.nextInt();
                        if (createAccount(cname, pass_code)) {
                            System.out.println(" Now You Login!\n");
                        }
                        else {
                            System.out.println("That username is taken, Try other\n");
                        }
                    }
                    catch (Exception e) {
                        System.out.println("⚠ : Please Enter Valid Data, Insertion Failed!\n");
                        sc.next();
                    }
                    break;
 
                case 2:
                    try {
                        System.out.print("\nEnter  UserName:");
                       cname=sc.next();
                        System.out.print("Enter  Password:");
                        pass_code=sc.nextInt();
                        if (loginAccount(cname, pass_code)) {
                            System.out.println("✔: Logout Successfully!\n");
                        }
                        else {
                            System.out.println("⚠ : login Failed!\n");
                        }
                    }
                    catch (Exception e) {
                        System.out.println("⚠: Enter Valid Data::Login Failed!\n");
                        sc.next();
                    }
                    break;
 
                default:
                    System.out.println("⚠: Invalid Entry!\n");
                }
                }
                catch(Exception e) {
                	System.out.println("⚠: Please Enter Valid input");
                	sc.next();}
               // sc.close();
        }
       
}

}
