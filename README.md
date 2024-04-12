# Inflearn_Spring_Security

## Inflearn URL
https://www.inflearn.com/course/%EC%BD%94%EC%96%B4-%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0/dashboard


## Spring Security란?
Spring Security는 인증, 권한 관리 그리고 데이터 보호 기능을 포함하여 웹 개발 과정에서 필수적인 사용자 관리 기능을 구현하는데 도움을 주는 Spring의 강력한 프레임워크입니다.  
일반적으로 개발 시 가장 먼저 작업하는 부분이 사용자 관리 부분으로 가볍게는 회원가입부터 로그인, 로그아웃, 세션 관리, 권한 관리까지 온라인 플랫폼에 맞춰 다양하게 작업되는 인가 & 보안 기능은 개발자에게 많은 시간을 요구하는데요.  
Spring 생태계 내에서 이러한 요구사항을 효과적으로 지원하기 위해 개발된 것이 Spring Security로 개발자들이 보안 관련 기능을 효율적이고 신속하게 구현할 수 있도록 도와줍니다.  
  
## Spring Security를 사용하는 이유
자바 개발자들이 보안 기능을 추가할 때 Spring Security 사용하는 이유는 Spring Security가 Spring의 생태계에서 보안에 필요한 기능들을 제공하기 때문입니다. Spring Security는 개발 구조가 Spring이라는 프레임워크 안에서 활용하기 적합한 구조로 설계되어 있어, 보안 기능을 추가할 때 활용하기 좋습니다.  
프레임워크를 사용하지 않고 코드를 직접 작성할 경우 Spring에서 추구하는 IoC/DI 패턴과 같은 확장 패턴을 염두 해서 인증/인가 부분을 직접 개발하기는 쉽지 않은데, Spring Security에서는 이와 같은 기능들을 제공해 주기 때문에 개발 작업 효율을 높일 수 있습니다.  
때문에 많은 개발자들이 Spring을 사용할 경우에는 Spring Security를 활용하여 보안 기능을 추가하고 있으며, Spring Security에서 제공하는 기능 외에 추가적인 기능이 필요할 경우 Spring Security를 베이스로 기능을 추가하여 업무 효율을 높이고 있습니다.  
  
## Spring Security 아키텍처
위의 그림은 Spring에서 프레임워크나 라이브러리를 사용했을 때, 작동되는 구조를 표현한 그림입니다. 위의 그림을 보시면 붉은색 박스 부분이 Spring 프레임워크에서 Spring Security가 적용되는 부분입니다. 부담되지 않는 수준에서 심플하게 표현하였습니다.  
Spring은 자바의 프레임워크답게 개발 구조가 잘 설계되어 있기  때문에 Spring 생태계에서 제공하는 라이브러리라면 기존의 코드를 변경하지 않고도 그림과 같이 중간에 삽입하여 사용할 수 있습니다.   
  
1. 사용자의 요청이 서버로 들어옵니다.  
2. Authotication Filter가 요청을 가로채고 Authotication Manger로 요청을 위임합니다.  
3. Authotication Manager는 등록된 Authotication Provider를 조회하며 인증을 요구합니다.  
4. Authotication Provider가 실제 데이터를 조회하여 UserDetails 결과를 돌려줍니다.  
5. 결과는 SecurityContextHolder에 저장이 되어 저장된 유저정보를 Spring Controller에서 사용할 수 있게 됩니다.  
  
## Spring Security가 작동하는 내부 구조
1. 사용자가 자격 증명 정보를 제출하면, AbstractAuthenticationProcessingFilter가 Authentication 객체를 생성합니다.  
2. Authentication 객체가 AuthenticationManager에게 전달됩니다.  
3. 인증에 실패하면, 로그인 된 유저정보가 저장된 SecurityContextHolder의 값이 지워지고 RememberMeService.joinFail()이 실행됩니다. 그리고 AuthenticationFailureHandler가 실행됩니다.  
4. 인증에 성공하면, SessionAuthenticationStrategy가 새로운 로그인이 되었음을 알리고, Authentication 이 SecurityContextHolder에 저장됩니다. 이후에 SecurityContextPersistenceFilter가 SecurityContext를 HttpSession에 저장하면서 로그인 세션 정보가 저장됩니다.  
그 뒤로 RememberMeServices.loginSuccess()가 실행됩니다. ApplicationEventPublisher가 InteractiveAuthenticationSuccessEvent를 발생시키고 AuthenticationSuccessHandler 가 실행됩니다.  
  
Spring Security을 적용하기 전에 이런 흐름을 가지고 작동한다는 것을 알아두면 도움이 됩니다. 사실 아키텍처나 처리 과정들은 구글링을 통하여 가볍게 구현 방법을 알 수 있기 때문에 이러한 과정들을 몰라도 실무에서 구현하는 데는 문제가 전혀 없습니다.  
하지만 이러한 과정을 이해하고 있을 경우 Spring Security를 상황에 맞춰 적용하기 수월합니다. Spring Security의 작동 구조를 알고 있기 때문에, 작업을 빠르게 진행할 수 있고, 업무 효율도 더 올릴 수 있습니다.  
때문에 Spring Security 아키텍처와 처리 과정을 이 글을 통해 꼭 한 번 상기해 보시는 것을 추천드리며, 부족한 부분은 실무에서 직접 경험하면서 채워나간다면, Spring Security를 다양한 환경에 적용할 수 있을 것입니다.  
