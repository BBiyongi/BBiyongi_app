# <img src="https://github.com/BBiyongi/BBiyongi_app/assets/84698896/6388bf06-5d03-4daa-ba96-f8c76179a91b" width="30" height="30"/>응급 사고 예방 서비스 BBiyongi

CCTV 모션 인식을 통한 심정지 사망 및 범죄 예방 안드로이드 애플리케이션

## 📷 시연 영상

시연 영상 링크 올려야 함

## <img src="https://github.com/BBiyongi/BBiyongi_app/assets/84698896/6388bf06-5d03-4daa-ba96-f8c76179a91b" width="30" height="30"/> 소개

어린이집, 요양원 등에서 폭행 사고가 발생하는 사례를 많이 접할 수 있습니다. 그러나 사건이 발생하였으나 보호자가 해당 사실을 즉시 인식하지 못해 대처가 늦어지는 경우가 많습니다. CCTV가 설치되어 있더라도 관계자의 영상 삭제, CCTV 고장, 권한 문제 등으로 사건이 감지된 증거 자료를 얻는 것이 어렵습니다. 또한, 뉴스 기사로 심정지가 발생했을 때 목격자의 즉각적인 대응으로 생명을 구하는 경우도 쉽게 접할 수 있습니다. 심정지 사고가 발생하면 골든타임 내에 심폐소생술을 실시해야 심정지 환자를 살릴 확률이 높아집니다. 그러나 보행자가 적은 곳에서 심정지 사고가 발생할 경우 주위 사람이 해당 사고를 파악하여 대처하는 것에 어려움이 있습니다.

BBiyongi는 기기를 사용자의 관할 위치나 보호 대상이 생활하는 장소에 설치하여 사용자가 언제 어디서든지 폭행 또는 심정지 사건을 감지하고 긴급 신고 또는 보호자에게 연락을 취함으로써 범죄를 파악하고 예방하는 시스템입니다. 또한, 서울 열린데이터광장의 AED 위치 공공데이터를 활용하여 심정지 사건이 발생한 주소에서 가장 가까운 AED 위치를 스피커로 제공하여 주변 사람들이 음성을 듣고 심정지 환자에게 알맞은 대처를 할 수 있도록 하였습니다. 이를 위해 인공지능으로 심정지, 폭행 행위를 학습하고 이를 DB를 통해 애플리케이션으로 전달하여 사용자가 원하는 방향으로 사고를 대처할 수 있도록 합니다.

가정폭력이나 아동폭력, 학교폭력은 남에게 알리거나 도움을 청하는 것이 힘든 경우가 많습니다. 또한 유치원이나 어린이집에서 발생하는 아동폭행, 양로원이나 요양원에서 발생하는 노인폭행의 경우는 보호자가 이를 알아차리기 어렵습니다. BBiyongi를 사용하였을 경우 이러한 사건, 사고를 보호자, 관리자가 쉽게 알 수 있습니다. 뿐만 아니라 외국인의 경우에도 보다 쉽게 폭력 상황을 남에게 알려 범죄를 예방할 수 있습니다.

인식할 수 있는 행동 모션으로는 2023년 5월 기준 폭행, 심정지가 있습니다.

## ⚙️ 시스템 구성 및 아키텍처

<p align="center"><img src="https://github.com/BBiyongi/BBiyongi_app/assets/85453429/84ba710e-0ca6-4d3c-8d56-e68efc45a84c" width="450"/></p>

여기 설명 써야함

## 🖥️ 개발 환경 및 개발 언어

- 운영체제 : Windows, Ubuntu 20.04, Raspbian
- 디바이스 구성 : Raspberry pi
- IDE : Visual Studio Code, Android Studio
- 개발 언어 : Python 3.10.4, Java 18.0.2
- 데이터베이스 : Firebase Realtime Database, Firebase Cloud Storage
- 협업 툴 : Github, Notion, Google Meet

## 🖼 구현 화면

여기에 UI 화면 캡쳐해서 넣어야 함

## Git Commit message Convention

Commit message 는 제목, 본문, 꼬리말로 구성합니다. 제목은 필수사항이며, 본문과 꼬리말은 선택사항입니다.

```
<type>: <subject>
```

### Commit Type

- fix : 버그 수정
- docs : 문서(주석) 수정
- feat : 새로운 기능 추가
- test : 테스트 코드 추가
- chore : 패키지 매니저 수정, 빌드 업무 수정
- style : 코드 스타일, 포맷팅
- refactor : 기능의 변화가 없는 코드 리팩터링

### Commit Message Emoji

| 아이콘 | 설명                      | 원문                     |
| ------ | ------------------------- | ------------------------ | --------------------------------------- |
| 🎨     | art                       | 코드의 구조/형태 개선    | Improve structure / format of the code. |
| 🔥     | fire                      | 코드/파일 삭제           | Remove code or files.                   |
| 🐛     | bug                       | 버그 수정                | Fix a bug.                              |
| ✨     | sparkles                  | 새 기능                  | Introduce new features.                 |
| 📝     | memo                      | 문서 추가/수정           | Add or update documentation.            |
| 💄     | lipstick                  | UI/스타일 파일 추가/수정 | Add or update the UI and style files.   |
| ✅     | white_check_mark          | 테스트 추가/수정         | Add or update tests.                    |
| ♻️     | recycle                   | 코드 리팩토링            | Refactor code.                          |
| ➕     | heavy_plus_sign           | 의존성 추가              | Add a dependency.                       |
| 🔀     | twisted_rightwards_arrows | 브랜치 합병              | Merge branches.                         |
| 💡     | bulb                      | 주석 추가/수정           | Add or update comments in source code.  |
