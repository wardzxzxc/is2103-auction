package oasadminpanel;

import ejb.session.stateless.AuctionListingControllerRemote;
import entity.AuctionListing;
import entity.Employee;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;
import util.enumeration.EmployeeAccessRightsEnum;
import util.exception.AuctionListingExistException;
import util.exception.AuctionListingNotFoundException;
import util.exception.GeneralException;
import util.exception.InvalidAccessRightException;

/**
 *
 * @author Cloud
 */
public class SalesModule {
    
    private AuctionListingControllerRemote auctionListingControllerRemote;
    
    private Employee currentEmployee;

    public SalesModule() {
    }

    public SalesModule(AuctionListingControllerRemote auctionListingControllerRemote, Employee currentEmployee) {
        this.auctionListingControllerRemote = auctionListingControllerRemote;
        this.currentEmployee = currentEmployee;
    }
    
    public void salesMenu() throws InvalidAccessRightException {
        
        if(currentEmployee.getAccessRight() != EmployeeAccessRightsEnum.SALES) {
            throw new InvalidAccessRightException("You don't have SALES rights to access the sales module.");
        }
        
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** OAS Admin Panel :: Sales ***\n");
            System.out.println("1: Create Auction Listing");
            System.out.println("2: View Auction Listing Details");
            System.out.println("3: View All Auction Listings");
            System.out.println("4: View All Auction Listings with Bids but Below Reserve Price");
            System.out.println("5: Back\n");
            response = 0;
            
            while(response < 1 || response > 5) {
                System.out.println("> ");
                
                response = sc.nextInt();
                
                if(response == 1) {
                    doCreateNewAuctionListing();
                }
                else if(response == 2) {
                    doViewAuctionListingDetails();
                }
                else if(response == 3) {
                    doViewAllAuctionListings();
                }
                else if(response == 4) {
                    doViewAllAuctionListingsBelowReserve();
                }
                else if(response == 5) {
                    break;
                }
                else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if(response == 5) {
                break;
            }
        }
        
    }
    
    private void doCreateNewAuctionListing() {
        Scanner sc = new Scanner(System.in);
        AuctionListing newAuctionListing = new AuctionListing();
        
        System.out.println("*** OAS Admin Panel :: Sales :: Create New Auction Listing ***\n");
        System.out.print("Enter Product Name> ");
        newAuctionListing.setProductName(sc.nextLine().trim());
        
        DateFormat df = new SimpleDateFormat("dd/mm/yyyy, HH:mm");
        
        while(true) {
            System.out.println("Enter Auction Start Time (Format: ddmmyyyy, HHmm)> ");
            String startDateTime = sc.nextLine().trim();
            
            try {
                newAuctionListing.setStartDateTime(df.parse(startDateTime));
                System.out.println("Start Time: " + df.format(startDateTime));
                break;
            }
            catch (ParseException ex) {
            }
        }
        
        while(true) {
            System.out.println("Enter Auction End Time (Format: ddmmyyyy, HHmm)> ");
            String endDateTime = sc.nextLine().trim();
            
            try {
                newAuctionListing.setEndDateTime(df.parse(endDateTime));
                System.out.println("End Time: " + df.format(endDateTime));
                break;
            }
            catch (ParseException ex) {
            }
        }
        
        System.out.println("Enter Starting Bid Amount> ");
        newAuctionListing.setCurrentHighestPrice(sc.nextBigDecimal());
        
        sc.nextLine();
        
        System.out.println("Enter Reserve Price (enter '0' if no reserve)> ");
        newAuctionListing.setReservePrice(sc.nextBigDecimal());
        
        sc.nextLine();
        
        try {
            newAuctionListing = auctionListingControllerRemote.createNewAuctionListing(newAuctionListing);
        }
        catch (AuctionListingExistException | GeneralException ex) {
        }
        
        System.out.println("New auction listing created successfully!: " + newAuctionListing.getAuctionListingId() + "\n");
    }
    
    private void doViewAuctionListingDetails() {
        
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** OAS Admin Panel :: Sales :: View Auction Listing Details ***\n");
        System.out.print("Enter Auction Listing ID> ");
        Long auctionListingId = sc.nextLong();
        
        DateFormat df = new SimpleDateFormat("dd/mm/yyyy, HH:mm");
        
        try
        {
            AuctionListing auctionListing = auctionListingControllerRemote.retrieveAuctionListingById(auctionListingId);
            System.out.printf("%8s%50s%25s%25s%20s%20s%8s\n", "Auction Listing ID", "Product Name", "Start Date Time", "End Date Time", "Current Highest Price", "Reserve Price", "Active");
            System.out.printf("%8s%50s%25s%25s%20s%20s%8s\n", auctionListing.getAuctionListingId().toString(), auctionListing.getProductName(), auctionListing.getStartDateTime(), auctionListing.getEndDateTime(), auctionListing.getCurrentHighestPrice(), auctionListing.getReservePrice(), auctionListing.getActive());         
            System.out.println("------------------------");
            System.out.println("1: Update Auction Listing");
            System.out.println("2: Delete Auction Listing");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = sc.nextInt();

            if(response == 1)
            {
                doUpdateAuctionListing(auctionListing);
            }
            else if(response == 2)
            {
                doDeleteAuctionListing(auctionListing);
            }
        }
        catch(AuctionListingNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving auction listing: " + ex.getMessage() + "\n");
        }
        
    }
    
    private void doUpdateAuctionListing(AuctionListing auctionListing) {
        
        Scanner sc = new Scanner(System.in);        
        String input;
        
        System.out.println("*** OAS Admin Panel :: Sales :: View Auction Listing Details :: Update Auction Listing ***\n");
        System.out.print("Enter Product Name (blank if no change)> ");
        input = sc.nextLine().trim();
        if(input.length() > 0)
        {
            auctionListing.setProductName(input);
        }
        
        DateFormat df = new SimpleDateFormat("dd/mm/yyyy, HH:mm");
        
        while(true) {
            System.out.println("Enter Auction Start Time (Format: ddmmyyyy, HHmm)> ");
            input = sc.nextLine().trim();
            
            try {
                auctionListing.setStartDateTime(df.parse(input));
                System.out.println("Start Time: " + df.format(input));
                break;
            }
            catch (ParseException ex) {
            }
        }
        
        while(true) {
            System.out.println("Enter Auction End Time (Format: ddmmyyyy, HHmm)> ");
            input = sc.nextLine().trim();
            
            try {
                auctionListing.setEndDateTime(df.parse(input));
                System.out.println("End Time: " + df.format(input));
                break;
            }
            catch (ParseException ex) {
            }
        }
        
        System.out.println("Enter Starting Bid Amount (enter '0' if no change)> ");
        BigDecimal amount = sc.nextBigDecimal();
        if (amount.compareTo(BigDecimal.ZERO) != 0) {
            auctionListing.setCurrentHighestPrice(sc.nextBigDecimal());
        }
        
        sc.nextLine();
        
        System.out.println("Enter Reserve Price (enter '0' if no change)> ");
        amount = sc.nextBigDecimal();
        if (amount.compareTo(BigDecimal.ZERO) != 0) {
            auctionListing.setReservePrice(sc.nextBigDecimal());
        }
        
        sc.nextLine();
        
        auctionListingControllerRemote.updateAuctionListing(auctionListing);
        System.out.println("Auction Listing updated successfully!\n");
        
    }
    
    private void doDeleteAuctionListing(AuctionListing auctionListing) {
        
        Scanner sc = new Scanner(System.in);        
        String input;
        
        System.out.println("*** OAS Admin Panel :: Sales :: View Auction Listing Details :: Delete Auction Listing ***\n");
        System.out.printf("Confirm Delete Auction Listing %s (Auction Listing ID: %d) (Enter 'Y' to Delete)> ", auctionListing.getProductName(), auctionListing.getAuctionListingId());
        input = sc.nextLine().trim();
        
        if(input.equals("Y"))
        {
            try {
                auctionListingControllerRemote.deleteAuctionListing(auctionListing.getAuctionListingId());
                System.out.println("Auction listing deleted successfully!\n");
            } 
            catch (AuctionListingNotFoundException ex) {
                System.out.println("An error has occurred while deleting auction listing: " + ex.getMessage() + "\n");
            }            
        }
        else {
            System.out.println("Auction listing NOT deleted!\n");
        }
        
    }
    
    private void doViewAllAuctionListings() {
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** OAS Admin Panel :: Sales :: View All Auction Listings ***\n");
        
        List<AuctionListing> auctionListings = auctionListingControllerRemote.retrieveAllAuctionListing();
        System.out.printf("%8s%50s%25s%25s%20s%20s%8s\n", "Auction Listing ID", "Product Name", "Start Date Time", "End Date Time", "Current Highest Price", "Reserve Price", "Active");
        
        for(AuctionListing listing:auctionListings) {
            System.out.printf("%8s%50s%25s%25s%20s%20s%8s\n", listing.getAuctionListingId().toString(), listing.getProductName(), listing.getStartDateTime(), listing.getEndDateTime(), listing.getCurrentHighestPrice(), listing.getReservePrice(), listing.getActive());
        }
        
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
        
    }
    
    private void doViewAllAuctionListingsBelowReserve() {
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** OAS Admin Panel :: Sales :: View All Auction Listings Below Reserve Price ***\n");
        
        List<AuctionListing> auctionListings = auctionListingControllerRemote.retrieveAllAuctionListing();
        System.out.printf("%8s%50s%25s%25s%20s%20s%8s\n", "Auction Listing ID", "Product Name", "Start Date Time", "End Date Time", "Current Highest Price", "Reserve Price", "Active");
        
        for(AuctionListing listing:auctionListings) {
            if (!(listing.getActive())&&(listing.getCurrentHighestPrice().compareTo(listing.getReservePrice()) < 0)) {
                System.out.printf("%8s%50s%25s%25s%20s%20s%8s\n", listing.getAuctionListingId().toString(), listing.getProductName(), listing.getStartDateTime(), listing.getEndDateTime(), listing.getCurrentHighestPrice(), listing.getReservePrice(), listing.getActive());
            }
        }
        
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
        
    }
    
}
