package com.example.psoft_22_23_project.subscriptionsmanagement.model;



import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import com.example.psoft_22_23_project.usermanagement.model.User;
import lombok.Getter;
import org.hibernate.StaleObjectStateException;

import javax.persistence.*;


@Entity
@Getter
@Table(name = "Subscriptions")
public class Subscriptions {

    protected Subscriptions(){

    }


    public Subscriptions(PaymentType paymentType, Plans plan, User user){
        this.startDate = new StartDate();
        this.endDate = new EndDate(paymentType.getPaymentType());
        this.activeStatus = new ActiveStatus(true);
        this.paymentType = paymentType;
        this.plan = plan;
        this.user = user;
        this.isBonus = new IsBonus();


    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private long version;

    @Embedded
    private StartDate startDate;

    @Embedded
    private EndDate endDate;

    @Embedded
    private ActiveStatus activeStatus;

    @Embedded
    private PaymentType paymentType;

    @ManyToOne(fetch = FetchType.EAGER)
    private Plans plan;

    @OneToOne(fetch = FetchType.EAGER)
    private User user;

    @Embedded
    private IsBonus isBonus;


    public void deactivate(final long desiredVersion) {
        // check current version
        if (this.version != desiredVersion) {
            throw new StaleObjectStateException("Object was already modified by another user", this.id);
        }
        // apply patch only if the field was sent in the request. we do not allow to
        // change the name attribute so we simply ignore it
        this.activeStatus.setActive(false);
    }


    public void checkChange(final long desiredVersion) {
        // check current version
        if (this.version != desiredVersion) {
            throw new StaleObjectStateException("Object was already modified by another user", this.id);
        }
    }

    public void changePlan(final long desiredVersion, final Plans plan) {
        // check current version
        if (this.version != desiredVersion) {
            throw new StaleObjectStateException("Object was already modified by another user", this.id);
        }

        this.plan = plan;
    }

}
