# 1. 사용할 베이스 이미지 (자바 17 환경)
FROM amazoncorretto:17-alpine-jdk

# 2. 빌드된 JAR 파일을 이미지 내부로 복사
# Gradle 빌드 시 생성되는 jar 파일의 위치를 지정합니다.
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 3. 컨테이너가 실행될 때 자바 앱 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]