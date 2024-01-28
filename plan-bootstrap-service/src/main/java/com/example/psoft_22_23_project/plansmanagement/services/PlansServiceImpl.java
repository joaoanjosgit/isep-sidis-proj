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


import com.example.psoft_22_23_project.plansmanagement.model.*;
import com.example.psoft_22_23_project.plansmanagement.repositories.PlansRepositoryBD;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlansServiceImpl implements PlansService {

	private final PlansRepositoryBD plansRepository;

	private final CreatePlansMapper createPlansMapper;
	private static final Logger logger = LoggerFactory.getLogger(PlansServiceImpl.class);

	@Override
	public void saveCreatedPlanRabbit(PlanDTO dto) {

		Plans obj = createPlansMapper.createPlanFromDTO(dto);

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
	public void saveUpdatedPlanRabbit(PlanDTO dto) {

		Plans obj = createPlansMapper.createPlanFromDTO(dto);

		try {
			Plans existingPlan = plansRepository.findByName_Name(obj.getName().getName()).orElse(null);

			if (existingPlan != null && existingPlan.getVersion() != Long.parseLong(dto.getVersion())) {

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
	public void saveDeletedPlanRabbit(PlanDTO dto) {

		Plans obj = createPlansMapper.createPlanFromDTO(dto);

		try {
			Plans existingPlan = plansRepository.findByName_Name(obj.getName().getName()).orElse(null);

			if (existingPlan != null && existingPlan.getVersion() != Long.parseLong(dto.getVersion())) {

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

	@Override
	public List<Plans> bootstrapPlans() {
		return plansRepository.findAll();
	}
}
