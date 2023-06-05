package kr.co.fns.app.core.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.fns.app.base.dto.ApiResult;
import kr.co.fns.app.base.dto.GeneralResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request,response);
        } catch (Exception e) {
            setFilterErrorResponse(HttpStatus.UNAUTHORIZED, response, e, filterChain);
        }
    }


    public void setFilterErrorResponse(HttpStatus status, HttpServletResponse response, Throwable e, FilterChain filterChain) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            GeneralResponse res = new GeneralResponse();
            res.setCode(String.valueOf(status.value()));
            res.setMsg("Unauthorized (Token authentication failed)");

            response.getWriter().write(objectMapper.writeValueAsString(res));

        } catch (IOException ex) {
            // TODO 수정필요
//            ex.printStackTrace();
        }
    }
}
