package Controller;

import Entity.Listing;
import Exception.ResourceNotFoundException;
import Repository.ListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/")
public class ListingController {

    @Autowired
    private ListingRepository listingRepository;

    // Get all listings
    @GetMapping("/listings")
    public List<Listing> getAllListings(){

        return this.listingRepository.findAll();

    }

    // Get listing by listing id
    @GetMapping("/listings/{id}")
    public ResponseEntity<Listing> getListingById(@PathVariable(value = "id") Long listingid)
        throws ResourceNotFoundException {

        // Lookup
        Listing listing = listingRepository.findById(listingid)
                .orElseThrow(() -> new ResourceNotFoundException("No listings matching ' " + listingid + " '"));

                return ResponseEntity.ok().body(listing);
    }

    // Create a listing

    @PostMapping("listings")
    public Listing createListing(@RequestBody Listing listing){
        return this.listingRepository.save(listing);
    }

    // Update a listing

    @PutMapping("listings/{id}")
    public ResponseEntity<Listing> updateListing
            (@PathVariable(value = "id") Long listingid,
             @Validated @RequestBody Listing listingInfo) throws ResourceNotFoundException {

        // Lookup
        Listing listing = listingRepository.findById(listingid)
                .orElseThrow(() -> new ResourceNotFoundException("No listings matching ' " + listingid + " '"));

        listing.setListingDesc(listingInfo.getListingDesc());
        listing.setListingPrice(listingInfo.getListingPrice());

        return ResponseEntity.ok(this.listingRepository.save(listing));

    }
    // Delete a listing
    @DeleteMapping("listings/{id}")
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