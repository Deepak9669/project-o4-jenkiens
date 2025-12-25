package in.co.rays.proj4.bean;

	public class AccountBean extends BaseBean {

	   
	    private String accountNo;

	    
	    private String accountType;

	    
	    private String bankName;

	    
	    private String balance;

	    	    public String getAccountNo() {
	        return accountNo;
	    }

	    
	    public void setAccountNo(String accountNo) {
	        this.accountNo = accountNo;
	    }

	  
	    public String getAccountType() {
	        return accountType;
	    }

	    
	    public void setAccountType(String accountType) {
	        this.accountType = accountType;
	    }

	   
	    public String getBankName() {
	        return bankName;
	    }

	   
	    public void setBankName(String bankName) {
	        this.bankName = bankName;
	    }

	   
	    public String getBalance() {
	        return balance;
	    }

	    	    public void setBalance(String balance) {
	        this.balance = balance;
	    }

	   
	    @Override
	    public String getKey() {
	        return id + "";
	    }

	    
	    @Override
	    public String getValue() {
	        return accountNo;
	    }
	}