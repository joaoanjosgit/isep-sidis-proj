package com.example.psoft_22_23_project.subscriptionsmanagement.services;

import com.example.psoft_22_23_project.subscriptionsmanagement.model.SubscriptionDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;

import java.util.List;

public interface SubscriptionsService {


    void saveCreatedSubsRabbit(Subscriptions obj);

    void saveCanceledSubsRabbit(Subscriptions obj, long version);

    void saveRenewedSubsRabbit(Subscriptions obj, long version);

    void saveChangedSubsRabbit(Subscriptions obj, long version);

    List<Subscriptions> bootstrapSubscriptions();


}
