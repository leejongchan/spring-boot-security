# Spring Security
**Client(Web 혹은 App)에서 Spring API Server에 인증을 요청할 경우 Login Form을 Response 해주는 것이 아닌
Client에서 인증 관련 데이터를 받아 인증을 처리하는 로직을 Spring Security를 분석하며 직접 구현해보았다.**

Spring Security Docs나 대부분의 참고 자료의 경우 LoginForm을 활용한 방법에 대해 설명하고 있어 코드를 작성하며 
문제나 의문이 드는 점은 Spring Security 관련 class나 filter들을 직접 분석하여 이해했습니다.

Spring Security, Code 그리고 Flow에 관련한 자세한 설명은 Code의 주석에서 확인할 수 있습니다.

# 분석한 내용
1. Spring Security 인증 Flow
  * AuthenticationManager 역할
  * ProviderManager (AuthenticationManager의 구현체)
  * Spring Security Filter Chain
2. Spring Security가 제공하는 LoginForm 기능에서 데이터를 전달받는 것이 아닌 Client가 전송한 Data를 이용한 인증 방법
3. Authentication Fail 시 처리 방법 (AuthenticationEntryPoint)
4. Authority Fail 시 처리 방법 (AuthorityHandler)
5. 이 외 정보들은 주석에서 확인 가능합니다.