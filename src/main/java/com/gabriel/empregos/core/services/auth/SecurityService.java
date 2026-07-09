package com.gabriel.empregos.core.services.auth;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.gabriel.empregos.core.models.User;
import com.gabriel.empregos.core.repositories.JobRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final JobRepository jobRepository;

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public boolean isAuthenticated() {
        var authentication = getAuthentication();
        return authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated();
    }

    public User getCurrentUser() {
        if (!isAuthenticated()) {
            throw new InsufficientAuthenticationException("Não ha usuario logado");
        }

        var authentication = (AuthenticatedUser) getAuthentication();
        return authentication.getUser();
    }

    public boolean isOwner(String companyEmail, Long jobId) {
        return jobRepository.existsByCompanyEmailAndId(companyEmail, jobId);
    }
}
