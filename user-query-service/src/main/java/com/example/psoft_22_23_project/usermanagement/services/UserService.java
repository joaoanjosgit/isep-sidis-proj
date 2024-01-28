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
package com.example.psoft_22_23_project.usermanagement.services;

import com.example.psoft_22_23_project.exceptions.NotFoundException;
import com.example.psoft_22_23_project.filestoragemanagement.service.FileStorageService;
import com.example.psoft_22_23_project.rabbit.producer.GetUserJsonProducer;
import com.example.psoft_22_23_project.usermanagement.model.User;
import com.example.psoft_22_23_project.usermanagement.repositories.UserImageRepository;
import com.example.psoft_22_23_project.usermanagement.repositories.UserRepositoryBD;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final UserRepositoryBD repository;

	private final GetUserJsonProducer getUserJsonProducer;

	private final UserImageRepository userImageRepository;
	private final FileStorageService fileStorageService;
	private final PasswordEncoder encoder;


	public User findUser(Long id){
		return repository.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

	public User findUserByUserName(String userName){

		User user = repository.findByUsername(userName)
				.orElseThrow(() -> new NotFoundException("User not found"));
		getUserJsonProducer.sendMessage(user);
		return user;

	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return null;
	}
/*
    public User upload(MultipartFile file) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		int commaIndex = username.indexOf(",");
		String newString;
		if (commaIndex != -1) {
			newString = username.substring(0, commaIndex);
		} else {
			newString = username;
		}

		User user = userRepository.findById(Long.valueOf(newString))
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		UserImage userImage = null;

		if (file != null) {
			final String fileName = fileStorageService.storeFile(user.getUsername(), file);

			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentRequestUri().pathSegment(fileName)
					.toUriString();
			fileDownloadUri = fileDownloadUri.replace("/photos/", "/photo/");

			userImage = new UserImage(Utils.transformSpaces(user.getUsername()), fileName, fileDownloadUri, file.getContentType(), file.getSize());
			user.setUserImage(userImage);
			userImageRepository.save(userImage);
			userRepository.save(user);
		}

		return user;

	}

	public Resource seeImage() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		int commaIndex = username.indexOf(",");
		String newString;
		if (commaIndex != -1) {
			newString = username.substring(0, commaIndex);
		} else {
			newString = username;
		}

		User user = userRepository.findById(Long.valueOf(newString))
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		final Resource resource = fileStorageService.loadFileAsResource(user.getUserImage().getFileName());
		return resource;

	}
	*/

}
