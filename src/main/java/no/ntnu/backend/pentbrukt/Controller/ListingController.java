package no.ntnu.backend.pentbrukt.Controller;

import no.ntnu.backend.pentbrukt.Entity.Listing;
import no.ntnu.backend.pentbrukt.Exception.ResourceNotFoundException;
import no.ntnu.backend.pentbrukt.Repository.ListingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api")
@CrossOrigin
public class ListingController {

    private ListingRepository listingRepository;

    public ListingController(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    // Get all listings
    @GetMapping("listings")
    //@PreAuthorize("permitAll()")
    public List<Listing> getAllListings() {

        return this.listingRepository.findAll();

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

    @PostMapping("listings")
    public Listing createListing(@RequestBody Listing listing) {

       /* Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getPrincipal().toString(); // gets the credentials used for login, email..

        */

        return this.listingRepository.save(listing);
    }


    // Update a listing TODO: CHECK LOGIN INFO - COMPARE WITH LISTING INFO - TO BE ABLE TO EDIT LISTING
    @PutMapping("listings/{id}")
    public ResponseEntity<Listing> updateListing

    (@PathVariable(value = "id") Long listingid,
     @Validated @RequestBody Listing listingInfo) throws ResourceNotFoundException {

        // Lookup
        Listing listing = listingRepository.findById(listingid)
                .orElseThrow(() -> new ResourceNotFoundException("No listings matching ' " + listingid + " '"));

        listing.setListingTitle(listingInfo.getListingTitle());
        listing.setListingDesc(listingInfo.getListingDesc());
        listing.setListingPrice(listingInfo.getListingPrice());

        return ResponseEntity.ok(this.listingRepository.save(listing));

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
