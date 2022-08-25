package com.project.cts.processpensionservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.project.cts.processpensionservice.exception.InvalidTokenException;
import com.project.cts.processpensionservice.feign.AuthenticationProxy;
import com.project.cts.processpensionservice.feign.PensionerDetailProxy;
import com.project.cts.processpensionservice.model.PensionDetail;
import com.project.cts.processpensionservice.model.PensionerDetailOutput;
import com.project.cts.processpensionservice.model.ProcessPensionInput;
import com.project.cts.processpensionservice.service.ProcessPensionService;

@RestController
public class ProcessPensionController {

	@Autowired
	ProcessPensionService processPensionService;

	@Autowired
	PensionerDetailProxy pensionerDetailProxy;
	@Autowired
	private AuthenticationProxy authenticationProxy;

	@PostMapping("ProcessPension")
	public PensionDetail processPension(@RequestHeader(name = "Authorization") String token,
			@RequestBody ProcessPensionInput input) {

		try {
			authenticationProxy.validateToken(token);

		} catch (Exception ex) {
			throw new InvalidTokenException("You are not allowed to access this resource!");
		}
		

		
		PensionerDetailOutput pensionerDetailOutput = pensionerDetailProxy.pensionerDetail(token, input.getAadharNo());
		return processPensionService.getPensionAmount(pensionerDetailOutput);

	}



}
