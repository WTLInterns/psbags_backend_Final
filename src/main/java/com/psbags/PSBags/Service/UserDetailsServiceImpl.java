package com.psbags.PSBags.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.psbags.PSBags.DTO.requests.SignupRequests;
import com.psbags.PSBags.DTO.response.SignupResponse;
import com.psbags.PSBags.Model.User;
import com.psbags.PSBags.Repo.UserRepo;



@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	
	@Autowired
	private UserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("==================== DEBUG START - LOAD USER BY USERNAME ====================");
		System.out.println("[USER DETAILS SERVICE] Loading user by username: " + username);
		
        User user = this.userRepo.findByEmail(username).orElseThrow();

        if (user != null) {
        	System.out.println("[USER DETAILS SERVICE] ✅ User found - ID: " + user.getId() + ", Email: " + user.getEmail());
        	System.out.println("[USER DETAILS SERVICE] User role: " + user.getRole());
        	System.out.println("[USER DETAILS SERVICE] User authorities: " + user.getAuthorities());
        	System.out.println("==================== DEBUG END - LOAD USER BY USERNAME ====================");
            return user;
        } else {
        	System.err.println("[USER DETAILS SERVICE] ❌ User not found for username: " + username);
        	System.out.println("==================== DEBUG END - LOAD USER BY USERNAME ====================");
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
	}


	// public List<User> getAllUserByRole(String email) {
	// 			User user = userRepo.findByEmail(username);
	// 			return userRepo.findAll();

	// }

	public SignupResponse showProfile(String email){
        User user = this.userRepo.findByEmail(email).orElseThrow();
		SignupResponse response = new SignupResponse();
		response.setId(user.getId());
		response.setEmail(user.getEmail());
		response.setPassword(user.getPassword());
		response.setFirstName(user.getFirstName());
		response.setLastName(user.getLastName());
		response.setPhoneNumber(user.getPhoneNumber());
		response.setRole(user.getRole());
		return response;
	}

	public SignupResponse updatePassword(String email, SignupRequests request) {
        User user = this.userRepo.findByEmail(email).orElseThrow();
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setEmail(request.getEmail());
    user.setPhoneNumber(request.getPhoneNumber());
    user.setPassword(request.getPassword());
    user.setRole(request.getRole());
    userRepo.save(user);

    SignupResponse response = new SignupResponse();
    response.setId(user.getId());
    response.setEmail(user.getEmail());
    response.setFirstName(user.getFirstName());
    response.setLastName(user.getLastName());

    return response; 
}














}
		
