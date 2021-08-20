package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.view.ConsoleService;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
	private AccountService accountService;

    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL), new AccountService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService, AccountService accountService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.accountService = accountService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		Balance balance = accountService.getUserBalance();
		System.out.println("Your current account balance is: $"+balance.getBalance());
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
		boolean shouldCotinue = true;
		while (shouldCotinue) {
			Account userAccount = accountService.getAccountInfo(currentUser.getUser().getId());
			Transfer[] allTransferSummaries = accountService.getTransferSummary(userAccount.getAccountId());
			System.out.println("-------------------------------------------");
			System.out.println("Transfers");
			System.out.println("ID          From/To                 Amount");
			System.out.println("-------------------------------------------");
			for(Transfer transferSummary : allTransferSummaries){
				if(transferSummary.getAccountFrom() == userAccount.getAccountId()){
					System.out.println(transferSummary.getTransferId()+"          "+"To"+": "+ transferSummary.getToUserName()+"    $"+ transferSummary.getAmount());
				}
				else{
					System.out.println(transferSummary.getTransferId()+"          "+"From"+": "+ transferSummary.getFromUserName()+"    $"+ transferSummary.getAmount());
				}

			}

			int response = console.getUserInputInteger("Please enter transfer ID to view details (0 to cancel): ");
			if (response == 0){
				shouldCotinue = false;
				break;
			}
			Transfer transfer = accountService.getTransferInfo(response);
			if(transfer != null) {
				System.out.println("--------------------------------------------");
				System.out.println("Transfer Details");
				System.out.println("--------------------------------------------");
				System.out.println("Id: " + transfer.getTransferId());
				System.out.println("From: " + transfer.getFromUserName());
				System.out.println("To: " + transfer.getToUserName());
				System.out.println("Type: " + (transfer.getTransferTypeId() == 2 ? "Send" : "Request"));
				System.out.println("Status: " + (transfer.getTransferStatusId() == 1 ? "Pending" : transfer.getTransferStatusId() == 2 ? "Approved" : "Rejected"));
				System.out.println("Amount: " + transfer.getAmount());
			} else {
				System.out.println("Invalid Transfer ID. Please enter a valid Transfer ID.");
			}


		}
	}
	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		boolean shouldCotinue = true;
		while (shouldCotinue) {
			Account userAccount = accountService.getAccountInfo(currentUser.getUser().getId());
			Transfer[] allTransferSummaries = accountService.getPendingTransfers(userAccount.getAccountId());
			System.out.println("-------------------------------------------");
			System.out.println("Pending Transfers");
			System.out.println("ID          From/To                 Amount");
			System.out.println("-------------------------------------------");
			for (Transfer transferSummary : allTransferSummaries) {
				if (transferSummary.getAccountFrom() == userAccount.getAccountId()) {
					System.out.println(transferSummary.getTransferId() + "          " + "To" + ": " + transferSummary.getToUserName() + "    $" + transferSummary.getAmount());
				} else {
					System.out.println(transferSummary.getTransferId() + "          " + "From" + ": " + transferSummary.getFromUserName() + "    $" + transferSummary.getAmount());
				}

			}

			int response = console.getUserInputInteger("Please enter transfer ID to approve/reject (0 to cancel): ");
			if (response == 0) {
				shouldCotinue = false;
				break;
			}
			Transfer transfer = accountService.getTransferInfo(response);
			if (transfer != null) {
				System.out.println("--------------------------------------------");
				System.out.println("Transfer Details");
				System.out.println("--------------------------------------------");
				System.out.println("Id: " + transfer.getTransferId());
				System.out.println("From: " + transfer.getFromUserName());
				System.out.println("To: " + transfer.getToUserName());
				System.out.println("Type: " + (transfer.getTransferTypeId() == 2 ? "Send" : "Request"));
				System.out.println("Status: " + (transfer.getTransferStatusId() == 1 ? "Pending" : transfer.getTransferStatusId() == 2 ? "Approved" : "Rejected"));
				System.out.println("Amount: " + transfer.getAmount());
			} else {
				System.out.println("Invalid Transfer ID. Please enter a valid Transfer ID.");
			}
		}

		}

	private void sendBucks() {
		boolean shouldCotinue = true;
		while(shouldCotinue) {
			User[] allUsers = accountService.getAllAvailableUsers();
			System.out.println("-------------------------------------------");
			System.out.println("Users");
			System.out.println("ID            Name");
			System.out.println("-------------------------------------------");
			console.displayMenuOptions(allUsers);

			int choice = console.getUserInputInteger("Enter ID of user you are sending to (0 to cancel)");

			if (choice == 0) {
				shouldCotinue = false;
				break;
			}
			boolean askForAmount= true;
			BigDecimal amount =  null;
			while(askForAmount) {
				 amount = new BigDecimal(console.getUserInputDouble("Enter the amount")).setScale(2, RoundingMode.HALF_UP);;


					if(amount.compareTo(accountService.getUserBalance().getBalance()) ==1){
						System.out.println("Insufficient Funds. Current Balance $"+accountService.getUserBalance().getBalance());

					}
					else{

					if(amount.compareTo(new BigDecimal("0.00")) == 0 ||amount.compareTo(new BigDecimal("0.00")) == -1) {
						System.out.println("Please enter an amount greater than $0");
					}
					else{
							askForAmount = false;
						}
					}



			}

			Account fromAccount = accountService.getAccountInfo(currentUser.getUser().getId());
			Account toAccount = accountService.getAccountInfo(choice);
			if(fromAccount != null && toAccount != null) {
				Transfer transfer = new Transfer();
				transfer.setAccountFrom(fromAccount.getAccountId());
				transfer.setAccountTo(toAccount.getAccountId());
				transfer.setTransferStatusId(2);
				transfer.setTransferTypeId(2);
				transfer.setAmount(amount);

				transfer = accountService.sendTransfer(transfer);
				shouldCotinue = false;
			} else {
				if(toAccount == null){
					System.out.println(choice + " is not a valid ID. Please enter a valid ID.");
				}
				if(fromAccount == null){
					System.out.println(currentUser.getUser().getId() + " is not a valid ID. Please enter a valid ID.");
				}
			}



		}

		
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		boolean shouldCotinue = true;
		while(shouldCotinue) {
			User[] allUsers = accountService.getAllAvailableUsers();
			System.out.println("-------------------------------------------");
			System.out.println("Users");
			System.out.println("ID            Name");
			System.out.println("-------------------------------------------");
			console.displayMenuOptions(allUsers);

			int choice = console.getUserInputInteger("Enter ID of user you are requesting from (0 to cancel)");

			if (choice == 0) {
				shouldCotinue = false;
				break;
			}
			boolean askForAmount= true;
			BigDecimal amount =  null;
			while(askForAmount) {
				amount = new BigDecimal(console.getUserInputDouble("Enter the amount")).setScale(2, RoundingMode.HALF_UP);;


						if(amount.compareTo(new BigDecimal("0.00")) == 0 ||amount.compareTo(new BigDecimal("0.00")) == -1) {
							System.out.println("Please enter an amount greater than $0");
						}
						else{
							askForAmount = false;
						}
					}

			Account fromAccount = accountService.getAccountInfo(currentUser.getUser().getId());
			Account toAccount = accountService.getAccountInfo(choice);
			if(fromAccount != null && toAccount != null) {
				Transfer transfer = new Transfer();
				transfer.setAccountFrom(fromAccount.getAccountId());
				transfer.setAccountTo(toAccount.getAccountId());
				transfer.setTransferStatusId(1);
				transfer.setTransferTypeId(1);
				transfer.setAmount(amount);

				transfer = accountService.sendTransfer(transfer);
				shouldCotinue = false;
			} else {
				if(toAccount == null){
					System.out.println(choice + " is not a valid ID. Please enter a valid ID.");
				}
				if(fromAccount == null){
					System.out.println(currentUser.getUser().getId() + " is not a valid ID. Please enter a valid ID.");
				}
			}



		}


	}
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		AccountService.setAuthToken("");
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
				AccountService.setAuthToken(currentUser.getToken());
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}



}
