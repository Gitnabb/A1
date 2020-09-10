package no.ntnu.backend.pentbrukt.Controller;

import no.ntnu.backend.pentbrukt.Entity.Listing;
import no.ntnu.backend.pentbrukt.Exception.ResourceNotFoundException;
import no.ntnu.backend.pentbrukt.Repository.ListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api")
public class ListingController {

    @Autowired
    private ListingRepository listingRepository;

    // Get all listings
    @GetMapping("listings")
    @PreAuthorize("hasAnyRole('ROLE_USERLOGGEDIN', 'ROLE_USER')")
    public List<Listing> getAllListings(){

        return this.listingRepository.findAll();

    }

    // Get listing by listing id
    @GetMapping("listings/{id}")
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
    @PreAuthorize("hasAuthority('listing:write')")
    public Listing createListing(@RequestBody Listing listing){
        return this.listingRepository.save(listing);
    }


    // Update a listing
    @PutMapping("listings/{id}")
    @PreAuthorize("hasAuthority('listing:write')")
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
    // Delete a listing
    @DeleteMapping("listings/{id}")
    @PreAuthorize("hasAuthority('listing:write')")
    public Map<String, Boolean> deleteListing(@PathVariable(value = "id") Long listingid) throws ResourceNotFoundException{

        // Lookup
        Listing listing = listingRepository.findById(listingid)
                .orElseThrow(() -> new ResourceNotFoundException("No listings matching ' " + listingid + " '"));

        this.listingRepository.delete(listing);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return response;

    }


}
