package med.voll.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;

    @Override
    protected void doFilterInternal (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJWT = recuperarToken ( request );

        if (tokenJWT != null) {
            try {
                var subject = tokenService.getSubject ( tokenJWT );
                var usuario = repository.findByLogin ( subject );

                if (usuario != null) {
                    var authentication = new UsernamePasswordAuthenticationToken ( usuario, null, usuario.getAuthorities ( ) );
                    SecurityContextHolder.getContext ( ).setAuthentication ( authentication );
                } else{
                    System.out.println ( "Usuário não encontrado: " + subject );
                }
            } catch (RuntimeException e) {
                System.out.println ( "Erro na validação do token: " + e.getMessage ( ) );
            }
        } else{
            System.out.println ( "Token JWT não encontrado no cabeçalho da requisição." );
        }

        filterChain.doFilter ( request, response );
    }

    private String recuperarToken (HttpServletRequest request) {
        var authorizationHeader = request.getHeader ( "Authorization" );
        if (authorizationHeader != null && authorizationHeader.startsWith ( "Bearer " )) {
            String token = authorizationHeader.replace ( "Bearer ", "" );
            System.out.println ( "Token recuperado: " + token );
            return token;
        }
        System.out.println ( "Token JWT não encontrado no cabeçalho da requisição." );
        return null;
    }
}