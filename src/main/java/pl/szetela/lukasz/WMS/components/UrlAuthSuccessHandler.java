package pl.szetela.lukasz.WMS.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pl.szetela.lukasz.WMS.dao.UserDao;
import pl.szetela.lukasz.WMS.models.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@Component
public class UrlAuthSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private static final Map<String, Supplier<String>> REDIRECT_SUPPLIER;
    private UserDao userDao;

    static {
        final Map<String, Supplier<String>> redirects = new HashMap<>();
        redirects.put("Warehouseman", () -> "/");
        redirects.put("Admin", () -> "/");
        redirects.put("Customer", () -> "/products");
        redirects.put("Office", () -> "/");
        REDIRECT_SUPPLIER = Collections.unmodifiableMap(redirects);
    }

    @Autowired
    public UrlAuthSuccessHandler(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication) throws IOException {
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
        String name = authentication.getName();
        User user = userDao.findUserByUsername(name);
        request.getSession().setAttribute("u_id", user.getId());
        request.getSession().setAttribute("u_role", user.getRole().getName());
    }

    protected void handle(HttpServletRequest request,
                          HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(authentication);
        if (response.isCommitted()) {
            return;
        }
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(Authentication authentication) {
        Supplier<String> redirect = null;
        Optional<? extends GrantedAuthority> optionalGrantedAuthority = authentication.getAuthorities().stream().findFirst();
        if (optionalGrantedAuthority.isPresent()) {
            redirect = REDIRECT_SUPPLIER.get(optionalGrantedAuthority.get().getAuthority());
        }
        if (redirect == null) {
            throw new IllegalStateException();
        }
       return redirect.get();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
