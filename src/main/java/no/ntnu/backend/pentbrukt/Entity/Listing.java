package no.ntnu.backend.pentbrukt.Entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "listings")
@AllArgsConstructor
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long listingid;

    @Column(name = "listingtitle")
    private String listingTitle;

    @Column(name = "listingdesc")
    private String listingDesc;

    @Column(name = "listingseller")
    private long listingsellerid;

    @Column(name = "listingprice")
    private int listingPrice;

/*    @Column(name = "listingseller")
    private String listingSeller;*/

    @Column(name = "listingpublished")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate listingPublished;

    public Listing(){
        super();
    }

    public long getListingid() {
        return listingid;
    }

    public void setListingid(long listingid) {
        this.listingid = listingid;
    }

    public String getListingTitle() {
        return listingTitle;
    }

    public void setListingTitle(String listingTitle) {
        this.listingTitle = listingTitle;
    }

    public String getListingDesc() {
        return listingDesc;
    }

    public void setListingDesc(String listingDesc) {
        this.listingDesc = listingDesc;
    }

    public int getListingPrice() {
        return listingPrice;
    }

    public void setListingPrice(int listingPrice) {
        this.listingPrice = listingPrice;
    }

/*    public String getListingSeller() {
        return listingSeller;
    }

    public void setListingSeller(String listingSeller) {
        this.listingSeller = listingSeller;
    }*/

    public LocalDate getListingPublished() {
        return listingPublished;
    }

    public void setListingPublished(LocalDate listingPublished) {
        this.listingPublished = listingPublished;
    }
}
