package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;

import com.techelevator.tenmo.services.TransferService;
import org.springframework.web.client.RestTemplate;

import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;


public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;
    private TransferService transferService;
    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        transferService = new TransferService(currentUser);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private BigDecimal viewCurrentBalance() {
        BigDecimal balance = null;
        try {
            ResponseEntity<BigDecimal> response =
                    restTemplate.exchange(API_BASE_URL + "accounts",
                            HttpMethod.GET, makeAuthEntity(), BigDecimal.class);
            balance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        System.out.println(balance);
        return balance;
    }

    private void viewTransferHistory() {
        Transfer[] transfers = transferService.viewTransferHistory();
        Map<Integer,Transfer> transferIdMap = new LinkedHashMap<>();
        transferIdMap.put(0,null);
        if (transfers != null) {
            for (Transfer transfer : transfers) {
                transferIdMap.put(transfer.getTransfer_id(),transfer);
                System.out.println("ID " + transfer.getTransfer_id() + " | FROM: " + transfer.getUsername_from() + " | TO: " + transfer.getUsername_to() + " | $" + transfer.getAmount());
            }
        } else {
            consoleService.printErrorMessage();
        }
        int transferSelection = -1;
        while (!transferIdMap.containsKey(transferSelection)) {
            transferSelection = consoleService.promptForMenuSelection("Please enter transfer ID to view details (0 to cancel): ");
        }
        if(transferSelection == 0){
            mainMenu();
        }
        System.out.println(transferIdMap.get(transferSelection));
    }

    private void viewPendingRequests() {
        // TODO Auto-generated method stub

    }

	private void sendBucks() {


        listUsers();
        Transfer transferFromCLI = makeTransfer();
        Transfer transferFromAPI = addTransfer(transferFromCLI);
        if (transferFromAPI == null) {
            consoleService.printErrorMessage();
        }

		
	}




    private void requestBucks() {
        // TODO Auto-generated method stub

    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }


    public Transfer addTransfer(Transfer newTransfer) {
        Transfer returnedTransfer = null;
        try {
            returnedTransfer = restTemplate.postForObject(API_BASE_URL + "transfers",
                    makeTransferEntity(newTransfer), Transfer.class);
            System.out.println("Transaction Complete");
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return returnedTransfer;
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(transfer, headers);
    }

    public void listUsers() {
        User[] users = null;
        try {
            ResponseEntity<User[]> response =
                    restTemplate.exchange(API_BASE_URL + "users",
                            HttpMethod.GET, makeAuthEntity(), User[].class);
            users = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        if (users != null) {
            for(User i: users) {
                if(currentUser.getUser().getUsername().equals(i.getUsername())) {
                } else {
                    System.out.println(i);
                }
            }
        }
    }




    public Transfer makeTransfer() {
        // list of users
        Transfer transfer = null;
        System.out.println("Enter User Id: ");
        String strId = scanner.nextLine();
        System.out.println("Enter Amount: ");
        String strAmount = scanner.nextLine();


        try {
            int id = Integer.parseInt(strId);
            double amount = Double.parseDouble(strAmount);
            int AccountId = userToAccountId(id);
            double compareAmount = Double.valueOf(viewCurrentBalance().doubleValue());

            if (amount > compareAmount) {
                throw new NumberFormatException();
            }
            if (amount <= 0) {
                throw new NumberFormatException();
            }
            Long l = currentUser.getUser().getId();
            Integer sender = Integer.valueOf(l.intValue());
            Integer senderId = userToAccountId(sender);
            transfer = new Transfer();
            transfer.setAccount_from(senderId);
            transfer.setAccount_to(AccountId);
            transfer.setAmount(amount);
            transfer.setTransfer_status_id(2);
            transfer.setTransfer_type_id(2);

        }

        catch (NumberFormatException e) {
            System.out.println("error");
        }
        return transfer;
    }

    public int userToAccountId(int id) {
        int num = 0;
        try {
            ResponseEntity<Integer> response =
                    restTemplate.exchange(API_BASE_URL + "accounts/" + id,
                            HttpMethod.GET, makeAuthEntity(), Integer.class);
            num = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return num;
    }

}
