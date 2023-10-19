# back-end
# 프로젝트 세팅 
1. env.properties와 application-database.propetie를 업데이트 하게 되면 github action secret에 똑같이 업데이트 해주어야 한다.
2. 개발할때에는 도커를 켜두고 개발한다. 웹서버 외에 필요한 프로그램들은 전부 컨테이너를 띄우고 개발하여야 한다.
3. main 브랜치에 푸시를 하게 되면 깃허브 액션을 통한 배포 업데이트가 실행되므로 feature 브랜치에 기능 구현을 완료하고 main 브랜치에 업데이트 하자
