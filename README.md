# 호수 수질 관리 시스템

호수 수질 관리 시스템은 IoT 센서, 수질 정화 로봇(Water Purifier Robot), CCTV 디바이스를 활용하여 호수의 수질을 실시간으로 모니터링하고 관리하는 애플리케이션입니다. Azure 클라우드 서비스를 기반으로 백엔드 API, 프론트엔드 웹 인터페이스, 그리고 이벤트 처리를 위한 Azure Functions가 연동됩니다.

## 1. 주요 기능

* **디바이스 관리:** 시스템에 등록된 센서, 수질 정화 로봇, CCTV 디바이스를 등록, 조회, 수정, 삭제합니다. (프론트엔드 - 디바이스 관리 탭)

* **디바이스 상태 조회:** 등록된 디바이스(센서, 로봇, CCTV)의 최신 상태 정보를 조회합니다. (프론트엔드 - 디바이스 상태 탭)

* **수질 정화 로봇 위치 추적:** 수질 정화 로봇의 실시간 위치를 지도(OpenStreetMap + Leaflet.js) 위에 표시하고, 10초마다 위치를 업데이트합니다. (프론트엔드 - 로봇 위치 추적 탭)

* **센서 데이터 차트:** 센서에서 수집된 온도, pH, 용존 산소, 탁도 데이터를 시간 흐름에 따라 차트로 시각화합니다. 범례를 통해 개별 센서 데이터를 켜고 끌 수 있으며, 하단 테이블도 연동됩니다. (프론트엔드 - 센서 데이터 차트 탭)

* **CCTV 이벤트 이미지:** CCTV에서 감지된 이벤트 이미지를 조회합니다. (프론트엔드 - CCTV 이미지 탭)

* **자동화된 데이터 수집:** 센서 및 로봇 데이터가 Azure Event Hub를 통해 실시간으로 수집되고, Azure Functions를 거쳐 백엔드 및 Redis에 저장됩니다.

## 2. 아키텍처 개요

이 프로젝트는 다음과 같은 주요 구성 요소로 이루어져 있습니다.

*시스템 아키텍처 다이어그램 (placeholder)*

* **프론트엔드 (Vue.js)**: 사용자 인터페이스를 제공하는 웹 애플리케이션.

* **백엔드 (Spring Boot)**: 프론트엔드 및 Azure Functions에 필요한 RESTful API를 제공하고, PostgreSQL DB와 상호작용.

* **Azure Event Hubs**: 대규모 이벤트 데이터를 실시간으로 수집하는 서비스.

* **Azure Functions (Python)**:

  * `azfa-sensor-data`: 센서 데이터를 Event Hub에서 읽어 Redis에 실시간 저장.

  * `azfa-waterbot-data`: 로봇 데이터를 Event Hub에서 읽어 Redis에 실시간 저장.

  * `azfa-cctv-event`: CCTV 이미지를 Azure Blob Storage에 저장하고, 메타데이터를 백엔드 API로 전송.

* **PostgreSQL Database**: 모든 디바이스 정보, 상태 데이터, 호수 상태, CCTV 이벤트 이미지 메타데이터를 저장.

* **Azure Cache for Redis**: 실시간 센서 및 로봇 데이터, 임시 데이터를 캐싱.

* **Azure Blob Storage**: CCTV 이미지와 같은 대용량 바이너리 데이터를 저장.

* **Python Sensor Simulators**: 센서 및 로봇 데이터를 Event Hub로 전송하는 시뮬레이터.

## 3. 프론트엔드 디렉토리 구조 요약

```
lake-monitoring-frontend/
├── node_modules/
├── public/
│   ├── images/
│   │   └── waterbot.png      # 로봇 마커 이미지 등 정적 이미지
│   └── index.html            # Vue 앱 진입점 HTML (CDN 링크 포함)
├── src/
│   ├── assets/
│   │   └── global.css        # 전역 스타일 (DaisyUI 호환)
│   ├── components/           # 재사용 가능한 UI 컴포넌트
│   │   └── HelloWorld.vue
│   ├── router/
│   │   └── index.js          # Vue Router 설정
│   ├── store/
│   │   └── index.js          # Vuex Store (현재는 기본 설정)
│   ├── views/                # 각 페이지(뷰) 컴포넌트
│   │   ├── CCTVImagesView.vue
│   │   ├── DeviceManagementView.vue
│   │   ├── DeviceStatusView.vue
│   │   ├── RobotLocationView.vue
│   │   └── SensorDataChartView.vue
│   ├── App.vue               # 최상위 컴포넌트 (레이아웃, 내비게이션, 전역 메시지 박스)
│   └── main.js               # Vue 앱 진입점 JS
├── .env                      # 환경 변수 (API_BASE_URL)
├── babel.config.js           # Babel 설정
├── package.json              # 프로젝트 메타데이터 및 의존성
├── prettier.config.js        # Prettier 코드 포매터 설정
├── README.md                 # 본 파일
└── vue.config.js             # Vue CLI 웹팩 설정 (프록시 등)
```

## 4. 환경 설정 및 실행 방법

### 4.1. 전제 조건

* **Java 17+**: Spring Boot 백엔드 실행을 위해 필요합니다.

* **Node.js 20+ & npm (또는 Yarn)**: Vue.js 프론트엔드 실행을 위해 필요합니다. (nvm 사용 권장)

* **PostgreSQL Database**: 데이터 저장을 위한 DB 서버가 필요합니다.

* **Maven 3.6+**: Spring Boot 빌드를 위해 필요합니다.

* **Python 3.8+ & pip**: Azure Functions 및 센서 시뮬레이터 실행을 위해 필요합니다.

* **Azure Subscription**: Event Hub, Blob Storage, App Service, Functions 등의 Azure 리소스를 프로비저닝하기 위해 필요합니다.

* **Visual Studio Code**: 개발 환경으로 권장됩니다.

    * VS Code Extension: `Java Extension Pack`, `Vue.js Extension Pack (Volar)`, `ESLint`, `Prettier - Code formatter`, `Azure Functions`, `Azure Account`.

### 4.2. PostgreSQL 데이터베이스 설정

1.  **데이터베이스 생성:** PostgreSQL 서버에 `lakemonitoring_db`와 같은 데이터베이스를 생성합니다.

2.  **테이블 생성 스크립트 실행:** 다음 스크립트를 사용하여 필요한 테이블들을 생성합니다.

    ```sql
    -- 1. devices table
    CREATE TABLE devices (
        device_id VARCHAR(255) PRIMARY KEY,
        device_name VARCHAR(255) NOT NULL,
        device_type VARCHAR(50) NOT NULL,
        owner VARCHAR(255),
        latitude NUMERIC(17,10),
        longitude NUMERIC(17,10),
        coord_system VARCHAR(50),
        status VARCHAR(50),
        description TEXT,
        created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        last_updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
    );

    -- 2. device_status table
    CREATE TABLE device_status (
        id BIGSERIAL PRIMARY KEY,
        device_id VARCHAR(255) NOT NULL,
        timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
        battery_level NUMERIC(5,2),
        current_status VARCHAR(50),
        filter_life_remaining NUMERIC(5,2),
        purified_volume_liters NUMERIC(10,2),
        latitude NUMERIC(17,10),
        longitude NUMERIC(17,10),
        error_code VARCHAR(100),
        xprops JSONB, -- 확장 속성 (JSONB 타입)
        FOREIGN KEY (device_id) REFERENCES devices(device_id) ON DELETE CASCADE
    );

    -- 3. lake_status table
    CREATE TABLE lake_status (
        id BIGSERIAL PRIMARY KEY,
        sensor_id VARCHAR(255) NOT NULL,
        ttimestamp TIMESTAMP WITH TIME ZONE NOT NULL, -- 'timestamp'와의 충돌을 피하기 위해 'ttimestamp' 사용
        temperature NUMERIC(5,2),
        ph NUMERIC(4,2),
        dissolved_oxygen NUMERIC(5,2),
        turbidity NUMERIC(5,2),
        pollution_level VARCHAR(50),
        recorded_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY (sensor_id) REFERENCES devices(device_id) ON DELETE CASCADE
    );

    -- 4. cctv_event_image table
    CREATE TABLE cctv_event_image (
        id BIGSERIAL PRIMARY KEY,
        device_id VARCHAR(255) NOT NULL,
        event_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
        blob_name VARCHAR(255) NOT NULL,
        blob_url TEXT NOT NULL,
        description TEXT,
        event_type VARCHAR(100),
        processed_status VARCHAR(50),
        captured_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY (device_id) REFERENCES devices(device_id) ON DELETE CASCADE
    );

    -- 인덱스 추가 (선택 사항, 성능 향상)
    CREATE INDEX idx_device_status_device_id_timestamp ON device_status (device_id, timestamp DESC);
    CREATE INDEX idx_lake_status_sensor_id_timestamp ON lake_status (sensor_id, ttimestamp DESC);
    CREATE INDEX idx_cctv_event_image_device_id_timestamp ON cctv_event_image (device_id, event_timestamp DESC);
    ```

### 4.3. 백엔드 (Spring Boot) 설정 및 실행

1.  **프로젝트 클론:**

    ```bash
    git clone your-backend-repo-url.git # 실제 백엔드 레포지토리 URL로 변경
    cd limo-be # 백엔드 프로젝트 디렉토리로 이동
    ```

2.  **`application.properties` 설정:**
    `src/main/resources/application.properties` 파일을 열어 데이터베이스, Azure Event Hubs, Azure Blob Storage 연결 정보를 설정합니다.

    * **PostgreSQL:**

        ```properties
        spring.datasource.url=jdbc:postgresql://<your_db_host>:5432/lakemonitoring_db
        spring.datasource.username=<your_db_user>
        spring.datasource.password=<your_db_password>
        spring.jpa.hibernate.ddl-auto=update # 개발 환경에서는 'update' 사용, 운영에서는 'none' 또는 'validate'
        ```

    * **Azure Event Hubs (Consumer):**

        ```properties
        azure.eventhubs.connection-string=Endpoint=sb://<your-eventhub-namespace>.servicebus.windows.net/;SharedAccessKeyName=<SAS_KEY_NAME>;SharedAccessKey=<SAS_KEY>;EntityPath=<EVENT_HUB_NAME>
        azure.eventhubs.name=<EVENT_HUB_NAME>
        azure.eventhubs.consumer-group=$Default # 또는 커스텀 컨슈머 그룹 이름
        ```

    * **Azure Blob Storage (Checkpoint Store 및 CCTV 이미지):**

        ```properties
        # 체크포인트 저장소용 (Event Hub Consumer)
        azure.storage.blob.account-name=<your_storage_account_name>
        azure.storage.blob.sas-token=<your_sas_token_for_checkpoint_container> # 체크포인트 컨테이너 (eventhub-checkpoints)에 대한 SAS 토큰
        azure.storage.blob.container-name=eventhub-checkpoints # 체크포인트 저장용 컨테이너 이름 (Azure Portal에서 미리 생성)

        # CCTV 이미지 저장용 (CCTV API)
        cctv.blob.account-name=<your_storage_account_name>
        cctv.blob.sas-token=<your_sas_token_for_cctv_images_container> # cctv-images 컨테이너에 대한 SAS 토큰
        cctv.blob.container-name=cctv-images # CCTV 이미지 저장용 컨테이너 이름 (Azure Portal에서 미리 생성)
        ```

        **참고:** `your_sas_token_for_checkpoint_container` 및 `your_sas_token_for_cctv_images_container`는 해당 컨테이너에 `읽기(Read)`, `쓰기(Write)`, `목록(List)` 권한이 부여된 SAS 토큰이어야 합니다.

    * **CORS 설정:**

        ```properties
        spring.web.cors.allowed-origins=http://localhost:8081,http://localhost:8080,https://<your-frontend-appservice-name>.azurewebsites.net
        spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
        spring.web.cors.allowed-headers=*
        spring.web.cors.allow-credentials=true
        ```

3.  **Maven 의존성 설치 및 빌드:**
    프로젝트 루트 디렉토리에서 Maven 의존성을 설치하고 애플리케이션을 빌드합니다.

    ```bash
    mvn clean install -Dmaven.test.skip=true # 테스트 건너뛰고 빌드
    ```

4.  **애플리케이션 실행:**

    ```bash
    java -jar target/limo-be-0.0.1-SNAPSHOT.jar # 생성된 JAR 파일명으로 변경
    ```

    또는 VS Code에서 디버거를 통해 실행 시 `launch.json` 설정:

    ```json
    {
        "configurations": [
            {
                "type": "java",
                "name": "Launch Spring Boot App",
                "request": "launch",
                "cwd": "${workspaceFolder}",
                "mainClass": "com.handson.gtmtech.limo.be.LakeMonitoringApiApplication",
                "projectName": "limo-be",
                "args": "",
                "vmArgs": "-Dspring.profiles.active=dev" # 개발 프로파일 활성화 (선택 사항)
            }
        ]
    }
    ```

### 4.4. 프론트엔드 (Vue.js) 설정 및 실행

1.  **프로젝트 클론:**

    ```bash
    git clone your-frontend-repo-url.git # 실제 프론트엔드 레포지토리 URL로 변경
    cd limo-fe # 프론트엔드 프로젝트 디렉토리로 이동
    ```

2.  **의존성 설치:**

    ```bash
    npm install # 또는 yarn install
    ```

    (Chart.js 어댑터가 설치되어 있지 않다면 `npm install chartjs-adapter-date-fns date-fns` 추가 설치)

3.  **`.env` 파일 설정:**
    프로젝트 루트에 `.env` 파일을 생성하고 백엔드 API URL을 설정합니다.

    ```dotenv
    VUE_APP_API_BASE_URL=http://localhost:8080/api # 백엔드 API의 실제 URL
    ```

    **참고:** Google Maps API 키는 Leaflet.js로 대체되어 더 이상 필요하지 않습니다.

4.  **`public/index.html` 확인:**
    `public/index.html` 파일에 다음 CDN 링크들이 포함되어 있는지 확인합니다.

    * Tailwind CSS (`https://cdn.tailwindcss.com`)

    * DaisyUI (`https://cdn.jsdelivr.net/npm/daisyui@latest/dist/full.css`)

    * Leaflet.js CSS 및 JS (`https://unpkg.com/leaflet@1.9.4/dist/leaflet.css`, `https://unpkg.com/leaflet@1.9.4/dist/leaflet.js`)

    * Axios (`https://unpkg.com/axios/dist/axios.min.js`)

    * Chart.js (`https://cdn.jsdelivr.net/npm/chart.js`)

    * Font Awesome (`https://cdnjs.cloudflare.com/ajax/libs/font-awesome/.../all.min.css`)

5.  **애플리케이션 실행:**

    ```bash
    npm run serve
    ```

    브라우저에서 `http://localhost:8081` (Vue CLI 개발 서버 기본 포트)로 접속하여 애플리케이션을 확인합니다.

### 4.5. Azure Functions (Python) 및 센서 시뮬레이터

이 부분은 Azure 환경에서 Event Hub, Blob Storage, Functions가 배포된 후에 작동합니다.

1.  **Azure Functions 배포:**
    각 Azure Function (`azfa-sensor-data`, `azfa-waterbot-data`, `azfa-cctv-event`)을 Azure Functions로 배포하고, `local.settings.json`에 명시된 환경 변수들을 Azure Functions App의 "Configuration"에 설정합니다.

2.  **센서 시뮬레이터 실행:**
    `water-quality-sensor-sim.py` 파일을 실행하기 전에, 프로젝트 루트에 `.env` 파일을 생성하고 Event Hub 연결 정보를 설정합니다.

    ```dotenv
    EVENTHUB_CONNECTION_STRING=Endpoint=sb://<your-eventhub-namespace>.servicebus.windows.net/;SharedAccessKeyName=<SAS_KEY_NAME>;SharedAccessKey=<SAS_KEY>;EntityPath=<EVENT_HUB_NAME>
    EVENT_HUB_CONSUMER_GROUP_NAME=$Default # 시뮬레이터는 생산자이므로 컨슈머 그룹은 크게 중요하지 않지만, 기본값으로 설정
    ```

    시뮬레이터 실행:

    ```bash
    python water-quality-sensor-sim.py
    ```

## 5. 배포 (Azure App Service)

* **백엔드 배포:** Spring Boot JAR 파일을 Azure App Service(Linux)에 배포합니다. GitHub Actions (`main_limoapi.yml`)를 사용하여 CI/CD 파이프라인을 구축할 수 있습니다.

* **프론트엔드 배포:** Vue.js 빌드 산출물( `npm run build` 후 생성되는 `dist` 폴더 내용)을 Azure App Service(Linux)에 배포합니다. GitHub Actions 또는 Azure CLI를 사용할 수 있습니다. 프론트엔드 App Service의 "구성" > "경로 매핑"에서 정적 파일을 제공하도록 설정해야 할 수 있습니다.

## 6. 문제 해결 및 참고 사항

* **CORS 오류:** 프론트엔드 개발 서버(Vue CLI)의 프록시 설정 (`vue.config.js`) 또는 백엔드(Spring Boot)의 CORS 설정 (`WebConfig.java`)이 올바른지 확인합니다.

* **API_BASE_URL:** 프론트엔드의 `.env` 파일에 설정된 `VUE_APP_API_BASE_URL`이 백엔드 API의 실제 URL과 일치하는지 확인합니다.

* **버전 충돌:** `npm install` 또는 `mvn install` 시 발생하는 버전 충돌 오류는 `package.json` 또는 `pom.xml`의 의존성 버전을 조정하여 해결해야 합니다.

* **Azure 리소스 생성:** 모든 Azure 리소스(Event Hub, Storage Account, App Service, Functions)가 올바르게 프로비저닝되었고 필요한 네트워크 설정(방화벽, 가상 네트워크)이 완료되었는지 확인합니다.

* **SAS 토큰 권한:** SAS 토큰에 필요한 `읽기`, `쓰기`, `목록` 권한이 올바르게 부여되었는지 확인합니다.

이 `README.md` 문서를 통해 프로젝트를 쉽게 이해하고 설정하며 실행할 수 있기를 바랍니다.
