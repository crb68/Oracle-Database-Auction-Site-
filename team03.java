import java.sql.*;  //import the file containing definitions for the parts
                    //needed by java for database connection and manipulation
import java.util.Scanner;
import java.util.StringTokenizer;

public class team03
{
  private Connection connection; //used to hold the jdbc connection to the DB
  private Statement statement; //used to create an instance of the connection
  private ResultSet resultSet; //used to hold the result of your query (if one
                               // exists)
  private String query;  //this will hold the query we are using
  private String username, password;

  public boolean connect()
  {
  	boolean connect = true;
    username = "crb68"; //This is your username in oracle
    password = "3653619"; //This is your password in oracle
    try{
     
      DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
     
      String url = "jdbc:oracle:thin:@db10.cs.pitt.edu:1521:dbclass"; 
      
      connection = DriverManager.getConnection(url, username, password); 
      //create a connection to DB on db10.cs.pitt.edu
    }
    catch(Exception Ex) 
    {
      System.out.println("Error connecting to database.  Machine Error: " + Ex.toString());
		Ex.printStackTrace();
		connect = false;
    }
    return connect;
  }
  
  public boolean disconnect(){
  	boolean disconnect = true;
  	try{
  		connection.close();
  	}catch(Exception Ex){
  		System.out.println("Error disconnecting from database.  Machine Error: " + Ex.toString());
		Ex.printStackTrace();
		disconnect = false;
  	}
  	return disconnect;
  }
  
  
  public String find_user_pwd(String userName){
  	try{
  	  query = "Select password from Customer where login = ?";
  	  PreparedStatement find_pwd = connection.prepareStatement(query);
      find_pwd.setString(1, userName);
    	resultSet = find_pwd.executeQuery();
    	if(resultSet.next()){
    		return resultSet.getString(1);
    	}
    }catch(Exception Ex){
  		System.out.println("Error find user" + Ex.toString());
		Ex.printStackTrace();
  	}
  	return null;
  }
  
  public void display_root_categories(){
  	try{
  	  query = "select name from category where lower(parent_category) is null";
  	  PreparedStatement root_cat = connection.prepareStatement(query);
    	resultSet = root_cat.executeQuery();
    	int counter = 1;
		 while(resultSet.next()) {
        	System.out.println("Category " + counter+ ": " +
             resultSet.getString(1));
	  		counter ++;
      	 }
    }catch(Exception Ex){
  		System.out.println("Error display Root Category" + Ex.toString());
		Ex.printStackTrace();
  	}
  }
  
    public int display_categories(String parent_category){
    int counter = 1;
  	try{
  	  query = "select name from category where lower(parent_category) = ?";
  	  PreparedStatement cat_search = connection.prepareStatement(query);
  	  	cat_search.setString(1, parent_category);
    	resultSet = cat_search.executeQuery();
		 while(resultSet.next()) {
        	System.out.println("Category " + counter + ": " +
             resultSet.getString(1));
	  		counter ++;
      	 }
    }catch(Exception Ex){
  		System.out.println("Error display Category" + Ex.toString());
		Ex.printStackTrace();
  	}
  	return counter-1;
  }
  
  
    public int display_products_maxbid(String category){
    int counter = 1;
  	try{
  	  query = "select p.auction_id,p.name,p.description,p.amount as Highest_Bid, p.min_price"
		+ " from belongsto b join product p on b.auction_id = p.auction_id"
		+ " where b.category = ?"
		+ " order by case when p.amount is null then 1 else 0 end, p.amount desc";
  	  
  	  PreparedStatement prod_dis = connection.prepareStatement(query);
  	  	prod_dis.setString(1, category);
    	resultSet = prod_dis.executeQuery();
		 while(resultSet.next()) {
        	System.out.println("Product " + counter + " :\n" +
             "	Auction ID: "+ resultSet.getInt(1) + ",\n" +
             "	Name: "+ resultSet.getString(2) + ",\n" +
             "	Description: "+resultSet.getString(3) + ",\n" +
             "	Highest Bid: "+resultSet.getInt(4) + ",\n" +
             "	Min Price: "+ resultSet.getInt(5)+"\n\n");
	  		counter ++;
      	 }
    }catch(Exception Ex){
  		System.out.println("Error display Product MaxBid" + Ex.toString());
		Ex.printStackTrace();
  	}
  	return counter-1;
  }
  
    public int display_products_alph(String category){
    int counter = 1;
  	try{
  	  query = "select p.auction_id,p.name,p.description,p.amount as Highest_Bid, p.min_price"
		+ " from belongsto b join product p on b.auction_id = p.auction_id"
		+ " where b.category = ?"
		+ " order by p.name";
  	  
  	  PreparedStatement prod_dis = connection.prepareStatement(query);
  	  	prod_dis.setString(1, category);
    	resultSet = prod_dis.executeQuery();
		 while(resultSet.next()) {
        	System.out.println("Product " + counter + " :\n" +
             "	Auction ID: "+ resultSet.getInt(1) + ",\n" +
             "	Name: "+ resultSet.getString(2) + ",\n" +
             "	Description: "+resultSet.getString(3) + ",\n" +
             "	Highest Bid: "+resultSet.getInt(4) + ",\n" +
             "	Min Price: "+ resultSet.getInt(5)+"\n\n");
	  		counter ++;
      	 }
    }catch(Exception Ex){
  		System.out.println("Error display Product MaxBid" + Ex.toString());
		Ex.printStackTrace();
  	}
  	return counter-1;
  }
  
    public int keyword_search(String kw[], int size){
    int counter = 1;
  	try{
  	  query = "select * from PRODUCT where lower(description) like ?";
  	  if(size == 2){
  	  	query += "and lower(description) like ?";
  	  }
  	  
  	  PreparedStatement kw_search = connection.prepareStatement(query);
  	  	kw_search.setString(1, "%"+kw[0]+"%");
  	  	if(size == 2){
  	  		kw_search.setString(2, "%" + kw[1]+ "%");
  	  	}
    	resultSet = kw_search.executeQuery();
		 while(resultSet.next()) {
        	System.out.println("Product " + counter + " :\n" +
             "	Auction ID: "+ resultSet.getInt(1) + ",\n" +
             "	Name: "+ resultSet.getString(2) + ",\n" +
             "	Description: "+resultSet.getString(3) + ",\n" +
             "	Seller: "+ resultSet.getString(4) + ",\n" +
             "	Start Date: "+ resultSet.getDate(5) + ",\n" +
             "	Min Price: "+ resultSet.getInt(6)+"\n"+
             "	Number of Days: "+resultSet.getInt(7) + ",\n" +
             "	Status: "+resultSet.getString(8) + ",\n" +
             "	Buyer: "+resultSet.getString(9) + ",\n" +
             "	Sell Date: "+ resultSet.getDate(10) + ",\n" +
             "	Amount: "+ resultSet.getInt(11) + ",\n\n");
	  		counter ++;
      	 }
    }catch(Exception Ex){
  		System.out.println("Error keyword search" + Ex.toString());
		Ex.printStackTrace();
  	}
  	return counter-1;
  }
  
    public boolean put_up_for_auction(String name, String des, String user, int min,int days,String cat){
  	try{
  	  query = "call PUT_PRODUCT(?,?,?,?,?,?)";
  	 
  	  PreparedStatement put = connection.prepareStatement(query);
  	  	put.setString(1, name);
  	  	put.setString(2, des);
  	  	put.setString(3, user);
  	  	put.setInt(4, min);
  	  	put.setInt(5, days);
  	  	put.setString(6, cat);

    	put.executeUpdate();
		 
    }catch(Exception Ex){
  		//System.out.println("Error product auction" + Ex.toString());
		//Ex.printStackTrace();
		return false;
  	}
  	return true;
  }
  
    public int max_amount(int auction_id){

  	try{
  	  query = "select max(amount) from bidlog where auction_id = ?";
  	  
  	  PreparedStatement kw_search = connection.prepareStatement(query);
  	  	kw_search.setInt(1,auction_id);
    	resultSet = kw_search.executeQuery();
		if(resultSet.next()){
    		return resultSet.getInt(1);
    	}
		
    }catch(Exception Ex){
  		System.out.println("Error max amout" + Ex.toString());
		Ex.printStackTrace();
  	}
	return 0;
  }

    public String find_status(int auction_id){

  	try{
  	  query = "select status from Product where auction_id = ?";
  	    PreparedStatement ps = connection.prepareStatement(query);
  	    ps.setInt(1,auction_id);
    	resultSet = ps.executeQuery();
		if(resultSet.next()){
    		return resultSet.getString(1);
    	}

    }catch(Exception Ex){
  		System.out.println("Error find status" + Ex.toString());
		Ex.printStackTrace();
  	}
	return null;
  }


  
    public boolean insert_bid(int auction_id,int amount,String user){

  	try{
  	  query = "select max(bidsn) from bidlog";
  	    PreparedStatement ps = connection.prepareStatement(query);
    	resultSet = ps.executeQuery();
    	int bid_sn = 0;
		if(resultSet.next()){
    		bid_sn = resultSet.getInt(1) +1;
    	}
    	
        query = "select * from OURDATE";
		ps = connection.prepareStatement(query);
    	resultSet = ps.executeQuery();
    	java.sql.Date current_date = null;
		if(resultSet.next()){
    		current_date = resultSet.getDate(1);
    	}
    	
    	query = "insert into bidlog values (?,?,?,?,?)";
		ps = connection.prepareStatement(query);
  	    ps.setInt(1,bid_sn);
  	    ps.setInt(2,auction_id);
  	    ps.setString(3,user);
  	    ps.setDate(4,current_date);
  	    ps.setInt(5,amount);
		ps.executeUpdate();

		return true;

    }catch(Exception Ex){
  		System.out.println("Error max amout" + Ex.toString());
		Ex.printStackTrace();
  	}
	return false;
  }
  
    public int suggest(String user){
    int counter = 1;
  	try{
        query = "select p.auction_id,p.name,p.description from product p join "+
        "(select b.auction_id, count(Distinct b.bidder) as desirability "+
        "from bidlog b join product p on b.auction_id = p.auction_id "+
        "where p.status = 'underauction' and b.bidder in (select Distinct bidder from bidlog where auction_id in "+
        "(select auction_id "+
         "from bidlog "+
         "where bidder = ?) and bidder != ?) "+
        "group by b.auction_id)  t2 on p.auction_id = t2.auction_id order by desirability desc";
  	  
  	    PreparedStatement ps = connection.prepareStatement(query);
  	    ps.setString(1,user);
  	    ps.setString(2,user);
    	resultSet = ps.executeQuery();
		while(resultSet.next()) {
			System.out.println("Rank " + counter + " :\n" +
			"	Auction ID: "+ resultSet.getInt(1) + ",\n" +
             "	Name: "+ resultSet.getString(2) + ",\n" +
             "	Description: "+ resultSet.getString(3) + "\n\n");
	  		counter ++;
      	 }

    }catch(Exception Ex){
  		System.out.println("Error Suggest" + Ex.toString());
		Ex.printStackTrace();
  	}
	return counter - 1;
  }
  
    public int show_user_products_underauction(String user){
    int counter = 1;
  	try{
  	  query = "select auction_id,name,description from product where seller = ? and status = 'underauction'";
  	    PreparedStatement ps = connection.prepareStatement(query);
  	    ps.setString(1,user);
    	resultSet = ps.executeQuery();
		while(resultSet.next()) {
			System.out.println("Number " + counter + " :\n" +
			"	Auction ID: "+ resultSet.getInt(1) + ",\n" +
             "	Name: "+ resultSet.getString(2) + ",\n" +
             "	Description: "+ resultSet.getString(3) + "\n");
	  		counter ++;
      	 }

    }catch(Exception Ex){
  		System.out.println("Error Suggest" + Ex.toString());
		Ex.printStackTrace();
  	}
	return counter - 1;
  }
  
  public boolean terminate_auction(String user,int auction_id){
  	Scanner sc = new Scanner(System.in);
  	try{
  	  query = "select * from product where seller = ? and auction_id = ? and status = 'underauction'";
  	    PreparedStatement ps = connection.prepareStatement(query);
  	    ps.setString(1,user);
  	    ps.setInt(2,auction_id);
    	resultSet = ps.executeQuery();
    	if(!resultSet.next()){
    		return false;
    	}
    	query = "select count(bidsn) from bidlog where auction_id = ?";
    	ps = connection.prepareStatement(query);
  	    ps.setInt(1,auction_id);
    	resultSet = ps.executeQuery();
    	resultSet.next();
    	int count = resultSet.getInt(1);
    	if(count == 0){
    		System.out.print("\n\n Zero bids on auction. Do you still want to terminate auction?(y/n)\n\n");
    		String input = sc.nextLine();
    		if(input.equals("y")){
    			//withdraw
    			query = "update product set status = 'withdrawn', sell_date = null,buyer = null,amount = null where auction_id = ?";
    			ps = connection.prepareStatement(query);
    			ps.setInt(1,auction_id);
    			ps.executeUpdate();
    			System.out.print("\n\n Product Withdrawn\n\n");
    		}else{
    			System.out.print("\n\n No Changes made\n\n");
    		}
    	}else if(count == 1){
    		query = "select amount from bidlog where auction_id = ?";
    		ps = connection.prepareStatement(query);
    			ps.setInt(1,auction_id);
    			resultSet = ps.executeQuery();
    			resultSet.next();
    			int second_highest = resultSet.getInt(1);
    			System.out.print("\n\n Second Highest Bid:" + second_highest + "\n\n");
    			System.out.print("\n\n Do you want to Sell or Withdraw the product?(S/W)\n\n");
    			String input = sc.nextLine();
    			if(input.equalsIgnoreCase("s")){
    				query = "update product set status = 'sold', sell_date = (select * from ourdate), amount = (select amount from bidlog where auction_id = ?), " +
					"buyer = (select bidder from bidlog where auction_id = ?) " +
					"where auction_id = ?";
					ps = connection.prepareStatement(query);
    				ps.setInt(1,auction_id);
    				ps.setInt(2,auction_id);
    				ps.setInt(3,auction_id);
    				ps.executeUpdate();
    				System.out.print("\n\n Product Sold\n\n");
    			}else if(input.equals("w")){
    				query = "update product set status = 'withdrawn', sell_date = null,buyer = null,amount = null where auction_id = ?";
    				ps = connection.prepareStatement(query);
    				ps.setInt(1,auction_id);
    				ps.executeUpdate();
    				System.out.print("\n\n Product Withdrawn\n\n");
    			}
    	}else if(count > 1){
    		query = "select amount from (" +
										"select amount,rownum as snum "+
										"from (select * " +
												"from bidlog "+
												"where auction_id = ? " +
												"order by amount desc)" +
										"where rownum <=2) "+
					"where snum > 1";
			ps = connection.prepareStatement(query);
    			ps.setInt(1,auction_id);
    			resultSet = ps.executeQuery();
    			resultSet.next();
    			int second_highest = resultSet.getInt(1);
    			System.out.print("\n\n Second Highest Bid:" + second_highest + "\n\n");
    			System.out.print("\n\n Do you want to Sell or Withdraw the product?(S/W)\n\n");
    			String input = sc.nextLine();
    			if(input.equalsIgnoreCase("s")){
    				query = "update product set status = 'sold', sell_date = (select * from ourdate), amount = (select amount from (" +
							"select amount,rownum as snum from (select * from bidlog where auction_id = ? "+
							"order by amount desc) where rownum <=2) where snum > 1), buyer = (select bidder from ( "+
							"select bidder,rownum as snum from (select * from bidlog where auction_id = ? "+
							"order by amount desc) where rownum <=2) where snum > 1) where auction_id = ?";
					ps = connection.prepareStatement(query);
    				ps.setInt(1,auction_id);
    				ps.setInt(2,auction_id);
    				ps.setInt(3,auction_id);
    				ps.executeUpdate();
    				System.out.print("\n\n Product Sold\n\n");
    			}else if(input.equals("w")){
    			    query = "update product set status = 'withdrawn', sell_date = null,buyer = null,amount = null where auction_id = ?";
    				ps = connection.prepareStatement(query);
    				ps.setInt(1,auction_id);
    				ps.executeUpdate();
    				System.out.print("\n\n Product Withdrawn\n\n");
    			}			
    	}
		return true;
    }catch(Exception Ex){
  		System.out.println("Error Suggest" + Ex.toString());
		Ex.printStackTrace();
  	}
	return false;
  }
    
    public boolean new_user(String login, String password,String name, String address, String email){
        try{
            query = "Select login from Customer where login = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, login);
            resultSet = ps.executeQuery();
            if(resultSet.next()){
                return false;
            }
            query = "Insert into Customer values(?,?,?,?,?)";
            ps = connection.prepareStatement(query);
            ps.setString(1, login);
            ps.setString(2, password);
            ps.setString(3, name);
            ps.setString(4, address);
            ps.setString(5, email);
            ps.executeUpdate();
            
        }catch(Exception Ex){
            System.out.println("Error find user" + Ex.toString());
            Ex.printStackTrace();
        }
        return true;
    }
 
 public boolean new_admin(String login, String password,String name, String address, String email){
        try{
            query = "Select login from Administrator where login = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, login);
            resultSet = ps.executeQuery();
            if(resultSet.next()){
                return false;
            }
            query = "Insert into Administrator values(?,?,?,?,?)";
            ps = connection.prepareStatement(query);
            ps.setString(1, login);
            ps.setString(2, password);
            ps.setString(3, name);
            ps.setString(4, address);
            ps.setString(5, email);
            ps.executeUpdate();
            
        }catch(Exception Ex){
            System.out.println("Error find Admin" + Ex.toString());
            Ex.printStackTrace();
        }
        return true;
    }
 
  public String find_admin_pwd(String userName){
  	try{
  	  query = "Select password from Administrator where login = ?";
  	  PreparedStatement find_pwd = connection.prepareStatement(query);
      find_pwd.setString(1, userName);
    	resultSet = find_pwd.executeQuery();
    	if(resultSet.next()){
    		return resultSet.getString(1);
    	}
    }catch(Exception Ex){
  		System.out.println("Error find admin" + Ex.toString());
		Ex.printStackTrace();
  	}
  	return null;
  }
  
   public boolean update_sys_time(String input)
   {
     try{
     query = "update Ourdate set C_Date =to_date( ?,'dd-mon-yyyy/hh12:mi:ssAM')";
     PreparedStatement u_sys = connection.prepareStatement(query);
     u_sys.setString(1, input);
     u_sys.executeUpdate();
       }catch(Exception Ex){
  		System.out.println("Error Update Sys Time");
		//Ex.printStackTrace();
		return false;
   }
   return true;
   }
   
    public String[] top_leaf(int month, int num)
   {
     String[] res = new String[num];
     try{
        
         query = "select count(name) as cnt from category";
         PreparedStatement find_c = connection.prepareStatement(query);
         resultSet = find_c.executeQuery();
         resultSet.next();
         int rowcount=resultSet.getInt("cnt");

         query = "select name from category";
         PreparedStatement find_cat = connection.prepareStatement(query);
         resultSet = find_cat.executeQuery();
        
           String[] names = new String[rowcount];
          int i= 0;
           while (resultSet.next()) 
           {
           names[i]=resultSet.getString("name");
           i++;
           }
           int c = names.length;
            
            int[] pcnt = new int[c]; 
             for(i=0;i<c;i++)
             {
            
             query="select product_count(?,\'"+ names[i] +"\') from dual";
             PreparedStatement pc = connection.prepareStatement(query);
             pc.setInt(1, month);
             resultSet = pc.executeQuery();
             resultSet.next();
             pcnt[i]=resultSet.getInt(1);
            
             }//end for
           int[] temp= new int[num];
           String[] temp2 = new String[num];
           int[] index = new int[num];
           int j=0;
           while(j<num)
           {
           int hold=0;
            String hld="";
          int ti=-1;
           for(i=0;i<c;i++)
           {
       
            if(pcnt[i] >= hold)
            {
            int icnt=index.length;
            int flag=0;
              for(int k=0;k<icnt;k++)
              {
               
               if(i==index[k])
               {
               flag=1;
               }
               
              }
                  if(flag==0)
                   {
                   hold=pcnt[i];
                   hld=names[i];
                   ti=i;
                   }
            }//end if
           }// end for i
           index[j]=ti;
           temp[j]=hold;
           temp2[j]=hld;
           j++;
           }//end while
           
             int rnum = temp.length;
                 for(i=0;i<rnum;i++)
                 {
                   res[i]=temp2[i] + "------ " + temp[i];
                 }
        }catch(Exception Ex){
        System.out.println("Error top_leaf" + Ex.toString());
		Ex.printStackTrace();
		String[] fail = new String[1];
		fail[0]="failed";
		return fail;
        }   
          String[] fail = new String[1];
          fail[0]="temp";
       return res;
   } //end top leaf 
   
    public String[] top_root(int month, int num)
   {
   String[] res = new String[num];
     
     try{
        query = "select count(distinct parent_category) as cnt from category where parent_category !='null'";
         
         PreparedStatement find_c = connection.prepareStatement(query);
         resultSet = find_c.executeQuery();
         resultSet.next();
         int rowcount=resultSet.getInt("cnt");
         
         query = "select distinct parent_category from category where parent_category !='null'";
         
         PreparedStatement find_cat = connection.prepareStatement(query);
         resultSet = find_cat.executeQuery();
        
           String[] names = new String[rowcount];
          int i= 0;
           while (resultSet.next()) 
           {
           names[i]=resultSet.getString("parent_category");
           i++;
           }
           int c = names.length;
           int[] pcnt = new int[c];
           int prcnt =0;
           
            for(i=0;i<c;i++)
            {
            query ="select name from category where parent_category=\'"+ names[i] +"\'";
            PreparedStatement fpc = connection.prepareStatement(query);
            resultSet = fpc.executeQuery();
            String[] prnt= new String[30];
          
           while (resultSet.next()) 
           {
           prnt[prcnt]=resultSet.getString("name");
           prcnt++;
           
           }
            int cd = prnt.length;
            
            int tempc=0;
            for(int k=0;k<cd;k++)
             {
            ResultSet rs;
             query="select product_count(?,\'"+ prnt[k] +"\') from dual";
             PreparedStatement pc = connection.prepareStatement(query);
             pc.setInt(1, month);
             rs = pc.executeQuery();
             rs.next();
             tempc= tempc + rs.getInt(1);
              //System.out.print("\n here");
              pc.close();
              rs.close();
             }//end for  k   
            pcnt[i]=tempc;
            //System.out.print("\n"+ names[i] + " " + tempc);
            }//end for i
            
             
             int[] temp= new int[num];
           String[] temp2 = new String[num];
           int[] index = new int[num];
           for(i=0;i<num;i++)
           {
           index[i]=-1;
           }
           
           int j=0;
           while(j<num)
           {
           int hold=0;
            String hld="";
          int ti=-1;
           for(i=0;i<c;i++)
           {
       
            if(pcnt[i] >= hold)
            {
            int icnt=index.length;
            
            int flag=0;
              for(int k=0;k<icnt;k++)
              {
                
               if(i==index[k])
               {
                
               flag=1;
               }
               
              }//end k
                  if(flag==0)
                   {
                   
                   hold=pcnt[i];
                   hld=names[i];
                
                   ti=i;
                   }
            }//end if
           }// end for i
           index[j]=ti;
           temp[j]=hold;
           temp2[j]=hld;
        
           j++;
           }//end while
           
             int rnum = temp.length;
                 for(i=0;i<rnum;i++)
                 {
                   res[i]=temp2[i] + "------ " + temp[i];
                 }
            
         
        }catch(Exception Ex){
        System.out.println("Error top_root" + Ex.toString());
		Ex.printStackTrace();
		String[] fail = new String[1];
		fail[0]="failed";
		return fail;
        }   
       
          return res;
   }
    
     public String[] top_bid(int month, int num)
   {
   String[] res = new String[num];
     
     try{
         query = "select count(login) as cnt from customer";
         
         PreparedStatement find_c = connection.prepareStatement(query);
         resultSet = find_c.executeQuery();
         resultSet.next();
         int rowcount=resultSet.getInt("cnt");
         
         query = "select login from customer";
         
         PreparedStatement find_cat = connection.prepareStatement(query);
         resultSet = find_cat.executeQuery();
        
           String[] names = new String[rowcount];
          int i= 0;
           while (resultSet.next()) 
           {
           names[i]=resultSet.getString("login");
           i++;
           }
           int c = names.length;
           int[] pcnt = new int[c];
           int prcnt =0;
           
             for(i=0;i<c;i++)
             {
            
             query="select bid_count(?,\'"+ names[i] +"\') from dual";
             PreparedStatement pc = connection.prepareStatement(query);
             pc.setInt(1, month);
             resultSet = pc.executeQuery();
             resultSet.next();
             pcnt[i]=resultSet.getInt(1);
            
             }//end for
             
            int[] temp= new int[num];
           String[] temp2 = new String[num];
           int[] index = new int[num];
           int j=0;
           while(j<num)
           {
           int hold=0;
            String hld="";
          int ti=-1;
           for(i=0;i<c;i++)
           {
       
            if(pcnt[i] >= hold)
            {
            int icnt=index.length;
            int flag=0;
              for(int k=0;k<icnt;k++)
              {
               
               if(i==index[k])
               {
               flag=1;
               }
               
              }
                  if(flag==0)
                   {
                   hold=pcnt[i];
                   hld=names[i];
                   ti=i;
                   }
            }//end if
           }// end for i
           index[j]=ti;
           temp[j]=hold;
           temp2[j]=hld;
           j++;
           }//end while
           
             int rnum = temp.length;
                 for(i=0;i<rnum;i++)
                 {
                   res[i]=temp2[i] + "------ " + temp[i];
                 } 
             
     
        }catch(Exception Ex){
        System.out.println("Error top_root" + Ex.toString());
		Ex.printStackTrace();
		String[] fail = new String[1];
		fail[0]="failed";
		return fail;
        }
        return res;
     }//end top bid      
    
    
    public String[] top_buy(int month, int num)
   {
   String[] res = new String[num];
     
     try{
         query = "select count(login) as cnt from customer";
         
         PreparedStatement find_c = connection.prepareStatement(query);
         resultSet = find_c.executeQuery();
         resultSet.next();
         int rowcount=resultSet.getInt("cnt");
         
         query = "select login from customer";
         
         PreparedStatement find_cat = connection.prepareStatement(query);
         resultSet = find_cat.executeQuery();
        
           String[] names = new String[rowcount];
          int i= 0;
           while (resultSet.next()) 
           {
           names[i]=resultSet.getString("login");
           i++;
           }
           int c = names.length;
           int[] pcnt = new int[c];
           int prcnt =0;
           
             for(i=0;i<c;i++)
             {
            
             query="select buying_amount(?,\'"+ names[i] +"\') from dual";
             PreparedStatement pc = connection.prepareStatement(query);
             pc.setInt(1, month);
             resultSet = pc.executeQuery();
             resultSet.next();
             pcnt[i]=resultSet.getInt(1);
            
             }//end for
             
            int[] temp= new int[num];
           String[] temp2 = new String[num];
           int[] index = new int[num];
           int j=0;
           while(j<num)
           {
           int hold=0;
            String hld="";
          int ti=-1;
           for(i=0;i<c;i++)
           {
       
            if(pcnt[i] >= hold)
            {
            int icnt=index.length;
            int flag=0;
              for(int k=0;k<icnt;k++)
              {
               
               if(i==index[k])
               {
               flag=1;
               }
               
              }
                  if(flag==0)
                   {
                   hold=pcnt[i];
                   hld=names[i];
                   ti=i;
                   }
            }//end if
           }// end for i
           index[j]=ti;
           temp[j]=hold;
           temp2[j]=hld;
           j++;
           }//end while
           
             int rnum = temp.length;
                 for(i=0;i<rnum;i++)
                 {
                   res[i]=temp2[i] + "------ " + temp[i];
                 } 
             
     
        }catch(Exception Ex){
        System.out.println("Error top_root" + Ex.toString());
		Ex.printStackTrace();
		String[] fail = new String[1];
		fail[0]="failed";
		return fail;
        }
        return res;
     }//end top buy      
    
  public static void main(String args[])
  {
    	team03 db = new team03();
    	if(db.connect()){
    		System.out.print("\nConnection Sucessful\n\n");
    	}else{
    		System.exit(0);
    	}
    	
    	Scanner sc = new Scanner(System.in);
    	boolean exit = false;
    	int input;
		while(!exit){	//main program loop
			System.out.print("           MyAuction\n====== Enter a selection ======\n"
			+ "1. User Login\n"
			+ "2. Administrator Login\n"
			+ "3. Exit\n\n");
			//input = sc.nextInt();
            while(true){
                try{
                    input = sc.nextInt();
                    break;
                }catch(Exception Ex){
                    sc.nextLine();
                    System.out.print("\nEnter a number\n");
                }
            }//end while true
			switch(input){//user switch
				case 1:
						sc.nextLine();
						System.out.print("\n-->Enter User Name\n");
						String user_input = sc.nextLine();
						String username = user_input;
						String user_password = db.find_user_pwd(user_input);
						if(user_password == null){
							System.out.print("\n\n	*User Name Not Found*\n\n");
							break;
						}
						
						System.out.print("\n-->Enter Password\n");
						user_input = sc.nextLine();
						if(!user_input.equals(user_password)){
							System.out.print("\n\n	*Password Incorrect*\n\n");
							break;
						}
						boolean return_to_main = false;
						while(!return_to_main){
							System.out.print("\n\n"+ username +"\n====== Enter a selection ======\n"
							+ "1. Browse Products\n"
							+ "2. Search for Product\n"
							+ "3. Put Product up for Auction\n"
							+ "4. Bid on Product\n"
							+ "5. Sell Product\n"
							+ "6. Product Suggestions\n"
							+ "7. Logout\n\n");
                            while(true){
                                try{
                                    input = sc.nextInt();
                                    break;
                                }catch(Exception Ex){
                                    sc.nextLine();
                                    System.out.print("\nEnter a number\n");
                                }
                            }
							switch(input){
								case 1:
									System.out.print("\n\n"+ username +"\n====== Browse Products ======\n");
									db.display_root_categories();
									System.out.print("\n\n-->Enter a Category(by name)\n");
									sc.nextLine();
									user_input = sc.nextLine();
									while(db.display_categories(user_input) > 0){
										System.out.print("\n-->Enter a Category(by name)\n");
										user_input = sc.nextLine();
									}
									String leaf_cat = user_input;
									System.out.print("\n\n"+ username +"\n====== Sort By ======\n"
									+ "1. Max Bid\n"
									+ "2. Product Name\n\n");
                                    while(true){
                                        try{
                                            input = sc.nextInt();
                                            break;
                                        }catch(Exception Ex){
                                            sc.nextLine();
                                            System.out.print("\nEnter a number\n");
                                        }//end catch
                                    }//end while true
									if(input == 1){
										db.display_products_maxbid(leaf_cat);
									}else if(input == 2){
										db.display_products_alph(leaf_cat);
									}
									System.out.print("\n\nPress the enter key to continue");
									sc.nextLine();
									sc.nextLine();						
									break;
								case 2:
									sc.nextLine();
									System.out.print("\n\n"+ username +"\n====== Product Search ======\n\n-->Enter Keywords(Limit 2)\n");
									user_input = sc.nextLine();
									StringTokenizer st = new StringTokenizer(user_input);
										int pos = 0;
										String kw[] =  new String[2];
									     while (st.hasMoreTokens() && pos < 2) {
         									kw[pos++] = st.nextToken();
     									 }
     									 db.keyword_search(kw,pos);
     								System.out.print("\n\nPress the enter key to continue");
									sc.nextLine();	
									break;
								case 3:
									sc.nextLine();
									System.out.print("\n\n"+ username +"\n====== Put Product Up for Auction ======\n\n-->Enter Product Name\n");
									String p_name = sc.nextLine();
									System.out.print("\n\n-->Enter Product Description\n");
									String p_des = sc.nextLine();
									System.out.print("\n\n-->Enter Min Price\n");
                                    int p_min;
                                    while(true){
                                        try{
                                            p_min =sc.nextInt();
                                            if(p_min < 0){
                                                throw new Exception();
                                            }else{
                                                break;
                                            }
                                        }catch(Exception Ex){
                                            sc.nextLine();
                                            System.out.print("\nEnter a number\n");
                                        }
                                    }
									//int p_min = sc.nextInt();
									sc.nextLine();
									System.out.print("\n\n-->Enter number of days\n");
                                    int p_days;
                                    while(true){
                                        try{
                                            p_days = sc.nextInt();
                                            if(p_days < 0){
                                                throw new Exception();
                                            }else{
                                                break;
                                            }
                                        }catch(Exception Ex){
                                            sc.nextLine();
                                            System.out.print("\nEnter a number\n");
                                        }
                                    }//end while true
									//int p_days = sc.nextInt();
									sc.nextLine();
									System.out.print("\n\n-->Enter Category\n");
									String p_cat = sc.nextLine();
									
									if(db.put_up_for_auction(p_name,p_des,username,p_min,p_days,p_cat)){
										System.out.print("\n\n Auction Sucessful\n\n");
									}else{
										System.out.print("\n\n Auction Error, Invalid Category\n\n");
									}
									break;
								case 4:
									sc.nextLine();
									System.out.print("\n\n"+ username +"\n====== Bid on Product ======\n\n-->Enter Auction ID\n");
									int id;
                                    while(true){
                                        try{
                                            id = sc.nextInt();
                                            break;
                                        }catch(Exception Ex){
                                            sc.nextLine();
                                            System.out.print("\nEnter a number\n");
                                        }
                                    }
                                    String status = db.find_status(id);
                                    if(status == null){
										System.out.print("\n\n	*Cannot Bid on item,No such auction ID*");
										break;
									}
                                    
									if(!status.equals("underauction")){
										System.out.print("\n\n	*Cannot Bid on item, Auction is over*");
										break;
									}
									int current_max = db.max_amount(id);
									sc.nextLine();
									System.out.print("\n\n-->Enter Bid\n");
									input = sc.nextInt();
									if(input > current_max){
										if(db.insert_bid(id,input,username)){
											System.out.print("\n\n Bid Successful\n\n");
										}else{
											System.out.print("\n\n	 *Bid Failed*\n\n");
										}
									}else{
										System.out.print("\n\n	*Bid is too low*\n\n");
									}
									break;
								case 5:
									sc.nextLine();
									System.out.print("\n\n"+ username +"\n====== Sell Product ======\n\n");
									 if(db.show_user_products_underauction(username) == 0){
									 	System.out.print("\n\n*You have no products for sale*\n\n");
									 	break;
									 }
									 System.out.print("-->Enter Auction ID\n");
                                    while(true){
                                        try{
                                            input = sc.nextInt();
                                            break;
                                        }catch(Exception Ex){
                                            sc.nextLine();
                                            System.out.print("\nEnter a number\n");
                                        }
                                    }
									if(!db.terminate_auction(username,input)){
										System.out.print("\n\n	*Invalid Auction ID* \n\n");
									}
									break;
								case 6:
									System.out.print("\n\n"+ username +"\n====== Suggested Products ======\n\n\n");
									if(db.suggest(username) == 0){
										System.out.print("\n\n No Suggestions found\n\n");
									}
									System.out.print("\n\nPress the enter key to continue");
									sc.nextLine();
									sc.nextLine();	
									break;
								case 7:
									return_to_main = true;
									break;
							}//end user switch
							
						}//end user sub loop while not main
						break;
				
				case 2://main loop 2 option
						//admin
					    sc.nextLine();
						System.out.print("\n-->Enter Admin Name\n");
						user_input = sc.nextLine();
						username = user_input;
						user_password = db.find_admin_pwd(user_input);
						if(user_password == null){
							System.out.print("\n\n	*Admin Name Not Found*\n\n");
							break;
						}
						
						System.out.print("\n-->Enter Password\n");
						user_input = sc.nextLine();
						if(!user_input.equals(user_password)){
							System.out.print("\n\n	*Password Incorrect*\n\n");
							break;
						}
						
						return_to_main = false;
						while(!return_to_main){
							System.out.print("\n\n"+ username +"\n====== Enter a selection ======\n"
							+ "1. Register New User\n"
							+ "2. Update System Date\n"
							+ "3. Product Statistics\n"
							+ "4. Logout\n\n");
                            while(true){
                                try{
                                    input = sc.nextInt();
                                    break;
                                }catch(Exception Ex){
                                    sc.nextLine();
                                    System.out.print("\nEnter a number\n");
                                }
                            }//end while true
							switch(input)//admin loop
							{
							
							case 1:
							sc.nextLine();
                            System.out.print("\n\n-->Enter 1 for New User, 2 for New Admin\n");
							String pick = sc.nextLine();
							int pick1 = Integer.parseInt(pick);
							System.out.print("\n\n-->Enter login name\n");
                            String login = sc.nextLine();
                            System.out.print("\n\n-->Enter Password\n");
                            String pass = sc.nextLine();
                            System.out.print("\n\n-->Enter Name\n");
                            String name = sc.nextLine();
                            System.out.print("\n\n-->Enter address\n");
                            String address = sc.nextLine();
                            System.out.print("\n\n-->Enter email\n");
                            String email = sc.nextLine();
							
							if(pick1==1)
							{
							if(db.new_user(login,pass,name,address,email)){
                            System.out.print("\n    Account Creation Successful\n\n");
                            }else{
                            System.out.print("\n    *Account Creation Failed. Login taken*\n\n");
                             }
							break;
							}//end pick 1
							
							if(pick1==2)
							{
							if(db.new_admin(login,pass,name,address,email)){
                            System.out.print("\n    Account Creation Successful\n\n");
                            }else{
                            System.out.print("\n    *Account Creation Failed. Login taken*\n\n");
                             }
							break;
							}//end pick 2
							     break;
							
							case 2:
							sc.nextLine();
						    System.out.print("\n\n"+ username +"\n====== Update System Time ======\n\n-->Enter Time ex:11-dec-2012/12:00:00am\n");
							String in = sc.nextLine();     
							    if(db.update_sys_time(in))
							      {
							      System.out.print("\n    System Time Updated\n\n");
							      }
							        else
							          {
							          System.out.print("\n    System Time Update Failed\n\n");
							          }   
							     break;//end case2 
							
							 case 3:
							 
							boolean rtm = false;
						while(!rtm){// product stats 
							System.out.print("\n\n"+ username +"\n====== Enter a selection ======\n"
							+ "1. Top Leaf Categories For Past X Months\n"
							+ "2. Top Root Categories For Past X Months\n"
							+ "3. Tob Bidders For Past X Months\n"
							+ "4. Top Spenders For Past X Months\n"
							+ "5. Return To Admin Menu\n\n");
                            while(true){
                                try{
                                    input = sc.nextInt();
                                    break;
                                }catch(Exception Ex){
                                    sc.nextLine();
                                    System.out.print("\nEnter a number\n");
                                }//end catch
                            }//end while true
							switch(input){//product stat switch 
							
							case 1:
							 sc.nextLine();
						     System.out.print("\n\n"+ username +"\n====== Top Leaf Categories For Past X Months ======\n\n-->Enter Number For Past Months \n");
							 String p_mon = sc.nextLine();    
							 System.out.print("\n\n-->Enter Number For Top Results\n");
                             String tk = sc.nextLine();    
							 int month =0;
							 int top = 0;
							 try
                             {
                              month = Integer.parseInt(p_mon);
                              }
                            catch(NumberFormatException e)
                             {
                               System.out.print("\n Please Use Only whole Numbers For Month \n"); 
                              }
							 try
                             {
                              top = Integer.parseInt(p_mon);
                              }
                            catch(NumberFormatException e)
                             {
                               System.out.print("\n Please Use Only whole Numbers For Top Results \n"); 
                              }
							 
							  
							    String[] r = new String[10];
							    r=db.top_leaf(month,top);
							     int rn = r.length;
							     for(int i=0;i<rn;i++)
							     {
							     System.out.print("\n"+r[i]);
							     }
							     break;     
							
							case 2:
							     sc.nextLine();
						     System.out.print("\n\n"+ username +"\n====== Top Root Categories For Past X Months ======\n\n-->Enter Number For Past Months \n");
							 p_mon = sc.nextLine();    
							 System.out.print("\n\n-->Enter Number For Top Results\n");
                              tk = sc.nextLine();    
							  month = Integer.parseInt(p_mon);
							  top = Integer.parseInt(tk);   
							    r = new String[10];
							     r=db.top_root(month,top);
							     rn = r.length;
							     for(int i=0;i<rn;i++)
							     {
							     System.out.print("\n"+r[i]);
							     }
							    
							     break;
							     
							case 3:
							 sc.nextLine();
						     System.out.print("\n\n"+ username +"\n====== Top Bidders For Past X Months ======\n\n-->Enter Number For Past Months \n");
							 p_mon = sc.nextLine();    
							 System.out.print("\n\n-->Enter Number For Top Results\n");
                              tk = sc.nextLine();    
							  month = Integer.parseInt(p_mon);
							  top = Integer.parseInt(tk);   
							    r = new String[10];
							    r=db.top_bid(month,top);
							     rn = r.length;
							     for(int i=0;i<rn;i++)
							     {
							     System.out.print("\n"+r[i]);
							     }
							    
							     break;
							     
							 case 4:
							 sc.nextLine();
						     System.out.print("\n\n"+ username +"\n====== Top Spenders For Past X Months ======\n\n-->Enter Number For Past Months \n");
							 p_mon = sc.nextLine();    
							 System.out.print("\n\n-->Enter Number For Top Results\n");
                              tk = sc.nextLine();    
							  month = Integer.parseInt(p_mon);
							  top = Integer.parseInt(tk);   
							    r = new String[10];
							    r=db.top_buy(month,top);
							     rn = r.length;
							     for(int i=0;i<rn;i++)
							     {
							     System.out.print("\n"+r[i]);
							     }
							     break;         
							
							case 5:
							   rtm = true;
								 break;
							
							}//end product stat switch
							
							
							
							}//end admin sub while
						
						case 4:
							  System.out.print("\n Why am I here? line 1368");
							   return_to_main = true;
								 break;
						
				}//end admin switch
				
			
			}//end admin while
		
		          case 3://main loop 3 option
						exit = true;
						break; 
		
		}//end big loop switch
	
    	
    	
    	//if(db.disconnect()){
    	//	System.out.print("\nDisconnection Sucessful\n\n");
    	//}
  
}//end big while

if(db.disconnect()){
    		System.out.print("\nDisconnection Sucessful\n\n");
    	}

}//end main
}//end class