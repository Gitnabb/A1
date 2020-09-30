package no.ntnu.backend.pentbrukt.Controller;

import no.ntnu.backend.pentbrukt.Email.MailController;
import no.ntnu.backend.pentbrukt.Entity.Listing;
import no.ntnu.backend.pentbrukt.Exception.ResourceNotFoundException;
import no.ntnu.backend.pentbrukt.Repository.ListingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/listings/")
@CrossOrigin
public class ListingController {

    private final ListingRepository listingRepository;
    private final MailController mailController;

    public ListingController(ListingRepository listingRepository, MailController mailController) {
        this.listingRepository = listingRepository;
        this.mailController = mailController;
    }

    // Get all listings
    @GetMapping("get-all-listings")
    //@PreAuthorize("permitAll()")
    public List<Listing> getAllListings() {

        return this.listingRepository.findAllByListingSold(false);

        //return this.listingRepository.findAll();

    }

    // Get listing by listing id
    @GetMapping("listings/{id}")
    //@PreAuthorize("permitAll()")
    public ResponseEntity<Listing> getListingById(@PathVariable(value = "id") Long listingid)
            throws ResourceNotFoundException {
        // Lookup
        Listing listing = listingRepository.findById(listingid)
                .orElseThrow(() -> new ResourceNotFoundException("No listings matching ' " + listingid + " '"));

        return ResponseEntity.ok().body(listing);
    }


    // Create a listing
    //hasRole, HasAnyRole, hasAuthority, hasAnyAuthority

    @PostMapping("new-listing")
    public Listing createListing(@RequestBody Listing listing) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();

        listing.setListingSeller(currentUser);
        listing.setListingSold(false);
        System.out.println(listing.getListingSeller() + " just posted " + listing.getListingTitle() + " to PentBrukt.");
        return this.listingRepository.save(listing);
    }


    // Update a listing TODO: CHECK LOGIN INFO - COMPARE WITH LISTING INFO - TO BE ABLE TO EDIT LISTING
    @PutMapping("edit-listing/{id}")
    public ResponseEntity<Listing> updateListing

    (@PathVariable(value = "id") Long listingid,
     @Validated @RequestBody Listing listingInfo) throws ResourceNotFoundException {

        // Look up the listing in the DB
        Listing listing = listingRepository.findById(listingid)
                .orElseThrow(() -> new ResourceNotFoundException("No listings matching ' " + listingid + " '"));

        listing.setListingTitle(listingInfo.getListingTitle());
        listing.setListingDesc(listingInfo.getListingDesc());
        listing.setListingPrice(listingInfo.getListingPrice());

        listing.setListingSold(listingInfo.isListingSold());

        return ResponseEntity.ok(this.listingRepository.save(listing));

    }


    @PutMapping("buy-listing/{id}")
    public ResponseEntity<Listing> buyListing
            (@PathVariable(value = "id") long listingid,
             @Validated @RequestBody Listing listing) throws ResourceNotFoundException {

        // Look up the listing in DB
        Listing soldListing = listingRepository.findById(listingid)
                .orElseThrow(() -> new ResourceNotFoundException("No listings matching ' " + listingid + " '"));

        // The JSON payload must and will include a 'true' for this
        // Essentially, a "buy" button that will send a JSON request including sold = true
        soldListing.setListingSold(listing.isListingSold());

        // REGISTER USER AS BUYER (?)


        // SEND EMAIL TO SELLER
        mailController.sendMail();

        return ResponseEntity.ok(this.listingRepository.save(soldListing));

    }


    // Delete a listing TODO: CHECK LOGIN INFO - COMPARE WITH LISTING INFO - TO BE ABLE TO DELETE
    @DeleteMapping("listings/{id}")
    public Map<String, Boolean> deleteListing(@PathVariable(value = "id") Long listingid) throws ResourceNotFoundException {

        // Lookup
        Listing listing = listingRepository.findById(listingid)
                .orElseThrow(() -> new ResourceNotFoundException("No listings matching ' " + listingid + " '"));

        this.listingRepository.delete(listing);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return response;

    }

}
