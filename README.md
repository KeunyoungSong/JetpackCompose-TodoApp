# Todo App with Jetpack Compose

이 프로젝트는 Jetpack Compose를 활용하여 기본적인 Todo 앱을 구현합니다.

### ComposeBom 버전
- 2023.08.00

## 이슈
학습을 위해 할 일 목록을 view model 에서 리스트를 가진 LiveData 타입으로 관리하려고 시도하였으나 LiveData 의 목록 전체 변경에도 recomposition 이 트리거되지 않음.
다른 방식을 찾아볼 계획

## 주요 기능
- **추가**: 새로운 할 일을 목록에 추가
- **삭제**: 기존의 할 일을 목록에서 삭제
- **수정**: 목록에 있는 할 일의 내용을 수정
- **완료 체크**: 할 일의 완료 상태를 체크하거나 해제

## 미리보기

<img src= "https://github.com/KeunyoungSong/Basic-TodoApp/assets/84883277/8d01813e-ce44-41d8-addd-bc90f1cfb596" height="470"/>
