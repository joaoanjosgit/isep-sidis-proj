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
package com.example.psoft_22_23_project.subscriptionsmanagement.api;

import com.example.psoft_22_23_project.subscriptionsmanagement.model.SubscriptionDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public abstract class SubscriptionDetailsViewMapper {
	@Mapping(source = "details.name", target = "name")
	@Mapping(source = "details.description", target = "description")
	@Mapping(source = "details.numberOfMinutes", target = "numberOfMinutes")
	@Mapping(source = "details.maximumNumberOfUsers", target = "maximumNumberOfUsers")
	@Mapping(source = "details.musicCollection", target = "musicCollection")
	@Mapping(source = "details.musicSuggestion", target = "musicSuggestion")
	@Mapping(source = "details.annualFee", target = "annualFee")
	@Mapping(source = "details.monthlyFee", target = "monthlyFee")
	@Mapping(source = "details.active", target = "active")
	@Mapping(source = "details.promoted", target = "promoted")
	public abstract SubscriptionDetailsView toSubscriptionDetailsView(SubscriptionDetails details);

}
