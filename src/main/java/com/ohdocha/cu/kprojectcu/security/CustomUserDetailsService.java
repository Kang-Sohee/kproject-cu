package com.ohdocha.cu.kprojectcu.security;

import com.ohdocha.cu.kprojectcu.domain.DochaUserInfoDto;
import com.ohdocha.cu.kprojectcu.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("userdetailservice")
public class CustomUserDetailsService implements UserDetailsService {
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="userInfo")
	private UserService userinfoService;

	@Override
	public DochaUserInfoDto loadUserByUsername(String username) throws UsernameNotFoundException {
		
		System.out.println("loadUserByUsername 시작");
		
		DochaUserInfoDto responseDto = new DochaUserInfoDto();
		DochaUserInfoDto paramDto = new DochaUserInfoDto();
		List<GrantedAuthority> grantList = new ArrayList<>();
				
		System.out.println("loadUserByUsername 시작 : " + username);
		paramDto.setUserId(username);

		try {
			responseDto = userinfoService.selectUserInfo(paramDto);
		} catch (Exception e) {
			logger.debug("loadUserByUsername selectUserInfo :::::::: fail!");
			throw new BadCredentialsException(username);		
		}

		if(responseDto !=null) {
			if(responseDto.getUserRole().equals("RA")) {
				grantList.add(new SimpleGrantedAuthority("RA"));
			} else if(responseDto.getUserRole().equals("CA")) {
				grantList.add(new SimpleGrantedAuthority("CA"));
			}
		} else {
			logger.debug("loadUserByUsername responseDto is null");
			throw new BadCredentialsException(username);
		}
		
		UserDetails userDetails = (UserDetails) new User(responseDto.getUserId(), responseDto.getUserPassword(), grantList);
		
		return responseDto;
	}
}
