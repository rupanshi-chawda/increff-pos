package com.increff.pos.dto;

import com.increff.pos.model.data.InfoData;
import com.increff.pos.util.SecurityUtil;
import com.increff.pos.util.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

@Configuration
@Service
public class AbstractUiDto {

    @Autowired
    private InfoData info;

    @Value("${app.baseUrl}")
    private String baseUrl;

    public ModelAndView mav(String page) {
        // Get current user
        UserPrincipal principal = SecurityUtil.getPrincipal();
        info.setEmail(Objects.isNull(principal) ? "" : principal.getEmail());

        // Set info
        ModelAndView mav = new ModelAndView(page);
        mav.addObject("info", info);
        mav.addObject("baseUrl", baseUrl);
        return mav;
    }
}