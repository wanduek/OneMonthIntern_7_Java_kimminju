# 플랫폼을 x86_64로 지정
FROM --platform=linux/amd64 openjdk:17-jdk-slim

# 애플리케이션 디렉토리 생성
WORKDIR /app

# JAR 파일 복사
COPY build/libs/OneMonth_7th_intern-0.0.1-SNAPSHOT.jar app.jar

# 애플리케이션 실행
CMD ["java", "-jar", "app.jar"]