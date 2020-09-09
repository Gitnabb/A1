package no.ntnu.backend.pentbrukt.Repository;

import no.ntnu.backend.pentbrukt.Entity.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingRepository extends JpaRepository <Listing, Long>{
}
