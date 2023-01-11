package com.increff.pos.dto;

import com.increff.pos.model.data.InfoData;
import com.increff.pos.model.form.LoginForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.util.ApiException;
import com.increff.pos.service.UserService;
import com.increff.pos.util.SecurityUtil;
import com.increff.pos.util.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Objects;

@Configuration
@Service
public class LoginDto {

    @Autowired
    private UserService service;
    @Autowired
    private InfoData info;

    public ModelAndView login(HttpServletRequest req, LoginForm f) throws ApiException {
        UserPojo p = service.get(f.getEmail());
        boolean authenticated = (!Objects.isNull(p) && Objects.equals(p.getPassword(), f.getPassword()));
        if (!authenticated) {
            info.setMessage("Invalid username or password");
            return new ModelAndView("redirect:/site/login");
        }

        // Create authentication object
        Authentication authentication = convert(p);
        // Create new session
        HttpSession session = req.getSession(true);
        // Attach Spring SecurityContext to this new session
        SecurityUtil.createContext(session);
        // Attach Authentication object to the Security Context
        SecurityUtil.setAuthentication(authentication);

        return new ModelAndView("redirect:/ui/home");
    }

    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        return new ModelAndView("redirect:/site/logout");
    }

    //Conversion Methods
    private static Authentication convert(UserPojo p) {
        // Create principal
        UserPrincipal principal = new UserPrincipal();
        principal.setEmail(p.getEmail());
        principal.setId(p.getId());

        // Create Authorities
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(p.getRole()));
        // you can add more roles if required

        // Create Authentication
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, null,
                authorities);
        return token;
    }
}
