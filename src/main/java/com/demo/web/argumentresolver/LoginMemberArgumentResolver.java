package com.demo.web.argumentresolver;

import com.demo.domain.member.Member;
import com.demo.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    // supportsParameter 메소드가 true 를 반환하면 resolveArgument 메소드가 자동으로 실행
    // supportsParameter 메소드는 해당 컨트롤러 메소드의 파라미터가 해당 리졸버(Resolver)가 지원하는 파라미터인지 검사하는 역할
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 실행");

        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        // parameter.getParameterType()에 지정된 클래스가 Member 클래스 또는 Member 클래스의 하위 클래스인지 확인합니다.
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginAnnotation && hasMemberType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        log.info("resolveArgument 실행");

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }

        return session.getAttribute(SessionConst.LOGIN_MEMBER);
    }
}
/* NativeWebRequest 는 Spring Web Framework 에서 제공하는 인터페이스로, HttpServletRequest 와 HttpServletResponse 를 추상화한 것입니다.
NativeWebRequest 인터페이스는 요청(request)과 응답(response)에 대한 정보를 제공하며, 이를 이용하여 웹 요청 처리에 필요한 다양한 기능을 수행할 수 있습니다.
NativeWebRequest 는 HttpServletRequest 와 HttpServletResponse 의 기능을 모두 제공하면서,
Spring 의 IoC 기능과 연동하여 다양한 서비스를 사용할 수 있도록 합니다.
이를 통해 HttpServletRequest 나 HttpServletResponse 와 같은 네이티브(native) 객체를 직접 다루는 것보다 더욱 유연하게 웹 요청 처리를 할 수 있습니다.
* */