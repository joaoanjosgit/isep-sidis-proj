package com.example.psoft_22_23_project.plansmanagement.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.StaleObjectStateException;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "Plans")
@SQLDelete(sql = "UPDATE Plans SET Is_Deleted = true WHERE id=?")
@Where(clause = "Is_Deleted=false")
@Getter
public class Plans {

    public Plans(Name name,
                 Description description,
                 NumberOfMinutes numberOfMinutes,
                 MaximumNumberOfUsers maximumNumberOfUsers,
                 MusicCollection musicCollection,
                 MusicSuggestion musicSuggestion,
                 AnnualFee annualFee,
                 MonthlyFee monthlyFee,
                 Active active,
                 Promoted promoted) {
        this.name = name;
        this.description= description;
        this.numberOfMinutes = numberOfMinutes;
        this.maximumNumberOfUsers = maximumNumberOfUsers;
        this.musicCollection = musicCollection;
        this.musicSuggestion = musicSuggestion;
        this.annualFee = annualFee;
        this.monthlyFee = monthlyFee;
        this.active = active;
        this.promoted = promoted;
        this.deleted = new Deleted();
        this.isBonus = new IsBonus();
    }

    protected  Plans(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private long version;
    @Embedded
    private Name name;
    @Embedded
    private Description description;
    @Embedded
    private NumberOfMinutes numberOfMinutes;
    @Embedded
    private MaximumNumberOfUsers maximumNumberOfUsers;
    @Embedded
    private MusicCollection musicCollection;
    @Embedded
    private MusicSuggestion musicSuggestion;
    @Embedded
    private AnnualFee annualFee;
    @Embedded
    private MonthlyFee monthlyFee ;
    @Embedded
    private Active active;
    @Embedded
    private Promoted promoted;
    @Embedded
    private Deleted deleted;
    @Embedded
    private IsBonus isBonus;



//@OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
// private List<Subscriptions> subscriptions = new ArrayList<>();


    public void updateData(long desiredVersion, String description,
                           Integer maximumNumberOfUsers,
                           String numberOfMinutes , Integer musicCollections,
                           String musicSuggestions, Boolean active, Boolean promoted) {

        if (this.version != desiredVersion) {
            throw new StaleObjectStateException("Object was already modified by another user", this.id);
        }
        if(description != null){
            this.description.setDescription(description);
        }

        if(maximumNumberOfUsers != null){
            this.maximumNumberOfUsers.setMaximumNumberOfUsers(maximumNumberOfUsers);
        }

        if(musicCollections != null){
            this.musicCollection.setMusicCollection(musicCollections);
        }

        if(numberOfMinutes != null){
            this.numberOfMinutes.setNumberOfMinutes(numberOfMinutes);
        }

        if(musicSuggestions != null){
            this.musicSuggestion.setMusicSuggestion(musicSuggestions);
        }
        if(active != null){
            this.active.setActive(active);
        }
        if(promoted != null){
            this.promoted.setPromoted(promoted);
        }


    }


    public void deactivate(final long desiredVersion) {
        // check current version
        if (this.version != desiredVersion) {
            throw new StaleObjectStateException("Object was already modified by another user", this.id);
        }

        this.active.setActive(false);
    }

    public void promote(final long desiredVersion) {
        // check current version
        if (this.version != desiredVersion) {
            throw new StaleObjectStateException("Object was already modified by another user", this.id);
        }

        this.promoted.setPromoted(true);
    }


}


