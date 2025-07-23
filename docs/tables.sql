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
    xprops  VARCHAR(1024), -- 확장속성
    FOREIGN KEY (device_id) REFERENCES devices(device_id) ON DELETE CASCADE
);

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


select * from devices;

select * from device_status;
INSERT INTO public.device_status
(id, ttimestamp, device_id, battery_level, current_status, last_command_id, filter_life_remaining, purified_volume_liters, latitude, longitude, error_code, recorded_at)
VALUES(1, CURRENT_TIMESTAMP, 'U1FMyvIKaf7q4Wef', 0, 'registered', '', 0, 0, 37.5120450000, 127.1061390000, '', CURRENT_TIMESTAMP);



*** 초기 데이터 삽입 예시 ***
INSERT INTO public.device_status (ttimestamp,device_id,battery_level,current_status,last_command_id,filter_life_remaining,purified_volume_liters,latitude,longitude,error_code,recorded_at,xprops) VALUES
	 ('2025-07-23 15:35:55.114488+09','bJ88NeQisVCI7QcG',NULL,'registered',NULL,NULL,NULL,NULL,NULL,NULL,'2025-07-23 15:35:55.114509+09',NULL),
	 ('2025-07-23 15:52:28.10963+09','PPZof7g2BwfipKVU',NULL,'registered',NULL,NULL,NULL,NULL,NULL,NULL,'2025-07-23 15:52:28.109643+09',NULL),
	 ('2025-07-23 15:52:56.209592+09','oltD2DuyGZONFf2k',NULL,'registered',NULL,NULL,NULL,NULL,NULL,NULL,'2025-07-23 15:52:56.209605+09',NULL),
	 ('2025-07-23 15:55:34.09855+09','xWjzBRdluhWw4Vj8',NULL,'registered',NULL,NULL,NULL,NULL,NULL,NULL,'2025-07-23 15:55:34.09856+09',NULL),
	 ('2025-07-23 15:56:15.796973+09','QUDAFUIEMGX8U3NY',NULL,'registered',NULL,NULL,NULL,NULL,NULL,NULL,'2025-07-23 15:56:15.796983+09',NULL),
	 ('2025-07-23 15:56:49.564933+09','XEuLO75SS3nElEy6',NULL,'registered',NULL,NULL,NULL,NULL,NULL,NULL,'2025-07-23 15:56:49.564944+09',NULL),
	 ('2025-07-23 15:57:37.667105+09','C7cZmyGfTt6UkWKZ',NULL,'registered',NULL,NULL,NULL,NULL,NULL,NULL,'2025-07-23 15:57:37.667115+09',NULL),
	 ('2025-07-23 16:49:45.736627+09','U1FMyvIKaf7q4Wef',0,'registered','',0,0.00,37.51204500,127.10613900,'','2025-07-23 16:49:45.736627+09',NULL);


INSERT INTO public.devices (device_id,device_name,device_type,"owner",latitude,longitude,coord_system,created_at,status,description,last_updated_at) VALUES
	 ('U1FMyvIKaf7q4Wef','water-purifier-robot-01','water-purifier-robot','admin',37.5120450000,127.1061390000,NULL,'2025-07-23 14:14:16.6259+09','active','https://maps.app.goo.gl/eNVrfX9KuaVXmLcL6','2025-07-23 14:47:53.951541+09'),
	 ('bJ88NeQisVCI7QcG','water-purifier-robot-02','water-purifier-robot','admin',37.5091830000,127.1015390000,'WGS84','2025-07-23 15:35:55.095033+09','maintenance','https://maps.app.goo.gl/is6Fb7Wc9ENxhYd16','2025-07-23 15:35:55.095053+09'),
	 ('PPZof7g2BwfipKVU','sensor-01','sensor','admin',37.5125400000,127.1064290000,'WGS84','2025-07-23 15:52:28.093626+09','active','https://maps.app.goo.gl/Sh5AFuSyFGRZNSZ47','2025-07-23 15:54:31.603758+09'),
	 ('oltD2DuyGZONFf2k','sensor-02','sensor','admin',37.5115500000,127.1053100000,'WGS84','2025-07-23 15:52:56.19207+09','active','https://maps.app.goo.gl/SWCYd3kcYTce4Kte6','2025-07-23 15:54:56.107665+09'),
	 ('QUDAFUIEMGX8U3NY','sensor-04','sensor','admin',37.5101680000,127.1043560000,'WGS84','2025-07-23 15:56:15.778397+09','active','https://maps.app.goo.gl/PeMSS1a9qzFxtfbe6','2025-07-23 15:56:15.778407+09'),
	 ('XEuLO75SS3nElEy6','sensor-05','sensor','admin',37.5117600000,127.1064880000,'WGS84','2025-07-23 15:56:49.549444+09','active','https://maps.app.goo.gl/NGLYqEuLGrhznxF16','2025-07-23 15:56:49.549454+09'),
	 ('xWjzBRdluhWw4Vj8','sensor-03','sensor','admin',37.5110180000,127.1037430000,'WGS84','2025-07-23 15:55:34.083499+09','active','https://maps.app.goo.gl/HmirRvUMRYyLQfqs9','2025-07-23 15:57:03.405025+09'),
	 ('C7cZmyGfTt6UkWKZ','cctv-01','cctv','admin',37.5124030000,127.1073920000,'WGS84','2025-07-23 15:57:37.642643+09','active','https://maps.app.goo.gl/jdZJ9DohPx7bmUvo7','2025-07-23 15:57:37.642652+09');
