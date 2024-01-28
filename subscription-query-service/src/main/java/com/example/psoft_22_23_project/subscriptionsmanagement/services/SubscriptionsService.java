package com.example.psoft_22_23_project.subscriptionsmanagement.services;

import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubscriptionsRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.SubscriptionDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;

public interface SubscriptionsService {
    /*
    Iterable<Subscriptions> findAll();

    Iterable<Subscriptions> findAllSubs();
    Subscriptions checkSub(String user);

    Subscriptions create(CreateSubscriptionsRequest resource, String token);

    //void delete(Long id);

    Subscriptions cancelSubscription(long desiredVersion, String authorization);

    Subscriptions cancelExternalSubscription(long desiredVersion);


    Subscriptions renewAnualSubscription(long desiredVersion, String authorization);

    Subscriptions renewExternalSubscription(long desiredVersion);

    Subscriptions changePlan(long desiredVersion,String authorization, String name);

    Subscriptions changeExternalPlan(long desiredVersion, String authorization, String name);
*/
    //void migrateAllToPlan(long desiredVersion,String actualPlan, String newPlan);
    Plans planDetails();


    void saveCreatedSubsRabbit(Subscriptions obj);

    void saveCanceledSubsRabbit(Subscriptions obj, long version);

    void saveRenewedSubsRabbit(Subscriptions obj, long version);

    void saveChangedSubsRabbit(Subscriptions obj, long version);

}
