/*
 * Copyright (c) 2022-2022 the original author or authors.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.example.psoft_22_23_project.plansmanagement.services;


import com.example.psoft_22_23_project.exceptions.ConflictException;
import com.example.psoft_22_23_project.exceptions.NotFoundException;
import com.example.psoft_22_23_project.plansmanagement.api.CreatePlanRequest;

import com.example.psoft_22_23_project.plansmanagement.api.EditPlansRequest;
import com.example.psoft_22_23_project.plansmanagement.model.*;
import com.example.psoft_22_23_project.plansmanagement.repositories.PlansRepositoryBD;
import com.example.psoft_22_23_project.plansmanagement.services.consumer.CreatePlanJsonConsumer;
import com.example.psoft_22_23_project.plansmanagement.services.producer.CreatePlanJsonProducer;
import com.example.psoft_22_23_project.plansmanagement.services.producer.DeletePlanJsonProducer;
import com.example.psoft_22_23_project.plansmanagement.services.producer.UpdatePlanJsonProducer;
import com.example.psoft_22_23_project.plansmanagement.services.rpc.SubscriptionRabbitClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlansServiceImpl implements PlansService {

	private final PlansRepositoryBD plansRepository;

	private final CreatePlansMapper createPlansMapper;
	private static final Logger logger = LoggerFactory.getLogger(PlansServiceImpl.class);

	private final CreatePlanJsonProducer createPlanJsonProducer;

	private final UpdatePlanJsonProducer updatePlanJsonProducer;

	private final DeletePlanJsonProducer deletePlanJsonProducer;

	private final SubscriptionRabbitClient subscriptionRabbitClient;

	@Override
	public Plans create(final String request, final CreatePlanRequest resource) {
		String planName = resource.getName();

		// Check if a plan with the same name already exists in the repository
		if (plansRepository.findByName_Name(planName).isPresent()) {
			throw new IllegalArgumentException("Plan with name " + planName + " already exists!");
		}

		Plans obj = createPlansMapper.create(resource);

		plansRepository.save(obj);

		// Send message to RabbitMQ for other instances and save
		createPlanJsonProducer.sendMessage(obj);
		return obj;
	}

	@Transactional
	@Override
	public Plans createBonus(long userId, CreatePlanRequest resource) {
		String planName = resource.getName();

		// Check if a plan with the same name already exists in the repository
		if (plansRepository.findByName_Name(planName).isPresent()) {
			throw new IllegalArgumentException("Plan with name " + planName + " already exists!");
		}

		Plans obj = createPlansMapper.create(resource);
		obj.getIsBonus().setBonus(true);

		plansRepository.save(obj);
		// Send message to RabbitMQ for other instances
		createPlanJsonProducer.sendMessage(obj);

		final boolean response = subscriptionRabbitClient.sendGetCreateBonusSub(userId, obj);

		if (response){
			return obj;
		}else {
			throw new RuntimeException("Error creating Bonus plan");
		}
	}

	@Override
	public void saveCreatedPlanRabbit(Plans obj) {

		// Check if the plan already exists in the local database
		if (plansRepository.findByName_Name(obj.getName().getName()).isEmpty()) {
			// Plan doesn't exist locally, so create it
			plansRepository.save(obj);
			logger.info("Plan added to the local database.");
		} else {
			logger.warn("Plan already exists in the local database. No action taken.");
		}
	}


	@Override
	public Plans partialUpdate(final String request, final String name, final EditPlansRequest resource, final long desiredVersion) {
		// Check if the plan name exists
		Plans plans = plansRepository.findByName_Name(name)
				.orElseThrow(() -> new NotFoundException("Plan with name " + name + " doesn't exist!"));

		// Update the existing plan
		plans.updateData(desiredVersion, resource.getDescription(),
				resource.getMaximumNumberOfUsers(), resource.getNumberOfMinutes(),
				resource.getMusicCollection(), resource.getMusicSuggestion(), resource.getActive(), resource.getPromoted());

		plansRepository.save(plans);

		// Publish the updated plan to a message queue and save
		updatePlanJsonProducer.sendMessage(plans);
		return plans;
	}

	@Override
	public void saveUpdatedPlanRabbit(Plans obj, long version) {

		try {
			Plans existingPlan = plansRepository.findByName_Name(obj.getName().getName()).orElse(null);

			if (existingPlan != null && existingPlan.getVersion() != version) {

				existingPlan.updateData(existingPlan.getVersion(), obj.getDescription().getDescription(),
						obj.getMaximumNumberOfUsers().getMaximumNumberOfUsers(), obj.getNumberOfMinutes().getNumberOfMinutes(),
						obj.getMusicCollection().getMusicCollection(), obj.getMusicSuggestion().getMusicSuggestion(),
						obj.getActive().getActive(), obj.getPromoted().getPromoted());

				plansRepository.save(existingPlan);
				logger.info("Plan updated to the local database.");
			} else {
				logger.warn("Received plan has an older version or already updated in the local database. No action taken.");
			}
		} catch (Exception e) {
			logger.error("Error processing received message: {}", e.getMessage(), e);
			// Handle the exception as needed, possibly retry or log an error.
		}
	}

	@Override
	public Plans deactivate(final String request, final String name, final long desiredVersion) {
		Plans plans = plansRepository.findByName_Name(name)
				.orElseThrow(() -> new NotFoundException("Plan with name " + name + " doesn't exist!"));

		if (!plans.getActive().getActive()) {
			throw new ConflictException("Plan with name " + name + " is already deactivated");
		}

		plans.deactivate(desiredVersion);

		plansRepository.save(plans);

		// Publish the deleted plan to a message queue and save
		deletePlanJsonProducer.sendMessage(plans);
		return plans;
	}

	@Override
	public void saveDeletedPlanRabbit(Plans obj, long version) {

		try {
			Plans existingPlan = plansRepository.findByName_Name(obj.getName().getName()).orElse(null);

			if (existingPlan != null && existingPlan.getVersion() != version) {

				existingPlan.deactivate(existingPlan.getVersion());

				plansRepository.save(existingPlan);
				logger.info("Plan deleted in the local database.");
			} else {
				logger.warn("Received plan has an older version or already deleted in the local database. No action taken.");
			}
		} catch (Exception e) {
			logger.error("Error processing received message: {}", e.getMessage(), e);
			// Handle the exception as needed, possibly retry or log an error.
		}
	}
}
