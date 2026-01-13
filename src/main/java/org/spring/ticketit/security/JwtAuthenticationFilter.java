package org.spring.ticketit.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority; // ADDED: For JWT authorities
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String username = jwtService.extractUsername(jwt);

        // Proceed if username is present and not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(jwt, userDetails)) {

                // 🟢 ADDED: Extract the user's role from the JWT "role" claim
                String role = jwtService.extractClaim(jwt, claims -> claims.get("role", String.class));

                // 🟢 ADDED: Wrap the role as a GrantedAuthority (Spring expects this format)
                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

                // 🟢 CHANGED: Use the above authorities (from JWT),
                // NOT userDetails.getAuthorities() from the DB
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        authorities           // Now authorities match JWT "role"!
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}

/*
-------------------------------
🟢 WHAT HAS BEEN ADDED/CHANGED?
-------------------------------
- Extract the "role" claim from the JWT payload (e.g., "ROLE_AGENT").
- Build a Spring authority list from that claim (List<SimpleGrantedAuthority>).
- Inject the authority list into the Authentication *instead of* relying solely on whatever is in your database's UserDetails object.

-------------------------------
🟢 WHY IS THIS NEEDED?
-------------------------------
- Spring Security enforces roles/permissions for @PreAuthorize and endpoint rules
  *only* by looking at the authorities list on the Authentication found in the SecurityContext.
- By putting the role from the JWT directly into authorities, your API respects the user's actual login role (even if your DB is out-of-sync).
- Fixes problems where you kept getting 403 Forbidden responses even though your JWT
  had "ROLE_AGENT"—now @PreAuthorize("hasAnyRole('AGENT','ADMIN')") will work!

-------------------------------
🟢 TL;DR (What to do)
-------------------------------
- Add the 2 marked lines and change the argument.
- Your JWT-based role authorization will start working for AGENT/ADMIN protected endpoints.
*/
