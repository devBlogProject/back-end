# back-end
# 프로젝트 세팅 
* env.properties와 application-database.propetie를 업데이트 하게 되면 github action secret에 똑같이 업데이트 해주어야 한다.
* 개발할때에는 도커를 켜두고 개발한다. 웹서버 외에 필요한 프로그램들은 전부 컨테이너를 띄우고 개발하여야 한다.
* main 브랜치에 푸시를 하게 되면 깃허브 액션을 통한 배포 업데이트가 실행되므로 feature 브랜치에 기능 구현을 완료하고 main 브랜치에 업데이트 하자
* 깃 훅 설치
    * <code> ./gradlew installGitHook </code> 을 실행하여 깃 훅을 컴퓨터에 설치한다.
    * 그럼 커밋을 실행하면 커밋 전에 테스트를 실행하게 되고 테스트가 성공해야지 커밋을 할 수 있다.
    * 테스트가 실패하게 되면 커밋은 취소된다. (테스트를 꼼꼼히 하고 커밋하자)