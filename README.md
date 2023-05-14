# WordPro

WordPro는 특정 주기(피보나치 수열)를 가지고 사용자가 입력한 내용을 반복해서 내용에 익숙해 질 수 있도록 도우는 학습 어플리케이션 입니다.
기본적으로, 사용자는 모바일 어플리케이션을 통해 내용과 힌트를 입력하며 이는 시간을 기준으로 다음날 부터 메인 페이지에 내용을 노출하게 됩니다.
내용을 입력하는 편의를 제공하기 위해 [웹페이지](http://54.180.94.51/) 가 구성되어 있습니다. DNS와 AWS의 고정 주소를 사용하고 있지 않아서 웹페이지의 주소는 바뀔 수 있습니다.

AWS EC2를 사용하고 있으며 Docker를 통해서 MySQL, Apache, Node.js 이미지를 사용해 서버를 구성하였습니다.
모바일 어플리케이션과의 통신을 위해서 express를 통해 API를 구성하였습니다. Node.js Express페이지는 index.js과 login_index.js를 참고하시면 됩니다.
모바일 Push Message를 위해서는 FCM을 사용하였습니다.

모바일 엑티비티 페이지는 다음과 같습니다. 추가적으로 다른 웹페이지는 WordPro.xd를 참고하시면 됩니다.
<img width="300" alt="image" src="https://github.com/treeralph/WordPro/assets/50291395/1ce46cf5-db39-4937-87e0-ba3cd33021c2">

