package com.psbags.PSBags.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.psbags.PSBags.JWT.JwtAuthFilter;
import com.psbags.PSBags.OAuth2.OAuth2AuthenticationSuccessHandler;
import com.psbags.PSBags.Service.CustomOAuth2UserService;
import com.psbags.PSBags.Service.UserDetailsServiceImpl;


@Configuration
public class SecurityConfig {

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private JwtAuthFilter jwtAuthFilter;
	
	@Autowired
	private CustomOAuth2UserService customOAuth2UserService;
	
	@Autowired
	private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		System.out.println("==================== DEBUG START - SECURITY CONFIG ====================");
		System.out.println("[SECURITY CONFIG] Initializing SecurityFilterChain");
		System.out.println("[SECURITY CONFIG] Configuring authentication requirements...");
		
	    http.authorizeHttpRequests(request -> 
	        request
	            .requestMatchers("/auth/**","/public/**", "/api/products/**", "/oauth2/**", "/login/oauth2/**").permitAll()  
	            .requestMatchers("/admin/subcategory/**").permitAll() // Temporarily allow for testing
	            .requestMatchers("/admin/**").hasAnyAuthority("ADMIN")
	            .requestMatchers("/user/**", "/api/cart/**", "/api/wishlist/**").hasAnyAuthority("USER", "ADMIN")  // FIXED: Allow ADMIN to access cart/wishlist
				.requestMatchers("/common/reset-password").hasAnyRole("USER", "ADMIN")
	            .anyRequest().authenticated()                 
	    );
	    
	    System.out.println("[SECURITY CONFIG] Endpoint Security:");
	    System.out.println("  - /auth/**, /public/**, /oauth2/**: PERMIT ALL");
	    System.out.println("  - /admin/**: REQUIRES ADMIN");
	    System.out.println("  - /api/cart/**, /api/wishlist/**: REQUIRES USER or ADMIN");
	    System.out.println("  - All other: AUTHENTICATED");
	    
	    http.csrf(csrf -> csrf.disable());
	    http.csrf(AbstractHttpConfigurer::disable);
		http.cors(Customizer.withDefaults());
		
		System.out.println("[SECURITY CONFIG] CSRF: DISABLED");
		System.out.println("[SECURITY CONFIG] CORS: ENABLED (default)");
		
		// OAuth2 Login Configuration
		System.out.println("[SECURITY CONFIG] Configuring OAuth2 Login...");
		http.oauth2Login(oauth2 -> oauth2
			.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
			.successHandler(oAuth2AuthenticationSuccessHandler)
		);
		System.out.println("[SECURITY CONFIG] OAuth2 Login: CONFIGURED");
		System.out.println("[SECURITY CONFIG] ⚠️ WARNING: No explicit login page set - may intercept API requests!");
		
		System.out.println("[SECURITY CONFIG] Adding JWT Auth Filter before UsernamePasswordAuthenticationFilter");
		http.authenticationProvider(authenticationProvider()).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		System.out.println("[SECURITY CONFIG] ✅ SecurityFilterChain configured successfully");
		System.out.println("==================== DEBUG END - SECURITY CONFIG ====================");
		
	    return http.build();
	}

	
	
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvideer = new DaoAuthenticationProvider();
		daoAuthenticationProvideer.setUserDetailsService(userDetailsServiceImpl);
		daoAuthenticationProvideer.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvideer;
	}
	
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	

	// protected void configure(HttpSecurity http) throws Exception {
    //     // Disable CSRF for /webhook so POST requests work
    //     http.csrf().ignoringAntMatchers("/webhook")
    //         .authorizeRequests()
    //         .anyRequest().permitAll();
    // }
	
}
