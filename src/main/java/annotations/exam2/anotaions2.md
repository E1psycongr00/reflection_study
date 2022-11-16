# 패키지 목록을 자동 탐색하고 annotation에 등록된 메서드 실행하기


## 요구 사항
1. @InitializeMethod는 메서드에만 붙일 수 있고 메서드를 초기화합니다.
2. 현재 Main 경로 패키지를 기준(상대 경로)으로 패키지 하위 클래스 목록에 대해서 annotation을 탐색합니다.


## 추가 요구사항 1
1. @ScanPackages 어노테이션을 활용한다
   1. 해당 어노테임션은 여러 패키지를 문자열 형태로 받는다.
2. initialize 구현 메서드에 패키지 정보 입력이 아닌 어노테이션 정보를 입력받아서 처리한다.

## 추가 요구사항 2
1. @RetryOperation 어노테이션을 활용한다.
   1. RetryOpetration은 다음과 같이 주어진다.
   ```java
   @RetryOperation(
            numberOfRetries = 10,
            retryException = IOException.class,
            durationBetweenRetries = 1000,
            failureMessage = "Connection to database 1 failed after retries"
    )
   ```
2. DatabaseConnection 메서드 중 IOException을 3번 발생시키는 메서드를 계속해서 재시도하는 어노
테이션을 구현한다.
   1. retryException이 일치하지 않으면 재시도 하지 않고 예외를 발생시킨다.
   2. retryException이 정확히 일치하는 경우 예외처리하고 재시도를 한다.
   3. 재시도는 numberOfRetries 설정만큼 시도할 수 있다.