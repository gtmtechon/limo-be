-- devices 테이블: IoT 센서 및 수질 정화 로봇 디바이스 정보 저장
CREATE TABLE IF NOT EXISTS devices (
    device_id VARCHAR(255) PRIMARY KEY, -- 디바이스 고유 ID (예: sensor-1, water-purifier-robot-01)
    device_name VARCHAR(255) NOT NULL, -- 디바이스 이름
    device_type VARCHAR(50) NOT NULL, -- 디바이스 타입 (예: 'sensor', 'water-purifier-robot', 'cctv')
    owner VARCHAR(255), -- 디바이스 소유자 또는 관리자
    latitude NUMERIC(10, 8), -- 디바이스 위치 위도
    longitude NUMERIC(11, 8), -- 디바이스 위치 경도
    coord_system VARCHAR(50) DEFAULT 'WGS84', -- 좌표계 (기본값: WGS84)
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, -- 디바이스 등록 시간
    status VARCHAR(50) NOT NULL DEFAULT 'active', -- 디바이스 현재 상태 (예: 'active', 'inactive', 'maintenance')
    description TEXT, -- 디바이스에 대한 추가 설명
    last_updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP -- 마지막 업데이트 시간
);

-- lake_status 테이블: 호수의 가장 최근 상태 정보 저장 (센서 데이터)
-- 센서 메시지 규격: timestamp, sensor_id, temperature, ph, dissolved_oxygen, turbidity
CREATE TABLE IF NOT EXISTS lake_status (
    id SERIAL PRIMARY KEY, -- 고유 ID
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL, -- 센서 데이터 측정 시간
    sensor_id VARCHAR(255) NOT NULL, -- 센서 ID
    temperature NUMERIC(5, 2), -- 온도
    ph NUMERIC(4, 2), -- 산도 (pH)
    dissolved_oxygen NUMERIC(5, 2), -- 용존 산소량
    turbidity NUMERIC(6, 2), -- 탁도
    pollution_level VARCHAR(50), -- 오염도 (예: 'good', 'moderate', 'poor' - 애플리케이션 로직에서 결정)
    recorded_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, -- 데이터베이스에 기록된 시간
    FOREIGN KEY (sensor_id) REFERENCES devices(device_id) ON DELETE CASCADE
);

-- device_status 테이블: 장치(센서, 로봇)의 가장 최근 상태 정보 저장
-- 로봇 메시지 규격: timestamp, device_id, battery_level, current_status, last_command_id, purification_status, location, error_code
CREATE TABLE IF NOT EXISTS device_status (
    id SERIAL PRIMARY KEY, -- 고유 ID
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL, -- 상태 정보 측정 시간
    device_id VARCHAR(255) NOT NULL, -- 장치 ID (센서 또는 로봇)
    battery_level INTEGER, -- 배터리 잔량 (%)
    current_status VARCHAR(50) NOT NULL, -- 현재 상태 (예: 'moving', 'purifying', 'charging', 'idle', 'error')
    last_command_id VARCHAR(255), -- 로봇이 마지막으로 받은 명령 ID
    filter_life_remaining INTEGER, -- 필터 수명 잔량 (%)
    purified_volume_liters NUMERIC(10, 2), -- 총 정화된 양 (리터)
    latitude NUMERIC(10, 8), -- 장치 현재 위치 위도
    longitude NUMERIC(11, 8), -- 장치 현재 위치 경도
    error_code VARCHAR(255), -- 에러 발생 시 코드
    recorded_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, -- 데이터베이스에 기록된 시간
    FOREIGN KEY (device_id) REFERENCES devices(device_id) ON DELETE CASCADE
);

-- cctv_event_image 테이블: 호수 감시카메라 영상 중 이벤트 화면 관리 테이블
-- 실제 이미지는 Azure Blob Storage에 저장되고, 테이블에는 메타데이터만 관리
CREATE TABLE IF NOT EXISTS cctv_event_image (
    id SERIAL PRIMARY KEY, -- 고유 ID
    event_timestamp TIMESTAMP WITH TIME ZONE NOT NULL, -- 이벤트 발생 시간
    device_id VARCHAR(255), -- CCTV 디바이스 ID (선택 사항, devices 테이블에 CCTV도 등록될 경우)
    blob_name VARCHAR(255) NOT NULL, -- Azure Blob Storage에 저장된 이미지 파일명
    blob_url TEXT NOT NULL, -- Azure Blob Storage에 저장된 이미지의 공개 URL
    description TEXT, -- 이벤트에 대한 설명 (예: '수상한 활동 감지', '야생 동물 포착')
    event_type VARCHAR(100), -- 이벤트 유형 (예: 'unusual_activity', 'wildlife', 'debris_detection')
    processed_status VARCHAR(50) DEFAULT 'new', -- 처리 상태 (예: 'new', 'reviewed', 'archived')
    captured_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, -- 데이터베이스에 기록된 시간
    FOREIGN KEY (device_id) REFERENCES devices(device_id) ON DELETE SET NULL
);
